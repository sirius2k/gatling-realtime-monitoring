package simulations

import io.gatling.core.scenario.Simulation
import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.http.protocol.HttpProtocolBuilder
import io.sentry.Sentry

import scala.concurrent.duration._
import scala.language.postfixOps

class TestSimulation extends Simulation {
  before {
    println("Initialize Sentry before test!!")
    Sentry.init()
  }

  after {
    println("Test has ended!")
  }

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
}
