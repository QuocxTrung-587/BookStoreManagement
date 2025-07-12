package vn.tdtu.finalterm.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.web.client.RestTemplate;
import vn.tdtu.finalterm.models.ResponseObject;

import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
// 1 instance of the test class to run all the tests instead of creating an instance for each method
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
// create a totally new application context after finish running the test class
@DirtiesContext
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public abstract class BaseTest<M> {
  @LocalServerPort // read the current port
  private int port;
  ClientHttpRequestFactory factory = new BufferingClientHttpRequestFactory(new SimpleClientHttpRequestFactory());
  RestTemplate restTemplate = new RestTemplate(factory);
  HttpHeaders headers = new HttpHeaders();
  ObjectMapper mapper = new ObjectMapper();
  M requestDTO;
  M data;
  List<M> listData;
  HttpEntity<M> requestEntity;
  ResponseEntity<ResponseObject<M>> responseEntity;
  ResponseEntity<ResponseObject<List<M>>> responseListEntity;
  Class<ResponseObject<List<M>>> responseListType = (Class<ResponseObject<List<M>>>) (Class<?>) ResponseObject.class;
  Class<ResponseObject<M>> responseType = (Class<ResponseObject<M>>) (Class<?>) ResponseObject.class;
  Long id;

  protected String createURLWithPort(String uri) {
    return "http://localhost:" + port + uri;
  }
}
