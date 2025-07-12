package vn.tdtu.finalterm.service.templatemethod;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import vn.tdtu.finalterm.models.ResponseObject;

public abstract class AbstractPhieuDeletion {
    // Template method: Quy trình xóa phiếu
    public final ResponseEntity<ResponseObject> deletePhieu(Long phieuId) {
        // Bước 1: Kiểm tra sự tồn tại của phiếu
        ResponseEntity<ResponseObject> checkResult = checkPhieuExistence(phieuId);
        if (checkResult.getStatusCode() != HttpStatus.OK) {
            return checkResult;
        }

        // Bước 2: Tìm danh sách chi tiết liên quan
        Object chiTietList = findRelatedChiTiet(phieuId);
        if (chiTietList == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new ResponseObject("failed", "No related ChiTiet found for phieuId = " + phieuId, ""));
        }

        // Bước 3: Cập nhật số lượng kho
        ResponseEntity<ResponseObject> updateResult = updateInventory(chiTietList);
        if (updateResult != null && updateResult.getStatusCode() != HttpStatus.OK) {
            return updateResult;
        }

        // Bước 4: Xóa chi tiết liên quan
        deleteRelatedChiTiet(chiTietList);

        // Bước 5: Xóa phiếu chính
        deletePhieuRecord(phieuId);

        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("ok", "Delete Phieu Success", ""));
    }

    // Bước 1: Kiểm tra sự tồn tại của phiếu (phải được triển khai bởi lớp con)
    protected abstract ResponseEntity<ResponseObject> checkPhieuExistence(Long phieuId);

    // Bước 2: Tìm danh sách chi tiết liên quan (phải được triển khai bởi lớp con)
    protected abstract Object findRelatedChiTiet(Long phieuId);

    // Bước 3: Cập nhật số lượng kho (phải được triển khai bởi lớp con)
    protected abstract ResponseEntity<ResponseObject> updateInventory(Object chiTietList);

    // Bước 4: Xóa chi tiết liên quan (phải được triển khai bởi lớp con)
    protected abstract void deleteRelatedChiTiet(Object chiTietList);

    // Bước 5: Xóa phiếu chính (phải được triển khai bởi lớp con)
    protected abstract void deletePhieuRecord(Long phieuId);
}