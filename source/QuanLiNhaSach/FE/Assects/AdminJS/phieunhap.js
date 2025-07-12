loadPhieuNhap("table-body");

function updateUI() {
  $("#table-body tr").remove();
}

function loadPhieuNhap(idTable) {
  updateUI();
  const tableContainer = document.getElementById("card-container");
  // Lấy thông tin của mỗi chi nhánh và phiếu nhập tương ứng
  getAllChiNhanh()
    .then((chiNhanhArray) => {
      chiNhanhArray.forEach((chiNhanh) => {
        if(chiNhanh.taiKhoanFK.taiKhoan != "this.is.manager.fahasa@gmail.com"){
          // console.log("Thông tin chi nhánh:", chiNhanh);
        // Tạo một bảng mới cho mỗi chi nhánh
        const card = document.createElement("section");
        card.classList.add("table__body");
        const cardHeader = document.createElement("div");
        cardHeader.classList.add(
          "card-header",
          "d-flex",
          "justify-content-between"
        );
        // Tạo tên chi nhánh
        const tenChiNhanhDiv = document.createElement("div");
        tenChiNhanhDiv.classList.add("hi");
        tenChiNhanhDiv.innerText = chiNhanh.tenChiNhanh;
        cardHeader.appendChild(tenChiNhanhDiv);
        card.appendChild(cardHeader);

        // Tạo body cho card
        const cardBody = document.createElement("div");
        cardBody.classList.add("card-body");

        // Tạo table và thead
        const table = document.createElement("table");
        table.classList.add("table");
        const thead = document.createElement("thead");
        thead.innerHTML =
          '<tr><th scope="col">Mã phiếu nhập</th><th scope="col">Ngày nhập</th><th scope="col">Tổng cộng</th><th scope="col"></th></tr>';
        table.appendChild(thead);

        // Tạo tbody
        const tbody = document.createElement("tbody");
        tbody.id = idTable;
        table.appendChild(tbody);

        cardBody.appendChild(table);
        card.appendChild(cardBody);

        // Tạo footer cho card
        const cardFooter = document.createElement("div");
        cardFooter.classList.add("card-footer");
        const tongChiDiv = document.createElement("div");
        tongChiDiv.classList.add("fw-bold", "fw-4");
        tongChiDiv.innerText = "Tổng chi: " + formatCurrency(0);
        cardFooter.appendChild(tongChiDiv);
        card.appendChild(cardFooter);

        // Thêm card
        tableContainer.appendChild(card);

        // Lấy danh sách phiếu nhập cho mỗi chi nhánh
        getAllPhieuNhapByCNId(chiNhanh.id)
          .then((result) => {
            // console.log("Thông tin chi nhánh:", chiNhanh.tenChiNhanh);
            // console.log("Danh sách phiếu nhập:", result);

            // Tách thằng PhieuNhap ra
            const phieuNhapArray = result.filter(
              (item) => item instanceof PhieuNhap
            );
            // Tách thằng chiTietPhieuNhap ra
            const chiTietPhieuNhapArray = result.filter(
              (item) => item instanceof ChiTietPhieuNhap
            );

            // Tính tổng cộng của mảng PhieuNhap
            const totalTongCong = phieuNhapArray.reduce(
              (total, item) => total + item.tongCong,
              0
            );

            // console.log("totalTongCong:", totalTongCong);

            phieuNhapArray.forEach((item) => {
              // Tạo một dòng mới trong bảng
              // console.log(item);

              // CÁC TRƯỜNG DỮ LIỆU
              // Thêm dòng mới
              const row = document.createElement("tr");

              // Thêm các ô dữ liệu
              const idCell = document.createElement("td");
              idCell.scope = "row";
              idCell.textContent = item.id;
              row.appendChild(idCell);

              const ngayNhapCell = document.createElement("td");
              ngayNhapCell.textContent = item.ngayNhap;
              row.appendChild(ngayNhapCell);

              const tongCongCell = document.createElement("td");
              tongCongCell.textContent = formatCurrency(item.tongCong);
              row.appendChild(tongCongCell);

              tbody.appendChild(row);

              // Thay đổi tổng chi
              const divTongChi = cardFooter.querySelector(".fw-bold");
              // console.log("totalTongCong:", totalTongCong);
              divTongChi.innerText = "Tổng chi: " + formatCurrency(totalTongCong);
            });
          })
          .catch((error) => {
            console.error("Lỗi khi lấy thông tin phiếu nhập:", error);
          });
        }
        
      });
    })
    .catch((error) => {
      console.error("Lỗi khi lấy thông tin chi nhánh:", error);
    });
}
