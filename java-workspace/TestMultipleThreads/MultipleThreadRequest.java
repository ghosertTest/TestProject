/**
 * Reimplement this request to define your own behavior.
 * 
 * @author jiawzhang
 */
public abstract class MultipleThreadRequest {
    /**
     * 1. If this method may throw exceptions, please make sure you will try-catch it.
     * 2. But, InterruptedException should always be thrown, don't try-catch it, otherwise, the consumer threads may not be shutdown correctly.
     * 
     * try {
     * } catch (InterruptedException e) {
     *     throw e;
     * } catch (Exception e) {
     *     // handling code.
     * }
     * @throws InterruptedException
     */
    public abstract void doAction() throws InterruptedException;
}
