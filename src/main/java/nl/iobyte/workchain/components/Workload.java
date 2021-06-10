package nl.iobyte.workchain.components;

public class Workload<T> {

    /**
     * Needed Data
     */
    private Work<T, ?> work;
    private T data;

    public Workload(Work<T, ?> work, T data) {
        this.work = work;
        this.data = data;
    }

    /**
     * Get work
     * @return Work
     */
    public Work<T, ?> getWork() {
        return work;
    }

    /**
     * Get provided data
     * @return Data
     */
    public T getData() {
        return data;
    }

    /**
     * Perform Task
     */
    public void compute() {
        work.execute(data);
    }

}
