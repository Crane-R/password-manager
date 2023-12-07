package crane.view;

import cn.hutool.core.util.StrUtil;
import crane.constant.Constant;
import crane.constant.DefaultFont;
import crane.constant.MainFrameCst;
import crane.function.configservice.Language;
import crane.model.bean.Account;
import crane.model.dao.AccountDao;
import crane.model.service.AccountService;
import crane.function.FrameService;
import crane.model.service.SecurityService;
import crane.function.ShowMessgae;
import crane.model.service.lightweight.LightService;
import crane.view.main.MainFrame;
import crane.view.module.stylehelper.BlinkBorderHelper;
import lombok.extern.slf4j.Slf4j;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.LinkedList;
import java.util.Objects;

/**
 * 添加窗口
 *
 * @Author Crane Resigned
 * @Date 2022-04-21 16:02:01
 */
@Slf4j
public class AddFrame extends JFrame {

    /**
     * 验证提示文本
     * Author: Crane Resigned
     * Date: 2022-12-30 21:36:05
     */
    private final String[] OUT_PUT_TEXTS = Language.get("nullTips").split(",");

    /**
     * 字段长度最长数
     *
     * @Author Crane Resigned
     * @Date 2023-02-22 19:31:28
     */
    private final static int[] MAX_LENGTH = {100, 100, 100, 255};

    /**
     * 常量标识符
     * Author: Crane Resigned
     * Date: 2022-11-27 01:16:16
     */
    private final String ADD = Language.get("purposeAdd");
    private final String UPDATE = Language.get("purposeUpdate");
    private final String DELETE = Language.get("purposeDelete");


    public AddFrame(LinkedList<String> list, MainFrame mainFrame) {

        //当前处理账户的id
        Integer currentId = "".equals(list.get(0)) ? null : Integer.valueOf(list.get(0));

        System.out.println("改变窗口传入的list：" + list);

        String purpose = list.get(list.size() - 1);
        this.setTitle(StrUtil.equals(purpose, DELETE) ? Language.get("sureDelete") : MainFrameCst.SIMPLE_TITLE + " >> "
                + purpose + Language.get("aAcount"));
        this.setLayout(null);
        this.setResizable(false);
        this.setSize(420, 400);
        if (StrUtil.equals(purpose, ADD)) {
            this.setLocation(1100, 330);
        } else {
            this.setLocationRelativeTo(null);
        }
        this.getContentPane().setBackground(Color.decode("#EBEBEB"));

        //设置标题栏的图标
        Image image = FrameService.getTitleImage();
        this.setIconImage(image);

        //四个标签和四个输入框
        JLabel jLabel = new JLabel(Language.get("accountName"));
        jLabel.setFont(DefaultFont.WEI_RUAN_PLAIN_13.getFont());
        jLabel.setBounds(30, 25, 100, 40);
        this.add(jLabel);

        JLabel jLabel1 = new JLabel(Language.get("addUsername"));
        jLabel1.setBounds(30, 75, 100, 40);
        jLabel1.setFont(DefaultFont.WEI_RUAN_PLAIN_13.getFont());
        this.add(jLabel1);

        JLabel jLabel2 = new JLabel(Language.get("addPassword"));
        jLabel2.setBounds(30, 125, 100, 40);
        jLabel2.setFont(DefaultFont.WEI_RUAN_PLAIN_13.getFont());
        this.add(jLabel2);

        JLabel surePassLabel = new JLabel(Language.get("surePassword"));
        surePassLabel.setBounds(30, 175, 100, 40);
        surePassLabel.setFont(DefaultFont.WEI_RUAN_PLAIN_13.getFont());
        this.add(surePassLabel);

        JLabel jLabel3 = new JLabel(Language.get("others"));
        jLabel3.setBounds(30, 225, 100, 40);
        jLabel3.setFont(DefaultFont.WEI_RUAN_PLAIN_13.getFont());
        this.add(jLabel3);

        JTextField jTextField = new JTextField(list.get(1));
        jTextField.setBounds(110, 30, 230, 30);
        jTextField.setFont(DefaultFont.WEI_RUAN_PLAIN_13.getFont());
        BlinkBorderHelper.addBorder(jTextField, BorderFactory.createLineBorder(Color.YELLOW, 1)
                , BorderFactory.createLineBorder(Color.decode("#BACCDA"), 1));
        this.add(jTextField);

        JTextField jTextField1 = new JTextField(list.get(2));
        jTextField1.setBounds(110, 80, 230, 30);
        jTextField1.setFont(DefaultFont.WEI_RUAN_PLAIN_13.getFont());
        BlinkBorderHelper.addBorder(jTextField1, BorderFactory.createLineBorder(Color.YELLOW, 1)
                , BorderFactory.createLineBorder(Color.decode("#BACCDA"), 1));
        this.add(jTextField1);

        JTextField jTextField2 = new JTextField(list.get(3));
        jTextField2.setBounds(110, 130, 230, 30);
        jTextField2.setFont(DefaultFont.WEI_RUAN_PLAIN_13.getFont());
        BlinkBorderHelper.addBorder(jTextField2, BorderFactory.createLineBorder(Color.YELLOW, 1)
                , BorderFactory.createLineBorder(Color.decode("#BACCDA"), 1));
        this.add(jTextField2);

        JTextField surePassTextField = new JTextField(list.get(3));
        surePassTextField.setBounds(110, 180, 230, 30);
        surePassTextField.setFont(DefaultFont.WEI_RUAN_PLAIN_13.getFont());
        BlinkBorderHelper.addBorder(surePassTextField, BorderFactory.createLineBorder(Color.YELLOW, 1)
                , BorderFactory.createLineBorder(Color.decode("#BACCDA"), 1));
        this.add(surePassTextField);

        JTextField jTextField3 = new JTextField(list.get(4));
        jTextField3.setBounds(110, 230, 230, 30);
        jTextField3.setFont(DefaultFont.WEI_RUAN_PLAIN_13.getFont());
        BlinkBorderHelper.addBorder(jTextField3, BorderFactory.createLineBorder(Color.YELLOW, 1)
                , BorderFactory.createLineBorder(Color.decode("#BACCDA"), 1));
        this.add(jTextField3);

        //两个按钮
        JButton resetButton = new JButton(Language.get("resetBtn"));
        resetButton.setBounds(40, 290, 80, 30);
        resetButton.setFocusPainted(false);
        resetButton.setForeground(Color.WHITE);
        resetButton.setBackground(Color.decode("#F27635"));
        resetButton.setFont(DefaultFont.WEI_RUAN_PLAIN_13.getFont());
        resetButton.setBorder(null);
        resetButton.addActionListener(e -> {
            //清空所有文本框
            jTextField.setText("");
            jTextField1.setText("");
            jTextField2.setText("");
            jTextField3.setText("");
            surePassTextField.setText("");
        });
        BlinkBorderHelper.addBorder(resetButton,BorderFactory.createLineBorder(Color.WHITE,2),null);
        this.add(resetButton);

        //生成随机强密码按钮
        JButton generatePassBtn = new JButton(Language.get("generateBtn"));
        generatePassBtn.setBounds(140, 290, 100, 30);
        generatePassBtn.setFocusPainted(false);
        generatePassBtn.setForeground(Color.WHITE);
        generatePassBtn.setBackground(Color.decode("#EB89A6"));
        generatePassBtn.setFont(DefaultFont.WEI_RUAN_PLAIN_13.getFont());
        generatePassBtn.setBorder(null);
        generatePassBtn.addActionListener(e -> {
            String randomPassword = SecurityService.generateRandomStrongPassword();
            jTextField2.setText(randomPassword);
            surePassTextField.setText(randomPassword);
        });
        BlinkBorderHelper.addBorder(generatePassBtn,BorderFactory.createLineBorder(Color.WHITE,2),null);
        this.add(generatePassBtn);

        //动态按钮信息
        JButton submitButton = new JButton(purpose);
        submitButton.setBounds(260, 290, 80, 30);
        submitButton.setFocusPainted(false);
        submitButton.setForeground(Color.WHITE);
        submitButton.setBackground(Color.decode("#5697C4"));
        submitButton.setFont(DefaultFont.WEI_RUAN_PLAIN_13.getFont());
        submitButton.setBorder(null);
        BlinkBorderHelper.addBorder(submitButton,BorderFactory.createLineBorder(Color.WHITE,2),null);
        submitButton.addActionListener(e -> {
            //检验确认密码是否和密码相同
            if (!StrUtil.equals(jTextField2.getText(), surePassTextField.getText())) {
                ShowMessgae.showWarningMessage(Language.get("errPassTipMsg"), Language.get("errPassTipTit"));
                return;
            }

            //长度验证
            int[] curLength = {jTextField.getText().length(), jTextField1.getText().length(), jTextField2.getText().length(),
                    jTextField3.getText().length()};
            int len = 4;
            for (int i = 0; i < len; i++) {
                if (curLength[i] > MAX_LENGTH[i]) {
                    ShowMessgae.showErrorMessage(Language.get("maxLengthTipMsg") + MAX_LENGTH[i]
                            + Language.get("maxLengthTipMsg2"), Language.get("maxLengthTipTit"));
                    switch (i) {
                        case 0:
                            jTextField.setText("");
                            return;
                        case 1:
                            jTextField1.setText("");
                            return;
                        case 2:
                            jTextField2.setText("");
                            return;
                        case 3:
                            jTextField3.setText("");
                            return;
                        default:
                            return;
                    }
                }
            }

            //账户为空
            if ("".equals(jTextField.getText())) {
                log.info(OUT_PUT_TEXTS[0]);
                ShowMessgae.showWarningMessage(OUT_PUT_TEXTS[0], "Warning");
                //用户名为空
            } else if ("".equals(jTextField1.getText())) {
                log.info(OUT_PUT_TEXTS[1]);
                ShowMessgae.showWarningMessage(OUT_PUT_TEXTS[1], "Warning");
                //密码为空
            } else if ("".equals(jTextField2.getText())) {
                log.info(OUT_PUT_TEXTS[2]);
                ShowMessgae.showWarningMessage(OUT_PUT_TEXTS[2], "Warning");
            } else {
                Boolean isTrue = null;
                int effect = Integer.MIN_VALUE;
                String accountName = jTextField.getText().trim();
                String username = SecurityService.encodeBase64Salt(jTextField1.getText().trim());
                String password = SecurityService.encodeBase64Salt(jTextField2.getText().trim());
                String other = SecurityService.encodeBase64Salt(jTextField3.getText().trim());
                String userKey = SecurityService.getUuidKey();
                LightService lightService = new LightService();
                if (StrUtil.isBlank(purpose)) {
                    ShowMessgae.showErrorMessage(Language.get("purposeNullTipMsg"), Language.get("purposeNullTipTit"));
                    return;
                }
                if (StrUtil.equals(purpose, ADD)) {
                    //判断是否轻量
                    isTrue = Constant.IS_LIGHT ?
                            lightService.addAccount(accountName, username, password, other, userKey)
                            : new AccountService().addAccount(accountName, username, password, other, userKey);
                } else if (StrUtil.equals(purpose, UPDATE)) {
                    Account account = new Account(currentId, accountName, username, password, other, userKey);
                    log.info("预备修改：" + account);
                    effect = Constant.IS_LIGHT ?
                            lightService.updateAccount(account)
                            : new AccountDao().update(account);
                    log.info("是否修改成功(1为成功)：" + effect);
                } else if (StrUtil.equals(purpose, DELETE)) {
                    Account readyDeleteAccount = new Account(currentId, accountName, username, password, other, userKey);
                    log.info("预备删除：" + readyDeleteAccount);
                    isTrue = Constant.IS_LIGHT ?
                            lightService.deleteAccount(readyDeleteAccount)
                            : new AccountDao().delete(readyDeleteAccount);
                    log.info("是否删除成功(true为成功)：" + (isTrue.equals(false) ? "true" : "false"));
                } else {
                    log.error("未知错误");
                }
                //The Dao add method : return false is true
                if (Boolean.FALSE.equals(isTrue) || effect == 1) {
//                    ShowMessgae.showInformationMessage(purpose + Language.get("successful"), Language.get("successfulTit"));
                    //更新账户数量
                    MainFrame.getResultNumbers().setText(AccountService.getLatestAccountNumberText());
                    log.info(purpose.concat("操作成功"));
                } else {
                    String errorText = Language.get("unKnowErr");
                    JOptionPane.showMessageDialog(null, errorText, "Error", JOptionPane.ERROR_MESSAGE);
                    log.error(errorText);
                }
                //关闭前刷新主界面
                //如果是更新或新增
                if (StrUtil.equals(ADD, purpose) || StrUtil.equals(UPDATE, purpose)) {
                    AccountService.setTableMessages(
                            new Object[][]{{Objects.isNull(currentId) ? Language.get("addSuccessiveTip") : currentId, accountName, username, password, other}}
                    );
                } else if (StrUtil.equals(purpose, DELETE)) {
                    //如果是删除就更新当前搜索的信息
                    if (Constant.IS_LIGHT) {
                        new LightService().searchAndSetTableMsg();
                    } else {
                        AccountService.setTableMessages();
                    }
                }
                this.dispose();
            }
        });
        this.add(submitButton);

        //增加关闭新增窗口的事件
        this.addWindowListener(new WindowListener() {
            @Override
            public void windowOpened(WindowEvent e) {
                log.info("窗口打开");
            }

            @Override
            public void windowClosing(WindowEvent e) {
                log.info("窗口点击关闭");
                //启动活性锁
                FrameService.activeTimeLock();
                mainFrame.setVisible(true);
            }

            @Override
            public void windowClosed(WindowEvent e) {
                log.info("窗口关闭后（调用this.dispose）");
                mainFrame.setVisible(true);
            }

            @Override
            public void windowIconified(WindowEvent e) {
                log.info("窗口最小化");
            }

            @Override
            public void windowDeiconified(WindowEvent e) {
                log.info("窗口取消最小化");
            }

            @Override
            public void windowActivated(WindowEvent e) {
                log.info("窗口激活");
            }

            @Override
            public void windowDeactivated(WindowEvent e) {
                log.info("窗口失活");
            }
        });

    }

}
