package crane.function;

import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import crane.constant.VersionCst;
import crane.model.jdbc.JdbcConnection;
import crane.model.service.ShowMessgae;

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
        if(JdbcConnection.IS_TEST){
            DefaultConfig.setDefaultProperty("recentUpdateTime", DateUtil.now());
        }

        judgeVersion(StrUtil.equals(VersionCst.VERSION,
                DefaultConfig.getDefaultProperty("resourcesVersion")));
    }

    /**
     * 版本审批
     *
     * @author zhouxingxue
     * @date 2023/10/14 18:13:47
     */
    private static void judgeVersion(boolean isSame) {
        if (!isSame) {
            ShowMessgae.showWarningMessage(Language.get("versionIsNotSameMsg"),
                    Language.get("versionIsNotSameTit"));
        }
    }

}
