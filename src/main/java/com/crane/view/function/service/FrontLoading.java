package com.crane.view.function.service;

import com.crane.constant.Constant;
import com.crane.constant.MainFrameCst;
import com.crane.view.function.config.Language;
import com.crane.view.function.tools.ShowMessage;
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
     * 如果是在C盘会存在权限问题，但是在C盘根目录不会存在权限问题，
     * 所以如果是C盘就会在C盘根目录创建
     * Author: Crane Resigned
     * Date: 2023-01-07 23:52:16
     */
    public static void checkKeysDirectory() {
        String path = Paths.get("keys").toAbsolutePath().toString();
        File keyFolder = new File(path);
        if (keyFolder.exists()) {
            return;
        }

        boolean isCreatedTrue = keyFolder.mkdirs();
        if (!isCreatedTrue) {
            String newKeyDir = "C://" + MainFrameCst.MAIN_TITLE + "//keys";
            isCreatedTrue = new File(newKeyDir).mkdirs();
            Constant.DIRECTORY_KEYS = newKeyDir;
        }
        log.info("创建key文件夹{}", isCreatedTrue);
        if (isCreatedTrue) {
            try {
                //设为只读
                Runtime.getRuntime().exec("attrib \"" + path + "\" +R");
                //设为隐藏
                Runtime.getRuntime().exec("attrib \"" + path + "\" +H");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            ShowMessage.showErrorMessage(Language.get("createKeysFolderFailMsg"), Language.get("createKeysFolderFailTip"));
            System.exit(0);
        }
    }

}
