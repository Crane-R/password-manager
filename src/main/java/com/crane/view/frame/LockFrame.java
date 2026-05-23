package com.crane.view.frame;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.crane.constant.Constant;
import com.crane.constant.DefaultFont;
import com.crane.constant.MainFrameCst;
import com.crane.model.jdbc.JdbcConnection;
import com.crane.model.service.SecurityService;
import com.crane.view.config.Config;
import com.crane.view.config.Language;
import com.crane.view.frame.module.ComboBoxRender;
import com.crane.view.frame.module.CustomFrame;
import com.crane.view.frame.module.stylehelper.BlinkBorderHelper;
import com.crane.view.service.LookFucService;
import com.crane.view.service.TimedExportService;
import com.crane.view.tools.ShowMessage;
import lombok.extern.slf4j.Slf4j;

import javax.swing.*;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import javax.swing.plaf.metal.MetalToggleButtonUI;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/**
 * Description: 锁页面
 * Author: ZhouXingxue
 * Date: 2022/11/27 0:45
 *
 * @author Crane Resigned
 */
@Slf4j
public class LockFrame extends CustomFrame {

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

    protected JTextField sceneLabelText;

    protected JLabel sceneLabelLabel;

    protected KeyAdapter enterEvent;

    /**
     * 回车登录提示
     * Author: Crane Resigned
     * Date: 2022-12-30 23:57:40
     */
    protected JLabel loginTip;

    protected JComboBox<String> isEng;

    protected JButton lookFunBtn;

    public LockFrame() {
        super(484, 320, e -> System.exit(0));
        final boolean isHaveKey = SecurityService.checkKeyAmountIsNotZero();
        String currentTitle = isHaveKey ? JdbcConnection.IS_TEST ? MainFrameCst.TEST_TITLE : MainFrameCst.MAIN_TITLE
                : Language.get("haveNotKeyTitle");
        this.getContentPane().setBackground(Color.decode(colorConfig.get("lockBg")));

        String currentTipLabelKey = isHaveKey ? Language.get("haveKey") : Language.get("haveNotKey");
        tipLabel = new JLabel(currentTipLabelKey);
        tipLabel.setBounds(50, 66, 400, 40);
        tipLabel.setForeground(Color.decode(colorConfig.get("lockTipLabel")));
        tipLabel.setFont(new Font("微软雅黑", Font.BOLD, 25));
        this.add(tipLabel);

        secretText = new JPasswordField();
        secretText.setBounds(50, 132, 380, 35);
        secretText.setFont(new Font("微软雅黑", Font.BOLD, 20));
        secretText.setForeground(Color.decode(colorConfig.get("lockSecretText")));
        secretText.setBorder(BorderFactory.createLineBorder(Color.decode(colorConfig.get("lockSecretTextBorder"))));
        secretText.setHorizontalAlignment(JPasswordField.CENTER);

        sceneLabelLabel = new JLabel(Language.get("sceneLabel"));
        sceneLabelLabel.setBounds(50, 173, 110, 30);
        sceneLabelLabel.setForeground(Color.decode(colorConfig.get("lockTipLabel")));
        sceneLabelLabel.setFont(DefaultFont.WEI_RUAN_PLAIN_13.getFont());
        sceneLabelLabel.setVisible(false);
        this.add(sceneLabelLabel);

        sceneLabelText = new JTextField();
        sceneLabelText.setBounds(170, 173, 260, 30);
        sceneLabelText.setFont(DefaultFont.WEI_RUAN_PLAIN_13.getFont());
        sceneLabelText.setForeground(Color.decode(colorConfig.get("lockSecretText")));
        sceneLabelText.setBorder(BorderFactory.createLineBorder(Color.decode(colorConfig.get("lockSecretTextBorder"))));
        sceneLabelText.setToolTipText(Language.get("sceneLabelInputTip"));
        sceneLabelText.setVisible(false);
        this.add(sceneLabelText);

        enterEvent = new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    handleLoginOrCreate(isHaveKey);
                }
            }
        };
        secretText.addKeyListener(enterEvent);
        sceneLabelText.addKeyListener(enterEvent);
        this.add(secretText);

        loginTip = new JLabel(Language.get("loginEnterLabel"));
        loginTip.setBounds(340, 205, 100, 30);
        loginTip.setForeground(Color.decode(colorConfig.get("loginTip")));
        loginTip.setFont(new Font("微软雅黑", Font.BOLD, 13));
        this.add(loginTip);

        boolean isLocalServer = Boolean.parseBoolean(new Config(null).get("isLocalServer"));
        isLocal = new JToggleButton(isLocalServer ? Language.get("isLocal2") : Language.get("isLocal"), isLocalServer);
        isLocal.setBounds(185, 242, 110, 30);
        isLocal.setForeground(Color.WHITE);
        isLocal.setFont(DefaultFont.WEI_RUAN_PLAIN_13.getFont());
        isLocal.setBackground(Color.decode(colorConfig.get("isLocalBg")));
        isLocal.setUI(new MetalToggleButtonUI() {
            @Override
            protected Color getSelectColor() {
                return Color.decode(colorConfig.get("isLocalSelectBg"));
            }
        });
        isLocal.setBorder(null);
        isLocal.addActionListener(e -> {
            boolean selected = isLocal.isSelected();
            if (!selected) {
                ShowMessage.showWarningMessage(Language.get("isLocal2TipMsg"), Language.get("isLocal2TipTitle"));
                isLocal.setText(Language.get("isLocal"));
            } else {
                isLocal.setText(Language.get("isLocal2"));
            }
            new Config(null).set("isLocalServer", String.valueOf(selected));
        });
        BlinkBorderHelper.addBorder(isLocal, BorderFactory.createLineBorder(Color.WHITE, 2), null);
        this.add(isLocal);

        isCreateScene = new JToggleButton(Language.get("loginShowScene"));
        isCreateScene.setBounds(50, 242, 100, 30);
        isCreateScene.setForeground(Color.decode(colorConfig.get("isCreateScene")));
        isCreateScene.setFont(DefaultFont.WEI_RUAN_PLAIN_13.getFont());
        isCreateScene.setUI(new MetalToggleButtonUI() {
            @Override
            protected Color getSelectColor() {
                return Color.decode(colorConfig.get("isCreateSelectBg"));
            }
        });
        isCreateScene.setBorder(null);
        isCreateScene.setBackground(Color.decode(colorConfig.get("isCreateBg")));
        isCreateScene.addActionListener(e -> toggleCreateScene(currentTipLabelKey));
        BlinkBorderHelper.addBorder(isCreateScene, BorderFactory.createLineBorder(Color.WHITE, 2), null);
        this.add(isCreateScene);

        boolean isFileModel = Boolean.parseBoolean(new Config(null).get("isFileModel"));
        if (isFileModel) {
            isLocal.setVisible(false);
        }
        String fileMode = Language.get("isFileMode");
        String dataMode = Language.get("isDataMode");
        isLightWeightVersion = new JToggleButton(isFileModel ? fileMode : dataMode, isFileModel);
        this.setTitle(currentTitle + " - " + (isFileModel ? dataMode : fileMode));
        isLightWeightVersion.setBounds(330, 242, 100, 30);
        isLightWeightVersion.setForeground(Color.decode(colorConfig.get("isLightWeightVersion")));
        isLightWeightVersion.setFont(DefaultFont.WEI_RUAN_PLAIN_13.getFont());
        isLightWeightVersion.setUI(new MetalToggleButtonUI() {
            @Override
            protected Color getSelectColor() {
                return Color.decode(colorConfig.get("isLightSelectBg"));
            }
        });
        isLightWeightVersion.setBorder(null);
        isLightWeightVersion.setBackground(Color.decode(colorConfig.get("isLightBg")));
        isLightWeightVersion.addActionListener(e -> {
            boolean selected = isLightWeightVersion.isSelected();
            if (selected) {
                isLightWeightVersion.setText(fileMode);
                isLocal.setVisible(false);
                this.setTitle(currentTitle + " - " + dataMode);
            } else {
                isLightWeightVersion.setText(dataMode);
                isLocal.setVisible(true);
                this.setTitle(currentTitle + " - " + fileMode);
            }
            new Config(null).set("isFileModel", String.valueOf(selected));
        });
        BlinkBorderHelper.addBorder(isLightWeightVersion, BorderFactory.createLineBorder(Color.decode(colorConfig.get("isLightBorder")), 2), null);
        this.add(isLightWeightVersion);

        isEng = new JComboBox<>(Language.getLanguages());
        isEng.setSelectedItem(Language.get("language"));
        isEng.setBounds(400, 26, 80, 30);
        isEng.setForeground(Color.decode(colorConfig.get("isEng")));
        isEng.setFont(DefaultFont.WEI_RUAN_PLAIN_13.getFont());
        isEng.setBackground(Color.decode(colorConfig.get("isEngBg")));
        isEng.setRenderer(new ComboBoxRender(isEng.getRenderer()));
        isEng.addPopupMenuListener(new PopupMenuListener() {
            @Override
            public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
                log.info("菜单展开");
            }

            @Override
            public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {
                String selectedItem = String.valueOf(isEng.getSelectedItem());
                if (!StrUtil.equals(Language.get("language"), selectedItem)) {
                    Language.refresh(selectedItem);
                    close();
                    new LockFrame().setVisible(true);
                    new Config(null).set("language", selectedItem);
                }
            }

            @Override
            public void popupMenuCanceled(PopupMenuEvent e) {
                log.info("取消选择");
            }
        });

        lookFunBtn = new JButton(Language.get("lookFunBtn"));
        lookFunBtn.setBounds(361, 26, 100, 30);
        lookFunBtn.setForeground(Color.decode(colorConfig.get("lookFun")));
        lookFunBtn.setBackground(Color.decode(colorConfig.get("lookFunBg")));
        lookFunBtn.setFont(new Font("微软雅黑", Font.PLAIN, 13));
        lookFunBtn.setHorizontalAlignment(JLabel.CENTER);
        lookFunBtn.setBorder(null);
        lookFunBtn.addActionListener(e -> new LookFucService().openFile());
        JLayeredPane funBtnEngLay = new JLayeredPane();
        funBtnEngLay.setSize(this.getSize());
        funBtnEngLay.add(lookFunBtn, 0);
        funBtnEngLay.add(isEng, 9);
        this.add(funBtnEngLay);
    }

    private void handleLoginOrCreate(boolean isHaveKey) {
        String passTxt = new String(secretText.getPassword());
        if (isCreateScene.isSelected()) {
            createScene(passTxt);
        } else {
            loginScene(isHaveKey, passTxt);
        }
    }

    private void createScene(String passTxt) {
        if (passTxt.length() < Constant.LEAST_PASS_LEN) {
            ShowMessage.showWarningMessage(Language.get("leastPassLengthMsg")
                    + Constant.LEAST_PASS_LEN, Language.get("leastPassLengthTit"));
            return;
        }
        String sceneLabel = normalizeSceneLabel(sceneLabelText.getText());
        if (StrUtil.isBlank(sceneLabel)) {
            ShowMessage.showWarningMessage(Language.get("sceneLabelNullTipMsg"), Language.get("sceneLabelNullTipTitle"));
            sceneLabelText.requestFocusInWindow();
            return;
        }
        if (!SecurityService.checkKeyFileIsExist(passTxt)) {
            SecurityService.createKey(passTxt, sceneLabel);
            openMainFrame();
        } else {
            ShowMessage.showWarningMessage(Language.get("keyDuplicateTipMsg"), Language.get("keyDuplicateTipTitle"));
        }
    }

    private void loginScene(boolean isHaveKey, String passTxt) {
        if (isHaveKey) {
            if (!SecurityService.checkKeyFileIsExist(passTxt)) {
                ShowMessage.showWarningMessage(Language.get("keyErrTipMsg"), Language.get("keyErrTipTitle"));
                secretText.setText(null);
                return;
            }
            if (!ensureSceneLabel(passTxt)) {
                return;
            }
        } else {
            if (passTxt.length() < Constant.LEAST_PASS_LEN) {
                ShowMessage.showWarningMessage(Language.get("leastPassLengthMsg")
                        + Constant.LEAST_PASS_LEN, Language.get("leastPassLengthTit"));
                return;
            }
            String sceneLabel = promptSceneLabel(Language.get("sceneLabelNullTipMsg"), Language.get("sceneLabelNullTipTitle"));
            if (sceneLabel == null) {
                return;
            }
            SecurityService.createKey(passTxt, sceneLabel);
        }
        openMainFrame();
    }

    private boolean ensureSceneLabel(String passTxt) {
        if (SecurityService.hasSceneLabel(passTxt)) {
            Constant.CURRENT_SCENE_LABEL = SecurityService.getSceneLabel(passTxt);
            return true;
        }
        String sceneLabel = promptSceneLabel(Language.get("sceneLabelFixTipMsg"), Language.get("sceneLabelFixTipTitle"));
        if (sceneLabel == null) {
            return false;
        }
        SecurityService.updateSceneLabel(passTxt, sceneLabel);
        Constant.CURRENT_SCENE_LABEL = sceneLabel;
        return true;
    }

    private String promptSceneLabel(String message, String title) {
        while (true) {
            String input = JOptionPane.showInputDialog(this, message, title, JOptionPane.PLAIN_MESSAGE);
            if (input == null) {
                return null;
            }
            String sceneLabel = normalizeSceneLabel(input);
            if (StrUtil.isNotBlank(sceneLabel)) {
                return sceneLabel;
            }
            ShowMessage.showWarningMessage(Language.get("sceneLabelNullTipMsg"), Language.get("sceneLabelNullTipTitle"));
        }
    }

    private String normalizeSceneLabel(String sceneLabel) {
        if (sceneLabel == null) {
            return null;
        }
        String normalized = sceneLabel.trim().replace("\r", " ").replace("\n", " ");
        return StrUtil.isBlank(normalized) ? null : normalized;
    }

    private void toggleCreateScene(String currentTipLabelKey) {
        if (isCreateScene.isSelected()) {
            isCreateScene.setText(Language.get("registerShowScene"));
            tipLabel.setText(Language.get("registerKey"));
            sceneLabelLabel.setVisible(true);
            sceneLabelText.setVisible(true);
            sceneLabelText.requestFocusInWindow();
        } else {
            isCreateScene.setText(Language.get("loginShowScene"));
            tipLabel.setText(currentTipLabelKey);
            sceneLabelLabel.setVisible(false);
            sceneLabelText.setVisible(false);
            sceneLabelText.setText(null);
            secretText.requestFocusInWindow();
        }
    }

    private void openMainFrame() {
        log.info("场景登录成功：{}", DateUtil.now());
        close();
        Constant.IS_LIGHT = isLightWeightVersion.isSelected();
        try {
            String className = Language.get("className");
            if (!className.startsWith("com.crane")) {
                throw new Exception();
            }
            MainFrame mainFrame = (MainFrame) Class.forName(className).getConstructor().newInstance();
            mainFrame.setVisible(true);
        } catch (Exception ex) {
            ShowMessage.showErrorMessage(ex.getStackTrace(), Language.get("startMainFrameErrTitle"));
            ShowMessage.showErrorMessage(Language.get("startMainFrameErrMsg"), Language.get("startMainFrameErrTitle"));
        }
    }

    private void close() {
        this.dispose();
    }

    public static void start() {
        LockFrame lockFrame = new LockFrame();
        lockFrame.setVisible(true);
        lockFrame.secretText.dispatchEvent(new FocusEvent(lockFrame.secretText, FocusEvent.FOCUS_GAINED, true));
        lockFrame.secretText.requestFocusInWindow();
    }
}

