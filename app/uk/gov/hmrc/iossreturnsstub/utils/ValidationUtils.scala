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

package uk.gov.hmrc.iossreturnsstub.utils

import play.api.libs.json.{JsonValidationError, JsString, Reads, Writes}

import java.time.{Instant, LocalDate, LocalDateTime, ZoneOffset}
import java.time.format.DateTimeFormatterBuilder
import scala.util.Try

object ValidationUtils {

  private val dateRegex = "([1-9]\\d{3}-(0[1-9]|1[0-2])-(0[1-9]|[12]\\d|3[01]))"

  private val currencyRegex = "^[\\d]+|[\\d]+\\.\\d{1,2}$"

  private val currencyRegexAllowNegative = "^-?[\\d]+|-?[\\d]+\\.\\d{1,2}$"

  private val vatRateRegex = "^[\\d]{1,3}(\\.\\d{1,2})?$"

  private val dateTimeWithMillisecondsFormatter = new DateTimeFormatterBuilder()
    .appendPattern("yyyy-MM-dd")
    .appendLiteral('T')
    .appendPattern("hh:mm:ss.SSS")
    .appendLiteral('Z')
    .toFormatter

  def range[A](from: Int, to: Int)(implicit reads: Reads[A], p: A => scala.collection.Iterable[_]): Reads[A] =
    Reads.filter[A](JsonValidationError(s"Collection must have between $from and $to values", from))(x => p(x).size >= from && p(x).size <= to)

  val validatedDateRead: Reads[LocalDate] =
    Reads.pattern(dateRegex.r, "Not a valid date with format yyyy-MM-dd")
      .map( x => LocalDate.parse(x))

  val validatedDateTimeRead: Reads[LocalDateTime] =
    Reads.filter[JsString](JsonValidationError("Not a valid date-time of format yyyy-MM-ddThh:mm:ss.SSSZ"))(
    (x: JsString) =>  Try { dateTimeWithMillisecondsFormatter.parse(x.value) }.isSuccess)
    .map(x => LocalDateTime.ofInstant(Instant.parse(x.value), ZoneOffset.of("Z")))

  val localDateTimeWrites: Writes[LocalDateTime] = Writes[LocalDateTime] {
    localDateTime => JsString(dateTimeWithMillisecondsFormatter.format(localDateTime)) }

  val currencyRead = implicitly[Reads[BigDecimal]]
    .filter(JsonValidationError("Value can only be positive and have up to 2 decimal places"))(x => x.toString.matches(currencyRegex))

  val currencyAllowNegativeRead = implicitly[Reads[BigDecimal]]
    .filter(JsonValidationError("Value can only have up to 2 decimal places"))(x => x.toString.matches(currencyRegexAllowNegative))

  val vatRateRead = implicitly[Reads[BigDecimal]]
    .filter(JsonValidationError("Value can only have 2 decimal places"))(x => x.toString.matches(vatRateRegex))
    .filter(JsonValidationError("Value cannot be 0"))(x => x > 0)
    .filter(JsonValidationError("Value must be below 100"))(x => x <= 100)

}
