import com.chen.controller.HomeController;
import com.chen.model.User;
import org.junit.Test;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.web.bind.annotation.ResponseBody;

import java.lang.reflect.Method;

/**
 * Description:
 * Author: wei
 * DateTime: 2016-02-13
 */
public class CustomTest {
    @Test
    public void test1() throws NoSuchMethodException {
        Method me=HomeController.class.getMethod("test1", User.class);
        System.out.println(AnnotationUtils.findAnnotation(me, ResponseBody.class));
    }
}
