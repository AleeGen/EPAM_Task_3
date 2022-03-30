package by.training.multithreading.creator;

import by.training.multithreading.entity.Car;
import by.training.multithreading.exception.CustomException;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Random;

public class CarCreator {
    private static final Logger logger = LogManager.getLogger();
    private static final String PATH = ClassLoader.getSystemResource("data.properties").getPath();
    private static final int MIN_AREA_MASS = 1;
    private static CarCreator instance;
    private List<Car> listCar;

    private CarCreator() throws CustomException {
        Properties data = new Properties();
        try {
            data.load(new FileReader(PATH));
        } catch (IOException e) {
            logger.log(Level.ERROR, "Not found file {}", PATH, e);
            throw new CustomException(e);
        }
        int countCar = Integer.parseInt(data.getProperty("countCar"));
        creator(countCar);
        logger.info("{} cars created", countCar);
    }

    public static CarCreator getInstance() throws CustomException {
        if (instance == null) {
            instance = new CarCreator();
        }
        return instance;
    }

    public List<Car> getListCar() {
        return listCar;
    }

    private void creator(int countCar) {
        listCar = new ArrayList<>();
        Random random = new Random();
        for (int i = 0; i < countCar; i++) {
            int area = random.nextInt(9) + MIN_AREA_MASS;
            int mass = random.nextInt(24) + MIN_AREA_MASS;
            listCar.add(new Car(area, mass));
        }
    }


}
