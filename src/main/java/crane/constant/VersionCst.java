package crane.constant;

import crane.view.function.config.Config;

/**
 * @Description 版本常量
 * @Author Crane Resigned
 * @Date 2023/2/3 23:12
 */
public interface VersionCst {

    /**
     * 版本号
     *
     * @Author Crane Resigned
     * @Date 2023-02-02 21:33:50
     */
    String VERSION = new Config(null).get("resourcesVersion");

    /**
     * 最近更新日期
     *
     * @Author Crane Resigned
     * @Date 2023-02-03 23:13:04
     */
    String RECENTLY_UPDATE_DATE = new Config(null).get("recentUpdateTime");
}
