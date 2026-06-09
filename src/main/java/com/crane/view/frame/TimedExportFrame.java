package com.crane.view.frame;

import cn.hutool.core.util.StrUtil;
import com.crane.constant.DefaultFont;
import com.crane.view.config.Language;
import com.crane.view.frame.module.CustomFrame;
import com.crane.view.frame.module.stylehelper.BlinkBorderHelper;
import com.crane.view.service.MessageService;
import com.crane.view.service.TimedExportService;
import com.crane.view.tools.ShowMessage;

import javax.swing.*;
import java.awt.*;
import java.io.File;

/**
 * 定时导出明文数据配置页。
 */
public class TimedExportFrame extends CustomFrame {

    private final JTextField pathTextField = new JTextField();
    private final JTextField intervalTextField = new JTextField();
    private final JLabel enabledLabel = new JLabel();
    private final JLabel latestLabel = new JLabel();
    private final JLabel nextRunLabel = new JLabel();
    private final JTextArea ruleArea = new JTextArea();

    public TimedExportFrame() {
        super(560, 390, null);
        this.setTitle(Language.get("timedExportTitle"));
        this.getContentPane().setBackground(Color.decode(colorConfig.get("lockBg")));

        JLabel titleLabel = new JLabel(Language.get("timedExportTitle"));
        titleLabel.setBounds(50, 56, 300, 32);
        titleLabel.setFont(new Font("微软雅黑", Font.BOLD, 20));
        titleLabel.setForeground(Color.decode(colorConfig.get("lockTipLabel")));
        this.add(titleLabel);

        JLabel pathLabel = new JLabel(Language.get("timedExportPathLabel"));
        pathLabel.setBounds(50, 106, 120, 30);
        pathLabel.setFont(DefaultFont.WEI_RUAN_PLAIN_15.getFont());
        pathLabel.setForeground(Color.decode(colorConfig.get("lockTipLabel")));
        this.add(pathLabel);

        pathTextField.setBounds(170, 106, 330, 32);
        pathTextField.setFont(DefaultFont.WEI_RUAN_PLAIN_13.getFont());
        pathTextField.setForeground(Color.decode(colorConfig.get("pathTextFore")));
        pathTextField.setBackground(Color.decode(colorConfig.get("outputAreaBg")));
        pathTextField.setBorder(BorderFactory.createLineBorder(Color.decode(colorConfig.get("pathTextLineBor"))));
        pathTextField.setText(TimedExportService.getExportPath());
        this.add(pathTextField);

        JButton chooseButton = buildButton(Language.get("chooseFileBtn"), 400, 146, 100, 30,
                "importBtnFore", "importBtnBg", "importBtnBlinkBorIn");
        chooseButton.addActionListener(e -> chooseDirectory());
        this.add(chooseButton);

        JLabel intervalLabel = new JLabel(Language.get("timedExportIntervalLabel"));
        intervalLabel.setBounds(50, 146, 120, 30);
        intervalLabel.setFont(DefaultFont.WEI_RUAN_PLAIN_15.getFont());
        intervalLabel.setForeground(Color.decode(colorConfig.get("lockTipLabel")));
        this.add(intervalLabel);

        intervalTextField.setBounds(170, 146, 210, 32);
        intervalTextField.setFont(DefaultFont.WEI_RUAN_PLAIN_13.getFont());
        intervalTextField.setForeground(Color.decode(colorConfig.get("pathTextFore")));
        intervalTextField.setBackground(Color.decode(colorConfig.get("outputAreaBg")));
        intervalTextField.setBorder(BorderFactory.createLineBorder(Color.decode(colorConfig.get("pathTextLineBor"))));
        intervalTextField.setText(TimedExportService.getIntervalHours() + "h");
        this.add(intervalTextField);

        enabledLabel.setBounds(50, 196, 460, 24);
        enabledLabel.setFont(DefaultFont.WEI_RUAN_PLAIN_13.getFont());
        enabledLabel.setForeground(Color.decode(colorConfig.get("loginTip")));
        this.add(enabledLabel);

        latestLabel.setBounds(50, 220, 460, 24);
        latestLabel.setFont(DefaultFont.WEI_RUAN_PLAIN_13.getFont());
        latestLabel.setForeground(Color.decode(colorConfig.get("loginTip")));
        this.add(latestLabel);

        nextRunLabel.setBounds(50, 244, 460, 24);
        nextRunLabel.setFont(DefaultFont.WEI_RUAN_PLAIN_13.getFont());
        nextRunLabel.setForeground(Color.decode(colorConfig.get("loginTip")));
        this.add(nextRunLabel);

        ruleArea.setBounds(50, 276, 450, 46);
        ruleArea.setFont(DefaultFont.WEI_RUAN_PLAIN_13.getFont());
        ruleArea.setForeground(Color.decode(colorConfig.get("outputAreaFore2")));
        ruleArea.setBackground(Color.decode(colorConfig.get("outputAreaBg")));
        ruleArea.setBorder(BorderFactory.createLineBorder(Color.decode(colorConfig.get("outputAreaBor"))));
        ruleArea.setEditable(false);
        ruleArea.setFocusable(false);
        ruleArea.setLineWrap(true);
        ruleArea.setWrapStyleWord(true);
        ruleArea.setText(TimedExportService.getExecutionRuleText());
        this.add(ruleArea);

        JButton testButton = buildButton(Language.get("timedExportTestBtn"), 170, 336, 120, 32,
                "addBtnFore", "addBtnBg", "addBtnBlinkBorIn");
        testButton.addActionListener(e -> testExport());
        this.add(testButton);

        JButton saveButton = buildButton(Language.get("timedExportSaveBtn"), 320, 336, 120, 32,
                "exportBtnFore", "exportBtnBg", "exportBtnBlinkBorIn");
        saveButton.addActionListener(e -> saveConfig());
        this.add(saveButton);

        refreshStatusLabels();
    }

    private JButton buildButton(String text, int x, int y, int width, int height,
                                String foreColorKey, String bgColorKey, String borderColorKey) {
        JButton button = new JButton(text);
        button.setBounds(x, y, width, height);
        button.setForeground(Color.decode(colorConfig.get(foreColorKey)));
        button.setBackground(Color.decode(colorConfig.get(bgColorKey)));
        button.setFont(DefaultFont.WEI_RUAN_BOLD_12.getFont());
        button.setFocusPainted(false);
        button.setBorder(null);
        button.setHorizontalAlignment(JLabel.CENTER);
        BlinkBorderHelper.addBorder(button, BorderFactory.createLineBorder(Color.decode(colorConfig.get(borderColorKey)), 2), null);
        return button;
    }

    private void refreshStatusLabels() {
        enabledLabel.setText(Language.get("timedExportEnabledLabel")
                + (TimedExportService.isFeatureEnabled() ? Language.get("timedExportEnabledYes") : Language.get("timedExportEnabledNo")));
        latestLabel.setText(Language.get("timedExportLastTime") + TimedExportService.getLastExportTimeText());
        nextRunLabel.setText(Language.get("timedExportNextTime") + TimedExportService.getNextExportTimeText());
    }

    private void chooseDirectory() {
        JFileChooser chooser = new JFileChooser();
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        chooser.setDialogTitle(Language.get("chooserDialogExportTitle"));
        if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
            File selectedFile = chooser.getSelectedFile();
            if (selectedFile != null) {
                pathTextField.setText(selectedFile.getAbsolutePath());
            }
        }
    }

    private void saveConfig() {
        String path = pathTextField.getText();
        String interval = intervalTextField.getText();
        if (StrUtil.isBlank(path) || StrUtil.isBlank(interval)) {
            ShowMessage.showWarningMessage(Language.get("timedExportEmptyTipMsg"), Language.get("timedExportEmptyTipTitle"));
            return;
        }
        try {
            TimedExportService.saveConfig(path, interval);
            MessageService.outputMessage(Language.get("timedExportLogConfigured") + path + " / " + interval);
            refreshStatusLabels();
            ShowMessage.showInformationMessage(
                    Language.get("timedExportSaveSuccessMsg") + "\n" + TimedExportService.getExecutionRuleText(),
                    Language.get("successfulTit"));
            dispose();
        } catch (Exception ex) {
            ShowMessage.showErrorMessage(ex.getStackTrace(), Language.get("timedExportSaveFailTitle"));
        }
    }

    private void testExport() {
        String path = pathTextField.getText();
        String interval = intervalTextField.getText();
        if (StrUtil.isBlank(path) || StrUtil.isBlank(interval)) {
            ShowMessage.showWarningMessage(Language.get("timedExportEmptyTipMsg"), Language.get("timedExportEmptyTipTitle"));
            return;
        }
        try {
            TimedExportService.saveConfig(path, interval);
            boolean success = TimedExportService.testExportNow(path);
            refreshStatusLabels();
            if (success) {
                ShowMessage.showInformationMessage(Language.get("timedExportTestSuccessMsg"), Language.get("successfulTit"));
            } else {
                ShowMessage.showWarningMessage(Language.get("timedExportTestFailMsg"), Language.get("timedExportSaveFailTitle"));
            }
        } catch (Exception ex) {
            ShowMessage.showErrorMessage(ex.getStackTrace(), Language.get("timedExportSaveFailTitle"));
        }
    }
}
