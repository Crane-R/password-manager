package com.crane.constant;

import com.crane.view.function.config.Config;

import java.nio.file.Paths;

/**
 * Description: 常量
 * Author: ZhouXingxue
 * Date: 2022/11/27 18:22
 *
 * @author Crane Resigned
 */
public class Constant {

    /**
     * 数字正则常量
     * Author: Crane Resigned
     * Date: 2022-11-30 19:23:48
     */
    public static final String IS_NUMBER = "^\\d+$";

    /**
     * 最小key长度，key为uuid 36位，因此密匙最低1位
     * 如果小于37说明密钥长度不合理
     * 雪花ID19位+最小密钥长度4位
     * Author: Crane Resigned
     * Date: 2023-01-07 22:42:16
     */
    public static final int MINIMUM_KEY_LENGTH = 19 + 4;

    /**
     * 记录当前场景的密钥
     * 当登录的时候记录在这里
     * Author: Crane Resigned
     * Date: 2023-01-07 22:55:07
     */
    public static String CURRENT_KEY = null;

    /**
     * 记录是否是轻量版
     *
     * @Author Crane Resigned
     * @Date 2023-02-04 00:26:00
     */
    public static boolean IS_LIGHT = false;

    /**
     * keys目录前缀
     * Author: Crane Resigned
     * Date: 2023-01-07 23:46:51
     */
    public static String DIRECTORY_KEYS = Paths.get("keys").toAbsolutePath() + "\\";

    /**
     * 一条账户数据最小的（正常的）list长度
     * Author: Crane Resigned
     * Date: 2023-01-22 15:35:46
     */
    public static final int ACCOUNT_LIST_LENGTH = 5;

    /**
     * 双击延迟
     * Author: Crane Resigned
     * Date: 2023-01-22 17:17:47
     */
    public static final long DOUBLE_ENTER_DELAY = 200;

    /**
     * 活性时间
     * Author: Crane Resigned
     * Date: 2023-01-22 18:39:49
     * 1000 * 60 * 2
     */
    public static final long ACTIVE_TIME = 1000 * 60 * 2;

    /**
     * 是否英文
     *
     * @Author Crane Resigned
     * @Date 2023-06-12 21:46:54
     */
    @Deprecated
    public static boolean IS_ENG = true;

    /**
     * 当前主题
     *
     * @Author AXing
     * @date 2023/12/7 15:40:32
     */
    public static final Config colorConfig = new Config("config/themes/"
            + new Config(null).get("theme") + ".properties");

    public static final Integer LEAST_PASS_LEN = 4;
}
