package simulations

import io.gatling.core.scenario.Simulation
import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.http.protocol.HttpProtocolBuilder
import io.sentry.Sentry

import scala.concurrent.duration._
import scala.language.postfixOps

class TestSimulation extends Simulation {
  Sentry.init()

  val httpProtocol = http
    .baseURL("http://127.0.0.1:3000")

  val testScenario = scenario("Test scenario")
      .during(10 seconds) {
        exec(http("Get Posts")
          .get("/posts"))
          .pause(1)
      }

  setUp(
    testScenario.inject(atOnceUsers(1))
  ).protocols(httpProtocol)
}
