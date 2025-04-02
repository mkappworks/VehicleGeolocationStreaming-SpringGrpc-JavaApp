package com.mkappworks.controller.advice;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.beans.factory.annotation.Autowired;

@WebFluxTest(controllers = TestController.class)
class ApplicationExceptionHandlerTest {

    @Autowired
    private WebTestClient webTestClient;

    @Test
    void handle_status_runtime_exception_invalid_argument() {
        webTestClient.get().uri("/test/throw-invalid-argument")
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody(String.class).isEqualTo("Invalid argument exception");
    }

    @Test
    void handle_status_runtime_exception_not_found() {
        webTestClient.get().uri("/test/throw-not-found")
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    void handle_status_runtime_exception_internal_error() {
        webTestClient.get().uri("/test/throw-internal-error")
                .exchange()
                .expectStatus().is5xxServerError()
                .expectBody(String.class).isEqualTo("INTERNAL: An internal error occurred");
    }
}

