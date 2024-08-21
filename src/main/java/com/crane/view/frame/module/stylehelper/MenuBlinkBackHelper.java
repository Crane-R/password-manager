package com.crane.view.frame.module.stylehelper;

import javax.swing.*;
import javax.swing.plaf.basic.BasicMenuItemUI;
import java.awt.*;

public final class MenuBlinkBackHelper {

    public static void addBlinkBackground(JMenuItem component, Color colorIn, Color colorOut) {
        component.setBackground(colorOut);
        component.setUI(new MenuItemUi(colorIn));
    }

    static class MenuItemUi extends BasicMenuItemUI {
        public MenuItemUi(Color color) {
            super.selectionBackground = color;
        }
    }
}
