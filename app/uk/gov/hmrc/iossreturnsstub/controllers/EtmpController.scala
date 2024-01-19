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
    val referencePeriod = toReferencePeriod(period)

    logger.info(s"Here's the request: ${request} ${request.headers} ${request.body}")
    jsonSchemaHelper.applySchemaHeaderValidation(request.headers) {
      val vatReturn = EtmpVatReturn(
        returnReference = s"XI/$iossNumber/$referencePeriod",
        returnVersion = LocalDateTime.of(2024, 1, 2, 0, 0, 0),
        periodKey = period,
        returnPeriodFrom = LocalDate.of(2023, 12, 1),
        returnPeriodTo = LocalDate.of(2023, 12, 31),
        goodsSupplied = Seq(
          EtmpVatReturnGoodsSupplied(
            msOfConsumption = "DE",
            vatRateType = EtmpVatRateType.StandardVatRate,
            taxableAmountGBP = BigDecimal(12345.67),
            vatAmountGBP = BigDecimal(2469.13)
          ),
          EtmpVatReturnGoodsSupplied(
            msOfConsumption = "FR",
            vatRateType = EtmpVatRateType.ReducedVatRate,
            taxableAmountGBP = BigDecimal(23973.03),
            vatAmountGBP = BigDecimal(2397.30)
          ),
        ),
        totalVATGoodsSuppliedGBP = BigDecimal(4866.43),
        totalVATAmountPayable = BigDecimal(0),
        totalVATAmountPayableAllSpplied = BigDecimal(4866.43),
        correctionPreviousVATReturn = Seq(
          EtmpVatReturnCorrection(
            periodKey = "23AH",
            periodFrom = LocalDate.of(2023, 8, 1).toString,
            periodTo = LocalDate.of(2023, 8, 31).toString,
            msOfConsumption = "DE",
            totalVATAmountCorrectionGBP = BigDecimal(-1000.00),
            totalVATAmountCorrectionEUR = BigDecimal(-1100.41)
          ),
          EtmpVatReturnCorrection(
            periodKey = "23AI",
            periodFrom = LocalDate.of(2023, 9, 1).toString,
            periodTo = LocalDate.of(2023, 9, 30).toString,
            msOfConsumption = "DE",
            totalVATAmountCorrectionGBP = BigDecimal(-1000.00),
            totalVATAmountCorrectionEUR = BigDecimal(-1100.41)
          ),
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
            msOfConsumption = "FR",
            totalVATAmountCorrectionGBP = BigDecimal(-1000.00),
            totalVATAmountCorrectionEUR = BigDecimal(-1100.41)
          )
        ),
        totalVATAmountFromCorrectionGBP = BigDecimal(-4000.00),
        balanceOfVATDueForMS = Seq(
          EtmpVatReturnBalanceOfVatDue(
            msOfConsumption = "DE",
            totalVATDueGBP = BigDecimal(0),
            totalVATEUR = BigDecimal(0)
          ),
          EtmpVatReturnBalanceOfVatDue(
            msOfConsumption = "FR",
            totalVATDueGBP = BigDecimal(1397.30),
            totalVATEUR = BigDecimal(1537.60)
          )
        ),
        totalVATAmountDueForAllMSGBP = BigDecimal(1397.30),
        paymentReference = s"XI/$iossNumber/$referencePeriod"
      )

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

  private def toReferencePeriod(etmpPeriod: String): String = {

    val etmpYear = etmpPeriod.substring(0, 2)
    val etmpMonth = etmpPeriod.substring(2, 4)

    val month = etmpMonth match {
      case "AA" => Month.JANUARY
      case "AB" => Month.FEBRUARY
      case "AC" => Month.MARCH
      case "AD" => Month.APRIL
      case "AE" => Month.MAY
      case "AF" => Month.JUNE
      case "AG" => Month.JULY
      case "AH" => Month.AUGUST
      case "AI" => Month.SEPTEMBER
      case "AJ" => Month.OCTOBER
      case "AK" => Month.NOVEMBER
      case "AL" => Month.DECEMBER
    }

    val stringMonth = if(month.getValue < 10) {
      s"0${month.getValue}"
    } else {
      month.getValue.toString
    }

    s"20$etmpYear.M$stringMonth"
  }
}
