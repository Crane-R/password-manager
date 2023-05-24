package crane.view;

import cn.hutool.core.util.StrUtil;
import crane.constant.Constant;
import crane.constant.DefaultFont;
import crane.model.bean.Account;
import crane.model.dao.AccountDao;
import crane.model.service.AccountService;
import crane.model.service.FrameService;
import crane.model.service.SecurityService;
import crane.model.service.ShowMessgae;
import crane.model.service.lightweight.LightService;
import lombok.extern.slf4j.Slf4j;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
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
    private final static String[] OUT_PUT_TEXTS = {"账户名不能为空", "用户名不能为空", "密码不能为空"};

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
    private final String ADD = "新增";
    private final String UPDATE = "更新";
    private final String DELETE = "删除";


    public AddFrame(LinkedList<String> list, MainFrame mainFrame) {

        //当前处理账户的id
        Integer currentId = "".equals(list.get(0)) ? null : Integer.valueOf(list.get(0));

        System.out.println("改变窗口传入的list：" + list);

        String purpose = list.get(list.size() - 1);
        this.setTitle(StrUtil.equals(purpose, DELETE) ? "真的真的要移除这个账户吗？" : purpose + "一个账户");
        this.setLayout(null);
        this.setResizable(false);
        this.setSize(400, 400);
        if (StrUtil.equals(purpose, ADD)) {
            this.setLocation(1100, 330);
        } else {
            this.setLocationRelativeTo(null);
        }
        this.getContentPane().setBackground(Color.decode("#DAE4E6"));

        //设置标题栏的图标
        Image image = FrameService.getTitleImage();
        this.setIconImage(image);

        //四个标签和四个输入框
        JLabel jLabel = new JLabel("账号名：");
        jLabel.setFont(DefaultFont.WEI_RUAN_PLAIN_13.getFont());
        jLabel.setBounds(30, 25, 100, 40);
        this.add(jLabel);

        JLabel jLabel1 = new JLabel("用户名：");
        jLabel1.setBounds(30, 85, 100, 40);
        jLabel1.setFont(DefaultFont.WEI_RUAN_PLAIN_13.getFont());
        this.add(jLabel1);

        JLabel jLabel2 = new JLabel("明文密码：");
        jLabel2.setBounds(30, 145, 100, 40);
        jLabel2.setFont(DefaultFont.WEI_RUAN_PLAIN_13.getFont());
        this.add(jLabel2);

        JLabel jLabel3 = new JLabel("其他信息：");
        jLabel3.setBounds(30, 205, 100, 40);
        jLabel3.setFont(DefaultFont.WEI_RUAN_PLAIN_13.getFont());
        this.add(jLabel3);

        JTextField jTextField = new JTextField(list.get(1));
        jTextField.setBounds(110, 30, 230, 30);
        jTextField.setFont(DefaultFont.WEI_RUAN_PLAIN_13.getFont());
        this.add(jTextField);

        JTextField jTextField1 = new JTextField(list.get(2));
        jTextField1.setBounds(110, 90, 230, 30);
        jTextField1.setFont(DefaultFont.WEI_RUAN_PLAIN_13.getFont());
        this.add(jTextField1);

        JTextField jTextField2 = new JTextField(list.get(3));
        jTextField2.setBounds(110, 150, 230, 30);
        jTextField2.setFont(DefaultFont.WEI_RUAN_PLAIN_13.getFont());
        jTextField2.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {

            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {
                log.info("新增页面鼠标移入");
            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });
        this.add(jTextField2);

        JTextField jTextField3 = new JTextField(list.get(4));
        jTextField3.setBounds(110, 210, 230, 30);
        jTextField3.setFont(DefaultFont.WEI_RUAN_PLAIN_13.getFont());
        this.add(jTextField3);

        //两个按钮
        JButton resetButton = new JButton("重置");
        resetButton.setBounds(40, 275, 80, 30);
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
        });
        this.add(resetButton);

        //生成随机强密码按钮
        JButton generatePassBtn = new JButton("生成密码");
        generatePassBtn.setBounds(140, 275, 100, 30);
        generatePassBtn.setFocusPainted(false);
        generatePassBtn.setForeground(Color.WHITE);
        generatePassBtn.setBackground(Color.decode("#EB89A6"));
        generatePassBtn.setFont(DefaultFont.WEI_RUAN_PLAIN_13.getFont());
        generatePassBtn.setBorder(null);
        generatePassBtn.addActionListener(e -> {
            jTextField2.setText(SecurityService.generateRandomStrongPassword());
        });
        this.add(generatePassBtn);

        //动态按钮信息
        JButton submitButton = new JButton(purpose);
        submitButton.setBounds(260, 275, 80, 30);
        submitButton.setFocusPainted(false);
        submitButton.setForeground(Color.WHITE);
        submitButton.setBackground(Color.decode("#5697C4"));
        submitButton.setFont(DefaultFont.WEI_RUAN_PLAIN_13.getFont());
        submitButton.setBorder(null);
        submitButton.addActionListener(e -> {

            //长度验证
            int[] curLength = {jTextField.getText().length(), jTextField1.getText().length(), jTextField2.getText().length(), jTextField3.getText().length()};
            int len = 4;
            for (int i = 0; i < len; i++) {
                if (curLength[i] > MAX_LENGTH[i]) {
                    ShowMessgae.showErrorMessage("长度限制为" + MAX_LENGTH[i] + "个字符", "超长错误");
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
                switch (purpose) {
                    case ADD:
                        //判断是否轻量
                        isTrue = Constant.IS_LIGHT ?
                                lightService.addAccount(accountName, username, password, other, userKey)
                                : new AccountService().addAccount(accountName, username, password, other, userKey);
                        break;
                    case UPDATE:
                        Account account = new Account(currentId, accountName, username, password, other, userKey);
                        log.info("预备修改：" + account);
                        effect = Constant.IS_LIGHT ?
                                lightService.updateAccount(account)
                                : new AccountDao().update(account);
                        log.info("是否修改成功(1为成功)：" + effect);
                        break;
                    case DELETE:
                        Account readyDeleteAccount = new Account(currentId, accountName, username, password, other, userKey);
                        log.info("预备删除：" + readyDeleteAccount);
                        isTrue = Constant.IS_LIGHT ?
                                lightService.deleteAccount(readyDeleteAccount)
                                : new AccountDao().delete(readyDeleteAccount);
                        log.info("是否删除成功(true为成功)：" + (isTrue.equals(false) ? "true" : "false"));
                        break;
                    default:
                        log.error("未知错误");
                }
                //The Dao add method : return false is true
                if (Boolean.FALSE.equals(isTrue) || effect == 1) {
                    ShowMessgae.showInformationMessage(purpose + "成功!", "Successful");
                    //更新账户数量
                    MainFrame.getResultNumbers().setText(AccountService.getLatestAccountNumberText());
                    log.info(purpose.concat("操作成功"));
                } else {
                    String errorText = "遇到了不可预料的错误！";
                    JOptionPane.showMessageDialog(null, errorText, "Error", JOptionPane.ERROR_MESSAGE);
                    log.error(errorText);
                }
                //关闭前刷新主界面
                //如果是更新或新增
                if (StrUtil.equals(ADD, purpose) || StrUtil.equals(UPDATE, purpose)) {
                    AccountService.setTableMessages(
                            new Object[][]{{Objects.isNull(currentId) ? "唯一标识刷新后查看" : currentId, accountName, username, password, other}}
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
