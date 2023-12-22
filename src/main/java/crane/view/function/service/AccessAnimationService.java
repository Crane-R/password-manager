package crane.view.function.service;

import lombok.Getter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * 进出动画服务
 *
 * @author AXing
 * @date 2023/12/20 22:33:01
 */
public class AccessAnimationService {

    private final Rectangle originBounds;

    private final JComponent component;

    public AccessAnimationService(JComponent component) {
        this.component = component;
        originBounds = component.getBounds();
    }

    public void bind(int offset, double largeTime, Direction direction) {
        component.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseExited(MouseEvent e) {
                component.setBounds(originBounds);
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                component.setBounds(
                        (int) originBounds.getX() + direction.getX() * offset,
                        (int) originBounds.getY() + direction.getY() * offset,
                        (int) (originBounds.getWidth() * largeTime),
                        (int) (originBounds.getHeight() * largeTime)
                );
            }
        });
    }

    /**
     * 方向枚举
     *
     * @author AXing
     * @date 2023/12/20 22:37:51
     */
    @Getter
    public enum Direction {

        Up(0, 1),
        Down(0, -1),
        Left(-1, 0),
        right(1, 0),
        /**
         * 当x或y存在负数时，选择此枚举
         *
         * @Author AXing
         * @Date 2023/12/21 18:14:16
         */
        MinusUp(0, -1),
        MinusDown(0, 1),
        MinusLeft(1, 0),
        MinusRight(-1, 0);


        /**
         * 计算因子
         *
         * @Author AXing
         * @date 2023/12/20 22:41:30
         */
        private final int x;
        private final int y;


        Direction(int x, int y) {
            this.x = x;
            this.y = y;
        }

    }

}
