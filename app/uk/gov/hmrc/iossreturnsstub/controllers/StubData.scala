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
import uk.gov.hmrc.iossreturnsstub.models.etmp.{EtmpObligationDetails, EtmpObligations, EtmpObligationsFulfilmentStatus}

import java.time.{LocalDate, Month}

object StubData {

  val firstDay1 = LocalDate.of(2021, Month.JULY, 1)
  val lastDay1 = LocalDate.of(2021, Month.SEPTEMBER, 30)
  val firstDay2 = LocalDate.of(2021, Month.OCTOBER, 1)
  val lastDay2 = LocalDate.of(2021, Month.DECEMBER, 31)
  val firstDay3 = LocalDate.of(2022, Month.JULY, 1)
  val lastDay3 = LocalDate.of(2022, Month.SEPTEMBER, 30)

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
      taxPeriodFrom = Some(firstDay1),
      taxPeriodTo = Some(lastDay1),
      originalAmount = Some(BigDecimal(1500)),
      outstandingAmount = Some(BigDecimal(500)),
      clearedAmount = Some(BigDecimal(1000)),
      items = Some(items)
    ))

  val allPaidItems = Seq(
    Item(
      amount = Some(BigDecimal(1500)),
      clearingReason = Some("01"),
      paymentReference = Some("a"),
      paymentAmount = Some(BigDecimal(1500)),
      paymentMethod = Some("A")
    )
  )

  val allPaidFinancialTransactions = Seq(
    FinancialTransaction(
      chargeType = Some("G Ret FR EU-OMS"),
      mainType = None,
      taxPeriodFrom = Some(firstDay1),
      taxPeriodTo = Some(lastDay1),
      originalAmount = Some(BigDecimal(1500)),
      outstandingAmount = Some(BigDecimal(0)),
      clearedAmount = Some(BigDecimal(1500)),
      items = Some(allPaidItems)
    )
  )

  val somePaidItems = Seq(
    Item(
      amount = Some(BigDecimal(500)),
      clearingReason = Some("01"),
      paymentReference = Some("a"),
      paymentAmount = Some(BigDecimal(500)),
      paymentMethod = Some("A")
    )
  )
  val somePaidFinancialTransactions = Seq(
    FinancialTransaction(
      chargeType = Some("G Ret FR EU-OMS"),
      mainType = None,
      taxPeriodFrom = Some(firstDay1),
      taxPeriodTo = Some(lastDay1),
      originalAmount = Some(BigDecimal(1500)),
      outstandingAmount = Some(BigDecimal(1000)),
      clearedAmount = Some(BigDecimal(500)),
      items = Some(somePaidItems)
    )
  )

  val multipleItemsNotPaidFinancialTransactions = Seq(
    FinancialTransaction(
      chargeType = Some("G Ret FR EU-OMS"),
      mainType = None,
      taxPeriodFrom = Some(firstDay1),
      taxPeriodTo = Some(lastDay1),
      originalAmount = Some(BigDecimal(1500)),
      outstandingAmount = Some(BigDecimal(1000)),
      clearedAmount = Some(BigDecimal(500)),
      items = Some(somePaidItems)
    ),
    FinancialTransaction(
      chargeType = Some("G Ret AT EU-OMS"),
      mainType = None,
      taxPeriodFrom = Some(firstDay2),
      taxPeriodTo = Some(lastDay2),
      originalAmount = Some(BigDecimal(1500)),
      outstandingAmount = Some(BigDecimal(1500)),
      clearedAmount = Some(BigDecimal(0)),
      items = None
    ),
    FinancialTransaction(
      chargeType = Some("G Ret ES EU-OMS"),
      mainType = None,
      taxPeriodFrom = Some(firstDay3),
      taxPeriodTo = Some(lastDay3),
      originalAmount = Some(BigDecimal(2500.99)),
      outstandingAmount = Some(BigDecimal(2500.99)),
      clearedAmount = Some(BigDecimal(0)),
      items = None
    )
  )

  val notPaidFinancialTransactions = Seq(
    FinancialTransaction(
      chargeType = Some("G Ret FR EU-OMS"),
      mainType = None,
      taxPeriodFrom = Some(firstDay1),
      taxPeriodTo = Some(lastDay1),
      originalAmount = Some(BigDecimal(1500)),
      outstandingAmount = Some(BigDecimal(1500)),
      clearedAmount = Some(BigDecimal(0)),
      items = None
    )
  )

  val defaultObligationsResponse: EtmpObligations = EtmpObligations(
    referenceNumber = "IM9001234567",
    referenceType = "IOSS",
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

  val multipleCorrectionPeriods: EtmpObligations = EtmpObligations(
    referenceNumber = "IM9001234568",
    referenceType = "IOSS",
    obligationDetails = Seq(
      EtmpObligationDetails(
        status = EtmpObligationsFulfilmentStatus.Open,
        periodKey = "23AL"
      ),
      EtmpObligationDetails(
        status = EtmpObligationsFulfilmentStatus.Open,
        periodKey = "23AK"
      ),
      EtmpObligationDetails(
        status = EtmpObligationsFulfilmentStatus.Open,
        periodKey = "23AJ"
      )
    )
  )

  val multipleCorrectionPeriodYears: EtmpObligations = EtmpObligations(
    referenceNumber = "IM9001234566",
    referenceType = "IOSS",
    obligationDetails = Seq(
      EtmpObligationDetails(
        status = EtmpObligationsFulfilmentStatus.Open,
        periodKey = "23AL"
      ),
      EtmpObligationDetails(
        status = EtmpObligationsFulfilmentStatus.Open,
        periodKey = "23AK"
      ),
      EtmpObligationDetails(
        status = EtmpObligationsFulfilmentStatus.Open,
        periodKey = "23AJ"
      ),
      EtmpObligationDetails(
        status = EtmpObligationsFulfilmentStatus.Open,
        periodKey = "22AL"
      ),
      EtmpObligationDetails(
        status = EtmpObligationsFulfilmentStatus.Open,
        periodKey = "22AK"
      ),
      EtmpObligationDetails(
        status = EtmpObligationsFulfilmentStatus.Open,
        periodKey = "22AJ"
      )
    )
  )

  val singleCorrectionPeriods: EtmpObligations = EtmpObligations(
    referenceNumber = "IM9008888888",
    referenceType = "IOSS",
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

  val firstPeriodNoCorrections: EtmpObligations = EtmpObligations(
    referenceNumber = "IM9009999999",
    referenceType = "IOSS",
    obligationDetails = Seq(
      EtmpObligationDetails(
        status = EtmpObligationsFulfilmentStatus.Open,
        periodKey = "23AK"
      )
    )
  )
}
