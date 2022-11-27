package crane.model.service;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Objects;

/**
 * @author Crane Resigned
 */
public class FrameService {
    /**
     * 获取标题栏的图片
     *
     * @Author Crane Resigned
     * @Date 2022-04-29 18:47:48
     */
    public static Image getTitleImage() {
        try {
            return ImageIO.read(Files.newInputStream(Paths.get("src/main/java/crane/img/pass_black_500.png")));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
