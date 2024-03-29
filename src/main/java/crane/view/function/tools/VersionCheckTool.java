package crane.view.function.tools;

import cn.hutool.core.date.DateUtil;
import crane.view.function.config.Config;
import crane.model.jdbc.JdbcConnection;

/**
 * 版本检测工具，用于检测程序是否与资源版本一致
 *
 * @author zhouxingxue
 * @date 2023/10/14 18:11:44
 */
public final class VersionCheckTool {

    private VersionCheckTool() {
    }

    /**
     * 版本检查
     *
     * @author zhouxingxue
     * @date 2023/10/14 18:14:08
     */
    public static void checkVersion() {
        //如果处于测试模式，将最新时间写入
        if (!JdbcConnection.IS_TEST) {
            return;
        }
        Config config = new Config(null);
        config.set("recentUpdateTime", DateUtil.now());
        config.set("resourcesVersion", generateVerNumber());
    }

    /**
     * 生成版本号
     *
     * @author zhouxingxue
     * @date 2023/10/14 19:40:52
     */
    private static String generateVerNumber() {
//        return "v" + DateUtil.format(new Date(), "yy.MMdd");
        return "v7.0.3-beta-Chocolate.Ed";
    }

}
