package vn.tdtu.finalterm.service.command;

import org.springframework.data.jpa.repository.JpaRepository;

public abstract class AbstractCreateCommand<T> implements CRUDCommand<T> {
    protected final JpaRepository<T, ?> repository;
    protected final T entity;
    protected T result;

    public AbstractCreateCommand(JpaRepository<T, ?> repository, T entity) {
        this.repository = repository;
        this.entity = entity;
    }

    @Override
    public void execute() {
        result = repository.save(entity);
    }

    @Override
    public void undo() {
        if (result != null) {
            repository.delete(result);
        }
    }

    @Override
    public T getResult() {
        return result;
    }
}