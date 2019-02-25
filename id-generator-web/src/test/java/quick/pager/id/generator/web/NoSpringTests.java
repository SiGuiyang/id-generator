package quick.pager.id.generator.web;

import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;
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
    }
//
//    @Test
//    public void testZK() throws Exception {
//
//        IdMaker idMaker = new IdMaker("127.0.0.1:2181",
//                "/NameSpace/IdGen8899889", "ID");
//        idMaker.start();
//
//        try {
//            for (int i = 0; i < 5; i++) {
//                String id = idMaker.generateId(IdMaker.RemoveMethod.DELAY);
//                System.out.println(id);
//
//            }
//        } finally {
//            idMaker.stop();
//
//        }
//    }

}
