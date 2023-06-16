package crane.view.module;

import javax.swing.*;
import java.awt.*;

/**
 * 多选框渲染器
 * @Author Crane Resigned
 * @Date 2023-06-16 13:41:44
 */
public class ComboBoxRender extends DefaultListCellRenderer {

    private final ListCellRenderer defaultRenderer;

    public ComboBoxRender(ListCellRenderer  defaultRenderer) {
        this.defaultRenderer = defaultRenderer;
    }

    @Override
    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        Component c = defaultRenderer.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
        if (isSelected) {
            c.setBackground(Color.decode("#407E54"));
            c.setForeground(Color.white);
        } else {
            c.setBackground(Color.decode("#DAE4E6"));
            c.setForeground(Color.black);
        }
        list.setSelectionBackground(Color.decode("#407E54"));
        list.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return c;

    }

}
