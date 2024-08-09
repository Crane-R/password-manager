package com.crane.constant;

import cn.hutool.core.codec.Rot;
import com.crane.view.function.config.Config;

/**
 * @Description 版本常量
 * @Author Crane Resigned
 * @Date 2023/2/3 23:12
 */
public interface VersionCst {

    /**
     * 版本号
     * 版本号不应该写在配置文件
     *
     * @Author Crane Resigned
     * @Date 2023-02-02 21:33:50
     */
    String VERSION = "v8.0-beta";

    /**
     * 最近更新日期
     * 该时间也不应该写在
     *
     * @Author Crane Resigned
     * @Date 2023-02-03 23:13:04
     */
    String RECENTLY_UPDATE_DATE = Rot.decode13(new Config(null).get("recentUpdateTime"));
}
