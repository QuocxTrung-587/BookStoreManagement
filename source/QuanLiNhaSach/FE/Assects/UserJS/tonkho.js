$(document).ready(function name() {
    loadHangHoa();
})
function updateUI() {
    $("#table-body tr").remove();
}
async function loadHangHoa() {
    updateUI();
    let numbooks = 0;
    let refund = 0;
    const QLSanPhamArray = await getQLSanPhamByCNId(chiNhanh.id);

    // Tạo một mảng tạm thời để lưu các hàng (rows)
    const rowsArray = [];

    for (const QLsanPham of QLSanPhamArray) {
        if (QLsanPham.trangThai == 0) {
            const row = $("<tr>");
            const SPInfo = QLsanPham.maSP;
            $("<th>").text(SPInfo.id).appendTo(row);
            $("<td>").text(SPInfo.tenSanPham).appendTo(row);
            $("<td>").text(SPInfo.loaiSanPham).appendTo(row);
            $("<td>").text(SPInfo.thuongHieu).appendTo(row);
            $("<td>").text(SPInfo.tacGia).appendTo(row);
            $("<td>").text(SPInfo.theLoai).appendTo(row);
            // console.log(SPInfo.id)
            await getPricingImport(chiNhanh.id, SPInfo.id).then(SPPrice => {
                $("<td>").append(formatCurrency(SPPrice)).appendTo(row);
                refund = refund +  parseInt(QLsanPham.tongHang)*parseInt(SPPrice);
            })
            $("<td>").text(QLsanPham.tongHang).appendTo(row);
            $("<td>").text(QLsanPham.hanTon).appendTo(row);
            $("<td>").text("Trả hàng").appendTo(row).addClass("text-danger fw-bold");

            rowsArray.push(row);

            numbooks += QLsanPham.tongHang;
        }
    }

    rowsArray.sort((row1, row2) => {
        const id1 = parseInt($(row1).find("th").text());
        const id2 = parseInt($(row2).find("th").text());
        return id1 - id2;
    });

    const tableBody = $("#table-body");
    rowsArray.forEach((row) => {
        tableBody.append(row);
    });

    $("#numBooksReturn").text("Số lượng sách hoàn trả: " + numbooks);
    $("#refundPricing").text("Số tiền hoàn lại: " + formatCurrency(refund));
}

function checkInputEmpty() {
    if($("#key").val() == ""){
        loadHangHoa();
    }
    else{
        getQLSPsBy();
    }
}
async function getQLSPsBy() {
    let key = $("#key").val();
    $("#table-body tr").remove();
    const QLSanPhamArray = await findQLSanPhamsBy(key,chiNhanh.id);
    console.log(QLSanPhamArray);
    // Tạo một mảng tạm thời để lưu các hàng (rows)
    const rowsArray = [];

    for (const QLsanPham of QLSanPhamArray) {
        if (QLsanPham.trangThai == 0) {
            const row = $("<tr>");
            const SPInfo = QLsanPham.maSP;
            $("<th>").text(SPInfo.id).appendTo(row);
            $("<td>").text(SPInfo.tenSanPham).appendTo(row);
            $("<td>").text(SPInfo.loaiSanPham).appendTo(row);
            $("<td>").text(SPInfo.thuongHieu).appendTo(row);
            $("<td>").text(SPInfo.tacGia).appendTo(row);
            $("<td>").text(SPInfo.theLoai).appendTo(row);
            // console.log(SPInfo.id)
            await getPricingImport(chiNhanh.id, SPInfo.id).then(SPPrice => {
                $("<td>").append(formatCurrency(SPPrice)).appendTo(row);
                // refund = refund +  parseInt(QLsanPham.tongHang)*parseInt(SPPrice);
            })
            $("<td>").text(QLsanPham.tongHang).appendTo(row);
            $("<td>").text(QLsanPham.hanTon).appendTo(row);
            $("<td>").text("Trả hàng").appendTo(row).addClass("text-danger fw-bold");

            rowsArray.push(row);

            // numbooks += QLsanPham.tongHang;
        }
    }

    rowsArray.sort((row1, row2) => {
        const id1 = parseInt($(row1).find("th").text());
        const id2 = parseInt($(row2).find("th").text());
        return id1 - id2;
    });

    const tableBody = $("#table-body");
    rowsArray.forEach((row) => {
        tableBody.append(row);
    });
}
