package vn.tdtu.finalterm.service.command;

public interface CRUDCommand<T> {
    void execute();
    void undo();
    T getResult();
}