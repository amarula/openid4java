![OpenID4Java](openid4java.png)

This repository is a maintained fork of the original OpenID4Java library.

> **Note:** OpenID Authentication 2.0 is a deprecated specification.
> This fork maintains the library for existing deployments; new projects
> should use OpenID Connect.

# What is this package? #

This library allows you to OpenID-enable your Java webapp.

The following specifications are supported:
  * [OpenID Authentication 2.0](http://openid.net/specs/openid-authentication-2_0.html)
  * [OpenID Authentication 1.1 (in compatibility mode)](http://openid.net/specs/openid-authentication-1_1.html)
  * [OpenID Attribute Exchange 1.0](http://openid.net/specs/openid-attribute-exchange-1_0.html)
  * [OpenID Simple Registration 1.0](http://openid.net/specs/openid-simple-registration-extension-1_0.html) and [1.1, draft 1](http://openid.net/specs/openid-simple-registration-extension-1_1-01.html)
  * [OpenID Provider Authentication Policy Extension 1.0](http://openid.net/specs/openid-provider-authentication-policy-extension-1_0.html)

# What is OpenID? #

OpenID starts with the concept that anyone can identify themselves on the
Internet the same way websites do - with a URI. Since URIs are at the very
core of Web architecture, they provide a solid foundation for user-centric
identity.

The first piece of the OpenID framework is authentication -- how you prove
ownership of a URI. Today, websites require usernames and passwords to
login, which means that many people use the same password everywhere. With
OpenID Authentication, your username is your URI, and your password (or
other credentials) stays safely stored on your OpenID Provider (which you
can run yourself, or use a third-party identity provider).

For more information about the OpenID protocol please refer to the
specification at: http://openid.net/specs.bml

# Quick Start #

To OpenID-enable a website - a Relying Party (RP) in OpenID terms, or Consumer at code-level, you need to do the following:

  * [Install](INSTALL) the libraries

  * Instead (or as an alternative to) prompting the user for their username/password, obtain their OpenID (URL) identifier

  * Create an authentication request for this identifier, and redirect the user to their OpenID Provider (with this request)

  * Receive the OpenID Provider's authentication response at your webapp's ReturnURL, and verify it

Have a look at the usage pattern in `src/overview.html` for a code-level walk-through of the above, and at `test/src/org/openid4java/consumer/SampleConsumer.java` for the full code.

The bulk of the action amounts to about a dozen lines of code.

# Simple-OpenID #

A working example of a simple (6 JSP files) OpenID Provider and Relying Party can be found under samples/simple-openid/.
Start it with:
```
mvn jetty:run
```
And access it at http://localhost:8080/simple-openid/

For more details see the README.txt file included in that folder.

# Building #

The library requires JDK 17 or newer and Maven 3.9 or newer:

```
mvn clean verify
```

The build is tested on JDK 17, 21 and 25.

# See also #

[Requirements](INSTALL)

[Installation](INSTALL)

[Documentation](README.md)

[SampleConsumer](test/src/org/openid4java/consumer/SampleConsumer.java)

[SampleServer](test/src/org/openid4java/server/SampleServer.java)

# Package contents #

[CHANGELOG](CHANGELOG)
Version highlight changes.

[INSTALL](INSTALL)
Installation and usage instructions.

[LICENSE](LICENSE)
OpenID4Java Library license (Apache 2.0).

[MAINTAINERS](MAINTAINERS)
List of code and documentation maintainers.

[README.md](README.md)
This file!

[TODO](TODO)
Tasks remaining and/or ongoing.

[samples/](samples)
Sample implementation projects. See samples/README for details.

[src/](src)
Source code.

[test/](test)
Test cases source code and test data.

[pom.xml](pom.xml)
Maven POM.
