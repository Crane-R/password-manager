package com.crane.view.tools;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;

/**
 * Description: 工具类
 * Author: ZhouXingxue
 * Date: 2023/1/22 15:19
 * @author Crane Resigned
 */
public final class TextTools {
    
    private TextTools() {
    }
    
    /**
     * 将传入的字符串粘贴到剪切板
     * Author: Crane Resigned
     * Date: 2023-01-22 15:20:36
     */
    public static void stick(String result) {
        Clipboard clip = Toolkit.getDefaultToolkit().getSystemClipboard();
        Transferable tText = new StringSelection(result);
        clip.setContents(tText, null);
    }
    
}
