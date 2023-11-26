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

import play.api.libs.json._

import java.time.{LocalDate, Month}
import java.time.format.TextStyle
import java.util.Locale
import scala.util.Try

case class Period(year: Int, month: Month) {
  def displayText: String =
    s"${month.getDisplayName(TextStyle.FULL, Locale.ENGLISH)} ${year}"

  def firstDay: LocalDate =
    LocalDate.of(year, month, 1)

  def lastDay: LocalDate =
    firstDay.plusMonths(1).minusDays(1)

  override def toString: String = s"$year-M${month.getValue}"
}

object Period {
  def apply(yearString: String, monthString: String): Try[Period] =
    for {
      year <- Try(yearString.toInt)
      month <- Try(Month.of(monthString.toInt))
    } yield Period(year, month)

  implicit val monthReads: Reads[Month] = {
    Reads.at[Int](__ \ "month")
      .map(Month.of)
  }

  implicit val monthWrites: Writes[Month] = {
    Writes.at[Int](__ \ "month")
      .contramap(_.getValue)
  }

  implicit val format: OFormat[Period] = Json.format[Period]

}