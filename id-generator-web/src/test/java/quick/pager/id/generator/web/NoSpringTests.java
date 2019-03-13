package quick.pager.id.generator.web;

import java.util.concurrent.BlockingDeque;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import org.junit.Test;

public class NoSpringTests {

    private static final BlockingDeque<Number> BLOCKING_DEQUE = new LinkedBlockingDeque<>();


    @Test
    public void test() throws InterruptedException {
        for (int i = 0; i < 10; i++) {
            BLOCKING_DEQUE.offer(i);
        }

        System.out.println(BLOCKING_DEQUE);

        for (int i = 0; i < 10; i++) {
            System.out.println(BLOCKING_DEQUE.poll(300, TimeUnit.MILLISECONDS));
        }
        System.out.println(BLOCKING_DEQUE);


        Executors.newCachedThreadPool(new ThreadFactory() {
            @Override
            public Thread newThread(Runnable r) {
                return null;
            }
        });
    }

}
