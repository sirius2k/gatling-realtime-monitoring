## Synopsis
This project is Gatling project to test local node server which uses Sentry for logging externally.
You don't need to start server externally, but automatically run the server during maven lifecycle. 
 
## Code Example
TBD

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