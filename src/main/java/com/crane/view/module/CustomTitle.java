package com.crane.view.module;

import com.crane.constant.Constant;
import com.crane.view.CustomFrame;
import com.crane.view.function.config.Config;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Objects;

/**
 * 自定义标题栏
 *
 * @Author Crane Resigned
 * @Date 2024/8/9 19:31:03
 */
public class CustomTitle extends JPanel {

    private final Config colorConfig = Constant.colorConfig;

    private final JLabel titleLabel;

    private int newX, newY, oldX, oldY;
    private int startX, startY;

    public CustomTitle(CustomFrame currentFrame) {
        //26
        this.setBounds(1, 1, currentFrame.getWidth() - 2, 25);
        this.setBackground(Color.decode(colorConfig.get("titleBg")));
        this.setLayout(null);

        //标题文本
        titleLabel = new JLabel();
        titleLabel.setBounds(10, 0, 390, 25);
        titleLabel.setFont(new Font("微软雅黑", Font.PLAIN, 12));
        MouseAdapter titleDragEvent = new MouseAdapter() {
            /**
             * 该事件应用于addMouseMotionListener
             *
             * @Author CraneResigned
             * @Date 2024/8/10 17:09:03
             */
            @Override
            public void mouseDragged(MouseEvent e) {
                newX = e.getXOnScreen();
                newY = e.getYOnScreen();
                currentFrame.setBounds(
                        startX + (newX - oldX),
                        startY + (newY - oldY),
                        currentFrame.getWidth(),
                        currentFrame.getHeight()
                );
            }

            /**
             * 该事件应用于addMouseListener
             *
             * @Author CraneResigned
             * @Date 2024/8/10 17:09:22
             */
            @Override
            public void mousePressed(MouseEvent e) {
                startX = currentFrame.getX();
                startY = currentFrame.getY();
                oldX = e.getXOnScreen();
                oldY = e.getYOnScreen();
            }
        };
        this.add(titleLabel);
        this.addMouseListener(titleDragEvent);
        this.addMouseMotionListener(titleDragEvent);

        ClassLoader classLoader = getClass().getClassLoader();
        //最小化图标
        ImageIcon minimizeIcon = new ImageIcon(Objects.requireNonNull(classLoader.getResource("img/icon/z4.png")));
        JButton miniBtn = new JButton();
        miniBtn.setBackground(Color.decode(colorConfig.get("titleBg")));
        miniBtn.setFocusPainted(false);
        miniBtn.setBorder(null);
        miniBtn.setBounds(currentFrame.getWidth() - 87, 1, 42, 23);
        miniBtn.setIcon(new ImageIcon(minimizeIcon.getImage().getScaledInstance(18, 18, 1)));
        miniBtn.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                currentFrame.setExtendedState(JFrame.HIDE_ON_CLOSE);
            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {
                miniBtn.setBackground(Color.decode(colorConfig.get("miniBtnBgIn")));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                miniBtn.setBackground(Color.decode(colorConfig.get("titleBg")));
            }
        });
        this.add(miniBtn);

        //关闭图标
        ImageIcon icon = new ImageIcon(Objects.requireNonNull(classLoader.getResource("img/icon/5h.png")));
        ImageIcon inIcon = new ImageIcon(Objects.requireNonNull(classLoader.getResource("img/icon/5b.png")));
        JButton closeBtn = new JButton();
        closeBtn.setBackground(Color.decode(colorConfig.get("titleBg")));
        closeBtn.setBounds(currentFrame.getWidth() - 45, 1, 42, 23);
        closeBtn.setFocusPainted(false);
        closeBtn.setBorder(null);
        closeBtn.setIcon(new ImageIcon(icon.getImage().getScaledInstance(18, 18, 1)));
        closeBtn.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                System.exit(0);
            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {
                closeBtn.setBackground(Color.decode(colorConfig.get("closeBtnBgIn")));
                closeBtn.setIcon(new ImageIcon(inIcon.getImage().getScaledInstance(18, 18, 1)));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                closeBtn.setBackground(Color.decode(colorConfig.get("titleBg")));
                closeBtn.setIcon(new ImageIcon(icon.getImage().getScaledInstance(18, 18, 1)));
            }
        });

        this.add(closeBtn);
    }

    public void setTitle(String titleContent) {
        titleLabel.setText(titleContent);
    }

}
