package com.crane.view.service;

import cn.hutool.core.date.DateUtil;
import com.crane.constant.Constant;
import com.crane.view.config.Config;
import com.crane.view.config.Language;
import com.crane.view.frame.MainFrame;
import com.crane.view.frame.LockFrame;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import javax.swing.*;
import java.awt.*;
import java.util.Date;
import java.util.Objects;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @author Crane Resigned
 */
@Slf4j
public class ActiveTimeService {

    /**
     * 开闭原则
     * 设定一个活性时间定时器的总开关，
     * 当锁住时修改此属性的值从而实现激活活性时间不会生效
     *
     * @Author Crane Resigned
     * @Date 2023-06-01 19:19:17
     */
    @Getter
    private static boolean isActiveLock = false;

    /**
     * 启动标识
     * Author: Crane Resigned
     * Date: 2023-01-22 19:09:01
     */
    @Getter
    private static boolean isStart = false;

    /**
     * 正常活性时间
     * Author: Crane Resigned
     * Date: 2023-01-22 19:19:03
     */
    private static final long[] TIME = {Constant.ACTIVE_TIME};

    /**
     * 红色警告时间
     * Author: Crane Resigned
     * Date: 2023-01-22 19:19:51
     */
    private static final long RED_TIME = (long) (Constant.ACTIVE_TIME * 0.1);

    private static ScheduledExecutorService ACTIVIST_TIMER;

    private static final Config colorConfig = Constant.colorConfig;

    /**
     * 创建主界面时校正标识以确保活性时间功能正常
     *
     * @Author Crane Resigned
     * @Date 2023/12/23 13:15:23
     */
    public static void activeTimeStart() {
        isActiveLock = false;
        activeTimeFresh();
    }

    /**
     * 更新活性时间
     * Author: Crane Resigned
     * Date: 2023-01-22 18:38:05
     */
    public static void activeTimeFresh() {
        if (isActiveLock) {
            return;
        }
        MainFrame.activistTimeLabel.setForeground(Color.decode(colorConfig.get("activistTimeLabel")));
        TIME[0] = Constant.ACTIVE_TIME;
        if (!isStart) {
            ACTIVIST_TIMER = Executors.newSingleThreadScheduledExecutor();
            ACTIVIST_TIMER.scheduleAtFixedRate(() -> {
                if (TIME[0] >= 0) {
                    MainFrame.activistTimeLabel.setText(Language.get("activistLabel") + DateUtil.format(new Date(TIME[0]), "mm:ss"));
                }
                if (TIME[0] <= RED_TIME) {
                    MainFrame.activistTimeLabel.setForeground(Color.RED);
                }
                if (TIME[0] <= 0) {
                    //表格失活
                    log.info("主界面失活{}", DateUtil.now());
                    MainFrame.mainFrame.dispose();
                    LockFrame lockFrame = new LockFrame();
                    //窗体最小化
                    lockFrame.setExtendedState(JFrame.ICONIFIED);
                    lockFrame.setVisible(true);
                    isStart = false;
                    ACTIVIST_TIMER.shutdown();
                }
                TIME[0] -= 1000;
            }, 0, 1, TimeUnit.SECONDS);
            isStart = true;
        }
    }

    /**
     * 停止/启动活性时间计时
     *
     * @Author Crane Resigned
     * @Date 2023-05-24 17:30:32
     */
    public static void activeTimeLock() {
        activeTimeLock(false);
    }

    /**
     * 方法重载
     *
     * @param isIgnoreActiveBtnSelect 是否无视锁按钮是否选择，适用于主界面锁按钮本身的切换
     * @Author Crane Resigned
     * @Date 2024/9/15 17:28
     **/
    public static void activeTimeLock(boolean isIgnoreActiveBtnSelect) {
        //如果锁定按钮已经是选中状态，即是已经锁定的，那这个方法应当是无响应
        if ((!isIgnoreActiveBtnSelect && MainFrame.getActivistLockBtn().isSelected()) || Objects.isNull(ACTIVIST_TIMER)) {
            return;
        }
        isStart = false;
        ACTIVIST_TIMER.shutdown();
        isActiveLock = !isActiveLock;
        if (!isActiveLock) {
            activeTimeFresh();
        }
    }

}
