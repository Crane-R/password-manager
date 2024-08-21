package com.crane.view.function.service;

import com.crane.view.function.tools.PathTool;

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
            return ImageIO.read(PathTool.getResources2InputStream("img/logo/FluentPassword48Regular.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
