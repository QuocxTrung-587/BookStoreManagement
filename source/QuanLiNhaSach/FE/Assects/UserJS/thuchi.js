$(document).ready(async function () {
    await loadPhieuNhap();
    await loadHoaDon();

    const refund = parseInt(sessionStorage.getItem('refundPricing'));
    const totalImportPricings = parseInt($("#totalImportPricings").val());
    const totalBillPricings = parseInt($("#totalBillPricings").val());
    const Income = parseInt(totalBillPricings) - parseInt(totalImportPricings) + parseInt(refund);
    $("#totalRefundPricing").text("Tiền hoàn hàng tồn: " + formatCurrency(refund));
    $("#totalIncomePricing").text("Tổng thu chi: " + formatCurrency(Income));
});

// load PhieuNhap
async function loadPhieuNhap() {
    const chiNhanhJSON = sessionStorage.getItem('infoChiNhanh');
    const chiNhanh = JSON.parse(chiNhanhJSON);
    const tableBody = $("#tablePhieuNhap")[0];
    let totalPricing = 0;

    const phieuNhapArray = await getAllPhieuNhapByCNId(chiNhanh.id);

    phieuNhapArray.forEach(element => {
        if (element instanceof PhieuNhap) {
            const row = document.createElement("tr");

            const idCell = document.createElement("th");
            idCell.textContent = element.id;
            row.appendChild(idCell);

            const dateImportCell = document.createElement("td");
            dateImportCell.textContent = element.ngayNhap;
            row.appendChild(dateImportCell);

            const totalCell = document.createElement("td");
            totalCell.textContent = formatCurrency(element.tongCong);
            row.appendChild(totalCell);

            totalPricing += parseInt(element.tongCong);
            tableBody.appendChild(row);
        }
    });

    $("#totalImportPricing").text(formatCurrency(totalPricing));
    $("#totalImportPricings").val(totalPricing);
}

// load HoaDon
async function loadHoaDon() {
    const chiNhanhJSON = sessionStorage.getItem('infoChiNhanh');
    const chiNhanh = JSON.parse(chiNhanhJSON);
    const tableBody = $("#tableHoaDon")[0];
    let totalPricing = 0;

    const hoaDonArray = await getAllHoaDonByCNId(chiNhanh.id);

    hoaDonArray.forEach(element => {
        if (element instanceof HoaDon) {
            const row = document.createElement("tr");

            const idCell = document.createElement("th");
            idCell.textContent = element.id;
            row.appendChild(idCell);

            const dateImportCell = document.createElement("td");
            dateImportCell.textContent = element.ngayNhap;
            row.appendChild(dateImportCell);

            const totalCell = document.createElement("td");
            totalCell.textContent = formatCurrency(element.tongCong);
            row.appendChild(totalCell);

            totalPricing += parseInt(element.tongCong);
            tableBody.appendChild(row);
        }
    });

    $("#totalBillPricing").text(formatCurrency(totalPricing));
    $("#totalBillPricings").val(totalPricing);
}
