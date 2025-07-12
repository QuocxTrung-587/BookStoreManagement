package vn.tdtu.finalterm.samples;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import vn.tdtu.finalterm.models.*;
import vn.tdtu.finalterm.repositories.*;

import java.sql.Date;
import java.util.List;

@Configuration
public class Sample {
    @Autowired
    private PasswordEncoder passwordEncoder;

    // logger
    private static final Logger logger = LoggerFactory.getLogger(Sample.class);

    @Bean
    CommandLineRunner initDataBase(SanPhamRepository sanPhamRepository,
                                   TaiKhoanRepository taiKhoanRepository,
                                   ChiNhanhRepository chiNhanhRepository,
                                   QuanLySPRepository quanLySPRepository,
                                   PhieuNhapRepository phieuNhapRepository,
                                   ChiTietPNRepository chiTietPNRepository) {
        return new CommandLineRunner() {
            @Override
            public void run(String... args) throws Exception {
                // SanPham
                SanPham.SanPhamBuilder productA =  SanPham.builder()
                        .tenSanPham("Doraemon")
                        .loaiSanPham("Sách")
                        .thuongHieu("NXB Kim Đồng")
                        .tacGia("Fujiko Fujio")
                        .theLoai("Truyện Tranh");
                SanPham.SanPhamBuilder productB =  SanPham.builder()
                        .tenSanPham("Hai vạn dặm dưới biển")
                        .loaiSanPham("Sách")
                        .thuongHieu("NXB Văn Học")
                        .tacGia("Jules Verne")
                        .theLoai("Tiểu Thuyết");
                SanPham.SanPhamBuilder productC =  SanPham.builder()
                        .tenSanPham("Đồng Hồ Báo Thức")
                        .loaiSanPham("Đồng Hồ")
                        .thuongHieu("Apple")
                        .tacGia("")
                        .theLoai("");
                logger.info("insert: " + sanPhamRepository.save(productA.build()));
                logger.info("insert: " + sanPhamRepository.save(productB.build()));
                logger.info("insert: " + sanPhamRepository.save(productC.build()));

                // ChiNhanh + TaiKhoan
                TaiKhoan.TaiKhoanBuilder taiKhoanA = TaiKhoan.builder()
                        .taiKhoan("this.is.manager.fahasa@gmail.com")
                        .matKhau(passwordEncoder.encode("123"))
                        .enabled(true);
                ChiNhanh.ChiNhanhBuilder chiNhanhA = ChiNhanh.builder()
                        .id(1L)
                        .tenChiNhanh("Fahasa - Admin")
                        .diaChi("Quan 7")
                        .email("this.is.manager.fahasa@gmail.com")
                        .taiKhoanFK(taiKhoanA.build());
                taiKhoanA.chiNhanh(chiNhanhA.build());
//                TaiKhoan taiKhoanP = taiKhoanA.build();
//                ChiNhanh chiNhanhP = chiNhanhA.build();
                logger.info("insert: " + taiKhoanRepository.save(taiKhoanA.build()));
                logger.info("insert: " + chiNhanhRepository.save(chiNhanhA.build()));

                TaiKhoan.TaiKhoanBuilder taiKhoanB = TaiKhoan.builder()
                        .taiKhoan("aeonBinhTan@gmail.com")
                        .matKhau(passwordEncoder.encode("123"))
                        .enabled(true);
                ChiNhanh.ChiNhanhBuilder chiNhanhB = ChiNhanh.builder()
                        .id(2L)
                        .tenChiNhanh("Fahasa - Aeon Binh Tan")
                        .diaChi("Binh Tan")
                        .email("aeonBinhTan@gmail.com")
                        .taiKhoanFK(taiKhoanB.build());
                taiKhoanB.chiNhanh(chiNhanhB.build());
                logger.info("insert: " + taiKhoanRepository.save(taiKhoanB.build()));
                logger.info("insert: " + chiNhanhRepository.save(chiNhanhB.build()));

//                TaiKhoan taiKhoan = new TaiKhoan("user1", "1111");
//                ChiNhanh chiNhanh = new ChiNhanh(1L, "hello", "Q7");
//                taiKhoan.setChiNhanh(chiNhanh);
//                taiKhoan.getChiNhanh().setTaiKhoan(taiKhoan);
//                chiNhanh.setTaiKhoan(taiKhoan);
//                logger.info("insert: " + taiKhoanRepository.save(taiKhoan));
//                logger.info("insert: " + chiNhanhRepository.save(chiNhanh));

//                List<ChiNhanh> box = chiNhanhRepository.findAll();

//                logger.info("list Chi Nhanh: " + box);

                // QuanLySanPham + SanPham + ChiNhanh
//                QuanLySanPham.QuanLySanPhamBuilder quanLyA = QuanLySanPham.builder()
//                        .id(1L)
//                        .hanTon(new Date(2023, 3, 2))
//                        .giaBan(302.0f)
//                        .trenKe(0)
//                        .trongKho(3000)
//                        .trangThai(1)
//                        // This line shouldn't use productA.build() because It's just Transient
//                        // not Persistent (we can fix this by put CascadeType.ALL next to FK of QuanLySP Class
//                        // but it will save 2 time because productA save once above and once from CascadeType.ALL)
//                        // so just call productA from DB(Detached -> Persistent) (productA was saved from the first time)
//                        .maSP(sanPhamRepository.getReferenceById(1L))
//                        .maCN(chiNhanhB.build());
//
//                QuanLySanPham.QuanLySanPhamBuilder quanLyB = QuanLySanPham.builder()
//                        .id(2L)
//                        .hanTon(new Date(2021, 4, 2))
//                        .giaBan(102.0f)
//                        .trenKe(100)
//                        .trongKho(2000)
//                        .trangThai(0)
//                        .maSP(sanPhamRepository.getReferenceById(2L))
//                        .maCN(chiNhanhB.build());
//
//                QuanLySanPham.QuanLySanPhamBuilder quanLyC = QuanLySanPham.builder()
//                        .id(3L)
//                        .hanTon(new Date(2021, 4, 2))
//                        .giaBan(102.0f)
//                        .trenKe(100)
//                        .trongKho(2000)
//                        .trangThai(0)
//                        .maSP(sanPhamRepository.getReferenceById(3L))
//                        .maCN(chiNhanhA.build());
//
//                logger.info("insert: " + quanLySPRepository.save(quanLyA.build()));
//                logger.info("insert: " + quanLySPRepository.save(quanLyB.build()));
//                logger.info("insert: " + quanLySPRepository.save(quanLyC.build()));
//
//                List<QuanLySanPham> box2 = quanLySPRepository.findAll();
//
//                logger.info("list Quan Ly San Pham: " + box2);

                // PhieuNhap + ChiNhanh
//                PhieuNhap.PhieuNhapBuilder phieuNhapA = PhieuNhap.builder()
//                        .id(1L)
//                        .ngayNhap(new Date(2020 - 1900, 2, 3))
//                        .tongCong(2000.0f)
//                        .chiNhanh(chiNhanhRepository.getReferenceById(1L));
//
//                PhieuNhap.PhieuNhapBuilder phieuNhapB = PhieuNhap.builder()
//                        .id(2L)
//                        .ngayNhap(new Date(2023 - 1900, 1, 11))
//                        .tongCong(3000.0f)
//                        .chiNhanh(chiNhanhRepository.getReferenceById(1L));
//
//                PhieuNhap.PhieuNhapBuilder phieuNhapC = PhieuNhap.builder()
//                        .id(3L)
//                        .ngayNhap(new Date(2022 - 1900, 0, 4))
//                        .tongCong(4000.0f)
//                        .chiNhanh(chiNhanhRepository.getReferenceById(2L));

//                logger.info("insert: " + phieuNhapRepository.save(phieuNhapA.build()));
//                logger.info("insert: " + phieuNhapRepository.save(phieuNhapB.build()));
//                logger.info("insert: " + phieuNhapRepository.save(phieuNhapC.build()));

                // ChiTietPhieuNhap
//                ChiTietPhieuNhap.ChiTietPhieuNhapBuilder chiTietA = ChiTietPhieuNhap.builder()
//                        .id(1L)
//                        .giaNhap(3000.0f)
//                        .soLuong(11)
//                        .tongTien(0.0f)
//                        .sanPham(sanPhamRepository.getReferenceById(1L))
//                        .chiNhanh(chiNhanhRepository.getReferenceById(1L))
//                        .phieuNhap(phieuNhapRepository.getReferenceById(1L));
//
//                ChiTietPhieuNhap.ChiTietPhieuNhapBuilder chiTietA2 = ChiTietPhieuNhap.builder()
//                        .id(2L)
//                        .giaNhap(400.0f)
//                        .soLuong(100)
//                        .tongTien(0.0f)
//                        .sanPham(sanPhamRepository.getReferenceById(1L))
//                        .chiNhanh(chiNhanhRepository.getReferenceById(1L))
//                        .phieuNhap(phieuNhapRepository.getReferenceById(1L));
//
//                ChiTietPhieuNhap.ChiTietPhieuNhapBuilder chiTietB = ChiTietPhieuNhap.builder()
//                        .id(3L)
//                        .giaNhap(2000.0f)
//                        .soLuong(10)
//                        .tongTien(0.0f)
//                        .sanPham(sanPhamRepository.getReferenceById(2L))
//                        .chiNhanh(chiNhanhRepository.getReferenceById(1L))
//                        .phieuNhap(phieuNhapRepository.getReferenceById(1L));

//                ChiTietPhieuNhap.ChiTietPhieuNhapBuilder chiTietC = ChiTietPhieuNhap.builder()
//                        .id(1L)
//                        .giaNhap(3000.0f)
//                        .soLuong(11)
//                        .tongTien(0.0f)
//                        .sanPham(sanPhamRepository.getReferenceById(1L))
//                        .chiNhanh(chiNhanhRepository.getReferenceById(1L))
//                        .phieuNhap(phieuNhapRepository.getReferenceById(1L));
//
//                logger.info("insert: " + chiTietPNRepository.save(chiTietA.build()));
//                logger.info("insert: " + chiTietPNRepository.save(chiTietA2.build()));
//                logger.info("insert: " + chiTietPNRepository.save(chiTietB.build()));
//
//                List<ChiTietPhieuNhap> box3 = chiTietPNRepository.findAll();
//
//                logger.info("list Chi Tiet Phieu Nhap: " + box3);
            }
        };
    }
}
