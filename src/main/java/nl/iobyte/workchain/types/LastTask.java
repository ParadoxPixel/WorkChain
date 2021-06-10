package nl.iobyte.workchain.types;

public interface LastTask<U> extends Task<U, Object> {

    @Override
    default Object run(U input) throws Exception {
        runLast(input);
        return null;
    }

    /**
     * Run Task
     * @param input Input
     */
    void runLast(U input) throws Exception;

}