package com.crane.view.frame;

import cn.hutool.core.util.StrUtil;
import com.alibaba.excel.EasyExcel;
import com.crane.constant.Constant;
import com.crane.constant.DefaultFont;
import com.crane.constant.ExportImportCst;
import com.crane.model.bean.Account;
import com.crane.model.dao.AccountDao;
import com.crane.model.jdbc.JdbcConnection;
import com.crane.model.service.AccountService;
import com.crane.model.service.ExcelService;
import com.crane.model.service.SecurityService;
import com.crane.model.dao.LightDao;
import com.crane.view.config.Language;
import com.crane.view.frame.module.CustomFrame;
import com.crane.view.tools.ExcelFileFilter;
import com.crane.view.tools.PathTool;
import com.crane.view.tools.ShowMessage;
import lombok.extern.slf4j.Slf4j;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLDecoder;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Description: 导出数据窗口
 * Author: ZhouXingxue
 * Date: 2022/12/30 23:30
 *
 * @author Crane Resigned
 */
@Slf4j
public class ExportImportDataFrame extends CustomFrame {

    protected JTextField pathTextField;

    protected JLabel tipLabel;

    protected JButton chooseFile;

    protected JButton sureBtn;

    protected ActionListener sureBtnactionListener;

    public ExportImportDataFrame(ExportImportCst exportImportCst) {
        super(500, 300, null);
        this.setTitle(exportImportCst.TITLE);

        tipLabel = new JLabel(exportImportCst.TIP_LABEL);
        tipLabel.setBounds(50, 76, 400, 40);
        tipLabel.setForeground(Color.decode(colorConfig.get("lockTipLabel")));
        tipLabel.setFont(new Font("微软雅黑", Font.BOLD, 18));
        this.add(tipLabel);

        //文本框
        pathTextField = new JTextField();
        pathTextField.setBounds(50, 146, 380, 35);
        pathTextField.setFont(DefaultFont.WEI_RUAN_BOLD_13.getFont());
        pathTextField.setForeground(Color.decode(colorConfig.get("pathTextFore")));
        pathTextField.setBorder(BorderFactory.createLineBorder(Color.decode(colorConfig.get("pathTextLineBor"))));
        pathTextField.setHorizontalAlignment(JPasswordField.CENTER);
        pathTextField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                //回车触发
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    if (exportImportCst.IS_EXPORT) {
                        exportFile();
                    } else {
                        importFile();
                    }
                }
            }
        });
        this.add(pathTextField);

        //文件选择器
        chooseFile = new JButton(Language.get("chooseFileBtn"));
        chooseFile.setBounds(50, 226, 100, 30);
        chooseFile.setForeground(Color.decode(colorConfig.get("chooseFileBtnFore")));
        chooseFile.setFont(DefaultFont.WEI_RUAN_BOLD_12.getFont());
        chooseFile.setBorder(null);
        chooseFile.setFocusPainted(false);
        chooseFile.setBackground(Color.decode(colorConfig.get("chooseFileBtnBg")));
        chooseFile.addActionListener(e -> {
            JFileChooser chooser = new JFileChooser(getRecentlyPath());
            chooser.setSize(1500, 900);
            //导出为只选择目录模式，导入为只选择文件模式
            chooser.setFileSelectionMode(exportImportCst.IS_EXPORT ? JFileChooser.DIRECTORIES_ONLY : JFileChooser.FILES_ONLY);
            chooser.setDialogTitle(exportImportCst.IS_EXPORT ? Language.get("chooserDialogExportTitle") : Language.get("chooserDialogImportTitle"));
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
        sureBtn = new JButton(exportImportCst.IS_EXPORT ? Language.get("exportSureBtn") : Language.get("importSureBtn"));
        sureBtn.setBounds(330, 226, 100, 30);
        sureBtn.setForeground(Color.decode(colorConfig.get("sureBtnFore")));
        sureBtn.setFont(DefaultFont.WEI_RUAN_BOLD_12.getFont());
        sureBtn.setBorder(null);
        sureBtn.setFocusPainted(false);
        sureBtn.setBackground(Color.decode(colorConfig.get("sureBtnBg")));
        sureBtnactionListener = e -> {
            if (exportImportCst.IS_EXPORT) {
                exportFile();
            } else {
                importFile();
            }
        };
        sureBtn.addActionListener(sureBtnactionListener);

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
        List<Account> accounts;
        if (!Constant.IS_LIGHT) {
            accounts = new AccountDao().select(null);
        } else {
            accounts = new LightDao().readData();
        }
        //清除key
        accounts.forEach(account -> {
            account.setUserKey(null);
            account.setAccountId(null);
            //解密
            account.setAccountName(SecurityService.decodeBase64Salt(account.getAccountName()));
            account.setUsername(SecurityService.decodeBase64Salt(account.getUsername()));
            account.setPassword(SecurityService.decodeBase64Salt(account.getPassword()));
            account.setOther(SecurityService.decodeBase64Salt(account.getOther()));
        });
        //执行导出
        boolean b = ExcelService.exportDataToExcel(accounts, path);
        ShowMessage.showInformationMessage(b ? Language.get("exportSuccessiveTipMsg1") + accounts.size()
                + Language.get("exportSuccessiveTipMsg2") : Language.get("exportSuccessiveTipMsg3"), Language.get("exportSuccessiveTipTit"));
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
            if (!Constant.IS_LIGHT) {
                //TODO：这个集合，每新增一条数据就需要重新连接一次数据库，因为是使用的jdbc，这样效率非常低，考虑建立线程池或上mybatis
                AccountDao accountDao = new AccountDao();
                //状态数组，成功，失败，总计
                int[] records = new int[3];
                for (Account account : accounts) {
                    //替换密钥
                    account.setUserKey(SecurityService.getUuidKey());
                    //加密
                    account.setUsername(SecurityService.encodeBase64Salt(account.getUsername()));
                    account.setPassword(SecurityService.encodeBase64Salt(account.getPassword()));
                    account.setOther(SecurityService.encodeBase64Salt(account.getOther()));
                    Boolean add = accountDao.add(account);
                    if (!add) {
                        records[0]++;
                    } else {
                        records[1]++;
                    }
                    records[2]++;
                }
                ShowMessage.showInformationMessage(Language.get("importLightSuccessiveTipMsg1")
                        + records[0] + Language.get("importLightSuccessiveTipMsg2")
                        + records[1] + Language.get("importLightSuccessiveTipMsg3")
                        + records[2], Language.get("importLightSuccessiveTipTit"));
            } else {
                LightDao lightDao = new LightDao();
                List<Account> deriveAccounts = lightDao.readData();
                int newSize = accounts.size();
                AtomicInteger id = new AtomicInteger(deriveAccounts.size() + 1);
                accounts.forEach(account -> {
                    account.setAccountId(id.getAndIncrement());
                    account.setUserKey(SecurityService.getUuidKey());
                    SecurityService.encodeAccount(account);
                });
                accounts.addAll(deriveAccounts);
                lightDao.writeData(accounts);
                ShowMessage.showInformationMessage(
                        Language.get("importSuccessiveTipMsg1") + newSize
                                + Language.get("importSuccessiveTipMsg2") + deriveAccounts.size()
                                + Language.get("importSuccessiveTipMsg3") + (newSize + deriveAccounts.size()),
                        Language.get("importSuccessiveTipTit"));
            }
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
            ShowMessage.showWarningMessage(Language.get("errPathTipMsg"), Language.get("errPathTipTit"));
            return null;
        }
        return path;
    }

    /**
     * 获取固定配置文件的属性值
     * Author: Crane Resigned
     * Date: 2022-12-31 14:18:32
     */
    protected static String getRecentlyPath() {
        Properties recentlyPath = new Properties();
        try {
            recentlyPath.load(PathTool.getResources2InputStream("records/recently_path.properties"));
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
    protected static void setRecentlyPath(String value) {
        Properties recentlyPath = new Properties();
        String resourcePath;
        try {
            resourcePath = PathTool.getResources("records/recently_path.properties");
            OutputStream writer = new BufferedOutputStream(Files.newOutputStream(new File(resourcePath).toPath()));
            recentlyPath.setProperty("recently_path", value);
            recentlyPath.store(writer, null);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

}
