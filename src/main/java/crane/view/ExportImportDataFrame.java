package crane.view;

import cn.hutool.core.util.StrUtil;
import com.alibaba.excel.EasyExcel;
import crane.constant.ExportImportCst;
import crane.constant.DefaultFont;
import crane.model.bean.Account;
import crane.model.dao.AccountDao;
import crane.model.jdbc.JdbcConnection;
import crane.model.service.AccountService;
import crane.model.service.ExcelFileFilter;
import crane.model.service.ExcelService;
import crane.model.service.SecurityService;
import lombok.extern.slf4j.Slf4j;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.*;
import java.net.URLDecoder;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Properties;

/**
 * Description: 导出数据窗口
 * Author: ZhouXingxue
 * Date: 2022/12/30 23:30
 *
 * @author Crane Resigned
 */
@Slf4j
public class ExportImportDataFrame extends LockFrame {

    protected JTextField pathTextField;

    public ExportImportDataFrame(ExportImportCst exportImportCst) {
        super();
        this.setTitle(exportImportCst.TITLE);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        //修改提示文本
        tipLabel.setText(exportImportCst.TIP_LABEL);
        tipLabel.setFont(DefaultFont.DEFAULT_FONT_ONE.setStyle(Font.BOLD).setFontSize(18).getFont());

        //移除是否本地
        this.remove(isLocal);

        //移除是否创建新密钥登录
        this.remove(isCreateScene);

        //移除密码框
        this.remove(secretText);

        //移除回车标
        this.remove(loginTip);

        //文本框
        pathTextField = new JTextField();
        pathTextField.setBounds(50, 120, 380, 35);
        pathTextField.setFont(DefaultFont.DEFAULT_FONT_ONE.setStyle(Font.BOLD).setFontSize(13).getFont());
        pathTextField.setForeground(Color.decode("#1A5599"));
        pathTextField.setBorder(BorderFactory.createLineBorder(Color.decode("#B8CE8E")));
        pathTextField.setHorizontalAlignment(JPasswordField.CENTER);
        pathTextField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                //回车触发
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    exportFile();
                }
            }
        });
        this.add(pathTextField);

        //文件选择器
        JButton chooseFile = new JButton("文件选择器");
        chooseFile.setBounds(50, 200, 100, 30);
        chooseFile.setForeground(Color.decode("#FFFFFF"));
        chooseFile.setFont(DefaultFont.DEFAULT_FONT_ONE.setFontSize(12).setStyle(Font.BOLD).getFont());
        chooseFile.setBorder(null);
        chooseFile.setFocusPainted(false);
        chooseFile.setBackground(Color.decode("#BBCBDC"));
        chooseFile.addActionListener(e -> {
            JFileChooser chooser = new JFileChooser(getRecentlyPath());
            chooser.setSize(1000, 600);
            //导出为只选择目录模式，导入为只选择文件模式
            chooser.setFileSelectionMode(exportImportCst.IS_EXPORT ? JFileChooser.DIRECTORIES_ONLY : JFileChooser.FILES_ONLY);
            chooser.setDialogTitle(exportImportCst.IS_EXPORT ? "选择保存的目录" : "选择源文件（只支持.xls和.xlsx）");
            if (!exportImportCst.IS_EXPORT) {
                chooser.setFileFilter(new ExcelFileFilter());
            }
            chooser.showOpenDialog(null);
            File selectedFile = chooser.getSelectedFile();
            if (selectedFile != null) {
                pathTextField.setText(selectedFile.getAbsolutePath());
            }
            setRecentlyPath(pathTextField.getText());
        });
        this.add(chooseFile);

        //确认按钮
        JButton sureBtn = new JButton(exportImportCst.IS_EXPORT ? "导出/√Enter" : "导入/√Enter");
        sureBtn.setBounds(330, 200, 100, 30);
        sureBtn.setForeground(Color.decode("#FFFFFF"));
        sureBtn.setFont(DefaultFont.DEFAULT_FONT_ONE.setFontSize(12).setStyle(Font.BOLD).getFont());
        sureBtn.setBorder(null);
        sureBtn.setFocusPainted(false);
        sureBtn.setBackground(Color.decode("#046D35"));
        sureBtn.addActionListener(e -> {
            if (exportImportCst.IS_EXPORT) {
                exportFile();
            } else {
                importFile();
            }
        });
        this.add(sureBtn);

    }

    /**
     * 导出方法
     * Author: Crane Resigned
     * Date: 2022-12-31 14:07:18
     */
    private void exportFile() {
        String path = getPath();
        if (path == null) {
            return;
        }
        //执行导出
        boolean b = ExcelService.exportDataToExcel(new AccountDao().select(null), path);
        JOptionPane.showMessageDialog(null, b ? "导出成功" : "导出失败", "星小花★", JOptionPane.INFORMATION_MESSAGE);
        this.dispose();
    }

    /**
     * 导入方法
     * Author: Crane Resigned
     * Date: 2022-12-31 16:52:46
     */
    protected void importFile() {
        String path = getPath();
        if (path != null) {
            List<Account> accounts = EasyExcel.read(path).head(Account.class).sheet().doReadSync();
            //TODO：这个集合，每新增一条数据就需要重新连接一次数据库，因为是使用的jdbc，这样效率非常低，考虑建立线程池或上mybatis
            AccountDao accountDao = new AccountDao();
            //状态数组，成功，失败，总计
            int[] records = new int[3];
            for (Account account : accounts) {
                Boolean add = accountDao.add(account);
                if (!add) {
                    records[0]++;
                } else {
                    records[1]++;
                }
                records[2]++;
            }
            JOptionPane.showMessageDialog(null,
                    "成功：" + records[0] + "，失败：" + records[1] + "，总计：" + records[2],
                    "星小花★", JOptionPane.INFORMATION_MESSAGE);
            AccountService.setTableMessagesByList(accounts);
            this.dispose();
        }
    }

    /**
     * 当前版本的导入数据至少需要做的操作
     * 向以前的版本导入数据提供
     * 这个方法也就是为3.0和4.2做的，不具有扩展性，仅作为不触发警告的封装方法
     * Author: Crane Resigned
     * Date: 2023-01-22 21:23:56
     */
    protected static void newEditionInsert(AccountDao accountDao, int[] records, Account account) {
        account.setOther(SecurityService.encodeBase64Salt(account.getOther()));
        account.setUserKey(SecurityService.getUuidKey());
        Boolean add = accountDao.add(account);
        if (!add) {
            records[0]++;
        } else {
            records[1]++;
        }
        records[2]++;
    }

    /**
     * 获取路径
     * Author: Crane Resigned
     * Date: 2023-01-22 21:02:58
     */
    protected String getPath() {
        String path = pathTextField.getText();
        //TODO:需要单独封装一个检验路径的方法
        if (StrUtil.isEmpty(path)) {
            JOptionPane.showMessageDialog(null, "路径不对(* ￣︿￣)", "星小花★", JOptionPane.WARNING_MESSAGE);
            return null;
        }
        return path;
    }

    /**
     * 获取固定配置文件的属性值
     * Author: Crane Resigned
     * Date: 2022-12-31 14:18:32
     */
    private static String getRecentlyPath() {
        Properties recentlyPath = new Properties();
        try {
            recentlyPath.load(JdbcConnection.IS_TEST ?
                    ClassLoader.getSystemResourceAsStream("records/recently_path.properties")
                    : Files.newInputStream(new File(Paths.get("").toAbsolutePath() + "/resources/records/recently_path.properties").toPath()));
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
        return recentlyPath.getProperty("recently_path");
    }

    /**
     * 写入属性配置
     * Author: Crane Resigned
     * Date: 2022-12-31 15:18:08
     */
    private static void setRecentlyPath(String value) {
        Properties recentlyPath = new Properties();
        String resourcePath;
        try {
            resourcePath = JdbcConnection.IS_TEST ?
                    URLDecoder.decode(ClassLoader.getSystemResource("records/recently_path.properties").getFile(), "utf-8")
                    : Paths.get("").toAbsolutePath() + "/resources/records/recently_path.properties";
            OutputStream writer = new BufferedOutputStream(Files.newOutputStream(new File(resourcePath).toPath()));
            recentlyPath.setProperty("recently_path", value);
            recentlyPath.store(writer, null);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

}
