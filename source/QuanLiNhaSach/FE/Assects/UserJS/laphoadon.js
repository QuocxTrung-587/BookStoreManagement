$(document).ready(function name() {
    // xử lý nhập ngày
    var input = document.getElementById("datepicker").value;
    // console.log(input);
    if (input) {
        document.getElementById("createBill").removeAttribute('disabled');
    } else {
        document.getElementById("createBill").setAttribute('disabled', true);
    }
    $('#datepicker').datepicker({
        uiLibrary: 'bootstrap5'
    });
    // <!-- Lấy danh sách mặt hàng tồn tại trong chi nhánh add vào select -->
    const selection = $("#listProduct");

    getQLSanPhamByCNId(chiNhanh.id).then(result => {
        // Lặp qua từng đối tượng trong mảng result
        result.forEach(item => {
            if (item.tongHang != 0 && item.trangThai == 1) {
                const option = $("<option>").text(item.maSP.tenSanPham).val(item.id);
                selection.append(option);
            }

        })
    })
});
async function checkSelect1() {
    var select = document.getElementById("listProduct");
    var quantity = document.getElementById("count");
    const SanPhamArray = await getQLSanPhamByCNId(chiNhanh.id);
    console.log(chiNhanh.id)
    console.log(select.value)
    SanPhamArray.forEach(element => {
        if (element.id == select.value) {
            quantity.setAttribute('max', element.tongHang);
            quantity.setAttribute('min', '0');
        }
    });
};
function addProductToSession(id) {
    // lấy đối tượng trả về rồi xử lý
    // let price = $("#price").val();
    let quantity = $("#count").val();
    // console.log(price, quantity);
    getQLSanPhamByCNId(chiNhanh.id).then(productInfo => {
        productInfo.forEach(productInfor => {
            // console.log(productInfor)
            if (productInfor.id == id) {
                const newProduct = {
                    id: productInfor.maSP.id,
                    tenSanPham: productInfor.maSP.tenSanPham,
                    loaiSanPham: productInfor.maSP.loaiSanPham,
                    thuongHieu: productInfor.maSP.thuongHieu,
                    tacGia: productInfor.maSP.tacGia,
                    theLoai: productInfor.maSP.theLoai,
                    price: productInfor.giaBan,
                    quantity: quantity,
                };
                if (sessionStorage.getItem('productArrayBill')) {
                    // Lấy mảng hiện tại từ sessionStorage
                    let existingArray = JSON.parse(sessionStorage.getItem('productArrayBill'));
                    matchFound = false;
                    existingArray.forEach(element => {
                        if (newProduct.id == element.id) {
                            // chỉ thêm số lượng
                            element.quantity = parseInt(element.quantity) + parseInt(newProduct.quantity);
                            matchFound = true;
                            sessionStorage.setItem('productArrayBill', JSON.stringify(existingArray));
                        }
                    });
                    if (matchFound == false) {
                        // Thêm giá trị mới vào mảng
                        existingArray.push(newProduct);

                        // Lưu lại mảng mới vào sessionStorage
                        sessionStorage.setItem('productArrayBill', JSON.stringify(existingArray));
                    }
                } else {
                    // Nếu chưa có mảng trong sessionStorage, tạo một mảng mới và thêm giá trị vào
                    let newArray = [newProduct];
                    sessionStorage.setItem('productArrayBill', JSON.stringify(newArray));
                }
                // Hiển thị nội dung bảng sau khi thêm sản phẩm thành công
                showSessionStorage();
            }
        });


    });
}
function updateUI() {
    $("#table-body tr").remove();
    totalPrice = 0;
    $("#totalBillPricing").text("Tổng cộng: " + formatCurrency(totalPrice));
    // location.reload();
}
let totalPrice = 0;
function showSessionStorage() {
    totalPrice = 0;
    updateUI();
    sortProductArrayById();
    let storedArray = JSON.parse(sessionStorage.getItem('productArrayBill'));
    const tableBody = $("#table-body");

    if (storedArray) {
        storedArray.forEach(item => {
            // Tạo một dòng mới
            const row = $("<tr>");

            // Thêm các thông tin từ đối tượng item vào các cột
            $("<td>").text(item.id).appendTo(row);
            $("<td>").text(item.tenSanPham).appendTo(row);
            $("<td>").text(formatCurrency(item.price)).appendTo(row);
            $("<td>").text(item.quantity).appendTo(row);
            $("<td>").text(formatCurrency(item.price * item.quantity)).appendTo(row);

            // Cộng dồn giá trị cho tổng giá tiền
            totalPrice += item.price * item.quantity;

            // Thêm dòng vào bảng
            tableBody.append(row);
        });
    }
    // Hiển thị tổng cộng
    $("#totalBillPricing").text("Tổng cộng: " + formatCurrency(totalPrice));
}

/* // xóa thông tin mảng sản phẩm trong session */
function clearSessionProductArray() {
    sessionStorage.removeItem("productArrayBill");
    updateUI();
    document.getElementById("datepicker").value = "";
    checkValidDate();
};
// sắp xếp
function sortProductArrayById() {
    // Lấy mảng từ sessionStorage
    let storedArray = JSON.parse(sessionStorage.getItem('productArrayBill'));

    // Sử dụng phương thức sort() để sắp xếp mảng theo trường 'id'
    if (storedArray) {
        storedArray.sort((a, b) => a.id - b.id);
        // Lưu lại mảng đã sắp xếp vào sessionStorage
        sessionStorage.setItem('productArrayBill', JSON.stringify(storedArray));
    }
}
showSessionStorage();
function checkValidDate() {
    var input = document.getElementById("datepicker").value;
    if (input != "") {
        document.getElementById("createBill").removeAttribute('disabled');
    } else {
        document.getElementById("createBill").setAttribute('disabled', true);
    }
};
function handleBill() {
    let productArray = JSON.parse(sessionStorage.getItem('productArrayBill'));
    if (!productArray) {
        $("#liveToast").removeClass("bg-sucess");
        $("#liveToast").addClass("bg-danger");
        const toast = new bootstrap.Toast($("#liveToast"));
        $("#toast-context").text("Bạn chưa nhập bất kỳ sản phẩm nào");
        toast.show();
    }
    else {
        // const chiNhanhJSON = sessionStorage.getItem('infoChiNhanh');
        // const chiNhanh = JSON.parse(chiNhanhJSON);
        const dateImport = $('#datepicker').val();
        addBill(productArray, chiNhanh.id, dateImport);
    }
}
