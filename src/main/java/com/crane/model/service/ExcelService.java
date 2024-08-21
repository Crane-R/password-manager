package com.crane.model.service;

import cn.hutool.core.date.DateUtil;
import com.alibaba.excel.EasyExcel;
import com.crane.constant.MainFrameCst;
import com.crane.model.bean.Account;
import com.crane.model.bean.vo.AccountVo;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;

/**
 * Description: Excel操作服务类
 * Author: ZhouXingxue
 * Date: 2022/12/30 23:15
 *
 * @author Crane Resigned
 */
@Slf4j
public class ExcelService {

    /**
     * 导出数据为excel
     * Author: Crane Resigned
     * Date: 2022-12-30 23:20:09
     */
    public static boolean exportDataToExcel(List<Account> dataList, String absolutePath) {
        if (!checkPathIsContainXlsx(absolutePath)) {
            absolutePath = absolutePath + "/" + MainFrameCst.MAIN_TITLE + "_" + DateUtil.format(new Date(), "yyMMddHHmmss") + ".xlsx";
        }
        boolean newFileIsCreated = fileIsExistElseCreate(absolutePath);
        if (newFileIsCreated) {
            EasyExcel.write(absolutePath, AccountVo.class).sheet("账户数据").doWrite(dataList);
        }
        return newFileIsCreated;
    }

    /**
     * 检测路径是否包含.xlsx
     * Author: Crane Resigned
     * Date: 2022-12-31 13:00:42
     */
    public static boolean checkPathIsContainXlsx(String path) {
        StringBuilder stringBuilder = new StringBuilder(path).reverse();
        char[] checks = new char[]{'x', 's', 'l', 'x', '.'};
        int len = checks.length;
        for (int i = 0; i < len; i++) {
            if (stringBuilder.charAt(i) != checks[i]) {
                return false;
            }
        }
        return true;
    }

    /**
     * 检查文件是否存在，不存在创建
     *
     * @Author Crane Resigned
     * @Date 2023-02-04 00:12:58
     */
    public static boolean fileIsExistElseCreate(String path) {
        File targetFile = new File(path);
        if (!targetFile.exists()) {
            try {
                return targetFile.createNewFile();
            } catch (IOException e) {
                log.error(path);
                throw new RuntimeException(e);
            }
        }
        return true;
    }

}
