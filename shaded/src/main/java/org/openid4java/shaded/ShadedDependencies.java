package org.openid4java.shaded;

/**
 * Marker describing the relocated third-party dependencies of the shaded
 * OpenID4Java artifact.
 *
 * <p>The shaded JAR relocates Guice, Guava, HttpClient 5, jsoup,
 * {@code jakarta.inject} and {@code org.aopalliance} under
 * {@code org.openid4java.shaded.*}, so they cannot conflict with the
 * versions provided by the host application.</p>
 */
final class ShadedDependencies {

    private ShadedDependencies() {
    }
}
