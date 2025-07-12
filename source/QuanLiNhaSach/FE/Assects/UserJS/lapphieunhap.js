$(document).ready(function name() {
    // thông tin chi nhánh
    // const chiNhanhJSON = sessionStorage.getItem('infoChiNhanh');
    // const chiNhanh = JSON.parse(chiNhanhJSON);
    //console.log(chiNhanhJSON);
    // xử lý nhập ngày
    var input = document.getElementById("datepicker").value;
    // console.log(input);
    if (input) {
        document.getElementById("createimport").removeAttribute('disabled');
    } else {
        document.getElementById("createimport").setAttribute('disabled', true);
    }
    $('#datepicker').datepicker({
        uiLibrary: 'bootstrap5'
    });
    // <!-- Lấy danh sách mặt hàng tồn tại trong hệ thống add vào select -->
    const selection = $("#listProduct");
    
    getAllSanPham().then(result => {
        // Lặp qua từng đối tượng trong mảng result
        result.forEach(item => {
            // tạo ra từng option
            const opt = document.createElement('option');
            opt.value = item.id;
            opt.textContent = item.tenSanPham;
            selection.append(opt);
        })
    })
});
function addProductToSession(id) {
    // lấy đối tượng trả về rồi xử lý
    let price = $("#price").val();
    let quantity = $("#count").val();
    // console.log(price, quantity);
    getSanPhamById(id).then(function (productInfo) {
        // console.log(productInfo); // In thông tin của đối tượng SanPham vào console
        const newProduct = {
            id: productInfo.id,
            tenSanPham: productInfo.tenSanPham,
            loaiSanPham: productInfo.loaiSanPham,
            thuongHieu: productInfo.thuongHieu,
            tacGia: productInfo.tacGia,
            theLoai: productInfo.theLoai,
            price: price,
            quantity: quantity,
        };
        if (sessionStorage.getItem('productArray')) {
            // Lấy mảng hiện tại từ sessionStorage
            let existingArray = JSON.parse(sessionStorage.getItem('productArray'));
            matchFound = false;
            existingArray.forEach(element => {
                if (newProduct.id == element.id) {
                    // chỉ thêm số lượng
                    element.quantity = parseInt(element.quantity) + parseInt(newProduct.quantity);
                    matchFound = true;
                    sessionStorage.setItem('productArray', JSON.stringify(existingArray));
                }
            });
            if (matchFound == false) {
                // Thêm giá trị mới vào mảng
                existingArray.push(newProduct);

                // Lưu lại mảng mới vào sessionStorage
                sessionStorage.setItem('productArray', JSON.stringify(existingArray));
            }
        } else {
            // Nếu chưa có mảng trong sessionStorage, tạo một mảng mới và thêm giá trị vào
            let newArray = [newProduct];
            sessionStorage.setItem('productArray', JSON.stringify(newArray));
        }
        // Hiển thị nội dung bảng sau khi thêm sản phẩm thành công
        showSessionStorage();
    });
}
// Hàm chuyển đổi giá tiền sang định dạng VNĐ
// function formatCurrency(value) {
//     // Sử dụng toLocaleString() với ngôn ngữ 'vi-VN' (Tiếng Việt, Việt Nam)
//     // và các tùy chọn định dạng tiền tệ (style: 'currency', currency: 'VND')
//     return value.toLocaleString('vi-VN', { style: 'currency', currency: 'VND' });
// }
function updateUI() {
    $("#table-body tr").remove();
    totalPrice = 0;
    $("#totalImportPricing").text("Tổng cộng: " + formatCurrency(totalPrice));
    // location.reload();
}
let totalPrice = 0;
function showSessionStorage() {
    totalPrice = 0;
    updateUI();
    sortProductArrayById();
    let storedArray = JSON.parse(sessionStorage.getItem('productArray'));
    const tableBody = $("#table-body");

    if (storedArray) {
        storedArray.forEach(item => {
            const row = $("<tr>");

            $("<td>").text(item.id).appendTo(row);
            $("<td>").text(item.tenSanPham).appendTo(row);
            $("<td>").text(item.loaiSanPham).appendTo(row);
            $("<td>").text(item.theLoai).appendTo(row);
            $("<td>").text(item.tacGia).appendTo(row);
            $("<td>").text(item.thuongHieu).appendTo(row);
            $("<td>").text(formatCurrency(item.price * 1)).appendTo(row);
            $("<td>").text(item.quantity).appendTo(row);
            $("<td>").text(formatCurrency(item.price * item.quantity)).appendTo(row);

            totalPrice += item.price * item.quantity;
            tableBody.append(row);
        });
    }

    $("#totalImportPricing").text("Tổng cộng: " + formatCurrency(totalPrice));
}

/* // xóa thông tin mảng sản phẩm trong session */
function clearSessionProductArray() {
    sessionStorage.removeItem("productArray");
    updateUI();
    document.getElementById("datepicker").value = "";
    checkValidDate();
};
// sắp xếp
function sortProductArrayById() {
    // Lấy mảng từ sessionStorage
    let storedArray = JSON.parse(sessionStorage.getItem('productArray'));

    // Sử dụng phương thức sort() để sắp xếp mảng theo trường 'id'
    if (storedArray) {
        storedArray.sort((a, b) => a.id - b.id);
        // Lưu lại mảng đã sắp xếp vào sessionStorage
        sessionStorage.setItem('productArray', JSON.stringify(storedArray));
    }
}
showSessionStorage();

// Check xem sản phẩm được chọn từ trước hay chưa
function checkSelect() {
    let select = document.getElementById("listProduct");
    let pricing = document.getElementById("price");
    let matchFound = false;

    let existingArray = JSON.parse(sessionStorage.getItem('productArray'));
    if (existingArray) {
        existingArray.forEach(element => {
            if (select.value == element.id) {
                pricing.value = element.price;
                pricing.disabled = true;
                matchFound = true;
                $("#count").val("");
            }
            $("#count").val("");
        });
        if (!matchFound) {
            $("#count").val("");
            pricing.value = "";
            pricing.disabled = false; // Nếu không tìm thấy kết quả, bỏ chế độ disabled
        }

    }
};
function checkValidDate() {
    var input = document.getElementById("datepicker").value;
    if (input != "") {
        document.getElementById("createimport").removeAttribute('disabled');
    } else {
        document.getElementById("createimport").setAttribute('disabled', true);
    }
};
function handleImport() {
    let productArray = JSON.parse(sessionStorage.getItem('productArray'));
    if (!productArray) {
        $("#liveToast").removeClass("bg-success");
        $("#liveToast").addClass("bg-danger");
        const toast = new bootstrap.Toast($("#liveToast"));
        $("#toast-context").text("Bạn chưa nhập bất kỳ sản phẩm nào");
        toast.show();
    }
    else {
        // const chiNhanhJSON = sessionStorage.getItem('infoChiNhanh');
        // const chiNhanh = JSON.parse(chiNhanhJSON);
        const dateImport = $('#datepicker').val();
        addImport(productArray, chiNhanh.id, dateImport);
        clearSessionProductArray();
    }
}