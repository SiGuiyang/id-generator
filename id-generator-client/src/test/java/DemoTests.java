import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import quick.pager.id.generator.utils.BeanUtils;

public class DemoTests {

    @Test
    public void test(){

        List<String> list = new ArrayList<>();

        System.out.println(BeanUtils.getSuperClassGenricType(String.class));
    }
}
