package crane.view;

import com.alibaba.excel.EasyExcel;
import crane.constant.ExportImportCst;
import crane.model.bean.Account;
import crane.model.dao.AccountDao;
import crane.model.service.AccountService;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * Description: 导入4.2的数据
 * Author: ZhouXingxue
 * Date: 2023/1/22 21:16
 *
 * @author Crane Resigned
 */
public class Import4DataFrame extends ExportImportDataFrame {

    public Import4DataFrame() {
        super(ExportImportCst.IMPORT);
        this.setTitle("导入PM4.2的数据");
        tipLabel.setText("选择PM4.2的xlsx数据文件");
        tipLabel.setForeground(Color.decode("#4FA485"));
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
