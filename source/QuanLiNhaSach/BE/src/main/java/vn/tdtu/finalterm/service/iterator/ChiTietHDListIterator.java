package vn.tdtu.finalterm.service.iterator;

import java.util.List;

import vn.tdtu.finalterm.models.ChiTietHoaDon;
import vn.tdtu.finalterm.service.ChiTietHDService;

public class ChiTietHDListIterator implements ChiTietHoaDonIterator {

    private final List<ChiTietHoaDon> list;
    private int index = 0;

    public ChiTietHDListIterator(List<ChiTietHoaDon> list) {
        this.list = list;
    }

    @Override
    public boolean hasNext() {
        return index < list.size();
    }

    @Override
    public ChiTietHoaDon next() {
        return list.get(index++);
    }
}
