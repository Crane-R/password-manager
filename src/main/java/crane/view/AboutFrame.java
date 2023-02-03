package crane.view;

import crane.constant.DefaultFont;
import crane.constant.MainFrameCst;
import crane.constant.VersionCst;
import crane.model.service.FrameService;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;

/**
 * @Description 关于
 * @Author Crane Resigned
 * @Date 2023/2/3 22:52
 */
public class AboutFrame extends JFrame {

    public AboutFrame() {

        this.setTitle("关于 " + MainFrameCst.SIMPLE_TITLE);
        this.setSize(450, 250);
        this.setLayout(null);
        this.setResizable(false);
        this.setLocationRelativeTo(null);
        this.setIconImage(FrameService.getTitleImage());

        //当前页面的字体
        Font currentFrameFont = new Font("微软雅黑", Font.PLAIN, 14);

        //开发者标识
        JLabel developerLabel = new JLabel("<html><b style='color:#58C3C3;'>开发者：</b>周星学</html>");
        developerLabel.setBounds(30, 20, 400, 30);
        developerLabel.setFont(currentFrameFont);
        this.add(developerLabel);

        //当前版本
        JLabel versionLabel = new JLabel("<html><b style='color:#4FA485;'>当前版本：</b>" + VersionCst.VERSION + "</html>");
        versionLabel.setBounds(30, 60, 400, 30);
        versionLabel.setFont(currentFrameFont);
        this.add(versionLabel);

        //最近更新日期
        JLabel recentlyDate = new JLabel("<html><b style='color:#4792B9;'>最近更新日期：</b>" + VersionCst.RECENTLY_UPDATE_DATE + "</html>");
        recentlyDate.setBounds(30, 100, 400, 30);
        recentlyDate.setFont(currentFrameFont);
        this.add(recentlyDate);

        //版权信息
        JLabel copyrightLabel = new JLabel("<html><b style='color:#1A5599;'>Copyright &copy; 2022-2023 Crane Resigned.</b></html>");
        copyrightLabel.setBounds(30, 160, 400, 30);
        copyrightLabel.setFont(new Font("微软雅黑", Font.PLAIN, 13));
        this.add(copyrightLabel);

        //图标
        JLabel iconLabel = new JLabel();
        iconLabel.setBounds(280, 20, 120, 120);
        ImageIcon icon = new ImageIcon(Objects.requireNonNull(FrameService.getTitleImage()).getScaledInstance(120, 120, Image.SCALE_DEFAULT));
        iconLabel.setIcon(icon);
        this.add(iconLabel);
    }

}
