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

package uk.gov.hmrc.iossreturnsstub.models

import play.api.libs.functional.syntax._
import play.api.libs.json._
import play.api.libs.json.Reads._
import uk.gov.hmrc.iossreturnsstub.utils.ValidationUtils._

import java.time.{Instant, LocalDate}
import java.util.UUID

case class CoreTraderId(IOSSNumber: String, issuedBy: String)

object CoreTraderId {

  implicit val format: Format[CoreTraderId] = Format.apply[CoreTraderId](
    (
      (__ \ "IOSSNumber").read[String](minLength[String](1) keepAnd maxLength[String](12)) and
        (__ \ "issuedBy").read[String](minLength[String](2) keepAnd maxLength[String](2) keepAnd
          Reads.pattern("XI".r, "Country code must be XI"))
      ) (CoreTraderId.apply _),
    (
      (__ \ "IOSSNumber").write[String] and
        (__ \ "issuedBy").write[String]
      ) (coreTraderId => Tuple.fromProductTyped(coreTraderId))
  )
}

case class CorePeriod(year: Int, month: String)

object CorePeriod {

  def apply(year: Int, month: Int): CorePeriod = {
    val formattedMonth = if(month < 10) {
      s"0$month"
    } else {
      month.toString
    }
    CorePeriod(year, formattedMonth)
  }

  implicit val format: Format[CorePeriod] = Format.apply[CorePeriod](
    (
      (__ \ "year").read[Int](min(2021) keepAnd
        filter[Int](JsonValidationError("Year should be represented in 4 digits"))(
          ((i: Int) => i.toString.length == 4))) and
        (__ \ "month").read[String]
      ) ((year, month) => CorePeriod.apply(year, month)),
    (
      (__ \ "year").write[Int] and
        (__ \ "month").write[String]
      ) (corePeriod => Tuple.fromProductTyped(corePeriod))
  )
}

case class CoreSupply(
                       supplyType: String,
                       vatRate: BigDecimal,
                       vatRateType: String,
                       taxableAmountGBP: BigDecimal,
                       vatAmountGBP: BigDecimal
                     )

object CoreSupply {

  implicit val format: Format[CoreSupply] = Format.apply[CoreSupply](
    (
      (__ \ "supplyType").read[String] and
        (__ \ "vatRate").read[BigDecimal](vatRateRead) and
        (__ \ "vatRateType").read[String] and
        (__ \ "taxableAmountGBP").read[BigDecimal](currencyRead) and
        (__ \ "vatAmountGBP").read[BigDecimal](currencyRead)
      ) (CoreSupply.apply _),
    (
      (__ \ "supplyType").write[String] and
        (__ \ "vatRate").write[BigDecimal] and
        (__ \ "vatRateType").write[String] and
        (__ \ "taxableAmountGBP").write[BigDecimal] and
        (__ \ "vatAmountGBP").write[BigDecimal]
      ) (coreSupply => Tuple.fromProductTyped(coreSupply))
  )

}

trait CoreEuTraderId

object CoreEuTraderId {

  implicit val reads: Reads[CoreEuTraderId] =
    CoreEuTraderVatId.format.widen[CoreEuTraderId] orElse
      CoreEuTraderTaxId.format.widen[CoreEuTraderId]

  implicit val writes: Writes[CoreEuTraderId] = Writes {
    case vatId: CoreEuTraderVatId => Json.toJson(vatId)(CoreEuTraderVatId.format)
    case taxId: CoreEuTraderTaxId => Json.toJson(taxId)(CoreEuTraderTaxId.format)
  }
}

case class CoreEuTraderVatId(vatIdNumber: String, issuedBy: String) extends CoreEuTraderId

object CoreEuTraderVatId {
  implicit val format: OFormat[CoreEuTraderVatId] = Json.format[CoreEuTraderVatId]
}

case class CoreEuTraderTaxId(taxRefNumber: String, issuedBy: String) extends CoreEuTraderId

object CoreEuTraderTaxId {
  implicit val format: OFormat[CoreEuTraderTaxId] = Json.format[CoreEuTraderTaxId]
}


case class CoreMsestSupply(
                            countryCode: Option[String],
                            euTraderId: Option[CoreEuTraderId],
                            supplies: List[CoreSupply]
                          )

object CoreMsestSupply {
  implicit val format: OFormat[CoreMsestSupply] = Json.format[CoreMsestSupply]
}


case class CoreCorrection(
                           period: CorePeriod,
                           totalVatAmountCorrectionGBP: BigDecimal
                         )

object CoreCorrection {

  implicit val format: Format[CoreCorrection] = Format.apply[CoreCorrection](
    (
      (__ \ "period").read[CorePeriod] and
        (__ \ "totalVatAmountCorrectionGBP").read[BigDecimal](currencyAllowNegativeRead)
      ) (CoreCorrection.apply _),
    (
      (__ \ "period").write[CorePeriod] and
        (__ \ "totalVatAmountCorrectionGBP").write[BigDecimal]
      ) (coreCorrection => Tuple.fromProductTyped(coreCorrection))
  )
}

case class CoreMsconSupply(
                            msconCountryCode: String,
                            balanceOfVatDueGBP: BigDecimal,
                            grandTotalMsidGoodsGBP: BigDecimal,
                            correctionsTotalGBP: BigDecimal,
                            msidSupplies: List[CoreSupply],
                            corrections: List[CoreCorrection]
                          )

object CoreMsconSupply {

  implicit val format: Format[CoreMsconSupply] = Format.apply[CoreMsconSupply](
    (
      (__ \ "msconCountryCode").read[String] and
        (__ \ "balanceOfVatDueGBP").read[BigDecimal](currencyAllowNegativeRead) and
        (__ \ "grandTotalMsidGoodsGBP").read[BigDecimal](currencyRead) and
        (__ \ "correctionsTotalGBP").read[BigDecimal](currencyAllowNegativeRead) and
        (__ \ "msidSupplies").read[List[CoreSupply]] and
        (__ \ "corrections").read[List[CoreCorrection]]
      ) (CoreMsconSupply.apply _),
    (
      (__ \ "msconCountryCode").write[String] and
        (__ \ "balanceOfVatDueGBP").write[BigDecimal] and
        (__ \ "grandTotalMsidGoodsGBP").write[BigDecimal] and
        (__ \ "correctionsTotalGBP").write[BigDecimal] and
        (__ \ "msidSupplies").write[List[CoreSupply]] and
        (__ \ "corrections").write[List[CoreCorrection]]
      ) (coreMsconSupply => Tuple.fromProductTyped(coreMsconSupply))
  )
}

case class CoreVatReturn(
                          vatReturnReferenceNumber: String,
                          version: String,
                          traderId: CoreTraderId,
                          changeDate: String,
                          period: CorePeriod,
                          startDate: LocalDate,
                          endDate: LocalDate,
                          submissionDateTime: Instant,
                          totalAmountVatDueGBP: BigDecimal,
                          msconSupplies: List[CoreMsconSupply]
                        )

object CoreVatReturn {

  implicit val format: Format[CoreVatReturn] = Format.apply[CoreVatReturn](
    (
      (__ \ "vatReturnReferenceNumber").read[String] and
        (__ \ "version").read[String] and
        (__ \ "traderId").read[CoreTraderId] and
        (__ \ "changeDate").read[String] and
        (__ \ "period").read[CorePeriod] and
        (__ \ "startDate").read[LocalDate] and
        (__ \ "endDate").read[LocalDate] and
        (__ \ "submissionDateTime").read[Instant] and
        (__ \ "totalAmountVatDueGBP").read[BigDecimal](currencyRead) and
        (__ \ "msconSupplies").read[List[CoreMsconSupply]]
      ) (CoreVatReturn.apply _),
    (
      (__ \ "vatReturnReferenceNumber").write[String] and
        (__ \ "version").write[String] and
        (__ \ "traderId").write[CoreTraderId] and
        (__ \ "changeDate").write[String] and
        (__ \ "period").write[CorePeriod] and
        (__ \ "startDate").write[LocalDate] and
        (__ \ "endDate").write[LocalDate] and
        (__ \ "submissionDateTime").write[Instant] and
        (__ \ "totalAmountVatDueGBP").write[BigDecimal] and
        (__ \ "msconSupplies").write[List[CoreMsconSupply]]
      ) (coreVatReturn => Tuple.fromProductTyped(coreVatReturn))
  )
}

case class CoreErrorResponse(
                              timestamp: Instant,
                              transactionId: Option[UUID],
                              errorCode: String,
                              errorMessage: String
                            )

object CoreErrorResponse {
  implicit val format: OFormat[CoreErrorResponse] = Json.format[CoreErrorResponse]
}

case class EisErrorResponse(
                             errorDetail: CoreErrorResponse
                           )

object EisErrorResponse {
  implicit val format: OFormat[EisErrorResponse] = Json.format[EisErrorResponse]
}