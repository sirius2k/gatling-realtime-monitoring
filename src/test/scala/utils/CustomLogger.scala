package utils

import com.google.gson.GsonBuilder
import io.gatling.http.Predef._
import io.netty.handler.codec.http.HttpHeaders
import org.slf4j.{Logger, MarkerFactory}

object CustomLogger {
  val gson = new GsonBuilder().setPrettyPrinting().create()
  val MARKER = MarkerFactory.getMarker("CustomMarker")

  def logHttp(logger: Logger, response: Response, message: String = "") = {
    val title = createTitle(response.request.getMethod, response.request.getUri.getPath, message)
    val requestLog = createRequestLog(response.request)
    val responseLog = createResponseLog(response)
    val fullLog = createFullLog(title, requestLog, responseLog)

    logger.error(MARKER, fullLog)
  }

  def createTitle(method: String, path: String, message: String): String = {
    s"$method $path | $message"
  }

  def createFullLog(title: String, requestLog: String, responseLog: String): String = {
    s"""
       |$title
       |====================================
       |$requestLog
       |====================================
       |$responseLog
       |>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>""".stripMargin
  }

  def createRequestLog(request: Request) = {
    s"""Http Request :
       |${request.getMethod} ${request.getUri}
       |headers = ${httpHeadersToString(request.getHeaders)}
       |body = ${gson.toJson(request.getStringData)}""".stripMargin
  }

  def createResponseLog(response: Response) = {
    s"""Http Response :
       |headers = ${httpHeadersToString(response.headers)}
       |body = ${gson.toJson(response.body.string)}""".stripMargin
  }

  def httpHeadersToString(headers: HttpHeaders): String = {
    val builder = StringBuilder.newBuilder
    val iterator = headers.names().iterator()

    while (iterator.hasNext) {
      val name = iterator.next()

      builder.append("\n")
        .append(name)
        .append(" : ")
        .append(headers.get(name))
    }

    builder.toString()
  }
}
