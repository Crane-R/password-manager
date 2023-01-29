package crane.model.service;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import crane.constant.Constant;
import crane.view.MainFrame;
import lombok.extern.slf4j.Slf4j;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Date;
import java.util.Objects;
import java.util.concurrent.*;

/**
 * @author Crane Resigned
 */
@Slf4j
public class FrameService {

    /**
     * 启动标识
     * Author: Crane Resigned
     * Date: 2023-01-22 19:09:01
     */
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

    /**
     * 获取标题栏的图片
     *
     * @Author Crane Resigned
     * @Date 2022-04-29 18:47:48
     */
    public static Image getTitleImage() {
        try {
            return ImageIO.read(Objects.requireNonNull(FrameService.class.getResource("/img/pm4.3.0_10.png")));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 更新活性时间
     * Author: Crane Resigned
     * Date: 2023-01-22 18:38:05
     */
    public static void activeTimeFresh() {
        MainFrame.activistTimeLabel.setForeground(Color.decode("#1A5599"));
        TIME[0] = Constant.ACTIVE_TIME;
        if (!isStart) {
            ScheduledExecutorService activistTimer = Executors.newSingleThreadScheduledExecutor();
            activistTimer.scheduleAtFixedRate(() -> {
                if (TIME[0] >= 0) {
                    MainFrame.activistTimeLabel.setText("活性时间剩余：" + DateUtil.format(new Date(TIME[0]), "mm:ss"));
                }
                if (TIME[0] <= RED_TIME) {
                    MainFrame.activistTimeLabel.setForeground(Color.RED);
                }
                if (TIME[0] <= 0) {
                    //表格失活
                    log.info("表格失活" + DateUtil.now());
                    if (MainFrame.jTable.getRowCount() > 0) {
                        AccountService.setTableMessages();
                        AccountService.toggleStatus(true);
                    }
                    isStart = false;
                    activistTimer.shutdown();
                }
                TIME[0] -= 1000;
            }, 0, 1, TimeUnit.SECONDS);
            isStart = true;
        }
    }

}
