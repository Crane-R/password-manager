package com.crane.view.frame;

import com.alibaba.excel.EasyExcel;
import com.crane.constant.Constant;
import com.crane.constant.DefaultFont;
import com.crane.constant.ExportImportCst;
import com.crane.model.bean.Account;
import com.crane.model.dao.AccountDao;
import com.crane.model.dao.LightDao;
import com.crane.model.service.AccountService;
import com.crane.model.service.SecurityService;
import com.crane.view.config.Language;
import com.crane.view.tools.ExcelFileFilter;
import com.crane.view.tools.ShowMessage;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 去加密导入
 * 不这样实现的话其实可以直接在父类加一个标识符，只是我觉得加个切换按钮太麻烦了
 * 直接在这里重写还方便点
 *
 * @Author Crane Resigned
 * @Date 2024/8/21 16:58:47
 */
public class ImportByEncryptFrame extends ExportImportDataFrame {

    private JTextField keyTextField;

    public ImportByEncryptFrame(ExportImportCst exportImportCst) {
        super(exportImportCst);

        pathTextField.setBounds(50, 166, 380, 35);
        pathTextField.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                JFileChooser chooser = new JFileChooser(getRecentlyPath());
                chooser.setSize(1500, 900);
                //导出为只选择目录模式，导入为只选择文件模式
                chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
                chooser.setDialogTitle(Language.get("chooserDialogImportTitle"));
                if (!exportImportCst.IS_EXPORT) {
                    chooser.setFileFilter(new ExcelFileFilter());
                }
                chooser.showOpenDialog(null);
                File selectedFile = chooser.getSelectedFile();
                if (selectedFile != null) {
                    pathTextField.setText(selectedFile.getAbsolutePath());
                }
                setRecentlyPath(pathTextField.getText());
            }
        });

        chooseFile.setVisible(false);

        //文本框
        keyTextField = new JTextField();
        keyTextField.setBounds(50, 126, 380, 35);
        keyTextField.setFont(DefaultFont.WEI_RUAN_BOLD_13.getFont());
        keyTextField.setForeground(Color.decode(colorConfig.get("pathTextFore")));
        keyTextField.setBorder(BorderFactory.createLineBorder(Color.decode(colorConfig.get("pathTextLineBor"))));
        keyTextField.setHorizontalAlignment(JPasswordField.CENTER);
        keyTextField.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                JFileChooser chooser = new JFileChooser(getRecentlyPath());
                chooser.setSize(1500, 900);
                //导出为只选择目录模式，导入为只选择文件模式
                chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
                chooser.setDialogTitle(Language.get("chooserDialogImportTitle"));
                chooser.showOpenDialog(null);
                File selectedFile = chooser.getSelectedFile();
                if (selectedFile != null) {
                    keyTextField.setText(selectedFile.getAbsolutePath());
                }
                setRecentlyPath(keyTextField.getText());
            }
        });
        this.add(keyTextField);

        sureBtn.removeActionListener(sureBtnactionListener);
        //开始解密
        sureBtn.addActionListener(e -> {
            //todo:判空

            importFile();
        });

    }

    /**
     * deriveAccounts是原本的数据
     *
     * @Author CraneResigned
     * @Date 2024/8/21 18:23:26
     */
    @Override
    protected void importFile() {
        String path = getPath();
        if (path != null) {
            List<Account> accounts = EasyExcel.read(path).head(Account.class).sheet().doReadSync();
            LightDao lightDao = new LightDao();
            //这是加密的数据
            List<Account> deriveAccounts = lightDao.readData();
            int newSize = accounts.size();
            AtomicInteger id = new AtomicInteger(deriveAccounts.size() + 1);
            accounts.forEach(account -> {
                //解密
                String fullKey = SecurityService.getKeyByKeyFile(keyTextField.getText());
                String realKey = SecurityService.getRealKey(fullKey);
                SecurityService.decodeAccount(account, realKey);
                account.setAccountId(id.getAndIncrement());
                account.setUserKey(SecurityService.getUuidKey());
                //加密
                SecurityService.encodeAccount(account);
            });
            accounts.addAll(deriveAccounts);
            lightDao.writeData(accounts);
            ShowMessage.showInformationMessage(
                    Language.get("importSuccessiveTipMsg1") + newSize
                            + Language.get("importSuccessiveTipMsg2") + deriveAccounts.size()
                            + Language.get("importSuccessiveTipMsg3") + (newSize + deriveAccounts.size()),
                    Language.get("importSuccessiveTipTit"));

            AccountService.setTableMessagesByList(accounts);
            this.dispose();
        }
    }
}
