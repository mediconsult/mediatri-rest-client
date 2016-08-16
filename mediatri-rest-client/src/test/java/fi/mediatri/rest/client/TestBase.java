package fi.mediatri.rest.client;

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
public class TestBase {
  // jvm param -Djavax.net.ssl.trustStore=certs.jks
  // use trust store for accessing real mediatri
  //  public static final String API_ADDRESS = "https://localhost:8443/CoreWEB/api/v1";
  public static final String API_ADDRESS = "http://localhost:8080/CoreWEB/api/v1";
}