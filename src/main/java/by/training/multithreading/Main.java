package by.training.multithreading;

import by.training.multithreading.creator.CarCreator;
import by.training.multithreading.entity.Car;
import by.training.multithreading.entity.Ferry;
import by.training.multithreading.exception.CustomException;

import java.util.*;
import java.util.concurrent.*;



public class Main {

    public static void main(String[] args) throws CustomException {
        Ferry.getInstance();
        List<Car> listCar = CarCreator.getInstance().getListCar();
        ExecutorService service = Executors.newFixedThreadPool(listCar.size());
        for (Car car : listCar) {
            service.submit(car);
        }
        service.shutdown();
    }
}
