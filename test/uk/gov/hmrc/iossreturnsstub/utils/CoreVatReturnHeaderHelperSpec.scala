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

import org.scalatest.concurrent.ScalaFutures
import org.scalatest.freespec.AnyFreeSpec
import org.scalatest.matchers.must.Matchers.mustBe
import org.scalatest.matchers.should.Matchers
import play.api.http.HeaderNames.{ACCEPT, AUTHORIZATION, CONTENT_TYPE, DATE}
import play.api.http.MimeTypes

import java.time.{LocalDateTime, ZoneId}
import java.time.format.DateTimeFormatter
import java.util.{Locale, UUID}

class CoreVatReturnHeaderHelperSpec extends AnyFreeSpec with ScalaFutures with Matchers {

  private val dateTimeFormatter = DateTimeFormatter.ofPattern("EEE, dd MMM yyyy HH:mm:ss z")
    .withLocale(Locale.ENGLISH)
    .withZone(ZoneId.of("GMT"))

  "CoreVatReturnHeaderHelper#" - {

    "return Right() if all required headers are present and in the required format" in {
      val headers: Seq[(String, String)] = Seq(
        (AUTHORIZATION, ""),
        (ACCEPT, MimeTypes.JSON),
        ("X-Correlation-Id", UUID.randomUUID().toString),
        ("X-Forwarded-Host", ""),
        (CONTENT_TYPE, MimeTypes.JSON),
        (DATE, dateTimeFormatter.format(LocalDateTime.now())))

      CoreVatReturnHeaderHelper.validateHeaders(headers) mustBe Right((): Unit)

    }

    "return Left(MissingHeader) if not all required headers are present" in {
      val headers: Seq[(String, String)] = Seq(
        (AUTHORIZATION, ""),
        (ACCEPT, MimeTypes.JSON),
        ("X-Correlation-Id", UUID.randomUUID().toString),
        ("X-Forwarded-Host", ""),
        (DATE, dateTimeFormatter.format(LocalDateTime.now())))

      CoreVatReturnHeaderHelper.validateHeaders(headers) mustBe Left(MissingHeader(CONTENT_TYPE))
    }

    "return Left(MissingHeader) if no headers are present" in {
      val headers: Seq[(String, String)] = Seq.empty

      CoreVatReturnHeaderHelper.validateHeaders(headers) mustBe Left(MissingHeader(AUTHORIZATION))
    }

    "return Left(InvalidHeader) if accept is not in the required format" in {
      val headers: Seq[(String, String)] = Seq(
        (AUTHORIZATION, ""),
        (ACCEPT, "something"),
        ("X-Correlation-Id", UUID.randomUUID().toString),
        ("X-Forwarded-Host", ""),
        (CONTENT_TYPE, MimeTypes.JSON),
        (DATE, dateTimeFormatter.format(LocalDateTime.now())))

      CoreVatReturnHeaderHelper.validateHeaders(headers) mustBe Left(InvalidHeader(ACCEPT))

    }

    "return Left(InvalidHeader) if correlation id is not in the required format" in {
      val headers: Seq[(String, String)] = Seq(
        (AUTHORIZATION, ""),
        (ACCEPT, MimeTypes.JSON),
        ("X-Correlation-Id", "something"),
        ("X-Forwarded-Host", ""),
        (CONTENT_TYPE, MimeTypes.JSON),
        (DATE, dateTimeFormatter.format(LocalDateTime.now())))

      CoreVatReturnHeaderHelper.validateHeaders(headers) mustBe Left(InvalidHeader("X-Correlation-Id"))

    }

    "return Left(InvalidHeader) if content type is not in the required format" in {
      val headers: Seq[(String, String)] = Seq(
        (AUTHORIZATION, ""),
        (ACCEPT, MimeTypes.JSON),
        ("X-Correlation-Id", UUID.randomUUID().toString),
        ("X-Forwarded-Host", ""),
        (CONTENT_TYPE, "something"),
        (DATE, dateTimeFormatter.format(LocalDateTime.now())))

      CoreVatReturnHeaderHelper.validateHeaders(headers) mustBe Left(InvalidHeader(CONTENT_TYPE))

    }

    "return Left(InvalidHeader) if date is not in the required format" in {
      val headers: Seq[(String, String)] = Seq(
        (AUTHORIZATION, ""),
        (ACCEPT, MimeTypes.JSON),
        ("X-Correlation-Id", UUID.randomUUID().toString),
        ("X-Forwarded-Host", ""),
        (CONTENT_TYPE, MimeTypes.JSON),
        (DATE, "something"))

      CoreVatReturnHeaderHelper.validateHeaders(headers) mustBe Left(InvalidHeader(DATE))

    }
  }

}
