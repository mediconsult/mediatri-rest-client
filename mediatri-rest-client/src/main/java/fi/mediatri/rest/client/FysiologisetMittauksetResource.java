package fi.mediatri.rest.client;

import javax.ws.rs.client.Invocation.Builder;
import javax.ws.rs.client.WebTarget;

import org.json.simple.JSONObject;

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
 * API for accessing Fysiologiset mittaukset
 */
public class FysiologisetMittauksetResource extends MediatriClientBase {

  FysiologisetMittauksetResource(MediatriRestClient c) {
    this.cookies = c.cookies;
    this.endpointURL = c.endpointURL;
  }

  /**
   * Fetch the list of measurements. Service returns the list of measurements for the last month from the current day. The measurements are grouped by title first. Titles are returned in alphabetical order.
   * @param patientId   Patient id for whom to fetch the data
   * @param count Set the limit to the result set. Default is 5 if no value is given. It means that service returns 5 latest measurements for each title.
   * @return object with array of mittausinfo
   */
  public JSONObject getMeasurements(int patientId, int count) {
    WebTarget target = getTarget("measurements/{id}");
    target = target.resolveTemplate("id", patientId);
    target = target.queryParam("count", count);
    final Builder request = setCookiesToRequest(target.request());
    return getJSONObject(getAndReleaseConnection(request));
  }

  /**
   * @return Configuration for fysiologiset mittaukset. Eg. permissions for current user, which title is editatble, what are the main titles.
   */
  public JSONObject getMeasumentConfig() {
    final WebTarget target = getTarget("measurements/config");
    final Builder request = setCookiesToRequest(target.request());
    return getJSONObject(getAndReleaseConnection(request));
  }
}
