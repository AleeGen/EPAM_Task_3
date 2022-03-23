package by.training.multithreading.entity;

import by.training.multithreading.util.IdParkingSpace;

public class ParkingSpace {
    private int idSpace;
    private boolean freedom;

    public ParkingSpace() {
        idSpace = IdParkingSpace.generate();
        freedom = true;
    }

    public boolean isFreedom() {
        return freedom;
    }

    public void setFreedom(boolean freedom) {
        this.freedom = freedom;
    }

    public long getIdSpace() {
        return idSpace;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ParkingSpace that = (ParkingSpace) o;
        return getIdSpace() == that.getIdSpace() && isFreedom() == that.isFreedom();
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(idSpace) + Boolean.hashCode(freedom);
    }

    @Override
    public String toString() {
        return new StringBuilder()
                .append("ParkingSpace{").append("id=")
                .append(idSpace).append(", busy=")
                .append(freedom).append('}').toString();
    }
}
