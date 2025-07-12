token = sessionStorage.getItem("tokenData");
function getAllSanPham() {
  const apiUrl = "http://localhost:8080/sanPham";
  return $.ajax({
    url: apiUrl,
    type: "GET",
    dataType: "json",
    headers: {
      Authorization: "Bearer " + token
    },
  })
    .then((response) => {
      const sanPhamArray = response.data.map(
        (element) =>
          new SanPham(
            element.id,
            element.tenSanPham,
            element.loaiSanPham,
            element.thuongHieu,
            element.tacGia,
            element.theLoai
          )
      );
      // console.log(sanPhamArray)
      return sanPhamArray;
    })
    .fail(function (xhr, status, error) {
      if (xhr.status == 403) {
        window.location.href = "../../Assects/pagesEvent/403.html"
      }
      console.error("Error while fetching products:", error);
      throw error;
    });
}
function getSanPhamById(id) {
  const apiUrl = "http://localhost:8080/sanPham/" + id;
  return $.ajax({
    url: apiUrl,
    type: "GET",
    dataType: "json",
    headers: {
      Authorization: "Bearer " + token
    },
  })
    .then((response) => {
      // const sanPham = response.map(element => new SanPham(element.id, element.tenSanPham, element.loaiSanPham, element.thuongHieu, element.tacGia, element.theLoai));
      // // console.log(sanPhamArray)
      // return sanPham;

      // Trích xuất thông tin cần thiết từ đối tượng
      const sanPham = new SanPham(
        response.data.id,
        response.data.tenSanPham,
        response.data.loaiSanPham,
        response.data.thuongHieu,
        response.data.tacGia,
        response.data.theLoai
      );
      // console.log(response)

      return sanPham;
    })
    .fail(function (xhr, status, error, response) {
      if (xhr.status == 403) {
        window.location.href = "../../Assects/pagesEvent/403.html"
      }
      console.error("Error while fetching products:", error);
      throw error;
    });
}

function addSanPham() {
  const sanPham = new SanPham(
    null, // id tự động tăng
    $("#product").val(),
    $("#loaisanpham").val(),
    $("#publisher").val(),
    $("#tacgia").val(),
    $("#categories").val()
  );
  if ($("#product").val() == "" || $("#loaisanpham").val() == "" || $("#publisher").val() == "") {
    $("#liveToast").removeClass("bg-success");
    $("#liveToast").addClass("bg-danger");
    const toast = new bootstrap.Toast($("#liveToast"));
    $("#toast-context").text("Vui lòng nhập đầy đủ thông tin mặt hàng");
    toast.show();
  }
  else {
    $.ajax({
      type: "POST",
      url: "http://localhost:8080/sanPham",
      data: JSON.stringify(sanPham),
      contentType: "application/json",
      headers: {
        Authorization: "Bearer " + token
      },
      success: function (response) {
        if (response.status === "ok") {
          // Thêm sản phẩm vào danh sách hiện có trong bảng
          const buttonsHtml = `
            <button type="button" value="1" class="btn btn-primary" data-bs-toggle="modal" data-bs-target="#exampleModal3" onclick="openEditModal(this)">
              <i class="bi bi-arrow-repeat"></i>&nbspSửa
            </button>
            <button type="button" class="btn btn-danger" data-bs-toggle="modal" data-bs-target="#exampleModal4" onclick="openDelModal(this)">
              <i class="bi bi-dash-circle-dotted"></i>&nbspXóa
            </button>`;
          const newRow = $("<tr>")
            .append($("<th>").text(response.data.id))
            .append($("<td>").text(sanPham.tenSanPham))
            .append($("<td>").text(sanPham.loaiSanPham))
            .append($("<td>").text(sanPham.thuongHieu))
            .append($("<td>").text(sanPham.tacGia))
            .append($("<td>").text(sanPham.theLoai))
            .append($("<td>").html(buttonsHtml));

          $("#table-body").append(newRow);


          // Đóng modal
          $("#exampleModal2").modal("hide");

          // Làm mới danh sách sản phẩm
          $("#liveToast").removeClass("bg-danger");
          $("#liveToast").addClass("bg-success");
          const toast = new bootstrap.Toast($("#liveToast"));
          $("#toast-context").text("Thêm sản phẩm thành công");
          refreshProductList();
          toast.show();

        }
      },
      error: function (response) {
        if (response.status == 403) {
          window.location.href = "../../Assects/pagesEvent/403.html"
        }
        $("#exampleModal2").modal("hide");
        $("#liveToast").removeClass("bg-success");
        $("#liveToast").addClass("bg-danger");
        const toast = new bootstrap.Toast($("#liveToast"));
        $("#toast-context").text("Thêm sản phẩm thất bại. Sản phẩm đã tồn tại trong hệ thống.");
        toast.show();
      },
    });
  }
}

function refreshProductList() {
  const emptyRow = $("<tr>")
    .append($("<td>"))
    .append($("<td>").text(""))
    .append($("<td>").text(""))
    .append($("<td>").text(""))
    .append($("<td>").text(""))
    .append($("<td>").text(""));
  $("#table-body").prepend(emptyRow);
}

function updateSP(idSP, tenSP, loaiSP, thuongHieu, tacGia, theLoai) {
  $.ajax({
    url: "http://localhost:8080/sanPham/" + idSP,
    dataType: "json",
    contentType: "application/json",
    type: "PUT",
    headers: {
      Authorization: "Bearer " + token
    },
    data: JSON.stringify({
      tenSanPham: tenSP,
      loaiSanPham: loaiSP,
      thuongHieu: thuongHieu,
      tacGia: tacGia,
      theLoai: theLoai
    }),
    success: function (response) {
      const toast = new bootstrap.Toast($("#liveToast"));
      $("#toast-context").text("Sửa thông tin sản phẩm thành công");
      loadSP();
      toast.show();
    },
    error: function (responseparams) {
      if (response.status == 403) {
        window.location.href = "../../Assects/pagesEvent/403.html"
      }
    }
  });
}

function deleteSP(idSP) {
  $.ajax({
    url: "http://localhost:8080/sanPham/" + idSP,
    dataType: "json",
    contentType: "application/json",
    type: "DELETE",
    headers: {
      Authorization: "Bearer " + token
    },
    success: function (response) {
      const toast = new bootstrap.Toast($("#liveToast"));
      $("#toast-context").text("Xóa sản phẩm thành công");
      loadSP();
      toast.show();
    },
    error: function (response) {
      if (response.status == 403) {
        window.location.href = "../../Assects/pagesEvent/403.html"
      }
    }
  });
}
