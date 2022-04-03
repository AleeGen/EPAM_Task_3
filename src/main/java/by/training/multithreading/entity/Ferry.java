package by.training.multithreading.entity;

import by.training.multithreading.exception.CustomException;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;

public class Ferry extends Thread {

    private static final Logger logger = LogManager.getLogger();
    private static final String PATH = ClassLoader.getSystemResource("data.properties").getPath();
    private static final int DEFAULT_VALUE = 0;
    private static final int TIME_WAITING_CAR = 1;
    private static final long MIN_TIME_LOAD_UNLOAD = 50;
    private static final int TIME_SWIM = 2;
    private static final Random random = new Random();
    private static final AtomicBoolean create = new AtomicBoolean(false);
    private static final ReentrantLock lock = new ReentrantLock();
    private static Ferry instance;

    private final AtomicInteger areaOccupied = new AtomicInteger();
    private final AtomicInteger loadCapacityOccupied = new AtomicInteger();
    private final AtomicInteger areaRemained = new AtomicInteger();
    private final AtomicInteger loadCapacityRemained = new AtomicInteger();
    private final Deque<Car> lotsCar;


    public static Ferry getInstance() throws CustomException {
        if (!create.get()) {
            lock.lock();
            try {
                if (instance == null) {
                    instance = new Ferry();
                    create.set(true);
                }
            } catch (CustomException e) {
                logger.log(Level.ERROR, "");
                throw new CustomException(e);
            } finally {
                lock.unlock();
            }
        }
        return instance;
    }

    private Ferry() throws CustomException {
        Properties data = new Properties();
        try (FileReader fileReader = new FileReader(PATH)) {
            data.load(fileReader);
        } catch (IOException e) {
            logger.error("Not found file {}", PATH, e);
            throw new CustomException(e);
        }
        areaRemained.set(Integer.parseInt(data.getProperty("area")));
        loadCapacityRemained.set(Integer.parseInt(data.getProperty("loadCapacity")));
        areaOccupied.set(DEFAULT_VALUE);
        loadCapacityOccupied.set(DEFAULT_VALUE);
        lotsCar = new ArrayDeque<>();
        this.setName("Ferry");
        logger.info("FERRY created: area = {}, loadCapacity = {}", areaRemained, loadCapacityRemained);
    }

    @Override
    public void run() {
        logger.info("Ferry started working");
        Car car = null;
        while (true) {
            try {
                car = Turn.getInstance().getTurnCar().peek();
                if (car == null) {
                    logger.info("Ferry waiting a cars..");
                    TimeUnit.SECONDS.sleep(TIME_WAITING_CAR);
                    car = Turn.getInstance().getTurnCar().peek();
                    if (car == null) {
                        if (lotsCar.isEmpty()) {
                            break;
                        } else {
                            unload();
                        }
                    }
                } else if (!load(car)) {
                    unload();
                }
            } catch (InterruptedException e) {
                logger.error("Blocking the execution stream ", e);
                Thread.currentThread().interrupt();
            }
        }
        logger.info("Ferry stopped working");
    }

    private boolean load(Car car) {
        logger.info("Ferry load start");
        try {
            if (car.getArea() < areaRemained.get() && car.getMass() < loadCapacityRemained.get()) {
                TimeUnit.MILLISECONDS.sleep(random.nextInt(1000) + MIN_TIME_LOAD_UNLOAD);
                lotsCar.add(Turn.getInstance().getTurnCar().pop());
                car.setCarState(Car.CarState.ON_FERRY);
                areaOccupied.addAndGet(car.getArea());
                areaRemained.addAndGet(-car.getArea());
                loadCapacityOccupied.addAndGet(car.getMass());
                loadCapacityRemained.addAndGet(-car.getMass());
                logger.info("load {}", car.getName());
                Turn.getInstance().init();
                return true;
            }
        } catch (InterruptedException e) {
            logger.error("Blocking the execution stream", e);
            Thread.currentThread().interrupt();
        }
        logger.info("No place on ferry!");
        return false;
    }

    private void unload() {
        try {
            logger.info("Ferry swim");
            TimeUnit.SECONDS.sleep(TIME_SWIM);
            logger.info("Ferry unload start");
            int size = lotsCar.size();
            for (int i = 0; i < size; i++) {
                TimeUnit.MILLISECONDS.sleep(random.nextInt(1000) + MIN_TIME_LOAD_UNLOAD);
                Car car = lotsCar.peek();
                lotsCar.pop().setCarState(Car.CarState.FINISHED);
                areaOccupied.getAndAdd(-car.getArea());
                loadCapacityOccupied.getAndAdd(-car.getMass());
                areaRemained.getAndAdd(car.getArea());
                loadCapacityRemained.getAndAdd(car.getMass());
            }
            logger.info("Ferry unload end ({} car)", size);
            logger.info("Ferry swim");
            TimeUnit.SECONDS.sleep(TIME_SWIM);
        } catch (InterruptedException e) {
            logger.error("Blocking the execution stream", e);
            Thread.currentThread().interrupt();
        }
    }
}

