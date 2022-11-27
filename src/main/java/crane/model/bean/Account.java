package crane.model.bean;

import java.io.Serializable;

/**
 * @author Crane Resigned
 */
public class Account implements Serializable {

    private Integer accountId;
    private String accountName;
    private String username;
    private String password;
    private String other;
    private String userKey;

    public Account(){}

    public Account(Integer accountId, String accountName, String username, String password, String other, String userKey) {
        this.accountId = accountId;
        this.accountName = accountName;
        this.username = username;
        this.password = password;
        this.other = other;
        this.userKey = userKey;
    }

    public Integer getAccountId() {
        return accountId;
    }

    public void setAccountId(Integer accountId) {
        this.accountId = accountId;
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

    public String getUserKey() {
        return userKey;
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
