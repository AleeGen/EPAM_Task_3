package by.training.multithreading.entity;

import by.training.multithreading.exception.CustomException;
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
    private static final int MAX_SIZE_TURN = 5;
    private static final int MIN_TIME_QUEUE = 50;
    private static ReentrantLock lock = new ReentrantLock();
    private static Condition condition = lock.newCondition();
    private static Turn instance;
    private Deque<Car> turnCar;
    private static AtomicBoolean create = new AtomicBoolean(false);

    public static Turn getInstance() throws CustomException {
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

    public void goQueue(Car car) {
        try {
            lock.lock();
            System.out.println("Start queue -> " + car.getName());
            if (turnCar.size() >= MAX_SIZE_TURN) {
                condition.await();
            }
            TimeUnit.MILLISECONDS.sleep(new Random().nextInt(1000) + MIN_TIME_QUEUE);
            turnCar.add(car);
            car.setCarState(Car.State.ON_TURN);
            System.out.println("Finish queue -> " + car.getName());
        } catch (InterruptedException e) {
            e.printStackTrace();
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
