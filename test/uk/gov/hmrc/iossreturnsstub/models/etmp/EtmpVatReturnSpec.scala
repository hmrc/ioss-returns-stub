/*
 * Copyright 2024 HM Revenue & Customs
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

package uk.gov.hmrc.iossreturnsstub.models.etmp

import org.scalatest.freespec.AnyFreeSpec
import org.scalatest.matchers.must.Matchers.mustBe
import org.scalatest.matchers.should.Matchers
import play.api.libs.json.{JsSuccess, Json}

import java.time.{LocalDate, LocalDateTime}

class EtmpVatReturnSpec extends AnyFreeSpec with Matchers {
  
  private val iossNumber = "IM9001234567"
  private val genEtmpVatReturn: EtmpVatReturn = EtmpVatReturn(
      returnReference = s"XI/$iossNumber/2023.M11",
      returnVersion = LocalDateTime.of(2024, 1, 2, 0, 0, 0),
      periodKey = "23AK",
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
      totalVATAmountPayable = BigDecimal(4866.43),
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
      paymentReference = s"XI/$iossNumber/2023.M11"
    )

  "EtmpVatReturn" - {

    "must serialise/deserialise to and from EtmpVatReturn" in {

      val json = Json.obj(
        "returnReference" -> genEtmpVatReturn.returnReference,
        "returnVersion" -> genEtmpVatReturn.returnVersion,
        "periodKey" -> genEtmpVatReturn.periodKey,
        "returnPeriodFrom" -> genEtmpVatReturn.returnPeriodFrom,
        "returnPeriodTo" -> genEtmpVatReturn.returnPeriodTo,
        "goodsSupplied" -> genEtmpVatReturn.goodsSupplied,
        "totalVATGoodsSuppliedGBP" -> genEtmpVatReturn.totalVATGoodsSuppliedGBP,
        "totalVATAmountPayable" -> genEtmpVatReturn.totalVATAmountPayable,
        "totalVATAmountPayableAllSpplied" -> genEtmpVatReturn.totalVATAmountPayableAllSpplied,
        "correctionPreviousVATReturn" -> genEtmpVatReturn.correctionPreviousVATReturn,
        "totalVATAmountFromCorrectionGBP" -> genEtmpVatReturn.totalVATAmountFromCorrectionGBP,
        "balanceOfVATDueForMS" -> genEtmpVatReturn.balanceOfVATDueForMS,
        "totalVATAmountDueForAllMSGBP" -> genEtmpVatReturn.totalVATAmountDueForAllMSGBP,
        "paymentReference" -> genEtmpVatReturn.paymentReference
      )

      val expectedResult = EtmpVatReturn(
        returnReference = genEtmpVatReturn.returnReference,
        returnVersion = genEtmpVatReturn.returnVersion,
        periodKey = genEtmpVatReturn.periodKey,
        returnPeriodFrom = genEtmpVatReturn.returnPeriodFrom,
        returnPeriodTo = genEtmpVatReturn.returnPeriodTo,
        goodsSupplied = genEtmpVatReturn.goodsSupplied,
        totalVATGoodsSuppliedGBP = genEtmpVatReturn.totalVATGoodsSuppliedGBP,
        totalVATAmountPayable = genEtmpVatReturn.totalVATAmountPayable,
        totalVATAmountPayableAllSpplied = genEtmpVatReturn.totalVATAmountPayableAllSpplied,
        correctionPreviousVATReturn = genEtmpVatReturn.correctionPreviousVATReturn,
        totalVATAmountFromCorrectionGBP = genEtmpVatReturn.totalVATAmountFromCorrectionGBP,
        balanceOfVATDueForMS = genEtmpVatReturn.balanceOfVATDueForMS,
        totalVATAmountDueForAllMSGBP = genEtmpVatReturn.totalVATAmountDueForAllMSGBP,
        paymentReference = genEtmpVatReturn.paymentReference
      )

      Json.toJson(expectedResult) mustBe json
      json.validate[EtmpVatReturn] mustBe JsSuccess(expectedResult)
    }
  }
}
