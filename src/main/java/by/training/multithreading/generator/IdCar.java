package by.training.multithreading.generator;

public class IdCar {
    private static int id = 0;

    public static int generate() {
        return ++id;
    }
}