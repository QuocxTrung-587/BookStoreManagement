package vn.tdtu.finalterm.service.iterator;

import vn.tdtu.finalterm.models.ChiTietHoaDon;

import java.util.List;

public class ChiTietHDList implements ChiTietHoaDonCollection {
    private final List<ChiTietHoaDon> chiTietList;

    public ChiTietHDList(List<ChiTietHoaDon> chiTietList) {
        this.chiTietList = chiTietList;
    }

    @Override
    public ChiTietHoaDonIterator iterator() {
        return new ChiTietHDListIterator(chiTietList);
    }

}
