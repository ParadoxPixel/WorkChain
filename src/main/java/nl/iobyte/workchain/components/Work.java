package nl.iobyte.workchain.components;

import nl.iobyte.workchain.queue.QueueManager;
import nl.iobyte.workchain.queue.WorkQueue;
import nl.iobyte.workchain.types.*;

public class Work<T, R> {

    /**
     * Parameters needed to get Parent, Start, Child, End and Task
     */
    private Work<?, T> parent;
    private Work<R, ?> child;
    private Task<T, R> task;
    private boolean fullExecute = false;
    private WorkQueue manager;

    /**
     * If null abort
     */
    private boolean nullAbort = false;
    private GeneralTask abort;

    /**
     * Handle error
     */
    private ErrorTask global_error;
    private NoErrorTask<R> error;

    public Work(Work<?, T> parent, Task<T, R> task) {
        this.parent = parent;
        this.task = task;
        this.manager = QueueManager.getGlobal();
    }

    public Work(Work<?, T> parent, Task<T, R> task, WorkQueue manager) {
        this.parent = parent;
        this.task = task;
        this.manager = manager;
    }

    /**
     * Get parent
     * @return Work
     */
    public Work<?, T> getParent() {
        return parent;
    }

    /**
     * Get start of Chain
     * @return Work
     */
    public Work<?, ?> getStart() {
        if(parent == null)
            return this;

        return parent.getStart();
    }

    /**
     * Get child
     * @return Work
     */
    public Work<R, ?> getChild() {
        return child;
    }

    /**
     * Check if Work is Child
     * @return Work
     */
    public boolean isChild() {
        return parent != null;
    }

    /**
     * Get end of Work Chain
     * @return Work
     */
    public Work<?, ?> getEnd() {
        if(child == null)
            return this;

        return child.getEnd();
    }

    /**
     * Enable abort on null
     * @return Work
     */
    public Work<T, R> abortIfNull() {
        return abortIfNull(null);
    }

    /**
     * Task to execute on null
     * @param task GeneralTask
     * @return Work
     */
    public Work<T, R> abortIfNull(GeneralTask task) {
        nullAbort = true;
        abort = task;
        return this;
    }

    /**
     * On Exception
     * @param task ErrorTask
     * @return Work
     */
    public Work<T, R> onException(NoErrorTask<R> task) {
        error = task;
        return this;
    }

    /**
     * On Global Exception
     * @param task ErrorTask
     * @return Work
     */
    public Work<T, R> onGlobalException(ErrorTask task) {
        getStart().global_error = task;
        return this;
    }

    /**
     * Get Task
     * @return Task
     */
    public Task<T, ?> getTask() {
        return task;
    }

    /**
     * If set full execute is true perform whole chain in one piece
     * @param b boolean
     * @return Work
     */
    public Work<T, R> setFullExecute(boolean b) {
        if(isChild()) {
            getStart().setFullExecute(b);
            return this;
        }

        fullExecute = b;
        return this;
    }

    /**
     * Set Queue Manager
     * @param manager WorkQueue
     * @return Work
     */
    public Work<T, R> setQueue(WorkQueue manager) {
        if(isChild()) {
            getStart().setQueue(manager);
            return this;
        }

        this.manager = manager;
        if(child != null)
            child.setInternalQueue(manager);

        return this;
    }

    /**
     * Set Queue Manager for Children
     * @param manager WorkQueue
     */
    private void setInternalQueue(WorkQueue manager) {
        this.manager = manager;
        if(child != null)
            child.setQueue(manager);
    }

    /**
     * Get new Work Chain
     * @param task FirstTask
     * @param <U> Output Type
     * @return Work
     */
    public static <U> Work<Object, U> firstTask(FirstTask<U> task) {
        return new Work<>(null, task);
    }

    /**
     * Get new Work Chain
     * @param task FirstTask
     * @param <U> Output Type
     * @return Work
     */
    public static <U> Work<Object, U> firstTask(FirstTask<U> task, WorkQueue manager) {
        return new Work<>(null, task, manager);
    }

    /**
     * Add task to chain
     * @param task Task
     * @param <U> Output Type
     * @return Work
     */
    public <U> Work<R, U> nextTask(Task<R, U> task) {
        Work<R, U> child = new Work<>(this, task, manager);
        this.child = child;
        return child;
    }

    /**
     * Add end to chain
     * @param task LastTask
     * @return Work
     */
    public Work<R, Object> lastTask(LastTask<R> task) {
        Work<R, Object> child = new Work<>(this, task, manager);
        this.child = child;
        return child;
    }

    /**
     * Execute Chain
     */
    public void execute() {
        if(isChild()) {
            getStart().execute();
            return;
        }

        if(manager == null)
            return;

        Workload<T> load = new Workload<>(this, null);
        manager.schedule(load);
    }

    /**
     * Execute current worker and add new Work to queue
     * @param input Input
     */
    public void execute(T input) {
        if(manager == null)
            return;

        try {
            if (task instanceof LastTask) {
                task.run(input);
                return;
            }

            R output = task.run(input);
            if (nullAbort && output == null) {
                if (abort != null)
                    abort.runGeneral();
                return;
            }

            if (child == null)
                return;

            if(!fullExecute) {
                Workload<R> load = new Workload<>(child, output);
                manager.schedule(load);
            } else {
                child.execute(output);
            }
        } catch (Exception e) {
            if(error != null) {
                R output = error.run(e);
                if (child == null)
                    return;

                if(!fullExecute) {
                    Workload<R> load = new Workload<>(child, output);
                    manager.schedule(load);
                } else {
                    child.execute(output);
                }
                return;
            }

            ErrorTask task = getStart().global_error;
            if(task == null) {
                e.printStackTrace();
                return;
            }

            task.runLast(e);
        }
    }

}
