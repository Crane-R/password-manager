package com.crane.view.tools;

import com.crane.model.jdbc.JdbcConnection;

import java.io.File;
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
        if (JdbcConnection.IS_TEST) {
            return path.toAbsolutePath() + File.separator + "src" + File.separator + "main" + File.separator
                    + "resources" + File.separator + resourcesPath;
        }
        return path.toAbsolutePath() + File.separator + "resources" + File.separator + resourcesPath;
    }

    public static InputStream getResources2InputStream(String resourcesPath) throws IOException {
        return Files.newInputStream(Paths.get(getResources(resourcesPath)));
    }

}
