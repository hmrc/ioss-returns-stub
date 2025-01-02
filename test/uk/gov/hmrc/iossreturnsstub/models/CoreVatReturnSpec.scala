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

import org.scalatest.freespec.AnyFreeSpec
import org.scalatest.matchers.must.Matchers.{mustBe, mustEqual}
import org.scalatest.matchers.should.Matchers
import play.api.libs.json.{JsObject, Json, Reads}


class CoreVatReturnSpec extends AnyFreeSpec with Matchers {

  val vatId: CoreEuTraderVatId = CoreEuTraderVatId("VAT123456", "DE")
  val taxId: CoreEuTraderTaxId = CoreEuTraderTaxId("TAX987654", "FR")

  "CoreEuTraderId" - {

    "deserialize a CoreEuTraderId" in {

      val vatJson = Json.obj(
        "vatIdNumber" -> "VAT123456",
        "issuedBy" -> "DE"
      )

      val taxJson = Json.obj(
        "taxRefNumber" -> "TAX987654",
        "issuedBy" -> "FR"
      )

      val vatResult = vatJson.as[CoreEuTraderId]
      vatResult mustBe a[CoreEuTraderVatId]
      vatResult.asInstanceOf[CoreEuTraderVatId].vatIdNumber mustBe "VAT123456"
      vatResult.asInstanceOf[CoreEuTraderVatId].issuedBy mustBe "DE"

      val taxResult = taxJson.as[CoreEuTraderId]
      taxResult mustBe a[CoreEuTraderTaxId]
      taxResult.asInstanceOf[CoreEuTraderTaxId].taxRefNumber mustBe "TAX987654"
      taxResult.asInstanceOf[CoreEuTraderTaxId].issuedBy mustBe "FR"
    }

    "serialize a CoreEuTraderId" in {
      val vatJson = Json.toJson(vatId)
      (vatJson \ "vatIdNumber").as[String] mustBe "VAT123456"
      (vatJson \ "issuedBy").as[String] mustBe "DE"

      val taxJson = Json.toJson(taxId)
      (taxJson \ "taxRefNumber").as[String] mustBe "TAX987654"
      (taxJson \ "issuedBy").as[String] mustBe "FR"
    }

    "handle unknown discriminator in JSON for CoreEuTraderId" in {
      val unknownJson = Json.obj(
        "unknownField" -> "something",
        "issuedBy" -> "DE"
      )

      val result = unknownJson.validate[CoreEuTraderId]

      result.isError mustBe true
    }

    "handle empty JSON for CoreEuTraderId" in {
      val emptyJson = Json.obj()
      val result = emptyJson.validate[CoreEuTraderId]

      result.isError mustBe true
    }
  }

  "CoreEuTraderVatId" - {

    "deserialize a CoreEuTraderVatId correctly" in {
      val json = Json.obj(
        "vatIdNumber" -> "VAT123456",
        "issuedBy" -> "DE"
      )

      val result = json.as[CoreEuTraderId]
      result mustBe a[CoreEuTraderVatId]
      result.asInstanceOf[CoreEuTraderVatId].vatIdNumber mustBe "VAT123456"
      result.asInstanceOf[CoreEuTraderVatId].issuedBy mustBe "DE"
    }

    "serialize a CoreEuTraderVatId correctly" in {
      val json = Json.toJson(vatId)
      (json \ "vatIdNumber").as[String] mustBe "VAT123456"
      (json \ "issuedBy").as[String] mustBe "DE"
    }

    "handle invalid JSON for CoreEuTraderId" in {
      val invalidJson = Json.obj("vatIdNumber" -> "VAT123456")
      val result = invalidJson.validate[CoreEuTraderId]
      result.isError mustBe true
    }
  }

  "CoreEuTraderTaxId" - {

    "deserialize a CoreEuTraderTaxId correctly" in {
      val json = Json.obj(
        "taxRefNumber" -> "TAX987654",
        "issuedBy" -> "FR"
      )

      val result = json.as[CoreEuTraderId]
      result mustBe a[CoreEuTraderTaxId]
      result.asInstanceOf[CoreEuTraderTaxId].taxRefNumber mustBe "TAX987654"
      result.asInstanceOf[CoreEuTraderTaxId].issuedBy mustBe "FR"
    }

    "serialize a CoreEuTraderTaxId correctly" in {
      val json = Json.toJson(taxId)
      (json \ "taxRefNumber").as[String] mustBe "TAX987654"
      (json \ "issuedBy").as[String] mustBe "FR"
    }

    "handle empty JSON for CoreEuTraderId" in {
      val emptyJson = Json.obj()
      val result = emptyJson.validate[CoreEuTraderId]

      result.isError mustBe true
    }
  }

  "CoreMsestSupply" - {

    val supply1 = CoreSupply("Type1", BigDecimal(20.0), "Standard", BigDecimal(100.0), BigDecimal(20.0))
    val supply2 = CoreSupply("Type2", BigDecimal(15.0), "Reduced", BigDecimal(50.0), BigDecimal(7.5))

    val msestSupply = CoreMsestSupply(
      countryCode = Some("DE"),
      euTraderId = Some(vatId),
      supplies = List(supply1, supply2)
    )

    "serialize and deserialize correctly" in {
      val serializedJson = Json.toJson(msestSupply)
      val deserializedObject = serializedJson.as[CoreMsestSupply]

      deserializedObject mustEqual msestSupply
    }

    "ensure all fields are serialized correctly" in {
      val json = Json.toJson(msestSupply)

      (json \ "countryCode").asOpt[String] mustBe Some("DE")
      (json \ "euTraderId").asOpt[JsObject] match {
        case Some(obj) =>
          (obj \ "vatIdNumber").asOpt[String] mustBe Some("VAT123456")
          (obj \ "issuedBy").asOpt[String] mustBe Some("DE")
        case None => fail("euTraderId was not serialized correctly")
      }
      (json \ "supplies").as[List[JsObject]].size mustBe 2
    }

    "handle optional countryCode and euTraderId fields" - {

      "With countryCode and euTraderId" in {
        val jsonWithCountryCodeAndTraderId = Json.toJson(msestSupply)
        (jsonWithCountryCodeAndTraderId \ "countryCode").asOpt[String] mustBe Some("DE")
        (jsonWithCountryCodeAndTraderId \ "euTraderId").asOpt[JsObject] mustBe defined
      }

      "Without countryCode" in {
        val msestSupplyWithoutCountryCode = msestSupply.copy(countryCode = None)
        val jsonWithoutCountryCode = Json.toJson(msestSupplyWithoutCountryCode)
        (jsonWithoutCountryCode \ "countryCode").asOpt[String] mustBe None
      }

      "Without euTraderId" in {
        val msestSupplyWithoutTraderId = msestSupply.copy(euTraderId = None)
        val jsonWithoutTraderId = Json.toJson(msestSupplyWithoutTraderId)
        (jsonWithoutTraderId \ "euTraderId").asOpt[JsObject] mustBe None
      }
    }

    "handle empty supplies list" in {
      val msestSupplyWithEmptySupplies = msestSupply.copy(supplies = List.empty)
      val serializedJson = Json.toJson(msestSupplyWithEmptySupplies)
      val deserializedObject = serializedJson.as[CoreMsestSupply]

      deserializedObject.supplies mustBe empty
    }

    "test invalid JSON" in {
      val invalidJson = Json.obj(
        "countryCode" -> "DE",
        "euTraderId" -> Json.obj("vatIdNumber" -> "VAT123456")
      )

      val result = invalidJson.asOpt[CoreMsestSupply]
      result mustBe None
    }

    "handle missing fields in JSON" in {
      val invalidJson = Json.obj(
        "countryCode" -> "DE"
      )

      val result = invalidJson.validate[CoreMsestSupply]
      result.isError mustBe true
    }

    "handle invalid field types in JSON" in {
      val invalidJson = Json.obj(
        "countryCode" -> "DE",
        "euTraderId" -> Json.obj("vatIdNumber" -> 123456)
      )

      val result = invalidJson.validate[CoreMsestSupply]
      result.isError mustBe true
    }
  }
}
