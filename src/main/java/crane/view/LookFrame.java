package crane.view;

import cn.hutool.core.util.StrUtil;
import crane.constant.Constant;
import crane.model.service.AccountService;
import crane.model.service.FrameService;
import lombok.extern.slf4j.Slf4j;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Arrays;

/**
 * Description: 锁页面
 * Author: ZhouXingxue
 * Date: 2022/11/27 0:45
 *
 * @author Crane Resigned
 */
@Slf4j
public class LookFrame extends JFrame {

    /**
     * 检测密钥文件是否存在
     * true为有
     * Author: Crane Resigned
     * Date: 2022-11-27 12:04:49
     */
    private final static boolean HAVE_KEY = AccountService.checkKeyFileIsExist();

    public LookFrame() {

        this.setTitle(HAVE_KEY ? Constant.MAIN_TITLE : "还没有密匙或密匙被损坏");
        this.setSize(500, 300);
        this.setLayout(null);
        this.setResizable(false);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setIconImage(FrameService.getTitleImage());

        JLabel tipLabel = new JLabel(HAVE_KEY ? "请输入您的密匙" : "检测不到密匙，创建并牢记");
        tipLabel.setBounds(50, 50, 400, 40);
        tipLabel.setForeground(Color.decode("#407E54"));
        tipLabel.setFont(new Font("微软雅黑", Font.BOLD, 25));
        this.add(tipLabel);

        JPasswordField secretText = new JPasswordField();
        secretText.setBounds(50, 120, 380, 35);
        secretText.setFont(new Font("微软雅黑", Font.PLAIN, 18));
        secretText.setForeground(Color.decode("#1A5599"));
        secretText.setBorder(BorderFactory.createLineBorder(Color.decode("#B8CE8E")));
        secretText.setHorizontalAlignment(JPasswordField.CENTER);
        secretText.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                //回车触发
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    String passTxt = new String(secretText.getPassword());
                    //是否有密匙
                    if (HAVE_KEY) {
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
                    close();
                    new MainFrame().setVisible(true);
                }
            }
        });
        this.add(secretText);

        //回车登录提示
        JLabel loginTip = new JLabel("√Enter");
        loginTip.setBounds(385, 200, 100, 30);
        loginTip.setForeground(Color.decode("#7D2720"));
        loginTip.setFont(new Font("微软雅黑", Font.BOLD, 13));
        this.add(loginTip);

    }

    private void close() {
        this.dispose();
    }

}
