package org.example.learn_springboot;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public class HelloApiTest {
	@Test
	void helloApi() {
		// API 요청을 호출해서 사용할 수 있다. 스프링에서 제공
		// 400이나 500 에러일때 예외를 던진다.
		// RestTemplate

		// 응답 코드로 던진다.
		TestRestTemplate restTemplate = new TestRestTemplate();
		ResponseEntity<String> result = restTemplate.getForEntity("http://localhost:8080/hello?name={name}", String.class,
			"suah");

		// 응답 검증 3가지
		// status code 200
		Assertions.assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);

		// header type : text/plain
		Assertions.assertThat(result.getHeaders().getFirst(HttpHeaders.CONTENT_TYPE)).startsWith(MediaType.TEXT_PLAIN_VALUE);
		// body : hello suah
		Assertions.assertThat(result.getBody()).isEqualTo("Hello suah");
	}
}
