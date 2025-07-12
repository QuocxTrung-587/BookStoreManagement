loadThuChi("table-body");

function updateUI() {
  $("#table-body tr").remove();
}

function loadThuChi(idTable) {
  updateUI();
  // const tableContainer = document.querySelector(".col.py-3[name='content']");
  const tableContainer = document.getElementById("store-container")
  getAllChiNhanh()
    .then((chiNhanhArray) => {
      chiNhanhArray.forEach((chiNhanh) => {
        if (chiNhanh.taiKhoanFK.taiKhoan != "this.is.manager.fahasa@gmail.com") {
          // Tạo card cho từng chi nhánh
        const card = document.createElement("section");
        card.classList.add("table__body");
        const cardHeader = document.createElement("div");
        cardHeader.classList.add(
          "card-header",
          "d-flex",
          "justify-content-between"
        );
        const tenChiNhanhDiv = document.createElement("div");
        tenChiNhanhDiv.classList.add("hi");
        tenChiNhanhDiv.textContent = chiNhanh.tenChiNhanh;
        cardHeader.appendChild(tenChiNhanhDiv);
        card.appendChild(cardHeader);

        const cardBody = document.createElement("div");
        cardBody.classList.add("card-body");

        const table = document.createElement("table");
        table.classList.add("table");
        const thead = document.createElement("thead");
        thead.innerHTML = `
                <tr>
                  <th scope="col">Mã phiếu nhập</th>
                  <th scope="col">Tổng chi</th>
                  <th scope="col">Mã hóa đơn</th>
                  <th scope="col">Tổng thu</th>
                </tr>`;
        table.appendChild(thead);

        const tbody = document.createElement("tbody");
        tbody.id = idTable;
        table.appendChild(tbody);

        cardBody.appendChild(table);
        card.appendChild(cardBody);

        const cardFooter = document.createElement("div");
        cardFooter.classList.add("card-footer");
        const tongThuChiDiv = document.createElement("div");
        tongThuChiDiv.classList.add("fw-bold", "fw-4");
        cardFooter.appendChild(tongThuChiDiv);
        card.appendChild(cardFooter);

        tableContainer.appendChild(card);

        // Lấy danh sách phiếu nhập và hóa đơn cho mỗi chi nhánh
        Promise.all([
          getAllPhieuNhapByCNId(chiNhanh.id),
          getAllHoaDonByCNId(chiNhanh.id),
        ])
          .then(([phieuNhapArray, hoaDonArray]) => {
            const phieuNhapInfoArray = phieuNhapArray.filter(
              (item) => item instanceof PhieuNhap
            );
            const hoaDonInfoArray = hoaDonArray.filter(
              (item) => item instanceof HoaDon
            );
            const totalTongChi = phieuNhapInfoArray.reduce(
              (total, item) => total + item.tongCong,
              0
            );
            const totalTongThu = hoaDonInfoArray.reduce(
              (total, item) => total + item.tongCong,
              0
            );

            const maxRowCount = Math.max(
              phieuNhapInfoArray.length,
              hoaDonInfoArray.length
            );

            for (let i = 0; i < maxRowCount; i++) {
              const row = document.createElement("tr");

              if (i < phieuNhapInfoArray.length) {
                const phieuNhap = phieuNhapInfoArray[i];
                row.innerHTML += `
                    <td>${phieuNhap.id}</td>
                    <td>${formatCurrency(phieuNhap.tongCong)}</td>`;
              } else {
                row.innerHTML += "<td></td><td></td>";
              }

              if (i < hoaDonInfoArray.length) {
                const hoaDon = hoaDonInfoArray[i];
                row.innerHTML += `
                    <td>${hoaDon.id}</td>
                    <td>${formatCurrency(hoaDon.tongCong)}</td>`;
              } else {
                row.innerHTML += "<td></td><td></td>";
              }

              tbody.appendChild(row);
            }
            // Tạo dòng tổng chi và tổng thu
            const totalRow = document.createElement("tr");
            totalRow.innerHTML = `
            <td colspan="1" class="fw-bold">Tổng chi</td>
            <td class="fw-bold">${formatCurrency(totalTongChi)}</td>
            <td colspan="1" class="fw-bold">Tổng thu</td>
            <td class="fw-bold">${formatCurrency(totalTongThu)}</td>
            `;
            tbody.appendChild(totalRow);

            const totalThuChi = totalTongThu - totalTongChi;
            tongThuChiDiv.innerText = `Tổng thu chi: ${formatCurrency(totalThuChi)}`;
          })
          .catch((error) => {
            console.error(
              "Lỗi khi lấy thông tin phiếu nhập hoặc hóa đơn:",
              error
            );
          });
         }
        
      });
    })
    .catch((error) => {
      console.error("Lỗi khi lấy thông tin chi nhánh:", error);
    });
}
