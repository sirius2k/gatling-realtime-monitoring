## Synopsis
This project is Gatling project to test local node server which uses Sentry for logging externally. The logging is using logback and I added custom logging with filters.
And, the custom logger can write logging request/response headers and content body conditially. 
You don't need to start server externally, but automatically run the server during maven lifecycle. 
 
## Code Example
The logging to the Sentry is filtered with the following logack configuration.
```xml
	<appender name="Sentry" class="io.sentry.logback.SentryAppender">
		<!--filter class="ch.qos.logback.classic.filter.ThresholdFilter">
			<level>WARN</level>
		</filter-->
		<!-- defaults to type ch.qos.logback.classic.boolex.JaninoEventEvaluator -->
		<filter class="ch.qos.logback.core.filter.EvaluatorFilter">
			<evaluator>
				<expression>return level > INFO &amp;&amp; !formattedMessage.contains("crashed with") &amp;&amp; !formattedMessage.contains("failed to execute");</expression>
				<!-- TODO : uncomment if you want to log with marker -->
				<!--expression>return marker!=null &amp;&amp; marker.contains("CustomMarker");</expression-->
			</evaluator>
			<OnMismatch>DENY</OnMismatch>
			<OnMatch>NEUTRAL</OnMatch>
		</filter>
	</appender>
	...
	<root level="INFO">
		<appender-ref ref="CONSOLE" />
        <appender-ref ref="Sentry" />
	</root>	
```

It can write logging conditially in the gatling code as follows.
```scala
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
        )
```

## Installation
In order to run demo test, you have to install npm and Node.js. Please, refer to https://www.npmjs.com/get-npm.


## Tests
Run Gatling test with embedded server
```sh
mvn clean verify -Dmaven.test.skip=true
```
or test against standalone server
```sh
mvn clean getling:test
```

## Contributors
Park, KyoungWook (Kevin) / sirius00@paran.com

## License

This project is licensed under the MIT License