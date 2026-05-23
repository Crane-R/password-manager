package com.crane.view.tools;

import java.awt.*;
import java.io.File;
import java.io.IOException;

/**
 * 文件工具。
 */
public final class FileTool {

    private FileTool() {
    }

    /**
     * 打开文件。
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
