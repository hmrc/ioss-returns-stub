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
import uk.gov.hmrc.iossreturnsstub.models.DateRange
import uk.gov.hmrc.iossreturnsstub.models.etmp._
import uk.gov.hmrc.iossreturnsstub.utils.FutureSyntax.FutureOps
import uk.gov.hmrc.iossreturnsstub.utils.JsonSchemaHelper
import uk.gov.hmrc.play.bootstrap.backend.controller.BackendController

import java.time.LocalDate
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
    val validPeriods = List("23AK", "23AL")

    logger.info(s"Here's the request: ${request} ${request.headers} ${request.body}")
    jsonSchemaHelper.applySchemaHeaderValidation(request.headers) {
      validPeriods.find(_ == period).map { validPeriod =>
      val vatReturn = EtmpVatReturn(
        returnReference = "XI/IM9001234567/2023.M11",
        periodKey = validPeriod,
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
            msOfConsumption = "FR"
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
        totalVATAmountDueForAllMSEUR = BigDecimal(2569.13),
        paymentReference = "XI/IM9001234567/2023.M11"

      )

      Ok(Json.toJson(vatReturn)).toFuture
    }.getOrElse {
        logger.warn(s"period $period is not a valid period {${validPeriods.mkString(", ")}}")

        NotFound(Json.toJson("")).toFuture
      }
    }
  }

  def getObligations(idType: String, idNumber: String, regimeType: String, dateRange: DateRange, status: String): Action[AnyContent] = Action.async {
    implicit request =>

      logger.info(s"With request: $request ${request.headers} ${request.body}")
      jsonSchemaHelper.applySchemaHeaderValidation(request.headers) {
        val obligationsResponse = EtmpObligations(
          referenceNumber = idNumber,
          referenceType = regimeType,
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

        Ok(Json.toJson(obligationsResponse)).toFuture
      }
  }
}
