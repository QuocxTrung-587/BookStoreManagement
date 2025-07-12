$(document).ready(function () {
    loadPhieuNhap();
    $("#totalImportPricing").text("Tổng cộng: " + formatCurrency(0));
})
function updateUIPhieuNhap() {
    $("#table-body tr").remove()
}
// load PhieuNhap
function loadPhieuNhap() {
    updateUIPhieuNhap();
    const chiNhanhJSON = sessionStorage.getItem('infoChiNhanh');
    const chiNhanh = JSON.parse(chiNhanhJSON);
    const tableBody = $("#table-body");
    let totalPricing = 0;

    getAllPhieuNhapByCNId(chiNhanh.id).then(function (phieuNhapArray) {
        phieuNhapArray.forEach(element => {
            if (element instanceof PhieuNhap) {
                const buttonsHtml = `
                    <button type="button" value="1" class="btn btn-primary" data-bs-toggle="modal" data-bs-target="#exampleModal3" onclick="getChiTietPhieuNhap(this)">
                        <i class="bi bi-arrow-repeat"></i>&nbspSửa
                    </button>
                    <button type="button" class="btn btn-danger" data-bs-toggle="modal" data-bs-target="#exampleModal" onclick="openDelPhieuNhap(this)">
                        <i class="bi bi-dash-circle-dotted"></i>&nbspXóa
                    </button>
                    <button type="button" value="2" class="btn btn-success" data-bs-toggle="modal" data-bs-target="#exampleModal2" onclick="getChiTietPhieuNhap(this)">
                        <i class="bi bi-card-text"></i>&nbspChi tiết
                    </button>`;
                const row = $("<tr>")
                    .append($("<td>").text(element.id))
                    .append($("<td>").text(element.ngayNhap))
                    .append($("<td>").text(formatCurrency(element.tongCong)))
                    .append($("<td>").html(buttonsHtml));

                tableBody.append(row);

                totalPricing += parseInt(element.tongCong);
            }
        });

        $("#totalImportPricing").text("Tổng cộng: " + formatCurrency(totalPricing));
        // Sorting sau khi dữ liệu từ Ajax được tải về
        eventSortingTable();
    });
}

function updateUIModal() {
    $(".chiTietPhieuNhapTable tr").remove()
}
async function getChiTietPhieuNhap(button) {
    updateUIModal();
    const phieuNhapId = $(button).closest("tr").find("td:first").text();
    try {
        const phieuNhapArray = await getAllPhieuNhapByCNId(chiNhanh.id);
        const tableChiTiet = $(".chiTietPhieuNhapTable");
        let totalImportDetailPricing = 0;
        phieuNhapArray.forEach(element => {
            if (element instanceof ChiTietPhieuNhap && element.phieuNhap.id == phieuNhapId) {
                // Chi tiết phiếu nhập
                let sanPham = element.sanPham;
                const row = $("<tr>");
                $("<td>").text(sanPham.id).appendTo(row);
                $("<td>").text(sanPham.tenSanPham).appendTo(row);
                if (button.value == 1) {
                    const inputSoLuong = $("<input>").addClass("form-control w-75").val(element.soLuong).attr("maxlength", 10);
                    const inputGiaNhap = $("<input>").addClass("form-control w-75").val(element.giaNhap).attr("maxlength", 10);
                    $("<td>").append(inputSoLuong).appendTo(row);
                    $("<td>").append(inputGiaNhap).appendTo(row);
                }
                else {
                    $("<td>").append(element.soLuong).appendTo(row);
                    $("<td>").append(formatCurrency(element.giaNhap)).appendTo(row);
                }
                $("<td>").text(formatCurrency(element.tongTien)).appendTo(row);
                if (button.value == 1) {
                    const buttonsCell = $("<td>").html(`<button type="button" class="btn btn-outline-danger"> <i class="bi bi-dash-circle-dotted"></i>&nbspXóa</button>`);
                    row.append(buttonsCell);
                }
                totalImportDetailPricing += parseInt(element.tongTien);
                tableChiTiet.append(row);
                $(".totalImportDetailPricing").text(formatCurrency(totalImportDetailPricing));
            }
        });
        if (button.value == 1) {
            const firstPhieuNhap = phieuNhapArray.find(element => element instanceof PhieuNhap && element.id == phieuNhapId);
            if (firstPhieuNhap) {
                $(".importDate").val(firstPhieuNhap.ngayNhap);
            }
        }
        else {
            const firstPhieuNhap = phieuNhapArray.find(element => element instanceof PhieuNhap && element.id == phieuNhapId);
            $(".importDate").text(firstPhieuNhap.ngayNhap);
        }
    } catch (error) {
        console.error("Error while fetching products:", error);
        throw error;
    }
}
function openDelPhieuNhap(button) {
    const phieuNhapId = $(button).closest("tr").find("td:first").text();
    console.log(phieuNhapId);
    $("#confirmImportDel").text("Xác nhận xóa phiếu nhập có mã " + phieuNhapId + "?");
    $("#idPhieuNhap").val(phieuNhapId);
}

function getPhieuNhapsByNN() {
    const ngayNhap = $("#key").val();
    console.log(ngayNhap)
    if (!ngayNhap) {
        loadPhieuNhap()
    }
    else {
        updateUIPhieuNhap();
        const chiNhanhJSON = sessionStorage.getItem('infoChiNhanh');
        const chiNhanh = JSON.parse(chiNhanhJSON);
        const tableBody = $("#table-body");
        let totalPricing = 0;
        getPhieuNhapsByNgayNhap(ngayNhap).then(function (phieuNhapArray) {
            if (phieuNhapArray.length == 0) {
                const row = $("<tr>")
                    .append($("<th colspan=4>").text("Không có phiếu nhập nào."))
                tableBody.append(row);
                $("#totalImportPricing").text("Tổng cộng: " + formatCurrency(0));
            }
            else {
                phieuNhapArray.forEach(phieuNhap => {
                    if (phieuNhap.chiNhanh.id == chiNhanh.id) {
                        const buttonsHtml = `
                            <button type="button" value="1" class="btn btn-primary" data-bs-toggle="modal" data-bs-target="#exampleModal3" onclick="getChiTietPhieuNhap(this)">
                                <i class="bi bi-arrow-repeat"></i>&nbspSửa
                            </button>
                            <button type="button" class="btn btn-danger" data-bs-toggle="modal" data-bs-target="#exampleModal" onclick="openDelPhieuNhap(this)">
                                <i class="bi bi-dash-circle-dotted"></i>&nbspXóa
                            </button>
                            <button type="button" value="2" class="btn btn-success" data-bs-toggle="modal" data-bs-target="#exampleModal2" onclick="getChiTietPhieuNhap(this)">
                                <i class="bi bi-card-text"></i>&nbspChi tiết
                            </button>`;
                        const row = $("<tr>")
                            .append($("<td>").text(phieuNhap.id))
                            .append($("<td>").text(phieuNhap.ngayNhap))
                            .append($("<td>").text(formatCurrency(phieuNhap.tongCong)))
                            .append($("<td>").html(buttonsHtml));

                        tableBody.append(row);
                        totalPricing += parseInt(phieuNhap.tongCong);
                    }
                });
                $("#totalImportPricing").text("Tổng cộng: " + formatCurrency(totalPricing));
            }
        });
    }
}

