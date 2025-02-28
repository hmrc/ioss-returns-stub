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

package uk.gov.hmrc.iossreturnsstub.utils

import uk.gov.hmrc.iossreturnsstub.models.etmp._

import java.time.{LocalDate, LocalDateTime, Month}

object ReturnData {

  def standardVatReturn(iossNumber: String, period: String): EtmpVatReturn = {
    val referencePeriod = toReferencePeriod(period)

    EtmpVatReturn(
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
      paymentReference = s"XI/$iossNumber/$referencePeriod"
    )
  }

  def basicVatReturn(iossNumber: String, period: String): EtmpVatReturn = {
    val referencePeriod = toReferencePeriod(period)

    EtmpVatReturn(
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
          vatAmountGBP = BigDecimal(1000.00)
        ),
        EtmpVatReturnGoodsSupplied(
          msOfConsumption = "FR",
          vatRateType = EtmpVatRateType.ReducedVatRate,
          taxableAmountGBP = BigDecimal(23973.03),
          vatAmountGBP = BigDecimal(1000.00)
        ),
      ),
      totalVATGoodsSuppliedGBP = BigDecimal(2000.00),
      totalVATAmountPayable = BigDecimal(2000.00),
      totalVATAmountPayableAllSpplied = BigDecimal(2000.00),
      correctionPreviousVATReturn = Seq.empty,
      totalVATAmountFromCorrectionGBP = BigDecimal(0.00),
      balanceOfVATDueForMS = Seq(
        EtmpVatReturnBalanceOfVatDue(
          msOfConsumption = "DE",
          totalVATDueGBP = BigDecimal(1000.00),
          totalVATEUR = BigDecimal(1000.00)
        ),
        EtmpVatReturnBalanceOfVatDue(
          msOfConsumption = "FR",
          totalVATDueGBP = BigDecimal(1000.00),
          totalVATEUR = BigDecimal(1000.00)
        )
      ),
      totalVATAmountDueForAllMSGBP = BigDecimal(2000.00),
      paymentReference = s"XI/$iossNumber/$referencePeriod"
    )
  }

  val correctionsScenarioOctoberReturn: EtmpVatReturn = {
    val referencePeriod = toReferencePeriod("23AJ")

    EtmpVatReturn(
      returnReference = s"XI/IM9001233211/$referencePeriod",
      returnVersion = LocalDateTime.of(2023, 11, 1, 0, 0, 0),
      periodKey = "23AJ",
      returnPeriodFrom = LocalDate.of(2023, 10, 1),
      returnPeriodTo = LocalDate.of(2023, 10, 31),
      goodsSupplied = Seq(
        EtmpVatReturnGoodsSupplied(
          msOfConsumption = "DE",
          vatRateType = EtmpVatRateType.StandardVatRate,
          taxableAmountGBP = BigDecimal(6000.00),
          vatAmountGBP = BigDecimal(6000.00)
        ),
        EtmpVatReturnGoodsSupplied(
          msOfConsumption = "FR",
          vatRateType = EtmpVatRateType.ReducedVatRate,
          taxableAmountGBP = BigDecimal(4000.00),
          vatAmountGBP = BigDecimal(4000.00)
        ),
      ),
      totalVATGoodsSuppliedGBP = BigDecimal(10000.00),
      totalVATAmountPayable = BigDecimal(10000.00),
      totalVATAmountPayableAllSpplied = BigDecimal(10000.00),
      correctionPreviousVATReturn = Seq.empty,
      totalVATAmountFromCorrectionGBP = BigDecimal(0.00),
      balanceOfVATDueForMS = Seq(
        EtmpVatReturnBalanceOfVatDue(
          msOfConsumption = "DE",
          totalVATDueGBP = BigDecimal(6000.00),
          totalVATEUR = BigDecimal(6000.00)
        ),
        EtmpVatReturnBalanceOfVatDue(
          msOfConsumption = "FR",
          totalVATDueGBP = BigDecimal(4000.00),
          totalVATEUR = BigDecimal(4000.00)
        )
      ),
      totalVATAmountDueForAllMSGBP = BigDecimal(10000.00),
      paymentReference = s"XI/IM9001233211/$referencePeriod"
    )
  }

  val December2024Returns: EtmpVatReturn = {
    val referencePeriod = toReferencePeriod("24AL")

    EtmpVatReturn(
      returnReference = s"XI/IM9001236667/$referencePeriod",
      returnVersion = LocalDateTime.of(2025, 1, 1, 0, 0, 0),
      periodKey = "24AL",
      returnPeriodFrom = LocalDate.of(2024, 12, 1),
      returnPeriodTo = LocalDate.of(2024, 12, 31),
      goodsSupplied = Seq(
        EtmpVatReturnGoodsSupplied(
          msOfConsumption = "DE",
          vatRateType = EtmpVatRateType.StandardVatRate,
          taxableAmountGBP = BigDecimal(6000.00),
          vatAmountGBP = BigDecimal(6000.00)
        ),
        EtmpVatReturnGoodsSupplied(
          msOfConsumption = "FR",
          vatRateType = EtmpVatRateType.ReducedVatRate,
          taxableAmountGBP = BigDecimal(4000.00),
          vatAmountGBP = BigDecimal(4000.00)
        ),
      ),
      totalVATGoodsSuppliedGBP = BigDecimal(10000.00),
      totalVATAmountPayable = BigDecimal(10000.00),
      totalVATAmountPayableAllSpplied = BigDecimal(10000.00),
      correctionPreviousVATReturn = Seq.empty,
      totalVATAmountFromCorrectionGBP = BigDecimal(0.00),
      balanceOfVATDueForMS = Seq(
        EtmpVatReturnBalanceOfVatDue(
          msOfConsumption = "DE",
          totalVATDueGBP = BigDecimal(6000.00),
          totalVATEUR = BigDecimal(6000.00)
        ),
        EtmpVatReturnBalanceOfVatDue(
          msOfConsumption = "FR",
          totalVATDueGBP = BigDecimal(4000.00),
          totalVATEUR = BigDecimal(4000.00)
        )
      ),
      totalVATAmountDueForAllMSGBP = BigDecimal(10000.00),
      paymentReference = s"XI/IM9001236667/$referencePeriod"
    )
  }

  val January2025Returns: EtmpVatReturn = {
    val referencePeriod = toReferencePeriod("25AA")

    EtmpVatReturn(
      returnReference = s"XI/IM9001236667/$referencePeriod",
      returnVersion = LocalDateTime.of(2025, 2, 1, 0, 0, 0),
      periodKey = "25AA",
      returnPeriodFrom = LocalDate.of(2025, 1, 1),
      returnPeriodTo = LocalDate.of(2025, 1, 31),
      goodsSupplied = Seq(
        EtmpVatReturnGoodsSupplied(
          msOfConsumption = "DE",
          vatRateType = EtmpVatRateType.StandardVatRate,
          taxableAmountGBP = BigDecimal(6000.00),
          vatAmountGBP = BigDecimal(6000.00)
        ),
        EtmpVatReturnGoodsSupplied(
          msOfConsumption = "FR",
          vatRateType = EtmpVatRateType.ReducedVatRate,
          taxableAmountGBP = BigDecimal(4000.00),
          vatAmountGBP = BigDecimal(4000.00)
        ),
      ),
      totalVATGoodsSuppliedGBP = BigDecimal(10000.00),
      totalVATAmountPayable = BigDecimal(10000.00),
      totalVATAmountPayableAllSpplied = BigDecimal(10000.00),
      correctionPreviousVATReturn = Seq.empty,
      totalVATAmountFromCorrectionGBP = BigDecimal(0.00),
      balanceOfVATDueForMS = Seq(
        EtmpVatReturnBalanceOfVatDue(
          msOfConsumption = "DE",
          totalVATDueGBP = BigDecimal(6000.00),
          totalVATEUR = BigDecimal(6000.00)
        ),
        EtmpVatReturnBalanceOfVatDue(
          msOfConsumption = "FR",
          totalVATDueGBP = BigDecimal(4000.00),
          totalVATEUR = BigDecimal(4000.00)
        )
      ),
      totalVATAmountDueForAllMSGBP = BigDecimal(10000.00),
      paymentReference = s"XI/IM9001236667/$referencePeriod"
    )
  }
  
  val correctionsScenarioNovemberReturn: EtmpVatReturn = {
    val referencePeriod = toReferencePeriod("23AK")

    EtmpVatReturn(
      returnReference = s"XI/IM9001233211/$referencePeriod",
      returnVersion = LocalDateTime.of(2023, 12, 1, 0, 0, 0),
      periodKey = "23AK",
      returnPeriodFrom = LocalDate.of(2023, 11, 1),
      returnPeriodTo = LocalDate.of(2023, 11, 30),
      goodsSupplied = Seq.empty,
      totalVATGoodsSuppliedGBP = BigDecimal(0.00),
      totalVATAmountPayable = BigDecimal(0.00),
      totalVATAmountPayableAllSpplied = BigDecimal(0.00),
      correctionPreviousVATReturn = Seq(
        EtmpVatReturnCorrection(
          periodKey = "23AJ",
          periodFrom = LocalDate.of(2023, 10, 1).toString,
          periodTo = LocalDate.of(2023, 10, 31).toString,
          msOfConsumption = "DE",
          totalVATAmountCorrectionGBP = BigDecimal(-1000.00),
          totalVATAmountCorrectionEUR = BigDecimal(-1000.00)
        ),
        EtmpVatReturnCorrection(
          periodKey = "23AJ",
          periodFrom = LocalDate.of(2023, 10, 1).toString,
          periodTo = LocalDate.of(2023, 10, 31).toString,
          msOfConsumption = "FR",
          totalVATAmountCorrectionGBP = BigDecimal(-500.00),
          totalVATAmountCorrectionEUR = BigDecimal(-500.00)
        )
      ),
      totalVATAmountFromCorrectionGBP = BigDecimal(-1500.00),
      balanceOfVATDueForMS = Seq(
        EtmpVatReturnBalanceOfVatDue(
          msOfConsumption = "DE",
          totalVATDueGBP = BigDecimal(0.00),
          totalVATEUR = BigDecimal(0.00)
        ),
        EtmpVatReturnBalanceOfVatDue(
          msOfConsumption = "FR",
          totalVATDueGBP = BigDecimal(0.00),
          totalVATEUR = BigDecimal(0.00)
        )
      ),
      totalVATAmountDueForAllMSGBP = BigDecimal(0.00),
      paymentReference = s"XI/IM9001233211/$referencePeriod"
    )
  }

  val correctionsScenarioDecemberReturn: EtmpVatReturn = {
    val referencePeriod = toReferencePeriod("23AL")

    EtmpVatReturn(
      returnReference = s"XI/IM9001233211/$referencePeriod",
      returnVersion = LocalDateTime.of(2024, 1, 1, 0, 0, 0),
      periodKey = "23AL",
      returnPeriodFrom = LocalDate.of(2023, 12, 1),
      returnPeriodTo = LocalDate.of(2023, 12, 31),
      goodsSupplied = Seq(
        EtmpVatReturnGoodsSupplied(
          msOfConsumption = "FR",
          vatRateType = EtmpVatRateType.ReducedVatRate,
          taxableAmountGBP = BigDecimal(4000.00),
          vatAmountGBP = BigDecimal(4000.00)
        ),
      ),
      totalVATGoodsSuppliedGBP = BigDecimal(4000.00),
      totalVATAmountPayable = BigDecimal(4000.00),
      totalVATAmountPayableAllSpplied = BigDecimal(4000.00),
      correctionPreviousVATReturn = Seq(
        EtmpVatReturnCorrection(
          periodKey = "23AJ",
          periodFrom = LocalDate.of(2023, 10, 1).toString,
          periodTo = LocalDate.of(2023, 10, 31).toString,
          msOfConsumption = "DE",
          totalVATAmountCorrectionGBP = BigDecimal(-1500.00),
          totalVATAmountCorrectionEUR = BigDecimal(-1500.00)
        ),
        EtmpVatReturnCorrection(
          periodKey = "23AJ",
          periodFrom = LocalDate.of(2023, 10, 1).toString,
          periodTo = LocalDate.of(2023, 10, 31).toString,
          msOfConsumption = "FR",
          totalVATAmountCorrectionGBP = BigDecimal(1000.00),
          totalVATAmountCorrectionEUR = BigDecimal(1000.00)
        ),
        EtmpVatReturnCorrection(
          periodKey = "23AK",
          periodFrom = LocalDate.of(2023, 11, 1).toString,
          periodTo = LocalDate.of(2023, 11, 30).toString,
          msOfConsumption = "DE",
          totalVATAmountCorrectionGBP = BigDecimal(854.25),
          totalVATAmountCorrectionEUR = BigDecimal(854.25)
        )
      ),
      totalVATAmountFromCorrectionGBP = BigDecimal(354.25),
      balanceOfVATDueForMS = Seq(
        EtmpVatReturnBalanceOfVatDue(
          msOfConsumption = "DE",
          totalVATDueGBP = BigDecimal(0.00),
          totalVATEUR = BigDecimal(0.00)
        ),
        EtmpVatReturnBalanceOfVatDue(
          msOfConsumption = "FR",
          totalVATDueGBP = BigDecimal(5000.00),
          totalVATEUR = BigDecimal(5000.00)
        )
      ),
      totalVATAmountDueForAllMSGBP = BigDecimal(5000.00),
      paymentReference = s"XI/IM9001233211/$referencePeriod"
    )
  }

  def salesToEuNoCorrectionsReturnPartial(iossNumber: String, period: String, fromDate: LocalDate, toDate: LocalDate): EtmpVatReturn = {
    val referencePeriod = toReferencePeriod(period)

    EtmpVatReturn(
      returnReference = s"XI/$iossNumber/$referencePeriod",
      returnVersion = LocalDateTime.of(2024, 1, 2, 0, 0, 0),
      periodKey = period,
      returnPeriodFrom = fromDate,
      returnPeriodTo = toDate,
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
      correctionPreviousVATReturn = Seq.empty,
      totalVATAmountFromCorrectionGBP = BigDecimal(0),
      balanceOfVATDueForMS = Seq(
        EtmpVatReturnBalanceOfVatDue(
          msOfConsumption = "DE",
          totalVATDueGBP = BigDecimal(2469.13),
          totalVATEUR = BigDecimal(2883.80)
        ),
        EtmpVatReturnBalanceOfVatDue(
          msOfConsumption = "FR",
          totalVATDueGBP = BigDecimal(2397.30),
          totalVATEUR = BigDecimal(2798.87)
        )
      ),
      totalVATAmountDueForAllMSGBP = BigDecimal(4866.43),
      paymentReference = s"XI/$iossNumber/$referencePeriod"
    )
  }

  def salesToEuNoCorrectionsReturn(iossNumber: String, period: String): EtmpVatReturn = {
    val referencePeriod = toReferencePeriod(period)

    EtmpVatReturn(
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
      totalVATAmountPayable = BigDecimal(4866.43),
      totalVATAmountPayableAllSpplied = BigDecimal(4866.43),
      correctionPreviousVATReturn = Seq.empty,
      totalVATAmountFromCorrectionGBP = BigDecimal(0),
      balanceOfVATDueForMS = Seq(
        EtmpVatReturnBalanceOfVatDue(
          msOfConsumption = "DE",
          totalVATDueGBP = BigDecimal(2469.13),
          totalVATEUR = BigDecimal(2883.80)
        ),
        EtmpVatReturnBalanceOfVatDue(
          msOfConsumption = "FR",
          totalVATDueGBP = BigDecimal(2397.30),
          totalVATEUR = BigDecimal(2798.87)
        )
      ),
      totalVATAmountDueForAllMSGBP = BigDecimal(4866.43),
      paymentReference = s"XI/$iossNumber/$referencePeriod"
    )
  }

  def salesToEuWithPositiveCorrectionsReturn(iossNumber: String, period: String): EtmpVatReturn = {
    val referencePeriod = toReferencePeriod(period)

    EtmpVatReturn(
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
      totalVATAmountPayable = BigDecimal(4866.43),
      totalVATAmountPayableAllSpplied = BigDecimal(4866.43),
      correctionPreviousVATReturn = Seq(
        EtmpVatReturnCorrection(
          periodKey = "23AH",
          periodFrom = LocalDate.of(2023, 8, 1).toString,
          periodTo = LocalDate.of(2023, 8, 31).toString,
          msOfConsumption = "DE",
          totalVATAmountCorrectionGBP = BigDecimal(1000.00),
          totalVATAmountCorrectionEUR = BigDecimal(1100.41)
        ),
        EtmpVatReturnCorrection(
          periodKey = "23AI",
          periodFrom = LocalDate.of(2023, 9, 1).toString,
          periodTo = LocalDate.of(2023, 9, 30).toString,
          msOfConsumption = "DE",
          totalVATAmountCorrectionGBP = BigDecimal(1000.00),
          totalVATAmountCorrectionEUR = BigDecimal(1100.41)
        ),
        EtmpVatReturnCorrection(
          periodKey = "23AJ",
          periodFrom = LocalDate.of(2023, 10, 1).toString,
          periodTo = LocalDate.of(2023, 10, 31).toString,
          msOfConsumption = "DE",
          totalVATAmountCorrectionGBP = BigDecimal(1000.00),
          totalVATAmountCorrectionEUR = BigDecimal(1100.41)
        ),
        EtmpVatReturnCorrection(
          periodKey = "23AJ",
          periodFrom = LocalDate.of(2023, 10, 1).toString,
          periodTo = LocalDate.of(2023, 10, 31).toString,
          msOfConsumption = "FR",
          totalVATAmountCorrectionGBP = BigDecimal(1000.00),
          totalVATAmountCorrectionEUR = BigDecimal(1100.41)
        ),
        EtmpVatReturnCorrection(
          periodKey = "23AJ",
          periodFrom = LocalDate.of(2023, 10, 1).toString,
          periodTo = LocalDate.of(2023, 10, 31).toString,
          msOfConsumption = "ES",
          totalVATAmountCorrectionGBP = BigDecimal(2500.00),
          totalVATAmountCorrectionEUR = BigDecimal(2919.84)
        )
      ),
      totalVATAmountFromCorrectionGBP = BigDecimal(6500.00),
      balanceOfVATDueForMS = Seq(
        EtmpVatReturnBalanceOfVatDue(
          msOfConsumption = "DE",
          totalVATDueGBP = BigDecimal(5469.13),
          totalVATEUR = BigDecimal(6385.72)
        ),
        EtmpVatReturnBalanceOfVatDue(
          msOfConsumption = "FR",
          totalVATDueGBP = BigDecimal(3397.30),
          totalVATEUR = BigDecimal(3966.53)
        ),
        EtmpVatReturnBalanceOfVatDue(
          msOfConsumption = "ES",
          totalVATDueGBP = BigDecimal(2500.00),
          totalVATEUR = BigDecimal(2919.05)
        )
      ),
      totalVATAmountDueForAllMSGBP = BigDecimal(11366.43),
      paymentReference = s"XI/$iossNumber/$referencePeriod"
    )
  }

  def noSalesToEuWithPositiveAndNegativeCorrectionsReturn(iossNumber: String, period: String): EtmpVatReturn = {
    val referencePeriod = toReferencePeriod(period)

    EtmpVatReturn(
      returnReference = s"XI/$iossNumber/$referencePeriod",
      returnVersion = LocalDateTime.of(2024, 1, 2, 0, 0, 0),
      periodKey = period,
      returnPeriodFrom = LocalDate.of(2023, 12, 1),
      returnPeriodTo = LocalDate.of(2023, 12, 31),
      goodsSupplied = Seq.empty,
      totalVATGoodsSuppliedGBP = BigDecimal(0),
      totalVATAmountPayable = BigDecimal(0),
      totalVATAmountPayableAllSpplied = BigDecimal(0),
      correctionPreviousVATReturn = Seq(
        EtmpVatReturnCorrection(
          periodKey = "23AH",
          periodFrom = LocalDate.of(2023, 8, 1).toString,
          periodTo = LocalDate.of(2023, 8, 31).toString,
          msOfConsumption = "DE",
          totalVATAmountCorrectionGBP = BigDecimal(1000.00),
          totalVATAmountCorrectionEUR = BigDecimal(1100.41)
        ),
        EtmpVatReturnCorrection(
          periodKey = "23AI",
          periodFrom = LocalDate.of(2023, 9, 1).toString,
          periodTo = LocalDate.of(2023, 9, 30).toString,
          msOfConsumption = "DE",
          totalVATAmountCorrectionGBP = BigDecimal(1000.00),
          totalVATAmountCorrectionEUR = BigDecimal(1100.41)
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
          totalVATAmountCorrectionGBP = BigDecimal(1000.00),
          totalVATAmountCorrectionEUR = BigDecimal(1100.41)
        ),
        EtmpVatReturnCorrection(
          periodKey = "23AJ",
          periodFrom = LocalDate.of(2023, 10, 1).toString,
          periodTo = LocalDate.of(2023, 10, 31).toString,
          msOfConsumption = "ES",
          totalVATAmountCorrectionGBP = BigDecimal(-500.00),
          totalVATAmountCorrectionEUR = BigDecimal(-583.84)
        ),
        EtmpVatReturnCorrection(
          periodKey = "23AJ",
          periodFrom = LocalDate.of(2023, 10, 1).toString,
          periodTo = LocalDate.of(2023, 10, 31).toString,
          msOfConsumption = "PL",
          totalVATAmountCorrectionGBP = BigDecimal(-500.00),
          totalVATAmountCorrectionEUR = BigDecimal(-583.84)
        )
      ),
      totalVATAmountFromCorrectionGBP = BigDecimal(1000.00),
      balanceOfVATDueForMS = Seq(
        EtmpVatReturnBalanceOfVatDue(
          msOfConsumption = "DE",
          totalVATDueGBP = BigDecimal(1000.00),
          totalVATEUR = BigDecimal(1167.56)
        ),
        EtmpVatReturnBalanceOfVatDue(
          msOfConsumption = "FR",
          totalVATDueGBP = BigDecimal(1000.00),
          totalVATEUR = BigDecimal(1167.56)
        ),
        EtmpVatReturnBalanceOfVatDue(
          msOfConsumption = "ES",
          totalVATDueGBP = BigDecimal(0),
          totalVATEUR = BigDecimal(0)
        ),
        EtmpVatReturnBalanceOfVatDue(
          msOfConsumption = "PL",
          totalVATDueGBP = BigDecimal(0),
          totalVATEUR = BigDecimal(0)
        )
      ),
      totalVATAmountDueForAllMSGBP = BigDecimal(2000.00),
      paymentReference = s"XI/$iossNumber/$referencePeriod"
    )
  }

  def nilReturn(iossNumber: String, period: String): EtmpVatReturn = {
    val referencePeriod = toReferencePeriod(period)

    EtmpVatReturn(
      returnReference = s"XI/$iossNumber/$referencePeriod",
      returnVersion = LocalDateTime.of(2024, 1, 2, 0, 0, 0),
      periodKey = period,
      returnPeriodFrom = LocalDate.of(2023, 12, 1),
      returnPeriodTo = LocalDate.of(2023, 12, 31),
      goodsSupplied = Seq.empty,
      totalVATGoodsSuppliedGBP = BigDecimal(0),
      totalVATAmountPayable = BigDecimal(0),
      totalVATAmountPayableAllSpplied = BigDecimal(0),
      correctionPreviousVATReturn = Seq.empty,
      totalVATAmountFromCorrectionGBP = BigDecimal(0),
      balanceOfVATDueForMS = Seq.empty,
      totalVATAmountDueForAllMSGBP = BigDecimal(0),
      paymentReference = s"XI/$iossNumber/$referencePeriod"
    )
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

    val stringMonth = if (month.getValue < 10) {
      s"0${month.getValue}"
    } else {
      month.getValue.toString
    }

    s"20$etmpYear.M$stringMonth"
  }

}
