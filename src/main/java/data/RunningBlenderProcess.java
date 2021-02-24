package data;

import util.core.socket.MySocketConnection;
import com.intellij.execution.process.OSProcessHandler;
import com.intellij.execution.process.ProcessHandler;

import javax.swing.*;

public class RunningBlenderProcess {

    private final BlenderInstance instance;
    private final OSProcessHandler processHandler;
    private final boolean debugMode;

    private MySocketConnection socket;
    private JComponent component;

    public RunningBlenderProcess(BlenderInstance instance, OSProcessHandler processHandler, boolean debugMode) {
        this.instance = instance;
        this.processHandler = processHandler;
        this.debugMode = debugMode;
        this.component = new JPanel();
    }

    public String getProcessName() {
        return instance.name;
    }

    public boolean isDebug() {
        return debugMode;
    }

    public ProcessHandler getProcess() {
        return processHandler;
    }

    public BlenderInstance getInstance() {
        return instance;
    }

    public void assignSocket(MySocketConnection socket) {
        this.socket = socket;
    }

    public MySocketConnection getSocket() {
        return socket;
    }

    public JComponent getComponent() {
        return component;
    }

    public void setComponent(JComponent component) {
        this.component = component;
    }
}
