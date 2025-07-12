package vn.tdtu.finalterm.service.iterator;

import java.util.Iterator;

import vn.tdtu.finalterm.models.ChiTietHoaDon;
import vn.tdtu.finalterm.service.ChiTietHDService;

public interface ChiTietHoaDonIterator extends Iterator<ChiTietHoaDon> {
    boolean hasNext();
    ChiTietHoaDon next();
}
