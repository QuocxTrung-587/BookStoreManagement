package vn.tdtu.finalterm.service.command.sanpham;

import vn.tdtu.finalterm.service.command.CRUDCommand;
import vn.tdtu.finalterm.models.SanPham;
import vn.tdtu.finalterm.repositories.SanPhamRepository;

public class UpdateSanPhamCommand implements CRUDCommand<SanPham> {
    private final SanPhamRepository repository;
    private final Long id;
    private final SanPham newSanPham;
    private SanPham oldSanPham;
    private SanPham result;

    public UpdateSanPhamCommand(SanPhamRepository repository, Long id, SanPham newSanPham) {
        this.repository = repository;
        this.id = id;
        this.newSanPham = newSanPham;
    }

    @Override
    public void execute() {
        oldSanPham = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("SanPham not found"));
        newSanPham.setId(id);
        result = repository.save(newSanPham);
    }

    @Override
    public void undo() {
        if (oldSanPham != null) {
            repository.save(oldSanPham);
        }
    }

    @Override
    public SanPham getResult() {
        return result;
    }
}