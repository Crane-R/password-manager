package crane.constant;

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
     * Author: Crane Resigned
     * Date: 2023-01-07 22:42:16
     */
    public static final int MINIMUM_KEY_LENGTH = 37;

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
    public static final String DIRECTORY_KEYS = "keys";

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
     * Date: 2023-01-22 18:39:49 1000 * 60 * 2
     */
    public static final long ACTIVE_TIME = 5*1000;

}
