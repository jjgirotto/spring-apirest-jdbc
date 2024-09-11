package com.juliana.demo_park_api;

import com.juliana.demo_park_api.web.dto.ResponseDto;
import com.juliana.demo_park_api.web.dto.UserCreateDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.reactive.server.WebTestClient;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(scripts = "/sql/users/users-insert.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "/sql/users/users-delete.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class UserIT {

    @Autowired
    WebTestClient testClient;

    @Test
    public void createUser_WithUsernameAndPasswordValids_ReturnUserCreatedStatus201() {
        ResponseDto responseDto = testClient
                .post()
                .uri("/api/v1/users")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UserCreateDto("tody@email.com", "123456"))
                .exchange()
                .expectStatus().isCreated()
                .expectBody(ResponseDto.class)
                .returnResult().getResponseBody();
        org.assertj.core.api.Assertions.assertThat(responseDto).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseDto.getId()).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseDto.getUsername()).isEqualTo("tody@email.com");
        org.assertj.core.api.Assertions.assertThat(responseDto.getRole()).isEqualTo("CLIENT");

    }

}
