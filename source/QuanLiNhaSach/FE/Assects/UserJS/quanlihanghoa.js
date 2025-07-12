$(document).ready(function name() {
    loadHangHoa();
})
function updateUI() {
    $("#table-body tr").remove();
    $("#listProduct1 option:not(:first-child)").remove();
    $("#listProduct2 option:not(:first-child)").remove();
    // loadHangHoa();
    // location.reload();
}


async function loadHangHoa() {
    updateUI();
    const QLSanPhamArray = await getQLSanPhamByCNId(chiNhanh.id);
    // console.log(QLSanPhamArray)
    const tableBody = $("#table-body");

    for (const QLsanPham of QLSanPhamArray) {
        if (QLsanPham.tongHang != 0 && QLsanPham.trangThai==1) {
            const row = $("<tr>");
            const SPInfo = QLsanPham.maSP;
            $("<th>").text(SPInfo.id).appendTo(row);
            // const SPInfo = await getSanPhamById(sanPham.maSP);
            $("<td>").text(SPInfo.tenSanPham).appendTo(row);
            $("<td>").text(SPInfo.loaiSanPham).appendTo(row);
            $("<td>").text(SPInfo.thuongHieu).appendTo(row);
            $("<td>").text(SPInfo.tacGia).appendTo(row);
            $("<td>").text(SPInfo.theLoai).appendTo(row);
            // console.log(SPInfo.id)
            await getPricingImport(chiNhanh.id, SPInfo.id).then(SPPrice => {
                if(SPPrice){
                    $("<td>").append(formatCurrency(SPPrice)).appendTo(row);
                }
                else{
                    $("<td>").append(formatCurrency(QLsanPham.giaBan/3*2)).appendTo(row);
                }
            })
            $("<td>").text(formatCurrency(QLsanPham.giaBan)).appendTo(row);
            $("<td>").text(QLsanPham.trenKe).appendTo(row);
            $("<td>").text(QLsanPham.trongKho).appendTo(row);
            $("<td>").text(QLsanPham.tongHang).appendTo(row);
            $("<td>").text(QLsanPham.hanTon).appendTo(row);
            if (QLsanPham.trangThai == 1) {
                $("<td>").text("Đang bán").appendTo(row).addClass("text-success fw-bold");
            }
            else {
                $("<td>").text("Trả hàng").appendTo(row).addClass("text-danger fw-bold");
            }
            tableBody.append(row);

            // Load thông tin lên modal
            const selection1 = $("#listProduct1");
            const selection2 = $("#listProduct2");

            const opt1 = document.createElement('option');
            // const quantity = $("#count");
            opt1.value = SPInfo.id;
            opt1.setAttribute("qlspid", QLsanPham.id);
            opt1.textContent = SPInfo.tenSanPham;
            //quantity.value = sanPham.trenKe;
            selection1.append(opt1);

            const opt2 = document.createElement('option');
            opt2.value = SPInfo.id;
            opt2.setAttribute("qlspid", QLsanPham.id);
            opt2.textContent = SPInfo.tenSanPham;
            selection2.append(opt2);
        }
          // Sorting sau khi dữ liệu từ Ajax được tải về
          eventSortingTable();
    }
}
async function checkSelect1() {
    var select = document.getElementById("listProduct1");
    var quantity = document.getElementById("count");
    var soLuongTrenKe = document.getElementById("soLuongTrenKe");
    const SanPhamArray = await getQLSanPhamByCNId(chiNhanh.id);
    SanPhamArray.forEach(element => {
        if (element.maSP.id == select.value) {
            quantity.value = element.trenKe;
            quantity.setAttribute('max', element.tongHang);
            quantity.setAttribute('min', '0');
            soLuongTrenKe.value = element.trenKe;
        }
    });
};

async function checkSelect2() {
    var select = document.getElementById("listProduct2");
    var pricing = document.getElementById("up-price");
    const SanPhamArray = await getQLSanPhamByCNId(chiNhanh.id);
    SanPhamArray.forEach(element => {
        if (element.maSP.id == select.value) {
            pricing.value = element.giaBan;
            pricing.setAttribute('min', '0');
        }
    });
};

async function handleMove() {
    var select = $("#listProduct1 option:selected");
    var quantity = document.getElementById("count");
    const qlspidValue = select.attr("qlspid");
    soLuongTrenKe = document.getElementById("soLuongTrenKe").value;
    if (parseInt(quantity.value) > parseInt(soLuongTrenKe)) {
        moveSP(qlspidValue, (quantity.value) - parseInt(soLuongTrenKe), 1);
    } else {
        moveSP(qlspidValue, parseInt(soLuongTrenKe) - parseInt(quantity.value), 0);
    }
};

function handleUpdatePrice() {
    var select = $("#listProduct2 option:selected");
    const qlspidValue = select.attr("qlspid");
    const giaBan = $("#up-price").val();
    updatePrice(qlspidValue, giaBan);
}

// findQLSanPhamsBy("đồng hồ", chiNhanh.id).then(QLSP =>{
//     console.log(QLSP)
// })
// testRes.forEach(element => {
//     console.log(element)
// });
function checkInputEmpty() {
    if ($("#key").val() == "") {
        loadHangHoa();
    }
    else {
        getQLSPsBy();
    }

}
async function getQLSPsBy() {
    let key = $("#key").val();
    $("#table-body tr").remove();
    const QLSanPhamArray = await findQLSanPhamsBy(key, chiNhanh.id);
    console.log(QLSanPhamArray);
    const tableBody = $("#table-body");

    for (const QLsanPham of QLSanPhamArray) {
        console.log(QLsanPham)
        if (QLsanPham.tongHang != 0 && QLsanPham.trangThai != 0) {
            const row = $("<tr>");
            const SPInfo = QLsanPham.maSP;
            $("<th>").text(SPInfo.id).appendTo(row);
            // const SPInfo = await getSanPhamById(sanPham.maSP);
            $("<td>").text(SPInfo.tenSanPham).appendTo(row);
            $("<td>").text(SPInfo.loaiSanPham).appendTo(row);
            $("<td>").text(SPInfo.thuongHieu).appendTo(row);
            $("<td>").text(SPInfo.tacGia).appendTo(row);
            $("<td>").text(SPInfo.theLoai).appendTo(row);
            // console.log(SPInfo.id)
            await getPricingImport(chiNhanh.id, SPInfo.id).then(SPPrice => {
                $("<td>").append(formatCurrency(SPPrice)).appendTo(row);
            })
            $("<td>").text(formatCurrency(QLsanPham.giaBan)).appendTo(row);
            $("<td>").text(QLsanPham.trenKe).appendTo(row);
            $("<td>").text(QLsanPham.trongKho).appendTo(row);
            $("<td>").text(QLsanPham.tongHang).appendTo(row);
            $("<td>").text(QLsanPham.hanTon).appendTo(row);
            if (QLsanPham.trangThai == 1) {
                $("<td>").text("Đang bán").appendTo(row).addClass("text-success fw-bold");
            }
            else {
                $("<td>").text("Trả hàng").appendTo(row).addClass("text-danger fw-bold");
            }
            tableBody.append(row);
        }
    }
}