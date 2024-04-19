package com.crane.view.function.service;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.IOException;
import java.util.Objects;

/**
 * 图标服务
 *
 * @Author Crane Resigned
 * @Date 2023/12/23 13:12:40
 */
public class ImageService {

    private ImageService() {
    }

    /**
     * 获取标题栏的图片
     *
     * @Author Crane Resigned
     * @Date 2022-04-29 18:47:48
     */
    public static Image getTitleImage() {
        try {
            return ImageIO.read(Objects.requireNonNull(ActiveTimeService.class.getResource("/img/pmv6.4.2.png")));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
