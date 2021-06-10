package nl.iobyte.workchain.thread;

import nl.iobyte.workchain.queue.QueueManager;
import nl.iobyte.workchain.queue.WorkQueue;

import java.util.ArrayList;

public class WorkThreader {

    private int workers;
    private ArrayList<WorkerThread> threads = new ArrayList<>();
    private WorkQueue manager;

    public WorkThreader(int workers) {
        this.workers = workers;
        this.manager = QueueManager.getGlobal();
    }

    public WorkThreader(int workers, WorkQueue manager) {
        this.workers = workers;
        this.manager = manager;
    }

    /**
     * Change amount of Workers
     * @param workers Integer
     */
    public void setWorkers(int workers) {
        this.workers = workers;
    }

    /**
     * Create Worker Threads
     */
    public void createWorkers() {
        if(threads.size() == workers)
            return;

        int difference = workers - threads.size();
        if(difference > 0) {
            for (int i = 0; i < difference; i++)
                threads.add(new WorkerThread(manager));

            return;
        }

        int i = 0;
        ArrayList<WorkerThread> toRemove = new ArrayList<>();
        for(WorkerThread thread : threads) {
            if(i <= difference)
                break;

            thread.stopThread();
            toRemove.add(thread);
            i--;
        }

        threads.removeAll(toRemove);
    }

    /**
     * Start Threads
     */
    public void start() {
        for(WorkerThread thread : threads)
            thread.start();
    }

    /**
     * Stop threads
     */
    public void stop() {
        for(WorkerThread thread : threads)
            thread.stopThread();
    }

}
