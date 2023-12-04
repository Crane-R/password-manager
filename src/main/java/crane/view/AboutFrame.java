package crane.view;

import crane.constant.DefaultFont;
import crane.constant.MainFrameCst;
import crane.constant.VersionCst;
import crane.function.Language;
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

        this.setTitle(Language.get("aboutFrameTitle") + MainFrameCst.SIMPLE_TITLE);
        this.setSize(450, 250);
        this.setLayout(null);
        this.setResizable(false);
        this.setLocationRelativeTo(null);
        this.setIconImage(FrameService.getTitleImage());
        this.getContentPane().setBackground(Color.decode("#DAE4E6"));

        //当前页面的字体
        Font currentFrameFont = new Font("微软雅黑", Font.PLAIN, 13);

        //开发者标识
        JLabel developerLabel = new JLabel("<html><b style='color:#382C78;'>" + Language.get("developer") + "</b>周星学</html>");
        developerLabel.setBounds(30, 15, 400, 30);
        developerLabel.setFont(currentFrameFont);
        this.add(developerLabel);

        //当前版本
        JLabel versionLabel = new JLabel("<html><b style='color:#4D5BC6;'>" + Language.get("currentVersion") + "</b>"
                + VersionCst.VERSION + "</html>");
        versionLabel.setBounds(30, 45, 400, 30);
        versionLabel.setFont(currentFrameFont);
        this.add(versionLabel);

        //最近更新日期
        JLabel recentlyDate = new JLabel("<html><b style='color:#024DA1;'>" + Language.get("lastUpdatedDate") + "</b>"
                + VersionCst.RECENTLY_UPDATE_DATE + "</html>");
        recentlyDate.setBounds(30, 75, 400, 30);
        recentlyDate.setFont(currentFrameFont);
        this.add(recentlyDate);

        //声明
        JLabel statement = new JLabel("<html><b style='color:#9A2734;'>" + Language.get("copyrightNotice") + "</b><br/>"
                + Language.get("copyrightContent") + "</html>");
        statement.setBounds(30, 105, 400, 60);
        statement.setFont(currentFrameFont);
        this.add(statement);

        //版权信息
        JLabel copyrightLabel = new JLabel("<html><b style='color:#1A5599;'>Copyright &copy; 2022-2023 Crane Resigned.</b></html>");
        copyrightLabel.setBounds(30, 170, 400, 30);
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
