package com.crane.test;

import com.crane.view.ConfigurableFrame;
import com.crane.view.module.ShadowPanel;

import javax.swing.*;
import java.awt.*;

/**
 * 临时测试
 *
 * @author AXing
 * @date 2023/12/22 22:04:28
 */
public class TempTest extends JFrame {

    public static void main(String[] args) {

        new ConfigurableFrame().setVisible(true);

    }

    public TempTest(){
        this.setSize(1000,1000);
        this.setLayout(new BorderLayout());
        ShadowPanel shadowPanel = new ShadowPanel(80);
        shadowPanel.add(new JButton("sdfd"));
        this.add(shadowPanel);
    }

}
