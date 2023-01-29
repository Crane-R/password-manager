package crane.model.service;

import cn.hutool.core.date.DateUtil;
import com.alibaba.excel.EasyExcel;
import crane.constant.MainFrameCst;
import crane.model.bean.Account;

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
public class ExcelService {

    /**
     * 导出数据为excel
     * Author: Crane Resigned
     * Date: 2022-12-30 23:20:09
     */
    public static boolean exportDataToExcel(List<Account> dataList, String absolutePath) {

        if (!checkPathIsContainXlsx(absolutePath)) {
            absolutePath = absolutePath + "/" + MainFrameCst.SIMPLE_TITLE + DateUtil.format(new Date(), "yyyyMMddHHmmss") + ".xlsx";
        }

        File targetFile = new File(absolutePath);
        boolean newFileIsCreated = false;
        if (!targetFile.exists()) {
            try {
                newFileIsCreated = targetFile.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        if (newFileIsCreated) {
            EasyExcel.write(absolutePath, Account.class).sheet("账户数据").doWrite(dataList);
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

}
