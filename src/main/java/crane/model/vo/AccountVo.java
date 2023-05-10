package crane.model.vo;

import com.alibaba.excel.annotation.ExcelProperty;

/**
 * @Description 账户类VO
 * @Author Crane Resigned
 * @Date 10/05/2023 09:20
 */
public class AccountVo {

    @ExcelProperty("账户名称")
    private String accountName;

    @ExcelProperty("用户名")
    private String username;

    @ExcelProperty("密码")
    private String password;

    @ExcelProperty("其他")
    private String other;

    public AccountVo() {

    }

    public AccountVo(String accountName, String username, String password, String other) {
        this.accountName = accountName;
        this.username = username;
        this.password = password;
        this.other = other;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getOther() {
        return other;
    }

    public void setOther(String other) {
        this.other = other;
    }

    @Override
    public String toString() {
        return "AccountVo{" +
                "accountName='" + accountName + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", other='" + other + '\'' +
                '}';
    }
}
