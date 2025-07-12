token = sessionStorage.getItem("tokenData");

function getAllChiNhanh() {
    const apiUrl = "http://localhost:8080/chiNhanh";
    return $.ajax({
        url: apiUrl,
        type: "GET",
        dataType: "json",
        headers: {
            Authorization: "Bearer "  + token
        },
    })
        .then(response => {
            const chiNhanhArray = response.data.map(element => new ChiNhanh(element.id, element.tenChiNhanh, element.diaChi, element.taiKhoanFK));
            return chiNhanhArray;
        })
        .fail(function (xhr) {
            if(xhr.status == 403) {
                window.location.href = "../../Assects/pagesEvent/403.html"
            }
            console.error("Error while fetching products:", error);
            throw error;
        });
}
function updateChiNhanh(tenChiNhanh,diaChi,maChiNhanh) {
    $.ajax({
        url: "http://localhost:8080/chiNhanh/" + maChiNhanh,
        type: "PUT",
        headers: {
            Authorization: "Bearer "  + token
        },
        data: JSON.stringify({
            "tenChiNhanh": tenChiNhanh,
            "diaChi": diaChi
        }),
        contentType: "application/json",
        dataType: "json",
        success: function (response) {
            $("#liveToast").removeClass("bg-danger");
            $("#liveToast").addClass("bg-success");
            const toast = new bootstrap.Toast($("#liveToast"));
            $("#toast-context").text("Thay đổi thông tin chi nhánh thành công");
            loadChiNhanh("table-body");
            toast.show();
        },
        error: function (xhr, response) {
            // if(xhr.status == 403) {
            //     window.location.href = "../../Assects/pagesEvent/403.html"
            // }
            const errorMessage = xhr;
            const errorObject = JSON.parse(JSON.stringify(errorMessage));
            const errorMessageText = errorObject.message;
            $("#liveToast").removeClass("bg-success");
            $("#liveToast").addClass("bg-danger");
            const toast = new bootstrap.Toast($("#liveToast"));
            $("#toast-context").text(errorMessageText);
            toast.show();
        }
    });
}
function deleteChiNhanh(event) {
    const apiUrl = "http://localhost:8080/chiNhanh/" + event;
    $.ajax({
        url: apiUrl,
        type: "DELETE",
        dataType: "json",
        headers: {
            Authorization: "Bearer "  + token
        },
        success: function (response) {
            if (response.status == "ok") {
                const toast = new bootstrap.Toast($("#liveToast"));
                $("#toast-context").text("Xóa chi nhánh thành công");
                loadChiNhanh("table-body");
                toast.show();
            } else {
                return response.message;
            }
        },
        error: function (xhr, status, error, response) {
            if(response.status == 403) {
                window.location.href = "../../Assects/pagesEvent/403.html"
            }
            $("#message").text("Error: " + xhr.status + " - " + xhr.statusText);
        }
    });
}
function addChiNhanh(tenChiNhanh, diaChi, email) {
    $.ajax({
        url: "http://localhost:8080/chiNhanh",
        type: "POST",
        data: JSON.stringify({
            "tenChiNhanh": tenChiNhanh,
            "diaChi": diaChi,
            "email": email
        }),
        contentType: "application/json",
        dataType: "json",
        success: function (response) {
            if(response.status==="ok"){
                $("#liveToast").removeClass("bg-danger");
                $("#liveToast").addClass("bg-success");
                const toast = new bootstrap.Toast($("#liveToast"));
                $("#toast-context").text("Thêm chi nhánh mới thành công. Vui lòng xác nhận tài khoản được gửi đến trong email.");
                loadChiNhanh("table-body");
                toast.show();
            }
            else{
                $("#liveToast").removeClass("bg-success");
                $("#liveToast").addClass("bg-danger");
                const toast = new bootstrap.Toast($("#liveToast"));
                $("#toast-context").text("Tên chi nhánh đã tồn tại trong hệ thống");
                loadChiNhanh("table-body");
                toast.show();
            }
        },
        error: function (xhr) {
            if(xhr.status == 403) {
                window.location.href = "../../Assects/pagesEvent/403.html"
            }
            $("#liveToast").removeClass("bg-success");
            $("#liveToast").addClass("bg-danger");
            const toast = new bootstrap.Toast($("#liveToast"));
            $("#toast-context").text("Thêm chi nhánh mới không thành công. Tên chi nhánh đã tồn tại trong hệ thống.");
            loadChiNhanh("table-body");
            toast.show();
        }
    });
}
function changePassword(taiKhoan, matKhau,matKhauMoi) {
    $.ajax({
        url: "http://localhost:8080/taiKhoanChange",
        type: "PUT",
        data: JSON.stringify({
            "taiKhoan": taiKhoan,
            "matKhau": matKhau,
            "matKhauMoi": matKhauMoi
        }),
        contentType: "application/json",
        dataType: "json",
        headers: {
            Authorization: "Bearer "  + token
        },
        success: function (response) {
            $("#liveToast").removeClass("bg-danger");
            $("#liveToast").addClass("bg-success");
            const toast = new bootstrap.Toast($("#liveToast"));
            $("#toast-context").text("Đổi mật khẩu thành công");
            chiNhanh.taiKhoanFK.matKhau = response.data.matKhau;
            var updatedChiNhanhJSON = JSON.stringify(chiNhanh);
            sessionStorage.setItem('infoChiNhanh', updatedChiNhanhJSON);
            toast.show();
        },
        error: function (xhr) {
            if(xhr.status == 403) {
                window.location.href = "../../Assects/pagesEvent/403.html"
            }
            $("#liveToast").removeClass("bg-success");
            $("#liveToast").addClass("bg-danger");
            const toast = new bootstrap.Toast($("#liveToast"));
            $("#toast-context").text("Mật khẩu cũ không trùng khớp");
            toast.show();
        }
    });
}
function forgotPassword(email) {
    $.ajax({
        url: "http://localhost:8080/taiKhoan",
        type: "PUT",
        data: JSON.stringify({
            "taiKhoan": email,
        }),
        contentType: "application/json",
        dataType: "json",
        success: function (response) {
            $("#liveToast").removeClass("bg-danger");
            $("#liveToast").addClass("bg-success");
            const toast = new bootstrap.Toast($("#liveToast"));
            $("#toast-context").text("Mật khẩu mới đã được gửi đến email của bạn");
            toast.show();
        },
        error: function (xhr) {
            if(xhr.status == 403) {
                window.location.href = "../../Assects/pagesEvent/403.html"
            }
            $("#liveToast").removeClass("bg-success");
            $("#liveToast").addClass("bg-danger");
            const toast = new bootstrap.Toast($("#liveToast"));
            $("#toast-context").text("Đã có lỗi xảy ra hoặc không tìm thấy tài khoản");
            toast.show();
        }
    });
}

