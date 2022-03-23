package by.training.multithreading.test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class MyThread extends Thread {


    @Override
    public void run() {
        int sum = 0;
        for (int i = 0; i < 5; i++) {
            sum += i;
            System.out.println("myThread: "+i);
            try {
                TimeUnit.MILLISECONDS.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
