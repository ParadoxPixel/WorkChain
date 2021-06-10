package nl.iobyte.workchain.types;

public interface ErrorTask extends Task<Exception, Object> {

    @Override
    default Object run(Exception input) {
        runLast(input);
        return null;
    }

    /**
     * Run Task
     * @param input Input
     */
    void runLast(Exception input);

}