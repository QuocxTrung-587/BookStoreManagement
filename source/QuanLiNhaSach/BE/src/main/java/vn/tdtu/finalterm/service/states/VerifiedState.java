package vn.tdtu.finalterm.service.states;

import vn.tdtu.finalterm.models.TaiKhoan;

public class VerifiedState implements AccountState {
    @Override
    public void handleState(TaiKhoan taiKhoan) {
        taiKhoan.setEnabled(true);
    }

    @Override
    public String getStateName() {
        return "VERIFIED";
    }
}