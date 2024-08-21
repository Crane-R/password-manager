package com.crane.view.function.tools;

import com.crane.model.jdbc.JdbcConnection;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * 获取resources下资源的工具
 *
 * @Author Crane Resigned
 * @Date 2024/8/11 19:05:14
 */
public final class PathTool {

    public static String getResources(String resourcesPath) {
        Path path = Paths.get("");
        return JdbcConnection.IS_TEST ?
                path.toAbsolutePath() + "\\src\\main\\resources\\" + resourcesPath
                : path.toAbsolutePath() + "\\resources\\" + resourcesPath;
    }

    public static InputStream getResources2InputStream(String resourcesPath) throws IOException {
        return Files.newInputStream(Paths.get(getResources(resourcesPath)));
    }

}
