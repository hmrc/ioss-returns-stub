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

import play.api.mvc.QueryStringBindable

import java.time.format.DateTimeFormatter
import java.time.{LocalDate, ZoneId}
import java.util.Locale
import scala.util.{Failure, Success, Try}

final case class DateRange(fromDate: LocalDate, toDate: LocalDate)

object DateRange {

  private val dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    .withLocale(Locale.UK)
    .withZone(ZoneId.systemDefault())

  implicit def queryStringBindable(implicit localDateBinder: QueryStringBindable[String]): QueryStringBindable[DateRange] = new QueryStringBindable[DateRange] {
    override def bind(key: String, params: Map[String, Seq[String]]): Option[Either[String, DateRange]] = {
      for {
        from <- localDateBinder.bind("dateFrom", params)
        to <- localDateBinder.bind("dateTo", params)
      } yield {
        (from, to) match {
          case (Right(from), Right(to)) =>
            (for {
              fromLocalDate <- Try(LocalDate.parse(from, dateTimeFormatter))
              toLocalDate <- Try(LocalDate.parse(to, dateTimeFormatter))
            } yield DateRange(fromLocalDate, toLocalDate)) match {
              case Success(value) => Right(value)
              case Failure(_) =>
                Left("Invalid dates")
            }
          case _ =>
            Left("Unable to bind an DateRange")
        }
      }
    }

    override def unbind(key: String, dateRange: DateRange): String = {
      localDateBinder.unbind("fromDate", dateTimeFormatter.format(dateRange.fromDate)) + "&" + localDateBinder.unbind("toDate", dateTimeFormatter.format(dateRange.toDate))
    }
  }
}