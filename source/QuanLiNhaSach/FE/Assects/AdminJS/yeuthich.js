document.addEventListener("DOMContentLoaded", () => {
  loadYeuThich("table-body");
  displayChartAndPopulateUl();
});

function updateUI() {
  $("#table-body tr").remove();
}

async function loadYeuThich() {
  try {
    const combinedData = await combineDataAndCreateTable();

    const tableBody = document.getElementById("table-body");

    tableBody.innerHTML = ""; // Xóa bỏ nội dung cũ của tbody trước khi thêm mới

    combinedData.forEach((dataItem) => {
      const row = document.createElement("tr");
      const columns = [
        dataItem.maSP,
        dataItem.tenSanPham,
        dataItem.theLoai,
        dataItem.tacGia,
        dataItem.thuongHieu,
        formatCurrency(dataItem.giaNhap),
        formatCurrency(dataItem.giaBan),
        dataItem.nhapVao,
        dataItem.banRa,
      ];

      columns.forEach((columnText) => {
        const cell = document.createElement("td");
        cell.textContent = columnText;
        row.appendChild(cell);
      });

      tableBody.appendChild(row);
    });
  } catch (error) {
    console.error("Error while loading data:", error);
  }
}

async function combineDataAndCreateTable() {
  const combinedDataMap = {};

  try {
    const chiNhanhArray = await getAllChiNhanh();

    for (const chiNhanh of chiNhanhArray) {
      const qlSanPhamArray = await getQLSanPhamByCNId(chiNhanh.id);

      for (const qlSanPham of qlSanPhamArray) {
        const sanPham = await getSanPhamById(qlSanPham.maSP.id);
        const phieuNhapArray = await getAllPhieuNhapByCNId(chiNhanh.id);
        const hoaDonArray = await getAllHoaDonByCNId(chiNhanh.id);

        const chiTietPieuNhapInfoArray = phieuNhapArray.filter(
          (item) =>
            item instanceof ChiTietPhieuNhap &&
            item.sanPham.id === qlSanPham.maSP.id
        );

        const chiTietHoaDonInfoArray = hoaDonArray.filter(
          (item) =>
            item instanceof ChiTietHoaDon &&
            item.sanPham.id === qlSanPham.maSP.id
        );

        const chiTietPhieuNhap = chiTietPieuNhapInfoArray.find(
          (item) => item.sanPham.id === qlSanPham.maSP.id
        );

        const chiTietHoaDon = chiTietHoaDonInfoArray.find(
          (item) => item.sanPham.id === qlSanPham.maSP.id
        );
        // console.log(chiTietPieuNhapInfoArray);
        // console.log(chiTietPhieuNhap);
        const giaNhap = chiTietPhieuNhap ? chiTietPhieuNhap.giaNhap : qlSanPham.giaBan*2/3;
        const giaBan = qlSanPham.giaBan;

        if (!combinedDataMap.hasOwnProperty(qlSanPham.maSP.id)) {
          combinedDataMap[qlSanPham.maSP.id] = {
            maSP: qlSanPham.maSP.id,
            tenSanPham: sanPham ? sanPham.tenSanPham : "",
            theLoai: sanPham ? sanPham.loaiSanPham : "",
            tacGia: sanPham ? sanPham.tacGia : "",
            thuongHieu: sanPham ? sanPham.thuongHieu : "",
            giaNhap: giaNhap,
            giaBan: giaBan,
            nhapVao: 0,
            banRa: 0,
          };
        }

        combinedDataMap[qlSanPham.maSP.id].nhapVao += chiTietPhieuNhap
          ? chiTietPhieuNhap.soLuong
          : 0;
        combinedDataMap[qlSanPham.maSP.id].banRa += chiTietHoaDon
          ? chiTietHoaDon.soLuong
          : 0;
      }
    }
  } catch (error) {
    console.error("Error while combining data:", error);
    throw error;
  }

  return Object.values(combinedDataMap);
}

function populateUl(ul, chartData) {
  ul.innerHTML = ""; // Xóa bỏ nội dung cũ của ul trước khi thêm mới

  chartData.labels.forEach((label, index) => {
    const li = document.createElement("li");
    // li.innerHTML = `${label}: <span class="percentage">${chartData.data[index]}%</span>`;
    const percentage =
      chartData.data[index] !== undefined && !isNaN(chartData.data[index])
        ? `${chartData.data[index]}%`
        : "0%";
    li.innerHTML = `${label}: <span class="percentage">${percentage}</span>`;
    ul.appendChild(li);
  });
}

async function displayChartAndPopulateUl() {
  try {
    const sanPhamArray = await getAllSanPham();
    const combinedData = await combineDataAndCreateTable();
    const totalBanRa = combinedData.reduce(
      (total, dataItem) => total + dataItem.banRa,
      0
    );

    const chartData = {
      labels: sanPhamArray.map((sanPham) => sanPham.tenSanPham),
      data: [],
    };

    combinedData.forEach((dataItem) => {
      const labelIndex = chartData.labels.indexOf(dataItem.tenSanPham);
      if (labelIndex !== -1 && dataItem.nhapVao !== 0) {
        const dataValue = parseFloat(
          ((dataItem.banRa / totalBanRa) * 100).toFixed(2)
        );
        chartData.data[labelIndex] = dataValue;
      }
    });

    const myChart = document.querySelector(".my-chart");
    const ul = document.querySelector(".programming-stats .details ul");

    new Chart(myChart, {
      type: "doughnut",
      data: {
        labels: chartData.labels,
        datasets: [
          {
            label: "Yêu thích",
            data: chartData.data,
          },
        ],
      },
      options: {
        borderWidth: 10,
        borderRadius: 2,
        hoverBorderWidth: 0,
        plugins: {
          legend: {
            display: false,
          },
        },
      },
    });

    populateUl(ul, chartData);
  } catch (error) {
    console.error("Error:", error);
  }
}
