package vn.tdtu.finalterm.service.command.chinhanh;

import vn.tdtu.finalterm.service.command.AbstractCreateCommand;
import vn.tdtu.finalterm.models.ChiNhanh;
import vn.tdtu.finalterm.repositories.ChiNhanhRepository;

public class CreateChiNhanhCommand extends AbstractCreateCommand<ChiNhanh> {
    public CreateChiNhanhCommand(ChiNhanhRepository repository, ChiNhanh chiNhanh) {
        super(repository, chiNhanh);
    }

    @Override
    public void execute() {
        if (validateChiNhanh()) {
            super.execute();
        }
    }

    private boolean validateChiNhanh() {
        return true;
    }
}