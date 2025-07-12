token = sessionStorage.getItem("tokenData");
function getAllHoaDonByCNId(chiNhanhId) {
  const apiUrl = "http://localhost:8080/hoaDon/" + chiNhanhId;
  return $.ajax({
    url: apiUrl,
    type: "GET",
    dataType: "json",
    headers: {
      Authorization: "Bearer " + token
    },
  })
    .then(response => {
      const hoaDonArray = response.data.map(element => new HoaDon(element.id, element.ngayLap, element.tongCong, element.chiNhanh));
      const chiTietHoaDonArray = response.data.flatMap(element => element.chiTietHoaDonList.map(item => new ChiTietHoaDon(item.id, item.giaBan, item.soLuong, item.tongTien, item.sanPham, item.chiNhanh, item.hoaDon)));

      // Gộp hai mảng phieuNhapArray và chiTietPhieuNhapArray thành một mảng duy nhất
      const combinedArray = [...hoaDonArray, ...chiTietHoaDonArray];

      return combinedArray;
    })
    .fail(function (xhr, status, error) {
      if (xhr.status == 403) {
        window.location.href = "../../Assects/pagesEvent/403.html"
      }
      console.error("Error while fetching products:", error);
      throw error;
    });
}
function addBill(productArray, chiNhanhId, ngayNhap) {
  // xử lý định dạng ngày theo yy-MM-dd
  const ngayNhapDate = new Date(ngayNhap);
  const formattedDate = ngayNhapDate.getFullYear() + '-' + (ngayNhapDate.getMonth() + 1).toString().padStart(2, '0') + '-' + ngayNhapDate.getDate().toString().padStart(2, '0');
  // xử lý dữ liệu
  const sanPhamId = []
  const hoaDon = { ngayLap: formattedDate };
  const chiTietHoaDon = [];
  productArray.forEach(element => {
    sanPhamId.push(element.id);
    const newInfoProduct = {
      giaBan: element.price,
      soLuong: element.quantity
    }
    chiTietHoaDon.push(newInfoProduct);
  });
  $.ajax({
    url: "http://localhost:8080/chiTietHD",
    type: "POST",
    contentType: "application/json",
    dataType: "json",
    headers: {
      Authorization: "Bearer " + token
    },
    data: JSON.stringify({
      "sanPhamId": sanPhamId,
      "chiNhanhId": chiNhanhId,
      "hoaDon": hoaDon,
      "chiTietHoaDon": chiTietHoaDon
    }),
    success: function (response) {
      $("#liveToast").removeClass("bg-danger");
      $("#liveToast").addClass("bg-success");
      const toast = new bootstrap.Toast($("#liveToast"));
      $("#toast-context").text("Tạo hóa đơn thành công");
      toast.show();
      clearSessionProductArray();
    },
    error: function (xhr) {
      if (xhr.status == 403) {
        window.location.href = "../../Assects/pagesEvent/403.html"
      }
      const errorMessage = xhr.responseText;
      const errorObject = JSON.parse(errorMessage);
      const errorMessageText = errorObject.message;
      $("#liveToast").removeClass("bg-success");
      $("#liveToast").addClass("bg-danger");
      const toast = new bootstrap.Toast($("#liveToast"));
      $("#toast-context").text(errorMessageText);
      toast.show();
      clearSessionProductArray();
    }
  })
}
function delBill(hoaDonId) {
  console.log(hoaDonId);
  const apiUrl = "http://localhost:8080/hoaDon/" + hoaDonId;
  $.ajax({
    url: apiUrl,
    type: "DELETE",
    dataType: "json",
    headers: {
      Authorization: "Bearer " + token
    },
    success: function (response) {
      if (response.status == "ok") {
        $("#liveToast").removeClass("bg-danger");
        $("#liveToast").addClass("bg-success");
        const toast = new bootstrap.Toast($("#liveToast"));
        $("#toast-context").text("Xóa hóa đơn thành công");
        toast.show();
        loadHoaDon();
      } else {
        return response.message;
      }
    },
    error: function (xhr, status, error) {
      if (error.status == 403) {
        window.location.href = "../../Assects/pagesEvent/403.html"
      }
      $("#liveToast").removeClass("bg-success");
      $("#liveToast").addClass("bg-danger");
      const toast = new bootstrap.Toast($("#liveToast"));
      $("#toast-context").text("Error: " + error);
      toast.show();
    }
  });
}
function getHoaDonsByNgayLap(ngayLap) {
  return $.ajax({
    url: "http://localhost:8080/hoaDonQuery",
    type: "POST",
    contentType: "application/json",
    dataType: "json",
    headers: {
      Authorization: "Bearer " + token
    },
    data: JSON.stringify({
      "ngayLap": ngayLap
    })
  })
    .then(response => {
      const hoaDonArray = response.data.map(element => new HoaDon(element.id, element.ngayLap, element.tongCong, element.chiNhanh));
      return hoaDonArray;
    })
    .fail(response => {
      if (response.status == 403) {
        window.location.href = "../../Assects/pagesEvent/403.html"
      }
    })
};
