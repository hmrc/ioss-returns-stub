/*
 * Copyright 2023 HM Revenue & Customs
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

package uk.gov.hmrc.iossreturnsstub.utils

import org.scalatest.concurrent.ScalaFutures
import org.scalatest.freespec.AnyFreeSpec
import org.scalatest.matchers.should.Matchers
import play.api.libs.json.Json
import play.api.mvc.Results.Accepted
import play.api.test.Helpers._
import uk.gov.hmrc.iossreturnsstub.models._

import java.time.{Clock, Instant, LocalDate, Month, ZoneId}
import java.time.temporal.ChronoUnit
import scala.concurrent.Future

class JsonSchemaHelperSpec extends AnyFreeSpec with ScalaFutures with Matchers {

  private val stubClock: Clock = Clock.fixed(LocalDate.now.atStartOfDay(ZoneId.systemDefault).toInstant, ZoneId.systemDefault)

  private val jsonSchemaHelper = new JsonSchemaHelper(stubClock)

  "JsonSchemaHelper#applySchemaValidation" - {
    val validSchemaPath = "/resources/schemas/core-vat-return-schema.json"

    "return with a successful status" in {

      val now = Instant.now(stubClock).truncatedTo(ChronoUnit.MILLIS)
      val period = Period(2021, Month.NOVEMBER)
      val coreVatReturn = CoreVatReturn(
        "XI/IM9001234567/M11.2021",
        now.toString,
        CoreTraderId(
          "IM9001234567",
          "XI"
        ),
        now.toString,
        CorePeriod(
          2021,
          3
        ),
        period.firstDay,
        period.lastDay,
        now,
        BigDecimal(5000),
        List(CoreMsconSupply(
          "DE",
          BigDecimal(5000),
          BigDecimal(1000),
          BigDecimal(1000),
          List(CoreSupply(
            "GOODS",
            BigDecimal(10),
            "STANDARD",
            BigDecimal(10),
            BigDecimal(10)
          )),
          List(CoreCorrection(
            CorePeriod(2021, 2),
            BigDecimal(100)
          ))
        ))
      )

      val validJson = Json.toJson(coreVatReturn)

      val result = jsonSchemaHelper.applySchemaValidation(validSchemaPath, Some(validJson)) {
        Future.successful(Accepted(""))
      }

      status(result) shouldBe ACCEPTED
    }

    "return with a failure status" - {
      "when json is invalid" in {
        val invalidJsonString = "{}"

        val invalidJson = Json.toJson(invalidJsonString)

        val result = jsonSchemaHelper.applySchemaValidation(validSchemaPath, Some(invalidJson)) {
          Future.successful(Accepted(""))
        }

        status(result) shouldBe BAD_REQUEST
        contentAsJson(result) shouldBe Json.toJson(EisErrorResponse(CoreErrorResponse(Instant.now(stubClock), None, "OSS_400", "Bad Request")))
      }

      "when no json is passed" in {
        val result = jsonSchemaHelper.applySchemaValidation(validSchemaPath, None) {
          Future.successful(Accepted(""))
        }

        status(result) shouldBe BAD_REQUEST
        contentAsJson(result) shouldBe Json.toJson(EisErrorResponse(CoreErrorResponse(Instant.now(stubClock), None, "OSS_400", "Missing Payload")))
      }

      "when schema doesn't exist" in {
        val result = jsonSchemaHelper.applySchemaValidation("invalidPath", None) {
          Future.successful(Accepted(""))
        }

        status(result) shouldBe INTERNAL_SERVER_ERROR
      }
    }

  }

}
