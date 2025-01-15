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
import org.scalatest.matchers.must.Matchers
import play.api.libs.json.{JsSuccess, Json}

class EtmpVatReturnBalanceOfVatDueSpec extends AnyFreeSpec with Matchers {

  private val genEtmpVatReturnBalanceOfVatDue: EtmpVatReturnBalanceOfVatDue = EtmpVatReturnBalanceOfVatDue(
    msOfConsumption = "DE",
    totalVATDueGBP = BigDecimal(0),
    totalVATEUR = BigDecimal(0)
  )

  "EtmpVatReturnBalanceOfVatDue" - {

    "must serialise/deserialise to and from EtmpVatReturnBalanceOfVatDue" in {

      val json = Json.obj(
        "msOfConsumption" -> genEtmpVatReturnBalanceOfVatDue.msOfConsumption,
        "totalVATDueGBP" -> genEtmpVatReturnBalanceOfVatDue.totalVATDueGBP,
        "totalVATEUR" -> genEtmpVatReturnBalanceOfVatDue.totalVATEUR
      )

      val expectedResult = EtmpVatReturnBalanceOfVatDue(
        msOfConsumption = genEtmpVatReturnBalanceOfVatDue.msOfConsumption,
        totalVATDueGBP = genEtmpVatReturnBalanceOfVatDue.totalVATDueGBP,
        totalVATEUR = genEtmpVatReturnBalanceOfVatDue.totalVATEUR
      )

      Json.toJson(expectedResult) mustBe json
      json.validate[EtmpVatReturnBalanceOfVatDue] mustBe JsSuccess(expectedResult)
    }
  }
}
