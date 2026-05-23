package com.crane;

import com.crane.view.frame.LockFrame;
import com.crane.view.service.FrontLoading;
import com.crane.view.service.LogService;
import com.crane.view.tools.InitLogRecord;
import com.crane.view.tools.ShowMessage;
import com.crane.view.tools.VersionCheckTool;
import lombok.extern.slf4j.Slf4j;

/**
 * 程序启动入口。
 */
@Slf4j
public class PmApplication {

    public static void main(String[] args) {
        InitLogRecord.initLog();
        try {
            VersionCheckTool.checkVersion();
            FrontLoading.checkKeysDirectory();
            LockFrame.start();
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage(), e);
            ShowMessage.showErrorMessage(e.getStackTrace(), "致命错误");
            new LogService().showLog();
        }
    }
}
