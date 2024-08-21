package com.crane.view.config;

import com.crane.view.tools.PathTool;
import com.crane.view.tools.ShowMessage;
import com.crane.model.jdbc.JdbcConnection;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

/**
 * 简体中文语言服务
 *
 * @Author Crane Resigned
 * @Date 2023-06-13 18:31:16
 */
@Slf4j
public final class Language {

    /**
     * 语言属性，记录程序当前所支持的语言
     *
     * @Author Crane Resigned
     * @Date 2023-06-13 18:47:55
     */
    private static Properties langProperties = new Properties();

    private static final String defaultPath = "config/language/";

    private Language() {
    }

    static {
        refresh(new Config(null).get("language"));
    }

    /**
     * 更新语言配置，重载语言配置
     *
     * @Author Crane Resigned
     * @Date 2023-06-13 18:51:37
     */
    public static void refresh(String langFile) {
        langFile = langFile + ".properties";
        langProperties = new Properties();
        try {
            langProperties.load(PathTool.getResources2InputStream(defaultPath + langFile));
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (NullPointerException e) {
            log.error(e.getMessage());
            ShowMessage.showErrorMessage("语言表加载失败，请检查语言配置文件是否有误。\r\n\nFailed to load the language table. Please check the language configuration.", String.valueOf(e.getCause()));
        }
    }

    /**
     * 获取属性
     *
     * @Author Crane Resigned
     * @Date 2023-06-13 18:38:29
     */
    public static String get(String key) {
        String property = langProperties.getProperty(key);
        return property == null ? "" : property;
    }

    /**
     * 获取所有语言的列表
     *
     * @Author Crane Resigned
     * @Date 2023-06-13 19:27:19
     */
    public static String[] getLanguages() {
        Path path = Paths.get("");
        File file = new File(JdbcConnection.IS_TEST ?
                (path.toAbsolutePath() + "/src/main/resources/" + defaultPath)
                : (path.toAbsolutePath() + "/resources/" + defaultPath));
        String[] list = file.list();
        if (list != null) {
            int len = list.length;
            for (int i = 0; i < len; i++) {
                list[i] = list[i].split("\\.")[0];
            }
        }
        return list;
    }

}
