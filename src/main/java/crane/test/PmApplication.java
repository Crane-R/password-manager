package crane.test;

import crane.view.function.service.LogService;
import crane.view.function.tools.VersionCheckTool;
import crane.view.function.service.FrontLoading;
import crane.view.LockFrame;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Crane Resigned
 */
@Slf4j
public class PmApplication {
    public static void main(String[] args) {
        try {
            //版本检测
            VersionCheckTool.checkVersion();
            //检测keys
            FrontLoading.checkKeysDirectory();
            //启动窗口
            LockFrame.start();
        } catch (Exception e) {
            log.error(e.toString());
            new LogService().showLog();
        }
    }
}
