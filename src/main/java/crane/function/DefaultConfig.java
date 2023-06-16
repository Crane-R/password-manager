package crane.function;

import crane.model.jdbc.JdbcConnection;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

public final class DefaultConfig {

    private static final Properties defaultConfig;

    private static final String defaultPath = "config/defaultConfig.properties";

    private DefaultConfig() {
    }

    static {
        defaultConfig = new Properties();
        try {
            InputStream inputStream = JdbcConnection.IS_TEST ?
                    ClassLoader.getSystemResourceAsStream(defaultPath)
                    : Files.newInputStream(new File(Paths.get("").toAbsolutePath() + "/resources/" + defaultPath).toPath());
            if (inputStream != null) {
                defaultConfig.load(new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8)));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 获取属性
     *
     * @Author Crane Resigned
     * @Date 2023-06-13 19:02:24
     */
    public static String getDefaultProperty(String key) {
        return defaultConfig.getProperty(key);
    }

    /**
     * 设置属性
     *
     * @Author Crane Resigned
     * @Date 2023-06-13 19:03:05
     */
    public static void setDefaultProperty(String key, String value) {

        //FIXME：设置中文会乱码

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
