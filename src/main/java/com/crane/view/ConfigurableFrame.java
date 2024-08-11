package com.crane.view;

import com.crane.view.function.config.Language;

import javax.swing.*;

/**
 * 配置界面
 *
 * @Author Crane Resigned
 * @Date 2024/8/11 13:56:07
 */
public class ConfigurableFrame extends CustomFrame{
    public ConfigurableFrame() {
        super(500, 800, null);
        this.setTitle(Language.get("configurable.title"));



    }

    class ConfigurableItemPanel extends JPanel {

    }

}
