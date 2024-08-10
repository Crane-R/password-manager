package com.crane.view.module;

import com.crane.constant.Constant;
import com.crane.view.function.config.Config;

import javax.swing.*;
import java.awt.*;

/**
 * 自定义全局窗口内容
 *
 * @Author Crane Resigned
 * @Date 2024/8/10 17:50:00
 */
public class MainContentPanel extends JPanel {

    protected final Config colorConfig = Constant.colorConfig;

    public MainContentPanel(JFrame currentFrame) {
        this.setBounds(0, 0, currentFrame.getWidth(), currentFrame.getHeight());
        this.setLayout(null);
        this.setBorder(BorderFactory.createLineBorder(Color.decode(colorConfig.get("mainPanelBg")), 1));
    }

}
