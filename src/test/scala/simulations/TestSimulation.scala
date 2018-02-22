package simulations

import io.gatling.core.scenario.Simulation
import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.http.protocol.HttpProtocolBuilder
import io.sentry.Sentry

import scala.concurrent.duration._
import scala.language.postfixOps

class TestSimulation extends Simulation {
  // TODO : If you want to use Sentry, please comment out and input your Sentry DSN
  /*
  val sentryDsn = "your sentry dsn";
  Sentry.init(sentryDsn)
  */

  val httpProtocol = http
    .baseURL("http://localhost:3000")

  val testScenario = scenario("Test scenario")
      .during(10 second) {
        exec(http("Get Posts")
          .get("/posts")
          .check(status.is(200)))
          .pause(1)
      }

  setUp(
    testScenario.inject(atOnceUsers(1))
  ).protocols(httpProtocol)
}
