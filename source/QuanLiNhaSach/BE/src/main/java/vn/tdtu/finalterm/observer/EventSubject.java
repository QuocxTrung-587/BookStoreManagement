package vn.tdtu.finalterm.observer;

public interface EventSubject {
    void addListener(EventListener listener);
    void removeListener(EventListener listener);
    void notifyListeners(String eventType, Object data);
}