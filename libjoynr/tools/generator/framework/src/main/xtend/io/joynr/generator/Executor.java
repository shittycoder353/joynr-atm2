package io.joynr.generator;

/*
 * #%L
 * %%
 * Copyright (C) 2011 - 2017 BMW Car IT GmbH
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import static io.joynr.generator.util.FileSystemAccessUtil.createFileSystemAccess;
import io.joynr.generator.loading.ModelLoader;
import io.joynr.generator.templates.util.JoynrGeneratorExtensions;
import io.joynr.generator.util.InvocationArguments;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.Resource.Diagnostic;
import org.eclipse.xtext.generator.IFileSystemAccess;
import org.eclipse.xtext.generator.IGenerator;
import org.eclipse.xtext.generator.JavaIoFileSystemAccess;
import org.franca.core.dsl.FrancaIDLStandaloneSetup;

import com.google.inject.AbstractModule;
import com.google.inject.Injector;
import com.google.inject.name.Names;

public class Executor {

    private final Logger logger = Logger.getLogger("io.joynr.generator.Executor");
    private final InvocationArguments arguments;
    private final IGenerator generator;
    private final IFileSystemAccess outputFileSystem;

    public Executor(final InvocationArguments arguments) throws ClassNotFoundException,
                                                        InstantiationException,
                                                        IllegalAccessException {
        arguments.checkArguments();
        this.arguments = arguments;

        // Get an injector and inject into the current instance
        Injector francaInjector = new FrancaIDLStandaloneSetup().createInjectorAndDoEMFRegistration();

        // Use a child injector that contains configuration parameters passed to this Executor
        Injector injector = francaInjector.createChildInjector(new AbstractModule() {
            @Override
            protected void configure() {
                bind(IFileSystemAccess.class).to(JavaIoFileSystemAccess.class);
                String generationId = arguments.getGenerationId();
                if (generationId != null) {
                    bindConstant().annotatedWith(Names.named("generationId")).to(generationId);
                } else {
                    // Guice does not allow null binding - use an empty string to show there is no generationId
                    bindConstant().annotatedWith(Names.named("generationId")).to("");
                }
                bind(Boolean.class).annotatedWith(Names.named(JoynrGeneratorExtensions.JOYNR_GENERATOR_GENERATE))
                                   .toInstance(arguments.generate());
                bind(Boolean.class).annotatedWith(Names.named(JoynrGeneratorExtensions.JOYNR_GENERATOR_CLEAN))
                                   .toInstance(arguments.clean());
            }
        });
        this.outputFileSystem = injector.getInstance(IFileSystemAccess.class);
        this.generator = createGenerator(injector);
    }

    private IGenerator createGenerator(Injector injector) throws ClassNotFoundException, InstantiationException,
                                                         IllegalAccessException {
        String rootGenerator = arguments.getRootGenerator();
        Class<?> rootGeneratorClass = Class.forName(rootGenerator, true, Thread.currentThread().getContextClassLoader());
        Object templateRootInstance = rootGeneratorClass.newInstance();

        if (templateRootInstance instanceof IGenerator) {
            // This is a standard generator
            IGenerator generator = (IGenerator) templateRootInstance;
            if (generator instanceof IJoynrGenerator) {
                IJoynrGenerator joynrGenerator = (IJoynrGenerator) generator;
                if (joynrGenerator.getGeneratorModule() != null) {
                    injector = injector.createChildInjector(joynrGenerator.getGeneratorModule());
                }
                injector.injectMembers(generator);
                joynrGenerator.setParameters(arguments.getParameter());
            } else {
                injector.injectMembers(generator);
            }
            return generator;
        } else {
            throw new IllegalStateException("Root generator \"" + "\" is not implementing interface \""
                    + IGenerator.class.getName() + "\"");
        }

    }

    private ModelLoader prepareGeneratorEnvironment() {
        String outputPath = arguments.getOutputPath();
        String modelPath = arguments.getModelPath();

        createFileSystemAccess(outputFileSystem, outputPath);

        return new ModelLoader(modelPath);
    }

    public IGenerator getGenerator() {
        return generator;
    }

    public void generate() {
        ModelLoader modelLoader = prepareGeneratorEnvironment();
        for (Resource resource : modelLoader.getResources()) {
            if (resource.getErrors().size() > 0) {
                StringBuilder errorMsg = new StringBuilder();
                errorMsg.append("Error loading model " + resource.getURI().toString()
                        + ". The following errors occured: \n");
                for (Diagnostic error : resource.getErrors()) {
                    errorMsg.append(error.getMessage());
                }
                logger.log(Level.SEVERE, errorMsg.toString());
                System.exit(-1);
            } else {
                generator.doGenerate(resource, outputFileSystem);
            }
        }
    }

    public static void main(String[] args) throws ClassNotFoundException, InstantiationException,
                                          IllegalAccessException {
        if (args.length == 1 && (args[0].equals("-help") || args[0].equals("-?"))) {
            System.out.println(InvocationArguments.usageString());
            System.exit(0);
        }
        // Parse the command line arguments
        InvocationArguments invocationArguments = new InvocationArguments(args);
        Executor executor = new Executor(invocationArguments);
        executor.generate();
    }
}
