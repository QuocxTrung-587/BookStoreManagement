package vn.tdtu.finalterm.service.templatemethod;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import vn.tdtu.finalterm.models.ChiTietPhieuNhap;
import vn.tdtu.finalterm.models.PhieuNhap;
import vn.tdtu.finalterm.models.ResponseObject;
import vn.tdtu.finalterm.repositories.ChiTietPNRepository;
import vn.tdtu.finalterm.repositories.PhieuNhapRepository;
import vn.tdtu.finalterm.service.QuanLySPService;

import java.util.List;
import java.util.Optional;

@Service
public class PhieuNhapDeletion extends AbstractPhieuDeletion {
    @Autowired
    private PhieuNhapRepository phieuNhapRepository;
    @Autowired
    private ChiTietPNRepository chiTietPNRepository;
    @Autowired
    private QuanLySPService quanLySPService;

    @Override
    protected ResponseEntity<ResponseObject> checkPhieuExistence(Long phieuId) {
        Optional<PhieuNhap> foundPN = phieuNhapRepository.findById(phieuId);
        if (!foundPN.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new ResponseObject("failed", "Can't find PhieuNhap with id = " + phieuId, ""));
        }
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

    @Override
    protected Object findRelatedChiTiet(Long phieuId) {
        Optional<PhieuNhap> foundPN = phieuNhapRepository.findById(phieuId);
        if (!foundPN.isPresent()) {
            return null;
        }
        return chiTietPNRepository.findAllByPhieuNhap(foundPN);
    }

    @Override
    protected ResponseEntity<ResponseObject> updateInventory(Object chiTietList) {
        List<ChiTietPhieuNhap> boxCTPN = (List<ChiTietPhieuNhap>) chiTietList;
        return quanLySPService.updateEffectByDeleteChiTietPN(boxCTPN);
    }

    @Override
    protected void deleteRelatedChiTiet(Object chiTietList) {
        List<ChiTietPhieuNhap> boxCTPN = (List<ChiTietPhieuNhap>) chiTietList;
        chiTietPNRepository.deleteAll(boxCTPN);
    }

    @Override
    protected void deletePhieuRecord(Long phieuId) {
        phieuNhapRepository.deleteById(phieuId);
    }
}