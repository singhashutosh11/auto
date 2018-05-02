package com.thed.zephyr.cloud.rest.util.json;

import com.atlassian.connect.play.java.AcHost;
import com.atlassian.connect.play.java.http.HttpMethod;
import com.atlassian.fugue.Option;
import com.atlassian.httpclient.api.Request;
import com.atlassian.jira.rest.client.api.AuthenticationHandler;
import com.atlassian.jwt.SigningAlgorithm;
import com.atlassian.jwt.core.TimeUtil;
import com.atlassian.jwt.core.writer.JsonSmartJwtJsonBuilder;
import com.atlassian.jwt.core.writer.JwtClaimsBuilder;
import com.atlassian.jwt.core.writer.NimbusJwtWriterFactory;
import com.atlassian.jwt.exception.JwtIssuerLacksSharedSecretException;
import com.atlassian.jwt.exception.JwtSigningException;
import com.atlassian.jwt.exception.JwtUnknownIssuerException;
import com.atlassian.jwt.httpclient.CanonicalHttpUriRequest;
import com.atlassian.jwt.writer.JwtJsonBuilder;
import com.atlassian.jwt.writer.JwtWriter;
import com.atlassian.jwt.writer.JwtWriterFactory;
import com.google.common.base.Function;
import com.google.common.collect.Maps;
import com.thed.zephyr.cloud.rest.model.ZConfig;
import com.thed.zephyr.cloud.rest.client.async.JwtAuthorizationGenerator;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicHeaderValueParser;
import org.apache.http.message.ParserCursor;
import org.apache.http.util.CharArrayBuffer;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLDecoder;
import java.security.NoSuchAlgorithmException;
import java.util.*;

import static com.atlassian.jwt.JwtConstants.HttpRequests.JWT_AUTH_HEADER_PREFIX;
import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by aliakseimatsarski on 3/25/16.
 */
public class ZephyrAuthenticationHandler implements AuthenticationHandler {

    private ZConfig zConfig;

    public ZephyrAuthenticationHandler(ZConfig zConfig) {
        this.zConfig = zConfig;
    }

    private ZephyrAuthenticationHandler() {
    }

    public void configure(Request request) {
        final JwtWriterFactory jwtWriterFactory = new NimbusJwtWriterFactory();
        final char[] QUERY_DELIMITERS = new char[]{'&'};
        final int jwtExpiryWindowSeconds = 60 * 3;
        final JwtAuthorizationGenerator jwtAuthorisationGenerator = new JwtAuthorizationGenerator(jwtWriterFactory, jwtExpiryWindowSeconds) {

            public Option<String> generate(HttpMethod httpMethod, URI url, Map<String, List<String>> parameters, AcHost acHost,
                                           Option<String> userId)
                    throws JwtIssuerLacksSharedSecretException, JwtUnknownIssuerException {

                checkArgument(null != parameters, "Parameters Map argument cannot be null");
                checkNotNull(acHost);

                Map<String, String[]> paramsAsArrays = Maps.transformValues(parameters, new Function<List<String>, String[]>() {
                    public String[] apply(List<String> input) {
                        return checkNotNull(input).toArray(new String[input.size()]);
                    }
                });
                return Option.some(JWT_AUTH_HEADER_PREFIX + encodeJwt(httpMethod, url, paramsAsArrays, userId.getOrNull(),
                        acHost));
            }

            private String encodeJwt(HttpMethod httpMethod, URI targetPath, Map<String, String[]> params, String userKeyValue,
                                     AcHost acHost) throws JwtUnknownIssuerException, JwtIssuerLacksSharedSecretException {
                checkArgument(null != httpMethod, "HttpMethod argument cannot be null");
                checkArgument(null != targetPath, "URI argument cannot be null");

                JwtJsonBuilder jsonBuilder = new JsonSmartJwtJsonBuilder()
                        .issuedAt(TimeUtil.currentTimeSeconds())
                        .expirationTime(TimeUtil.currentTimePlusNSeconds(jwtExpiryWindowSeconds))
                        .issuer(zConfig.host.getKey());

                if (null != userKeyValue) {
                    jsonBuilder = jsonBuilder.subject(userKeyValue);
                }

                Map<String, String[]> completeParams = params;

                try {
                    if (!StringUtils.isEmpty(targetPath.getQuery())) {
                        completeParams = new HashMap<String, String[]>(params);
                        completeParams.putAll(constructParameterMap(targetPath));
                    }

                    CanonicalHttpUriRequest canonicalHttpUriRequest = new CanonicalHttpUriRequest(httpMethod.toString(),
                            targetPath.getPath(), "", completeParams);

                    JwtClaimsBuilder.appendHttpRequestClaims(jsonBuilder, canonicalHttpUriRequest);
                } catch (UnsupportedEncodingException e) {
                    throw new RuntimeException(e);
                } catch (NoSuchAlgorithmException e) {
                    throw new RuntimeException(e);
                }

                return issueJwt(jsonBuilder.build(), zConfig);
            }

            private String issueJwt(String jsonPayload, ZConfig config) throws JwtSigningException, JwtIssuerLacksSharedSecretException, JwtUnknownIssuerException {
                return getJwtWriter(config).jsonToJwt(jsonPayload);
            }

            private JwtWriter getJwtWriter(ZConfig config) throws JwtUnknownIssuerException, JwtIssuerLacksSharedSecretException {
                return jwtWriterFactory.macSigningWriter(SigningAlgorithm.HS256, config.JIRA_SHARED_SECRET);
            }

            private Map<String, String[]> constructParameterMap(URI uri) throws UnsupportedEncodingException {
                final String query = uri.getQuery();
                if (query == null) {
                    return Collections.emptyMap();
                }
                Map<String, String[]> queryParams = new HashMap<String, String[]>();
                CharArrayBuffer buffer = new CharArrayBuffer(query.length());
                buffer.append(query);
                ParserCursor cursor = new ParserCursor(0, buffer.length());

                while (!cursor.atEnd()) {
                    NameValuePair nameValuePair = BasicHeaderValueParser.DEFAULT.parseNameValuePair(buffer, cursor, QUERY_DELIMITERS);
                    if (!StringUtils.isEmpty(nameValuePair.getName())) {
                        String decodedName = urlDecode(nameValuePair.getName());
                        String decodedValue = urlDecode(nameValuePair.getValue());
                        String[] oldValues = queryParams.get(decodedName);
                        String[] newValues = null == oldValues ? new String[1] : Arrays.copyOf(oldValues, oldValues.length + 1);
                        newValues[newValues.length - 1] = decodedValue;
                        queryParams.put(decodedName, newValues);
                    }
                }
                return queryParams;
            }

            private String urlDecode(final String content) throws UnsupportedEncodingException {
                return null == content ? null : URLDecoder.decode(content, "UTF-8");
            }
        };
        Option<String> jwt = null;
        try {
            final URI uriWithoutProductContext = getUri(request, zConfig.ZEPHYR_BASE_URL);
            jwt = jwtAuthorisationGenerator.generate(HttpMethod.valueOf(request.getMethod().name()), uriWithoutProductContext, new HashMap<String, List<String>>(), zConfig.host, zConfig.USER_NAME);
        } catch (Exception e) {
        }
        final String authorizationHeaderValue = jwt.getOrNull();

        request.setHeader("Authorization", authorizationHeaderValue);
        request.setHeader("X-acpt", authorizationHeaderValue);
        request.setHeader("zapiAccessKey", zConfig.ACCESS_KEY);
    }

    private URI getUri(Request request, String baseUrlString) throws URISyntaxException {
        final URI uri = request.getUri();
        final String path = uri.getPath();
        final URI baseUrl = new URI(baseUrlString);
        final String productContext = baseUrl.getPath();
        final String pathWithoutProductContext = path.substring(productContext.length());
        return new URI(uri.getScheme(), uri.getUserInfo(), uri.getHost(), uri.getPort(),
                pathWithoutProductContext, uri.getQuery(), uri.getFragment());
    }
}
