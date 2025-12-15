import concurrency.MyRunnable;
import concurrency.MyThread;

public class Main {
    public static void main(String[] args) {

    }

    private static void extendThreadMethod() {
        MyThread threadOne = new MyThread();
        MyThread threadTwo = new MyThread();

        threadOne.start();
        threadTwo.start();
    }

    private static void implementRunnableMethod() {
        Thread threadOne = new Thread(new MyRunnable());
        Thread threadTwo = new Thread(new MyRunnable());

        threadOne.start();
        threadTwo.start();
    }
}
