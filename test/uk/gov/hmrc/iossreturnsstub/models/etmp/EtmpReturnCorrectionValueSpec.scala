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

class EtmpReturnCorrectionValueSpec extends AnyFreeSpec with Matchers {

  "EtmpReturnCorrectionValueSpec" - {

    "EtmpReturnCorrectionValue should serialize and deserialize correctly" in {
      val correctionValue = EtmpReturnCorrectionValue(maximumCorrectionValue = BigDecimal(12345.67))

      val json = Json.toJson(correctionValue)

      json.toString() mustBe """{"maximumCorrectionValue":12345.67}"""

      val deserialized = json.validate[EtmpReturnCorrectionValue]
      deserialized mustBe JsSuccess(correctionValue)
    }

    "EtmpReturnCorrectionValue should handle edge cases for maximumCorrectionValue" in {
      val minCorrectionValue = EtmpReturnCorrectionValue(maximumCorrectionValue = BigDecimal(0))
      val minJson = Json.toJson(minCorrectionValue)
      minJson.toString() mustBe """{"maximumCorrectionValue":0}"""
      minJson.validate[EtmpReturnCorrectionValue] mustBe JsSuccess(minCorrectionValue)

      val largeCorrectionValue = EtmpReturnCorrectionValue(maximumCorrectionValue = BigDecimal("9999999999999999.99"))
      val largeJson = Json.toJson(largeCorrectionValue)
      largeJson.toString() mustBe """{"maximumCorrectionValue":9999999999999999.99}"""
      largeJson.validate[EtmpReturnCorrectionValue] mustBe JsSuccess(largeCorrectionValue)
    }
  }
}
