package io.joynr.generator.util;

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

import io.joynr.generator.IJoynrGenerator;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.reflections.Reflections;

public class InvocationArguments {
    private static Logger logger = Logger.getLogger(InvocationArguments.class);

    public static final String OUTPUT_PATH = "outputPath";

    // A lookup of language to root generator
    protected static Map<String, String> languages = new HashMap<>();
    static {
        Reflections reflections = new Reflections("io", "com", "de", "org");
        Set<Class<? extends IJoynrGenerator>> generators = reflections.getSubTypesOf(IJoynrGenerator.class);
        for (Class<? extends IJoynrGenerator> generator : generators) {
            try {
                IJoynrGenerator instance = generator.newInstance();
                languages.put(instance.getLanguageId(), generator.getName());
            } catch (Exception e) {
                logger.error("unable to load language generator:" + generator.getName(), e);
            }
        }
    }

    private String modelPath = null;

    private String rootGenerator = null;

    private String outputPath = null;

    private String generationId = null;

    private Map<String, String> parameter;

    private boolean generate = true;

    private boolean clean = false;

    public InvocationArguments() {
        // allows setting args programmatically
    }

    public InvocationArguments(String[] args) {
        if (args.length == 0) {
            throw new IllegalArgumentException("No parameters provided!" + dumpCorrectInvocation());
        }
        parseArguments(args);
        checkArguments(true);
    }

    private static String getLanguages(String seperator) {
        assert seperator != null;
        StringBuilder appender = new StringBuilder();
        for (String language : languages.keySet()) {
            appender.append(language + seperator);
        }
        if (!languages.isEmpty()) {
            appender.delete(appender.length() - seperator.length(), appender.length());
        } else {
            appender.append("none");
        }
        return appender.toString();
    }

    private String dumpCorrectInvocation() {
        String result = "\n";
        result += ("------------------------------------------------------------------------\n");
        result += ("Generator could not be started due to wrong parameter settings!\n");
        result += ("------------------------------------------------------------------------\n");
        result += usageString();
        return result;
    }

    public static String usageString() {
        StringBuilder usageString = new StringBuilder();
        usageString.append("Start the application with the following parameters: \n");
        usageString.append("      Required: \n");
        usageString.append("       " + dumpModelPathDefinition() + "\n");
        usageString.append("       " + dumpOutputPathDefinition() + "\n");
        usageString.append("      One of:\n");
        usageString.append("       " + dumpRootGeneratorDefinition() + " OR\n");
        usageString.append("       " + dumpGenerationLanguageDefinition() + "\n");
        usageString.append("      Optional: \n");
        usageString.append("       -templatesDir <folder name of templates directory>\n");
        usageString.append("       -templatesEncoding <encoding of templates>\n");
        usageString.append("       -generationId <name of what is being generated>\n");
        usageString.append("      Optional, C++ only: \n");
        usageString.append("       -outputHeaderPath <path to directory containing header files>\n");
        usageString.append("       -includePrefix <prefix to use in include statements>\n");
        return usageString.toString();
    }

    private static String dumpOutputPathDefinition() {
        return "-outputPath <path to output directory>";
    }

    private static String dumpModelPathDefinition() {
        return "-modelPath <path to model>";
    }

    private static String dumpRootGeneratorDefinition() {
        return "-rootGenerator <full name of template root>";
    }

    private static String dumpGenerationLanguageDefinition() {
        return "-generationLanguage <" + getLanguages("|") + ">";
    }

    public void parseArguments(String[] args) {
        for (int i = 0; i < args.length; i++) {
            if (args[i].equalsIgnoreCase("-clean")) {
                setClean(true);
            } else if (args[i].equalsIgnoreCase("-modelPath")) {
                setModelPath(args[i + 1]);
                i++;
            } else if (args[i].equalsIgnoreCase("-generate")) {
                setGenerate(args[i + 1].equalsIgnoreCase("true"));
                i++;
            } else if (args[i].equalsIgnoreCase("-generationId")) {
                setGenerationId(args[i + 1].replace("\"", ""));
                i++;
            } else if (args[i].equalsIgnoreCase("-generationLanguage")) {
                setGenerationLanguage(args[i + 1].replace("\"", ""));
                i++;
            } else if (args[i].equalsIgnoreCase("-jee")) {
                setParameterElement("jee", args[i + 1].replace("\"", ""));
                i++;
            } else if (args[i].equalsIgnoreCase("-outputHeaderPath")) {
                setParameterElement("outputHeaderPath", args[i + 1].replace("\"", ""));
                i++;
            } else if (args[i].equalsIgnoreCase("-outputPath")) {
                setOutputPath(new File(args[i + 1]).getAbsolutePath());
                i++;
            } else if (args[i].equalsIgnoreCase("-rootGenerator")) {
                setRootGenerator(args[i + 1].replace("\"", ""));
                i++;
            }
        }
    }

    public void setClean(boolean clean) {
        this.clean = clean;
    }

    public void setGenerate(boolean generate) {
        this.generate = generate;
    }

    private void setParameterElement(String key, String value) {
        if (parameter == null) {
            parameter = new HashMap<>();
        }

        parameter.put(key, value);

    }

    public boolean generate() {
        return generate;
    }

    public boolean clean() {
        return clean;
    }

    public void checkArguments() {
        checkArguments(false);
    }

    public void checkArguments(boolean checkIfModelPathIsValid) {
        StringBuilder errorMessages = new StringBuilder();
        String newLine = System.getProperty("line.separator");
        if (outputPath == null) {
            errorMessages.append("- Output path is missing. Please invoke the generator with the following argument: "
                    + dumpOutputPathDefinition());
            errorMessages.append(newLine);
        }
        if (modelPath == null) {
            errorMessages.append("- Model path is missing. Please invoke the generator with the following argument: "
                    + dumpModelPathDefinition());
            errorMessages.append(newLine);
        } else if (checkIfModelPathIsValid && !new File(modelPath).exists()) {
            errorMessages.append("- Path to model \"" + modelPath + "\" is not correct. File could not be found");
            errorMessages.append(newLine);
        }
        if (rootGenerator == null) {
            errorMessages.append("- Root generator could not be found. Please invoke the generator with the following argument: "
                    + dumpRootGeneratorDefinition() + " OR " + dumpGenerationLanguageDefinition());
            errorMessages.append(newLine);
        }

        if (errorMessages.length() > 0) {
            throw new IllegalArgumentException("Invocation arguments are not set properly. See the following error messages for further details "
                    + newLine + errorMessages.toString() + dumpCorrectInvocation());
        }
    }

    public String getModelPath() {
        return modelPath;
    }

    public void setModelPath(String modelPath) {
        this.modelPath = modelPath;
    }

    public String getRootGenerator() {
        return rootGenerator;
    }

    public void setRootGenerator(String rootGenerator) {
        this.rootGenerator = rootGenerator;
    }

    public void setGenerationLanguage(String generationLanguage) {
        if (rootGenerator == null && generationLanguage != null) {
            rootGenerator = languages.get(generationLanguage);
            if (rootGenerator == null) {
                throw new IllegalArgumentException("The generation language \""
                        + generationLanguage
                        + "\" could not be found in the configuration. The following languages have been found: "
                        + getLanguages(", ")
                        + ". Be sure to have the respective generation templates included in your dependencies. The package of generator templates shall start with \"io\", \"com\", \"org\" or \"de\""
                        + dumpCorrectInvocation());
            }
        }
    }

    public String getOutputPath() {
        return outputPath;
    }

    public void setOutputPath(String outputPath) {
        this.outputPath = outputPath;
    }

    public Map<String, String> getParameter() {
        if (parameter == null) {
            parameter = new HashMap<>();
        }
        if (!parameter.containsKey(OUTPUT_PATH)) {
            parameter.put(OUTPUT_PATH, getOutputPath());
        }
        return parameter;
    }

    public void setParameter(Map<String, String> parameter) {
        this.parameter = parameter;
    }

    public String getGenerationId() {
        return generationId;
    }

    public void setGenerationId(String generationId) {
        this.generationId = generationId;
    }

}
