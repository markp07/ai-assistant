package nl.markpost.aiassistant;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ServletComponentScan
@ActiveProfiles(value = "test")
class AiAssistantTest {

  @LocalServerPort private int port;

  @Autowired private TestRestTemplate restTemplate;

  @Test
  void applicationStartsSuccessfully() {
    String response =
        this.restTemplate.getForObject("http://localhost:" + port + "/", String.class);
    assertThat(response).contains("<h1>HTTP Status 404 – Not Found</h1>");
  }

  @Test
  void applicationContextLoads() {
    AiAssistant.main(new String[] {});
  }
}
