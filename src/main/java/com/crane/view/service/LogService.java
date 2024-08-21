package com.crane.view.service;

import com.crane.view.config.Language;
import com.crane.view.tools.FileTool;
import com.crane.view.tools.ShowMessage;

import java.io.File;
import java.nio.file.Paths;

/**
 * 日志服务
 * 该服务是依赖于exe4j的哦
 *
 * @author AXing
 * @date 2023/12/22 20:42:55
 */
public class LogService {

    public void showLog() {
        File logFile = new File(Paths.get("").toAbsolutePath() + "/logs/log.log");
        if (logFile.exists()) {
            FileTool.openFile(logFile.getPath());
        } else {
            ShowMessage.showErrorMessage(Language.get("notLookFile"), Language.get("notLookFileTit"));
        }
    }

}
