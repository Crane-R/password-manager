package com.crane.constant;

import com.crane.view.function.config.Language;

/**
 * Description: 主界面常量类
 * Author: ZhouXingxue
 * Date: 2022/12/30 21:24
 *
 * @author Crane Resigned
 */
public class MainFrameCst {

    /**
     * 主要标题
     * Author: Crane Resigned
     * Date: 2022-11-27 18:25:35
     */
    public static String MAIN_TITLE = "PManager " + VersionCst.VERSION;

    /**
     * 版本简称
     * Author: Crane Resigned
     * Date: 2023-01-22 20:54:59
     */
    public final static String SIMPLE_TITLE = "PManager";

    /**
     * 定义视图表标题
     *
     * @Author Crane Resigned
     * @Date 2022-06-11 22:55:44
     */
    @Deprecated
    public static final Object[] TITLES = Constant.IS_ENG ?
            new Object[]{"ID", "Account Name", "Username", "Password", "Others"}
            : new Object[]{"唯一标识", "账户名称", "用户名", "密码", "其他信息"};

    public static Object[] getTitles() {
        String tableTitle = Language.get("tableTitle");
        return tableTitle.split(",");
    }

    /**
     * 测试环境标题
     *
     * @Author Crane Resigned
     * @Date 2023-02-14 19:09:15
     */
    public final static String TEST_TITLE = MAIN_TITLE + " - 测试环境";

}
