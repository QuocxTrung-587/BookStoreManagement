(function () {
    'use strict';
    var forms = document.querySelectorAll('.needs-validation');
    Array.prototype.slice.call(forms).forEach(function (form) {
        form.addEventListener('submit', function (event) {
            if (!form.checkValidity()) {
                event.preventDefault();
                event.stopPropagation();
            }
            form.classList.add('was-validated');
        }, false);
    });
})();
$(document).ready(function () {
    loadChiNhanh("table-body");
})
// update table
function updateUI() {
    $("#table-body tr").remove();
}
function loadChiNhanh(idTable) {
    updateUI();
    const tableBody = $('#' + idTable); 

    getAllChiNhanh()
        .then(result => {
            result.forEach(item => {
                const row = $('<tr>');

                $('<td>', { 'scope': 'row', 'text': item.id }).appendTo(row);
                $('<td>', { 'text': item.tenChiNhanh }).appendTo(row);
                $('<td>', { 'text': item.diaChi }).appendTo(row);
                $('<td>', { 'text': "username: " + item.taiKhoanFK.taiKhoan + ", password: " + item.taiKhoanFK.matKhau }).appendTo(row);

                if(item.taiKhoanFK.taiKhoan == "this.is.manager.fahasa@gmail.com"){
                    $('<td>').appendTo(row)
                }
                else{
                    const buttonsCell = $('<td>');
                    buttonsCell.html(`
                        <button type="submit" id="edit-button"  class="btn btn-primary" data-bs-toggle="modal" data-bs-target="#exampleModal" onclick="openEditModal(this)"><i class="bi bi-arrow-repeat"></i>&nbspSửa</button>
                        <button type="button" class="btn btn-danger" data-bs-toggle="modal" data-bs-target="#exampleModal1" onclick="openDelModal(this)"><i class="bi bi-dash-circle-dotted"></i>&nbspXóa</button>
                    `);
                    buttonsCell.appendTo(row);
                }
                // Thêm điều kiện xác nhận email
                const enableCondition = $('<td>');
                enableCondition.attr('scope', 'row');
                enableCondition.text(item.taiKhoanFK.enabled ? 'Đã xác nhận' : 'Chưa xác nhận email');
                enableCondition.appendTo(row);

                row.appendTo(tableBody);
            });

            // Sorting sau khi dữ liệu từ Ajax được tải về
            eventSortingTable();
        })
        .catch(error => {
            console.error(error);
        });
}

// Lấy reference đến tbody để thêm các dòng vào
// Lấy thông tin chi nhánh modal xóa
function openDelModal(button) {
    const chiNhanhName = $(button).closest("tr").find("td")[1];
    const chiNhanhId = $(button).closest("tr").find("td")[0];
    $("#text-confirm").text("Xác nhận xóa chi nhánh " + chiNhanhName.textContent + "?");
    $("#id-chinhanh").val(chiNhanhId.textContent);
}
// Lấy thông tin chi nhánh modal
function openEditModal(button) {
    const chiNhanhId = $(button).closest("tr").find("td")[0];
    const chiNhanhName = $(button).closest("tr").find("td")[1];
    const chiNhanhAddress = $(button).closest("tr").find("td")[2];
    $("#chiNhanhName").val(chiNhanhName.textContent);
    $("#chiNhanhAddress").val(chiNhanhAddress.textContent);
    $("#id-chiNhanhEdit").val(chiNhanhId.textContent);
}
updateUI();
