package com.crane;

import com.crane.view.function.service.LogService;
import com.crane.view.function.tools.ShowMessage;
import com.crane.view.function.tools.VersionCheckTool;
import com.crane.view.function.service.FrontLoading;
import com.crane.view.LockFrame;
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
            log.error(e.getMessage());
            ShowMessage.showErrorMessage(e.getMessage() + "</br>"
                    + e.getStackTrace()[0].toString(), "致命错误");
            new LogService().showLog();
        }
    }
}
