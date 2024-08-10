package com.crane.view;

import cn.hutool.core.date.DateUtil;
import com.crane.constant.VersionCst;
import com.crane.constant.Constant;
import com.crane.constant.MainFrameCst;
import com.crane.view.function.config.Config;
import com.crane.view.function.config.Language;
import com.crane.view.function.service.ImageService;
import lombok.SneakyThrows;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URL;
import java.util.Objects;

/**
 * @Description 关于
 * @Author Crane Resigned
 * @Date 2023/2/3 22:52
 */
public class AboutFrame extends CustomFrame {

    public AboutFrame() {
        super(550, 260, null);

        this.setTitle(Language.get("aboutFrameTitle") + MainFrameCst.SIMPLE_TITLE);
        this.getContentPane().setBackground(Color.decode(colorConfig.get("aboutContentPaneBg")));

        //当前页面的字体
        Font currentFrameFont = new Font("微软雅黑", Font.PLAIN, 17);

        //开发者标识
        JLabel developerLabel = new JLabel("<html><b style='color:" + colorConfig.get("developerLabel")
                + ";'>" + Language.get("developer") + "</b>周星学</html>");
        developerLabel.setBounds(30, 41, 450, 40);
        developerLabel.setFont(currentFrameFont);
        this.add(developerLabel);

        //当前版本
        JLabel versionLabel = new JLabel("<html><b style='color:" + colorConfig.get("versionLabel") + ";'>"
                + Language.get("currentVersion") + "</b>"
                + VersionCst.VERSION + "</html>");
        versionLabel.setBounds(30, 81, 450, 40);
        versionLabel.setFont(currentFrameFont);
        this.add(versionLabel);

        //最近更新日期
        JLabel recentlyDate = new JLabel("<html><b style='color:" + colorConfig.get("recentlyDate") + ";'>"
                + Language.get("lastUpdatedDate") + "</b>"
                + VersionCst.RECENTLY_UPDATE_DATE + "</html>");
        recentlyDate.setBounds(30, 121, 450, 40);
        recentlyDate.setFont(currentFrameFont);
        this.add(recentlyDate);

        //声明
        JLabel statement = new JLabel("<html><b style='color:" + colorConfig.get("statement") + ";'>"
                + Language.get("copyrightNotice") + "</b><a href='"
                + Language.get("repositoryAddress") + "'>"
                + Language.get("copyrightContent") + "</a>"
                + Language.get("copyrightTip")
                + "</html>");
        statement.addMouseListener(new MouseAdapter() {
            @SneakyThrows
            @Override
            public void mouseClicked(MouseEvent e) {
                URL repositoryAddress = new URL(Language.get("repositoryAddress"));
                Desktop.getDesktop().browse(repositoryAddress.toURI());
            }
        });
        statement.setBounds(30, 161, 450, 40);
        statement.setFont(currentFrameFont);

        this.add(statement);

        //版权信息
        JLabel copyrightLabel = new JLabel("<html><b style='color:" + colorConfig.get("copyRight")
                + ";'>Copyright &copy; 2022-" + DateUtil.year(DateUtil.date()) + " Crane Resigned.</b></html>");
        copyrightLabel.setBounds(30, 201, 600, 40);
        copyrightLabel.setFont(new Font("微软雅黑", Font.BOLD, 16));
        this.add(copyrightLabel);

        //图标
        JLabel iconLabel = new JLabel();
        iconLabel.setBounds(370, 56, 120, 120);
        ImageIcon icon = new ImageIcon(Objects.requireNonNull(ImageService.getTitleImage()).getScaledInstance(120, 120, Image.SCALE_DEFAULT));
        iconLabel.setIcon(icon);
        this.add(iconLabel);
    }

}
