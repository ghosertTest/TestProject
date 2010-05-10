
public abstract class MultipleThreadRequestProducer {
    
    /**
     * Implement this method to create MultipleThreadRequest and put it to channel or add Sleep method here.
     * All the logic in this method will be run by producer threads in MultipleThreadChannel class.
     * 
     * 1. If this method may throw exceptions, please make sure you will try-catch it.
     * 2. But, InterruptedException should always be thrown, don't try-catch it, otherwise, the producer threads may not be shutdown correctly.
     * 
     * try {
     * } catch (InterruptedException e) {
     *     throw e;
     * } catch (Exception e) {
     *     // handling code.
     * }
     * @param channel MultipleThreadChannel 
     * @return isBreakProcess: false means continue while true means break.
     * @throws InterruptedException Don't catch this exception, just let it be.
     */
    public abstract boolean process(MultipleThreadChannel channel) throws InterruptedException;
}
