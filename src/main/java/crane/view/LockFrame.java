package crane.view;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import crane.constant.Constant;
import crane.constant.DefaultFont;
import crane.constant.MainFrameCst;
import crane.function.DefaultConfig;
import crane.function.FileTool;
import crane.function.Language;
import crane.model.jdbc.JdbcConnection;
import crane.model.service.FrameService;
import crane.model.service.SecurityService;
import crane.model.service.ShowMessgae;
import crane.view.main.MainFrame;
import crane.view.module.ComboBoxRender;
import lombok.extern.slf4j.Slf4j;

import javax.swing.*;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import javax.swing.plaf.metal.MetalToggleButtonUI;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;

/**
 * Description: 锁页面
 * Author: ZhouXingxue
 * Date: 2022/11/27 0:45
 *
 * @author Crane Resigned
 */
@Slf4j
public class LockFrame extends JFrame {

    /**
     * 是否本地开关
     * Author: Crane Resigned
     * Date: 2022-12-01 20:43:35
     */
    public static JToggleButton isLocal;

    /**
     * 是否创建新密钥登录开关
     * Author: Crane Resigned
     * Date: 2023-01-07 23:20:41
     */
    public static JToggleButton isCreateScene;

    /**
     * 是否启动轻量版
     * Author: Crane Resigned
     * Date: 2023-01-29 17:38:59
     */
    public static JToggleButton isLightWeightVersion;

    /**
     * 提示文本
     * Author: Crane Resigned
     * Date: 2022-12-30 23:36:25
     */
    protected JLabel tipLabel;

    /**
     * 密码文本
     * Author: Crane Resigned
     * Date: 2022-12-30 23:48:46
     */
    protected JPasswordField secretText;

    /**
     * 回车事件
     * Author: Crane Resigned
     * Date: 2022-12-30 23:50:51
     */
    protected KeyAdapter enterEvent;

    /**
     * 回车登录提示
     * Author: Crane Resigned
     * Date: 2022-12-30 23:57:40
     */
    protected JLabel loginTip;

    protected JComboBox<String> isEng;

    public LockFrame() {
        //检测密钥文件是否存在
        final boolean isHaveKey = SecurityService.checkKeyAmountIsNotZero();

        //窗体初始化
        this.setTitle(isHaveKey ? JdbcConnection.IS_TEST ? MainFrameCst.TEST_TITLE : MainFrameCst.MAIN_TITLE
                : Language.get("haveNotKeyTitle"));
        this.setSize(500, 300);
        this.setLayout(null);
        this.setResizable(false);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setIconImage(FrameService.getTitleImage());
        this.getContentPane().setBackground(Color.decode("#DAE4E6"));

        tipLabel = new JLabel(isHaveKey ? Language.get("haveKey") : Language.get("haveNotKey"));
        tipLabel.setBounds(50, 50, 400, 40);
        tipLabel.setForeground(Color.decode("#407E54"));
        tipLabel.setFont(new Font("微软雅黑", Font.BOLD, 25));
        this.add(tipLabel);

        secretText = new JPasswordField();
        secretText.setBounds(50, 120, 380, 35);
        secretText.setFont(new Font("微软雅黑", Font.PLAIN, 18));
        secretText.setForeground(Color.decode("#046D35"));
        secretText.setBorder(BorderFactory.createLineBorder(Color.decode("#B8CE8E")));
        secretText.setHorizontalAlignment(JPasswordField.CENTER);
        enterEvent = new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                //回车触发
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    String passTxt = new String(secretText.getPassword());
                    //是否为创建新密钥登录
                    if (isCreateScene.isSelected()) {
                        if (!SecurityService.checkKeyFileIsExist(passTxt)) {
                            SecurityService.createKey(passTxt);
                        } else {
                            ShowMessgae.showWarningMessage(Language.get("keyDuplicateTipMsg"), Language.get("keyDuplicateTipTitle"));
                        }
                    } else {
                        //是否有密匙
                        if (isHaveKey) {
                            log.info("检测匹配");
                            //检测该密钥是否存在
                            if (!SecurityService.checkKeyFileIsExist(passTxt)) {
                                log.info("匹配失败，密钥文件不存在");
                                ShowMessgae.showWarningMessage(Language.get("keyErrTipMsg"), Language.get("keyErrTipTitle"));
                                secretText.setText(null);
                                return;
                            }
                        } else {
                            log.info("一个密匙都没有，创建密匙");
                            SecurityService.createKey(passTxt);
                        }
                    }
                    log.info("密钥匹配成功" + DateUtil.now());
                    //关闭当前
                    close();
                    //赋值轻量版标识
                    Constant.IS_LIGHT = isLightWeightVersion.isSelected();
                    try {
                        String className = Language.get("className");
                        if (!className.startsWith("crane")) {
                            throw new Exception();
                        } else {
                            MainFrame mainFrame = (MainFrame) Class.forName(className).getConstructor().newInstance();
                            mainFrame.setVisible(true);
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        ShowMessgae.showErrorMessage(Language.get("startMainFrameErrMsg"), Language.get("startMainFrameErrTitle"));
                    }
                }
            }
        };
        secretText.addKeyListener(enterEvent);
        this.add(secretText);

        //回车登录提示
        loginTip = new JLabel("√Enter");
        loginTip.setBounds(385, 155, 100, 30);
        loginTip.setForeground(Color.decode("#7D2720"));
        loginTip.setFont(new Font("微软雅黑", Font.BOLD, 13));
        this.add(loginTip);

        //是否是本地数据库
        boolean isLocalServer = Boolean.parseBoolean(DefaultConfig.getDefaultProperty("isLocalServer"));
        isLocal = new JToggleButton(isLocalServer ? Language.get("isLocal") : Language.get("isLocal2"), isLocalServer);
        isLocal.setBounds(50, 200, 100, 30);
        isLocal.setForeground(Color.WHITE);
        isLocal.setFont(DefaultFont.WEI_RUAN_PLAIN_13.getFont());
        isLocal.setBackground(Color.decode("#68A694"));
        //设置选中的颜色
        isLocal.setUI(new MetalToggleButtonUI() {
            @Override
            protected Color getSelectColor() {
                return Color.decode("#046D35");
            }
        });
        isLocal.setBorder(null);
        isLocal.addActionListener(e -> {
            boolean selected = isLocal.isSelected();
            if (!selected) {
                ShowMessgae.showWarningMessage(Language.get("isLocal2TipMsg"), Language.get("isLocal2TipTitle"));
                isLocal.setText(Language.get("isLocal2"));
            } else {
                isLocal.setText(Language.get("isLocal"));
            }
            //设置默认数据库
            DefaultConfig.setDefaultProperty("isLocalServer", String.valueOf(selected));
        });
        this.add(isLocal);

        isCreateScene = new JToggleButton(Language.get("isCreateScene"));
        isCreateScene.setBounds(180, 200, 110, 30);
        isCreateScene.setForeground(Color.WHITE);
        isCreateScene.setFont(DefaultFont.WEI_RUAN_PLAIN_13.getFont());
        isCreateScene.setUI(new MetalToggleButtonUI() {
            @Override
            protected Color getSelectColor() {
                return Color.decode("#2A3050");
            }
        });
        isCreateScene.setBorder(null);
        isCreateScene.setBackground(Color.decode("#65BAD1"));
        isCreateScene.addActionListener(e -> {
            if (isCreateScene.isSelected()) {
                ShowMessgae.showInformationMessage(Language.get("isCreateSceneTipMsg"), Language.get("isCreateSceneTipTitle"));
            }
        });
        this.add(isCreateScene);

        boolean isFileModel = Boolean.parseBoolean(DefaultConfig.getDefaultProperty("isFileModel"));
        isLightWeightVersion = new JToggleButton(isFileModel ? Language.get("isLightWeightVersion") : Language.get("isLightWeightVersion2"), isFileModel);
        isLightWeightVersion.setBounds(320, 200, 100, 30);
        isLightWeightVersion.setForeground(Color.WHITE);
        isLightWeightVersion.setFont(DefaultFont.WEI_RUAN_PLAIN_13.getFont());
        isLightWeightVersion.setUI(new MetalToggleButtonUI() {
            @Override
            protected Color getSelectColor() {
                return Color.decode("#9AB3CD");
            }
        });
        isLightWeightVersion.setBorder(null);
        isLightWeightVersion.setBackground(Color.decode("#407E54"));
        isLightWeightVersion.addActionListener(e -> {
            boolean selected = isLightWeightVersion.isSelected();
            if (selected) {
                isLightWeightVersion.setText(Language.get("isLightWeightVersion"));
            } else {
                isLightWeightVersion.setText(Language.get("isLightWeightVersion2"));
                ShowMessgae.showPlainMessage(Language.get("isLightWeightVersion2TipMsg"), Language.get("isLightWeightVersion2TipTitle"));
            }
            //设置默认模式
            DefaultConfig.setDefaultProperty("isFileModel", String.valueOf(selected));
        });
        this.add(isLightWeightVersion);

        isEng = new JComboBox<>(Language.getLanguages());
        isEng.setSelectedItem(Language.get("language"));
        isEng.setBounds(0, 0, 80, 25);
        isEng.setForeground(Color.WHITE);
        isEng.setFont(DefaultFont.WEI_RUAN_PLAIN_13.getFont());
        isEng.setBackground(Color.decode("#407E54"));
        isEng.setRenderer(new ComboBoxRender(isEng.getRenderer()));
        isEng.addPopupMenuListener(new PopupMenuListener() {
            @Override
            public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
                log.info("菜单展开");
            }

            @Override
            public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {
                log.info("菜单关闭");
                String selectedItem = String.valueOf(isEng.getSelectedItem());
                log.info("当前选择项为：" + selectedItem);
                if (!StrUtil.equals(Language.get("language"), selectedItem)) {
                    Language.refresh(selectedItem);
                    close();
                    new LockFrame().setVisible(true);
                    //设置默认语言
                    DefaultConfig.setDefaultProperty("language", selectedItem);
                }
            }

            @Override
            public void popupMenuCanceled(PopupMenuEvent e) {
                log.info("取消选择");
            }
        });

        //查看功能按钮
        JButton lookFunBtn = new JButton(Language.get("lookFunBtn"));
        lookFunBtn.setBounds(380, 0, 100, 30);
        lookFunBtn.setForeground(Color.BLACK);
        lookFunBtn.setBackground(Color.WHITE);
        lookFunBtn.setFont(new Font("微软雅黑", Font.BOLD, 13));
        lookFunBtn.setHorizontalAlignment(JLabel.CENTER);
        lookFunBtn.addActionListener(e -> {
            File logFile = new File("function.html");
            if (logFile.exists()) {
                FileTool.openFile(logFile.getPath());
            }else {
                ShowMessgae.showErrorMessage(Language.get("notFunFile"),Language.get("notFunFileTit"));
            }
        });
        this.add(lookFunBtn);

        this.add(isEng);
    }

    private void close() {
        this.dispose();
    }

    public static void start() {
        LockFrame lockFrame = new LockFrame();
        lockFrame.setVisible(true);
        //启动界面默认聚焦
        lockFrame.secretText.dispatchEvent(new FocusEvent(lockFrame.secretText, FocusEvent.FOCUS_GAINED, true));
        lockFrame.secretText.requestFocusInWindow();
    }

}
