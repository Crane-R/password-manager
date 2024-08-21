package com.crane.view.tools;

import java.awt.*;
import java.io.File;
import java.io.IOException;

/**
 * 文件工具
 *
 * @Author Crane Resigned
 * @Date 2023-09-01 17:33:16
 */
public final class FileTool {

    private FileTool() {
    }

    /**
     * 打开文件
     *
     * @Author Crane Resigned
     * @Date 2023-09-01 17:37:51
     */
    public static void openFile(String path) {
        try {
            File file = new File(path);
            Desktop.getDesktop().open(file);
        } catch (IOException | NullPointerException e) {
            e.printStackTrace();
        }
    }

}
