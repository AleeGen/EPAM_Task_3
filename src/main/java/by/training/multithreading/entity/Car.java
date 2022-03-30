package by.training.multithreading.entity;

import by.training.multithreading.exception.CustomException;
import by.training.multithreading.generator.IdCar;

import java.util.concurrent.TimeUnit;


public class Car extends Thread {
    private int idCar;
    private int area;
    private int mass;
    private State state;


    public enum State {
        CREATED, ON_TURN, ON_FERRY, FINISHED
    }

    public Car(int area, int mass) {
        idCar = IdCar.generate();
        this.area = area;
        this.mass = mass;
        this.setName("Car_" + idCar);
        state = State.CREATED;
    }

    @Override
    public void run() {
        try {
            Turn.getInstance().goQueue(this);
        } catch (CustomException e) {
            e.printStackTrace();
        }
        while (true) { //// TODO: 30.03.2022 без этого поток не живет 
            if (this.getCarState() == State.FINISHED) break;
            try {
                TimeUnit.NANOSECONDS.sleep(1);  //// FIXME: 30.03.2022 без задержки не заканчивается
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println("end------------> " + this.getName());
    }

    public void setCarState(State state) {
        this.state = state;
    }

    public State getCarState() {
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
