token = sessionStorage.getItem("tokenData");
function getAllPhieuCSP() {
  const apiUrl = "http://localhost:8080/phieuCSP";
  return $.ajax({
    url: apiUrl,
    type: "GET",
    dataType: "json",
    headers: {
      Authorization: "Bearer " + token
    },
  }).then((response) => {
    const phieuChuyenArray = response.data.map(
      (element) =>
        new PhieuChuyen(
          element.chiNhanhChuyenId,
          element.chiNhanhNhanId,
          element.soLuongChuyen,
          element.sanPhamId
        )
    )
    .fail(response =>{
      if (response.status == 403) {
        window.location.href = "../../Assects/pagesEvent/403.html"
      }
    })
    return phieuChuyenArray;
  });
}

function getAllPhieuCSPFromChiNhanh(chiNhanhId) {
  const apiUrl = "http://localhost:8080/phieuCSPFrom/" + chiNhanhId;
  return $.ajax({
    url: apiUrl,
    type: "GET",
    dataType: "json",
    headers: {
      Authorization: "Bearer " + token
    },
  }).then((response) => {
    const phieuChuyenArray = response.data.map(
      (element) =>
        new PhieuChuyen(
          element.chiNhanhChuyenId,
          element.chiNhanhNhanId,
          element.soLuongChuyen,
          element.sanPhamId
        )
    )
    .fail(response => {
      if (response.status == 403) {
        window.location.href = "../../Assects/pagesEvent/403.html"
      }
    })
    return phieuChuyenArray;
  });
}

function insertNewPhieuCSP(newPhieu) {
  const apiUrl = "http://localhost:8080/phieuCSP";

  newPhieu.ngayChuyen = new Date(); // Ngày chuyển sản phẩm hiện tại

  return $.ajax({
    url: apiUrl,
    type: "POST",
    contentType: "application/json",
    data: JSON.stringify(newPhieu),
    headers: {
      Authorization: "Bearer " + token
    },
  })
    .then((response) => {
      $("#liveToast").removeClass("bg-danger");
      $("#liveToast").addClass("bg-success");
      const toast = new bootstrap.Toast($("#liveToast"));
      $("#toast-context").text("Chuyển sản phẩm thành công");
      toast.show();
      console.log("Phiếu chuyển sản phẩm đã được thêm:", response);
    })
    .catch((error) => {
      if (error.status == 403) {
        window.location.href = "../../Assects/pagesEvent/403.html"
      }
      $("#liveToast").removeClass("bg-success");
      $("#liveToast").addClass("bg-danger");
      const toast = new bootstrap.Toast($("#liveToast"));
      $("#toast-context").text("Đã có lỗi xảy ra");
      toast.show();
      console.error("Lỗi khi thêm phiếu chuyển sản phẩm:", error);
    });
}
