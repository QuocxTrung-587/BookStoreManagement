let tonKhoData;
$(document).ready(function () {
  loadHangHoa();
});

async function loadHangHoa() {
  const allChiNhanh = await getAllChiNhanh();
  const combinedProducts = await getCombinedProducts(allChiNhanh);
  updateUI(combinedProducts);
  createChart();
}

async function getCombinedProducts(allChiNhanh) {
  const combinedProducts = {};
  const combinedProducts1 = {};
  for (const chiNhanh of allChiNhanh) {
    const QLSanPhamArray = await getQLSanPhamByCNId(chiNhanh.id);

    QLSanPhamArray.forEach((QLsanPham) => {
      // console.log(QLsanPham)
      const productId = QLsanPham.maSP.id;
      const chiNhanhId = QLsanPham.maCN.id;

      if (QLsanPham.trangThai === 0) {
        if (!combinedProducts1[productId+"."+chiNhanhId]) {
          combinedProducts1[productId+"."+chiNhanhId] = {
            QLSP: QLsanPham,
            totalQuantity: QLsanPham.tongHang,
          };
        } else {
          combinedProducts1[productId+"."+chiNhanhId].totalQuantity += QLsanPham.tongHang;
        }
      }
      if (QLsanPham.trangThai === 0) {
        if (!combinedProducts[productId]) {
          combinedProducts[productId] = {
            QLSP: QLsanPham,
            totalQuantity: QLsanPham.tongHang,
          };
        } else {
          combinedProducts[productId].totalQuantity += QLsanPham.tongHang;
        }
      }
    });
  }
  tonKhoData = combinedProducts;
  return Object.values(combinedProducts1);
}

async function updateUI(combinedProducts) {
  $("#table-body tr").remove();
  let numbooks = 0;
  let refund = 0;

  for (const combinedProduct of combinedProducts) {
    // console.log(combinedProduct)
    const row = $("<tr>");
    const SPInfo = combinedProduct.QLSP.maSP;
    const chiNhanh = combinedProduct.QLSP.maCN;

    $("<th>").text(SPInfo.id).appendTo(row);
    $("<td>").text(SPInfo.tenSanPham).appendTo(row);
    $("<td>").text(SPInfo.loaiSanPham).appendTo(row);
    $("<td>").text(SPInfo.thuongHieu).appendTo(row);
    $("<td>").text(SPInfo.tacGia).appendTo(row);
    $("<td>").text(SPInfo.theLoai).appendTo(row);
    // console.log(combinedProduct);
    const priceCell = $("<td>");
    const SPPrice = await getPricingImport(
      combinedProduct.QLSP.maCN.id,
      SPInfo.id
    );
    priceCell.append(formatCurrency(SPPrice));
    priceCell.appendTo(row);

    $("<td>").text(combinedProduct.totalQuantity).appendTo(row);
    $("<td>").text(combinedProduct.QLSP.hanTon).appendTo(row);
    $("<td>").text("Trả hàng").appendTo(row).addClass("text-danger fw-bold");
    $("<td>").text(chiNhanh.tenChiNhanh).appendTo(row);
    $("#table-body").append(row);

    refund += parseInt(combinedProduct.totalQuantity) * parseInt(SPPrice);
    numbooks += combinedProduct.totalQuantity;
  }

  $("#numBooksReturn").text("Số lượng sách hoàn trả: " + numbooks);
  $("#refundPricing").text("Số tiền hoàn lại: " + formatCurrency(refund));
}

async function createChart() {
  const sanPhamArray = await getAllSanPham();
  const productNames = sanPhamArray.map((product) => product.tenSanPham);
  const tongHang = [];
  productNames.forEach(tenSanPham => {
    found = false;
    for (const key in tonKhoData) {
      if (tonKhoData.hasOwnProperty(key)) {
        const value = tonKhoData[key];
        if(tenSanPham == value.QLSP.maSP.tenSanPham){
          tongHang.push(value.totalQuantity)
          found = true;
        }
      }
    }
    if(found == false){
      tongHang.push(0);
    }
  });
  
  const data = {
    labels: productNames,
    datasets: [
      {
        label: "Tồn kho",
        data: tongHang,
        backgroundColor: [
          "rgba(255, 26, 104, 0.2)",
          "rgba(54, 162, 235, 0.2)",
          "rgba(255, 206, 86, 0.2)",
          "rgba(75, 192, 192, 0.2)",
          "rgba(153, 102, 255, 0.2)",
          "rgba(255, 159, 64, 0.2)",
          "rgba(0, 0, 0, 0.2)",
        ],
        borderColor: [
          "rgba(255, 26, 104, 1)",
          "rgba(54, 162, 235, 1)",
          "rgba(255, 206, 86, 1)",
          "rgba(75, 192, 192, 1)",
          "rgba(153, 102, 255, 1)",
          "rgba(255, 159, 64, 1)",
          "rgba(0, 0, 0, 1)",
        ],
        borderWidth: 1,
      },
    ],
  };
  // config
  const config = {
    type: "bar",
    data,
    options: {
      scales: {
        y: {
          beginAtZero: true,
        },
      },
    },
  };

  // render init block
  const myChart = new Chart(document.getElementById("myChart"), config);

  // Instantly assign Chart.js version
  const chartVersion = document.getElementById("chartVersion");
  if(chartVersion){
    chartVersion.innerText = Chart.version;
  }
}
