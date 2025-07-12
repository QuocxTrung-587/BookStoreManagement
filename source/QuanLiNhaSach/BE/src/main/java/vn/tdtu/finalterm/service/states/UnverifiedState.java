package vn.tdtu.finalterm.service.states;

import vn.tdtu.finalterm.models.TaiKhoan;

public class UnverifiedState implements AccountState {
    @Override
    public void handleState(TaiKhoan taiKhoan) {
        taiKhoan.setEnabled(false);
    }

    @Override
    public String getStateName() {
        return "UNVERIFIED";
    }
}