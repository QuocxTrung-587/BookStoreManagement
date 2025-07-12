package vn.tdtu.finalterm.service.command;

import org.springframework.stereotype.Component;
import java.util.Stack;

@Component
public class AdminCommandInvoker {
    private final Stack<CRUDCommand<?>> commandHistory = new Stack<>();

    public <T> T executeCommand(CRUDCommand<T> command) {
        command.execute();
        commandHistory.push(command);
        return command.getResult();
    }

    public void undoLastCommand() {
        if (!commandHistory.isEmpty()) {
            CRUDCommand<?> lastCommand = commandHistory.pop();
            lastCommand.undo();
        }
    }
}