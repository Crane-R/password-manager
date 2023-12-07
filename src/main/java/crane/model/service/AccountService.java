package crane.model.service;

import crane.constant.Constant;
import crane.constant.MainFrameCst;
import crane.function.configservice.Language;
import crane.model.bean.Account;
import crane.model.dao.AccountDao;
import crane.view.main.MainFrame;
import lombok.extern.slf4j.Slf4j;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Crane Resigned
 */
@Slf4j
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
        Collections.reverse(list);
        System.out.println("查询出的list:" + list);
        System.out.println("list.size:" + list.size());
        return listToTwoObj(list);
    }

    /**
     * 将list转换为二维obj
     *
     * @Author Crane Resigned
     * @Date 2023-02-04 02:08:58
     */
    public static Object[][] listToTwoObj(List<Account> list) {
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
    public boolean addAccount(String accountName, String username, String password, String others, String userKey) {
        Account account = new Account(null, accountName, username, password, others, userKey);
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
     * 获取当前页面账户数量的字符串
     * Author: Crane Resigned
     * Date: 2022-11-26 20:02:11
     */
    public static String getLatestAccountNumberText() {
        return Language.get("lastestAccountNumberText1").concat(String.valueOf(MainFrame.jTable.getRowCount()))
                .concat(Language.get("lastestAccountNumberText2"));
    }

    /**
     * 设置表值的方法
     * Author: Crane Resigned
     * Date: 2022-11-26 20:28:38
     */
    public static void setTableMessages() {
        String text = MainFrame.getSearchText().getText();

        //sql语句轻微过滤
        text = text.replaceAll("'", "").replaceAll("\"", "");

        try {
            //这里因为文本框事件频繁触发而导致的异常
            MainFrame.jTable.setModel(new DefaultTableModel(new AccountService().selectData(text), MainFrameCst.getTitles()));
        } catch (Exception e) {
            log.info("事件并发异常（使用了线程池）");
            e.printStackTrace();
        }
        //更新账户数量
        MainFrame.getResultNumbers().setText(AccountService.getLatestAccountNumberText());
    }

    public static void setTableMessages(Object[][] data) {
        MainFrame.jTable.setModel(new DefaultTableModel(data, MainFrameCst.getTitles()));
        //更新账户数量
        MainFrame.getResultNumbers().setText(AccountService.getLatestAccountNumberText());
    }

    /**
     * 传入集合，设置表数据
     * Author: Crane Resigned
     * Date: 2022-12-31 17:09:28
     */
    public static void setTableMessagesByList(List<Account> list) {
        //list转为二维数组
        Object[][] result = new Object[list.size()][MainFrameCst.getTitles().length];
        int len = list.size();
        for (int i = 0; i < len; i++) {
            Account account = list.get(i);
            result[i][0] = "刷新查看唯一标识";
            result[i][1] = account.getAccountName();
            result[i][2] = account.getUsername();
            result[i][3] = account.getPassword();
            result[i][4] = account.getOther();
        }
        setTableMessages(result);
    }

    /**
     * 获取当前页面上的数据，并解密
     * Author: Crane Resigned
     * Date: 2022-11-27 13:39:32
     */
    public static Object[][] getTableData() {
        JTable jTable = MainFrame.jTable;
        int columnCount = jTable.getColumnCount();
        int rowCount = jTable.getRowCount();
        Object[][] resultData = new Object[rowCount][columnCount];
        for (int i = 0; i < rowCount; i++) {
            for (int j = 0; j < columnCount; j++) {
                resultData[i][j] = j == 3 || j == 2 || j == 4 ?
                        SecurityService.decodeBase64Salt(String.valueOf(jTable.getValueAt(i, j)))
                        : String.valueOf(jTable.getValueAt(i, j));
            }
        }
        return resultData;
    }

    /**
     * 传入一条账户信息，生成要粘贴的账户信息字符串
     * Author: Crane Resigned
     * Date: 2023-01-22 15:25:45
     */
    public static String generateAccountMsg(List<String> list) {
        if (list.size() < Constant.ACCOUNT_LIST_LENGTH) {
            return "生成失败，集合长度小于5";
        }
        return "账户：" + list.get(1) + "\r\n"
                + "用户名：" + list.get(2) + "\r\n"
                + "密码：" + list.get(3) + "\r\n"
                + "其他：" + list.get(4) + "\r\n";
    }

    /**
     * 搜索按钮状态切换
     * Author: Crane Resigned
     * Date: 2023-01-22 15:55:33
     */
    public static void toggleStatus(Boolean isDecode) {
        MainFrame.switchRecord = isDecode != null ? isDecode : !MainFrame.switchRecord;
        MainFrame.searchButton.setText(MainFrame.switchRecord ? Language.get("searchBtn2") : Language.get("searchBtn"));
    }

    /**
     * 解密账户集合
     * Author: Crane Resigned
     * Date: 2023-01-22 16:25:35
     */
    public static void decodeList(List<String> list) {
        //密码
        list.set(3, SecurityService.decodeBase64Salt(list.get(3)));
        //用户名
        list.set(2, SecurityService.decodeBase64Salt(list.get(2)));
        //其他信息
        list.set(4, SecurityService.decodeBase64Salt(list.get(4)));
    }

}