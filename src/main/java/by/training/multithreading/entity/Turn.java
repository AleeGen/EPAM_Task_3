package by.training.multithreading.entity;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class Turn {

    private static final Logger logger = LogManager.getLogger();
    private static final long MAX_SIZE_TURN = 5;
    private static final long MIN_TIME_QUEUE = 50;
    private static final ReentrantLock lock = new ReentrantLock();
    private static final Condition condition = lock.newCondition();
    private static final AtomicBoolean create = new AtomicBoolean(false);
    private static final Random random = new Random();
    private static Turn instance;

    private final Deque<Car> turnCar;


    public static Turn getInstance() {
        if (!create.get()) {
            lock.lock();
            try {
                if (instance == null) {
                    instance = new Turn();
                    create.set(true);
                }
            } finally {
                lock.unlock();
            }
        }
        return instance;
    }

    private Turn() {
        turnCar = new ArrayDeque<>();
    }

    public Deque<Car> getTurnCar() {
        return turnCar;
    }

    public void goQueue(Car car) throws InterruptedException {
        try {
            lock.lock();
            while (turnCar.size() >= MAX_SIZE_TURN) {
                condition.await();
            }
            TimeUnit.MILLISECONDS.sleep(random.nextInt(1000) + MIN_TIME_QUEUE);
            turnCar.add(car);
            car.setCarState(Car.CarState.ON_TURN);
            logger.info("{} in queue", car.getName());
        } catch (InterruptedException e) {
            logger.error("Blocking the execution stream ", e);
            throw e;
        } finally {
            lock.unlock();
        }
    }

    public void init() {
        try {
            lock.lock();
        } finally {
            condition.signal();
            lock.unlock();
        }
    }
}
