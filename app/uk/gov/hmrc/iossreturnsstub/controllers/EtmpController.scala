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

import play.api.Logging
import play.api.libs.json.Json
import play.api.mvc._
import uk.gov.hmrc.iossreturnsstub.models.ObligationsDateRange
import uk.gov.hmrc.iossreturnsstub.models.etmp._
import uk.gov.hmrc.iossreturnsstub.utils.FutureSyntax.FutureOps
import uk.gov.hmrc.iossreturnsstub.utils.JsonSchemaHelper
import uk.gov.hmrc.iossreturnsstub.utils.ReturnData._
import uk.gov.hmrc.play.bootstrap.backend.controller.BackendController

import javax.inject.{Inject, Singleton}
import scala.concurrent.ExecutionContext

@Singleton()
class EtmpController @Inject()(
                                cc: ControllerComponents,
                                jsonSchemaHelper: JsonSchemaHelper
                              )
  extends BackendController(cc) with Logging {

  implicit val ec: ExecutionContext = cc.executionContext

  def getVatReturn(iossNumber: String, period: String): Action[AnyContent] = Action.async { implicit request =>
    logger.info(s"Here's the request: ${request} ${request.headers} ${request.body}")


    jsonSchemaHelper.applySchemaHeaderValidation(request.headers) {

      val vatReturn = (iossNumber, period) match {
        case ("IM9003333333", _) => nilReturn(iossNumber, period)
        case ("IM9004444444", _) => salesToEuNoCorrectionsReturn(iossNumber, period)
        case ("IM9005555555", _) => salesToEuWithPositiveCorrectionsReturn(iossNumber, period)
        case ("IM9006666666", _) => noSalesToEuWithPositiveAndNegativeCorrectionsReturn(iossNumber, period)
        case ("IM9001233211", "23AJ") => correctionsScenarioOctoberReturn
        case ("IM9001233211", "23AK") => correctionsScenarioNovemberReturn
        case ("IM9001233211", "23AL") => correctionsScenarioDecemberReturn
        case ("IM9006230000", _) | ("IM9004230000", _) | ("IM9007230002", _) | ("IM9007230001", _) => basicVatReturn(iossNumber, period)
        case _ => standardVatReturn(iossNumber, period)
      }

      Ok(Json.toJson(vatReturn)).toFuture

    }
  }

  def getObligations(idType: String, idNumber: String, regimeType: String, dateRange: ObligationsDateRange, status: Option[String]): Action[AnyContent] = Action.async {
    implicit request =>

      logger.info(s"With request: $request ${request.headers} ${request.body}")
      jsonSchemaHelper.applySchemaHeaderValidation(request.headers) {

        def generateObligations(idNumber: String): EtmpObligations = idNumber match {
          case "IM9001234568" | "IM9029999994" | "IM9039999994" | "IM9003999993" | "IM9059999994" | "IM9001233211" => StubData.multipleCorrectionPeriods
          case "IM9001234569" => StubData.multipleCorrectionPeriodYears
          case "IM9001234999" | "IM9001238999" => StubData.moreThanThreeCorrectionPeriodYears
          case "IM9001239999" => StubData.moreThanThreeYearsOpenReturns
          case "IM9008888888" | "IM9008888887" => StubData.singleCorrectionPeriods
          case "IM9009999888" | "IM9009999992" => StubData.firstPeriodNoCorrections
          case "IM9008888886" | "IM9008888884" | "IM9008888883" | "IM9007230000" | "IM9007230003"=> StubData.previousThreeMonthsSubmittedPeriods
          case "IM9006230000" | "IM9007230002" | "IM9004230000" => StubData.previousFourToSixMonthsSubmittedPeriods
          case "IM9007230001" => StubData.previousSevenToNineMonthsSubmittedPeriods
          case "IM9008888882" => StubData.sixMonthsAcrossTwoYearsSubmittedPeriods
          case "IM9008888885" => StubData.previousMonthSubmittedPeriod
          case "IM9005999997" => StubData.firstReturnAfterTransferringFromAnotherMSID
          case "IM9005999977" => StubData.secondReturnAfterTransferringFromAnotherMSID
          case "IM9009999555" => StubData.returnsBeforeTransferringToAnotherMSID
          case "IM9009995555" => StubData.returnsBeforeTransferringToAnotherMSIDTwoOpen
          case _ => StubData.defaultObligationsResponse
        }

        val obligationsResponse = generateObligations(idNumber)

        Ok(Json.toJson(obligationsResponse)).toFuture
      }
  }


}
