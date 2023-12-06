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

import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import play.api.http.Status
import play.api.libs.json.JsSuccess
import play.api.test.Helpers._
import play.api.test.{FakeRequest, Helpers}
import uk.gov.hmrc.iossreturnsstub.models._

import java.time._

class FinancialDataControllerSpec extends AnyWordSpec with Matchers {

  private val firstDateOfYear = LocalDate.of(2021, 1, 1)
  private val lastDateOfYear = LocalDate.of(2021, 12, 31)
  private val dateRange = DateRange(firstDateOfYear, lastDateOfYear)
  private val fakeRequest = FakeRequest("GET", uk.gov.hmrc.iossreturnsstub.controllers.routes.FinancialDataController.getFinancialData("", "0", "", dateRange).url)
  private val stubClock: Clock = Clock.fixed(LocalDate.now.atStartOfDay(ZoneId.systemDefault).toInstant, ZoneId.systemDefault)
  private val controller = new FinancialDataController(Helpers.stubControllerComponents())
  private val firstDay = LocalDate.of(2021, Month.JULY, 1)
  private val lastDay = LocalDate.of(2021, Month.SEPTEMBER, 30)

  private val items = Seq(
    Item(
      amount = Some(BigDecimal(1000)),
      clearingReason = Some("01"),
      paymentReference = Some("a"),
      paymentAmount = Some(BigDecimal(500)),
      paymentMethod = Some("A")
    )
  )

  private val financialTransactions = Seq(
    FinancialTransaction(
      chargeType = Some("G Ret FR EU-OMS"),
      mainType = None,
      taxPeriodFrom = Some(firstDay),
      taxPeriodTo = Some(lastDay),
      originalAmount = Some(BigDecimal(1500)),
      outstandingAmount = Some(BigDecimal(500)),
      clearedAmount = Some(BigDecimal(1000)),
      items = Some(items)
    ))

  private val somePaidItems = Seq(
    Item(
      amount = Some(BigDecimal(500)),
      clearingReason = Some("01"),
      paymentReference = Some("a"),
      paymentAmount = Some(BigDecimal(500)),
      paymentMethod = Some("A")
    )
  )
  private val somePaidFinancialTransactions = Seq(
    FinancialTransaction(
      chargeType = Some("G Ret FR EU-OMS"),
      mainType = None,
      taxPeriodFrom = Some(firstDay),
      taxPeriodTo = Some(lastDay),
      originalAmount = Some(BigDecimal(1500)),
      outstandingAmount = Some(BigDecimal(1000)),
      clearedAmount = Some(BigDecimal(500)),
      items = Some(somePaidItems)
    )
  )

  private val successfulResponse = FinancialDataResponse(
    idType = None,
    idNumber = None,
    regimeType = None,
    processingDate = ZonedDateTime.now(stubClock),
    financialTransactions = Some(financialTransactions)
  )

  "GET /financial-data" should {
    "return a successful FinancialDataResponse" in {
      val result = controller.getFinancialData(idType = "", idNumber = "012345678", regimeType = "", dateRange = dateRange)(fakeRequest)
      status(result) shouldBe Status.OK
      contentAsJson(result).validate[FinancialDataResponse] shouldBe JsSuccess(successfulResponse)
    }

    "return a all paid when vat number starts with 1" in {
      val allPaidItems = Seq(
        Item(
          amount = Some(BigDecimal(1500)),
          clearingReason = Some("01"),
          paymentReference = Some("a"),
          paymentAmount = Some(BigDecimal(1500)),
          paymentMethod = Some("A")
        )
      )
      val allPaidFinancialTransactions = Seq(
        FinancialTransaction(
          chargeType = Some("G Ret FR EU-OMS"),
          mainType = None,
          taxPeriodFrom = Some(firstDay),
          taxPeriodTo = Some(lastDay),
          originalAmount = Some(BigDecimal(1500)),
          outstandingAmount = Some(BigDecimal(0)),
          clearedAmount = Some(BigDecimal(1500)),
          items = Some(allPaidItems)
        )
      )

      val result = controller.getFinancialData(idType = "", idNumber = "123456789", regimeType = "", dateRange = dateRange)(fakeRequest)
      status(result) shouldBe Status.OK
      contentAsJson(result).validate[FinancialDataResponse] shouldBe
        JsSuccess(successfulResponse.copy(
          financialTransactions = Some(allPaidFinancialTransactions)))
    }

    "return a all paid when vat number starts with 2" in {

      val result = controller.getFinancialData(idType = "", idNumber = "234567890", regimeType = "", dateRange = dateRange)(fakeRequest)
      status(result) shouldBe Status.OK
      contentAsJson(result).validate[FinancialDataResponse] shouldBe
        JsSuccess(successfulResponse.copy(
          financialTransactions = Some(somePaidFinancialTransactions)))
    }

    "return a not paid when vat number starts with 3" in {

      val notPaidFinancialTransactions = Seq(
        FinancialTransaction(
          chargeType = Some("G Ret FR EU-OMS"),
          mainType = None,
          taxPeriodFrom = Some(firstDay),
          taxPeriodTo = Some(lastDay),
          originalAmount = Some(BigDecimal(1500)),
          outstandingAmount = Some(BigDecimal(1500)),
          clearedAmount = Some(BigDecimal(0)),
          items = None
        )
      )

      val result = controller.getFinancialData(idType = "", idNumber = "345678900", regimeType = "", dateRange = dateRange)(fakeRequest)
      status(result) shouldBe Status.OK
      contentAsJson(result).validate[FinancialDataResponse] shouldBe
        JsSuccess(successfulResponse.copy(
          financialTransactions = Some(notPaidFinancialTransactions)))
    }

    "return empty for wrong year" in {

      val result = controller.getFinancialData(idType = "", idNumber = "345678900", regimeType = "", dateRange = DateRange(LocalDate.of(2022, 1, 1), LocalDate.of(2022, 12, 31)))(fakeRequest)
      status(result) shouldBe Status.OK
      contentAsJson(result).validate[FinancialDataResponse] shouldBe
        JsSuccess(successfulResponse.copy(
          financialTransactions = Some(Seq.empty)))
    }

    "return a no vat owed when vat number starts with 5" in {

      val result = controller.getFinancialData(idType = "", idNumber = "545678900", regimeType = "", dateRange = dateRange)(fakeRequest)
      status(result) shouldBe Status.NOT_FOUND
    }
  }
}
