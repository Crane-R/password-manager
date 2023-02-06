package crane.model.service.lightweight;

import crane.constant.MainFrameCst;
import crane.model.bean.Account;
import crane.model.service.AccountService;
import crane.view.MainFrame;
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
        List<Account> resultList = new ArrayList<>();
        String keyword = MainFrame.getSearchText().getText();
        for (Account tempAccount : accounts) {
            if (tempAccount.getAccountName().contains(keyword)) {
                resultList.add(tempAccount);
            }
        }
        Object[][] data = AccountService.listToTwoObj(resultList);
        try {
            //这里因为文本框事件频繁触发而导致的异常
            MainFrame.jTable.setModel(new DefaultTableModel(data, MainFrameCst.TITLES));
        } catch (Exception e) {
            log.info("事件并发异常（使用了线程池）");
            e.printStackTrace();
        }
        //更新账户数量
        MainFrame.getResultNumbers().setText(AccountService.getLatestAccountNumberText());
    }

}
