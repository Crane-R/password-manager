package crane.model.dao;

import crane.model.bean.Account;

import java.util.List;

/**
 * @author Crane Resigned
 */
public interface DaoMethod {
    
    /**
     * 根据搜索文本查询
     * Author: Crane Resigned
     * Date: 2022-11-26 19:29:19
     */
    List<Account> select(String searchText);

    Boolean add(Account account);

    Integer update(Account account);

    Boolean delete(Account account);
}
