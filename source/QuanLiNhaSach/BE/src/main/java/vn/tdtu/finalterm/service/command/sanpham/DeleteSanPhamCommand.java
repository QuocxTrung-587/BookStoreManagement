package vn.tdtu.finalterm.service.command.sanpham;

import vn.tdtu.finalterm.service.command.CRUDCommand;
import vn.tdtu.finalterm.models.SanPham;
import vn.tdtu.finalterm.repositories.SanPhamRepository;

public class DeleteSanPhamCommand implements CRUDCommand<Void> {
    private final SanPhamRepository repository;
    private final Long id;
    private SanPham deletedSanPham;

    public DeleteSanPhamCommand(SanPhamRepository repository, Long id) {
        this.repository = repository;
        this.id = id;
    }

    @Override
    public void execute() {
        deletedSanPham = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("SanPham not found"));
        repository.deleteById(id);
    }

    @Override
    public void undo() {
        if (deletedSanPham != null) {
            repository.save(deletedSanPham);
        }
    }

    @Override
    public Void getResult() {
        return null;
    }
}