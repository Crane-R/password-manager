package com.crane;

import com.crane.view.service.LogService;
import com.crane.view.tools.InitLogRecord;
import com.crane.view.tools.ShowMessage;
import com.crane.view.tools.VersionCheckTool;
import com.crane.view.service.FrontLoading;
import com.crane.view.frame.LockFrame;
import lombok.extern.slf4j.Slf4j;


/**
 * @author Crane Resigned
 */
@Slf4j
public class PmApplication {

    public static void main(String[] args) {
        InitLogRecord.initLog();
        try {

            //版本检测
            VersionCheckTool.checkVersion();
            //检测keys
            FrontLoading.checkKeysDirectory();
            //启动窗口
            LockFrame.start();
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
            ShowMessage.showErrorMessage(e.getStackTrace(),"致命错误");
            new LogService().showLog();
        }
    }
}
