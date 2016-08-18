package fi.mediatri.rest.client;

import java.lang.invoke.MethodHandles;
import javax.ws.rs.client.Invocation.Builder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.simple.JSONObject;
import fi.mediconsult.medicloud.coreclient.exception.AuthenticationException;
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
/**
 * Main class for mediatri rest client.<br/>
 * Simple usage guide:<br/>
 * <pre>
 * MediatriRestClient client = new MediatriRestClient("https://mediatri-rest-accesspoint");
 * AuthenticationDto dto = client.authenticate("username", "password");
 * client.setRole(5,10);
 * client.getPatientAPI().getPatientList();
 * </pre>
 */
public class MediatriRestClient extends MediatriClientBase {

  private final Log log = LogFactory.getLog(MethodHandles.lookup().lookupClass());

  /**
   * Creates a new MediatriRestClient with given endpoint.
   * @param endpointURL MediatriRestClient server address
   */
  public MediatriRestClient(String endpointURL) {
    if (endpointURL == null) {
      throw new IllegalArgumentException();
    }
    this.endpointURL = endpointURL + (endpointURL.endsWith("/") ? "" : "/");
    if (log.isDebugEnabled()) {
      log.debug("CoreClient init with endpointURL: " + endpointURL);
    }
  }

  /**
   * Authenticates to mediatri using /authentication/contextLogin
   *
   * @param sessionKey Context sessionKey
   * @return JSONObject for authentication response
   * @throws AuthenticationException Thrown if authentication wasn't successful
   * @throws ClientException Thrown on http error or problem parsing the response
   */
  public JSONObject authenticateWithContext(String sessionKey) throws AuthenticationException {
    final Form f = new Form();
    f.param("sessionKey", sessionKey);
    final Builder request = setCookiesToRequest(getTarget("authentication/contextLogin").request());
    final String response = postAndReleaseConnection(request, Entity.entity(f, MediaType.APPLICATION_FORM_URLENCODED_TYPE));
    final JSONObject o = getJSONObject(response);

    @SuppressWarnings("unchecked")
    final Boolean b = (Boolean) o.getOrDefault("success", false);
    if (b == null || !b) {
      throw new AuthenticationException("Unable to login:" + o.get("message"));
    }
    return o;
  }

  /**
   * Authenticates to mediatri using /authentication/login GET
   *
   * @param username Mediatri username
   * @param password Mediatri user password
   * @param role Optional roleId for autoselecting user role+unit
   * @param unit Optional unitId for autoselecting user role+unit
   * @return JSONObject for authentication response
   * @throws AuthenticationException Thrown if authentication wasn't successful
   * @throws ClientException Thrown on http error or problem parsing the response
   */
  public JSONObject authenticate(String username, String password, Integer role, Integer unit) throws AuthenticationException {
    WebTarget target = getTarget("authentication/login")
        .queryParam("username", username)
        .queryParam("password", password)
        .queryParam("mediatritunnus", "");
    if (role != null) {
      target = target.queryParam("role", role).queryParam("unit", unit);
    }
    final Builder request = setCookiesToRequest(target.request());
    final String s = getAndReleaseConnection(request);
    final JSONObject o = getJSONObject(s);
    @SuppressWarnings("unchecked")
    final Boolean b = (Boolean) o.getOrDefault("success", false);
    if (b == null || !b) {
      throw new AuthenticationException("Unable to login:" + o.get("message"));
    }
    return o;

  }

  /**
   * Logs current user out.
   * @return
   */
  public void logout() {
    if (cookies != null && !cookies.isEmpty()) {
      if (log.isDebugEnabled()) {
        log.debug("logout with cookies: " + cookies);
      }

      final Builder request = setCookiesToRequest(getTarget("authentication/logout").request());
      request.accept(MediaType.APPLICATION_JSON);
      final Response response = request.post(null);
      //do the post
      if (log.isDebugEnabled()) {
        log.debug("Logout request status: " + response.getStatus());
        log.debug("response entity: " + response.getEntity());
      }

      if (response.getStatus() != 200) {
        throw new ClientException("Logout unsuccesful: " + response.getStatus());
      }
    }
    else {
      log.error("logout cookies are invalid");
    }
  }

  /**
   * You can verify current session information using this API
   * @return Authentication information for current user. Listing all available Units and roles for user.
   */
  public JSONObject getIdentity() {
    final WebTarget target = getTarget("authentication/identity");
    final Builder request = setCookiesToRequest(target.request());
    return getJSONObject(getAndReleaseConnection(request));
  }

  /**
   * @return Information about Mediplan related rights and settings that are configured in Mediatri
   */
  public JSONObject getPlannerRights() {
    final WebTarget target = getTarget("authentication/plannerRights");
    final Builder request = setCookiesToRequest(target.request());
    return getJSONObject(getAndReleaseConnection(request));
  }

  /**
   * @param roleId Id for users role.
   * @param unitId Id for Unit
   * @return Authentication information on valid role change
   */
  @SuppressWarnings("unchecked")
  public JSONObject setRoleForThisSesson(int roleId, int unitId) {
    final WebTarget target = getTarget("authentication/role");
    final Builder request = setCookiesToRequest(target.request());
    final JSONObject o = new JSONObject();
    o.put("role", roleId);
    o.put("unit", unitId);
    return getJSONObject(postAndReleaseConnection(request, Entity.entity(o, MediaType.APPLICATION_JSON)));
  }

  public PatientResource getPatientAPI() {
    return new PatientResource(this);
  }

  public FysiologisetMittauksetResource getFysiolosetMittauksetAPI() {
    return new FysiologisetMittauksetResource(this);
  }
}
