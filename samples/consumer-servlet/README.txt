How to run the demo.

Step 1. Build and install the openid4java library from the repository root:

  mvn install -DskipTests

Step 2. Run the demo:

  cd samples/consumer-servlet
  mvn jetty:run

Step 3. Open http://localhost:8080/consumer-servlet/ in a browser.

The demo requires JDK 17 or newer and uses Jakarta EE 10 (Jetty 12).
