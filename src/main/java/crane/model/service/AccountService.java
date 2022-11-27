package crane.model.service;

import cn.hutool.core.util.StrUtil;
import crane.constant.Constant;
import crane.model.bean.Account;
import crane.model.dao.AccountDao;
import crane.view.MainFrame;
import lombok.extern.slf4j.Slf4j;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.util.*;

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
        return "星小花★(✿◡‿◡)：当前共有 ".concat(String.valueOf(MainFrame.jTable.getRowCount())).concat(" 个账户");
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
            MainFrame.jTable.setModel(new DefaultTableModel(new AccountService().selectData(text), Constant.TITLES));
        } catch (Exception e) {
            log.info("事件并发异常（使用了线程池）");
            e.printStackTrace();
        }
        //更新账户数量
        MainFrame.getResultNumbers().setText(AccountService.getLatestAccountNumberText());
    }

    public static void setTableMessages(Object[][] data) {
        MainFrame.jTable.setModel(new DefaultTableModel(data, Constant.TITLES));
        //更新账户数量
        MainFrame.getResultNumbers().setText(AccountService.getLatestAccountNumberText());
    }

    /**
     * 检测密钥文件是否存在的方法
     * Author: Crane Resigned
     * Date: 2022-11-27 11:54:47
     */
    public static boolean checkKeyFileIsExist() {
        File file = new File(Paths.get("key").toAbsolutePath().toString());
        return file.exists() && Objects.isNull(file.listFiles());
    }

    /**
     * 创建密匙的方法
     * Author: Crane Resigned
     * Date: 2022-11-27 12:07:28
     */
    public static void createKey(String keyPre) {
        String checkoutKey = keyPre.replaceAll("\"", "'");
        String finalKey = checkoutKey.concat(UUID.randomUUID().toString());

        File targetKeyFile = new File(Paths.get("key").toAbsolutePath().toString());

        //设为只读命令
        String command1 = "attrib \"" + targetKeyFile.getAbsolutePath() + "\" +R";
        //隐藏命令
        String command2 = "attrib \"" + targetKeyFile.getAbsolutePath() + "\" +H";

        FileOutputStream fileOutputStream;
        try {
            fileOutputStream = new FileOutputStream(targetKeyFile);
            fileOutputStream.write(finalKey.getBytes());

            Runtime.getRuntime().exec(command1);
            Runtime.getRuntime().exec(command2);

            fileOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取密匙
     * Author: Crane Resigned
     * Date: 2022-11-27 13:14:59
     */
    public static String getKey() {
        BufferedReader bufferedReader;
        String result = null;
        try {
            bufferedReader = new BufferedReader(new FileReader(Paths.get("key").toAbsolutePath().toAbsolutePath().toString()));
            result = bufferedReader.readLine();
            bufferedReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 获取密钥的uuid
     * Author: Crane Resigned
     * Date: 2022-11-27 17:01:41
     */
    public static String getUuidKey() {
        String uuidKey = getKey();
        if (uuidKey.length() < 37) {
            return "密匙长度不对";
        }
        return new StringBuilder(new StringBuilder(uuidKey).reverse().substring(0, 36)).reverse().toString();
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
                resultData[i][j] = j == 3 || j == 2 ? decodeBase64Salt(String.valueOf(jTable.getValueAt(i, j))) : String.valueOf(jTable.getValueAt(i, j));
            }
        }

        return resultData;
    }

    /**
     * 解密算法
     * Author: Crane Resigned
     * Date: 2022-11-27 13:43:29
     */
    public static String decodeBase64Salt(String password) {
        if (StrUtil.isEmpty(password)) {
            return password;
        }
        Base64.Decoder decoder = Base64.getDecoder();
        String decode = new String(decoder.decode(password), StandardCharsets.UTF_8);
        String key = getRealKey();

        int keyLastIndex = key.length() - 1;
        int decodeLastIndex = decode.length() - 1;
        for (int i = 0; i < keyLastIndex + 1; i++) {
            if (decode.charAt(decodeLastIndex--) != key.charAt(keyLastIndex--)) {
                return "假密匙";
            }
        }
        return new StringBuilder(decode).substring(0, decodeLastIndex - keyLastIndex - 1);
    }

    /**
     * 获取真实密钥
     * Author: Crane Resigned
     * Date: 2022-11-27 13:52:51
     */
    public static String getRealKey() {
        String uuidKey = getKey();
        if (uuidKey.length() < 37) {
            return "密匙长度不对";
        }
        return new StringBuilder(new StringBuilder(uuidKey).reverse().substring(36, uuidKey.length())).reverse().toString();
    }

    /**
     * 加密算法
     * Author: Crane Resigned
     * Date: 2022-11-27 14:13:12
     */
    public static String encodeBase64Salt(String password) {
        return Base64.getEncoder().encodeToString(password.concat("*").concat(getRealKey()).getBytes(StandardCharsets.UTF_8));
    }

}
