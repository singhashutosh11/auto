package com.thed.zephyr.cloud.rest.client.async;

import com.atlassian.event.api.EventPublisher;
import com.atlassian.httpclient.apache.httpcomponents.DefaultHttpClient;
import com.atlassian.httpclient.api.HttpClient;
import com.atlassian.httpclient.api.Request;
import com.atlassian.httpclient.api.factory.HttpClientOptions;
import com.atlassian.httpclient.spi.ThreadLocalContextManagers;
import com.atlassian.jira.rest.client.api.AuthenticationHandler;
import com.atlassian.jira.rest.client.internal.async.AtlassianHttpClientDecorator;
import com.atlassian.jira.rest.client.internal.async.DisposableHttpClient;
import com.atlassian.sal.api.ApplicationProperties;
import com.atlassian.util.concurrent.Effect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.Date;
import java.util.Properties;

/**
 * Created by aliakseimatsarski on 3/30/16.
 */
public class ZAsyncHttpClientFactory {

    public ZAsyncHttpClientFactory() {
    }

    public DisposableHttpClient createClient(URI serverUri, final AuthenticationHandler authenticationHandler, HttpClientOptions options) {
        options.setRequestPreparer(new Effect<Request>() {
            public void apply(Request request) {
                authenticationHandler.configure(request);
            }
        });
        final DefaultHttpClient defaultHttpClient = new DefaultHttpClient(new NoOpEventPublisher(), new RestClientApplicationProperties(serverUri), ThreadLocalContextManagers.noop(), options);
        return new AtlassianHttpClientDecorator(defaultHttpClient) {
            public void destroy() throws Exception {
                defaultHttpClient.destroy();
            }
        };
    }

    public DisposableHttpClient createClient(final HttpClient client) {
        return new AtlassianHttpClientDecorator(client) {
            public void destroy() throws Exception {
            }
        };
    }

    private static final class MavenUtils {
        private static final Logger logger = LoggerFactory.getLogger(ZAsyncHttpClientFactory.MavenUtils.class);
        private static final String UNKNOWN_VERSION = "unknown";

        private MavenUtils() {
        }

        static String getVersion(String groupId, String artifactId) {
            Properties props = new Properties();
            InputStream resourceAsStream = null;

            String ioe;
            try {
                resourceAsStream = ZAsyncHttpClientFactory.MavenUtils.class.getResourceAsStream(String.format("/META-INF/maven/%s/%s/pom.properties", new Object[]{groupId, artifactId}));
                props.load(resourceAsStream);
                String e = props.getProperty("version", "unknown");
                return e;
            } catch (Exception var15) {
                logger.debug("Could not find version for maven artifact {}:{}", groupId, artifactId);
                logger.debug("Got the following exception", var15);
                ioe = "unknown";
            } finally {
                if(resourceAsStream != null) {
                    try {
                        resourceAsStream.close();
                    } catch (IOException var14) {
                        ;
                    }
                }

            }

            return ioe;
        }
    }

    private static class RestClientApplicationProperties implements ApplicationProperties {
        private final String baseUrl;

        private RestClientApplicationProperties(URI jiraURI) {
            this.baseUrl = jiraURI.getPath();
        }

        public String getBaseUrl() {
            return this.baseUrl;
        }

        public String getDisplayName() {
            return "Atlassian JIRA Rest Java Client";
        }

        public String getVersion() {
            return ZAsyncHttpClientFactory.MavenUtils.getVersion("com.atlassian.jira", "jira-rest-java-com.atlassian.jira.rest.client");
        }

        public Date getBuildDate() {
            throw new UnsupportedOperationException();
        }

        public String getBuildNumber() {
            return String.valueOf(0);
        }

        public File getHomeDirectory() {
            return new File(".");
        }

        public String getPropertyValue(String s) {
            throw new UnsupportedOperationException("Not implemented");
        }
    }

    private static class NoOpEventPublisher implements EventPublisher {
        private NoOpEventPublisher() {
        }

        public void publish(Object o) {
        }

        public void register(Object o) {
        }

        public void unregister(Object o) {
        }

        public void unregisterAll() {
        }
    }
}
