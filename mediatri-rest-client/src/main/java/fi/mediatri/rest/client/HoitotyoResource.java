package fi.mediatri.rest.client;

import javax.ws.rs.client.Invocation.Builder;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.client.Entity;
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
 * API for accessing hoitotyon merkinnät and hoitotyön kirjaukset
 */
public class HoitotyoResource extends MediatriClientBase {

  HoitotyoResource(MediatriRestClient c) {
    this.cookies = c.cookies;
    this.endpointURL = c.endpointURL;
  }

  /**
   * This method returns hoitotyön merkintä entry by OSA numbery. Or 404- if hoitotyömerkintä is missing.
   * @param patientId
   * @param osaId
   * @return HoitotyoJSON
   */
  public JSONObject getHoitotyonMerkinta(int patientId, int osaId) {
    WebTarget target = getTarget("hoitotyo/{patientOmistajaId}/{osaId}");
    target = target.resolveTemplate("patientOmistajaId", patientId);
    target = target.resolveTemplate("osaId", osaId);
    final Builder request = setCookiesToRequest(target.request());
    return getJSONObject(getAndReleaseConnection(request));
  }

  /**
   * Creates new version for given Hoitotyomerkinta. Post can be partial as most fields are set by server (time, kirjaaja etc.) anyways. Posts eg: {"teksti":"new teksti", "tilanne":"PA"} {"teksti":"new teksti"} are valid posts.
   *
   */
  public JSONObject createVersion(int patientId, int hoitotyoId, int merkintaId, JSONObject newVersionChanges) {
    WebTarget target = getTarget("hoitotyo/{patientOmistajaId}/{osaId}/createVersion");
    target = target.resolveTemplate("patientOmistajaId", patientId);
    target = target.resolveTemplate("hoitotyoId", hoitotyoId);
    target = target.resolveTemplate("merkintaId", merkintaId);
    final Builder request = setCookiesToRequest(target.request());
    return getJSONObject(postAndReleaseConnection(request, Entity.entity(newVersionChanges, MediaType.APPLICATION_JSON)));
  }

  /**
   * Adds new record to hoitotyo. This can be anything but it is validated for correct code-usages and searches for duplicates within one hoitotyo.
   */
  public JSONObject addMerkintaToHoitotyo(int patientId, int hoitotyoId, JSONObject newRecord) {
    WebTarget target = getTarget("hoitotyo/{patientOmistajaId}/{osaId}/addMerkinta");
    target = target.resolveTemplate("patientOmistajaId", patientId);
    target = target.resolveTemplate("hoitotyoId", hoitotyoId);
    final Builder request = setCookiesToRequest(target.request());
    return getJSONObject(postAndReleaseConnection(request, Entity.entity(newRecord, MediaType.APPLICATION_JSON)));
  }

  /**
   * This method returns current hoitotyön merkinnät entry. Or 404- if current hoitotyön merkinnat is missing (new must be created in mediatri).
   * @param patientId patient id
   * @param includeHoitotyoAsetuksetInResponse Prefetch asetukset to this response. Asetukset can be read separately too with #getAsetukset
   * @return
   */
  public JSONObject getCurrentHoitotyo(int patientId, boolean includeHoitotyoAsetuksetInResponse) {
    WebTarget target = getTarget("hoitotyo/{patientOmistajaId}/current");
    target = target.resolveTemplate("id", patientId);
    target = target.queryParam("includeHoitotyoasetukset", includeHoitotyoAsetuksetInResponse);
    final Builder request = setCookiesToRequest(target.request());
    return getJSONObject(getAndReleaseConnection(request));
  }

  /**
   * This method returns current hoitotyön merkinnät entry. Or 404- if current hoitotyön merkinnat is missing (new must be created in mediatri).
   * @param patientId patient id
   * @return  Settings for hoitotyo. Used koodistot, which permissions etc.
   */
  public JSONObject getHoitotyoasetukset(int patientId) {
    WebTarget target = getTarget("hoitotyo/asetukset");
    target = target.resolveTemplate("id", patientId);
    final Builder request = setCookiesToRequest(target.request());
    return getJSONObject(getAndReleaseConnection(request));
  }
}
