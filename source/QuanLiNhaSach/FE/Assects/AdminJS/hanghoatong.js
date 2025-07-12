$(document).ready(function () {
  loadSP();
})
function loadSP() {
  updateUI();
  const tableBody = $("#table-body");

  getAllSanPham()
    .then((result) => {
      result.forEach((item) => {
        const buttonsHtml = `
        <button type="button" value="1" class="btn btn-primary" data-bs-toggle="modal" data-bs-target="#exampleModal3" onclick="openEditModal(this)">
          <i class="bi bi-arrow-repeat"></i>&nbspSửa
        </button>
        <button type="button" class="btn btn-danger" data-bs-toggle="modal" data-bs-target="#exampleModal4" onclick="openDelModal(this)">
          <i class="bi bi-dash-circle-dotted"></i>&nbspXóa
        </button>`;

        const row = $("<tr>")
          .append($("<th>").attr("scope", "row").text(item.id))
          .append($("<td>").text(item.tenSanPham))
          .append($("<td>").text(item.loaiSanPham))
          .append($("<td>").text(item.thuongHieu))
          .append($("<td>").text(item.tacGia))
          .append($("<td>").text(item.theLoai))
          .append($("<td>").html(buttonsHtml));

        tableBody.append(row);
      });
      // Sorting sau khi dữ liệu từ Ajax được tải về
      eventSortingTable();
    })
    .catch((error) => {
      console.error(error);
    });
}
function updateUI() {
  $("#table-body tr").remove();
}


// Lấy thông tin chi nhánh modal sửa
function openEditModal(button) {
  const idSP = $(button).closest("tr").find("th")[0];
  const tenSanPham = $(button).closest("tr").find("td")[0];
  const loaiSanPham = $(button).closest("tr").find("td")[1];
  const thuongHieu = $(button).closest("tr").find("td")[2];
  const tacGia = $(button).closest("tr").find("td")[3];
  const theLoai = $(button).closest("tr").find("td")[4];
  $("#idSPham").val(idSP.textContent);
  $("#productInfo").val(tenSanPham.textContent);
  $("#loaisanphamInfo").val(loaiSanPham.textContent);
  $("#publisherInfo").val(thuongHieu.textContent);
  $("#tacgiaInfo").val(tacGia.textContent);
  $("#categoriesInfo").val(theLoai.textContent);
}

function updateSanPham() {
  const idSP = $("#idSPham").val();
  const tenSanPham = $("#productInfo").val();
  const loaiSanPham = $("#loaisanphamInfo").val();
  const thuongHieu = $("#publisherInfo").val();
  const tacGia = $("#tacgiaInfo").val();
  const theLoai = $("#categoriesInfo").val();
  updateSP(idSP, tenSanPham, loaiSanPham, thuongHieu, tacGia, theLoai);
}
// Lấy thông tin chi nhánh modal xóa
function openDelModal(button) {
  const sanPhamName = $(button).closest("tr").find("td")[0];
  const idSP = $(button).closest("tr").find("th")[0];
  $("#text-confirm").text("Xác nhận xóa sản phẩm " + sanPhamName.textContent + "?");
  $("#idSP").val(idSP.textContent);
}

function deleteSanPham() {
  const idSP = $('#idSP').val();
  deleteSP(idSP);
}