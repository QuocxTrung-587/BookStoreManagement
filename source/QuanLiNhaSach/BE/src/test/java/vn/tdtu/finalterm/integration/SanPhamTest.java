package vn.tdtu.finalterm.integration;

import com.fasterxml.jackson.core.type.TypeReference;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import vn.tdtu.finalterm.models.SanPham;

import java.util.List;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class SanPhamTest extends BaseTest<SanPham> {
  final String apiUrl = "/sanPham";
  @Test
  @Order(1)
  public void testCreate() {
    requestDTO = new SanPham().setTenSanPham("tenSanPham").setLoaiSanPham("loaiSanPham").setThuongHieu("thuongHieu");
    requestEntity = new HttpEntity(requestDTO, headers);
    responseEntity = restTemplate.exchange(
      createURLWithPort(apiUrl),
      HttpMethod.POST,
      requestEntity,
      responseType);

    assertEquals(200, responseEntity.getStatusCode().value());

    data = mapper.convertValue(Objects.requireNonNull(responseEntity.getBody()).getData(), SanPham.class);

    assertEquals("tenSanPham", data.getTenSanPham());
    assertEquals("loaiSanPham", data.getLoaiSanPham());
    assertEquals("thuongHieu", data.getThuongHieu());
    id = data.getId();
  }

  @Test
  @Order(2)
  public void testGetById() throws Exception {
    requestEntity = new HttpEntity<>(null, headers);
    responseEntity =
      restTemplate.exchange(
        createURLWithPort(apiUrl.concat("/").concat(id.toString())),
        HttpMethod.GET,
        requestEntity,
        responseType);

    assertEquals(200, responseEntity.getStatusCode().value());

    data = mapper.convertValue(Objects.requireNonNull(responseEntity.getBody()).getData(), SanPham.class);

    assertEquals("tenSanPham", data.getTenSanPham());
    assertEquals("loaiSanPham", data.getLoaiSanPham());
    assertEquals("thuongHieu", data.getThuongHieu());
  }

  @Test
  @Order(3)
  public void testUpdateById() throws Exception {
    requestDTO.setTenSanPham("updated");
    requestEntity = new HttpEntity<>(requestDTO, headers);
    responseEntity =
      restTemplate.exchange(
        createURLWithPort(apiUrl.concat("/").concat(id.toString())),
        HttpMethod.PUT,
        requestEntity,
        responseType);

    assertEquals(200, responseEntity.getStatusCode().value());

    data = mapper.convertValue(Objects.requireNonNull(responseEntity.getBody()).getData(), SanPham.class);

    assertEquals("updated", data.getTenSanPham());
    assertEquals("loaiSanPham", data.getLoaiSanPham());
    assertEquals("thuongHieu", data.getThuongHieu());
  }

  @Test
  @Order(4)
  public void testGetlist() throws Exception {
    requestEntity = new HttpEntity<>(null, headers);
    responseListEntity =
      restTemplate.exchange(
        createURLWithPort(apiUrl),
        HttpMethod.GET,
        requestEntity,
        responseListType);

    assertEquals(200, responseListEntity.getStatusCode().value());

    listData = mapper.convertValue(responseListEntity.getBody().getData(), new TypeReference<List<SanPham>>() {});

    assertThat(listData)
      .extracting(SanPham::getId, SanPham::getTenSanPham)
      .containsOnlyOnce(tuple(id, "updated"));
  }

  @Test
  @Order(5)
  public void testDeleteById() throws Exception {
    requestEntity = new HttpEntity<>(null, headers);
    responseEntity =
      restTemplate.exchange(
        createURLWithPort(apiUrl).concat("/").concat(id.toString()),
        HttpMethod.DELETE,
        requestEntity,
        responseType);

    assertEquals(200, responseEntity.getStatusCode().value());
  }
}
