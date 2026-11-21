OpenID4Java Simple JSP Demo

************
INTRODUCTION
************

This is a demo consumer and provider implemented as JSPs. It uses only
in-memory stores for nonces and associations, and the provider does no
user authentication, though provider.jsp shows where user authentication
would go.

The index.jsp and consumer_*.jsp files comprise the consumer.
The provider_*.jsp files comprise the provider.
The user.jsp produces an XRDS file which points to the provider.

There are hardcoded URLs in the JSP files; a comment at the top indicates
what to change to deploy this somewhere other than
http://localhost:8080/simple-openid.

There are no dependencies between the consumer and the provider.

**********
REQUIREMENTS
**********

JDK 17 or newer and Maven 3.9 or newer.

The demo uses Jakarta EE 10 (Jakarta Servlet 6.0, Jetty 12).

****************
RUNNING THE DEMO
****************

Build and install the library from the repository root first:

  mvn install -DskipTests

Then run:

  cd samples/simple-openid
  mvn jetty:run

DEMO CONSUMER
-------------
Visit the demo consumer at:
http://localhost:8080/simple-openid

You can use an IName, or any HTTP URL which acts as an OpenID.

DEMO PROVIDER
-------------
To visit the demo provider, visit the consumer at:
http://localhost:8080/simple-openid

And log in with the following URL:
http://localhost:8080/simple-openid/user.jsp

You will be prompted to approve the OP request (no authentication).

*******
LOGGING
*******

The library logs through Apache Commons Logging. To use the simple logger:

  mvn -Dorg.apache.commons.logging.Log=org.apache.commons.logging.impl.SimpleLog \
      jetty:run

****
TODO
****

Lots.

Error handling is almost nonexistent.

Authentication of the user is an exercise left to the reader.

There are probably better ways to manage the ConsumerManager and
ServerManager objects.

In general, the JSPs are rather brittle.
