package simulations

import io.gatling.core.Predef._
import io.gatling.core.scenario.Simulation
import io.gatling.http.Predef._
import io.sentry.Sentry
import org.slf4j.{LoggerFactory, MDC, MarkerFactory}

import scala.concurrent.duration._
import scala.language.postfixOps

class TestSimulation extends Simulation {
  private val LOGGER = LoggerFactory.getLogger(getClass)
  private val MARKER = MarkerFactory.getMarker("myMarker")

  before {
    println("Initialize Sentry before test!!")
    Sentry.init()

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
          .get("/posts"))
          .pause(1)
      }

  setUp(
    simpleScenario.inject(atOnceUsers(1))
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
