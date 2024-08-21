package com.crane.view.tools;

import lombok.SneakyThrows;

import java.io.Reader;

/**
 * 关闭流工具
 *
 * @Author Crane Resigned
 * @Date 2024/8/21 18:52:31
 */
public class CloseTool {

    @SneakyThrows
    public static void close(Reader readable){
        if(readable!=null){
            readable.close();
        }
    }

}
