package nl.iobyte.workchain.thread;

import nl.iobyte.workchain.queue.WorkQueue;
import nl.iobyte.workchain.components.Workload;

public class WorkerThread extends Thread {

    private boolean running = false;
    private WorkQueue manager;

    public WorkerThread(WorkQueue manager) {
        this.manager = manager;
    }

    public void stopThread() {
        running = false;
    }

    @Override
    public final void run() {
        if(manager == null)
            return;

        if(running)
            return;

        running = true;
        while(running)
            work();
    }

    /**
     * Try to get work then execute, sleep if there's no Work
     */
    public void work() {
        Workload<?> load = manager.getWork();
        if(load == null) {
            try {
                sleep(50);
            } catch (Exception e) {
                e.printStackTrace();
            }

            return;
        }

        load.compute();
    }

}
