package by.training.multithreading.test;

import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class FileResourse {
    FileWriter writer;
    String path;

    public FileResourse(String path) {
        this.path = path;
    }

    public synchronized void wr(int i) {
        try {
            writer = new FileWriter(path, true);
            writer.write(Thread.currentThread().getName() + " : " + i + " -> Start" + "\n");
            System.out.println((Thread.currentThread().getName() + " : " + i + " -> Start"));
            // TimeUnit.SECONDS.sleep(1);
            writer.write(Thread.currentThread().getName() + " : " + i + " -> End" + "\n");
            System.out.println((Thread.currentThread().getName() + " : " + i + " -> End"));
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
