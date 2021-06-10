package nl.iobyte.workchain.types;

public interface GeneralTask extends Task<Object, Object> {

    @Override
    default Object run(Object input) throws Exception {
        runGeneral();
        return null;
    }

    /**
     * Run Task
     */
    void runGeneral() throws Exception;

}
