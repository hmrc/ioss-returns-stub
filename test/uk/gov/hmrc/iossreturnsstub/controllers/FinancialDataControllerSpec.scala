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

  private val stubClock: Clock = Clock.fixed(LocalDate.now.atStartOfDay(ZoneId.systemDefault).toInstant, ZoneId.systemDefault)
  private val controller = new FinancialDataController(Helpers.stubControllerComponents())

  private val successfulResponse = FinancialDataResponse(
    idType = None,
    idNumber = None,
    regimeType = None,
    processingDate = ZonedDateTime.now(stubClock),
    financialTransactions = Some(StubData.financialTransactions)
  )

  "GET /financial-data" should {
    "return a successful FinancialDataResponse" in {
      val dateRange = DateRange(LocalDate.of(2023, 1, 1), LocalDate.of(2023, 12, 31))
      val fakeRequest = FakeRequest("GET", uk.gov.hmrc.iossreturnsstub.controllers.routes.FinancialDataController.getFinancialData("", "0", "", dateRange).url)

      successfulResponse.copy(financialTransactions = Some(StubData.financialTransactions))

      val result = controller.getFinancialData(idType = "", idNumber = "012345678", regimeType = "", dateRange = dateRange)(fakeRequest)
      status(result) shouldBe Status.OK
      contentAsJson(result).validate[FinancialDataResponse] shouldBe JsSuccess(successfulResponse)
    }

  }
}
