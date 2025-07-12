$(document).ready(function () {
    loadThongTin();
    $("#chiNhanhID").val(chiNhanh.id);
})
function openEditModal() {
    $("#chiNhanhNameEdit").val(chiNhanh.tenChiNhanh);
    $("#chiNhanhAddressEdit").val(chiNhanh.diaChi);
}
function loadChiNhanh() {
    chiNhanh.tenChiNhanh =  $("#chiNhanhNameEdit").val();
    chiNhanh.diaChi =  $("#chiNhanhAddressEdit").val();
    var updatedChiNhanhJSON = JSON.stringify(chiNhanh);
    sessionStorage.setItem('infoChiNhanh', updatedChiNhanhJSON);
    setTimeout(() => {
        location.reload();
    }, 3000);
    loadThongTin();
}
function loadThongTin() {
    $("#chiNhanhName").text(chiNhanh.tenChiNhanh);  
    $("#chiNhanhAddress").text(chiNhanh.diaChi);
    $("#chiNhanhAccount").text(chiNhanh.taiKhoanFK.taiKhoan);
    $("#chiNhanhPassword").text("******");
}
function openChangePsdModal() {
    $("#oldPassword").val(""); 
    $("#newPassword").val("");
    $("#confirmNewPassword").val(""); 
}

function checkPassword() {
    const taiKhoan = chiNhanh.taiKhoanFK.taiKhoan;
    const oldPsd = $("#oldPassword").val();
    const newPsd = $("#newPassword").val();
    const confirmNewPsd = $("#confirmNewPassword").val();
    
        if(newPsd === confirmNewPsd){
            changePassword(taiKhoan,oldPsd,newPsd);
        }
        else{
            $("#liveToast").removeClass("bg-success");
            $("#liveToast").addClass("bg-danger");
            const toast = new bootstrap.Toast($("#liveToast"));
            $("#toast-context").text("Mật khẩu xác nhận không trùng khớp");
            toast.show();
        }
    }

