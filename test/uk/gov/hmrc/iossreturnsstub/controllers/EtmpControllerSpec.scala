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
import play.api.test.Helpers.*
import play.api.test.{FakeRequest, Helpers}
import uk.gov.hmrc.iossreturnsstub.models.*
import uk.gov.hmrc.iossreturnsstub.models.etmp.*
import uk.gov.hmrc.iossreturnsstub.utils.JsonSchemaHelper
import uk.gov.hmrc.iossreturnsstub.utils.ReturnData._

import java.time.{Clock, LocalDate, LocalDateTime, Month, ZoneId}
import java.time.format.DateTimeFormatter
import java.util.{Locale, UUID}

class EtmpControllerSpec extends AnyFreeSpec with Matchers {

  private val iossNumber = "IM9001234567"
  private val period = Period(2023, Month.NOVEMBER)
  private val country: String = "DE"

  private val dateTimeFormatter = DateTimeFormatter.ofPattern("EEE, dd MMM yyyy HH:mm:ss z")
    .withLocale(Locale.ENGLISH)
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
        returnReference = s"XI/IM9001234567/2023.M11",
        returnVersion = LocalDateTime.of(2024, 1, 2, 0, 0, 0),
        periodKey = "23AK",
        returnPeriodFrom = LocalDate.of(2023, 11, 1),
        returnPeriodTo = LocalDate.of(2023, 11, 30),
        goodsSupplied = Seq(
          EtmpVatReturnGoodsSupplied(
            msOfConsumption = "FR",
            vatRateType = EtmpVatRateType.ReducedVatRate,
            taxableAmountGBP = BigDecimal(23973.03),
            vatAmountGBP = BigDecimal(2397.30)
          ),
        ),
        totalVATGoodsSuppliedGBP = BigDecimal(4866.43),
        totalVATAmountPayable = BigDecimal(4866.43),
        totalVATAmountPayableAllSpplied = BigDecimal(4866.43),
        correctionPreviousVATReturn = Seq(
          EtmpVatReturnCorrection(
            periodKey = "23AJ",
            periodFrom = LocalDate.of(2023, 10, 1).toString,
            periodTo = LocalDate.of(2023, 10, 31).toString,
            msOfConsumption = "DE",
            totalVATAmountCorrectionGBP = BigDecimal(-1000.00),
            totalVATAmountCorrectionEUR = BigDecimal(-1100.41)
          ),
          EtmpVatReturnCorrection(
            periodKey = "23AJ",
            periodFrom = LocalDate.of(2023, 10, 1).toString,
            periodTo = LocalDate.of(2023, 10, 31).toString,
            msOfConsumption = "AT",
            totalVATAmountCorrectionGBP = BigDecimal(2000.00),
            totalVATAmountCorrectionEUR = BigDecimal(2100.41)
          )
        ),
        totalVATAmountFromCorrectionGBP = BigDecimal(1000.00),
        balanceOfVATDueForMS = Seq(
          EtmpVatReturnBalanceOfVatDue(
            msOfConsumption = "FR",
            totalVATDueGBP = BigDecimal(2397.30),
            totalVATEUR = BigDecimal(2397.30)
          )
        ),
        totalVATAmountDueForAllMSGBP = BigDecimal(11366.43),
        paymentReference = s"XI/IM9001234567/2023.M11"
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

    "Return OK for nilReturn scenario" in {

      val iossNumber = "IM9003333333"
      val period = Period(2023, Month.DECEMBER)

      val expectedResponse = nilReturn(iossNumber, period.toEtmpPeriodString)
      val fakeRequestWithBody = fakeRequest.withHeaders(validFakeHeaders)

      val result = controller.getVatReturn(iossNumber, period.toEtmpPeriodString)(fakeRequestWithBody)

      status(result) shouldBe Status.OK
      contentAsJson(result) shouldBe Json.toJson(expectedResponse)
    }

    "Return OK for salesToEuNoCorrectionsReturn scenario" in {
      val iossNumber = "IM9004444444"
      val period = Period(2023, Month.JANUARY)

      val expectedResponse = salesToEuNoCorrectionsReturn(iossNumber, period.toEtmpPeriodString)
      val fakeRequestWithBody = fakeRequest.withHeaders(validFakeHeaders)

      val result = controller.getVatReturn(iossNumber, period.toEtmpPeriodString)(fakeRequestWithBody)

      status(result) shouldBe Status.OK
      contentAsJson(result) shouldBe Json.toJson(expectedResponse)
    }

    "Return OK for salesToEuNoCorrectionsReturnPartial scenario" in {
      val iossNumber = "IM9005999777"
      val period = Period(2024, Month.JANUARY)

      val expectedResponse = salesToEuNoCorrectionsReturnPartial(
        iossNumber,
        period.toEtmpPeriodString,
        LocalDate.of(2024, 1, 15),
        LocalDate.of(2024, 1, 31)
      )
      val fakeRequestWithBody = fakeRequest.withHeaders(validFakeHeaders)

      val result = controller.getVatReturn(iossNumber, period.toEtmpPeriodString)(fakeRequestWithBody)

      status(result) shouldBe Status.OK
      contentAsJson(result) shouldBe Json.toJson(expectedResponse)
    }

    "Return OK for salesToEuWithPositiveCorrectionsReturn scenario" in {
      val iossNumber = "IM9005555555"
      val period = Period(2023, Month.FEBRUARY)

      val expectedResponse = salesToEuWithPositiveCorrectionsReturn(iossNumber, period.toEtmpPeriodString)
      val fakeRequestWithBody = fakeRequest.withHeaders(validFakeHeaders)

      val result = controller.getVatReturn(iossNumber, period.toEtmpPeriodString)(fakeRequestWithBody)

      status(result) shouldBe Status.OK
      contentAsJson(result) shouldBe Json.toJson(expectedResponse)
    }

    "Return OK for noSalesToEuWithPositiveAndNegativeCorrectionsReturn scenario" in {
      val iossNumber = "IM9006666666"
      val period = Period(2023, Month.MARCH)

      val expectedResponse = noSalesToEuWithPositiveAndNegativeCorrectionsReturn(iossNumber, period.toEtmpPeriodString)
      val fakeRequestWithBody = fakeRequest.withHeaders(validFakeHeaders)

      val result = controller.getVatReturn(iossNumber, period.toEtmpPeriodString)(fakeRequestWithBody)

      status(result) shouldBe Status.OK
      contentAsJson(result) shouldBe Json.toJson(expectedResponse)
    }

    "Return OK for correctionsScenarioOctoberReturn" in {
      val iossNumber = "IM9001233211"
      val period = "23AJ"

      val expectedResponse = correctionsScenarioOctoberReturn
      val fakeRequestWithBody = fakeRequest.withHeaders(validFakeHeaders)

      val result = controller.getVatReturn(iossNumber, period)(fakeRequestWithBody)

      status(result) shouldBe Status.OK
      contentAsJson(result) shouldBe Json.toJson(expectedResponse)
    }

    "Return OK for correctionsScenarioNovemberReturn" in {
      val iossNumber = "IM9001233211"
      val period = "23AK"

      val expectedResponse = correctionsScenarioNovemberReturn
      val fakeRequestWithBody = fakeRequest.withHeaders(validFakeHeaders)

      val result = controller.getVatReturn(iossNumber, period)(fakeRequestWithBody)

      status(result) shouldBe Status.OK
      contentAsJson(result) shouldBe Json.toJson(expectedResponse)
    }

    "Return OK for correctionsScenarioDecemberReturn" in {
      val iossNumber = "IM9001233211"
      val period = "23AL"

      val expectedResponse = correctionsScenarioDecemberReturn
      val fakeRequestWithBody = fakeRequest.withHeaders(validFakeHeaders)

      val result = controller.getVatReturn(iossNumber, period)(fakeRequestWithBody)

      status(result) shouldBe Status.OK
      contentAsJson(result) shouldBe Json.toJson(expectedResponse)
    }

    "Return OK for basicVatReturn scenario" in {
      val iossNumber = "IM9006230000"
      val period = Period(2023, Month.APRIL)

      val expectedResponse = basicVatReturn(iossNumber, period.toEtmpPeriodString)
      val fakeRequestWithBody = fakeRequest.withHeaders(validFakeHeaders)

      val result = controller.getVatReturn(iossNumber, period.toEtmpPeriodString)(fakeRequestWithBody)

      status(result) shouldBe Status.OK
      contentAsJson(result) shouldBe Json.toJson(expectedResponse)
    }

    "Return OK for standardVatReturn scenario" in {
      val iossNumber = "IM9009999999"
      val period = Period(2023, Month.MAY)

      val expectedResponse = standardVatReturn(iossNumber, period.toEtmpPeriodString)
      val fakeRequestWithBody = fakeRequest.withHeaders(validFakeHeaders)

      val result = controller.getVatReturn(iossNumber, period.toEtmpPeriodString)(fakeRequestWithBody)

      status(result) shouldBe Status.OK
      contentAsJson(result) shouldBe Json.toJson(expectedResponse)
    }

    "Return Bad Request when headers are missing" in {
      val fakeRequestWithBody = fakeRequest

      val result = controller.getVatReturn("IM9009999999", "23AK")(fakeRequestWithBody)

      status(result) shouldBe Status.BAD_REQUEST
    }
  }

  "GET /enterprise/obligation-data/{idType}/{idNumber}/{regimeType}" - {

    val idType = "IOSS"
    val regimeType = "IOSS"
    val obligationFulfilmentStatus = "O"
    val referenceNumber = "IM9001234567"
    val firstDateOfYear = LocalDate.of(2021, 1, 1)
    val lastDateOfYear = LocalDate.of(2021, 12, 31)
    val dateRange = ObligationsDateRange(firstDateOfYear, lastDateOfYear)

    val fakeRequest = FakeRequest(
      GET,
      routes.EtmpController.getObligations(
        idType = idType,
        idNumber = iossNumber,
        regimeType = regimeType,
        dateRange,
        status = Some(obligationFulfilmentStatus)
      ).url
    )

    "return a successful response" in {

      val successfulObligationsResponse = EtmpObligations(obligations = Seq(EtmpObligation(
        obligationDetails = Seq(
          EtmpObligationDetails(
            status = EtmpObligationsFulfilmentStatus.Open,
            periodKey = "23AL"
          ),
          EtmpObligationDetails(
            status = EtmpObligationsFulfilmentStatus.Fulfilled,
            periodKey = "23AK"
          ),
          EtmpObligationDetails(
            status = EtmpObligationsFulfilmentStatus.Fulfilled,
            periodKey = "23AJ"
          )
        )
      )))

      val fakeRequestWithBody = fakeRequest.withHeaders(validFakeHeaders)

      val result = controller
        .getObligations(
          idType = idType,
          idNumber = referenceNumber,
          regimeType = regimeType,
          dateRange,
          status = Some(obligationFulfilmentStatus)
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
          status = Some(obligationFulfilmentStatus)
        )(fakeRequestWithBody)

      status(result) shouldBe Status.BAD_REQUEST
    }

    "Return OK for default obligations when idNumber does not match any known case" in {
      val idNumber = "IM1234567890"
      val expectedResponse = StubData.defaultObligationsResponse
      val fakeRequestWithBody = fakeRequest.withHeaders(validFakeHeaders)

      val result = controller.getObligations(idType, idNumber, regimeType, dateRange, status = None)(fakeRequestWithBody)

      status(result) shouldBe Status.OK
      contentAsJson(result) shouldBe Json.toJson(expectedResponse)
    }

    "Return OK for multipleCorrectionPeriods when idNumber matches one of the cases" in {
      val idNumber = "IM9001234568"
      val expectedResponse = StubData.multipleCorrectionPeriods
      val fakeRequestWithBody = fakeRequest.withHeaders(validFakeHeaders)

      val result = controller.getObligations(idType, idNumber, regimeType, dateRange, status = None)(fakeRequestWithBody)

      status(result) shouldBe Status.OK
      contentAsJson(result) shouldBe Json.toJson(expectedResponse)
    }

    "Return OK for multipleCorrectionPeriodYears when idNumber matches 'IM9001234569'" in {
      val idNumber = "IM9001234569"
      val expectedResponse = StubData.multipleCorrectionPeriodYears
      val fakeRequestWithBody = fakeRequest.withHeaders(validFakeHeaders)

      val result = controller.getObligations(idType, idNumber, regimeType, dateRange, status = None)(fakeRequestWithBody)

      status(result) shouldBe Status.OK
      contentAsJson(result) shouldBe Json.toJson(expectedResponse)
    }

    "Return OK for moreThanThreeCorrectionPeriodYears when idNumber matches 'IM9001234999'" in {
      val idNumber = "IM9001234999"
      val expectedResponse = StubData.moreThanThreeCorrectionPeriodYears
      val fakeRequestWithBody = fakeRequest.withHeaders(validFakeHeaders)

      val result = controller.getObligations(idType, idNumber, regimeType, dateRange, status = None)(fakeRequestWithBody)

      status(result) shouldBe Status.OK
      contentAsJson(result) shouldBe Json.toJson(expectedResponse)
    }

    "Return OK for moreThanThreeYearsOpenReturns when idNumber matches 'IM9001239999'" in {
      val idNumber = "IM9001239999"
      val expectedResponse = StubData.moreThanThreeYearsOpenReturns
      val fakeRequestWithBody = fakeRequest.withHeaders(validFakeHeaders)

      val result = controller.getObligations(idType, idNumber, regimeType, dateRange, status = None)(fakeRequestWithBody)

      status(result) shouldBe Status.OK
      contentAsJson(result) shouldBe Json.toJson(expectedResponse)
    }

    "Return OK for moreThanSixYearsFulfilledReturns when idNumber matches 'IM9001236666'" in {
      val idNumber = "IM9001236666"
      val expectedResponse = StubData.moreThanSixYearsFulfilledReturns
      val fakeRequestWithBody = fakeRequest.withHeaders(validFakeHeaders)

      val result = controller.getObligations(idType, idNumber, regimeType, dateRange, status = None)(fakeRequestWithBody)

      status(result) shouldBe Status.OK
      contentAsJson(result) shouldBe Json.toJson(expectedResponse)
    }

    "Return OK for singleCorrectionPeriods when idNumber matches 'IM9008888888'" in {
      val idNumber = "IM9008888888"
      val expectedResponse = StubData.singleCorrectionPeriods
      val fakeRequestWithBody = fakeRequest.withHeaders(validFakeHeaders)

      val result = controller.getObligations(idType, idNumber, regimeType, dateRange, status = None)(fakeRequestWithBody)

      status(result) shouldBe Status.OK
      contentAsJson(result) shouldBe Json.toJson(expectedResponse)
    }

    "Return OK for firstPeriodNoCorrections  when idNumber matches 'IM9009999888'" in {
      val idNumber = "IM9009999888"
      val expectedResponse = StubData.firstPeriodNoCorrections
      val fakeRequestWithBody = fakeRequest.withHeaders(validFakeHeaders)

      val result = controller.getObligations(idType, idNumber, regimeType, dateRange, status = None)(fakeRequestWithBody)

      status(result) shouldBe Status.OK
      contentAsJson(result) shouldBe Json.toJson(expectedResponse)
    }

    "Return OK for previousThreeMonthsSubmittedPeriods when idNumber matches 'IM9008888886'" in {
      val idNumber = "IM9008888886"
      val expectedResponse = StubData.previousThreeMonthsSubmittedPeriods
      val fakeRequestWithBody = fakeRequest.withHeaders(validFakeHeaders)

      val result = controller.getObligations(idType, idNumber, regimeType, dateRange, status = None)(fakeRequestWithBody)

      status(result) shouldBe Status.OK
      contentAsJson(result) shouldBe Json.toJson(expectedResponse)
    }

    "Return OK for previousFourToSixMonthsSubmittedPeriods when idNumber matches 'IM9006230000'" in {
      val idNumber = "IM9006230000"
      val expectedResponse = StubData.previousFourToSixMonthsSubmittedPeriods
      val fakeRequestWithBody = fakeRequest.withHeaders(validFakeHeaders)

      val result = controller.getObligations(idType, idNumber, regimeType, dateRange, status = None)(fakeRequestWithBody)

      status(result) shouldBe Status.OK
      contentAsJson(result) shouldBe Json.toJson(expectedResponse)
    }

    "Return OK for previousSevenToNineMonthsSubmittedPeriods when idNumber matches 'IM9007230001'" in {
      val idNumber = "IM9007230001"
      val expectedResponse = StubData.previousSevenToNineMonthsSubmittedPeriods
      val fakeRequestWithBody = fakeRequest.withHeaders(validFakeHeaders)

      val result = controller.getObligations(idType, idNumber, regimeType, dateRange, status = None)(fakeRequestWithBody)

      status(result) shouldBe Status.OK
      contentAsJson(result) shouldBe Json.toJson(expectedResponse)
    }

    "Return OK for sixMonthsAcrossTwoYearsSubmittedPeriods when idNumber matches 'IM9008888882'" in {
      val idNumber = "IM9008888882"
      val expectedResponse = StubData.sixMonthsAcrossTwoYearsSubmittedPeriods
      val fakeRequestWithBody = fakeRequest.withHeaders(validFakeHeaders)

      val result = controller.getObligations(idType, idNumber, regimeType, dateRange, status = None)(fakeRequestWithBody)

      status(result) shouldBe Status.OK
      contentAsJson(result) shouldBe Json.toJson(expectedResponse)
    }

    "Return OK for previousMonthSubmittedPeriod when idNumber matches 'IM9008888885'" in {
      val idNumber = "IM9008888885"
      val expectedResponse = StubData.previousMonthSubmittedPeriod
      val fakeRequestWithBody = fakeRequest.withHeaders(validFakeHeaders)

      val result = controller.getObligations(idType, idNumber, regimeType, dateRange, status = None)(fakeRequestWithBody)

      status(result) shouldBe Status.OK
      contentAsJson(result) shouldBe Json.toJson(expectedResponse)
    }

    "Return OK for firstSubmittedReturnAfterTransferringFromAnotherMSID when idNumber matches 'IM9005999777'" in {
      val idNumber = "IM9005999777"
      val expectedResponse = StubData.firstSubmittedReturnAfterTransferringFromAnotherMSID
      val fakeRequestWithBody = fakeRequest.withHeaders(validFakeHeaders)

      val result = controller.getObligations(idType, idNumber, regimeType, dateRange, status = None)(fakeRequestWithBody)

      status(result) shouldBe Status.OK
      contentAsJson(result) shouldBe Json.toJson(expectedResponse)
    }

    "Return OK for firstReturnAfterTransferringFromAnotherMSID when idNumber matches 'IM9005999997'" in {
      val idNumber = "IM9005999997"
      val expectedResponse = StubData.firstReturnAfterTransferringFromAnotherMSID
      val fakeRequestWithBody = fakeRequest.withHeaders(validFakeHeaders)

      val result = controller.getObligations(idType, idNumber, regimeType, dateRange, status = None)(fakeRequestWithBody)

      status(result) shouldBe Status.OK
      contentAsJson(result) shouldBe Json.toJson(expectedResponse)
    }

    "Return OK for secondReturnAfterTransferringFromAnotherMSID when idNumber matches 'IM9005999977'" in {
      val idNumber = "IM9005999977"
      val expectedResponse = StubData.secondReturnAfterTransferringFromAnotherMSID
      val fakeRequestWithBody = fakeRequest.withHeaders(validFakeHeaders)

      val result = controller.getObligations(idType, idNumber, regimeType, dateRange, status = None)(fakeRequestWithBody)

      status(result) shouldBe Status.OK
      contentAsJson(result) shouldBe Json.toJson(expectedResponse)
    }

    "Return OK for returnsSubmittedBeforeTransferringToAnotherMSID when idNumber matches 'IM9009955555'" in {
      val idNumber = "IM9009955555"
      val expectedResponse = StubData.returnsSubmittedBeforeTransferringToAnotherMSID
      val fakeRequestWithBody = fakeRequest.withHeaders(validFakeHeaders)

      val result = controller.getObligations(idType, idNumber, regimeType, dateRange, status = None)(fakeRequestWithBody)

      status(result) shouldBe Status.OK
      contentAsJson(result) shouldBe Json.toJson(expectedResponse)
    }

    "Return OK for returnsBeforeTransferringToAnotherMSID when idNumber matches 'IM9009999555'" in {
      val idNumber = "IM9009999555"
      val expectedResponse = StubData.returnsBeforeTransferringToAnotherMSID
      val fakeRequestWithBody = fakeRequest.withHeaders(validFakeHeaders)

      val result = controller.getObligations(idType, idNumber, regimeType, dateRange, status = None)(fakeRequestWithBody)

      status(result) shouldBe Status.OK
      contentAsJson(result) shouldBe Json.toJson(expectedResponse)
    }

    "Return OK for returnsBeforeTransferringToAnotherMSIDTwoOpen when idNumber matches 'IM9009995555'" in {
      val idNumber = "IM9009995555"
      val expectedResponse = StubData.returnsBeforeTransferringToAnotherMSIDTwoOpen
      val fakeRequestWithBody = fakeRequest.withHeaders(validFakeHeaders)

      val result = controller.getObligations(idType, idNumber, regimeType, dateRange, status = None)(fakeRequestWithBody)

      status(result) shouldBe Status.OK
      contentAsJson(result) shouldBe Json.toJson(expectedResponse)
    }

    "Return OK for periodsBeforeQuarantine when idNumber matches 'IM9002999993'" in {
      val idNumber = "IM9002999993"
      val expectedResponse = StubData.periodsBeforeQuarantine
      val fakeRequestWithBody = fakeRequest.withHeaders(validFakeHeaders)

      val result = controller.getObligations(idType, idNumber, regimeType, dateRange, status = None)(fakeRequestWithBody)

      status(result) shouldBe Status.OK
      contentAsJson(result) shouldBe Json.toJson(expectedResponse)
    }

    "Return OK for periodBeforeCurrentQuarantine when idNumber matches 'IM9003999993'" in {
      val idNumber = "IM9003999993"
      val expectedResponse = StubData.periodBeforeCurrentQuarantine
      val fakeRequestWithBody = fakeRequest.withHeaders(validFakeHeaders)

      val result = controller.getObligations(idType, idNumber, regimeType, dateRange, status = None)(fakeRequestWithBody)

      status(result) shouldBe Status.OK
      contentAsJson(result) shouldBe Json.toJson(expectedResponse)
    }
  }

  "GET /vec/iossreturns/returncorrection/v1/{IOSSReference}/{MSCON}{PeriodKey}" - {

    val fakeRequest = FakeRequest(POST, routes.EtmpController.getReturnCorrection(iossNumber, country, period.toEtmpPeriodString).url)

    val etmpReturnCorrectionValue: EtmpReturnCorrectionValue =
      EtmpReturnCorrectionValue(
        maximumCorrectionValue = BigDecimal(2469.13)
      )

    "must return OK with a successful payload" in {

      val fakeRequestWithBody = fakeRequest.withHeaders(validFakeHeaders)

      val result = controller.getReturnCorrection(iossNumber, country, period.toEtmpPeriodString)(fakeRequestWithBody)

      status(result) shouldBe Status.OK
      contentAsJson(result) shouldBe Json.toJson(etmpReturnCorrectionValue)
    }

    "must return Bad Request when headers are missing" in {

      val fakeRequestWithBody = fakeRequest

      val result = controller.getReturnCorrection(iossNumber, country, period.toEtmpPeriodString)(fakeRequestWithBody)

      status(result) shouldBe Status.BAD_REQUEST
    }

    "must return OK with correct maximumCorrectionValue for various (iossNumber, country, period)" in {
      val testCases = Seq(
        ("IM9001234567", "DE", "23AJ", BigDecimal(1469.13)),
        ("IM9001234567", "DE", "23AK", BigDecimal(2469.13)),
        ("IM9001234567", "FR", "23AJ", BigDecimal(1397.30)),
        ("IM9001234567", "FR", "23AK", BigDecimal(2397.30)),
        ("IM9001233211", "DE", "23AJ", BigDecimal(3500.00)),
        ("IM9001233211", "FR", "23AJ", BigDecimal(4500.00)),
        ("IM9001234569", "DE", "23AL", BigDecimal(2469.13)),
        ("IM9001234569", "FR", "22AL", BigDecimal(2397.30)),
      )

      testCases.foreach { case (iossNumber, country, period, expectedValue) =>
        val fakeRequestWithBody = fakeRequest.withHeaders(validFakeHeaders)
        val etmpReturnCorrectionValue = EtmpReturnCorrectionValue(maximumCorrectionValue = expectedValue)

        val result = controller.getReturnCorrection(iossNumber, country, period)(fakeRequestWithBody)

        status(result) shouldBe Status.OK
        contentAsJson(result) shouldBe Json.toJson(etmpReturnCorrectionValue)
      }
    }
  }
}
