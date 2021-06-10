package nl.iobyte.workchain.queue;

import java.util.HashMap;

public class QueueManager {

    private static WorkQueue global = new WorkQueue("global");
    private static HashMap<String, WorkQueue> managers = new HashMap<>();

    public static WorkQueue getGlobal() {
        return global;
    }

    public static WorkQueue addQueue(String id) {
        if(id == null || id.isEmpty())
            return null;

        if(managers.containsKey(id))
            return null;

        WorkQueue queue = new WorkQueue(id);
        managers.put(id, queue);
        return queue;
    }

    public static WorkQueue getQueue(String id) {
        if(id == null || id.isEmpty())
            return null;

        return managers.get(id);
    }

    public static boolean hasQueue(String id) {
        if(id == null || id.isEmpty())
            return false;

        return managers.containsKey(id);
    }

    public static void removeQueue(String id) {
        if(id == null || id.isEmpty())
            return;

        managers.remove(id);
    }

}
