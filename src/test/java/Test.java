import com.crane.constant.MainFrameCst;

import java.io.File;

/**
 * ≤‚ ‘
 *
 * @Author Crane Resigned
 * @Date 2024/8/21 15:14:53
 */
public class Test {

    @org.junit.Test
    public void test(){

        String newKeyDir = "C://" + "csjdlkfgjsdlfkj" + "//keys";
        boolean mkdirs = new File(newKeyDir).mkdirs();
        System.out.println(mkdirs);

    }

}
