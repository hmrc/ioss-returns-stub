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

package uk.gov.hmrc.iossreturnsstub.controllers

import org.scalatest.freespec.AnyFreeSpec
import org.scalatest.matchers.should.Matchers
import play.api.http.{MimeTypes, Status}
import play.api.libs.json.Json
import play.api.mvc.Headers
import play.api.test.{FakeRequest, Helpers}
import play.api.test.Helpers._
import uk.gov.hmrc.iossreturnsstub.models._
import uk.gov.hmrc.iossreturnsstub.utils.JsonSchemaHelper

import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.time.{Clock, Instant, LocalDate, LocalDateTime, Month, ZoneId}
import java.util.{Locale, UUID}

class CoreControllerSpec extends AnyFreeSpec with Matchers {

  private val dateTimeFormatter = DateTimeFormatter.ofPattern("EEE, dd MMM yyyy HH:mm:ss z")
    .withLocale(Locale.ENGLISH)
    .withZone(ZoneId.of("GMT"))
  private val fakeRequest = FakeRequest(POST, routes.CoreController.submitVatReturn().url)
  private val stubClock: Clock = Clock.fixed(LocalDate.now.atStartOfDay(ZoneId.systemDefault).toInstant, ZoneId.systemDefault)
  private val jsonSchemaHelper = new JsonSchemaHelper(stubClock)
  private val controller = new CoreController(Helpers.stubControllerComponents(), jsonSchemaHelper, stubClock)
  private val validHeaders: Seq[(String, String)] = Seq(
    (AUTHORIZATION, ""),
    (ACCEPT, MimeTypes.JSON),
    ("X-Correlation-Id", UUID.randomUUID().toString),
    ("X-Forwarded-Host", ""),
    (CONTENT_TYPE, MimeTypes.JSON),
    (DATE, dateTimeFormatter.format(LocalDateTime.now())))

  val validFakeHeaders = new Headers(validHeaders)

  "POST /oss/returns/v1/return" - {
    "Return accepted when valid payload" in {

      val now = Instant.now(stubClock).truncatedTo(ChronoUnit.MILLIS)
      val period = Period(2021, Month.NOVEMBER)
      val coreVatReturn = CoreVatReturn(
        "XI/XI123456789/Q4.2021",
        now.toString,
        CoreTraderId(
          "123456789AAA",
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

      val fakeRequestWithBody = fakeRequest.withJsonBody(Json.toJson(coreVatReturn)).withHeaders(validFakeHeaders)

      val result = controller.submitVatReturn()(fakeRequestWithBody)

      status(result) shouldBe Status.ACCEPTED
    }

    "Return missing registration error for IOSS Number IM9007777777" in {

      val now = Instant.now()
      val period = Period(2021, Month.NOVEMBER)
      val coreVatReturn = CoreVatReturn(
        "XI/IM9007777777/M11.2021",
        now.toString,
        CoreTraderId(
          "123456789AAA",
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

      val fakeRequestWithBody = fakeRequest.withJsonBody(Json.toJson(coreVatReturn))
        .withHeaders(validFakeHeaders)

      val result = controller.submitVatReturn()(fakeRequestWithBody)

      status(result) shouldBe Status.BAD_REQUEST
    }

    "Return error when invalid payload" in {

      val coreVatReturn = """{"badJson":"bad"}""""

      val fakeRequestWithBody = fakeRequest.withJsonBody(Json.toJson(coreVatReturn)).withHeaders(validFakeHeaders)

      val result = controller.submitVatReturn()(fakeRequestWithBody)

      status(result) shouldBe Status.BAD_REQUEST

      val responseBody = contentAsString(result)
      responseBody.isEmpty shouldBe false
      val errorResponse = Json.parse(responseBody).validate[EisErrorResponse]
      errorResponse.isSuccess shouldBe true
    }

    "Return error when using more than two decimal digits" in {

      val coreVatReturn = """{
                            |  "vatReturnReferenceNumber" : "XI/XI123456789/Q4.2021",
                            |  "version" : "2022-03-07T14:58:07.374Z",
                            |  "traderId" : {
                            |    "vatNumber" : "123456789AAA",
                            |    "issuedBy" : "XI"
                            |  },
                            |  "period" : {
                            |    "year" : 2021,
                            |    "quarter" : 3
                            |  },
                            |  "startDate" : "2021-07-01",
                            |  "endDate" : "2021-09-30",
                            |  "submissionDateTime" : "2022-03-07T14:58:07.374Z",
                            |  "totalAmountVatDueGBP" : 5000.123456789,
                            |  "msconSupplies" : [ {
                            |    "msconCountryCode" : "DE",
                            |    "balanceOfVatDueGBP" : 5000.123456789,
                            |    "grandTotalMsidGoodsGBP" : 1000.123456789,
                            |    "correctionsTotalGBP" : 1000.123456789,
                            |    "msidSupplies" : [ {
                            |      "supplyType" : "GOODS",
                            |      "vatRate" : 10,
                            |      "vatRateType" : "STANDARD",
                            |      "taxableAmountGBP" : 10.123456789,
                            |      "vatAmountGBP" : 10.123456789
                            |    } ],
                            |    "corrections" : [ {
                            |      "period" : {
                            |        "year" : 2021,
                            |        "quarter" : 2
                            |      },
                            |      "totalVatAmountCorrectionGBP" : 100.123456789
                            |    } ]
                            |  } ]
                            |}"""".stripMargin

      val fakeRequestWithBody = fakeRequest.withJsonBody(Json.parse(coreVatReturn)).withHeaders(validFakeHeaders)

      val result = controller.submitVatReturn()(fakeRequestWithBody)

      status(result) shouldBe Status.BAD_REQUEST

      val responseBody = contentAsString(result)
      responseBody.isEmpty shouldBe false
      val errorResponse = Json.parse(responseBody).validate[EisErrorResponse]
      errorResponse.isSuccess shouldBe true
    }

    "Return accepted when using one decimal digit" in {

      val coreVatReturn = """{
                            |  "vatReturnReferenceNumber" : "XI/XI123456789/Q4.2021",
                            |  "version" : "2022-03-07T14:58:07.374Z",
                            |  "traderId" : {
                            |    "IOSSNumber" : "IM9001234567",
                            |    "issuedBy" : "XI"
                            |  },
                            |  "changeDate" : "2022-03-07T14:58:07.374Z",
                            |  "period" : {
                            |    "year" : 2021,
                            |    "month" : "03"
                            |  },
                            |  "startDate" : "2021-07-01",
                            |  "endDate" : "2021-09-30",
                            |  "submissionDateTime" : "2022-03-07T14:58:07.374Z",
                            |  "totalAmountVatDueGBP" : 5000.1,
                            |  "msconSupplies" : [ {
                            |    "msconCountryCode" : "DE",
                            |    "balanceOfVatDueGBP" : 5000.1,
                            |    "grandTotalMsidGoodsGBP" : 1000.1,
                            |    "correctionsTotalGBP" : 1000.1,
                            |    "msidSupplies" : [ {
                            |      "supplyType" : "GOODS",
                            |      "vatRate" : 10,
                            |      "vatRateType" : "STANDARD",
                            |      "taxableAmountGBP" : 10.1,
                            |      "vatAmountGBP" : 10.1
                            |    } ],
                            |    "corrections" : [ {
                            |      "period" : {
                            |        "year" : 2021,
                            |        "month" : "02"
                            |      },
                            |      "totalVatAmountCorrectionGBP" : 100.1
                            |    } ]
                            |  } ]
                            |}"""".stripMargin

      val fakeRequestWithBody = fakeRequest.withJsonBody(Json.parse(coreVatReturn)).withHeaders(validFakeHeaders)

      val result = controller.submitVatReturn()(fakeRequestWithBody)

      status(result) shouldBe Status.ACCEPTED
    }

    "Return accepted when using two decimal digits" in {

      val coreVatReturn = """{
                            |  "vatReturnReferenceNumber" : "XI/XI123456789/Q4.2021",
                            |  "version" : "2022-03-07T14:58:07.374Z",
                            |  "traderId" : {
                            |    "IOSSNumber" : "IM9001234567",
                            |    "issuedBy" : "XI"
                            |  },
                            |  "changeDate" : "2022-03-07T14:58:07.374Z",
                            |  "period" : {
                            |    "year" : 2021,
                            |    "month" : "03"
                            |  },
                            |  "startDate" : "2021-07-01",
                            |  "endDate" : "2021-09-30",
                            |  "submissionDateTime" : "2022-03-07T14:58:07.374Z",
                            |  "totalAmountVatDueGBP" : 5000.10,
                            |  "msconSupplies" : [ {
                            |    "msconCountryCode" : "DE",
                            |    "balanceOfVatDueGBP" : 5000.10,
                            |    "grandTotalMsidGoodsGBP" : 1000.10,
                            |    "correctionsTotalGBP" : 1000.10,
                            |    "msidSupplies" : [ {
                            |      "supplyType" : "GOODS",
                            |      "vatRate" : 10,
                            |      "vatRateType" : "STANDARD",
                            |      "taxableAmountGBP" : 10.10,
                            |      "vatAmountGBP" : 10.10
                            |    } ],
                            |    "corrections" : [ {
                            |      "period" : {
                            |        "year" : 2021,
                            |        "month" : "02"
                            |      },
                            |      "totalVatAmountCorrectionGBP" : 100.10
                            |    } ]
                            |  } ]
                            |}"""".stripMargin

      val fakeRequestWithBody = fakeRequest.withJsonBody(Json.parse(coreVatReturn)).withHeaders(validFakeHeaders)

      val result = controller.submitVatReturn()(fakeRequestWithBody)

      status(result) shouldBe Status.ACCEPTED
    }

    "Return accepted for negative correction" in {

      val coreVatReturn = """{
                            |  "vatReturnReferenceNumber" : "XI/XI123456789/Q4.2021",
                            |  "version" : "2022-03-07T14:58:07.374Z",
                            |  "traderId" : {
                            |    "IOSSNumber" : "IM9001234567",
                            |    "issuedBy" : "XI"
                            |  },
                            |  "changeDate" : "2022-03-07T14:58:07.374Z",
                            |  "period" : {
                            |    "year" : 2021,
                            |    "month" : "03"
                            |  },
                            |  "startDate" : "2021-07-01",
                            |  "endDate" : "2021-09-30",
                            |  "submissionDateTime" : "2022-03-07T14:58:07.374Z",
                            |  "totalAmountVatDueGBP" : 0,
                            |  "msconSupplies" : [ {
                            |    "msconCountryCode" : "DE",
                            |    "balanceOfVatDueGBP" : -10,
                            |    "grandTotalMsidGoodsGBP" : 0,
                            |    "correctionsTotalGBP" : -10,
                            |    "msidSupplies" : [],
                            |    "corrections" : [ {
                            |      "period" : {
                            |        "year" : 2021,
                            |        "month" : "02"
                            |      },
                            |      "totalVatAmountCorrectionGBP" : -10
                            |    } ]
                            |  } ]
                            |}"""".stripMargin

      val fakeRequestWithBody = fakeRequest.withJsonBody(Json.parse(coreVatReturn)).withHeaders(validFakeHeaders)

      val result = controller.submitVatReturn()(fakeRequestWithBody)

      status(result) shouldBe Status.ACCEPTED
    }

    "Return error when vat rate is invalid" in {

      val coreVatReturn = """{
                            |  "vatReturnReferenceNumber" : "XI/XI123456789/Q4.2021",
                            |  "version" : "2022-03-07T14:58:07.374Z",
                            |  "traderId" : {
                            |    "vatNumber" : "123456789AAA",
                            |    "issuedBy" : "XI"
                            |  },
                            |  "period" : {
                            |    "year" : 2021,
                            |    "quarter" : 3
                            |  },
                            |  "startDate" : "2021-07-01",
                            |  "endDate" : "2021-09-30",
                            |  "submissionDateTime" : "2022-03-07T14:58:07.374Z",
                            |  "totalAmountVatDueGBP" : 5000.12,
                            |  "msconSupplies" : [ {
                            |    "msconCountryCode" : "DE",
                            |    "balanceOfVatDueGBP" : 5000.12,
                            |    "grandTotalMsidGoodsGBP" : 1000.12,
                            |    "correctionsTotalGBP" : 1000.12,
                            |    "msidSupplies" : [ {
                            |      "supplyType" : "GOODS",
                            |      "vatRate" : 101,
                            |      "vatRateType" : "STANDARD",
                            |      "taxableAmountGBP" : 10.12,
                            |      "vatAmountGBP" : 10.12
                            |    } ],
                            |    "corrections" : [ {
                            |      "period" : {
                            |        "year" : 2021,
                            |        "quarter" : 2
                            |      },
                            |      "totalVatAmountCorrectionGBP" : 100.12
                            |    } ]
                            |  } ]
                            |}"""".stripMargin

      val fakeRequestWithBody = fakeRequest.withJsonBody(Json.parse(coreVatReturn)).withHeaders(validFakeHeaders)

      val result = controller.submitVatReturn()(fakeRequestWithBody)

      status(result) shouldBe Status.BAD_REQUEST
    }

    "Return bad request when headers are missing" in {

      val now = Instant.now()
      val period = Period(2021, Month.NOVEMBER)
      val coreVatReturn = CoreVatReturn(
        "XI/XI123456789/Q4.2021",
        now.toString,
        CoreTraderId(
          "123456789AAA",
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

      val fakeRequestWithBody = fakeRequest.withJsonBody(Json.toJson(coreVatReturn))

      val result = controller.submitVatReturn()(fakeRequestWithBody)

      status(result) shouldBe Status.BAD_REQUEST
    }
  }

}
