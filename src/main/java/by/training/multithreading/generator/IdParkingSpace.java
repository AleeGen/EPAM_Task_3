package by.training.multithreading.generator;

public class IdParkingSpace {
    private static int id = 0;

    public static int generate() {
        return ++id;
    }
}
