package by.training.multithreading.test;

import java.util.Locale;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class Guest {

    ReentrantLock lock = new ReentrantLock();
    int count;

    public Guest() {
    }

    public void invite() {
        if (lock.tryLock()) {
            try {
                System.out.println(Thread.currentThread().getName() + " invite start");
            } finally {
                try {
                    TimeUnit.MILLISECONDS.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(Thread.currentThread().getName() + " unlock start");
                System.out.println(Thread.currentThread().getName() + " unlock end");
                lock.unlock();
                System.out.println(Thread.currentThread().getName() + " invite end");
            }
        }
    }

}
