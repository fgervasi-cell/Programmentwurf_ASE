# Programmentwurf_ASE

Project for Advanced Software Engineering at DHBW Karlsruhe.

## Run and debug

Run: Navigate into folder where the pom.xml is located. Run the command `mvn javafx:run`.

Debug: Navigate into folder where the pom.xml is located. Run the command `mvn clean javafx:run@debug`. Attach the Java debugger by running the attach configuration in the launch.json.

Run tests: Navigate into folder where the pom.xml is located. Run the command `mvn test`.

Debug tests: Run the command `mvnDebug test -DforkMode=never`.
