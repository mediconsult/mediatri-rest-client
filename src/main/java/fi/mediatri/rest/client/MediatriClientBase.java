package fi.mediatri.rest.client;

import java.lang.invoke.MethodHandles;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.client.Invocation.Builder;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.NewCookie;
import javax.ws.rs.core.Response;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import fi.mediconsult.medicloud.coreclient.exception.ClientException;

/*
 * Copyright (C) 2016 Mediconsult
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
public class MediatriClientBase {

  private final Log log = LogFactory.getLog(MethodHandles.lookup().lookupClass());

  protected Map<String, NewCookie> cookies;

  protected String endpointURL;

  protected JSONObject getJSONObject(String response) {
    try {
      return (JSONObject) new JSONParser().parse(response);
    }
    catch (final ParseException e) {
      throw new ClientException("Unable parse JSON response: " + e.getMessage());
    }
  }

  protected String postAndReleaseConnection(Builder request, Entity<?> e) {
    request = request.accept(MediaType.APPLICATION_JSON);
    final Response response = request.post(e, Response.class);
    if (response.getStatus() != 200 && response.getStatus() != 204) {
      throw new WebApplicationException(response.getStatus());
    }
    if (cookies == null || cookies.isEmpty()) {
      cookies = getCookiesFromHeaders(response);
    }
    //readEntity closes the connection
    return response.readEntity(String.class);
  }

  protected String getAndReleaseConnection(Builder request) {
    request = request.accept(MediaType.APPLICATION_JSON);
    final Response response = request.get(Response.class);
    if (response.getStatus() != 200 && response.getStatus() != 204) {
      throw new WebApplicationException(response.getStatus());
    }
    if (cookies == null || cookies.isEmpty()) {
      cookies = getCookiesFromHeaders(response);
    }
    //readEntity closes the connection
    return response.readEntity(String.class);
  }

  /**
   * Extracts cookies from headers.
   * @param response the response from authentication
   */
  private Map<String, NewCookie> getCookiesFromHeaders(
      final Response response) {

    final Map<String, NewCookie> cookies = new HashMap<>();

    final MultivaluedMap<String, Object> headers = response.getHeaders();

    final Set<Entry<String, List<Object>>> entries = headers.entrySet();
    for (final Entry<String, List<Object>> entry : entries) {
      if (log.isDebugEnabled()) {
        log.debug("entry; " + entry.getKey());
        log.debug("values: " + Arrays.asList(entry.getValue()));
      }
      if (entry.getKey() != null && "Set-Cookie".equals(entry.getKey())) {
        if (log.isDebugEnabled()) {
          log.debug("setting cookie");
        }
        for (final Object valueObject : entry.getValue()) {
          final String value = (String) valueObject;
          if (value != null && !"".equals(value)) {
            //value = value.substring(0, value.indexOf(";"));
            final String cookieName = value.substring(0, value.indexOf("="));
            final String cookieValue =
                value.substring(value.indexOf("=") + 1, value.length());
            if (log.isDebugEnabled()) {
              log.debug("cookieName: " + cookieName);
              log.debug("cookieValue: " + cookieValue);
            }

            final NewCookie cookie = new NewCookie(cookieName, cookieValue);
            cookies.put(cookieName, cookie);
          }
        }
      }
    }
    return cookies;
  }

  protected WebTarget getTarget(String api) {
    final String address = endpointURL + api;
    //create the request
    final Client client = ClientBuilder.newClient();
    return client.target(address);
  }

  protected Builder setCookiesToRequest(Builder request) {
    if (cookies == null || cookies.isEmpty()) {
      return request;
    }
    for (final NewCookie cookie : cookies.values()) {
      log.debug("setting cookie for request: " + cookie.getName());
      log.debug("cookie.getValue(): " + cookie.getValue());
      request = request.cookie(cookie.getName(), cookie.getValue());
    }
    return request;
  }
}
