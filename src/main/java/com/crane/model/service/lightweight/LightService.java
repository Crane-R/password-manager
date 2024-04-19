package com.crane.model.service.lightweight;

import cn.hutool.core.util.StrUtil;
import com.crane.constant.MainFrameCst;
import com.crane.model.bean.Account;
import com.crane.model.service.AccountService;
import com.crane.model.service.SecurityService;
import com.crane.view.main.MainFrame;
import lombok.extern.slf4j.Slf4j;

import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;
import java.util.List;

/**
 * @Description 组装数据
 * @Author Crane Resigned
 * @Date 2023/2/4 0:28
 */
@Slf4j
public class LightService {

    /**
     * 传入账户散数据，封装一条list调用写入
     *
     * @Author Crane Resigned
     * @Date 2023-02-04 00:29:21
     */
    public boolean addAccount(String accountName, String username, String password, String others, String useKey) {
        List<Account> accounts = new LightDao().readData();
        Account account = new Account(accounts.size() + 1, accountName, username, password, others, useKey);
        account.setAccountId(accounts.size() + 1);
        account.setUsername(SecurityService.decodeBase64Salt(account.getUsername()));
        account.setPassword(SecurityService.decodeBase64Salt(account.getPassword()));
        account.setOther(SecurityService.decodeBase64Salt(account.getOther()));
        SecurityService.encodeAccount(account);
        accounts.add(account);
        //添加成功后是返回false的，所以这里取反
        return !new LightDao().writeData(accounts);
    }

    /**
     * 修改
     *
     * @Author Crane Resigned
     * @Date 2023-02-04 01:54:34
     */
    public int updateAccount(Account account) {
        //在新增界面即将传入到这里时已经加密，故这里不需要再调用加密方法
        List<Account> accounts = new LightDao().readData();
        for (Account tempAccount : accounts) {
            Integer accountId = tempAccount.getAccountId();
            if (accountId != null && accountId.equals(account.getAccountId())) {
                tempAccount.setAccountName(account.getAccountName());
                tempAccount.setUsername(account.getUsername());
                tempAccount.setPassword(account.getPassword());
                tempAccount.setOther(account.getOther());
                tempAccount.setUserKey(account.getUserKey());
                new LightDao().writeData(accounts);
                return 1;
            }
        }
        return 0;
    }

    /**
     * 删除
     *
     * @Author Crane Resigned
     * @Date 2023-02-04 02:01:17
     */
    public boolean deleteAccount(Account account) {
        List<Account> accounts = new LightDao().readData();
        for (Account tempAccount : accounts) {
            Integer accountId = tempAccount.getAccountId();
            if (accountId != null && accountId.equals(account.getAccountId())) {
                boolean remove = accounts.remove(tempAccount);
                new LightDao().writeData(accounts);
                //return false is true
                return !remove;
            }
        }
        return true;
    }

    /**
     * 搜索
     *
     * @Author Crane Resigned
     * @Date 2023-02-04 02:04:45
     */
    public void searchAndSetTableMsg() {
        List<Account> accounts = new LightDao().readData();
        accounts.forEach(System.out::println);
        List<Account> resultList = new ArrayList<>();
        String keyword = MainFrame.getSearchText().getText();
        for (Account tempAccount : accounts) {
            tempAccount.setUserKey(SecurityService.decodeBase64Salt(tempAccount.getUserKey()));
            tempAccount.setAccountName(SecurityService.decodeBase64Salt(tempAccount.getAccountName()));
            if (tempAccount.getAccountName().toUpperCase().contains(keyword.toUpperCase())
                    && StrUtil.equals(tempAccount.getUserKey(), SecurityService.getUuidKey())) {
                resultList.add(tempAccount);
            }
        }
        Object[][] data = AccountService.listToTwoObj(resultList);
        try {
            //这里因为文本框事件频繁触发而导致的异常
            MainFrame.jTable.setModel(new DefaultTableModel(data, MainFrameCst.getTitles()));
        } catch (Exception e) {
            log.info("事件并发异常（使用了线程池）");
            e.printStackTrace();
        }
        //更新账户数量
        MainFrame.getResultNumbers().setText(AccountService.getLatestAccountNumberText());
    }

}
