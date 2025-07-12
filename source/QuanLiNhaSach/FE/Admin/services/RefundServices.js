// tính refund và lưu vào session'
async function calculateAndSaveRefund() {
    let refund = 0;
    const QLSanPhamArray = await getQLSanPhamByCNId(chiNhanh.id);
    for (const QLsanPham of QLSanPhamArray) {
        if (QLsanPham.trangThai == 0) {
            const SPInfo = QLsanPham.maSP;
            const SPPrice = await getPricingImport(chiNhanh.id, SPInfo.id);
            refund = refund + parseInt(QLsanPham.tongHang) * parseInt(SPPrice);
        }
    }
    sessionStorage.setItem('refundPricing', refund);
}

$(function () {
    calculateAndSaveRefund();
})