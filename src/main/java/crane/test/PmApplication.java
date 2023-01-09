package crane.test;

import crane.model.service.FrontLoading;
import crane.view.LockFrame;

/**
 * @author Crane Resigned
 */
public class PmApplication {
    public static void main(String[] args) {
        //检测keys
        FrontLoading.checkKeysDirectory();
        //启动窗口
        new LockFrame().setVisible(true);
    }
}
