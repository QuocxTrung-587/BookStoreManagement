token = sessionStorage.getItem("tokenData");
function getQLSanPhamByCNId(chiNhanhId) {
    const apiUrl = "http://localhost:8080/quanLySPByCN/" + chiNhanhId;
    return $.ajax({
        url: apiUrl,
        type: "GET",
        dataType: "json",
        headers: {
            Authorization: "Bearer " + token
        },
    })
        .then(response => {
            const QLsanPhamArray = response.data.map(element => new QLSanPham(element.id, element.hanTon, element.giaBan, element.trenKe, element.trongKho, element.tongHang, element.trangThai, element.maSP, element.maCN));
            // console.log(sanPhamArray)
            return QLsanPhamArray;
        })
        .fail(function (xhr, status, error) {
            if (xhr.status == 403) {
                window.location.href = "../../Assects/pagesEvent/403.html"
            }
            console.error("Error while fetching products:", error);
            throw error;
        });
}
function moveSP(id, soLuong, dk) {
    const apiUrl = "http://localhost:8080/quanLySP/moveSP/" + id;
    // chuyển sách vô trong kho
    if (dk === 0) {
        $.ajax({
            url: apiUrl,
            type: "PUT",
            contentType: "application/json",
            dataType: "json",
            headers: {
                Authorization: "Bearer " + token
            },
            data: JSON.stringify({
                "trongKho": soLuong
            }),
            success: function (response) {
                $("#liveToast").removeClass("bg-danger");
                $("#liveToast").addClass("bg-success");
                const toast = new bootstrap.Toast($("#liveToast"));
                $("#toast-context").text("Cập nhật số lượng thành công");
                loadHangHoa();
                toast.show();
            },
            error: function (xhr, status, error) {
                if (xhr.status == 403) {
                    window.location.href = "../../Assects/pagesEvent/403.html"
                }
                const errorMessage = xhr.responseText;
                const errorObject = JSON.parse(errorMessage);
                const errorMessageText = errorObject.message;
                $("#liveToast").removeClass("bg-success");
                $("#liveToast").addClass("bg-danger");
                const toast = new bootstrap.Toast($("#liveToast"));
                // $("#toast-context").text("Error: " + errorMessageText);
                $("#toast-context").text("Error: Số lượng đã có sẵn trên kệ");
                loadHangHoa();
                toast.show();
            }
        })
    }
    else if (dk == 1) {
        $.ajax({
            url: apiUrl,
            type: "PUT",
            contentType: "application/json",
            dataType: "json",
            headers: {
                Authorization: "Bearer " + token
            },
            data: JSON.stringify({
                trenKe: soLuong
            }),
            success: function (response) {
                $("#liveToast").removeClass("bg-danger");
                $("#liveToast").addClass("bg-success");
                const toast = new bootstrap.Toast($("#liveToast"));
                $("#toast-context").text("Cập nhật số lượng thành công");
                loadHangHoa();
                toast.show();
            },
            error: function (xhr, status, error) {
                if (xhr.status == 403) {
                    window.location.href = "../../Assects/pagesEvent/403.html"
                }
                const errorMessage = xhr.responseText;
                const errorObject = JSON.parse(errorMessage);
                const errorMessageText = errorObject.message;
                $("#liveToast").removeClass("bg-success");
                $("#liveToast").addClass("bg-danger");
                const toast = new bootstrap.Toast($("#liveToast"));
                // $("#toast-context").text("Error: " + errorMessageText);
                $("#toast-context").text("Error: " + "Số lượng đã có sẵn trên kệ hoặc quá số lượng có trong kho");
                loadHangHoa();
                toast.show();
            }
        })
    }
}

function updatePrice(QLSPId, giaBanMoi) {
    $.ajax({
        url: "http://localhost:8080/quanLySP/" + QLSPId,
        type: "PUT",
        contentType: "application/json",
        dataType: "json",
        headers: {
            Authorization: "Bearer " + token
        },
        data: JSON.stringify({
            "giaBan": giaBanMoi
        }),
        success: function (response) {
            $("#liveToast").removeClass("bg-danger");
            $("#liveToast").addClass("bg-success");
            const toast = new bootstrap.Toast($("#liveToast"));
            $("#toast-context").text("Cập nhật giá bán thành công");
            loadHangHoa();
            toast.show();
        },
        error: function (response) {
            if (response.status == 403) {
                window.location.href = "../../Assects/pagesEvent/403.html"
            }
            $("#liveToast").removeClass("bg-success");
            $("#liveToast").addClass("bg-danger");
            const toast = new bootstrap.Toast($("#liveToast"));
            $("#toast-context").text("Cập nhật giá bán không thành công");
            loadHangHoa();
            toast.show();
        }
    })
};
async function findQLSanPhamsBy(key, chiNhanhId) {
    const apiUrl = "http://localhost:8080/sanPhamQuery/" + key;
    try {
        const response = await $.ajax({
            url: apiUrl,
            type: "GET",
            dataType: "json",
            headers: {
                Authorization: "Bearer " + token
            },
        });
        const sanPhamArray = response.data.map(element => new SanPham(element.id, element.tenSanPham, element.loaiSanPham, element.thuongHieu, element.tacGia, element.theLoai));
        const QLSanPhamArray = [];
        for (const sanPham of sanPhamArray) {
            const QLSP = await getQLSanPhamByCNId(chiNhanhId);
            const matchingQLSP = QLSP.find(QLSPArray => QLSPArray.maSP.id === sanPham.id);
            if (matchingQLSP) {
                const QLSanPhamInstance = new QLSanPham(matchingQLSP.id, matchingQLSP.hanTon, matchingQLSP.giaBan, matchingQLSP.trenKe, matchingQLSP.trongKho, matchingQLSP.tongHang, matchingQLSP.trangThai, matchingQLSP.maSP, matchingQLSP.maCN);
                QLSanPhamArray.push(QLSanPhamInstance);
            }
        }
        return QLSanPhamArray;
    } catch (response) {
        if (response.status == 403) {
            window.location.href = "../../Assects/pagesEvent/403.html"
        }
        console.error("Error while fetching products:", error);
        throw error;
    }
}
