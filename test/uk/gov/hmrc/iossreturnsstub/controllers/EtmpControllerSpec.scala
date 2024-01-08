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
import play.api.http.HeaderNames.{ACCEPT, AUTHORIZATION, CONTENT_TYPE, DATE}
import play.api.http.{MimeTypes, Status}
import play.api.libs.json.Json
import play.api.mvc.Headers
import play.api.test.Helpers._
import play.api.test.{FakeRequest, Helpers}
import uk.gov.hmrc.iossreturnsstub.models._
import uk.gov.hmrc.iossreturnsstub.models.etmp._
import uk.gov.hmrc.iossreturnsstub.utils.JsonSchemaHelper

import java.time.{Clock, LocalDate, LocalDateTime, Month, ZoneId}
import java.time.format.DateTimeFormatter
import java.util.{Locale, UUID}

class EtmpControllerSpec extends AnyFreeSpec with Matchers {

  private val iossNumber = "IM9001234567"
  private val period = Period(2023, Month.NOVEMBER)

  private val dateTimeFormatter = DateTimeFormatter.ofPattern("EEE, dd MMM yyyy HH:mm:ss z")
    .withLocale(Locale.UK)
    .withZone(ZoneId.of("GMT"))
  private val fakeRequest = FakeRequest(POST, routes.EtmpController.getVatReturn(iossNumber, period.toEtmpPeriodString).url)
  private val stubClock: Clock = Clock.fixed(LocalDate.now.atStartOfDay(ZoneId.systemDefault).toInstant, ZoneId.systemDefault)
  private val jsonSchemaHelper = new JsonSchemaHelper(stubClock)
  private val controller = new EtmpController(Helpers.stubControllerComponents(), jsonSchemaHelper)
  private val validHeaders: Seq[(String, String)] = Seq(
    (AUTHORIZATION, ""),
    (ACCEPT, MimeTypes.JSON),
    ("X-Correlation-Id", UUID.randomUUID().toString),
    ("X-Forwarded-Host", ""),
    (CONTENT_TYPE, MimeTypes.JSON),
    (DATE, dateTimeFormatter.format(LocalDateTime.now())))

  val validFakeHeaders = new Headers(validHeaders)

  "GET /vec/iossreturns/viewreturns/v1/{iossref}/{periodkey}" - {
    "Return OK when valid" in {

      val vatReturn = EtmpVatReturn(
        returnReference = "XI/IM9001234567/2023.M11",
        periodKey = "23AK",
        returnPeriodFrom = LocalDate.of(2023, 11, 1),
        returnPeriodTo = LocalDate.of(2023, 11, 30),
        goodsSupplied = Seq(
          EtmpVatReturnGoodsSupplied(
            msOfConsumption = "FR",
            vatRateType = EtmpVatRateType.ReducedVatRate,
            taxableAmountGBP = BigDecimal(12345.67),
            vatAmountGBP = BigDecimal(2469.13)
          )
        ),
        totalVATGoodsSuppliedGBP = BigDecimal(2469.13),
        totalVATAmountPayable = BigDecimal(2469.13),
        totalVATAmountPayableAllSpplied = BigDecimal(2469.13),
        correctionPreviousVATReturn = Seq(
          EtmpVatReturnCorrection(
            periodKey = "23AJ",
            periodFrom = LocalDate.of(2023, 10, 1).toString,
            periodTo = LocalDate.of(2023, 10, 31).toString,
            msOfConsumption = "FR",
            totalVATAmountCorrectionGBP = BigDecimal(2469.13),
            totalVATAmountCorrectionEUR = BigDecimal(2469.13)
          )
        ),
        totalVATAmountFromCorrectionGBP = BigDecimal(100.00),
        balanceOfVATDueForMS = Seq(
          EtmpVatReturnBalanceOfVatDue(
            msOfConsumption = "FR",
            totalVATDueGBP = BigDecimal(2569.13),
            totalVATEUR = BigDecimal(2569.13)
          )
        ),
        totalVATAmountDueForAllMSGBP = BigDecimal(2569.13),
        paymentReference = "XI/IM9001234567/2023.M11"

      )

      val fakeRequestWithBody = fakeRequest.withHeaders(validFakeHeaders)

      val result = controller.getVatReturn(iossNumber, period.toEtmpPeriodString)(fakeRequestWithBody)

      status(result) shouldBe Status.OK
      contentAsJson(result) shouldBe Json.toJson(vatReturn)
    }


    "Return bad request when headers are missing" in {

      val fakeRequestWithBody = fakeRequest

      val result = controller.getVatReturn(iossNumber, period.toEtmpPeriodString)(fakeRequestWithBody)

      status(result) shouldBe Status.BAD_REQUEST
    }
  }

  "GET /enterprise/obligation-data/{idType}/{idNumber}/{regimeType}" - {

    val idType = "IOSS"
    val regimeType = "IOSS"
    val obligationFulfilmentStatus = "A"
    val referenceNumber = "IM9001234567"
    val firstDateOfYear = LocalDate.of(2021, 1, 1)
    val lastDateOfYear = LocalDate.of(2021, 12, 31)
    val dateRange = DateRange(firstDateOfYear, lastDateOfYear)

    val fakeRequest = FakeRequest(
      GET,
      routes.EtmpController.getObligations(
        idType = idType,
        idNumber = iossNumber,
        regimeType = regimeType,
        dateRange,
        status = obligationFulfilmentStatus
      ).url
    )

    "return a successful response" in {

      val successfulObligationsResponse = EtmpObligations(
        referenceNumber = referenceNumber,
        referenceType = idType,
        obligationDetails = Seq(
          EtmpObligationDetails(
            status = EtmpObligationsFulfilmentStatus.Open,
            periodKey = "23AL"
          ),
          EtmpObligationDetails(
            status = EtmpObligationsFulfilmentStatus.Fulfilled,
            periodKey = "23AK"
          )
        )
      )

      val fakeRequestWithBody = fakeRequest.withHeaders(validFakeHeaders)

      val result = controller
        .getObligations(
          idType = idType,
          idNumber = referenceNumber,
          regimeType = regimeType,
          dateRange,
          status = obligationFulfilmentStatus
        )(fakeRequestWithBody)

      status(result) shouldBe Status.OK
      contentAsJson(result) shouldBe Json.toJson(successfulObligationsResponse)
    }

    "return a bad request when headers are missing" in {

      val fakeRequestWithBody = fakeRequest

      val result = controller
        .getObligations(
          idType = idType,
          idNumber = referenceNumber,
          regimeType = regimeType,
          dateRange,
          status = obligationFulfilmentStatus
        )(fakeRequestWithBody)

      status(result) shouldBe Status.BAD_REQUEST
    }
  }
}
