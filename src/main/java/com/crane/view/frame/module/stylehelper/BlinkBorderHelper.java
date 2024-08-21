package com.crane.view.frame.module.stylehelper;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public final class BlinkBorderHelper {

    /**
     * 传递一个组件，继承Component的
     * 装上闪烁边框
     *
     * @Author Crane Resigned
     * @Date 2023-09-02 13:03:25
     */
    public static void addBorder(JComponent component, Border borderIn, Border borderOut) {
        component.setBorder(borderOut);
        component.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {

            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {
                component.setBorder(borderIn);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                component.setBorder(borderOut);
            }
        });
    }

}
