package vn.tdtu.finalterm.service.command.sanpham;

import vn.tdtu.finalterm.service.command.AbstractCreateCommand;
import vn.tdtu.finalterm.models.SanPham;
import vn.tdtu.finalterm.repositories.SanPhamRepository;

public class CreateSanPhamCommand extends AbstractCreateCommand<SanPham> {
    public CreateSanPhamCommand(SanPhamRepository repository, SanPham sanPham) {
        super(repository, sanPham);
    }

    @Override
    public void execute() {
        if (validateSanPham()) {
            super.execute();
        }
    }

    private boolean validateSanPham() {
        return true;
    }
}