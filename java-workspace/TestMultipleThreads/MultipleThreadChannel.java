import java.util.LinkedList;

/**
 * Add mutiple thread to speed up batch job process.
 * TODO: Add producer threads and stop alive threads function.
 * @author jiawzhang
 */
public class MultipleThreadChannel {
	
	private LinkedList<MultipleThreadRequest> queue = new LinkedList<MultipleThreadRequest>();
	
	private boolean noMoreRequest = false;
	
	private ConsumerThread[] consumerThreads = null;
    
	private ProducerThread[] producerThreads = null;
	
	public MultipleThreadChannel() {
	}
	
	public synchronized void put(MultipleThreadRequest request) {
		this.queue.add(request);
		this.notifyAll();
	}
	
	private synchronized MultipleThreadRequest get() throws InterruptedException {
		while (this.queue.size() <= 0) {
			if (noMoreRequest == false) {
		        this.wait();
			} else {
				return null;
			}
		}
        MultipleThreadRequest request = this.queue.removeFirst();
		return request;
	}
	
	public synchronized int size() {
		return this.queue.size();
	}
    
	/**
	 * Invoke this method after all the requests have been put into queue.
	 * In other words, don't invoke this method before put(Request) method above.
	 * This method will make sure all the consumer threads will exist after all the requests in the queue are consumed.
	 */
	public synchronized void stopConsumerWithoutRequest() {
		this.noMoreRequest = true;
		this.notifyAll();
	}
	
	/**
	 * Invoke this method after invoking stopConsumerWitoutRequest() method only.
	 * This will pend the main thread until all the consumer threads exist.
	 */
	public void waitingForConsumerExist() {
		try {
			for (int i = 0; i < this.consumerThreads.length; i++) {
				this.consumerThreads[i].join();
		    }
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
        this.noMoreRequest = false;
	}
	
    
	public void startConsumer(int iMaxThreads) {
		this.consumerThreads = new ConsumerThread[iMaxThreads];
		for (int i = 0; i < this.consumerThreads.length; i++) {
			this.consumerThreads[i] = new ConsumerThread(this);
			this.consumerThreads[i].start();
		}
	}
    
	public void stopConsumer() {
		for (int i = 0; i < this.consumerThreads.length; i++) {
			this.consumerThreads[i].shutdown();
		}
    }
	
	/**
	 * Consumer Thread Class, get Request and invoke Reqeust.doAction() automatically.
	 * @author jiawzhang
	 */
	private class ConsumerThread extends Thread {
        
        private boolean shutdownSign = false;
		
		private MultipleThreadChannel channel = null;
        
        public void shutdown() {
            shutdownSign = true;
            interrupt();
        }
        
        public boolean isShutdown() {
            return shutdownSign;
        }
		
		public ConsumerThread(MultipleThreadChannel channel) {
			this.channel = channel;
		}
		
		public void run() {
    		try {
    	        while (!shutdownSign) {
                    MultipleThreadRequest request = this.channel.get();
    			    if (request == null) break;
                    try {
    			        request.doAction();
                    } catch (InterruptedException e) {
                        throw e;
                    } catch (Exception e) {
                        System.out.println("Unexpected exception happen, please try catch it in MultipleThreadRequest.doAction() method.");
                        e.printStackTrace();
                    }
    	        }
    		} catch (InterruptedException e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    		}
		}
	}
    
	public void waitingForProducerExist() {
		try {
			for (int i = 0; i < this.producerThreads.length; i++) {
				this.producerThreads[i].join();
		    }
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
    
    /**
     * Start producer threads rather than using main thread as producer thread. Because:
     * 1. Producer thread may be a polling thread which is alive.
     * 2. Producer thread may be several ones, not single one.
     * 3. The main thread may be a UI thread which can not be blocked too much time. Thus, need a dedicate producer thread instead.
     * Otherwise, this method is not necessary to invoke.
     * @param producers: the number of the producers array will decide the number of producer threads.
     */
	public void startProducer(MultipleThreadRequestProducer[] producers) {
		this.producerThreads = new ProducerThread[producers.length];
		for (int i = 0; i < this.producerThreads.length; i++) {
			this.producerThreads[i] = new ProducerThread(this, producers[i]);
			this.producerThreads[i].start();
		}
	}
    
	public void stopProducer() {
		for (int i = 0; i < this.producerThreads.length; i++) {
			this.producerThreads[i].shutdown();
		}
    }
    
    private class ProducerThread extends Thread {
        
        private boolean shutdownSign = false;
		
		private MultipleThreadChannel channel = null;
        
        public void shutdown() {
            shutdownSign = true;
            interrupt();
        }
        
        public boolean isShutdown() {
            return shutdownSign;
        }
		
        private MultipleThreadRequestProducer producer = null;
		
		public ProducerThread(MultipleThreadChannel channel, MultipleThreadRequestProducer producer) {
			this.channel = channel;
            this.producer = producer;
		}
		
		public void run() {
    		try {
    	        while (!shutdownSign) {
                    try {
                        if (this.producer.process(this.channel)) break;
                    } catch (InterruptedException e) {
                        throw e;
                    } catch (Exception e) {
                        System.out.println("Unexpected exception happen, please try catch it in MultipleThreadRequestProducer.process(channel) method.");
                        e.printStackTrace();
                    }
    	        }
    		} catch (InterruptedException e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    		}
		}
        
    }
}
