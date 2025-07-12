token = sessionStorage.getItem("tokenData");
function getAllPhieuNhapByCNId(chiNhanhId) {
  const apiUrl = "http://localhost:8080/phieuNhap/" + chiNhanhId;
  return $.ajax({
    url: apiUrl,
    type: "GET",
    dataType: "json",
    headers: {
      Authorization: "Bearer " + token
    },
  })
    .then(response => {
      const phieuNhapArray = response.data.map(element => new PhieuNhap(element.id, element.ngayNhap, element.tongCong, element.chiNhanh));
      const chiTietPhieuNhapArray = response.data.flatMap(element => element.chiTietPhieuNhapList.map(item => new ChiTietPhieuNhap(item.id, item.giaNhap, item.soLuong, item.tongTien, item.sanPham, item.chiNhanh, item.phieuNhap)));

      // Gộp hai mảng phieuNhapArray và chiTietPhieuNhapArray thành một mảng duy nhất
      const combinedArray = [...phieuNhapArray, ...chiTietPhieuNhapArray];

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

function addImport(productArray, chiNhanhId, ngayNhap) {
  // xử lý định dạng ngày theo yy-MM-dd
  const ngayNhapDate = new Date(ngayNhap);
  const formattedDate = ngayNhapDate.getFullYear() + '-' + (ngayNhapDate.getMonth() + 1).toString().padStart(2, '0') + '-' + ngayNhapDate.getDate().toString().padStart(2, '0');
  // xử lý dữ liệu
  const sanPhamId = []
  const phieuNhap = { ngayNhap: formattedDate };
  const chiTietPhieuNhap = [];
  productArray.forEach(element => {
    sanPhamId.push(element.id);
    const newInfoProduct = {
      giaNhap: element.price,
      soLuong: element.quantity
    }
    chiTietPhieuNhap.push(newInfoProduct);
  });
  // console.log("sanPhamId: " + sanPhamId);
  // console.log("chiNhanhId: " + chiNhanhId);
  // console.log("phieuNhap: " + phieuNhap);
  // console.log("chiTietPhieuNhap: " + chiTietPhieuNhap);
  $.ajax({
    url: "http://localhost:8080/chiTietPN",
    type: "POST",
    contentType: "application/json",
    dataType: "json",
    headers: {
      Authorization: "Bearer " + token
    },
    data: JSON.stringify({
      "sanPhamId": sanPhamId,
      "chiNhanhId": chiNhanhId,
      "phieuNhap": phieuNhap,
      "chiTietPhieuNhap": chiTietPhieuNhap
    }),
    success: function (response) {
      $("#liveToast").removeClass("bg-danger");
      $("#liveToast").addClass("bg-success");
      const toast = new bootstrap.Toast($("#liveToast"));
      $("#toast-context").text("Thêm phiếu nhập thành công");
      toast.show();
      clearSessionProductArray();
    },
    error: function (response) {
      if (response.status == 403) {
        window.location.href = "../../Assects/pagesEvent/403.html"
      }
    }
  })
}
function delImport(phieuNhapId) {
  //console.log(phieuNhapId);
  const apiUrl = "http://localhost:8080/phieuNhap/" + phieuNhapId;
  $.ajax({
    url: apiUrl,
    type: "DELETE",
    dataType: "json",
    contentType: "json/application",
    headers: {
      Authorization: "Bearer " + token
    },
    success: function (response) {
      if (response.status == "ok") {
        $("#liveToast").removeClass("bg-danger");
        $("#liveToast").addClass("bg-success");
        const toast = new bootstrap.Toast($("#liveToast"));
        $("#toast-context").text("Xóa phiếu nhập thành công");
        toast.show();
        loadPhieuNhap();
      } else {
        return response.message;
      }
    },
    error: function (xhr, status) {
      if (xhr.status == 403) {
        window.location.href = "../../Assects/pagesEvent/403.html"
      }
      const errorMessage = xhr.responseText;
      const errorObject = JSON.parse(errorMessage);
      const errorMessageText = errorObject.message;
      $("#liveToast").removeClass("bg-success");
      $("#liveToast").addClass("bg-danger");
      const toast = new bootstrap.Toast($("#liveToast"));
      // $("#toast-context").text("Error:  " + errorMessageText);
      $("#toast-context").text("Error:  Số lượng trong kho không đủ bằng tổng số lượng đã nhập.");
      toast.show();
    }
  });
}
function getPricingImport(chiNhanhId, sanPhamId) {
  return $.ajax({
      url: "http://localhost:8080/chiTietPN/" + chiNhanhId + "/" + sanPhamId,
      type: "GET",
      contentType: "application/json",
      dataType: "json",
      headers: {
        Authorization: "Bearer " + token
      }
  })
      .then(response => {
          // chỉ lấy giá của phiếu nhập lần gần nhất
          const element = response.data[response.data.length - 1];
          if(element){
            return element.giaNhap;
          }
      })
      .fail(response =>{
        if (response.status == 403) {
          window.location.href = "../../Assects/pagesEvent/403.html"
        }
      })
};
function getPhieuNhapsByNgayNhap(ngayNhap) {
  return $.ajax({
    url: "http://localhost:8080/phieuNhapQuery",
    type: "POST",
    contentType: "application/json",
    dataType: "json",
    headers: {
      Authorization: "Bearer " + token
    },
    data: JSON.stringify({
      "ngayNhap": ngayNhap
    })
  })
  .then (response =>{
    const phieuNhapArray = response.data.map(element => new PhieuNhap(element.id, element.ngayNhap, element.tongCong, element.chiNhanh));
    return phieuNhapArray;
  })
  .fail(response =>{
    if (response.status == 403) {
      window.location.href = "../../Assects/pagesEvent/403.html"
    }
  })
};
