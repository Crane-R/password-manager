package com.crane.view.service;

import com.crane.constant.Constant;
import com.crane.model.bean.Account;
import com.crane.model.dao.AccountDao;
import com.crane.model.dao.LightDao;
import com.crane.model.service.ExcelService;
import com.crane.model.service.SecurityService;

import java.util.List;

/**
 * 导出服务。
 */
public final class ExportService {

    private ExportService() {
    }

    public static boolean exportCurrentScenePlaintext(String path) {
        List<Account> accounts = Constant.IS_LIGHT ? new LightDao().readData() : new AccountDao().select(null);
        accounts.forEach(account -> {
            account.setUserKey(null);
            account.setAccountId(null);
            account.setAccountName(SecurityService.decodeIfEncrypted(account.getAccountName()));
            account.setUsername(SecurityService.decodeIfEncrypted(account.getUsername()));
            account.setPassword(SecurityService.decodeIfEncrypted(account.getPassword()));
            account.setOther(SecurityService.decodeIfEncrypted(account.getOther()));
        });
        return ExcelService.exportDataToExcel(accounts, path);
    }

    public static int getCurrentSceneAccountCount() {
        return Constant.IS_LIGHT ? new LightDao().readData().size() : new AccountDao().select(null).size();
    }
}
