# pairing-ws
## Development
### Prerequisites
1. Java 11

### Running tests with IntelliJ IDEA
Install the Kotest plugin via "Settings > Plugins"
Set "Settings > Build, Execution, Deployment > Gradle Projects > Build and run > Run tests using" to "IntelliJ IDEA"

### Running the application
```sh
$ ./gradlew run
```
The app will be started locally on port 8080. It will block the terminal from furthur input until it is stopped with CTRL+C.

### Building a jarfile
```sh
./gradlew clean shadowJar
```
A jarfile named `pairing-ws.jar` will be placed in `build/libs`

### Running a jarfile
```sh
$ java -jar build/libs/pairing-ws.jar
$ curl -s localhost:8080/version
{"version":"probably the version"}
```
This runs the application as an HTTP server on port 8080.

#### Running with a different port
```sh
$ java -jar build/libs/fall-color-history.jar -port 8081
$ curl -s localhost:8081/version
{"version":"probably the version but using a different port"}
```

### Running the tests
```sh
./gradlew clean test
```

#### Against yagni
```sh
./gradlew clean test \
  -DbaseUrl=http://yagni:8085 \
  -DwsBaseUrl=ws://yagni:8085
```

#### Against pairing-ws.cruftbusters.com
```sh
./gradlew clean test \
  -DbaseUrl=https://pairing-ws.cruftbusters.com \
  -DwsBaseUrl=wss://pairing-ws.cruftbusters.com
```

## Updating dependencies
```sh
./gradlew dependencies --write-locks
```
