import com.crane.constant.Constant;
import com.crane.constant.MainFrameCst;
import com.crane.model.service.SecurityService;

import java.io.File;

/**
 *
 *
 * @Author Crane Resigned
 * @Date 2024/8/21 15:14:53
 */
public class Test {

    @org.junit.Test
    public void test(){

        Constant.CURRENT_KEY = "2002";
        String s = SecurityService.decodeBase64Salt("\u007FwNmBQN5ZQZlZQRlZQNl");
        System.out.println(s);
    }

}
