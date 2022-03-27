package by.training.multithreading.entity;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Queue;

public class Turn {
    private static Turn instance;
    private Deque<Car> turnCar;

    public static Turn getInstance() {
        if (instance == null) {
            instance = new Turn();
        }
        return instance;
    }

    private Turn() {
        turnCar = new ArrayDeque<>();
    }
}
