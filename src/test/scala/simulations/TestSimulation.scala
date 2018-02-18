package simulations

import io.gatling.core.scenario.Simulation
import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.http.request.builder.HttpRequestBuilder.toActionBuilder

class TestSimulation extends Simulation {
  val scenario = scenario("Test scenario")
    .exec(http("find metric request")
      .get(s"/test").check(status.is(200))).pause(1)
}
