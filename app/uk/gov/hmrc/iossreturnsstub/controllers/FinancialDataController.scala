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

import play.api.libs.json.Json
import play.api.mvc.{Action, AnyContent, ControllerComponents}
import uk.gov.hmrc.iossreturnsstub.controllers.StubData._
import uk.gov.hmrc.iossreturnsstub.models._
import uk.gov.hmrc.play.bootstrap.backend.controller.BackendController

import java.time.{Clock, LocalDate, ZoneId, ZonedDateTime}
import javax.inject.{Inject, Singleton}
import scala.concurrent.Future

@Singleton()
class FinancialDataController @Inject()(
                                         cc: ControllerComponents
                                       )
  extends BackendController(cc) {

  private val stubClock: Clock = Clock.fixed(LocalDate.now.atStartOfDay(ZoneId.systemDefault).toInstant, ZoneId.systemDefault)

  val successfulResponse = FinancialDataResponse(
    idType = None,
    idNumber = None,
    regimeType = None,
    processingDate = ZonedDateTime.now(stubClock),
    financialTransactions = Some(StubData.financialTransactions)
  )

  def getFinancialData(idType: String, idNumber: String, regimeType: String, dateRange: DateRange): Action[AnyContent] = Action.async {
    val (responseStatus, maybeFinancialTransactions) = idNumber match {
      case "IM9009999888" => (Ok, Some(Seq.empty)) //Single return, not submitted yet therefore no payments
      case "IM9003333333" => (Ok, Some(Seq.empty)) //Nil return so no payments required
      case "IM9008888888" => (Ok, Some(singleOutstandingPayment)) //Single return, partially paid
      case "IM9008888887" => (Ok, Some(singleReturnFullyPaid)) //Single return, fully paid
      case "IM9008888886" => (Ok, Some(threeReturnsTwoOutstandingOnePaid)) //Three returns submitted, one due, one overdue and one paid
      case "IM9008888885" => (Ok, Some(oneReturnWithOutstanding)) //One return submitted that's due payment
      case "IM9008888884" => (Ok, Some(threeReturnsOnePartialOneUnpaidOnePaid)) //Three returns submitted, one due, one overdue. One fully paid, one partial and one unpaid
      case "IM9001231231" => (NotFound, None) //Error (not found) with payments API
      case "IM9001231232" => (ServiceUnavailable, None) //Error (service unavailable) with payments API
      case "IM9008888883" => (Ok, Some(threeReturnsOneUnknownOneUnpaidOnePaid)) //Three returns submitted, one due, one overdue. One fully paid, one partial and one unpaid
      case _ => (Ok, successfulResponse.financialTransactions) //Two returns, both with outstanding payments
    }

    val filteredFinancialTransactions = maybeFinancialTransactions.map { financialTransactions =>
      financialTransactions.filter { financialTransaction =>
        financialTransaction.taxPeriodFrom.exists(tpf => tpf.isAfter(dateRange.fromDate) || tpf.isEqual(dateRange.fromDate))
      }
    }
    val response = successfulResponse.copy(financialTransactions = filteredFinancialTransactions)

    Future.successful(responseStatus match {
      case Ok => Ok(Json.toJson(response))
      case status => status
    })
  }
}
