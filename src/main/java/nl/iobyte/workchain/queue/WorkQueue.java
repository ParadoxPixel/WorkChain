package nl.iobyte.workchain.queue;

import nl.iobyte.workchain.components.Workload;
import java.util.concurrent.ConcurrentLinkedQueue;

public class WorkQueue {

    private String id;
    private ConcurrentLinkedQueue<Workload<?>> work = new ConcurrentLinkedQueue<>();

    public WorkQueue(String id) {
        this.id = id;
    }

    /**
     * Get Queue ID
     * @return String
     */
    public String getID() {
        return id;
    }

    /**
     * Add workload to queue
     * @param workload Workload
     */
    public void schedule(Workload<?> workload) {
        work.add(workload);
    }

    /**
     * Return if has Work
     * @return boolean
     */
    public boolean hasWork() {
        return !work.isEmpty();
    }

    /**
     * Get work at start of queue
     * @return Workload
     */
    public Workload<?> getWork() {
        return work.poll();
    }

}
