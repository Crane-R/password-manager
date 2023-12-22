package crane.view.function.service;

import crane.view.function.config.Language;
import crane.view.function.tools.FileTool;
import crane.view.function.tools.ShowMessage;

import java.io.File;

/**
 * 日志服务
 *
 * @author AXing
 * @date 2023/12/22 20:42:55
 */
public class LogService {

    public void showLog(){
        File logFile = new File("log.log");
        if (logFile.exists()) {
            FileTool.openFile(logFile.getPath());
        } else {
            ShowMessage.showErrorMessage(Language.get("notLookFile"), Language.get("notLookFileTit"));
        }
    }

}
