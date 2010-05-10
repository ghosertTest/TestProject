import junit.framework.TestCase;


/*
 * Created on Sep 24, 2009
 */

/**
 * @author jiawzhang
 */

public class MultipleThreadChannelTester extends TestCase {
    
    // Initialize the Mutilple Threads Util class.
    private MultipleThreadChannel channel = new MultipleThreadChannel();
    
    public void testMainAsProducerThread() {
            
        // Start with 5 consumer threads.
        channel.startConsumer(5);
            
        // Put 25 * 2 heavy jobs into the thread request queue.
        for (int i = 0; i < 25; i++) {
            
            // Inner class for request.
            MultipleThreadRequest request = new MultipleThreadRequest() {
                @Override
                public void doAction() throws InterruptedException {
                    System.out.println(Thread.currentThread().getName() + " Doing heavy job here...");
                    Thread.sleep(1000);
                }
            };
            
            // put inner request class.
            channel.put(request);
                
            // put self-defined request class.
            channel.put(new MyRequest("jiawei", "zhang"));
        }
            
        // Set a signal here to tell all the consumer threads:
        // There is no following requests will be put to request queue any more.
        channel.stopConsumerWithoutRequest();
            
        // Pending the main thread here until all the consumer threads complete their jobs.
        channel.waitingForConsumerExist(); 
    }
    
    public void testSpecifiedProducerThread() {
        
        System.out.println("Begin to test MultipleThreadRequestProducer ...");
        
        // Test MultipleThreadRequestProducer.
        channel.startConsumer(5);
        
        // start producer threads rather than using main thread, because producer thread may be a polling thread which is alive.
        channel.startProducer(new MyProducer[]{new MyProducer()}); 
        
        System.out.println("Waiting for producer threads exist ...");
        
        channel.waitingForProducerExist();
    }
}

/**
 * Self redefined thread request class for user data and behavior.
 * @author Zhang Jiawei
 *
 */
class MyRequest extends MultipleThreadRequest {
        
    private String arg1;
    private String arg2;
        
    public MyRequest(String arg1, String arg2) {
        this.arg1 = arg1;
        this.arg2 = arg2;
    }
    
    @Override
    public void doAction() throws InterruptedException {
        System.out.println(Thread.currentThread().getName() + " Doing heavy job here..." + " with arg1: " + arg1 + " arg2: " + arg2);
        Thread.sleep(1000);
    }
}

class MyProducer extends MultipleThreadRequestProducer {
    
    @Override
    public boolean process(MultipleThreadChannel channel) throws InterruptedException {
        
        // Put 25 * 2 heavy jobs into the thread request queue.
        for (int i = 0; i < 25; i++) {
            
            // Inner class for request.
            MultipleThreadRequest request = new MultipleThreadRequest() {
                @Override
                public void doAction() throws InterruptedException {
                    System.out.println(Thread.currentThread().getName() + " Doing heavy job here...");
                    Thread.sleep(1000);
                }
            };
            channel.put(request);
        }
        
        channel.stopConsumerWithoutRequest();
        channel.waitingForConsumerExist();
        
        // Break and kill the producer thread out sider.
        return true;
    }
}
	