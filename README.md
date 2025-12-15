# Concurrency

I created this repository to document my findings regarding concurrency within Java.
I will discuss the following topics:

## Key Topics
1. What is concurrency?
2. Creating Threads in Java
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