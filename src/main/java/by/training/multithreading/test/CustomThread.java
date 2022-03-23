package by.training.multithreading.test;

import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

public class CustomThread implements Callable<Integer> {

    @Override
    public Integer call() {
        int sum = 0;
        for (int i = 0; i < 5; i++) {
            sum += i;
            System.out.println("call: "+i);
            try {
                TimeUnit.MILLISECONDS.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        try {
            TimeUnit.SECONDS.sleep(5);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return sum;
    }
}
