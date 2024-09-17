package com.juliana.demo_park_api;

import com.juliana.demo_park_api.web.dto.PageableDto;
import com.juliana.demo_park_api.web.dto.ParkingCreateDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.reactive.server.WebTestClient;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(scripts = "/sql/parkings/parkings-insert.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "/sql/parkings/parkings-delete.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class ParkingIT {

    @Autowired
    WebTestClient testClient;

    @Test
    public void createCheckIn_WithValidData_ReturnCreatedAndLocation() {
        ParkingCreateDto parkingCreateDto = ParkingCreateDto.builder()
                .plate("ABC-0101").brand("VOLKS").model("GOL")
                .color("WHITE").clientCpf("88110019005")
                .build();
        testClient.post().uri("/api/v1/parkings/checkin")
                .contentType(MediaType.APPLICATION_JSON)
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "juliana@gmail.com", "123456"))
                .bodyValue(parkingCreateDto)
                .exchange()
                .expectStatus().isCreated()
                .expectHeader().exists(HttpHeaders.LOCATION)
                .expectBody()
                .jsonPath("plate").isEqualTo("ABC-0101")
                .jsonPath("brand").isEqualTo("VOLKS")
                .jsonPath("model").isEqualTo("GOL")
                .jsonPath("color").isEqualTo("WHITE")
                .jsonPath("clientCpf").isEqualTo("88110019005")
                .jsonPath("recipt").exists()
                .jsonPath("dateEntry").exists()
                .jsonPath("spaceCode").exists();
    }

    @Test
    public void createCheckIn_ByClient_ReturnErrorStatus403() {
        ParkingCreateDto parkingCreateDto = ParkingCreateDto.builder()
                .plate("ABC-0101").brand("VOLKS").model("GOL")
                .color("WHITE").clientCpf("88110019005")
                .build();
        testClient.post().uri("/api/v1/parkings/checkin")
                .contentType(MediaType.APPLICATION_JSON)
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "juli@gmail.com", "123456"))
                .bodyValue(parkingCreateDto)
                .exchange()
                .expectStatus().isForbidden()
                .expectBody()
                .jsonPath("status").isEqualTo("403")
                .jsonPath("path").isEqualTo("/api/v1/parkings/checkin")
                .jsonPath("method").isEqualTo("POST");
    }

    @Test
    public void createCheckIn_WithInvalidData_ReturnErrorStatus422() {
        ParkingCreateDto parkingCreateDto = ParkingCreateDto.builder()
                .plate("").brand("").model("")
                .color("").clientCpf("")
                .build();
        testClient.post().uri("/api/v1/parkings/checkin")
                .contentType(MediaType.APPLICATION_JSON)
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "ana@gmail.com", "123456"))
                .bodyValue(parkingCreateDto)
                .exchange()
                .expectStatus().isEqualTo(422)
                .expectBody()
                .jsonPath("status").isEqualTo("422")
                .jsonPath("path").isEqualTo("/api/v1/parkings/checkin")
                .jsonPath("method").isEqualTo("POST");
    }

    @Test
    public void createCheckIn_WithNonExistsCpf_ReturnErrorStatus404() {
        ParkingCreateDto parkingCreateDto = ParkingCreateDto.builder()
                .plate("ABC-0101").brand("VOLKS").model("GOL")
                .color("WHITE").clientCpf("07634159018")
                .build();
        testClient.post().uri("/api/v1/parkings/checkin")
                .contentType(MediaType.APPLICATION_JSON)
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "juliana@gmail.com", "123456"))
                .bodyValue(parkingCreateDto)
                .exchange()
                .expectStatus().isNotFound()
                .expectBody()
                .jsonPath("status").isEqualTo("404")
                .jsonPath("path").isEqualTo("/api/v1/parkings/checkin")
                .jsonPath("method").isEqualTo("POST");
    }

    @Sql(scripts = "/sql/parkings/parkings-insert-occupied.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/sql/parkings/parkings-delete-occupied.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @Test
    public void createCheckIn_WithNoFreeSpaces_ReturnErrorStatus404() {
        ParkingCreateDto parkingCreateDto = ParkingCreateDto.builder()
                .plate("ABC-0101").brand("VOLKS").model("GOL")
                .color("WHITE").clientCpf("88110019005")
                .build();
        testClient.post().uri("/api/v1/parkings/checkin")
                .contentType(MediaType.APPLICATION_JSON)
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "ana@email.com.br", "123456"))
                .bodyValue(parkingCreateDto)
                .exchange()
                .expectStatus().isNotFound()
                .expectBody()
                .jsonPath("status").isEqualTo("404")
                .jsonPath("path").isEqualTo("/api/v1/parkings/checkin")
                .jsonPath("method").isEqualTo("POST");
    }

    @Test
    public void getCheckin_ByAdmin_ReturnDataStatus200() {
        testClient.get()
                .uri("/api/v1/parkings/checkin/{recipt}", "20230313-101300")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "juliana@gmail.com", "123456"))
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("plate").isEqualTo("FIT-1020")
                .jsonPath("brand").isEqualTo("FIAT")
                .jsonPath("model").isEqualTo("PALIO")
                .jsonPath("color").isEqualTo("GREEN")
                .jsonPath("clientCpf").isEqualTo("55327006050")
                .jsonPath("recipt").isEqualTo("20230313-101300")
                .jsonPath("dateEntry").isEqualTo("2023-03-13 10:13:00")
                .jsonPath("spaceCode").isEqualTo("A-01");
    }

    @Test
    public void getCheckin_ByClient_ReturnDataStatus200() {
        testClient.get()
                .uri("/api/v1/parkings/checkin/{recipt}", "20230313-101300")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "juli@gmail.com", "123456"))
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("plate").isEqualTo("FIT-1020")
                .jsonPath("brand").isEqualTo("FIAT")
                .jsonPath("model").isEqualTo("PALIO")
                .jsonPath("color").isEqualTo("GREEN")
                .jsonPath("clientCpf").isEqualTo("55327006050")
                .jsonPath("recipt").isEqualTo("20230313-101300")
                .jsonPath("dateEntry").isEqualTo("2023-03-13 10:13:00")
                .jsonPath("spaceCode").isEqualTo("A-01");
    }

    @Test
    public void getCheckin_NonExistsRecipt_ReturnErrorStatus404() {

        testClient.get()
                .uri("/api/v1/parkings/checkin/{recipt}", "20230313-999999")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "juli@gmail.com", "123456"))
                .exchange()
                .expectStatus().isNotFound()
                .expectBody()
                .jsonPath("status").isEqualTo("404")
                .jsonPath("path").isEqualTo("/api/v1/parkings/checkin/20230313-999999")
                .jsonPath("method").isEqualTo("GET");
    }

    @Test
    public void createCheckOut_WithRecipt_ReturnSuccess() {
        testClient.put().uri("/api/v1/parkings/checkout/{recipt}", "20230313-101300")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "juliana@gmail.com", "123456"))
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("plate").isEqualTo("FIT-1020")
                .jsonPath("brand").isEqualTo("FIAT")
                .jsonPath("model").isEqualTo("PALIO")
                .jsonPath("color").isEqualTo("GREEN")
                .jsonPath("clientCpf").isEqualTo("55327006050")
                .jsonPath("dateEntry").isEqualTo("2023-03-13 10:13:00")
                .jsonPath("spaceCode").isEqualTo("A-01")
                .jsonPath("recipt").isEqualTo("20230313-101300")
                .jsonPath("dateExit").exists()
                .jsonPath("value").exists()
                .jsonPath("discount").exists();

    }

    @Test
    public void createCheckOut_WithNonExistsRecipt_ReturnError404() {
        testClient.put().uri("/api/v1/parkings/checkout/{recipt}", "20230313-000300")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "juliana@gmail.com", "123456"))
                .exchange()
                .expectStatus().isNotFound()
                .expectBody()
                .jsonPath("status").isEqualTo("404")
                .jsonPath("path").isEqualTo("/api/v1/parkings/checkout/20230313-000300")
                .jsonPath("method").isEqualTo("PUT");

    }

    @Test
    public void createCheckOut_AsClient_ReturnError403() {
        testClient.put().uri("/api/v1/parkings/checkout/{recipt}", "20230313-101300")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "juli@gmail.com", "123456"))
                .exchange()
                .expectStatus().isForbidden()
                .expectBody()
                .jsonPath("status").isEqualTo("403")
                .jsonPath("path").isEqualTo("/api/v1/parkings/checkout/20230313-101300")
                .jsonPath("method").isEqualTo("PUT");

    }

    @Test
    public void getParkings_ByClientCpf_ReturnSuccess() {

        PageableDto responseBody = testClient.get()
                .uri("/api/v1/parkings/cpf/{cpf}?size=1&page=0", "55327006050")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "juliana@gmail.com", "123456"))
                .exchange()
                .expectStatus().isOk()
                .expectBody(PageableDto.class)
                .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getContent().size()).isEqualTo(1);
        org.assertj.core.api.Assertions.assertThat(responseBody.getTotalPages()).isEqualTo(2);
        org.assertj.core.api.Assertions.assertThat(responseBody.getNumber()).isEqualTo(0);
        org.assertj.core.api.Assertions.assertThat(responseBody.getSize()).isEqualTo(1);

        responseBody = testClient.get()
                .uri("/api/v1/parkings/cpf/{cpf}?size=1&page=1", "55327006050")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "juliana@gmail.com", "123456"))
                .exchange()
                .expectStatus().isOk()
                .expectBody(PageableDto.class)
                .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getContent().size()).isEqualTo(1);
        org.assertj.core.api.Assertions.assertThat(responseBody.getTotalPages()).isEqualTo(2);
        org.assertj.core.api.Assertions.assertThat(responseBody.getNumber()).isEqualTo(1);
        org.assertj.core.api.Assertions.assertThat(responseBody.getSize()).isEqualTo(1);
    }

    @Test
    public void getParkings_ByClientCpfAsClient_ReturnErrorStatus403() {

        testClient.get()
                .uri("/api/v1/parkings/cpf/{cpf}", "55327006050")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "juli@gmail.com", "123456"))
                .exchange()
                .expectStatus().isForbidden()
                .expectBody()
                .jsonPath("status").isEqualTo("403")
                .jsonPath("path").isEqualTo("/api/v1/parkings/cpf/55327006050")
                .jsonPath("method").isEqualTo("GET");
    }

    @Test
    public void getParkings_ByLoggedClient_ReturnSuccess() {

        PageableDto responseBody = testClient.get()
                .uri("/api/v1/parkings?size=1&page=0")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "juli@gmail.com", "123456"))
                .exchange()
                .expectStatus().isOk()
                .expectBody(PageableDto.class)
                .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getContent().size()).isEqualTo(1);
        org.assertj.core.api.Assertions.assertThat(responseBody.getTotalPages()).isEqualTo(2);
        org.assertj.core.api.Assertions.assertThat(responseBody.getNumber()).isEqualTo(0);
        org.assertj.core.api.Assertions.assertThat(responseBody.getSize()).isEqualTo(1);

        responseBody = testClient.get()
                .uri("/api/v1/parkings?size=1&page=1")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "juli@gmail.com", "123456"))
                .exchange()
                .expectStatus().isOk()
                .expectBody(PageableDto.class)
                .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getContent().size()).isEqualTo(1);
        org.assertj.core.api.Assertions.assertThat(responseBody.getTotalPages()).isEqualTo(2);
        org.assertj.core.api.Assertions.assertThat(responseBody.getNumber()).isEqualTo(1);
        org.assertj.core.api.Assertions.assertThat(responseBody.getSize()).isEqualTo(1);
    }

    @Test
    public void getParkings_ByLoggedClientAsAdmin_ReturnErrorStatus403() {

        testClient.get()
                .uri("/api/v1/parkings")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "juliana@gmail.com", "123456"))
                .exchange()
                .expectStatus().isForbidden()
                .expectBody()
                .jsonPath("status").isEqualTo("403")
                .jsonPath("path").isEqualTo("/api/v1/parkings")
                .jsonPath("method").isEqualTo("GET");
    }

}
