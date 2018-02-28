package simulations

import io.gatling.core.Predef._
import io.gatling.core.scenario.Simulation
import io.gatling.http.Predef._
import org.slf4j.{LoggerFactory, MDC, MarkerFactory}
import utils.CustomLogger

import scala.concurrent.duration._
import scala.language.postfixOps

class TestSimulation extends Simulation {
  val LOGGER = LoggerFactory.getLogger(getClass)
  val MARKER = MarkerFactory.getMarker("TestMarker")

  before {
    println("Initialize Sentry before test!!")

    LOGGER.warn(s"before Logger warn test!!")
  }

  after {
    println("Test has ended!")

    LOGGER.warn(s"after Logger warn test!!")
  }

  warnStartLogging()
  errorLogging()
  errorLoggingWithTag()
  errorLoggingWithExtraInfo()

  val httpProtocol = http
    .baseURL("http://127.0.0.1:3000")

  val simpleScenario = scenario("Test scenario")
      .during(10 seconds) {
        exec(http("Get Posts")
          .get("/posts")
          .header("accept", "*/*")
          .body(StringBody("{ param1 : 1, param2 : 2 }"))
          .transformResponse {
            case response if !response.isReceived => {
              CustomLogger.logHttp(LOGGER, response, "Response not received.")
              response
            }
            case response if response.isReceived => {
              CustomLogger.logHttp(LOGGER, response, "Response received")
              response
            }
          }
        ).pause(1)
      }

  setUp(
    simpleScenario.inject(constantUsersPerSec(5) during(10 seconds))).throttle(
      reachRps(3) in (3 seconds),
      holdFor(2 seconds),
      jumpToRps(5),
      holdFor(2 seconds)
  ).protocols(httpProtocol)

  def warnStartLogging() {
    LOGGER.warn(s"Simulation start logging.")
  }

  def errorLogging() {
    LOGGER.error(s"Logger error test!!")
  }

  def errorLoggingWithTag() {
    LOGGER.error(MARKER, s"Logger error with tag test!!")
  }

  def errorLoggingWithExtraInfo() {
    MDC.put("extra_key", "extra_value")
    LOGGER.error(s"Logger error with MDC extra data test!!")
  }
}
