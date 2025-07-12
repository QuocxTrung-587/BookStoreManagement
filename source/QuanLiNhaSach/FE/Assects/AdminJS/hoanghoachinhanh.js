loadHangHoaChiNhanh("table-body");

// function updateUI() {
//   $("#table-body tr").remove();
// }

function loadHangHoaChiNhanh(idTable) {
  // updateUI();
  const tableContainer = document.getElementById("card-container");
  tableContainer.innerHTML = "";
  // Lấy thông tin của mỗi chi nhánh và phiếu nhập tương ứng

  getAllChiNhanh()
    .then(async (chiNhanhArray) => {
      for (const chiNhanh of chiNhanhArray) {
        if(chiNhanh.taiKhoanFK.taiKhoan != "this.is.manager.fahasa@gmail.com"){

          // console.log("Thông tin chi nhánh:", chiNhanh);

        // Tạo một card mới cho mỗi chi nhánh
        const card = document.createElement("section");
        card.classList.add("table__body");
        const cardHeader = document.createElement("div");
        cardHeader.classList.add(
          "card-header",
          "d-flex",
          "justify-content-between"
        );
        const cardTitle = document.createElement("div");
        cardTitle.textContent = chiNhanh.tenChiNhanh;
        cardHeader.appendChild(cardTitle);
        card.appendChild(cardHeader);

        // Tạo bảng cho danh sách phiếu nhập
        const cardBody = document.createElement("div");
        cardBody.classList.add("card-body");
        const table = document.createElement("table");
        table.classList.add("table");
        const tableHeader = document.createElement("thead");
        const headerRow = document.createElement("tr");
        const headers = [
          "Mã sản phẩm",
          "Tên sản phẩm",
          "Thể loại",
          "Tác giả",
          "Nhà xuất bản",
          "Trên kệ",
          "Trong kho",
          "Tổng hàng",
        ];
        headers.forEach((headerText) => {
          const headerCell = document.createElement("th");
          headerCell.textContent = headerText;
          headerRow.appendChild(headerCell);
        });
        tableHeader.appendChild(headerRow);
        table.appendChild(tableHeader);

        const tableBody = document.createElement("tbody");
        await getQLSanPhamByCNId(chiNhanh.id)
          .then((QLsanPhamArray) => {
            // console.log("Thông tin chi nhánh:", chiNhanh.tenChiNhanh);
            // console.log("Danh sách phiếu nhập:", QLsanPhamArray);

            const optionChuyen = document.createElement("option");
            optionChuyen.value = chiNhanh.id;
            optionChuyen.textContent = chiNhanh.tenChiNhanh;

            const optionNhan = document.createElement("option");
            optionNhan.value = chiNhanh.id;
            optionNhan.textContent = chiNhanh.tenChiNhanh;

            if (
              !selectCuaHangChuyen.querySelector(
                `option[value="${chiNhanh.id}"]`
              )
            ) {
              selectCuaHangChuyen.appendChild(optionChuyen);
            }

            if (
              !selectCuaHangNhan.querySelector(`option[value="${chiNhanh.id}"]`)
            ) {
              selectCuaHangNhan.appendChild(optionNhan);
            }

            // console.log(selectCuaHangNhan);
            // Xử lý disable tùy chọn
            selectCuaHangChuyen.addEventListener("change", () => {
              const selectedCuaHangNhanValue = selectCuaHangChuyen.value;

              // Duyệt qua tất cả các tùy chọn trong selectCuaHangChuyen và kiểm tra
              for (const optionNhan of selectCuaHangNhan.options) {
                if (optionNhan.value === selectedCuaHangNhanValue) {
                  optionNhan.disabled = true;
                } else {
                  optionNhan.disabled = false;
                }
              }
            });

            // console.log(QLsanPhamArray);
            QLsanPhamArray.forEach((item) => {
              const rowData = [
                item.maSP.id, // Mã sách
                item.maSP.tenSanPham,
                item.maSP.loaiSanPham,
                item.maSP.tacGia,
                item.maSP.thuongHieu, // Nhà xuất bản
                item.trenKe,
                item.trongKho,
                item.tongHang,
              ];

              const row = document.createElement("tr");
              rowData.forEach((cellData) => {
                const cell = document.createElement("td");
                cell.textContent = cellData;
                row.appendChild(cell);
              });

              tableBody.appendChild(row);
            });

            table.appendChild(tableBody);
            cardBody.appendChild(table);
            card.appendChild(cardBody);
            tableContainer.appendChild(card);

            // Tạo tùy chọn sách cho selectSanPham
            const selectSanPham = document.getElementById("selectSanPham");
            QLsanPhamArray.forEach((sanPham) => {
              if (
                !selectSanPham.querySelector(
                  `option[value="${sanPham.maSP.id}"]`
                )
              ) {
                const optionSanPham = document.createElement("option");
                optionSanPham.value = sanPham.maSP.id;
                optionSanPham.textContent = sanPham.maSP.tenSanPham;
                selectSanPham.appendChild(optionSanPham);
              }
            });
          })
          .catch((error) => {
            console.error("Lỗi khi lấy thông tin phiếu nhập:", error);
          });
      };
        }
        
    })
    .catch((error) => {
      console.error("Lỗi khi lấy thông tin chi nhánh:", error);
    });
}

async function chuyenSach() {
  const selectedSanPham = document.getElementById("selectSanPham");
  const selectedCuaHangChuyen = document.getElementById("selectCuaHangChuyen");
  const selectedCuaHangNhan = document.getElementById("selectCuaHangNhan");
  const soLuongInput = document.getElementById("count");

  const sachId = selectedSanPham.value;
  const CNChuyenId = selectedCuaHangChuyen.value;
  const CNNhanId = selectedCuaHangNhan.value;
  const soLuongChuyen = parseInt(soLuongInput.value, 10);

  if (
    !sachId ||
    !CNChuyenId ||
    !CNNhanId ||
    isNaN(soLuongChuyen) ||
    soLuongChuyen <= 0
  ) {
    $("#liveToast").removeClass("bg-success");
    $("#liveToast").addClass("bg-danger");
    const toast = new bootstrap.Toast($("#liveToast"));
    $("#toast-context").text("Vui lòng chọn sách, cửa hàng chuyển, cửa hàng nhận và nhập số lượng hợp lệ!");
    toast.show();
    return;
  }

  try {
    const sanPhamCNChuyen = await getQLSanPhamByCNId(CNChuyenId);
    const sanPhamCNNhan = await getQLSanPhamByCNId(CNNhanId);
    const selectedSanPhamInfoCNChuyen = sanPhamCNChuyen.find(
      (item) => item.maSP.id == sachId
    );
    if (selectedSanPhamInfoCNChuyen) {
      if (selectedSanPhamInfoCNChuyen.trongKho >= soLuongChuyen) {
        // Tạo đối tượng phiếu chuyển sản phẩm
        const newPhieu = {
          ngayChuyen: new Date(),
          soLuongChuyen: soLuongChuyen,
          quanLySanPham: selectedSanPhamInfoCNChuyen,
          chiNhanhFrom: {
            id: selectedCuaHangChuyen.value,
          },
          chiNhanhTo: {
            id: selectedCuaHangNhan.value,
          },
        };

        insertNewPhieuCSP(newPhieu)
          .then(() => {
            loadHangHoaChiNhanh();
          })
          .catch((error) => {
            console.error("Lỗi khi thêm phiếu chuyển sản phẩm:", error);
          });
      } else {
        $("#liveToast").removeClass("bg-success");
        $("#liveToast").addClass("bg-danger");
        const toast = new bootstrap.Toast($("#liveToast"));
        $("#toast-context").text("Số lượng sách chuyển vượt quá tổng hàng trong kho chuyển");
        toast.show();
      }
    } else {
      $("#liveToast").removeClass("bg-success");
      $("#liveToast").addClass("bg-danger");
      const toast = new bootstrap.Toast($("#liveToast"));
      $("#toast-context").text("Không tìm thấy thông tin sách trong kho chuyển");
      toast.show();
    }
  } catch (error) {
    console.error("Lỗi khi chuyển sách:", error);
  }
}

// Xử lý sự kiện khi người dùng ấn nút tìm kiếm
function searchChiNhanh() {
  const searchTerm = document.getElementById("search").value;
  const tableContainer = document.getElementById("card-container");
  tableContainer.innerHTML = ""; // Xóa bỏ nội dung cũ trong container

  // Lấy thông tin của mỗi chi nhánh và phiếu nhập tương ứng
  getAllChiNhanh()
    .then((chiNhanhArray) => {
      let found = false; // Biến đánh dấu xem có tìm thấy chi nhánh hay không

      chiNhanhArray.forEach((chiNhanh) => {
        if (
          chiNhanh.tenChiNhanh.toLowerCase().includes(searchTerm.toLowerCase())
        ) {
          console.log("Thông tin chi nhánh:", chiNhanh);
          found = true;

          // Tạo card và hiển thị thông tin của chi nhánh
          const card = document.createElement("section");
          card.classList.add("table__body");
          const cardHeader = document.createElement("div");
          cardHeader.classList.add(
            "card-header",
            "d-flex",
            "justify-content-between"
          );
          const cardTitle = document.createElement("div");
          cardTitle.textContent = chiNhanh.tenChiNhanh;
          cardHeader.appendChild(cardTitle);
          card.appendChild(cardHeader);

          // Tạo bảng cho danh sách phiếu nhập
          const cardBody = document.createElement("div");
          cardBody.classList.add("card-body");
          const table = document.createElement("table");
          table.classList.add("table");
          const tableHeader = document.createElement("thead");
          const headerRow = document.createElement("tr");
          const headers = [
            "Mã sách",
            "Tên sách",
            "Thể loại",
            "Tác giả",
            "Nhà xuất bản",
            "Trên kệ",
            "Trong kho",
            "Tổng hàng",
          ];
          headers.forEach((headerText) => {
            const headerCell = document.createElement("th");
            headerCell.textContent = headerText;
            headerRow.appendChild(headerCell);
          });
          tableHeader.appendChild(headerRow);
          table.appendChild(tableHeader);

          const tableBody = document.createElement("tbody");
          getQLSanPhamByCNId(chiNhanh.id)
            .then((QLsanPhamArray) => {
              console.log("Thông tin chi nhánh:", chiNhanh.tenChiNhanh);
              console.log("Danh sách phiếu nhập:", QLsanPhamArray);

              QLsanPhamArray.forEach((item) => {
                const rowData = [
                  item.maSP.id, // Mã sách
                  item.maSP.tenSanPham,
                  item.maSP.loaiSanPham,
                  item.maSP.tacGia,
                  item.maSP.thuongHieu, // Nhà xuất bản
                  item.trenKe,
                  item.trongKho,
                  item.tongHang,
                ];

                const row = document.createElement("tr");
                rowData.forEach((cellData) => {
                  const cell = document.createElement("td");
                  cell.textContent = cellData;
                  row.appendChild(cell);
                });

                tableBody.appendChild(row);
              });

              table.appendChild(tableBody);
              cardBody.appendChild(table);
              card.appendChild(cardBody);
              tableContainer.appendChild(card);
            })
            .catch((error) => {
              console.error("Lỗi khi lấy thông tin phiếu nhập:", error);
            });
        }
      });

      if (!found) {
        // Hiển thị thông báo khi không tìm thấy chi nhánh
        const alertMessage = document.createElement("div");
        alertMessage.classList.add("alert", "alert-danger");
        alertMessage.textContent = "Không tồn tại chi nhánh bạn tìm kiếm.";
        tableContainer.appendChild(alertMessage);
      }
    })
    .catch((error) => {
      console.error("Lỗi khi lấy thông tin chi nhánh:", error);
    });
}
