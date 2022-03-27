package by.training.multithreading;

import by.training.multithreading.creator.CarCreator;
import by.training.multithreading.entity.Car;
import by.training.multithreading.entity.Ferry;
import by.training.multithreading.exception.CustomException;
import by.training.multithreading.test.FileResourse;
import by.training.multithreading.test.Guest;
import by.training.multithreading.test.MyThread;

import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;
import java.net.URL;
import java.util.*;
import java.util.concurrent.*;


public class Main {

    static final String PATH = ClassLoader.getSystemResource("data.properties").getPath();

    public static void main(String[] args) throws CustomException, InterruptedException, IOException {


        Ferry.getInstance();
        List<Car> listCar = CarCreator.getInstance().getListCar();
        ExecutorService service = Executors.newFixedThreadPool(listCar.size());
        for (Car car : listCar) {
            service.submit(car);
        }
        //service.shutdown();
        TimeUnit.SECONDS.sleep(8);



    }
}
