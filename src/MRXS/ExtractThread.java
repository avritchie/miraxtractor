package MRXS;

import java.awt.Taskbar;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;
import javax.swing.SwingWorker;

public abstract class ExtractThread extends SwingWorker<Void, Integer> {

    MRXS frame;
    MRXSLevel level;
    int id;
    Taskbar tbar;
    public List<Exception> exceptionList;
    boolean win;

    public ExtractThread(MRXSLevel level, int progressID) {
        this.frame = MRXS.frame;
        this.level = level;
        this.id = progressID;
        this.win = System.getProperty("os.name").toLowerCase().contains("windows");
        this.exceptionList = new ArrayList<>();
        try {
            tbar = Taskbar.getTaskbar();
        } catch (NoClassDefFoundError | RuntimeException ex) {
            System.out.println("Taskbar not found");
        }
    }

    @Override
    protected Void doInBackground() throws IOException {
        if (MRXS.syncCounter != null) {
            frame.buttonsEnabled(false);
            frame.progressBar.setMinimum(0);
            frame.progressBar.setMaximum(1000);
        }
        core();
        return null;
    }

    @Override
    protected void process(List<Integer> chunks) {
        int i = (chunks.get(chunks.size() - 1));
        if (MRXS.syncCounter != null) {
            MRXS.syncCounter[id] = i;
            int value = IntStream.of(MRXS.syncCounter).sum() / MRXS.syncCounter.length;
            frame.progressBar.setValue(value);
            if (tbar != null & win) {
                tbar.setWindowProgressValue(frame, value / 10);
            }
        }
        super.process(chunks);
    }

    @Override
    protected void done() {
        if (MRXS.syncCounter != null) {
            MRXS.syncCounter[id] = 1000;
            if (IntStream.of(MRXS.syncCounter).sum() / MRXS.syncCounter.length == 1000) {
                frame.progressBar.setValue(frame.progressBar.getMaximum());
                frame.buttonsEnabled(true);
                frame.readyMessage();
                if (tbar != null & win) {
                    tbar.setWindowProgressValue(frame, -1);
                }
            }
            if (!exceptionList.isEmpty()) {
                MRXS.error(exceptionList.get(0));
            }
        }
    }

    protected abstract void core();
}
