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
 * API for accessing mediatri patient information
 */
public class PatientResource extends MediatriClientBase {

  PatientResource(MediatriRestClient c) {
    this.cookies = c.cookies;
    this.endpointURL = c.endpointURL;
  }

  /**
  * @param id Patient Id
  * @return Patient by Id
  */
  public JSONObject getPatient(int id) {
    WebTarget target = getTarget("patients/id/{id}");
    target = target.resolveTemplate("id", id);
    final Builder request = setCookiesToRequest(target.request());
    return getJSONObject(getAndReleaseConnection(request));
  }

  /**
   * @param ssn social secutity number
   * @return Patient by patient social security number
   */
  public JSONObject getPatient(String ssn) {
    WebTarget target = getTarget("patients/ssn/{id}");
    target = target.resolveTemplate("id", ssn);
    final Builder request = setCookiesToRequest(target.request());
    return getJSONObject(getAndReleaseConnection(request));
  }

  /**
   *
   * @param id Patient Id
   * @param includeBasicInfo prefetches basicInfo to response if true
   * @param includeHoitosuhde prefetches hoitosuhde to response if true
   * @param includeRiskitietoStatus prefetches riskitieto to response if true
   * @return Patient by Id
   */
  public JSONObject getPatient(int id, boolean includeBasicInfo, boolean includeHoitosuhde, boolean includeRiskitietoStatus) {
    WebTarget target = getTarget("patients/id/{id}");
    target = target.resolveTemplate("id", id);
    target = target.queryParam("includeBasicInfo", includeBasicInfo);
    target = target.queryParam("includeHoitosuhde", includeHoitosuhde);
    target = target.queryParam("includeRiskitietoStatus", includeRiskitietoStatus);
    final Builder request = setCookiesToRequest(target.request());
    return getJSONObject(getAndReleaseConnection(request));
  }

  /**
   *
   * @param pageIndex page index, starting from which client wants to list patients. Minimum value fior page index is 1.
   * @param pageSize defines page size i.e. maximum amount of patients returned in on query, if page size is set, then also pageIndex parameter must be defined. If not set, returning all the patients matching given conditions.
   * @return List of patients that user has access to
   */
  public JSONObject getPatientList(int pageIndex, int pageSize) {
    WebTarget target = getTarget("patients/list}");
    target = target.queryParam("pageIndex", pageIndex);
    target = target.queryParam("pageSize", pageSize);
    final Builder request = setCookiesToRequest(target.request());
    return getJSONObject(getAndReleaseConnection(request));
  }

  /**
   *
   * @param firstNamePrefix first name starts with condition, or null if not used as criterion
   * @param fullNameContains patient's full name contains string condition, or null if not used as criterion Full name search overrides more specific lastNamePrefix, firstNamePrefix, userNamePrefix searches.
   * @param lastNamePrefix last name starts with condition, or null if not used as criterion
   * @param pageIndex page index, starting from which client wants to list patients. Minimum value fior page index is 1.
   * @param pageSize defines page size i.e. maximum amount of patients returned in on query, if page size is set, then also pageIndex parameter must be defined. If not set, returning all the patients matching given conditions.
   * @param ssnPrefix
   * @return List of patients that user has access to and matches search parameters
   */
  public JSONObject getPatientSearch(String firstNamePrefix, String fullNameContains, String lastNamePrefix, String pageIndex,
      String pageSize, String ssnPrefix) {
    WebTarget target = getTarget("patients/list}");
    target = target.queryParam("pageIndex", pageIndex);
    target = target.queryParam("pageSize", pageSize);
    final Builder request = setCookiesToRequest(target.request());
    return getJSONObject(getAndReleaseConnection(request));
  }
}
