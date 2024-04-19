package com.crane.model.dao;

import com.crane.model.bean.Account;

import java.util.List;

/**
 * @author Crane Resigned
 */
public interface DaoMethod {

    /**
     * 根据搜索文本查询
     * Author: Crane Resigned
     * Date: 2022-11-26 19:29:19
     *
     * @param searchText 搜索文本
     * @return 返回账户集合
     */
    List<Account> select(String searchText);

    /**
     * 添加
     *
     * @param account 账户对象
     * @return 返回布尔
     * Author: Crane Resigned
     * Date: 2022-11-26 19:31:50
     */
    Boolean add(Account account);

    /**
     * 修改
     *
     * @param account 账户对象
     * @return 修改行数
     * Author: Crane Resigned
     * Date: 2022-11-26 19:32:58
     */
    Integer update(Account account);

    /**
     * 删除
     *
     * @param account 账户对象
     * @return 返回布尔
     * Author: Crane Resigned
     * Date: 2022-11-26 19:33:39
     */
    Boolean delete(Account account);
}
