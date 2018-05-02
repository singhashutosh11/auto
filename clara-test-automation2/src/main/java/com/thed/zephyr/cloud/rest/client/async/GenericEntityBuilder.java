package com.thed.zephyr.cloud.rest.client.async;

import com.atlassian.httpclient.api.EntityBuilder;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Collections;
import java.util.Map;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by aliakseimatsarski on 3/25/16.
 */
public class GenericEntityBuilder implements EntityBuilder {

    private final Object object;
    private Logger log = LoggerFactory.getLogger(GenericEntityBuilder.class);

    public GenericEntityBuilder(Object object) {
        this.object = object;
    }

    public Entity build() {
        Entity entity = new Entity() {
            public Map<String, String> getHeaders() {
                return Collections.singletonMap("Content-Type", "application/json");
            }

            public InputStream getInputStream() {
                try {
                    ObjectMapper objectMapper = new ObjectMapper();
                    String jsonString = objectMapper.writeValueAsString(object).toString();
                    log.debug("Convert input object class:{} to string:{}", object.getClass().getCanonicalName(), jsonString);
                    return new ByteArrayInputStream(jsonString.getBytes(Charset.forName("UTF-8")));
                } catch (Exception exception) {
                    log.error("Error convert entity:{} to input stream.", object.getClass().getCanonicalName(), exception);
                }
                return null;
            }
        };
        return entity;
    }
}
