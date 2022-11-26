package crane.model.service;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.IOException;
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
            return ImageIO.read(Objects.requireNonNull(FrameService.class.getResource("/crane/img/password2.21-002fa7.png")));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
