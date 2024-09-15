package com.juliana.demo_park_api;

import com.juliana.demo_park_api.web.dto.ClientCreateDto;
import com.juliana.demo_park_api.web.dto.ClientResponseDto;
import com.juliana.demo_park_api.web.dto.UserCreateDto;
import com.juliana.demo_park_api.web.dto.UserResponseDto;
import com.juliana.demo_park_api.web.exception.ErrorMessage;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.reactive.server.WebTestClient;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(scripts = "/sql/clients/clients-insert.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "/sql/clients/clients-delete.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class ClientIT {

    @Autowired
    WebTestClient testClient;

    @Test
    public void createClient_ValidData_ReturnClientStatus201() {
        ClientResponseDto responseBody = testClient
                .post()
                .uri("/api/v1/clients")
                .contentType(MediaType.APPLICATION_JSON)
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "toby@gmail.com", "123456"))
                .bodyValue(new ClientCreateDto("Tobias Ferreira", "87245304092"))
                .exchange()
                .expectStatus().isCreated()
                .expectBody(ClientResponseDto.class)
                .returnResult().getResponseBody();
        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getId()).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getName()).isEqualTo("Tobias Ferreira");
        org.assertj.core.api.Assertions.assertThat(responseBody.getCpf()).isEqualTo("87245304092");
    }

    @Test
    public void createClient_WithCPFRegistered_ReturnErrorMessage409() {
        ErrorMessage responseBody = testClient
                .post()
                .uri("/api/v1/clients")
                .contentType(MediaType.APPLICATION_JSON)
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "toby@gmail.com", "123456"))
                .bodyValue(new ClientCreateDto("Ana Carolina da Silva", "88110019005"))
                .exchange()
                .expectStatus().isEqualTo(409)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();
        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo(409);

    }

    @Test
    public void createClient_WithInvalidData_ReturnErrorMessage422() {
        ErrorMessage responseBody = testClient
                .post()
                .uri("/api/v1/clients")
                .contentType(MediaType.APPLICATION_JSON)
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "toby@gmail.com", "123456"))
                .bodyValue(new ClientCreateDto("", ""))
                .exchange()
                .expectStatus().isEqualTo(422)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();
        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo(422);

        responseBody = testClient
                .post()
                .uri("/api/v1/clients")
                .contentType(MediaType.APPLICATION_JSON)
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "toby@gmail.com", "123456"))
                .bodyValue(new ClientCreateDto("Toby", "872.453.040-92"))
                .exchange()
                .expectStatus().isEqualTo(422)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();
        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo(422);

        responseBody = testClient
                .post()
                .uri("/api/v1/clients")
                .contentType(MediaType.APPLICATION_JSON)
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "toby@gmail.com", "123456"))
                .bodyValue(new ClientCreateDto("Toby", "00000000000"))
                .exchange()
                .expectStatus().isEqualTo(422)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();
        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo(422);

    }

//    @Test
//    public void createClient_WithInvalidUser_ReturnErrorMessage403() {
//        ErrorMessage responseBody = testClient
//                .post()
//                .uri("/api/v1/clients")
//                .contentType(MediaType.APPLICATION_JSON)
//                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "ana@gmail.com", "123456"))
//                .bodyValue(new ClientCreateDto("Tobias Ferreira", "87245304092"))
//                .exchange()
//                .expectStatus().isEqualTo(403)
//                .expectBody(ErrorMessage.class)
//                .returnResult().getResponseBody();
//        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
//        org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo(403);
//    }

    @Test
    public void getUser_WithIdExistsByAdmin_ReturnClientStatus200() {
        ClientResponseDto clientResponseDto = testClient
                .get()
                .uri("/api/v1/users/102")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "juliana@gmail.com", "123456"))
                .exchange()
                .expectStatus().isOk()
                .expectBody(ClientResponseDto.class)
                .returnResult().getResponseBody();
        org.assertj.core.api.Assertions.assertThat(clientResponseDto).isNotNull();
        org.assertj.core.api.Assertions.assertThat(clientResponseDto.getId()).isEqualTo(102);

    }


}
