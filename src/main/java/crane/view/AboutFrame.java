package crane.view;

import crane.constant.Constant;
import crane.constant.MainFrameCst;
import crane.constant.VersionCst;
import crane.view.function.config.Config;
import crane.view.function.config.Language;
import crane.view.function.service.ImageService;

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
        this.setSize(550, 300);
        this.setLayout(null);
        this.setResizable(false);
        this.setLocationRelativeTo(null);
        this.setIconImage(ImageService.getTitleImage());

        Config colorConfig = Constant.colorConfig;
        this.getContentPane().setBackground(Color.decode(colorConfig.get("aboutContentPaneBg")));

        //当前页面的字体
        Font currentFrameFont = new Font("微软雅黑", Font.PLAIN, 16);

        //开发者标识
        JLabel developerLabel = new JLabel("<html><b style='color:" + colorConfig.get("developerLabel")
                + ";'>" + Language.get("developer") + "</b>周星学</html>");
        developerLabel.setBounds(30, 15, 450, 30);
        developerLabel.setFont(currentFrameFont);
        this.add(developerLabel);

        //当前版本
        JLabel versionLabel = new JLabel("<html><b style='color:" + colorConfig.get("versionLabel") + ";'>"
                + Language.get("currentVersion") + "</b>"
                + VersionCst.VERSION + "</html>");
        versionLabel.setBounds(30, 55, 450, 30);
        versionLabel.setFont(currentFrameFont);
        this.add(versionLabel);

        //最近更新日期
        JLabel recentlyDate = new JLabel("<html><b style='color:" + colorConfig.get("recentlyDate") + ";'>"
                + Language.get("lastUpdatedDate") + "</b>"
                + VersionCst.RECENTLY_UPDATE_DATE + "</html>");
        recentlyDate.setBounds(30, 95, 450, 30);
        recentlyDate.setFont(currentFrameFont);
        this.add(recentlyDate);

        //声明
        JLabel statement = new JLabel("<html><b style='color:" + colorConfig.get("statement") + ";'>"
                + Language.get("copyrightNotice") + "</b><br/>"
                + Language.get("repositoryAddress")
                + Language.get("copyrightContent") + "</html>");
        statement.setBounds(30, 145, 450, 60);
        statement.setFont(currentFrameFont);

        this.add(statement);

        //版权信息
        JLabel copyrightLabel = new JLabel("<html><b style='color:" + colorConfig.get("copyRight")
                + ";'>Copyright &copy; 2022-2023 Crane Resigned.</b></html>");
        copyrightLabel.setBounds(30, 205, 600, 60);
        copyrightLabel.setFont(new Font("微软雅黑",Font.BOLD,16));
        this.add(copyrightLabel);

        //图标
        JLabel iconLabel = new JLabel();
        iconLabel.setBounds(370, 30, 120, 120);
        ImageIcon icon = new ImageIcon(Objects.requireNonNull(ImageService.getTitleImage()).getScaledInstance(120, 120, Image.SCALE_DEFAULT));
        iconLabel.setIcon(icon);
        this.add(iconLabel);
    }

}
