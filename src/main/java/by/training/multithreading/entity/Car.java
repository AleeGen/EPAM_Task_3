package by.training.multithreading.entity;

import by.training.multithreading.util.IdCar;


public class Car extends Thread {
    private int idCar;
    private int area;
    private int mass;

    public Car(int area, int mass) {
        idCar = IdCar.generate();
        this.area = area;
        this.mass = mass;
    }

    @Override
    public void run() {
        //// TODO: 23.03.2022  
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
