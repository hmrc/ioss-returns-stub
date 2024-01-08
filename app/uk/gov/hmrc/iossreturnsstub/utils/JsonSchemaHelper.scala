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

/*
 * Copyright 2020 HM Revenue & Customs
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

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.{JsonNode, ObjectMapper}
import com.github.fge.jsonschema.core.report.ProcessingReport
import com.github.fge.jsonschema.main.{JsonSchema, JsonSchemaFactory}
import play.api.Logging
import play.api.libs.json.{Json, JsValue}
import play.api.mvc._
import play.api.mvc.Results._
import uk.gov.hmrc.iossreturnsstub.models.{CoreErrorResponse, EisErrorResponse}

import java.time.{Clock, Instant}
import java.util.UUID
import javax.inject.Inject
import scala.concurrent._
import scala.io.Source
import scala.util.{Failure, Success, Try}

class JsonSchemaHelper @Inject()(clock: Clock) extends Logging {

  private final lazy val jsonMapper = new ObjectMapper()
  private final lazy val jsonFactory = jsonMapper.getFactory

  private def loadRequestSchema(requestSchema: JsValue): JsonSchema = {
    val schemaMapper = new ObjectMapper()
    val factory = schemaMapper.getFactory
    val schemaParser: JsonParser = factory.createParser(requestSchema.toString)
    val schemaJson: JsonNode = schemaMapper.readTree(schemaParser)
    JsonSchemaFactory.byDefault().getJsonSchema(schemaJson)
  }

  private def validRequest(jsonSchema: JsValue, json: Option[JsValue]): Option[ProcessingReport] = {
    json.map { response =>
      val jsonParser = jsonFactory.createParser(response.toString)
      val jsonNode: JsonNode = jsonMapper.readTree(jsonParser)
      loadRequestSchema(jsonSchema).validate(jsonNode)
    }
  }

  def applySchemaValidation(schemaPath: String, jsonBody: Option[JsValue])(f: => Future[Result]): Future[Result] = {
    retrieveJsonSchema(schemaPath) match {
      case Success(schema) =>
        val validationResult = validRequest(schema, jsonBody)
        validationResult match {
            case Some(res) if res.isSuccess => f
            case Some(res) =>
              logger.error(s"Failed json schema ${res.getExceptionThreshold}")
              res.forEach { test =>
                logger.error(s"${test.toString}")
                logger.error(test.getMessage)
              }
              Future.successful(BadRequest(Json.toJson(EisErrorResponse(CoreErrorResponse(Instant.now(clock), None, "OSS_400", "Bad Request")))))
            case _ =>
              Future.successful(BadRequest(Json.toJson(EisErrorResponse(CoreErrorResponse(Instant.now(clock), None, "OSS_400", "Missing Payload")))))
        }
      case Failure(e) =>
        logger.error(s"Error: ${e.getMessage}", e)
        Future.successful(InternalServerError(""))
    }
  }

  private def retrieveJsonSchema(schemaPath: String): Try[JsValue] = {
    val jsonSchema = Try(Source.fromInputStream(getClass.getResourceAsStream(schemaPath)).mkString)
    jsonSchema.map(Json.parse)
  }

  def applySchemaHeaderValidation(headers: Headers)(f: => Future[Result]): Future[Result] = {

    CoreVatReturnHeaderHelper.validateHeaders(headers.headers) match {
      case Right(_) => f
      case Left(MissingHeader(header)) => Future.successful(BadRequest(Json.toJson(EisErrorResponse(CoreErrorResponse(Instant.now(), Some(UUID.randomUUID()), "OSS_003", s"Bad Request - missing $header")))))
      case Left(InvalidHeader(header)) => Future.successful(BadRequest(Json.toJson(EisErrorResponse(CoreErrorResponse(Instant.now(), Some(UUID.randomUUID()), "OSS_002", s"Bad Request - invalid $header")))))
      case _ => Future.successful(BadRequest(Json.toJson(EisErrorResponse(CoreErrorResponse(Instant.now(), Some(UUID.randomUUID()), "OSS_000", s"Bad Request - unknown error")))))

    }
  }

}