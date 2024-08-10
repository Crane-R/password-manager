package com.crane.view;

import com.crane.constant.Constant;
import com.crane.view.function.config.Config;
import com.crane.view.function.service.ImageService;
import com.crane.view.module.CustomTitle;
import com.crane.view.module.MainContentPanel;
import lombok.Getter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

/**
 * 自定义窗口
 *
 * @Author Crane Resigned
 * @Date 2024/8/10 18:02:07
 */
public class CustomFrame extends JFrame {

    /**
     * 颜色配置对象
     *
     * @author AXing
     * @date 2023/12/7 14:35:01
     */
    protected final Config colorConfig = Constant.colorConfig;

    /**
     * 承载所有组件内容
     *
     * @Author CraneResigned
     * @Date 2024/8/10 18:18:57
     */
    @Getter
    protected MainContentPanel mainContentPanel;

    @Getter
    protected CustomTitle customTitle;

    public CustomFrame(int width, int height, ActionListener closeBtnListener) {
        this.setSize(width, height);
        this.setLayout(null);
        this.setResizable(false);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setUndecorated(true);
        this.setIconImage(ImageService.getTitleImage());
        mainContentPanel = new MainContentPanel(this);
        super.add(mainContentPanel);

        customTitle = new CustomTitle(this, closeBtnListener);
        this.add(customTitle);
    }


    @Override
    public void setTitle(String title) {
        customTitle.setTitle(title);
        super.setTitle(title);
    }

    @Override
    public Component add(Component comp) {
        return mainContentPanel.add(comp);
    }
}
