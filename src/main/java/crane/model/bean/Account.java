package crane.model.bean;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Getter;

import java.io.Serializable;

/**
 * @author Crane Resigned
 */
@Getter
public class Account implements Serializable {

    /**
     * 账户流水id
     * Author: Crane Resigned
     * Date: 2022-12-30 23:11:46
     */
    @ExcelProperty("account_id")
    private Integer accountId;

    /**
     * 账户名称
     * Author: Crane Resigned
     * Date: 2022-12-30 23:11:59
     */
    @ExcelProperty("account_name")
    private String accountName;

    /**
     * 用户名
     * Author: Crane Resigned
     * Date: 2022-12-30 23:12:09
     */
    @ExcelProperty("username")
    private String username;

    /**
     * 密码
     * Author: Crane Resigned
     * Date: 2022-12-30 23:12:15
     */
    @ExcelProperty("password")
    private String password;

    /**
     * 其他信息
     * Author: Crane Resigned
     * Date: 2022-12-30 23:12:21
     */
    @ExcelProperty("other")
    private String other;

    /**
     * 用户key
     * Author: Crane Resigned
     * Date: 2022-12-30 23:12:28
     */
    @ExcelProperty("user_key")
    private String userKey;

    public Account() {
    }

    public Account(Integer accountId, String accountName, String username, String password, String other, String userKey) {
        this.accountId = accountId;
        this.accountName = accountName;
        this.username = username;
        this.password = password;
        this.other = other;
        this.userKey = userKey;
    }

    public void setAccountId(Integer accountId) {
        this.accountId = accountId;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setOther(String other) {
        this.other = other;
    }

    public void setUserKey(String userKey) {
        this.userKey = userKey;
    }

    @Override
    public String toString() {
        return "Account{" +
                "accountId=" + accountId +
                ", accountName='" + accountName + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", other='" + other + '\'' +
                ", userKey='" + userKey + '\'' +
                '}';
    }
}
