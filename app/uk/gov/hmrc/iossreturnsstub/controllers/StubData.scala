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

import uk.gov.hmrc.iossreturnsstub.models._
import uk.gov.hmrc.iossreturnsstub.models.etmp.{EtmpObligation, EtmpObligationDetails, EtmpObligations, EtmpObligationsFulfilmentStatus}

import java.time.{LocalDate, Month}
import java.time.Month._

object StubData {

  private val firstDayForOctober = LocalDate.of(2023, Month.OCTOBER, 1)
  private val lastDayForOctober = LocalDate.of(2023, Month.OCTOBER, 31)
  private val firstDayForNovember = LocalDate.of(2023, Month.NOVEMBER, 1)
  private val lastDayForNovember = LocalDate.of(2023, Month.NOVEMBER, 30)

  private val firstDayOfThreeMonthsAgoPeriod = LocalDate.now().minusMonths(3).withDayOfMonth(1)
  private val lastDayOfThreeMonthsAgoPeriod = LocalDate.now().minusMonths(2).withDayOfMonth(1).minusDays(1)
  private val firstDayOfTwoMonthsAgoPeriod = LocalDate.now().minusMonths(2).withDayOfMonth(1)
  private val lastDayOfTwoMonthsAgoPeriod = LocalDate.now().minusMonths(1).withDayOfMonth(1).minusDays(1)
  private val firstDayOfOneMonthAgoPeriod = LocalDate.now().minusMonths(1).withDayOfMonth(1)
  private val lastDayOfOneMonthAgoPeriod = LocalDate.now().withDayOfMonth(1).minusDays(1)

  val items = Seq(
    Item(
      amount = Some(BigDecimal(1000)),
      clearingReason = Some("01"),
      paymentReference = Some("a"),
      paymentAmount = Some(BigDecimal(500)),
      paymentMethod = Some("A")
    )
  )

  val financialTransactions = Seq(
    FinancialTransaction(
      chargeType = Some("G Ret FR EU-OMS"),
      mainType = None,
      taxPeriodFrom = Some(firstDayForOctober),
      taxPeriodTo = Some(lastDayForOctober),
      originalAmount = Some(BigDecimal(1500)),
      outstandingAmount = Some(BigDecimal(500)),
      clearedAmount = Some(BigDecimal(1000)),
      items = Some(items)
    ),
    FinancialTransaction(
      chargeType = Some("G Ret FR EU-OMS"),
      mainType = None,
      taxPeriodFrom = Some(firstDayForNovember),
      taxPeriodTo = Some(lastDayForNovember),
      originalAmount = Some(BigDecimal(1500)),
      outstandingAmount = Some(BigDecimal(500)),
      clearedAmount = Some(BigDecimal(1000)),
      items = Some(items)
    )
  )

  val singleOutstandingPayment = Seq(
    FinancialTransaction(
      chargeType = Some("G Ret FR EU-OMS"),
      mainType = None,
      taxPeriodFrom = Some(firstDayForNovember),
      taxPeriodTo = Some(lastDayForNovember),
      originalAmount = Some(BigDecimal(1500)),
      outstandingAmount = Some(BigDecimal(500)),
      clearedAmount = Some(BigDecimal(1000)),
      items = Some(items)
    )
  )

  val singleReturnFullyPaid = Seq(
    FinancialTransaction(
      chargeType = Some("G Ret FR EU-OMS"),
      mainType = None,
      taxPeriodFrom = Some(firstDayForNovember),
      taxPeriodTo = Some(lastDayForNovember),
      originalAmount = Some(BigDecimal(1500)),
      outstandingAmount = Some(BigDecimal(0)),
      clearedAmount = Some(BigDecimal(1500)),
      items = Some(items)
    )
  )

  val threeReturnsTwoOutstandingOnePaid = Seq(
    FinancialTransaction(
      chargeType = Some("G Ret FR EU-OMS"),
      mainType = None,
      taxPeriodFrom = Some(firstDayOfThreeMonthsAgoPeriod),
      taxPeriodTo = Some(lastDayOfThreeMonthsAgoPeriod),
      originalAmount = Some(BigDecimal(1500)),
      outstandingAmount = Some(BigDecimal(500)),
      clearedAmount = Some(BigDecimal(1000)),
      items = Some(items)
    ),
    FinancialTransaction(
      chargeType = Some("G Ret FR EU-OMS"),
      mainType = None,
      taxPeriodFrom = Some(firstDayOfTwoMonthsAgoPeriod),
      taxPeriodTo = Some(lastDayOfTwoMonthsAgoPeriod),
      originalAmount = Some(BigDecimal(1500)),
      outstandingAmount = Some(BigDecimal(0)),
      clearedAmount = Some(BigDecimal(1500)),
      items = Some(items)
    ),
    FinancialTransaction(
      chargeType = Some("G Ret FR EU-OMS"),
      mainType = None,
      taxPeriodFrom = Some(firstDayOfOneMonthAgoPeriod),
      taxPeriodTo = Some(lastDayOfOneMonthAgoPeriod),
      originalAmount = Some(BigDecimal(1500)),
      outstandingAmount = Some(BigDecimal(500)),
      clearedAmount = Some(BigDecimal(1000)),
      items = Some(items)
    )
  )

  val threeReturnsOnePartialOneUnpaidOnePaid: Seq[FinancialTransaction] = Seq(
    FinancialTransaction(
      chargeType = Some("G Ret FR EU-OMS"),
      mainType = None,
      taxPeriodFrom = Some(firstDayOfThreeMonthsAgoPeriod),
      taxPeriodTo = Some(lastDayOfThreeMonthsAgoPeriod),
      originalAmount = Some(BigDecimal(1500)),
      outstandingAmount = Some(BigDecimal(1500)),
      clearedAmount = Some(BigDecimal(0)),
      items = Some(items)
    ),
    FinancialTransaction(
      chargeType = Some("G Ret FR EU-OMS"),
      mainType = None,
      taxPeriodFrom = Some(firstDayOfTwoMonthsAgoPeriod),
      taxPeriodTo = Some(lastDayOfTwoMonthsAgoPeriod),
      originalAmount = Some(BigDecimal(1500)),
      outstandingAmount = Some(BigDecimal(0)),
      clearedAmount = Some(BigDecimal(1500)),
      items = Some(items)
    ),
    FinancialTransaction(
      chargeType = Some("G Ret FR EU-OMS"),
      mainType = None,
      taxPeriodFrom = Some(firstDayOfOneMonthAgoPeriod),
      taxPeriodTo = Some(lastDayOfOneMonthAgoPeriod),
      originalAmount = Some(BigDecimal(1500)),
      outstandingAmount = Some(BigDecimal(500)),
      clearedAmount = Some(BigDecimal(1000)),
      items = Some(items)
    )
  )

  val threeReturnsOneUnknownOneUnpaidOnePaid: Seq[FinancialTransaction] = Seq(
    FinancialTransaction(
      chargeType = Some("G Ret FR EU-OMS"),
      mainType = None,
      taxPeriodFrom = Some(firstDayOfThreeMonthsAgoPeriod),
      taxPeriodTo = Some(lastDayOfThreeMonthsAgoPeriod),
      originalAmount = Some(BigDecimal(1500)),
      outstandingAmount = Some(BigDecimal(1500)),
      clearedAmount = Some(BigDecimal(0)),
      items = Some(items)
    ),
    FinancialTransaction(
      chargeType = Some("G Ret FR EU-OMS"),
      mainType = None,
      taxPeriodFrom = Some(firstDayOfTwoMonthsAgoPeriod),
      taxPeriodTo = Some(lastDayOfTwoMonthsAgoPeriod),
      originalAmount = Some(BigDecimal(1500)),
      outstandingAmount = Some(BigDecimal(0)),
      clearedAmount = Some(BigDecimal(1500)),
      items = Some(items)
    )
  )


  val oneReturnWithOutstanding = Seq(
    FinancialTransaction(
      chargeType = Some("G Ret FR EU-OMS"),
      mainType = None,
      taxPeriodFrom = Some(firstDayOfOneMonthAgoPeriod),
      taxPeriodTo = Some(lastDayOfOneMonthAgoPeriod),
      originalAmount = Some(BigDecimal(1500)),
      outstandingAmount = Some(BigDecimal(500)),
      clearedAmount = Some(BigDecimal(1000)),
      items = Some(items)
    ))


  val defaultObligationsResponse: EtmpObligations = EtmpObligations(obligations = Seq(EtmpObligation(
    obligationDetails = Seq(
      EtmpObligationDetails(
        status = EtmpObligationsFulfilmentStatus.Open,
        periodKey = "23AL"
      ),
      EtmpObligationDetails(
        status = EtmpObligationsFulfilmentStatus.Fulfilled,
        periodKey = "23AK"
      ),
      EtmpObligationDetails(
        status = EtmpObligationsFulfilmentStatus.Fulfilled,
        periodKey = "23AJ"
      )
    )
  )))

  val multipleCorrectionPeriods: EtmpObligations = EtmpObligations(obligations = Seq(EtmpObligation(
    obligationDetails = Seq(
      EtmpObligationDetails(
        status = EtmpObligationsFulfilmentStatus.Fulfilled,
        periodKey = "23AJ"
      ),
      EtmpObligationDetails(
        status = EtmpObligationsFulfilmentStatus.Fulfilled,
        periodKey = "23AK"
      ),
      EtmpObligationDetails(
        status = EtmpObligationsFulfilmentStatus.Fulfilled,
        periodKey = "23AL"
      )
    )
  )))

  val multipleCorrectionPeriodYears: EtmpObligations = EtmpObligations(obligations = Seq(EtmpObligation(
    obligationDetails = Seq(
      EtmpObligationDetails(
        status = EtmpObligationsFulfilmentStatus.Fulfilled,
        periodKey = "23AJ"
      ),
      EtmpObligationDetails(
        status = EtmpObligationsFulfilmentStatus.Fulfilled,
        periodKey = "23AK"
      ),
      EtmpObligationDetails(
        status = EtmpObligationsFulfilmentStatus.Fulfilled,
        periodKey = "23AL"
      ),
      EtmpObligationDetails(
        status = EtmpObligationsFulfilmentStatus.Fulfilled,
        periodKey = "22AJ"
      ),
      EtmpObligationDetails(
        status = EtmpObligationsFulfilmentStatus.Fulfilled,
        periodKey = "22AK"
      ),
      EtmpObligationDetails(
        status = EtmpObligationsFulfilmentStatus.Fulfilled,
        periodKey = "22AL"
      )
    )
  )))

  val singleCorrectionPeriods: EtmpObligations = EtmpObligations(obligations = Seq(EtmpObligation(
    obligationDetails = Seq(
      EtmpObligationDetails(
        status = EtmpObligationsFulfilmentStatus.Fulfilled,
        periodKey = "23AK"
      ),
      EtmpObligationDetails(
        status = EtmpObligationsFulfilmentStatus.Open,
        periodKey = "23AL"
      )
    )
  )))

  val firstPeriodNoCorrections: EtmpObligations = EtmpObligations(obligations = Seq(EtmpObligation(
    obligationDetails = Seq(
      EtmpObligationDetails(
        status = EtmpObligationsFulfilmentStatus.Open,
        periodKey = "23AL"
      )
    )
  )))

  val previousThreeMonthsSubmittedPeriods: EtmpObligations = {
    val threeMonthsAgoPeriod = getEtmpStringFromDate(LocalDate.now().minusMonths(3))
    val twoMonthsAgoPeriod = getEtmpStringFromDate(LocalDate.now().minusMonths(2))
    val oneMonthAgoPeriod = getEtmpStringFromDate(LocalDate.now().minusMonths(1))

    EtmpObligations(obligations =
      Seq(EtmpObligation(
        obligationDetails = Seq(
          EtmpObligationDetails(
            status = EtmpObligationsFulfilmentStatus.Fulfilled,
            periodKey = threeMonthsAgoPeriod
          ),
          EtmpObligationDetails(
            status = EtmpObligationsFulfilmentStatus.Fulfilled,
            periodKey = twoMonthsAgoPeriod
          ),
          EtmpObligationDetails(
            status = EtmpObligationsFulfilmentStatus.Fulfilled,
            periodKey = oneMonthAgoPeriod
          )
        )
      ))
    )
  }

  val previousSixMonthsSubmittedPeriods: EtmpObligations = {
    val fourteenMonthsAgoPeriod = getEtmpStringFromDate(LocalDate.now().minusMonths(14))
    val thirteenMonthsAgoPeriod = getEtmpStringFromDate(LocalDate.now().minusMonths(13))
    val twelveMonthsAgoPeriod = getEtmpStringFromDate(LocalDate.now().minusMonths(12))
    val threeMonthsAgoPeriod = getEtmpStringFromDate(LocalDate.now().minusMonths(3))
    val twoMonthsAgoPeriod = getEtmpStringFromDate(LocalDate.now().minusMonths(2))
    val oneMonthAgoPeriod = getEtmpStringFromDate(LocalDate.now().minusMonths(1))

    EtmpObligations(obligations =
      Seq(EtmpObligation(
        obligationDetails = Seq(
          EtmpObligationDetails(
            status = EtmpObligationsFulfilmentStatus.Fulfilled,
            periodKey = fourteenMonthsAgoPeriod
          ),
          EtmpObligationDetails(
            status = EtmpObligationsFulfilmentStatus.Fulfilled,
            periodKey = thirteenMonthsAgoPeriod
          ),
          EtmpObligationDetails(
            status = EtmpObligationsFulfilmentStatus.Fulfilled,
            periodKey = twelveMonthsAgoPeriod
          ),
          EtmpObligationDetails(
            status = EtmpObligationsFulfilmentStatus.Fulfilled,
            periodKey = threeMonthsAgoPeriod
          ),
          EtmpObligationDetails(
            status = EtmpObligationsFulfilmentStatus.Fulfilled,
            periodKey = twoMonthsAgoPeriod
          ),
          EtmpObligationDetails(
            status = EtmpObligationsFulfilmentStatus.Fulfilled,
            periodKey = oneMonthAgoPeriod
          )
        )
      ))
    )
  }

  val previousMonthSubmittedPeriod: EtmpObligations = {
    val oneMonthAgoPeriod = getEtmpStringFromDate(LocalDate.now().minusMonths(1))

    EtmpObligations(obligations =
      Seq(EtmpObligation(
        obligationDetails = Seq(
          EtmpObligationDetails(
            status = EtmpObligationsFulfilmentStatus.Fulfilled,
            periodKey = oneMonthAgoPeriod
          )
        )
      ))
    )
  }

  private def getEtmpStringFromDate(date: LocalDate): String = {
    s"${toEtmpYearString(date.getYear)}${toEtmpMonthString(date.getMonth)}"
  }

  private def toEtmpYearString(year: Int): String =
    year.toString.substring(2)

  private def toEtmpMonthString(month: Month): String = {
    month match {
      case JANUARY => "AA"
      case FEBRUARY => "AB"
      case MARCH => "AC"
      case APRIL => "AD"
      case MAY => "AE"
      case JUNE => "AF"
      case JULY => "AG"
      case AUGUST => "AH"
      case SEPTEMBER => "AI"
      case OCTOBER => "AJ"
      case NOVEMBER => "AK"
      case DECEMBER => "AL"
    }
  }
}
