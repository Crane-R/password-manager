package crane.view;

import cn.hutool.core.util.StrUtil;
import com.sun.deploy.util.StringUtils;
import crane.constant.Constant;
import crane.model.bean.Account;
import crane.model.dao.AccountDao;
import crane.model.service.AccountService;
import crane.model.service.FrameService;
import lombok.extern.slf4j.Slf4j;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
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
public class AddFrame extends JFrame {

    private final static String[] OUT_PUT_TEXTS = {"账户名不能为空", "用户名不能为空", "密码不能为空"};

    /**
     * 常量标识符
     * Author: Crane Resigned
     * Date: 2022-11-27 01:16:16
     */
    private final String ADD = "新增";
    private final String UPDATE = "更新";
    private final String DELETE = "删除";


    public AddFrame(LinkedList<String> list) {

        //当前处理账户的id
        Integer currentId = "".equals(list.get(0)) ? null : Integer.valueOf(list.get(0));

        System.out.println("改变窗口传入的list：" + list);

        String purpose = list.get(list.size() - 1);
        this.setTitle(StrUtil.equals(purpose, DELETE) ? "真的真的要删除这个账户吗？" : purpose + "账户");
        this.setLayout(null);
        this.setResizable(false);
        this.setSize(400, 400);
        this.setLocationRelativeTo(null);

        //设置标题栏的图标
        Image image = FrameService.getTitleImage();
        this.setIconImage(image);

        //四个标签和四个输入框
        JLabel jLabel = new JLabel("账户");
        jLabel.setBounds(60, 25, 100, 40);
        this.add(jLabel);

        JLabel jLabel1 = new JLabel("用户名");
        jLabel1.setBounds(60, 85, 100, 40);
        this.add(jLabel1);

        JLabel jLabel2 = new JLabel("密码");
        jLabel2.setBounds(60, 145, 100, 40);
        this.add(jLabel2);

        JLabel jLabel3 = new JLabel("说明");
        jLabel3.setBounds(60, 205, 100, 40);
        this.add(jLabel3);

        JTextField jTextField = new JTextField(list.get(1));
        jTextField.setBounds(140, 30, 190, 30);
        this.add(jTextField);

        JTextField jTextField1 = new JTextField(list.get(2));
        jTextField1.setBounds(140, 90, 190, 30);
        this.add(jTextField1);

        JTextField jTextField2 = new JTextField(list.get(3));
        jTextField2.setBounds(140, 150, 190, 30);
        this.add(jTextField2);

        JTextField jTextField3 = new JTextField(list.get(4));
        jTextField3.setBounds(140, 210, 190, 30);
        this.add(jTextField3);

        //两个按钮
        JButton resetButton = new JButton("重置");
        resetButton.setBounds(70, 275, 100, 30);
        resetButton.setFocusPainted(false);
        resetButton.setForeground(Color.WHITE);
        resetButton.setBackground(Color.decode("#F27635"));
        resetButton.setFont(new Font("微软雅黑", Font.PLAIN, 13));
        resetButton.setBorder(null);
        resetButton.addActionListener(e -> {
            //清空所有文本框
            jTextField.setText("");
            jTextField1.setText("");
            jTextField2.setText("");
            jTextField3.setText("");
        });
        this.add(resetButton);

        //动态按钮信息
        JButton submitButton = new JButton(purpose);
        submitButton.setBounds(210, 275, 100, 30);
        submitButton.setFocusPainted(false);
        submitButton.setForeground(Color.WHITE);
        submitButton.setBackground(Color.decode("#5697C4"));
        submitButton.setFont(new Font("微软雅黑", Font.PLAIN, 13));
        submitButton.setBorder(null);
        submitButton.addActionListener(e -> {

            //账户为空
            if ("".equals(jTextField.getText())) {
                log.info(OUT_PUT_TEXTS[0]);
                JOptionPane.showMessageDialog(null, OUT_PUT_TEXTS[0], "Warning", JOptionPane.WARNING_MESSAGE);
                //用户名为空
            } else if ("".equals(jTextField1.getText())) {
                log.info(OUT_PUT_TEXTS[1]);
                JOptionPane.showMessageDialog(null, OUT_PUT_TEXTS[1], "Warning", JOptionPane.WARNING_MESSAGE);
                //密码为空
            } else if ("".equals(jTextField2.getText())) {
                log.info(OUT_PUT_TEXTS[2]);
                JOptionPane.showMessageDialog(null, OUT_PUT_TEXTS[2], "Warning", JOptionPane.WARNING_MESSAGE);
            } else {
                Boolean isTrue = null;
                Integer effect = Integer.MIN_VALUE;
                String id = jTextField.getText().trim();
                String username = AccountService.encodeBase64Salt(jTextField1.getText().trim());
                String password = AccountService.encodeBase64Salt(jTextField2.getText().trim());
                String other = jTextField3.getText().trim();
                String userKey = AccountService.getUuidKey();
                switch (purpose) {
                    case ADD:
                        isTrue = new AccountService().addAccount(id, username, password, other, userKey);
                        break;
                    case UPDATE:
                        Account account = new Account(currentId, id, username, password, other, userKey);
                        System.out.println("预备修改：" + account);
                        effect = new AccountDao().update(account);
                        System.out.println("是否修改成功(1为成功)：" + effect);
                        break;
                    case DELETE:
                        Account readyDeleteAccount = new Account(currentId, id, username, password, other, userKey);
                        System.out.println("预备删除：" + readyDeleteAccount);
                        isTrue = new AccountDao().delete(readyDeleteAccount);
                        System.out.println("是否删除成功(true为成功)：" + (isTrue.equals(false) ? "true" : "false"));
                        break;
                    default:
                        System.out.println("未知错误");
                }
                //The Dao add method : return false is true
                if (Boolean.FALSE.equals(isTrue) || effect == 1) {
                    JOptionPane.showMessageDialog(null, purpose + "成功!", "Successful", JOptionPane.INFORMATION_MESSAGE);
                    //更新账户数量
                    MainFrame.getResultNumbers().setText(AccountService.getLatestAccountNumberText());
                    log.info(purpose.concat("操作成功"));
                } else {
                    String errorText = "遇到了不可预料的错误！";
                    JOptionPane.showMessageDialog(null, errorText, "Error", JOptionPane.ERROR_MESSAGE);
                    log.error(errorText);
                }
                //关闭前刷新主界面
                MainFrame.jTable.setModel(
                        new DefaultTableModel(
                                StrUtil.equals(ADD, purpose) || StrUtil.equals(UPDATE, purpose) ?
                                        new Object[][]{{Objects.isNull(currentId) ?
                                                "唯一标识刷新后查看"
                                                : currentId, id, username, password, other}}
                                        : new Object[0][0], Constant.TITLES));
                this.dispose();
            }
        });
        this.add(submitButton);
    }

}
