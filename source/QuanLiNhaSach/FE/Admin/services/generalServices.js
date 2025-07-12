// Hàm chuyển đổi giá tiền sang định dạng VNĐ
function formatCurrency(value) {
    // Sử dụng toLocaleString() với ngôn ngữ 'vi-VN' (Tiếng Việt, Việt Nam)
    // và các tùy chọn định dạng tiền tệ (style: 'currency', currency: 'VND')
    if(value){
        return value.toLocaleString('vi-VN', { style: 'currency', currency: 'VND' });
    }
    else{
        value = 0;
        return value.toLocaleString('vi-VN', { style: 'currency', currency: 'VND' });
    }
    
}
// Lấy thông tin chi nhánh
var chiNhanhJSON = sessionStorage.getItem('infoChiNhanh');
var chiNhanh = JSON.parse(chiNhanhJSON);
