package alireza.ch2.consumer;

import alireza.ch2.consumer.interfaces.IConsumerHandler;

public class ShutdownHook<K,V> extends Thread{

    private IConsumerHandler<K,V> consumerHandler;
    private Thread mainThread;

    public ShutdownHook(IConsumerHandler<K,V> consumerHandler, Thread mainThread){
        this.consumerHandler = consumerHandler;
        this.mainThread = mainThread;
    }

    @Override
    public void run() {
        this.consumerHandler.getConsumer().wakeup();

        // to make sure that WakeupException should be thrown before exit.
        this.consumerHandler.setWakeupCalled(true);
        try {
            mainThread.join();
        } catch (InterruptedException ie) {
            ie.printStackTrace();
        }
    }
}
