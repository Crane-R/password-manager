package crane.view;

import cn.hutool.core.date.DateUtil;
import crane.constant.Constant;
import crane.constant.DefaultFont;
import crane.constant.LockFrameCst;
import crane.constant.MainFrameCst;
import crane.model.service.FrameService;
import crane.model.service.SecurityService;
import crane.model.service.ShowMessgae;
import lombok.extern.slf4j.Slf4j;

import javax.swing.*;
import javax.swing.plaf.metal.MetalToggleButtonUI;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/**
 * Description: 锁页面
 * Author: ZhouXingxue
 * Date: 2022/11/27 0:45
 *
 * @author Crane Resigned
 */
@Slf4j
public class LockFrame extends JFrame {

    /**
     * 是否本地开关
     * Author: Crane Resigned
     * Date: 2022-12-01 20:43:35
     */
    public static JToggleButton isLocal;

    /**
     * 是否创建新密钥登录开关
     * Author: Crane Resigned
     * Date: 2023-01-07 23:20:41
     */
    public static JToggleButton isCreateScene;

    /**
     * 是否启动轻量版
     * Author: Crane Resigned
     * Date: 2023-01-29 17:38:59
     */
    public static JToggleButton isLightWeightVersion;

    /**
     * 提示文本
     * Author: Crane Resigned
     * Date: 2022-12-30 23:36:25
     */
    protected JLabel tipLabel;

    /**
     * 密码文本
     * Author: Crane Resigned
     * Date: 2022-12-30 23:48:46
     */
    protected JPasswordField secretText;

    /**
     * 回车事件
     * Author: Crane Resigned
     * Date: 2022-12-30 23:50:51
     */
    protected KeyAdapter enterEvent;

    /**
     * 回车登录提示
     * Author: Crane Resigned
     * Date: 2022-12-30 23:57:40
     */
    protected JLabel loginTip;

    public LockFrame() {
        //检测密钥文件是否存在
        final boolean isHaveKey = SecurityService.checkKeyAmountIsNotZero();

        //窗体初始化
        this.setTitle(isHaveKey ? MainFrameCst.MAIN_TITLE : LockFrameCst.HAVE_NOT_SECRET_KEY_TITLE);
        this.setSize(500, 300);
        this.setLayout(null);
        this.setResizable(false);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setIconImage(FrameService.getTitleImage());
        this.getContentPane().setBackground(Color.decode("#DAE4E6"));

        tipLabel = new JLabel(isHaveKey ? LockFrameCst.HAVE_SECRET_KEY : LockFrameCst.HAVE_NOT_SECRET_KEY);
        tipLabel.setBounds(50, 50, 400, 40);
        tipLabel.setForeground(Color.decode("#407E54"));
        tipLabel.setFont(new Font("微软雅黑", Font.BOLD, 25));
        this.add(tipLabel);

        secretText = new JPasswordField();
        secretText.setBounds(50, 120, 380, 35);
        secretText.setFont(new Font("微软雅黑", Font.PLAIN, 18));
        secretText.setForeground(Color.decode("#046D35"));
        secretText.setBorder(BorderFactory.createLineBorder(Color.decode("#B8CE8E")));
        secretText.setHorizontalAlignment(JPasswordField.CENTER);
        enterEvent = new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                //回车触发
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    String passTxt = new String(secretText.getPassword());
                    //是否为创建新密钥登录
                    if (isCreateScene.isSelected()) {
                        SecurityService.createKey(passTxt);
                    } else {
                        //是否有密匙
                        if (isHaveKey) {
                            log.info("检测匹配");
                            //检测该密钥是否存在
                            if (!SecurityService.checkKeyFileIsExist(passTxt)) {
                                log.info("匹配失败，密钥文件不存在");
                                ShowMessgae.showWarningMessage("密钥错误", "未知密钥");
                                secretText.setText(null);
                                return;
                            }
                        } else {
                            log.info("一个密匙都没有，创建密匙");
                            SecurityService.createKey(passTxt);
                        }
                    }
                    log.info("密钥匹配成功" + DateUtil.now());
                    //关闭当前
                    close();
                    //赋值轻量版标识
                    Constant.IS_LIGHT = isLightWeightVersion.isSelected();
                    new MainFrame().setVisible(true);
                }
            }
        };
        secretText.addKeyListener(enterEvent);
        this.add(secretText);

        //回车登录提示
        loginTip = new JLabel("√Enter");
        loginTip.setBounds(385, 155, 100, 30);
        loginTip.setForeground(Color.decode("#7D2720"));
        loginTip.setFont(new Font("微软雅黑", Font.BOLD, 13));
        this.add(loginTip);

        //是否是本地数据库
        isLocal = new JToggleButton("本地数据库", true);
        isLocal.setBounds(50, 200, 100, 30);
        isLocal.setForeground(Color.WHITE);
        isLocal.setFont(DefaultFont.WEI_RUAN_PLAIN_13.getFont());
        isLocal.setBackground(Color.decode("#68A694"));
        //设置选中的颜色
        isLocal.setUI(new MetalToggleButtonUI() {
            @Override
            protected Color getSelectColor() {
                return Color.decode("#046D35");
            }
        });
        isLocal.setBorder(null);
        isLocal.addActionListener(e -> {
            ShowMessgae.showWarningMessage("当前服务器已过期，无法开启此选项", "服务器无法使用");
            isLocal.setSelected(true);
        });
        this.add(isLocal);

        isCreateScene = new JToggleButton("以新密钥登录");
        isCreateScene.setBounds(180, 200, 110, 30);
        isCreateScene.setForeground(Color.WHITE);
        isCreateScene.setFont(DefaultFont.WEI_RUAN_PLAIN_13.getFont());
        isCreateScene.setUI(new MetalToggleButtonUI() {
            @Override
            protected Color getSelectColor() {
                return Color.decode("#2A3050");
            }
        });
        isCreateScene.setBorder(null);
        isCreateScene.setBackground(Color.decode("#65BAD1"));
        isCreateScene.addActionListener(e -> {
            if (isCreateScene.isSelected()) {
                ShowMessgae.showInformationMessage("创建一个新密钥以启动新场景。密钥隔离，不可访问其他场景账户。", "以新密钥登录");
            }
        });
        this.add(isCreateScene);

        isLightWeightVersion = new JToggleButton("数据库模式");
        isLightWeightVersion.setBounds(320, 200, 100, 30);
        isLightWeightVersion.setForeground(Color.WHITE);
        isLightWeightVersion.setFont(DefaultFont.WEI_RUAN_PLAIN_13.getFont());
        isLightWeightVersion.setUI(new MetalToggleButtonUI() {
            @Override
            protected Color getSelectColor() {
                return Color.decode("#9AB3CD");
            }
        });
        isLightWeightVersion.setBorder(null);
        isLightWeightVersion.setBackground(Color.decode("#407E54"));
        isLightWeightVersion.addActionListener(e -> {
            if (isLightWeightVersion.isSelected()) {
                isLightWeightVersion.setText("轻量模式");
                ShowMessgae.showPlainMessage(
                        "此版本无需使用数据库，凭借此程序即可使用，但会有些功能不可用\r\n\n" +
                                "因为轻量模式开发时间短，未经严格的测试，建议临时使用\r\n\n" +
                                "源文件在resources/light_weight_data目录下，可随时移走",
                        MainFrameCst.SIMPLE_TITLE + " 轻量版");
            } else {
                isLightWeightVersion.setText("数据库模式");
                ShowMessgae.showPlainMessage("请确保本机安装有MySQL服务，数据库所需配置见说明文档", "数据库模式");
            }
        });
        this.add(isLightWeightVersion);
    }

    private void close() {
        this.dispose();
    }

}
