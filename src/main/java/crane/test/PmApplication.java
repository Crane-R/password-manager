package crane.test;

import crane.function.tools.VersionCheckTool;
import crane.function.service.FrontLoading;
import crane.view.LockFrame;

/**
 * @author Crane Resigned
 */
public class PmApplication {
    public static void main(String[] args) {
        //版本检测
        VersionCheckTool.checkVersion();
        //检测keys
        FrontLoading.checkKeysDirectory();
        //启动窗口
        LockFrame.start();
    }
}
