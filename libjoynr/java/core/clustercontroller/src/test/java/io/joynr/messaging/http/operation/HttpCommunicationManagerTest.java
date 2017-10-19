package io.joynr.messaging.http.operation;

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

import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Singleton;
import com.google.inject.TypeLiteral;
import com.google.inject.multibindings.Multibinder;

import io.joynr.common.ExpiryDate;
import io.joynr.common.JoynrPropertiesModule;
import io.joynr.dispatching.Dispatcher;
import io.joynr.messaging.AtmosphereMessagingModule;
import io.joynr.messaging.JoynrMessageProcessor;
import io.joynr.messaging.MessagingPropertyKeys;
import io.joynr.messaging.MessagingTestModule;
import io.joynr.messaging.ReceiverStatusListener;
import io.joynr.messaging.routing.MessageRouter;

import java.util.Properties;
import java.util.UUID;

import joynr.Message;
import joynr.MutableMessage;

import org.apache.http.client.config.RequestConfig;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.webapp.WebAppContext;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.google.inject.Injector;
import com.jayway.restassured.RestAssured;
import com.jayway.restassured.config.HttpClientConfig;
import com.jayway.restassured.config.RestAssuredConfig;
import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.specification.RequestSpecification;

/**
 * HttpCommunicationManager tested by sending a request to the local host and checking the received message.
 *
 */

@RunWith(MockitoJUnitRunner.class)
public class HttpCommunicationManagerTest {

    LongPollingMessageReceiver longpollingMessageReceiver;
    private String testChannelId = "HttpCommunicationManagerTest-" + UUID.randomUUID().toString();

    private static Server server;
    private static int port;

    @BeforeClass
    public static void startBounceProxy() throws Exception {

        WebAppContext bounceproxyWebapp = new WebAppContext();
        bounceproxyWebapp.setContextPath("/bounceproxy");
        bounceproxyWebapp.setWar("target/bounceproxy.war");

        server = new Server(0);
        server.setHandler(bounceproxyWebapp);
        server.start();

        port = ((ServerConnector) server.getConnectors()[0]).getLocalPort();
    }

    @AfterClass
    public static void stopBounceProxy() throws Exception {
        try {
            server.stop();
        } catch (Exception e) {
            // do nothing as we don't want tests to fail only because
            // stopping of the server did not work
        }
        ;
    }

    @Mock
    private Dispatcher dispatcher;
    @Mock
    private MessageRouter mockMessageRouter;
    private String bounceProxyUrlString;

    @Before
    @edu.umd.cs.findbugs.annotations.SuppressWarnings(value = "ST_WRITE_TO_STATIC_FROM_INSTANCE_METHOD",
                                                      justification = "correct use of RestAssured API")
    public void setUp() throws Exception {

        RestAssured.port = port;
        String basePath = "/bounceproxy/";
        RestAssured.basePath = basePath;

        bounceProxyUrlString = "http://localhost:" + port + basePath;

        Properties properties = new Properties();
        properties.put(MessagingPropertyKeys.CHANNELID, testChannelId);
        properties.put(MessagingPropertyKeys.BOUNCE_PROXY_URL, bounceProxyUrlString);

        Injector injector = Guice.createInjector(new JoynrPropertiesModule(properties),
                                                 new MessagingTestModule(),
                                                 new AtmosphereMessagingModule(),
                                                 new AbstractModule() {
                                                     @Override
                                                     protected void configure() {
                                                         bind(RequestConfig.class).toProvider(HttpDefaultRequestConfigProvider.class)
                                                                                  .in(Singleton.class);
                                                         bind(MessageRouter.class).toInstance(mockMessageRouter);
                                                         Multibinder.newSetBinder(binder(),
                                                                                  new TypeLiteral<JoynrMessageProcessor>() {
                                                                                  });
                                                     }
                                                 });

        longpollingMessageReceiver = injector.getInstance(LongPollingMessageReceiver.class);
    }

    @After
    public void tearDown() throws Exception {
        longpollingMessageReceiver.shutdown(true);
    }

    @Test
    public void testCreateOpenAndDeleteChannel() throws Exception {

        MutableMessage mutableMessage = new MutableMessage();
        mutableMessage.setType(Message.VALUE_MESSAGE_TYPE_REQUEST);
        mutableMessage.setSender("testSender");
        mutableMessage.setRecipient("testRecipient");
        mutableMessage.setPayload(new byte[]{ 0, 1, 2 });
        mutableMessage.setTtlAbsolute(true);
        mutableMessage.setTtlMs(ExpiryDate.fromRelativeTtl(30000).getValue());

        byte[] serializedMessage = mutableMessage.getImmutableMessage().getSerializedMessage();

        final Object waitForChannelCreated = new Object();
        longpollingMessageReceiver.start(dispatcher, new ReceiverStatusListener() {

            @Override
            public void receiverStarted() {
                synchronized (waitForChannelCreated) {
                    waitForChannelCreated.notify();
                }
            }

            @Override
            public void receiverException(Throwable e) {
            }
        });

        synchronized (waitForChannelCreated) {
            waitForChannelCreated.wait(5000);
        }

        // post to the channel to see if it exists

        onrequest(1000).with()
                       .body(serializedMessage)
                       .expect()
                       .statusCode(201)
                       .when()
                       .post("channels/" + testChannelId + "/message/");

        longpollingMessageReceiver.shutdown(true);

        // post again; this time it should be missing (NO_CONTENT)
        onrequest(1000).with()
                       .body(serializedMessage)
                       .expect()
                       .statusCode(400)
                       .body(containsString("Channel not found"))
                       .when()
                       .post("channels/" + testChannelId + "/message/");

    }

    // ///////////////////////
    // HELPERS

    /**
     * initialize a RequestSpecification with the given timeout
     *
     * @param timeout_ms
     *            : a SocketTimeoutException will be thrown if no response is received in this many milliseconds
     * @return
     */
    private RequestSpecification onrequest(int timeout_ms) {
        return given().contentType(ContentType.BINARY)
                      .log()
                      .everything()
                      .config(RestAssuredConfig.config().httpClient(HttpClientConfig.httpClientConfig()
                                                                                    .setParam("http.socket.timeout",
                                                                                              timeout_ms)));
    }

}
