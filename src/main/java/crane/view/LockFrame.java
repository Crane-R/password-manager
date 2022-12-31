package crane.view;

import cn.hutool.core.util.StrUtil;
import crane.constant.DefaultFont;
import crane.constant.LockFrameCst;
import crane.constant.MainFrameCst;
import crane.model.jdbc.JDBCConnection;
import crane.model.service.AccountService;
import crane.model.service.FrameService;
import lombok.extern.slf4j.Slf4j;

import javax.swing.*;
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
     * 检测密钥文件是否存在
     * true为有
     * Author: Crane Resigned
     * Date: 2022-11-27 12:04:49
     */
    private static final boolean IS_HAVE_KEY = AccountService.checkKeyFileIsExist();

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

        //窗体初始化
        this.setTitle(IS_HAVE_KEY ? MainFrameCst.MAIN_TITLE : LockFrameCst.HAVE_NOT_SECRET_KEY_TITLE);
        this.setSize(500, 300);
        this.setLayout(null);
        this.setResizable(false);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setIconImage(FrameService.getTitleImage());

        tipLabel = new JLabel(IS_HAVE_KEY ? LockFrameCst.HAVE_SECRET_KEY : LockFrameCst.HAVE_NOT_SECRET_KEY);
        tipLabel.setBounds(50, 50, 400, 40);
        tipLabel.setForeground(Color.decode("#407E54"));
        tipLabel.setFont(new Font("微软雅黑", Font.BOLD, 25));
        this.add(tipLabel);

        secretText = new JPasswordField();
        secretText.setBounds(50, 120, 380, 35);
        secretText.setFont(new Font("微软雅黑", Font.PLAIN, 18));
        secretText.setForeground(Color.decode("#1A5599"));
        secretText.setBorder(BorderFactory.createLineBorder(Color.decode("#B8CE8E")));
        secretText.setHorizontalAlignment(JPasswordField.CENTER);
        enterEvent = new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                //回车触发
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    String passTxt = new String(secretText.getPassword());
                    //是否有密匙
                    if (IS_HAVE_KEY) {
                        log.info("检测匹配");
                        if (!StrUtil.equals(AccountService.getRealKey(), passTxt)) {
                            log.info("密匙错误error");
                            JOptionPane.showMessageDialog(null, "密钥不对(* ￣︿￣)", "星小花★", JOptionPane.WARNING_MESSAGE);
                            return;
                        }
                    } else {
                        log.info("创建密匙");
                        AccountService.createKey(passTxt);
                    }
                    //关闭当前
                    close();
                    //判断是否是测试环境，如果是改一下标题
                    if (JDBCConnection.IS_TEST) {
                        MainFrameCst.MAIN_TITLE = MainFrameCst.MAIN_TITLE + " -- 当前为测试环境，欢迎回来";
                    }
                    new MainFrame().setVisible(true);
                }
            }
        };
        secretText.addKeyListener(enterEvent);
        this.add(secretText);

        //回车登录提示
        loginTip = new JLabel("√Enter");
        loginTip.setBounds(385, 200, 100, 30);
        loginTip.setForeground(Color.decode("#7D2720"));
        loginTip.setFont(new Font("微软雅黑", Font.BOLD, 13));
        this.add(loginTip);

        isLocal = new JToggleButton("是否本地", true);
        isLocal.setBounds(50, 200, 100, 30);
        isLocal.setForeground(Color.decode("#7D2720"));
        isLocal.setFont(DefaultFont.DEFAULT_FONT_ONE.getFont());
        isLocal.setBorder(null);
        isLocal.setFocusPainted(false);
        this.add(isLocal);
    }

    private void close() {
        this.dispose();
    }

}