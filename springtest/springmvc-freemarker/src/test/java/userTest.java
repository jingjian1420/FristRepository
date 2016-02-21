import com.chen.controller.HomeController;
import com.chen.utils.ReflectUtil;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.Map;

/**
 * Description:
 * Author: wei
 * DateTime: 2016-02-11
 */
public class userTest extends BaseTest {

    @Test
    public void addTest() throws IOException {
        for(int i=0;i<1000;i++){
            System.out.println(i);
        }
    }
}
