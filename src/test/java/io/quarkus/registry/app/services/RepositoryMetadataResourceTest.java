package io.quarkus.registry.app.services;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;

import javax.inject.Inject;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;

import org.junit.jupiter.api.Test;

import io.quarkus.registry.app.maven.MavenConfig;
import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
public class RepositoryMetadataResourceTest {

    @Inject
    MavenConfig mavenConfig;

    @Test
    void should_support_repository_proxied_as_nexus_repository() {
        given()
                .get("/maven/.meta/repository-metadata.xml")
                .then()
                .statusCode(200)
                .header(HttpHeaders.CONTENT_TYPE, containsString(MediaType.APPLICATION_XML));

        given()
                .get("/maven/.meta/repository-metadata.xml.sha1")
                .then()
                .statusCode(200)
                .header(HttpHeaders.CONTENT_TYPE, containsString(MediaType.TEXT_PLAIN));

        given()
                .get("/maven/.meta/prefixes.txt")
                .then()
                .statusCode(200)
                .header(HttpHeaders.CONTENT_TYPE, containsString(MediaType.TEXT_PLAIN))
                .body(is("## repository-prefixes/2.0\n/" + mavenConfig.getRegistryGroupId().replace('.', '/')));
    }

}
