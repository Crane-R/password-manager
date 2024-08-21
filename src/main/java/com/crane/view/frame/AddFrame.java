package com.crane.view.frame;

import cn.hutool.core.util.StrUtil;
import com.crane.constant.Constant;
import com.crane.constant.DefaultFont;
import com.crane.constant.MainFrameCst;
import com.crane.model.bean.Account;
import com.crane.model.dao.AccountDao;
import com.crane.model.service.AccountService;
import com.crane.model.service.LightService;
import com.crane.view.config.Language;
import com.crane.view.frame.module.CustomFrame;
import com.crane.view.service.MessageService;
import com.crane.view.tools.ShowMessage;
import com.crane.view.frame.module.stylehelper.BlinkBorderHelper;
import com.crane.view.service.ActiveTimeService;
import com.crane.model.service.SecurityService;
import lombok.extern.slf4j.Slf4j;

import javax.swing.*;
import java.awt.*;
import java.util.LinkedList;
import java.util.Objects;

/**
 * 添加窗口
 *
 * @Author Crane Resigned
 * @Date 2022-04-21 16:02:01
 */
@Slf4j
public class AddFrame extends CustomFrame {

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
        super(410, 390, e -> {

            //启动活性锁
            if (!MainFrame.getActivistLockBtn().isSelected()) {
                ActiveTimeService.activeTimeLock();
            }
            mainFrame.setVisible(true);
        });

        //当前处理账户的id
        Integer currentId = "".equals(list.get(0)) ? null : Integer.valueOf(list.get(0));

        System.out.println("改变窗口传入的list：" + list);

        String purpose = list.get(list.size() - 1);
        this.setTitle(StrUtil.equals(purpose, DELETE) ? Language.get("sureDelete") : MainFrameCst.SIMPLE_TITLE + " - "
                + purpose + Language.get("aAcount"));
        if (StrUtil.equals(purpose, ADD)) {
            this.setLocation(1100, 330);
        } else {
            this.setLocationRelativeTo(null);
        }
        this.getContentPane().setBackground(Color.decode(colorConfig.get("addContentPaneBg")));

        //四个标签和四个输入框
        JLabel accountNameLabel = new JLabel(Language.get("accountName"));
        accountNameLabel.setFont(DefaultFont.WEI_RUAN_PLAIN_15.getFont());
        accountNameLabel.setBounds(30, 51, 100, 40);
        this.add(accountNameLabel);

        JLabel userNameLabel = new JLabel(Language.get("addUsername"));
        userNameLabel.setBounds(30, 101, 100, 40);
        userNameLabel.setFont(DefaultFont.WEI_RUAN_PLAIN_15.getFont());
        this.add(userNameLabel);

        JLabel passwordLabel = new JLabel(Language.get("addPassword"));
        passwordLabel.setBounds(30, 151, 100, 40);
        passwordLabel.setFont(DefaultFont.WEI_RUAN_PLAIN_15.getFont());
        this.add(passwordLabel);

        JLabel surePassLabel = new JLabel(Language.get("surePassword"));
        surePassLabel.setBounds(30, 201, 100, 40);
        surePassLabel.setFont(DefaultFont.WEI_RUAN_PLAIN_15.getFont());
        this.add(surePassLabel);

        JLabel otherLabel = new JLabel(Language.get("others"));
        otherLabel.setBounds(30, 251, 100, 40);
        otherLabel.setFont(DefaultFont.WEI_RUAN_PLAIN_15.getFont());
        this.add(otherLabel);

        JTextField accountNameTextField = new JTextField(list.get(1));
        accountNameTextField.setBounds(110, 56, 260, 30);
        accountNameTextField.setFont(DefaultFont.WEI_RUAN_PLAIN_15.getFont());
        BlinkBorderHelper.addBorder(accountNameTextField, BorderFactory.createLineBorder(Color.decode(colorConfig.get("textFieldBlinkBorIn1")), 2)
                , BorderFactory.createLineBorder(Color.decode(colorConfig.get("textFieldBlinkBorOut1")), 1));
        this.add(accountNameTextField);

        JTextField usernameTextField = new JTextField(list.get(2));
        usernameTextField.setBounds(110, 106, 260, 30);
        usernameTextField.setFont(DefaultFont.WEI_RUAN_PLAIN_15.getFont());
        BlinkBorderHelper.addBorder(usernameTextField, BorderFactory.createLineBorder(Color.decode(colorConfig.get("textFieldBlinkBorIn2")), 2)
                , BorderFactory.createLineBorder(Color.decode(colorConfig.get("textFieldBlinkBorOut2")), 1));
        this.add(usernameTextField);

        JTextField passwordTextField = new JTextField(list.get(3));
        passwordTextField.setBounds(110, 156, 260, 30);
        passwordTextField.setFont(DefaultFont.WEI_RUAN_PLAIN_15.getFont());
        BlinkBorderHelper.addBorder(passwordTextField, BorderFactory.createLineBorder(Color.decode(colorConfig.get("textFieldBlinkBorIn3")), 2)
                , BorderFactory.createLineBorder(Color.decode(colorConfig.get("textFieldBlinkBorOut3")), 1));
        this.add(passwordTextField);

        JTextField surePassTextField = new JTextField(list.get(3));
        surePassTextField.setBounds(110, 206, 260, 30);
        surePassTextField.setFont(DefaultFont.WEI_RUAN_PLAIN_15.getFont());
        BlinkBorderHelper.addBorder(surePassTextField, BorderFactory.createLineBorder(Color.decode(colorConfig.get("surePassBlinkBorIn")), 2)
                , BorderFactory.createLineBorder(Color.decode(colorConfig.get("surePassBlinkBorOut")), 1));
        this.add(surePassTextField);

        JTextField otherTextField = new JTextField(list.get(4));
        otherTextField.setBounds(110, 256, 260, 30);
        otherTextField.setFont(DefaultFont.WEI_RUAN_PLAIN_15.getFont());
        BlinkBorderHelper.addBorder(otherTextField, BorderFactory.createLineBorder(Color.decode(colorConfig.get("textFieldBlinkBorIn4")), 2)
                , BorderFactory.createLineBorder(Color.decode(colorConfig.get("textFieldBlinkBorOut4")), 1));
        this.add(otherTextField);

        //两个按钮
        JButton resetButton = new JButton(Language.get("resetBtn"));
        resetButton.setBounds(30, 316, 100, 30);
        resetButton.setFocusPainted(false);
        resetButton.setForeground(Color.decode(colorConfig.get("resetBtnFore")));
        resetButton.setBackground(Color.decode(colorConfig.get("resetBtnBg")));
        resetButton.setFont(DefaultFont.WEI_RUAN_PLAIN_15.getFont());
        resetButton.setBorder(null);
        resetButton.addActionListener(e -> {
            //清空所有文本框
            accountNameTextField.setText("");
            usernameTextField.setText("");
            passwordTextField.setText("");
            otherTextField.setText("");
            surePassTextField.setText("");
        });
        BlinkBorderHelper.addBorder(resetButton, BorderFactory.createLineBorder(Color.decode(colorConfig.get("resetBtnBlinkBorIn")), 2), null);
        this.add(resetButton);

        //生成随机强密码按钮
        JButton generatePassBtn = new JButton(Language.get("generateBtn"));
        generatePassBtn.setBounds(150, 316, 100, 30);
        generatePassBtn.setFocusPainted(false);
        generatePassBtn.setForeground(Color.decode(colorConfig.get("generateBtnFore")));
        generatePassBtn.setBackground(Color.decode(colorConfig.get("generateBtnBg")));
        generatePassBtn.setFont(DefaultFont.WEI_RUAN_PLAIN_15.getFont());
        generatePassBtn.setBorder(null);
        generatePassBtn.addActionListener(e -> {
            String randomPassword = SecurityService.generateRandomStrongPassword();
            passwordTextField.setText(randomPassword);
            surePassTextField.setText(randomPassword);
        });
        BlinkBorderHelper.addBorder(generatePassBtn, BorderFactory.createLineBorder(Color.decode(colorConfig.get("generateBtnBlinkBorIn")), 2), null);
        this.add(generatePassBtn);

        //动态按钮信息
        JButton submitButton = new JButton(purpose);
        submitButton.setBounds(270, 316, 100, 30);
        submitButton.setFocusPainted(false);
        submitButton.setForeground(Color.decode(colorConfig.get("submitBtnFore")));
        submitButton.setBackground(Color.decode(colorConfig.get("submitBtnBg")));
        submitButton.setFont(DefaultFont.WEI_RUAN_PLAIN_15.getFont());
        submitButton.setBorder(null);
        BlinkBorderHelper.addBorder(submitButton, BorderFactory.createLineBorder(Color.decode(colorConfig.get("submitBtnBlinkBorIn")), 2), null);
        submitButton.addActionListener(e -> {
            //检验确认密码是否和密码相同
            if (!StrUtil.equals(passwordTextField.getText(), surePassTextField.getText())) {
                ShowMessage.showWarningMessage(Language.get("errPassTipMsg"), Language.get("errPassTipTit"));
                return;
            }

            //长度验证
            int[] curLength = {accountNameTextField.getText().length(), usernameTextField.getText().length(), passwordTextField.getText().length(),
                    otherTextField.getText().length()};
            int len = 4;
            for (int i = 0; i < len; i++) {
                if (curLength[i] > MAX_LENGTH[i]) {
                    ShowMessage.showErrorMessage(Language.get("maxLengthTipMsg") + MAX_LENGTH[i]
                            + Language.get("maxLengthTipMsg2"), Language.get("maxLengthTipTit"));
                    switch (i) {
                        case 0:
                            accountNameTextField.setText("");
                            return;
                        case 1:
                            usernameTextField.setText("");
                            return;
                        case 2:
                            passwordTextField.setText("");
                            return;
                        case 3:
                            otherTextField.setText("");
                            return;
                        default:
                            return;
                    }
                }
            }

            //账户为空
            if ("".equals(accountNameTextField.getText())) {
                log.info(OUT_PUT_TEXTS[0]);
                ShowMessage.showWarningMessage(OUT_PUT_TEXTS[0], "Warning");
                //用户名为空
            } else if ("".equals(usernameTextField.getText())) {
                log.info(OUT_PUT_TEXTS[1]);
                ShowMessage.showWarningMessage(OUT_PUT_TEXTS[1], "Warning");
                //密码为空
            } else if ("".equals(passwordTextField.getText())) {
                log.info(OUT_PUT_TEXTS[2]);
                ShowMessage.showWarningMessage(OUT_PUT_TEXTS[2], "Warning");
            } else {
                Boolean isTrue = null;
                int effect = Integer.MIN_VALUE;
                String accountName = accountNameTextField.getText().trim();
                String username = SecurityService.encodeBase64Salt(usernameTextField.getText().trim());
                String password = SecurityService.encodeBase64Salt(passwordTextField.getText().trim());
                String other = SecurityService.encodeBase64Salt(otherTextField.getText().trim());
                String userKey = SecurityService.getUuidKey();
                LightService lightService = new LightService();
                if (StrUtil.isBlank(purpose)) {
                    ShowMessage.showErrorMessage(Language.get("purposeNullTipMsg"), Language.get("purposeNullTipTit"));
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
                    log.info(purpose.concat("操作成功"));
                    MessageService.outputMessage(purpose.concat("操作成功"));
                } else {
                    String errorText = Language.get("unKnowErr");
                    ShowMessage.showErrorMessage(errorText, "Error");
                    log.error(errorText);
                    MessageService.outputMessage(errorText);
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
                //启动活性锁
                if (!MainFrame.getActivistLockBtn().isSelected()) {
                    ActiveTimeService.activeTimeLock();
                }
                mainFrame.setVisible(true);
            }
        });
        this.add(submitButton);

    }

}
