package by.training.multithreading.entity;

import by.training.multithreading.exception.CustomException;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.StringReader;
import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicBoolean;

public class Ferry {
    private static final Logger logger = LogManager.getLogger();
    private static final String PATH = ClassLoader.getSystemResource("data.properties").getPath();
    private static Ferry instance;
    private int area;
    private int loadCapacity;
    private Deque<ParkingSpace> parkingPlaces;


    private static CountDownLatch initialisingLatch = new CountDownLatch(1);
    private static AtomicBoolean isInstanceInitialized = new AtomicBoolean(false);

    private CountDownLatch connectionsCheckLatch;
    private AtomicBoolean connectionsNumberCheck = new AtomicBoolean(false);


    public static Ferry getInstance() throws CustomException {   //// TODO: 22.03.2022 разобраться что здесь происходит, и с 4 полями сверху
        if (instance == null) {
            while (isInstanceInitialized.compareAndSet(false, true)) {
                instance = new Ferry();
                initialisingLatch.countDown();
            }
            try {
                initialisingLatch.await();
            } catch (InterruptedException e) {
                logger.log(Level.ERROR, "");
                throw new CustomException(e);
            }
        }
        return instance;
    }

    private Ferry() throws CustomException {
        Properties data = new Properties();
        try {
            data.load(new StringReader(PATH));
        } catch (IOException e) {
            logger.log(Level.ERROR, "Not found file {}", PATH, e);
            throw new CustomException(e);
        }
        area = Integer.parseInt(data.getProperty("area"));
        loadCapacity = Integer.parseInt(data.getProperty("loadCapacity"));
        int countParkingPlaces = Integer.parseInt(data.getProperty("parkingPlaces"));
        parkingPlaces = new ArrayDeque<>();
        for (int i = 0; i < countParkingPlaces; i++) {
            parkingPlaces.add(new ParkingSpace());
        }
        logger.info("Ferry created");
    }


}
