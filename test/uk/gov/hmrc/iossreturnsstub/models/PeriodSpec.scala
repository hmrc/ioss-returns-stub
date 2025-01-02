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

package uk.gov.hmrc.iossreturnsstub.models

import org.scalatest.freespec.AnyFreeSpec
import org.scalatest.matchers.must.Matchers
import org.scalatest.PrivateMethodTester
import play.api.libs.json.{JsSuccess, Json}

import java.time.{LocalDate, Month}
import scala.util.{Failure, Success}

class PeriodSpec extends AnyFreeSpec with Matchers with PrivateMethodTester {

  "Period" - {

    "Period.displayText should return the correct display text for a given period" in {
      val period = Period(2023, Month.JANUARY)
      period.displayText mustBe "January 2023"
    }

    "Period.lastDay should return the correct last day of the period" in {
      val period = Period(2023, Month.FEBRUARY)
      period.lastDay mustBe LocalDate.of(2023, 2, 28)
    }

    "Period.toString should return the correct string representation" in {
      val period = Period(2023, Month.MARCH)
      period.toString mustBe "2023-M3"
    }

    "Period.toEtmpPeriodString should return the correct ETMP period string" in {
      val period = Period(2023, Month.JANUARY)
      period.toEtmpPeriodString mustBe "23AA"

      val periodDecember = Period(2023, Month.DECEMBER)
      periodDecember.toEtmpPeriodString mustBe "23AL"
    }

    "Period.apply should parse valid year and month strings into a Period" in {
      val result = Period("2023", "1")
      result mustBe Success(Period(2023, Month.JANUARY))
    }

    "Period.apply should return Failure for invalid year or month strings" in {
      Period("invalidYear", "1") mustBe a[Failure[_]]
      Period("2023", "13") mustBe a[Failure[_]] // Month 13 does not exist
    }

    "Period should serialize and deserialize to/from JSON" in {
      val period = Period(2023, Month.JANUARY)

      val json = Json.obj(
        "year" -> 2023,
        "month" -> Json.obj(
          "month" -> 1
        )
      )

      Json.toJson(period) mustBe json

      val deserialized = json.validate[Period]
      deserialized mustBe JsSuccess(period)
    }

    "Period should handle edge cases for months and years" in {
      val januaryPeriod = Period(2023, Month.JANUARY)
      januaryPeriod.firstDay mustBe LocalDate.of(2023, 1, 1)
      januaryPeriod.lastDay mustBe LocalDate.of(2023, 1, 31)

      val decemberPeriod = Period(2023, Month.DECEMBER)
      decemberPeriod.firstDay mustBe LocalDate.of(2023, 12, 1)
      decemberPeriod.lastDay mustBe LocalDate.of(2023, 12, 31)
    }
  }

  "toEtmpMonthString should return the correct ETMP month string for all months" in {

    val toEtmpMonthString = PrivateMethod[String](Symbol("toEtmpMonthString"))

    val period = Period(2023, Month.JANUARY)

    val monthToExpectedMapping = Map(
      Month.JANUARY -> "AA",
      Month.FEBRUARY -> "AB",
      Month.MARCH -> "AC",
      Month.APRIL -> "AD",
      Month.MAY -> "AE",
      Month.JUNE -> "AF",
      Month.JULY -> "AG",
      Month.AUGUST -> "AH",
      Month.SEPTEMBER -> "AI",
      Month.OCTOBER -> "AJ",
      Month.NOVEMBER -> "AK",
      Month.DECEMBER -> "AL"
    )

    monthToExpectedMapping.foreach { case (month, expected) =>
      val result = period.invokePrivate(toEtmpMonthString(month))
      result mustBe expected
    }
  }
}
