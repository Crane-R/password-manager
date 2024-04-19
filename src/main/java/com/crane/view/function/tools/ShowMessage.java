package com.crane.view.function.tools;

import com.crane.constant.DefaultFont;

import javax.swing.*;
import javax.swing.plaf.FontUIResource;

/**
 * Description: 自定义弹窗类
 * Author: ZhouXingxue
 * Date: 2023/1/29 18:03
 *
 * @author Crane Resigned
 */
public class ShowMessage {

    private ShowMessage() {
    }

    /**
     * 自定义消息弹窗 -- 正常
     * Author: Crane Resigned
     * Date: 2023-01-29 17:59:39
     */
    public static void showInformationMessage(String message, String title) {
        setAlert();
        JOptionPane.showMessageDialog(null, message, title, JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * 错误弹窗
     * Author: Crane Resigned
     * Date: 2023-01-29 18:01:36
     */
    public static void showErrorMessage(String message, String title) {
        setAlert();
        JOptionPane.showMessageDialog(null, message, title, JOptionPane.ERROR_MESSAGE);
    }

    /**
     * 无图标弹窗
     * Author: Crane Resigned
     * Date: 2023-01-29 18:02:58
     */
    public static void showPlainMessage(String message, String title) {
        setAlert();
        JOptionPane.showMessageDialog(null, message, title, JOptionPane.PLAIN_MESSAGE);
    }

    /**
     * 警告弹窗
     * Author: Crane Resigned
     * Date: 2023-01-29 18:05:23
     */
    public static void showWarningMessage(String message, String title) {
        setAlert();
        JOptionPane.showMessageDialog(null, message, title, JOptionPane.WARNING_MESSAGE);
    }

    /**
     * 设置弹窗样式
     * Author: Crane Resigned
     * Date: 2023-01-29 18:06:06
     */
    private static void setAlert() {
        UIManager.put("OptionPane.messageFont", new FontUIResource(DefaultFont.WEI_RUAN_PLAIN_13.getFont()));
        UIManager.put("OptionPane.buttonFont", new FontUIResource(DefaultFont.WEI_RUAN_PLAIN_13.getFont()));
    }

}
