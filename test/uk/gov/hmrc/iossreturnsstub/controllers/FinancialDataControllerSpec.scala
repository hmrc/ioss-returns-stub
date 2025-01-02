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
import play.api.test.Helpers.*
import play.api.test.{FakeRequest, Helpers}
import uk.gov.hmrc.iossreturnsstub.controllers.StubData.*
import uk.gov.hmrc.iossreturnsstub.models.*

import java.time.*

class FinancialDataControllerSpec extends AnyWordSpec with Matchers {

  private val stubClock: Clock = Clock.fixed(LocalDate.now.atStartOfDay(ZoneId.systemDefault).toInstant, ZoneId.systemDefault)
  private val controller = new FinancialDataController(Helpers.stubControllerComponents())

  private val successfulResponse = FinancialDataResponse(
    idType = None,
    idNumber = None,
    regimeType = None,
    processingDate = ZonedDateTime.now(stubClock),
    financialTransactions = Some(StubData.financialTransactions)
  )

  private  val dateRange = DateRange(LocalDate.of(2023, 1, 1), LocalDate.of(2023, 12, 31))

  "GET /financial-data" should {
    "return a successful FinancialDataResponse" in {

      val fakeRequest = FakeRequest("GET", uk.gov.hmrc.iossreturnsstub.controllers.routes.FinancialDataController.getFinancialData("", "0", "", dateRange).url)

      successfulResponse.copy(financialTransactions = Some(StubData.financialTransactions))

      val result = controller.getFinancialData(idType = "", idNumber = "012345678", regimeType = "", dateRange = dateRange)(fakeRequest)
      status(result) shouldBe Status.OK
      contentAsJson(result).validate[FinancialDataResponse] shouldBe JsSuccess(successfulResponse)
    }

    "return Ok with empty financial transactions for 'IM9009999888'" in {
      val fakeRequest = FakeRequest("GET", uk.gov.hmrc.iossreturnsstub.controllers.routes.FinancialDataController.getFinancialData("", "IM9009999888", "", dateRange).url)

      val result = controller.getFinancialData(idType = "", idNumber = "IM9009999888", regimeType = "", dateRange = dateRange)(fakeRequest)
      status(result) shouldBe Status.OK
      contentAsJson(result).validate[FinancialDataResponse] shouldBe JsSuccess(successfulResponse.copy(financialTransactions = Some(Seq.empty)))
    }

    "return Ok with empty financial transactions for 'IM9003333333'" in {
      val fakeRequest = FakeRequest("GET", uk.gov.hmrc.iossreturnsstub.controllers.routes.FinancialDataController.getFinancialData("", "IM9003333333", "", dateRange).url)

      val result = controller.getFinancialData(idType = "", idNumber = "IM9003333333", regimeType = "", dateRange = dateRange)(fakeRequest)
      status(result) shouldBe Status.OK
      contentAsJson(result).validate[FinancialDataResponse] shouldBe JsSuccess(successfulResponse.copy(financialTransactions = Some(Seq.empty)))
    }

    "return Ok with a single outstanding payment for 'IM9008888888'" in {
      val fakeRequest = FakeRequest("GET", uk.gov.hmrc.iossreturnsstub.controllers.routes.FinancialDataController.getFinancialData("", "IM9008888888", "", dateRange).url)

      val result = controller.getFinancialData(idType = "", idNumber = "IM9008888888", regimeType = "", dateRange = dateRange)(fakeRequest)
      status(result) shouldBe Status.OK
      contentAsJson(result).validate[FinancialDataResponse] shouldBe JsSuccess(successfulResponse.copy(financialTransactions = Some(singleOutstandingPayment)))
    }

    "return Ok with 6 returns outstanding for 'IM9001238999'" in {
      val dateRange = DateRange(LocalDate.of(2020, 1, 1), LocalDate.of(2020, 12, 31))
      val fakeRequest = FakeRequest("GET", uk.gov.hmrc.iossreturnsstub.controllers.routes.FinancialDataController.getFinancialData("", "IM9001238999", "", dateRange).url)

      val result = controller.getFinancialData(idType = "", idNumber = "IM9001238999", regimeType = "", dateRange = dateRange)(fakeRequest)
      status(result) shouldBe Status.OK
      contentAsJson(result).validate[FinancialDataResponse] shouldBe JsSuccess(successfulResponse.copy(financialTransactions = Some(sixReturnsOutstanding)))
    }

    "return Ok with Single return, fully paid for 'IM9008888887'" in {
      val fakeRequest = FakeRequest("GET", uk.gov.hmrc.iossreturnsstub.controllers.routes.FinancialDataController.getFinancialData("", "IM9008888887", "", dateRange).url)

      val result = controller.getFinancialData(idType = "", idNumber = "IM9008888887", regimeType = "", dateRange = dateRange)(fakeRequest)
      status(result) shouldBe Status.OK
      contentAsJson(result).validate[FinancialDataResponse] shouldBe JsSuccess(successfulResponse.copy(financialTransactions = Some(singleReturnFullyPaid)))

    }

    "return Ok with Three returns submitted, one due, one overdue and one paid for 'IM9008888886'" in {
      val fakeRequest = FakeRequest("GET", uk.gov.hmrc.iossreturnsstub.controllers.routes.FinancialDataController.getFinancialData("", "IM9008888886", "", dateRange).url)

      val result = controller.getFinancialData(idType = "", idNumber = "IM9008888886", regimeType = "", dateRange = dateRange)(fakeRequest)
      status(result) shouldBe Status.OK
      contentAsJson(result).validate[FinancialDataResponse] shouldBe JsSuccess(successfulResponse.copy(financialTransactions = Some(threeReturnsTwoOutstandingOnePaid)))

    }

    "return Ok with One return submitted that's due payment for 'IM9008888885'" in {
      val fakeRequest = FakeRequest("GET", uk.gov.hmrc.iossreturnsstub.controllers.routes.FinancialDataController.getFinancialData("", "IM9008888885", "", dateRange).url)

      val result = controller.getFinancialData(idType = "", idNumber = "IM9008888885", regimeType = "", dateRange = dateRange)(fakeRequest)
      status(result) shouldBe Status.OK
      contentAsJson(result).validate[FinancialDataResponse] shouldBe JsSuccess(successfulResponse.copy(financialTransactions = Some(oneReturnWithOutstanding)))

    }

    "return Ok with Three returns submitted, one due, one overdue. One fully paid, one partial and one unpaid for 'IM9008888884'" in {
      val fakeRequest = FakeRequest("GET", uk.gov.hmrc.iossreturnsstub.controllers.routes.FinancialDataController.getFinancialData("", "IM9008888884", "", dateRange).url)

      val result = controller.getFinancialData(idType = "", idNumber = "IM9008888884", regimeType = "", dateRange = dateRange)(fakeRequest)
      status(result) shouldBe Status.OK
      contentAsJson(result).validate[FinancialDataResponse] shouldBe JsSuccess(successfulResponse.copy(financialTransactions = Some(threeReturnsOnePartialOneUnpaidOnePaid)))

    }

    "return Error (not found) with payments API for 'IM9001231231'" in {
      val fakeRequest = FakeRequest("GET", uk.gov.hmrc.iossreturnsstub.controllers.routes.FinancialDataController.getFinancialData("", "IM9001231231", "", dateRange).url)

      val result = controller.getFinancialData(idType = "", idNumber = "IM9001231231", regimeType = "", dateRange = dateRange)(fakeRequest)
      status(result) shouldBe Status.NOT_FOUND

    }

    "return Error (service unavailable) with payments API for 'IM9001231232'" in {
      val fakeRequest = FakeRequest("GET", uk.gov.hmrc.iossreturnsstub.controllers.routes.FinancialDataController.getFinancialData("", "IM9001231232", "", dateRange).url)

      val result = controller.getFinancialData(idType = "", idNumber = "IM9001231232", regimeType = "", dateRange = dateRange)(fakeRequest)
      status(result) shouldBe Status.SERVICE_UNAVAILABLE

    }

    "return OK with Three returns submitted, one due, one overdue. One fully paid, one partial and one unpaid for 'IM9008888883'" in {
      val fakeRequest = FakeRequest("GET", uk.gov.hmrc.iossreturnsstub.controllers.routes.FinancialDataController.getFinancialData("", "IM9008888883", "", dateRange).url)

      val result = controller.getFinancialData(idType = "", idNumber = "IM9008888883", regimeType = "", dateRange = dateRange)(fakeRequest)
      status(result) shouldBe Status.OK
      contentAsJson(result).validate[FinancialDataResponse] shouldBe JsSuccess(successfulResponse.copy(financialTransactions = Some(threeReturnsOneUnknownOneUnpaidOnePaid)))

    }

    "return OK with Three returns, partially paid for previous registration for 'IM9006230000'" in {
      val fakeRequest = FakeRequest("GET", uk.gov.hmrc.iossreturnsstub.controllers.routes.FinancialDataController.getFinancialData("", "IM9006230000", "", dateRange).url)

      val result = controller.getFinancialData(idType = "", idNumber = "IM9006230000", regimeType = "", dateRange = dateRange)(fakeRequest)
      status(result) shouldBe Status.OK
      contentAsJson(result).validate[FinancialDataResponse] shouldBe JsSuccess(successfulResponse.copy(financialTransactions = Some(threeReturnsOutstandingPreviousRegistration)))

    }

    "return OK with Three returns, partially paid and paid for oldest multiple previous registration for 'IM9007230001'" in {
      val fakeRequest = FakeRequest("GET", uk.gov.hmrc.iossreturnsstub.controllers.routes.FinancialDataController.getFinancialData("", "IM9007230001", "", dateRange).url)

      val result = controller.getFinancialData(idType = "", idNumber = "IM9007230001", regimeType = "", dateRange = dateRange)(fakeRequest)
      status(result) shouldBe Status.OK
      contentAsJson(result).validate[FinancialDataResponse] shouldBe JsSuccess(successfulResponse.copy(financialTransactions = Some(threeReturnsTwoOutstandingOnePaidPreviousRegistration)))

    }

    "return OK with Three returns, one outstanding, two paid for previous registration for 'IM9004230000'" in {
      val fakeRequest = FakeRequest("GET", uk.gov.hmrc.iossreturnsstub.controllers.routes.FinancialDataController.getFinancialData("", "IM9004230000", "", dateRange).url)

      val result = controller.getFinancialData(idType = "", idNumber = "IM9004230000", regimeType = "", dateRange = dateRange)(fakeRequest)
      status(result) shouldBe Status.OK
      contentAsJson(result).validate[FinancialDataResponse] shouldBe JsSuccess(successfulResponse.copy(financialTransactions = Some(threeReturnsOneOutstandingTwoPaidPreviousRegistration)))

    }

    "return OK with Three returns, one outstanding, two paid for older previous registration for 'IM9007230004'" in {
      val fakeRequest = FakeRequest("GET", uk.gov.hmrc.iossreturnsstub.controllers.routes.FinancialDataController.getFinancialData("", "IM9007230004", "", dateRange).url)

      val result = controller.getFinancialData(idType = "", idNumber = "IM9007230004", regimeType = "", dateRange = dateRange)(fakeRequest)
      status(result) shouldBe Status.OK
      contentAsJson(result).validate[FinancialDataResponse] shouldBe JsSuccess(successfulResponse.copy(financialTransactions = Some(threeReturnsOneOutstandingTwoPaidOlderPreviousRegistration)))

    }
  }
}
