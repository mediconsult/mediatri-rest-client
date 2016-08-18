package fi.mediatri.rest.client;

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
public class AuthenticationTest {

  @Test
  public void testAuthentication() {
    // jvm param -Djavax.net.ssl.trustStore=certs.jks
    // use trust store for accessing real mediatri
    // final MediatriRestClient client = new MediatriRestClient("https://localhost:8443/CoreWEB/api/v1");
    final MediatriRestClient client = new MediatriRestClient(TestBase.API_ADDRESS);
    try {
      client.authenticate("MGR", "", 5, 600000001);
    }
    catch (final AuthenticationException e) {
      Assert.fail(e.getMessage());
    }
    client.logout();
  }

  @Test
  public void testContextAuthentication() {
    final MediatriRestClient client = new MediatriRestClient(TestBase.API_ADDRESS);
    try {
      //Create context with mediatri by logging in to Mediatri
      client.authenticateWithContext("MCM:3C9ED497A2EDD9C835929A80E36268C1A600F0C5@IHEOY5J0");
    }
    catch (final AuthenticationException e) {
      Assert.fail(e.getMessage());
    }
    client.logout();
  }

  @Test
  public void testSetRoleAfterLogin() {
    final MediatriRestClient client = new MediatriRestClient(TestBase.API_ADDRESS);
    try {
      client.authenticate("MGR", "", 5, 600000001);
    }
    catch (final AuthenticationException e) {
      Assert.fail(e.getMessage());
    }
    client.setRoleForThisSesson(5, 600000001);
    Assert.assertEquals(5l, client.getIdentity().get("activeRole"));
    client.logout();
  }
}
