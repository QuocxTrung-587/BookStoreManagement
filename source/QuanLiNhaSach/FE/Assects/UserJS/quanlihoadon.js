$(function () {
    loadHoaDon();
})
function updateUIHoaDon() {
    $("#table-body tr").remove()
}
// load PhieuNhap
function loadHoaDon() {
    updateUIHoaDon();
    const chiNhanhJSON = sessionStorage.getItem('infoChiNhanh');
    const chiNhanh = JSON.parse(chiNhanhJSON);
    const tableBody = $("#table-body");
    let totalPricing = 0;

    getAllHoaDonByCNId(chiNhanh.id).then(function (hoaDonArray) {
        hoaDonArray.forEach(element => {
            if (element instanceof HoaDon) {
                const buttonsHtml = `
                    <button type="button" value="1" class="btn btn-primary" data-bs-toggle="modal" data-bs-target="#exampleModal3" onclick="getChiTietHoaDon(this)">
                        <i class="bi bi-arrow-repeat"></i>&nbspSửa
                    </button>
                    <button type="button" class="btn btn-danger" data-bs-toggle="modal" data-bs-target="#exampleModal" onclick="openDelHoaDon(this)">
                        <i class="bi bi-dash-circle-dotted"></i>&nbspXóa
                    </button>
                    <button type="button" value="2" class="btn btn-success" data-bs-toggle="modal" data-bs-target="#exampleModal2" onclick="getChiTietHoaDon(this)">
                        <i class="bi bi-card-text"></i>&nbspChi tiết
                    </button>`;

                const row = $("<tr>")
                    .append($("<th>").text(element.id))
                    .append($("<td>").text(element.ngayNhap))
                    .append($("<td>").text(formatCurrency(element.tongCong)))
                    .append($("<td>").html(buttonsHtml));

                tableBody.append(row);

                totalPricing += parseInt(element.tongCong);
            }
        });

        $("#totalBillPricing").text("Tổng cộng: " + formatCurrency(totalPricing));
        // Sorting sau khi dữ liệu từ Ajax được tải về
        eventSortingTable();
    });
}


function updateUIModal() {
    $(".chiTietHoaDonTable tr").remove()
}
async function getChiTietHoaDon(button) {
    updateUIModal();
    const hoaDonId = $(button).closest("tr").find("th:first").text();
    // console.log(phieuNhapId);

    try {
        const hoaDonArray = await getAllHoaDonByCNId(chiNhanh.id);
        const tableChiTiet = $(".chiTietHoaDonTable");

        let totalImportDetailPricing = 0;

        hoaDonArray.forEach(element => {
            if (element instanceof ChiTietHoaDon && element.hoaDon.id == hoaDonId) {
                // Chi tiết phiếu nhập
                let sanPham = element.sanPham;

                const row = $("<tr>");

                $("<td>").text(sanPham.id).appendTo(row);
                $("<td>").text(sanPham.tenSanPham).appendTo(row);
                if (button.value == 1) {
                    const inputSoLuong = $("<input>").addClass("form-control w-75").val(element.soLuong).attr("maxlength", 10);
                    const inputGiaBan = $("<input>").addClass("form-control w-75").val(element.giaBan).attr("maxlength", 10);

                    $("<td>").append(inputSoLuong).appendTo(row);
                    $("<td>").append(inputGiaBan).appendTo(row);
                }
                else {
                    // const inputSoLuong = $("<input>").addClass("form-control w-50").val(element.soLuong).attr("maxlength", 10);
                    // const inputGiaNhap = $("<input>").addClass("form-control w-50").val(element.giaNhap).attr("maxlength", 10);

                    $("<td>").append(element.soLuong).appendTo(row);
                    $("<td>").append(formatCurrency(element.giaBan)).appendTo(row);
                }
                $("<td>").text(formatCurrency(element.tongTien)).appendTo(row);

                if (button.value == 1) {
                    const buttonsCell = $("<td>").html(`<button type="button" class="btn btn-outline-danger"> <i class="bi bi-dash-circle-dotted"></i>&nbspXóa</button>`);
                    row.append(buttonsCell);
                }
                totalImportDetailPricing += parseInt(element.tongTien);

                tableChiTiet.append(row);
                $(".totalBillDetailPricing").text(formatCurrency(totalImportDetailPricing));
            }
        });
        if (button.value == 1) {
            const firstHoaDon = hoaDonArray.find(element => element instanceof HoaDon && element.id == hoaDonId);
            if (hoaDonArray) {
                $(".importDate").val(firstHoaDon.ngayNhap);
            }
        }
        else {
            const firstHoaDon = hoaDonArray.find(element => element instanceof HoaDon && element.id == hoaDonId);
            $(".importDate").text(firstHoaDon.ngayNhap);
        }
    } catch (error) {
        console.error("Error while fetching products:", error);
        throw error;
    }
}
function openDelHoaDon(button) {
    const hoaDonId = $(button).closest("tr").find("th:first").text();
    // console.log(phieuNhapId);
    $("#confirmBillDel").text("Xác nhận xóa hóa đơn có mã " + hoaDonId + "?");
    $("#idHoaDon").val(hoaDonId);
}
function getHoaDonsByNL() {
    const ngayLap = $("#key").val();
    // console.log(ngayLap)
    if(ngayLap == ""){
        loadHoaDon()
    }
    else{
        updateUIHoaDon();
        const chiNhanhJSON = sessionStorage.getItem('infoChiNhanh');
        const chiNhanh = JSON.parse(chiNhanhJSON);
        const tableBody = $("#table-body");
        let totalPricing = 0;
        getHoaDonsByNgayLap(ngayLap).then(function (hoaDonArray) {
            if(hoaDonArray.length==0){
                const row = $("<tr>").append($("<th colspan=4>").text("Không có hóa đơn nào."))
                tableBody.append(row);
                $("#totalBillPricing").text("Tổng cộng: " + formatCurrency(0));
            }
            else{
                hoaDonArray.forEach(hoaDon => {
                    if (hoaDon.chiNhanh.id == chiNhanh.id) {
                        const buttonsHtml = `
                            <button type="button" value="1" class="btn btn-primary" data-bs-toggle="modal" data-bs-target="#exampleModal3" onclick="getChiTietHoaDon(this)">
                                <i class="bi bi-arrow-repeat"></i>&nbspSửa
                            </button>
                            <button type="button" class="btn btn-danger" data-bs-toggle="modal" data-bs-target="#exampleModal" onclick="openDelHoaDon(this)">
                                <i class="bi bi-dash-circle-dotted"></i>&nbspXóa
                            </button>
                            <button type="button" value="2" class="btn btn-success" data-bs-toggle="modal" data-bs-target="#exampleModal2" onclick="getChiTietHoaDon(this)">
                                <i class="bi bi-card-text"></i>&nbspChi tiết
                            </button>`;
    
                        const row = $("<tr>")
                            .append($("<th>").text(hoaDon.id))
                            .append($("<td>").text(hoaDon.ngayNhap))
                            .append($("<td>").text(formatCurrency(hoaDon.tongCong)))
                            .append($("<td>").html(buttonsHtml));
    
                        tableBody.append(row);
                        totalPricing += parseInt(hoaDon.tongCong);
                    }
                });
                $("#totalBillPricing").text("Tổng cộng: " + formatCurrency(totalPricing));
            }
        });
    }
}
