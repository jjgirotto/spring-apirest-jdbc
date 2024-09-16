package com.juliana.demo_park_api;

import com.juliana.demo_park_api.web.dto.SpaceCreateDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.reactive.server.WebTestClient;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(scripts = "/sql/spaces/spaces-insert.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "/sql/spaces/spaces-delete.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class SpaceIT {

    @Autowired
    WebTestClient testClient;

    @Test
    public void createSpace_WithValidData_ReturnLocationStatus201() {
        testClient
                .post()
                .uri("/api/v1/spaces")
                .contentType(MediaType.APPLICATION_JSON)
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "juliana@gmail.com", "123456"))
                .bodyValue(new SpaceCreateDto("A-05", "FREE"))
                .exchange()
                .expectStatus().isCreated()
                .expectHeader().exists(HttpHeaders.LOCATION);
    }

    @Test
    public void createSpace_WithExistsCode_ReturnErrorMessageStatus409() {
        testClient
                .post()
                .uri("/api/v1/spaces")
                .contentType(MediaType.APPLICATION_JSON)
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "juliana@gmail.com", "123456"))
                .bodyValue(new SpaceCreateDto("A-01", "FREE"))
                .exchange()
                .expectStatus().isEqualTo(409)
                .expectBody()
                .jsonPath("status").isEqualTo(409)
                .jsonPath("method").isEqualTo("POST")
                .jsonPath("path").isEqualTo("/api/v1/spaces");

    }

    @Test
    public void createSpace_WithInvalidData_ReturnErrorMessageStatus422() {
        testClient
                .post()
                .uri("/api/v1/spaces")
                .contentType(MediaType.APPLICATION_JSON)
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "juliana@gmail.com", "123456"))
                .bodyValue(new SpaceCreateDto("", ""))
                .exchange()
                .expectStatus().isEqualTo(422)
                .expectBody()
                .jsonPath("status").isEqualTo(422)
                .jsonPath("method").isEqualTo("POST")
                .jsonPath("path").isEqualTo("/api/v1/spaces");

        testClient
                .post()
                .uri("/api/v1/spaces")
                .contentType(MediaType.APPLICATION_JSON)
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "juliana@gmail.com", "123456"))
                .bodyValue(new SpaceCreateDto("A-501", "DESOCUPADA"))
                .exchange()
                .expectStatus().isEqualTo(422)
                .expectBody()
                .jsonPath("status").isEqualTo(422)
                .jsonPath("method").isEqualTo("POST")
                .jsonPath("path").isEqualTo("/api/v1/spaces");
    }


    @Test
    public void searchSpace_WithExistsCode_ReturnSpaceStatus200() {
        testClient
                .get()
                .uri("/api/v1/spaces/{code}", "A-01")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "juliana@gmail.com", "123456"))
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("id").isEqualTo(10)
                .jsonPath("code").isEqualTo("A-01")
                .jsonPath("status").isEqualTo("FREE");

    }

    @Test
    public void searchSpace_WithNonExistsCode_ReturnErrorStatus404() {
        testClient
                .get()
                .uri("/api/v1/spaces/{code}", "A-10")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "juliana@gmail.com", "123456"))
                .exchange()
                .expectStatus().isNotFound()
                .expectBody()
                .jsonPath("status").isEqualTo(404)
                .jsonPath("method").isEqualTo("GET")
                .jsonPath("path").isEqualTo("/api/v1/spaces/A-10");
    }

    @Test
    public void searchSpace_WithNotAllowedUser_ReturnErrorStatus403() {
        testClient
                .get()
                .uri("/api/v1/spaces/{code}", "A-01")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "ana@gmail.com", "123456"))
                .exchange()
                .expectStatus().isForbidden()
                .expectBody()
                .jsonPath("status").isEqualTo(403)
                .jsonPath("method").isEqualTo("GET")
                .jsonPath("path").isEqualTo("/api/v1/spaces/A-01");
    }

    @Test
    public void createSpace_WithNotAllowedUser_ReturnErrorStatus403() {
        testClient
                .post()
                .uri("/api/v1/spaces")
                .contentType(MediaType.APPLICATION_JSON)
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "ana@gmail.com", "123456"))
                .bodyValue(new SpaceCreateDto("A-05", "OCCUPIED"))
                .exchange()
                .expectStatus().isForbidden()
                .expectBody()
                .jsonPath("status").isEqualTo(403)
                .jsonPath("method").isEqualTo("POST")
                .jsonPath("path").isEqualTo("/api/v1/spaces");
    }
}
