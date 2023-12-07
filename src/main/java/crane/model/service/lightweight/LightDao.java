package crane.model.service.lightweight;

import com.alibaba.excel.EasyExcel;
import crane.model.bean.Account;
import crane.model.jdbc.JdbcConnection;
import crane.model.service.ExcelService;
import crane.model.service.SecurityService;
import crane.function.tools.ShowMessage;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.util.List;

/**
 * @Description 轻量版数据读写
 * @Author Crane Resigned
 * @Date 2023/2/4 0:06
 */
@Slf4j
public class LightDao {

    private static final String PATH;

    static {
        //获取程序运行的当前路径
        String userCurrentPath = System.getProperty("user.dir");
        log.info(userCurrentPath);
        PATH = JdbcConnection.IS_TEST ?
                userCurrentPath + "\\src\\main\\resources\\light_weight_data\\" + SecurityService.getUuidKey() + "_data.xlsx"
                : userCurrentPath + "\\resources\\light_weight_data\\" + SecurityService.getUuidKey() + "_data.xlsx";
    }


    /**
     * 数据写入
     *
     * @Author Crane Resigned
     * @Date 2023-02-04 00:07:26
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
     *
     * @Author Crane Resigned
     * @Date 2023-02-04 00:19:24
     */
    public List<Account> readData() {
        //读数据需要先写一个空文件进去，调用写以创建文件
        if (!new File(PATH).exists()) {
            writeData(null);
        }
        return EasyExcel.read(PATH).head(Account.class).sheet("账户数据").doReadSync();
    }

}
