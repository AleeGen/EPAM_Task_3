package by.training.multithreading;

import by.training.multithreading.creator.CarCreator;
import by.training.multithreading.entity.Car;
import by.training.multithreading.entity.Ferry;
import by.training.multithreading.exception.CustomException;

import java.io.IOException;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class Main {
    //// TODO: 30.03.2022 добавить логи
    // TODO: 28.03.2022 использовать приоритетную очередь priorytyQuae...
    //// TODO: 27.03.2022  подумать над исключениями в getInstance
    static final String PATH = ClassLoader.getSystemResource("data.properties").getPath();

    public static void main(String[] args) throws CustomException, InterruptedException, IOException {

        List<Car> listCar = CarCreator.getInstance().getListCar();
        ExecutorService service = Executors.newFixedThreadPool(listCar.size());
        for (Car car : listCar) {
            service.execute(car);
        }
        Ferry.getInstance().start();
        System.out.println("End main!");
        service.shutdown();


        //// TODO: 22.03.2022 разобраться что здесь происходит, и с 4 полями сверху
        /*private static CountDownLatch initialisingLatch = new CountDownLatch(1);
        private static AtomicBoolean isInstanceInitialized = new AtomicBoolean(false);

        private CountDownLatch connectionsCheckLatch;
        private AtomicBoolean connectionsNumberCheck = new AtomicBoolean(false);

        public static Ferry getInstance () throws CustomException
        {
            if (instance == null) {
                while (isInstanceInitialized.compareAndSet(false, true)) {
                    instance = new Ferry();
                    initialisingLatch.countDown();
                }
                try {
                    initialisingLatch.await();
                } catch (InterruptedException e) {
                    logger.log(Level.ERROR, "");
                    throw new CustomException(e);
                }
            }
            return instance;
        }*/
    }
}