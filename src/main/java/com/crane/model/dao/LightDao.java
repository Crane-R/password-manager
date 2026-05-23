package com.crane.model.dao;

import com.alibaba.excel.EasyExcel;
import com.crane.model.bean.Account;
import com.crane.model.jdbc.JdbcConnection;
import com.crane.model.service.ExcelService;
import com.crane.model.service.SecurityService;
import com.crane.view.tools.ShowMessage;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * @Description 轻量版数据读写
 * @Author Crane Resigned
 * @Date 2023/2/4 0:06
 */
@Slf4j
public class LightDao {

    private static final String UPDATE_TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";

    private static String PATH;

    static {
        updatePath();
    }

    /**
     * 这个方法本来是在静态块里的，之所以抽出来：
     * 因为静态path只加载一次，所以需要在切换场景的时候切换path，
     * 切换currentKey时同时切换path即可。
     */
    public static void updatePath() {
        String userCurrentPath = System.getProperty("user.dir");
        log.info(userCurrentPath);
        PATH = JdbcConnection.IS_TEST
                ? userCurrentPath + "\\src\\main\\resources\\light_weight_data\\" + SecurityService.getUuidKey() + "_data.xlsx"
                : userCurrentPath + "\\resources\\light_weight_data\\" + SecurityService.getUuidKey() + "_data.xlsx";
    }

    /**
     * 数据写入
     */
    public boolean writeData(List<Account> dataList) {
        log.info(PATH);
        boolean isExist = ExcelService.fileIsExistElseCreate(PATH);
        if (isExist) {
            try {
                EasyExcel.write(PATH, Account.class).sheet("账户数据").doWrite(dataList);
            } catch (Exception e) {
                ShowMessage.showErrorMessage(String.valueOf(e.getCause()), e.getMessage());
                return false;
            }
        }
        return isExist;
    }

    /**
     * 数据读入
     */
    public List<Account> readData() {
        File file = new File(PATH);
        if (!file.exists()) {
            writeData(null);
        }
        List<Account> accounts = EasyExcel.read(PATH).head(Account.class).sheet("账户数据").doReadSync();
        return fillLegacyUpdateTime(accounts, file);
    }

    public List<Account> readData(String path) {
        return EasyExcel.read(path).head(Account.class).sheet("账户数据").doReadSync();
    }

    private List<Account> fillLegacyUpdateTime(List<Account> accounts, File file) {
        if (accounts == null || accounts.isEmpty()) {
            return accounts;
        }
        String fallbackTime = new SimpleDateFormat(UPDATE_TIME_PATTERN).format(new Date(file.lastModified()));
        boolean changed = false;
        for (Account account : accounts) {
            if (account != null && (account.getUpdateTime() == null || account.getUpdateTime().trim().isEmpty())) {
                account.setUpdateTime(fallbackTime);
                changed = true;
            }
        }
        if (changed) {
            writeData(accounts);
        }
        return accounts;
    }
}
