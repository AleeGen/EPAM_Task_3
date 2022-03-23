package by.training.multithreading.test;

import java.time.LocalTime;
import java.util.concurrent.TimeUnit;


public class BlockSyn extends Thread {
    FileResourse a;
    FileResourse b;

    public BlockSyn(FileResourse a, FileResourse b) {
        this.a = a;
        this.b = b;
    }

    @Override
    public void run() {
        synchronized (a) {
            System.out.println("Start Block " + a + " " + Thread.currentThread().getName() + " time: " + LocalTime.now());
            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("End Block " + a + " " + Thread.currentThread().getName() + " time: " + LocalTime.now());
        }
        synchronized (b) {
            System.out.println("Start Block " + b + " " + Thread.currentThread().getName() + " time: " + LocalTime.now());
            System.out.println("End Block " + b + " " + Thread.currentThread().getName() + " time: " + LocalTime.now());
        }
    }
}
