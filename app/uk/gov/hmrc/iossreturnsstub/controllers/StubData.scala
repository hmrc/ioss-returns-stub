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

  private val firstDayOfSixMonthsAgoPeriod = LocalDate.now().minusMonths(6).withDayOfMonth(1)
  private val lastDayOfSixMonthsAgoPeriod = LocalDate.now().minusMonths(5).withDayOfMonth(1).minusDays(1)
  private val firstDayOfFiveMonthsAgoPeriod = LocalDate.now().minusMonths(5).withDayOfMonth(1)
  private val lastDayOfFiveMonthsAgoPeriod = LocalDate.now().minusMonths(4).withDayOfMonth(1).minusDays(1)
  private val firstDayOfFourMonthsAgoPeriod = LocalDate.now().minusMonths(4).withDayOfMonth(1)
  private val lastDayOfFourMonthsAgoPeriod = LocalDate.now().minusMonths(3).withDayOfMonth(1).minusDays(1)

  private val firstDayOfNineMonthsAgoPeriod = LocalDate.now().minusMonths(9).withDayOfMonth(1)
  private val lastDayOfNineMonthsAgoPeriod = LocalDate.now().minusMonths(8).withDayOfMonth(1).minusDays(1)
  private val firstDayOfEightMonthsAgoPeriod = LocalDate.now().minusMonths(8).withDayOfMonth(1)
  private val lastDayOfEightMonthsAgoPeriod = LocalDate.now().minusMonths(7).withDayOfMonth(1).minusDays(1)
  private val firstDayOfSevenMonthsAgoPeriod = LocalDate.now().minusMonths(7).withDayOfMonth(1)
  private val lastDayOfSevenMonthsAgoPeriod = LocalDate.now().minusMonths(6).withDayOfMonth(1).minusDays(1)


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

  val threeReturnsTwoOutstandingOnePaidPreviousRegistration = Seq(
    FinancialTransaction(
      chargeType = Some("G Ret FR EU-OMS"),
      mainType = None,
      taxPeriodFrom = Some(firstDayOfNineMonthsAgoPeriod),
      taxPeriodTo = Some(lastDayOfNineMonthsAgoPeriod),
      originalAmount = Some(BigDecimal(2000)),
      outstandingAmount = Some(BigDecimal(1750)),
      clearedAmount = Some(BigDecimal(250)),
      items = Some(items)
    ),
    FinancialTransaction(
      chargeType = Some("G Ret FR EU-OMS"),
      mainType = None,
      taxPeriodFrom = Some(firstDayOfEightMonthsAgoPeriod),
      taxPeriodTo = Some(lastDayOfEightMonthsAgoPeriod),
      originalAmount = Some(BigDecimal(2000)),
      outstandingAmount = Some(BigDecimal(1000)),
      clearedAmount = Some(BigDecimal(1000)),
      items = Some(items)
    ),
    FinancialTransaction(
      chargeType = Some("G Ret FR EU-OMS"),
      mainType = None,
      taxPeriodFrom = Some(firstDayOfSevenMonthsAgoPeriod),
      taxPeriodTo = Some(lastDayOfSevenMonthsAgoPeriod),
      originalAmount = Some(BigDecimal(2000)),
      outstandingAmount = Some(BigDecimal(0)),
      clearedAmount = Some(BigDecimal(2000)),
      items = Some(items)
    )
  )

  val threeReturnsOutstandingPreviousRegistration = Seq(
    FinancialTransaction(
      chargeType = Some("G Ret FR EU-OMS"),
      mainType = None,
      taxPeriodFrom = Some(firstDayOfSixMonthsAgoPeriod),
      taxPeriodTo = Some(lastDayOfSixMonthsAgoPeriod),
      originalAmount = Some(BigDecimal(2000)),
      outstandingAmount = Some(BigDecimal(500)),
      clearedAmount = Some(BigDecimal(1500)),
      items = Some(items)
    ),
    FinancialTransaction(
      chargeType = Some("G Ret FR EU-OMS"),
      mainType = None,
      taxPeriodFrom = Some(firstDayOfFiveMonthsAgoPeriod),
      taxPeriodTo = Some(lastDayOfFiveMonthsAgoPeriod),
      originalAmount = Some(BigDecimal(2000)),
      outstandingAmount = Some(BigDecimal(500)),
      clearedAmount = Some(BigDecimal(1500)),
      items = Some(items)
    ),
    FinancialTransaction(
      chargeType = Some("G Ret FR EU-OMS"),
      mainType = None,
      taxPeriodFrom = Some(firstDayOfFourMonthsAgoPeriod),
      taxPeriodTo = Some(lastDayOfFourMonthsAgoPeriod),
      originalAmount = Some(BigDecimal(2000)),
      outstandingAmount = Some(BigDecimal(500)),
      clearedAmount = Some(BigDecimal(1500)),
      items = Some(items)
    )
  )

  val threeReturnsOneOutstandingTwoPaidPreviousRegistration = Seq(
    FinancialTransaction(
      chargeType = Some("G Ret FR EU-OMS"),
      mainType = None,
      taxPeriodFrom = Some(firstDayOfSixMonthsAgoPeriod),
      taxPeriodTo = Some(lastDayOfSixMonthsAgoPeriod),
      originalAmount = Some(BigDecimal(2000)),
      outstandingAmount = Some(BigDecimal(0)),
      clearedAmount = Some(BigDecimal(2000)),
      items = Some(items)
    ),
    FinancialTransaction(
      chargeType = Some("G Ret FR EU-OMS"),
      mainType = None,
      taxPeriodFrom = Some(firstDayOfFiveMonthsAgoPeriod),
      taxPeriodTo = Some(lastDayOfFiveMonthsAgoPeriod),
      originalAmount = Some(BigDecimal(2000)),
      outstandingAmount = Some(BigDecimal(2000)),
      clearedAmount = Some(BigDecimal(0)),
      items = Some(items)
    ),
    FinancialTransaction(
      chargeType = Some("G Ret FR EU-OMS"),
      mainType = None,
      taxPeriodFrom = Some(firstDayOfFourMonthsAgoPeriod),
      taxPeriodTo = Some(lastDayOfFourMonthsAgoPeriod),
      originalAmount = Some(BigDecimal(2000)),
      outstandingAmount = Some(BigDecimal(0)),
      clearedAmount = Some(BigDecimal(2000)),
      items = Some(items)
    )
  )

  val threeReturnsOneOutstandingTwoPaidOlderPreviousRegistration = Seq(
    FinancialTransaction(
      chargeType = Some("G Ret FR EU-OMS"),
      mainType = None,
      taxPeriodFrom = Some(firstDayOfNineMonthsAgoPeriod),
      taxPeriodTo = Some(lastDayOfNineMonthsAgoPeriod),
      originalAmount = Some(BigDecimal(2000)),
      outstandingAmount = Some(BigDecimal(0)),
      clearedAmount = Some(BigDecimal(2000)),
      items = Some(items)
    ),
    FinancialTransaction(
      chargeType = Some("G Ret FR EU-OMS"),
      mainType = None,
      taxPeriodFrom = Some(firstDayOfEightMonthsAgoPeriod),
      taxPeriodTo = Some(lastDayOfEightMonthsAgoPeriod),
      originalAmount = Some(BigDecimal(2000)),
      outstandingAmount = Some(BigDecimal(1750)),
      clearedAmount = Some(BigDecimal(250)),
      items = Some(items)
    ),
    FinancialTransaction(
      chargeType = Some("G Ret FR EU-OMS"),
      mainType = None,
      taxPeriodFrom = Some(firstDayOfSevenMonthsAgoPeriod),
      taxPeriodTo = Some(lastDayOfSevenMonthsAgoPeriod),
      originalAmount = Some(BigDecimal(2000)),
      outstandingAmount = Some(BigDecimal(0)),
      clearedAmount = Some(BigDecimal(2000)),
      items = Some(items)
    )
  )

  val sixReturnsOutstanding = Seq(
    FinancialTransaction(
      chargeType = Some("G Ret FR EU-OMS"),
      mainType = None,
      taxPeriodFrom = Some(LocalDate.of(2020, Month.JULY, 1)),
      taxPeriodTo = Some(LocalDate.of(2020, Month.JULY, 31)),
      originalAmount = Some(BigDecimal(1500)),
      outstandingAmount = Some(BigDecimal(500)),
      clearedAmount = Some(BigDecimal(1000)),
      items = Some(items)
    ),
    FinancialTransaction(
      chargeType = Some("G Ret FR EU-OMS"),
      mainType = None,
      taxPeriodFrom = Some(LocalDate.of(2020, Month.AUGUST, 1)),
      taxPeriodTo = Some(LocalDate.of(2020, Month.AUGUST, 31)),
      originalAmount = Some(BigDecimal(1500)),
      outstandingAmount = Some(BigDecimal(500)),
      clearedAmount = Some(BigDecimal(1000)),
      items = Some(items)
    ),
    FinancialTransaction(
      chargeType = Some("G Ret FR EU-OMS"),
      mainType = None,
      taxPeriodFrom = Some(LocalDate.of(2020, Month.SEPTEMBER, 1)),
      taxPeriodTo = Some(LocalDate.of(2020, Month.SEPTEMBER, 30)),
      originalAmount = Some(BigDecimal(1500)),
      outstandingAmount = Some(BigDecimal(500)),
      clearedAmount = Some(BigDecimal(1000)),
      items = Some(items)
    ),
    FinancialTransaction(
      chargeType = Some("G Ret FR EU-OMS"),
      mainType = None,
      taxPeriodFrom = Some(LocalDate.of(2020, Month.OCTOBER, 1)),
      taxPeriodTo = Some(LocalDate.of(2020, Month.OCTOBER, 31)),
      originalAmount = Some(BigDecimal(1500)),
      outstandingAmount = Some(BigDecimal(500)),
      clearedAmount = Some(BigDecimal(1000)),
      items = Some(items)
    ),
    FinancialTransaction(
      chargeType = Some("G Ret FR EU-OMS"),
      mainType = None,
      taxPeriodFrom = Some(LocalDate.of(2020, Month.NOVEMBER, 1)),
      taxPeriodTo = Some(LocalDate.of(2020, Month.NOVEMBER, 30)),
      originalAmount = Some(BigDecimal(1500)),
      outstandingAmount = Some(BigDecimal(500)),
      clearedAmount = Some(BigDecimal(1000)),
      items = Some(items)
    ),
    FinancialTransaction(
      chargeType = Some("G Ret FR EU-OMS"),
      mainType = None,
      taxPeriodFrom = Some(LocalDate.of(2020, Month.DECEMBER, 1)),
      taxPeriodTo = Some(LocalDate.of(2020, Month.DECEMBER, 31)),
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
        periodKey = "23AA"
      ),
      EtmpObligationDetails(
        status = EtmpObligationsFulfilmentStatus.Fulfilled,
        periodKey = "23AB"
      ),
      EtmpObligationDetails(
        status = EtmpObligationsFulfilmentStatus.Fulfilled,
        periodKey = "23AC"
      ),
      EtmpObligationDetails(
        status = EtmpObligationsFulfilmentStatus.Fulfilled,
        periodKey = "23AD"
      ),
      EtmpObligationDetails(
        status = EtmpObligationsFulfilmentStatus.Fulfilled,
        periodKey = "23AE"
      ),
      EtmpObligationDetails(
        status = EtmpObligationsFulfilmentStatus.Fulfilled,
        periodKey = "23AF"
      ),
      EtmpObligationDetails(
        status = EtmpObligationsFulfilmentStatus.Fulfilled,
        periodKey = "23AG"
      ),
      EtmpObligationDetails(
        status = EtmpObligationsFulfilmentStatus.Fulfilled,
        periodKey = "23AH"
      ),
      EtmpObligationDetails(
        status = EtmpObligationsFulfilmentStatus.Fulfilled,
        periodKey = "23AI"
      ),
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

  val moreThanThreeCorrectionPeriodYears: EtmpObligations = EtmpObligations(obligations = Seq(EtmpObligation(
    obligationDetails = Seq(
      EtmpObligationDetails(
        status = EtmpObligationsFulfilmentStatus.Fulfilled,
        periodKey = "23AA"
      ),
      EtmpObligationDetails(
        status = EtmpObligationsFulfilmentStatus.Fulfilled,
        periodKey = "23AB"
      ),
      EtmpObligationDetails(
        status = EtmpObligationsFulfilmentStatus.Fulfilled,
        periodKey = "23AC"
      ),
      EtmpObligationDetails(
        status = EtmpObligationsFulfilmentStatus.Fulfilled,
        periodKey = "23AD"
      ),
      EtmpObligationDetails(
        status = EtmpObligationsFulfilmentStatus.Fulfilled,
        periodKey = "23AE"
      ),
      EtmpObligationDetails(
        status = EtmpObligationsFulfilmentStatus.Fulfilled,
        periodKey = "23AF"
      ),
      EtmpObligationDetails(
        status = EtmpObligationsFulfilmentStatus.Fulfilled,
        periodKey = "23AG"
      ),
      EtmpObligationDetails(
        status = EtmpObligationsFulfilmentStatus.Fulfilled,
        periodKey = "23AH"
      ),
      EtmpObligationDetails(
        status = EtmpObligationsFulfilmentStatus.Fulfilled,
        periodKey = "23AI"
      ),
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
        periodKey = "22AA"
      ),
      EtmpObligationDetails(
        status = EtmpObligationsFulfilmentStatus.Fulfilled,
        periodKey = "22AB"
      ),
      EtmpObligationDetails(
        status = EtmpObligationsFulfilmentStatus.Fulfilled,
        periodKey = "22AC"
      ),
      EtmpObligationDetails(
        status = EtmpObligationsFulfilmentStatus.Fulfilled,
        periodKey = "22AD"
      ),
      EtmpObligationDetails(
        status = EtmpObligationsFulfilmentStatus.Fulfilled,
        periodKey = "22AE"
      ),
      EtmpObligationDetails(
        status = EtmpObligationsFulfilmentStatus.Fulfilled,
        periodKey = "22AF"
      ),
      EtmpObligationDetails(
        status = EtmpObligationsFulfilmentStatus.Fulfilled,
        periodKey = "22AG"
      ),
      EtmpObligationDetails(
        status = EtmpObligationsFulfilmentStatus.Fulfilled,
        periodKey = "22AH"
      ),
      EtmpObligationDetails(
        status = EtmpObligationsFulfilmentStatus.Fulfilled,
        periodKey = "22AI"
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
      ),
      EtmpObligationDetails(
        status = EtmpObligationsFulfilmentStatus.Fulfilled,
        periodKey = "21AA"
      ),
      EtmpObligationDetails(
        status = EtmpObligationsFulfilmentStatus.Fulfilled,
        periodKey = "21AB"
      ),
      EtmpObligationDetails(
        status = EtmpObligationsFulfilmentStatus.Fulfilled,
        periodKey = "21AC"
      ),
      EtmpObligationDetails(
        status = EtmpObligationsFulfilmentStatus.Fulfilled,
        periodKey = "21AD"
      ),
      EtmpObligationDetails(
        status = EtmpObligationsFulfilmentStatus.Fulfilled,
        periodKey = "21AE"
      ),
      EtmpObligationDetails(
        status = EtmpObligationsFulfilmentStatus.Fulfilled,
        periodKey = "21AF"
      ),
      EtmpObligationDetails(
        status = EtmpObligationsFulfilmentStatus.Fulfilled,
        periodKey = "21AG"
      ),
      EtmpObligationDetails(
        status = EtmpObligationsFulfilmentStatus.Fulfilled,
        periodKey = "21AH"
      ),
      EtmpObligationDetails(
        status = EtmpObligationsFulfilmentStatus.Fulfilled,
        periodKey = "21AI"
      ),
      EtmpObligationDetails(
        status = EtmpObligationsFulfilmentStatus.Fulfilled,
        periodKey = "21AJ"
      ),
      EtmpObligationDetails(
        status = EtmpObligationsFulfilmentStatus.Fulfilled,
        periodKey = "21AK"
      ),
      EtmpObligationDetails(
        status = EtmpObligationsFulfilmentStatus.Fulfilled,
        periodKey = "21AL"
      ),
      EtmpObligationDetails(
        status = EtmpObligationsFulfilmentStatus.Fulfilled,
        periodKey = "20AG"
      ),
      EtmpObligationDetails(
        status = EtmpObligationsFulfilmentStatus.Fulfilled,
        periodKey = "20AH"
      ),
      EtmpObligationDetails(
        status = EtmpObligationsFulfilmentStatus.Fulfilled,
        periodKey = "20AI"
      ),
      EtmpObligationDetails(
        status = EtmpObligationsFulfilmentStatus.Fulfilled,
        periodKey = "20AJ"
      ),
      EtmpObligationDetails(
        status = EtmpObligationsFulfilmentStatus.Fulfilled,
        periodKey = "20AK"
      ),
      EtmpObligationDetails(
        status = EtmpObligationsFulfilmentStatus.Fulfilled,
        periodKey = "20AL"
      )
    )
  )))

  val moreThanThreeYearsOpenReturns: EtmpObligations = EtmpObligations(obligations = Seq(EtmpObligation(
    obligationDetails = Seq(
      EtmpObligationDetails(
        status = EtmpObligationsFulfilmentStatus.Open,
        periodKey = "23AA"
      ),
      EtmpObligationDetails(
        status = EtmpObligationsFulfilmentStatus.Open,
        periodKey = "23AB"
      ),
      EtmpObligationDetails(
        status = EtmpObligationsFulfilmentStatus.Open,
        periodKey = "23AC"
      ),
      EtmpObligationDetails(
        status = EtmpObligationsFulfilmentStatus.Open,
        periodKey = "23AD"
      ),
      EtmpObligationDetails(
        status = EtmpObligationsFulfilmentStatus.Open,
        periodKey = "23AE"
      ),
      EtmpObligationDetails(
        status = EtmpObligationsFulfilmentStatus.Open,
        periodKey = "23AF"
      ),
      EtmpObligationDetails(
        status = EtmpObligationsFulfilmentStatus.Open,
        periodKey = "23AG"
      ),
      EtmpObligationDetails(
        status = EtmpObligationsFulfilmentStatus.Open,
        periodKey = "23AH"
      ),
      EtmpObligationDetails(
        status = EtmpObligationsFulfilmentStatus.Open,
        periodKey = "23AI"
      ),
      EtmpObligationDetails(
        status = EtmpObligationsFulfilmentStatus.Open,
        periodKey = "23AJ"
      ),
      EtmpObligationDetails(
        status = EtmpObligationsFulfilmentStatus.Open,
        periodKey = "23AK"
      ),
      EtmpObligationDetails(
        status = EtmpObligationsFulfilmentStatus.Open,
        periodKey = "23AL"
      ),
      EtmpObligationDetails(
        status = EtmpObligationsFulfilmentStatus.Open,
        periodKey = "22AA"
      ),
      EtmpObligationDetails(
        status = EtmpObligationsFulfilmentStatus.Open,
        periodKey = "22AB"
      ),
      EtmpObligationDetails(
        status = EtmpObligationsFulfilmentStatus.Open,
        periodKey = "22AC"
      ),
      EtmpObligationDetails(
        status = EtmpObligationsFulfilmentStatus.Open,
        periodKey = "22AD"
      ),
      EtmpObligationDetails(
        status = EtmpObligationsFulfilmentStatus.Open,
        periodKey = "22AE"
      ),
      EtmpObligationDetails(
        status = EtmpObligationsFulfilmentStatus.Open,
        periodKey = "22AF"
      ),
      EtmpObligationDetails(
        status = EtmpObligationsFulfilmentStatus.Open,
        periodKey = "22AG"
      ),
      EtmpObligationDetails(
        status = EtmpObligationsFulfilmentStatus.Open,
        periodKey = "22AH"
      ),
      EtmpObligationDetails(
        status = EtmpObligationsFulfilmentStatus.Open,
        periodKey = "22AI"
      ),
      EtmpObligationDetails(
        status = EtmpObligationsFulfilmentStatus.Open,
        periodKey = "22AJ"
      ),
      EtmpObligationDetails(
        status = EtmpObligationsFulfilmentStatus.Open,
        periodKey = "22AK"
      ),
      EtmpObligationDetails(
        status = EtmpObligationsFulfilmentStatus.Open,
        periodKey = "22AL"
      ),
      EtmpObligationDetails(
        status = EtmpObligationsFulfilmentStatus.Open,
        periodKey = "21AA"
      ),
      EtmpObligationDetails(
        status = EtmpObligationsFulfilmentStatus.Open,
        periodKey = "21AB"
      ),
      EtmpObligationDetails(
        status = EtmpObligationsFulfilmentStatus.Open,
        periodKey = "21AC"
      ),
      EtmpObligationDetails(
        status = EtmpObligationsFulfilmentStatus.Open,
        periodKey = "21AD"
      ),
      EtmpObligationDetails(
        status = EtmpObligationsFulfilmentStatus.Open,
        periodKey = "21AE"
      ),
      EtmpObligationDetails(
        status = EtmpObligationsFulfilmentStatus.Open,
        periodKey = "21AF"
      ),
      EtmpObligationDetails(
        status = EtmpObligationsFulfilmentStatus.Open,
        periodKey = "21AG"
      ),
      EtmpObligationDetails(
        status = EtmpObligationsFulfilmentStatus.Open,
        periodKey = "21AH"
      ),
      EtmpObligationDetails(
        status = EtmpObligationsFulfilmentStatus.Open,
        periodKey = "21AI"
      ),
      EtmpObligationDetails(
        status = EtmpObligationsFulfilmentStatus.Open,
        periodKey = "21AJ"
      ),
      EtmpObligationDetails(
        status = EtmpObligationsFulfilmentStatus.Open,
        periodKey = "21AK"
      ),
      EtmpObligationDetails(
        status = EtmpObligationsFulfilmentStatus.Open,
        periodKey = "21AL"
      ),
      EtmpObligationDetails(
        status = EtmpObligationsFulfilmentStatus.Open,
        periodKey = "20AG"
      ),
      EtmpObligationDetails(
        status = EtmpObligationsFulfilmentStatus.Open,
        periodKey = "20AH"
      ),
      EtmpObligationDetails(
        status = EtmpObligationsFulfilmentStatus.Open,
        periodKey = "20AI"
      ),
      EtmpObligationDetails(
        status = EtmpObligationsFulfilmentStatus.Open,
        periodKey = "20AJ"
      ),
      EtmpObligationDetails(
        status = EtmpObligationsFulfilmentStatus.Open,
        periodKey = "20AK"
      ),
      EtmpObligationDetails(
        status = EtmpObligationsFulfilmentStatus.Open,
        periodKey = "20AL"
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

  val previousFourToSixMonthsSubmittedPeriods: EtmpObligations = {
    val sixMonthsAgoPeriod = getEtmpStringFromDate(LocalDate.now().minusMonths(6))
    val fiveMonthsAgoPeriod = getEtmpStringFromDate(LocalDate.now().minusMonths(5))
    val fourMonthAgoPeriod = getEtmpStringFromDate(LocalDate.now().minusMonths(4))

    EtmpObligations(obligations =
      Seq(EtmpObligation(
        obligationDetails = Seq(
          EtmpObligationDetails(
            status = EtmpObligationsFulfilmentStatus.Fulfilled,
            periodKey = sixMonthsAgoPeriod
          ),
          EtmpObligationDetails(
            status = EtmpObligationsFulfilmentStatus.Fulfilled,
            periodKey = fiveMonthsAgoPeriod
          ),
          EtmpObligationDetails(
            status = EtmpObligationsFulfilmentStatus.Fulfilled,
            periodKey = fourMonthAgoPeriod
          )
        )
      ))
    )
  }
  val previousSevenToNineMonthsSubmittedPeriods: EtmpObligations = {
    val nineMonthsAgoPeriod = getEtmpStringFromDate(LocalDate.now().minusMonths(9))
    val eightMonthsAgoPeriod = getEtmpStringFromDate(LocalDate.now().minusMonths(8))
    val sevenMonthAgoPeriod = getEtmpStringFromDate(LocalDate.now().minusMonths(7))

    EtmpObligations(obligations =
      Seq(EtmpObligation(
        obligationDetails = Seq(
          EtmpObligationDetails(
            status = EtmpObligationsFulfilmentStatus.Fulfilled,
            periodKey = nineMonthsAgoPeriod
          ),
          EtmpObligationDetails(
            status = EtmpObligationsFulfilmentStatus.Fulfilled,
            periodKey = eightMonthsAgoPeriod
          ),
          EtmpObligationDetails(
            status = EtmpObligationsFulfilmentStatus.Fulfilled,
            periodKey = sevenMonthAgoPeriod
          )
        )
      ))
    )
  }

  val sixMonthsAcrossTwoYearsSubmittedPeriods: EtmpObligations = {
    val october2022Period = getEtmpStringFromDate(LocalDate.of(2022, 10, 31))
    val november2022Period = getEtmpStringFromDate(LocalDate.of(2022, 11, 30))
    val december2022Period = getEtmpStringFromDate(LocalDate.of(2022, 12, 31))
    val october2023Period = getEtmpStringFromDate(LocalDate.of(2023, 10, 31))
    val november2023Period = getEtmpStringFromDate(LocalDate.of(2023, 11, 30))
    val december2023Period = getEtmpStringFromDate(LocalDate.of(2023, 12, 31))

    EtmpObligations(obligations =
      Seq(EtmpObligation(
        obligationDetails = Seq(
          EtmpObligationDetails(
            status = EtmpObligationsFulfilmentStatus.Fulfilled,
            periodKey = october2022Period
          ),
          EtmpObligationDetails(
            status = EtmpObligationsFulfilmentStatus.Fulfilled,
            periodKey = november2022Period
          ),
          EtmpObligationDetails(
            status = EtmpObligationsFulfilmentStatus.Fulfilled,
            periodKey = december2022Period
          ),
          EtmpObligationDetails(
            status = EtmpObligationsFulfilmentStatus.Fulfilled,
            periodKey = october2023Period
          ),
          EtmpObligationDetails(
            status = EtmpObligationsFulfilmentStatus.Fulfilled,
            periodKey = november2023Period
          ),
          EtmpObligationDetails(
            status = EtmpObligationsFulfilmentStatus.Fulfilled,
            periodKey = december2023Period
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

  val firstReturnAfterTransferringFromAnotherMSID: EtmpObligations = EtmpObligations(obligations = Seq(EtmpObligation(
    obligationDetails = Seq(
      EtmpObligationDetails(
        status = EtmpObligationsFulfilmentStatus.Open,
        periodKey = "24AA"
      )
    )
  )))

  val secondReturnAfterTransferringFromAnotherMSID: EtmpObligations = EtmpObligations(obligations = Seq(EtmpObligation(
    obligationDetails = Seq(
      EtmpObligationDetails(
        status = EtmpObligationsFulfilmentStatus.Open,
        periodKey = "24AB"
      ),
        EtmpObligationDetails(
        status = EtmpObligationsFulfilmentStatus.Fulfilled,
        periodKey = "24AA"
      )
    )
  )))

  val returnsBeforeTransferringToAnotherMSID: EtmpObligations = EtmpObligations(obligations = Seq(EtmpObligation(
    obligationDetails = Seq(
      EtmpObligationDetails(
        status = EtmpObligationsFulfilmentStatus.Open,
        periodKey = "24AB"
      ),
      EtmpObligationDetails(
        status = EtmpObligationsFulfilmentStatus.Fulfilled,
        periodKey = "24AA"
      ),
      EtmpObligationDetails(
        status = EtmpObligationsFulfilmentStatus.Fulfilled,
        periodKey = "23AL"
      )
    )
  )))

  val returnsBeforeTransferringToAnotherMSIDTwoOpen: EtmpObligations = EtmpObligations(obligations = Seq(EtmpObligation(
    obligationDetails = Seq(
      EtmpObligationDetails(
        status = EtmpObligationsFulfilmentStatus.Open,
        periodKey = "24AB"
      ),
      EtmpObligationDetails(
        status = EtmpObligationsFulfilmentStatus.Open,
        periodKey = "24AA"
      ),
      EtmpObligationDetails(
        status = EtmpObligationsFulfilmentStatus.Fulfilled,
        periodKey = "23AL"
      )
    )
  )))

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
