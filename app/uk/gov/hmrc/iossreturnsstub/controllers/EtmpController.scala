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

import java.time.{LocalDate, LocalDateTime, Month}
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

      val vatReturn = iossNumber match {
        case "IM9003333333" => nilReturn(iossNumber, period)
        case "IM9004444444" => salesToEuNoCorrectionsReturn(iossNumber, period)
        case "IM9005555555" => salesToEuWithPositiveCorrectionsReturn(iossNumber, period)
        case "IM9006666666" => noSalesToEuWithPositiveAndNegativeCorrectionsReturn(iossNumber, period)
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
          case "IM9001234568" => StubData.multipleCorrectionPeriods
          case "IM9001234569" => StubData.multipleCorrectionPeriodYears
          case "IM9008888888" => StubData.singleCorrectionPeriods
          case "IM9009999888" => StubData.firstPeriodNoCorrections
          case _ => StubData.defaultObligationsResponse
        }

        val obligationsResponse = generateObligations(idNumber)

        Ok(Json.toJson(obligationsResponse)).toFuture
      }
  }


}
