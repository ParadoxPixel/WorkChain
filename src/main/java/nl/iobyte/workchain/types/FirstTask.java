package nl.iobyte.workchain.types;

public interface FirstTask<U> extends Task<Object, U> {

    @Override
    default U run(Object input) throws Exception {
        return runFirst();
    }

    /**
     * Run Task
     * @return Ouput
     */
    U runFirst() throws Exception;

}
