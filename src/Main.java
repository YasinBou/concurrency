import concurrency.Counter;
import concurrency.CounterLock;
import concurrency.MyRunnable;
import concurrency.MyThread;

public class Main {
    public static void main(String[] args) {

    }

    private static void implementSynchronizationWithLock() throws InterruptedException {
        CounterLock counter = new CounterLock();

        Runnable task = () -> {
            for (int i = 0; i < 1000; i++) {
                counter.increment();
            }
        };

        Thread threadOne = new Thread(task);
        Thread threadTwo = new Thread(task);

        threadOne.start();
        threadTwo.start();

        // Tells the main thread to wait for threadOne and threadTwo before continuing
        threadOne.join();
        threadTwo.join();

        System.out.println("Final count: " + counter.getCount());
    }

    private static void implementSynchronization() throws InterruptedException {
        Counter counter = new Counter();

        Runnable task = () -> {
            for (int i = 0; i < 1000; i++) {
                counter.increment();
            }
        };

        Thread threadOne = new Thread(task);
        Thread threadTwo = new Thread(task);

        threadOne.start();
        threadTwo.start();

        // Tells the main thread to wait for threadOne and threadTwo before continuing
        threadOne.join();
        threadTwo.join();

        System.out.println("Final count: " + counter.getCount());
    }

    private static void implementRunnableMethod() {
        Thread threadOne = new Thread(new MyRunnable());
        Thread threadTwo = new Thread(new MyRunnable());

        threadOne.start();
        threadTwo.start();
    }

    private static void extendThreadMethod() {
        MyThread threadOne = new MyThread();
        MyThread threadTwo = new MyThread();

        threadOne.start();
        threadTwo.start();
    }
}
