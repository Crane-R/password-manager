package crane.function.service;

import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

/**
 * Description: 前置，执行程序前需要确定的事
 * Author: ZhouXingxue
 * Date: 2023/1/7 23:51
 *
 * @author Crane Resigned
 */
@Slf4j
public final class FrontLoading {

    private FrontLoading() {
        
    }

    /**
     * 检测keys文件夹是否存在的前置
     * Author: Crane Resigned
     * Date: 2023-01-07 23:52:16
     */
    public static void checkKeysDirectory() {
        String path = Paths.get("keys").toAbsolutePath().toString();
        File keyFolder = new File(path);
        if (!keyFolder.exists()) {
            boolean isCreatedTrue = keyFolder.mkdirs();
            log.info("创建key文件夹" + isCreatedTrue);
            if (isCreatedTrue) {
                try {
                    //设为只读
                    Runtime.getRuntime().exec("attrib \"" + path + "\" +R");
                    //设为隐藏
                    Runtime.getRuntime().exec("attrib \"" + path + "\" +H");
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

}
