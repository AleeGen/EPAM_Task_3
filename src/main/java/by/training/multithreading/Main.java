package by.training.multithreading;

import by.training.multithreading.creator.CarCreator;
import by.training.multithreading.entity.Car;
import by.training.multithreading.entity.Ferry;
import by.training.multithreading.exception.CustomException;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {

    public static void main(String[] args) throws CustomException {

        List<Car> listCar = CarCreator.getInstance().getListCar();
        ExecutorService service = Executors.newFixedThreadPool(listCar.size());
        for (Car car : listCar) {
            service.execute(car);
        }
        Ferry.getInstance().start();
        service.shutdown();
    }
}