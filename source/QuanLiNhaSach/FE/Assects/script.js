$(document).ready(function() {
    token = sessionStorage.getItem("tokenData")
    //check token nếu k tồn tại => 403 page.
    if(!token){
        location.href = "../Assects/pagesEvent/403.html"
    }
    let chiNhanh = sessionStorage.getItem("infoChiNhanh");
    chiNhanh = JSON.parse(chiNhanh)
    if(chiNhanh.taiKhoanFK.taiKhoan == "this.is.manager.fahasa@gmail.com"){
        $("#includedSidebar").load("nav2.html", function() {
            $("#navlink0").click(function () {
                sessionStorage.setItem("idNavBarActive", null);
            })
            $(".nav-link").click(function (event) {
                event.preventDefault();
                $(".nav-link").removeClass("active");
                if ($(this).attr("id") == "navlink0") {
                    location.href = $(this).attr("href")
                }
                else {
                    sessionStorage.setItem("idNavBarActive", $(this).attr("id"));
                    location.href = $(this).attr("href")
                }
            });
            var idNavActive = sessionStorage.getItem("idNavBarActive");
            if (idNavActive == "navlink4" || idNavActive == "navlink5") {
                $("#navlink3").addClass("collapsed");
                $("#navlink3").attr("aria-expanded", true);
                $("#submenu4").addClass("show");
            }
            else if (idNavActive == "navlink7" || idNavActive == "navlink8" || idNavActive == "navlink9") {
                $("#navlink6").addClass("collapsed");
                $("#navlink6").attr("aria-expanded", true);
                $("#submenu3").addClass("show");
            }
            if (idNavActive) {
                $("#" + idNavActive).addClass("active");
            }
        });
    } else {
        $("#includedSidebar").load("nav.html", function () {
            $("#navlink0").click(function () {
                sessionStorage.setItem("idNavBarActive", null);
            })
            $(".nav-link").click(function (event) {
                event.preventDefault();
                $(".nav-link").removeClass("active");
                if ($(this).attr("id") == "navlink0") {
                    location.href = $(this).attr("href")
                }
                else {
                    sessionStorage.setItem("idNavBarActive", $(this).attr("id"));
                    location.href = $(this).attr("href")
                }
            });
            var idNavActive = sessionStorage.getItem("idNavBarActive");
            if (idNavActive == "navlink7" || idNavActive == "navlink8") {
                $("#navlink6").addClass("collapsed");
                $("#navlink6").attr("aria-expanded", true);
                $("#submenu3").addClass("show");
            }
            if (idNavActive) {
                $("#" + idNavActive).addClass("active");
            }
        })
    }
});

function eventSortingTable() {
    table_rows = document.querySelectorAll('tbody tr')
    table_headings = document.querySelectorAll('thead th');
    // 2. Sorting | Ordering data of HTML table

    table_headings.forEach((head, i) => {
        let sort_asc = true;
        head.onclick = () => {
            table_headings.forEach(head => head.classList.remove('active'));
            head.classList.add('active');

            document.querySelectorAll('td').forEach(td => td.classList.remove('active'));
            table_rows.forEach(row => {
                row.querySelectorAll('td')[i].classList.add('active');
            })

            head.classList.toggle('asc', sort_asc);
            sort_asc = head.classList.contains('asc') ? false : true;

            sortTable(i, sort_asc);
        }
    })
}
function sortTable(column, sort_asc) {
    [...table_rows].sort((a, b) => {
        let first_row = a.querySelectorAll('td')[column].textContent.toLowerCase(),
            second_row = b.querySelectorAll('td')[column].textContent.toLowerCase();

        return sort_asc ? (first_row < second_row ? 1 : -1) : (first_row < second_row ? -1 : 1);
    })
        .map(sorted_row => document.querySelector('tbody').appendChild(sorted_row));
}


toPDF = document.getElementById('toPDF')
if(toPDF){
    toPDF.addEventListener('click', function () {
        var HTML_Width = $("#form-print").width();
        var HTML_Height = $("#form-print").height();
        var top_left_margin = 15;
        var PDF_Width = HTML_Width + (top_left_margin * 2);
        var PDF_Height = (PDF_Width * 1.5) + (top_left_margin * 2);
        var canvas_image_width = HTML_Width;
        var canvas_image_height = HTML_Height;
        console.log(HTML_Width)
        console.log(HTML_Height)
        var totalPDFPages = Math.ceil(HTML_Height / PDF_Height) - 1;
    
        html2canvas($("#form-print")[0]).then(function (canvas) {
            var imgData = canvas.toDataURL("image/jpeg", 1.0);
            var pdf = new jsPDF('p', 'pt', [PDF_Width, PDF_Height]);
            pdf.addImage(imgData, 'JPG', top_left_margin, top_left_margin, canvas_image_width, canvas_image_height);
            console.log(pdf)
    
            for (var i = 1; i <= totalPDFPages; i++) {
                pdf.addPage(PDF_Width, PDF_Height);
                pdf.addImage(imgData, 'JPG', top_left_margin, -(PDF_Height * i) + (top_left_margin * 4), canvas_image_width, canvas_image_height);
            }
            var url = window.location.href;
            var segments = url.split("/");
            var tenTrangHienTai = segments[segments.length - 1];
            tenTrangHienTai = tenTrangHienTai.split(".")[0];
            console.log(tenTrangHienTai);
            pdf.save(tenTrangHienTai + ".pdf");
        });
    
    })
}
toEXCEL = document.getElementById('toEXCEL')
if(toEXCEL){
    toEXCEL.addEventListener('click', function () {
        var data = document.getElementById('data');
        console.log(data)
        var file = XLSX.utils.table_to_book(data, { sheet: "sheet1" });
        XLSX.write(file, { bookType: 'xlsx', bookSST: true, type: 'base64' });
        var url = window.location.href;
        var segments = url.split("/");
        var tenTrangHienTai = segments[segments.length - 1];
        tenTrangHienTai = tenTrangHienTai.split(".")[0];
        console.log(tenTrangHienTai);
        XLSX.writeFile(file, tenTrangHienTai + '.xlsx');
    })
}


// function html_table_to_excel(type) {
//     var data = document.getElementById('data');

//     var file = XLSX.utils.table_to_book(data, { sheet: "sheet1" });

//     XLSX.write(file, { bookType: type, bookSST: true, type: 'base64' });

//     XLSX.writeFile(file, 'file.' + type);
// }

// const export_button = document.getElementById('toEXCEL');

// export_button.addEventListener('click', () => {
//     html_table_to_excel('xlsx');
// });
