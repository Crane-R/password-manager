package com.crane.view.function.service;

import com.crane.view.MainFrame;

/**
 * 输出消息服务
 *
 * @author AXing
 * @date 2023/12/22 19:27:10
 */
public class MessageService {

    private MessageService(){}

    public static void outputMessage(String message) {
        MainFrame.getOutputArea().outputMessage(message);
    }

}
