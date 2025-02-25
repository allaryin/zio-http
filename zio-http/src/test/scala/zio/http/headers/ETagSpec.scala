/*
 * Copyright 2021 - 2023 Sporta Technologies PVT LTD & the ZIO HTTP contributors.
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

package zio.http.headers

import zio.Scope
import zio.test._

import zio.http.Header.ETag
import zio.http.Header.ETag.{Strong, Weak}

object ETagSpec extends ZIOSpecDefault {
  override def spec: Spec[TestEnvironment with Scope, Any] = suite("ETag header suite")(
    test("parse ETag header") {
      assertTrue(
        ETag.parse("""W/"testEtag"""") == Right(Weak("testEtag")),
        ETag.parse("""w/"testEtag"""") == Right(Weak("testEtag")),
        ETag.parse(""""testEtag"""") == Right(Strong("testEtag")),
        ETag.parse("W/Etag").isLeft,
        ETag.parse("Etag").isLeft,
        ETag.parse("""W/""""") == Right(Weak("")),
        ETag.parse("""""""") == Right(Strong("")),
      )
    },
    test("encode ETag header to String") {
      assertTrue(
        ETag.render(Strong("TestEtag")) == """"TestEtag"""",
        ETag.render(Weak("TestEtag")) == """W/"TestEtag"""",
      )
    },
    test("parsing and encoding are symmetrical") {
      assertTrue(
        ETag.render(ETag.parse("""w/"testEtag"""").toOption.get) == """W/"testEtag"""",
        ETag.render(ETag.parse("""W/"testEtag"""").toOption.get) == """W/"testEtag"""",
        ETag.render(ETag.parse(""""testEtag"""").toOption.get) == """"testEtag"""",
      )

    },
  )
}
