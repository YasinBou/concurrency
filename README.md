# Concurrency

I created this repository to document my findings regarding concurrency within Java.
I will discuss the following topics:

## Key Topics
1. What is concurrency?
2. Creating threads in Java
    * Extending the Thread Class
    * Implementing the Runnable Interface
3. Thread Lifecycle
4. Synchronization
    * Synchronized methods
    * Synchronized blocks
5. Thread safety with locks
6. Executor framework
7. Questions and answers

### 1. What is concurrency?
Concurrency allows your computer to run multiple tasks simultaneously. For example, if we take a look at Spotify,
we can observe that it does multiple things at once. It reads digital audio off the network, decompresses it, manages playback and
updates its display all at the same time. Most modern programming frameworks like Spring Boot or Django use concurrency internally without
us even knowing about it. As you can see, concurrency is everywhere.

Java provides a great amount of support for concurrency through its threading model. This enables
developers to execute tasks in parallel, manage shared resources and handle sycnhronization.

**Key concepts**
* **Thread:** A thread is nothing more than a single unit of execution that runs independently within a program.
Multiple threads can run simultaneously, sharing the same memory but executing different instructions.
* **Concurrency:** Concurrency is the ability of a computer or program to handle multiple tasks at the same time.
* **Thread Safety:** Thread safety means that a program or a pieceof code behaves correctly when accessed
by multiple threads at the same time, without causing incorrect results or unexpected behavior.

---

### 2. Creating threads in Java
There are two ways to create a thread in Java. You can either extend the Thread class or implement the
Runnable interface.

**Extending**<br>
When you extend the Thread class, you can override its run() method and create your own implementation.

```java
public class MyThread extends Thread {
    @Override
    public void run() {
        for (int i = 1; i <= 10; i++) {
            System.out.println(Thread.currentThread().getName() + " printing: " + i);
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                System.out.println("Thread interrupted: " + e.getMessage());
            }
        }
    }
}
```

The MyThread class inherits from Thread and implements its work inside the run() method,
and calling start() launches the thread on its own execution flow, producing
mixed output from thread one and thread two. This is a display of concurrent
behavior.

```java
public class Main {
    public static void main(String[] args) {
        extendThreadMethod();
    }

    private static void extendThreadMethod() {
        MyThread threadOne = new MyThread();
        MyThread threadTwo = new MyThread();

        threadOne.start();
        threadTwo.start();
    }
}
```

**Mixed output**<br>
As you can see, the output is mixed because both threads run at the same time, and the CPU
switches between them while they are executing, so their print statement appear mixed instead of one thread
finishing completely before the other.
```
Thread-1 printing: 1
Thread-0 printing: 1
Thread-0 printing: 2
Thread-1 printing: 2
Thread-0 printing: 3
Thread-1 printing: 3
Thread-0 printing: 4
Thread-1 printing: 4
Thread-1 printing: 5
Thread-0 printing: 5
Thread-0 printing: 6
Thread-1 printing: 6
Thread-0 printing: 7
Thread-1 printing: 7
Thread-0 printing: 8
Thread-1 printing: 8
Thread-0 printing: 9
Thread-1 printing: 9
Thread-0 printing: 10
Thread-1 printing: 10
```

**Runnable interface**<br>
If your class already extends another class then you can use the runnable interface instead.

```java
public class MyRunnable implements Runnable {
    @Override
    public void run() {
        for (int i = 1; i <= 10; i++) {
            System.out.println(Thread.currentThread().getName() + " printing: " + i);
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                System.out.println("Thread interrupted: " + e.getMessage());
            }
        }
    }
}
```

If you use the Runnable interface, you cannot call the start() method on the class
that implements Runnable, because Runnable does not define a start() method.
Instead, you must create a Thread object and pass the Runnable instance to the Thread constructor.
Then you call start() on the Thread object.

```java
public class Main {
    public static void main(String[] args) {
        implementRunnableMethod();
    }
    
    private static void implementRunnableMethod() {
        Thread threadOne = new Thread(new MyRunnable());
        Thread threadTwo = new Thread(new MyRunnable());

        threadOne.start();
        threadTwo.start();
    }
}
```

**Mixed output (again)**<br>
The same thing applies here: because both threads run concurrently and the CPU switches between them,
their output may be mixed.

```
Thread-0 printing: 1
Thread-1 printing: 1
Thread-1 printing: 2
Thread-0 printing: 2
Thread-1 printing: 3
Thread-0 printing: 3
Thread-1 printing: 4
Thread-0 printing: 4
Thread-1 printing: 5
Thread-0 printing: 5
Thread-0 printing: 6
Thread-1 printing: 6
Thread-1 printing: 7
Thread-0 printing: 7
Thread-0 printing: 8
Thread-1 printing: 8
Thread-1 printing: 9
Thread-0 printing: 9
Thread-1 printing: 10
Thread-0 printing: 10
```
---

### 3.Thread lifecycle

In java, a thread goes through multiple states.
* **New:** A new thread has been created but has not been started yet.
* **Runnable:** The thread is ready to run or is running.
* **Blocked/Waiting:** The thread is waiting for a resource or monitor lock.
* **Timed waiting:** The thread is waiting because a timeout method of some sorts has been called, for example; sleep().
* **Terminated:** The thread has been terminated or completed its execution.

Understand these concepts helps with managing threads effectively.
---

### 4. Synchronization
When several threads operate on the same shared resource (such as a counter), they may conflict
with one another, leading to race conditions or unpredictable outcomes. Synchronization prevents this
by allowing only a single thread to execute the critical portion of code at any given moment.
In java, this behavior is achieved using the **synchronized** keyword.

**Synchronized methods**<br>
The **synchronized** keyword will ensure that only thread executes a method at a time.

```java
public class Counter {
    private int count = 0;

    public synchronized void increment() {
        count++;
    }

    public synchronized int getCount() {
        return this.count;
    }
}
```
```java
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

    threadOne.join();
    threadTwo.join();

    System.out.println("Final count: " + counter.getCount());
}
```

**Explanation:**<br>
The increment() method is now synchronized, which means that it will now ensure thread-safe updates.
Without synchronization, the output will likely be less than 2000 due to **race conditions**.
Our solution will ensure that our output remains consistent. join() makes the main thread
wait for threadOne and threadTwo to finish before it continues,

**Synchronized blocks**<br>
If you prefer more flexibility and finer control, you can use synchronized blocks to
lock specific parts of your methods.

```java
public class CounterLock {
    private int count = 0;
    private final Object lock = new Object();

    public void increment() {
        synchronized (lock) {
            count++;
        }
    }

    public int getCount() {
        synchronized (lock) {
            return count;
        }
    }
}
```

Now you know that the synchronized lock ensures that only one thread can modify the count at a time!

---