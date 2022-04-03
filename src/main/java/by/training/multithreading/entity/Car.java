package by.training.multithreading.entity;

import by.training.multithreading.generator.IdCar;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;


public class Car extends Thread {

    private static final Logger logger = LogManager.getLogger();

    private final ReentrantLock lock = new ReentrantLock();
    private final Condition condition = lock.newCondition();
    private final int idCar;
    private final int area;
    private final int mass;
    private CarState state;

    public enum CarState {
        CREATED, ON_TURN, ON_FERRY, FINISHED
    }

    public Car(int area, int mass) {
        idCar = IdCar.generate();
        this.area = area;
        this.mass = mass;
        this.setName("Car_" + idCar);
        state = CarState.CREATED;
    }

    @Override
    public void run() {
        try {
            lock.lock();
            Turn.getInstance().goQueue(this);
            while (this.getCarState() != CarState.FINISHED) {
                condition.await();
            }
        } catch (InterruptedException e) {
            logger.error("Blocking the execution stream ", e);
            Thread.currentThread().interrupt();
        } finally {
            lock.unlock();
        }
        logger.info("{} finished", this.getName());
    }

    public void setCarState(CarState state) {
        try {
            lock.lock();
            this.state = state;
            if (state == CarState.FINISHED) {
                condition.signal();
            }
        } finally {
            lock.unlock();
        }
    }

    public CarState getCarState() {
        return state;
    }

    public int getIdCar() {
        return idCar;
    }

    public int getArea() {
        return area;
    }

    public int getMass() {
        return mass;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Car car = (Car) o;
        return getIdCar() == car.getIdCar() && getArea() == car.getArea() && getMass() == car.getMass();
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(idCar) + Integer.hashCode(area) + Integer.hashCode(mass);
    }

    @Override
    public String toString() {
        return new StringBuilder()
                .append("Car{").append("idCar=")
                .append(idCar).append(", area=")
                .append(area).append(", mass=")
                .append(mass).append('}').toString();
    }
}
