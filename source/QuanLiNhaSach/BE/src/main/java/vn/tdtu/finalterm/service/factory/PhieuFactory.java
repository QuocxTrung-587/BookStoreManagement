package vn.tdtu.finalterm.service.factory;

import vn.tdtu.finalterm.models.ChiNhanh;

public abstract class PhieuFactory {
    public abstract Phieu createPhieu(ChiNhanh chiNhanh);
}