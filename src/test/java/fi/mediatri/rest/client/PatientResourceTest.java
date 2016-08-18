package fi.mediatri.rest.client;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.junit.Assert;
import org.junit.Test;

import fi.mediconsult.medicloud.coreclient.exception.AuthenticationException;

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
public class PatientResourceTest {

  @Test
  public void testGetPatientById() {
    final MediatriRestClient client = new MediatriRestClient(TestBase.API_ADDRESS);
    try {
      client.authenticate("MGR", "", 5, 600000001);
    }
    catch (final AuthenticationException e) {
      Assert.fail(e.getMessage());
    }
    final JSONObject o = client.getPatientAPI().getPatient(3);
    Assert.assertEquals("TEST", o.get("lastName"));
    client.logout();
  }

  @Test
  public void testGetPatientByIdWithBasicInfo() {
    final MediatriRestClient client = new MediatriRestClient(TestBase.API_ADDRESS);
    try {
      client.authenticate("MGR", "", 5, 600000001);
    }
    catch (final AuthenticationException e) {
      Assert.fail(e.getMessage());
    }
    final JSONObject o = client.getPatientAPI().getPatient(3, true, false, false);
    Assert.assertEquals("TEST", o.get("lastName"));
    final JSONObject basicInfo = (JSONObject) o.get("basicInfo");
    System.out.println(basicInfo.toJSONString());
    Assert.assertEquals(true, basicInfo.get("dataLoaded"));
    Assert.assertTrue(((JSONArray) basicInfo.get("titles")).size() > 0);//just simple test that there is titles present
    client.logout();
  }

  @Test
  public void testGetPatientBySSN() {
    final MediatriRestClient client = new MediatriRestClient(TestBase.API_ADDRESS);
    try {
      client.authenticate("MGR", "", 5, 600000001);
    }
    catch (final AuthenticationException e) {
      Assert.fail(e.getMessage());
    }
    final JSONObject o = client.getPatientAPI().getPatient("010101-0101");
    Assert.assertNotNull(o);
    client.logout();
  }
}
