package crane.model.service;

import crane.model.bean.Account;
import crane.model.dao.AccountDao;
import crane.view.MainFrame;

import javax.swing.*;
import java.util.Date;
import java.util.LinkedList;
import java.util.Timer;
import java.util.TimerTask;

/**
 * @author Crane Resigned
 */
public class AccountService {

    /**
     * 获取数据
     * 返回一个Object二维数据，数据表
     *
     * @Author Crane Resigned
     * @Date 2022-06-02 22:49:06
     */
    public Object[][] selectData(String searchText) {
        //查询出全部或指定数据
        LinkedList<Account> list = (LinkedList<Account>) new AccountDao().select(searchText);
        System.out.println("查询出的list:" + list);
        System.out.println("list.size:" + list.size());

        //开始处理数据
        int length = list.size();
        Object[][] objects = new Object[length][5];
        for (int i = 0; i < length; i++) {
            Account account = list.get(i);
            objects[i][0] = account.getAccountId();
            objects[i][1] = account.getAccountName().trim();
            objects[i][2] = account.getUsername().trim();
            objects[i][3] = account.getPassword().trim();
            objects[i][4] = null == account.getOther() ? "" : account.getOther().trim();
        }
        return objects;
    }

    /**
     * 添加窗口传入四个数值，构建对象传进dao的添加方法
     *
     * @Author Crane Resigned
     * @Date 2022-06-02 22:49:14
     */
    public boolean addAccount(String accountName, String username, String password, String others) {
        Account account = new Account(null, accountName, username, password, others);
        return new AccountDao().add(account);
    }

    /**
     * 数据格式为
     * id，输入框1，输入框2，输入框3，输入框4
     * 获取选择的数据表的账户信息
     *
     * @Author Crane Resigned
     * @Date 2022-04-21 19:41:14
     */
    public LinkedList<String> getRowValues(JTable jTable) {
        int cols = jTable.getColumnCount();
        int row = jTable.getSelectedRow();
        LinkedList<String> list = new LinkedList<>();
        for (int i = 0; i < cols; i++) {
            list.add(String.valueOf(jTable.getValueAt(row, i)));
        }
        return list;
    }


    /**
     * 定时器，定时更新数据表的数据条数
     *
     * @Author Crane Resigned
     * @Date 2022-06-11 23:25:57
     */
    public void checkRowCount() {
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                MainFrame.getResultNumbers().setText("★共" + MainFrame.jTable.getRowCount() + "个账户");
                System.out.println("定时器执行：更新数据行数");
            }
        }, new Date(), 3000);
    }
}
