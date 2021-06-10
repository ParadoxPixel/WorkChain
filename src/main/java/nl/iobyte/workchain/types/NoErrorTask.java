package nl.iobyte.workchain.types;

public interface NoErrorTask<U> extends Task<Exception, U> {

    /**
     * Run Task
     * @param input Input
     * @return Output
     */
    U run(Exception input);

}
