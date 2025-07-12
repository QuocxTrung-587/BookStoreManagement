package vn.tdtu.finalterm.service.states;

import vn.tdtu.finalterm.models.TaiKhoan;

public interface AccountState {
    void handleState(TaiKhoan taiKhoan);
    String getStateName();
}