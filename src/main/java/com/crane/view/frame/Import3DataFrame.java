package com.crane.view.frame;

import com.alibaba.excel.EasyExcel;
import com.crane.constant.ExportImportCst;
import com.crane.model.bean.Account;
import com.crane.model.service.AccountService;
import com.crane.model.dao.AccountDao;
import com.crane.model.service.SecurityService;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * Description: 导入3.0的数据
 * Author: ZhouXingxue
 * Date: 2023/1/22 20:37
 *
 * @author Crane Resigned
 */
public class Import3DataFrame extends ExportImportDataFrame {
    public Import3DataFrame() {
        super(ExportImportCst.IMPORT);
        this.setTitle("导入PM3.0的数据");
        tipLabel.setText("选择Password Manager v3.0的xlsx数据文件");
        tipLabel.setForeground(Color.decode("#002FA7"));
    }

    @Override
    protected void importFile() {
        String path = getPath();
        if (path != null) {
            List<Account> accounts = EasyExcel.read(path).head(Account.class).sheet().doReadSync();
            //TODO：这个集合，每新增一条数据就需要重新连接一次数据库，因为是使用的jdbc，这样效率非常低，考虑建立线程池或上mybatis
            AccountDao accountDao = new AccountDao();
            //状态数组，成功，失败，总计
            int[] records = new int[3];
            for (Account account : accounts) {
                account.setUsername(SecurityService.encodeBase64Salt(account.getUsername()));
                account.setPassword(SecurityService.encodeBase64Salt(account.getPassword()));
                newEditionInsert(accountDao, records, account);
            }
            JOptionPane.showMessageDialog(null,
                    "成功：" + records[0] + "，失败：" + records[1] + "，总计：" + records[2],
                    "星小花★", JOptionPane.INFORMATION_MESSAGE);
            AccountService.setTableMessagesByList(accounts);
            this.dispose();
        }
    }
}
