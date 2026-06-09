package com.crane.view.config;

import cn.hutool.core.util.StrUtil;
import com.crane.model.jdbc.JdbcConnection;
import com.crane.view.tools.PathTool;
import com.crane.view.tools.ShowMessage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.Properties;

/**
 * 配置文件读写。
 */
public class Config {

    private final Properties defaultConfig;

    private String defaultPath = "config/defaultConfig.properties";

    public Config(String configPath) {
        if (Objects.nonNull(configPath) && !StrUtil.isEmpty(configPath)) {
            defaultPath = configPath;
        }
        defaultConfig = new Properties();
        try {
            InputStream inputStream = PathTool.getResources2InputStream(defaultPath);
            defaultConfig.load(new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8)));
        } catch (IOException e) {
            ShowMessage.showErrorMessage(e.getStackTrace(), e.getMessage());
            throw new RuntimeException(e);
        }
    }

    /**
     * 获取属性。
     */
    public String get(String key) {
        return defaultConfig.getProperty(key);
    }

    /**
     * 设置属性。
     */
    public void set(String key, String value) {
        if (value == null) {
            value = "";
        }
        defaultConfig.setProperty(key, new String(value.getBytes(StandardCharsets.UTF_8), StandardCharsets.UTF_8));
        Path path = Paths.get("");
        String filePath = JdbcConnection.IS_TEST ?
                (path.toAbsolutePath() + "/src/main/resources/" + defaultPath)
                : (path.toAbsolutePath() + "/resources/" + defaultPath);
        try {
            defaultConfig.store(new OutputStreamWriter(Files.newOutputStream(Paths.get(filePath)), StandardCharsets.UTF_8), null);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
