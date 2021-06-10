package nl.iobyte.workchain.types;

public interface Task<T, U> {

    /**
     * Run Task
     * @param input Input
     * @return Output
     */
    U run(T input) throws Exception;

}
