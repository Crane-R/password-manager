package crane.model.service.lightweight;

import com.alibaba.excel.EasyExcel;
import crane.model.bean.Account;
import crane.model.service.ExcelService;
import lombok.extern.slf4j.Slf4j;

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
        PATH = userCurrentPath + "\\src\\main\\resources\\light_weight_data\\light_weight_data.xlsx";
    }


    /**
     * 数据写入
     *
     * @Author Crane Resigned
     * @Date 2023-02-04 00:07:26
     */
    public boolean writeData(List<Account> dataList) {
        boolean isExist = ExcelService.fileIsExistElseCreate(PATH);
        if (isExist) {
            EasyExcel.write(PATH, Account.class).sheet("账户数据").doWrite(dataList);
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
        return EasyExcel.read(PATH).head(Account.class).sheet().doReadSync();
    }

}
