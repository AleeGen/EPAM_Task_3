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
    private static final int MIN_TIME_LOAD_UNLOAD = 50;
    private static final int TIME_SWIM = 2;
    private static ReentrantLock lock = new ReentrantLock();
    private static Ferry instance;
    private AtomicInteger areaOccupied = new AtomicInteger();
    private AtomicInteger loadCapacityOccupied = new AtomicInteger();
    private AtomicInteger areaRemained = new AtomicInteger();
    private AtomicInteger loadCapacityRemained = new AtomicInteger();
    private Deque<Car> lotsСar;
    private static AtomicBoolean create = new AtomicBoolean(false);

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
        try {
            data.load(new FileReader(PATH));
        } catch (IOException e) {
            logger.log(Level.ERROR, "Not found file {}", PATH, e);
            throw new CustomException(e);
        }
        areaRemained.set(Integer.parseInt(data.getProperty("area")));
        loadCapacityRemained.set(Integer.parseInt(data.getProperty("loadCapacity")));
        areaOccupied.set(DEFAULT_VALUE);
        loadCapacityOccupied.set(DEFAULT_VALUE);
        lotsСar = new ArrayDeque<>();
        this.setName("Ferry");
        logger.info("\nFERRY created! area = {}, loadCapacity = {}", areaRemained, loadCapacityRemained);
    }

    @Override
    public void run() {
        System.out.println("Ferry WORK");
        Car car = null;
        while (true) {
            try {
                car = Turn.getInstance().getTurnCar().peek();
                if (car == null) {
                    System.out.println("Паром ожидает авто...");
                    TimeUnit.SECONDS.sleep(TIME_WAITING_CAR);
                    car = Turn.getInstance().getTurnCar().peek();
                    if (car == null) {
                        if (lotsСar.size() == 0) {
                            break;
                        } else if (lotsСar.size() != 0) {
                            unload();
                            continue;
                        }
                    }
                }
            } catch (CustomException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (!load(car)) {
                unload();
            }
        }
        System.out.println("Ferry NOT WORK");
    }

    private boolean load(Car car) {
        System.out.println("Ferry load start");
        try {
            if (car.getArea() < areaRemained.get() && car.getMass() < loadCapacityRemained.get()) {
                TimeUnit.MILLISECONDS.sleep(new Random().nextInt(1000) + MIN_TIME_LOAD_UNLOAD);
                lotsСar.add(Turn.getInstance().getTurnCar().poll());
                car.setCarState(Car.State.ON_FERRY);
                areaOccupied.addAndGet(car.getArea());
                areaRemained.addAndGet(-car.getArea());
                loadCapacityOccupied.addAndGet(car.getMass());
                loadCapacityRemained.addAndGet(-car.getMass());
                System.out.println("Загрузил " + car.getName());
                try {
                    Turn.getInstance().init();
                } catch (CustomException e) {
                    e.printStackTrace();
                }
                System.out.println("Ferry load end");
                return true;
            }
        } catch (CustomException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Места на пароме нет!");
        return false;
    }

    private void unload() {
        try {
            System.out.println("Ferry swim");
            TimeUnit.SECONDS.sleep(TIME_SWIM);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Ferry unload start");
        int size = lotsСar.size();
        for (int i = 0; i < size; i++) {
            try {
                TimeUnit.MILLISECONDS.sleep(new Random().nextInt(1000) + MIN_TIME_LOAD_UNLOAD);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Car car = lotsСar.peek();
            lotsСar.pop().setCarState(Car.State.FINISHED);
            //car.setState(Car.CustomState.FINISHED);
            areaOccupied.getAndAdd(-car.getArea());
            loadCapacityOccupied.getAndAdd(-car.getMass());
            areaRemained.getAndAdd(car.getArea());
            loadCapacityRemained.getAndAdd(car.getMass());
        }
        System.out.println("Ferry unload end (" + size + "car)");
        try {
            System.out.println("Ferry swim");
            TimeUnit.SECONDS.sleep(TIME_SWIM);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

