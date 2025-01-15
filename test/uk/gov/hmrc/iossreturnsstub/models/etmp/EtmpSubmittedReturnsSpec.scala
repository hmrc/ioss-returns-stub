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

class EtmpSubmittedReturnsSpec extends AnyFreeSpec with Matchers {

  private val obligationDetails: Seq[EtmpObligationDetails] = Seq(
    EtmpObligationDetails(
      status = EtmpObligationsFulfilmentStatus.Open,
      periodKey = "23AJ"
    ),
    EtmpObligationDetails(
      status = EtmpObligationsFulfilmentStatus.Open,
      periodKey = "23AK"
    )
  )

  private val etmpObligations: Seq[EtmpObligations] = Seq(
    EtmpObligations(
      obligations = Seq(
        EtmpObligation(obligationDetails = obligationDetails)
      )
    )
  )

  "EtmpSubmittedReturns" - {

    "must serialize and deserialize correctly" in {

      val etmpSubmittedReturns = EtmpSubmittedReturns(obligations = etmpObligations)

      val json = Json.obj(
        "obligations" -> Json.arr(
          Json.obj(
            "obligations" -> Json.arr(
              Json.obj(
                "obligationDetails" -> obligationDetails.map { obligationDetail =>
                  Json.obj(
                    "status" -> obligationDetail.status.toString,
                    "periodKey" -> obligationDetail.periodKey
                  )
                }
              )
            )
          )
        )
      )

      Json.toJson(etmpSubmittedReturns) mustBe json

      json.validate[EtmpSubmittedReturns] mustBe JsSuccess(etmpSubmittedReturns)
    }

    "must handle an empty list of obligations" in {

      val etmpSubmittedReturns = EtmpSubmittedReturns(obligations = Seq.empty)

      val json = Json.obj(
        "obligations" -> Json.arr()
      )

      Json.toJson(etmpSubmittedReturns) mustBe json

      json.validate[EtmpSubmittedReturns] mustBe JsSuccess(etmpSubmittedReturns)
    }
  }
}
