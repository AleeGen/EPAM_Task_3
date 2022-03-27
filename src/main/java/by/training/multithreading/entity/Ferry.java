package by.training.multithreading.entity;

import by.training.multithreading.exception.CustomException;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.ReentrantLock;

public class Ferry extends Thread {
    private static final Logger logger = LogManager.getLogger();
    private static final String PATH = ClassLoader.getSystemResource("data.properties").getPath();
    private static ReentrantLock lock = new ReentrantLock();
    private static Ferry instance;
    private int area;
    private int loadCapacity;
    //private Deque<ParkingSpace> parkingPlaces;
    private Deque<Car> turnCar = new ArrayDeque<>();
    private AtomicBoolean readiness;


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
            data.load(new FileReader(PATH));
        } catch (IOException e) {
            logger.log(Level.ERROR, "Not found file {}", PATH, e);
            throw new CustomException(e);
        }
        readiness = new AtomicBoolean(false);
        area = Integer.parseInt(data.getProperty("area"));
        loadCapacity = Integer.parseInt(data.getProperty("loadCapacity"));
        Turn.getInstance();
        /*int countParkingPlaces = Integer.parseInt(data.getProperty("parkingPlaces"));
        parkingPlaces = new ArrayDeque<>();
        for (int i = 0; i < countParkingPlaces; i++) {
            parkingPlaces.add(new ParkingSpace());
        }*/
        logger.info("FERRY and TURN created!");
    }

    public void goQueue(Car car) {   //// TODO: 27.03.2022 продумать алгоритм взаимодействия машины с паромом, в какой момент паром уезжает и что происходит с очередью 
        try {
            lock.lock();
            System.out.print("Start goQueue -> ");
            TimeUnit.MILLISECONDS.sleep(50);
            turnCar.add(car);
            System.out.println(" -> Finish goQueue!");
        } catch (InterruptedException e) {
            e.printStackTrace(); //// TODO: 27.03.2022 лог добавить
        } finally {
            lock.unlock();
        }
        if (readiness.get()) {
            this.start();
        }
    }

    private boolean readyFerry() {

        return false;
    }

    @Override
    public void run() {
        readiness.set(false);
    }
}
