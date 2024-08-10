package com.crane.view.main;

import cn.hutool.core.util.StrUtil;
import com.crane.constant.Constant;
import com.crane.constant.DefaultFont;
import com.crane.constant.ExportImportCst;
import com.crane.constant.MainFrameCst;
import com.crane.model.jdbc.JdbcConnection;
import com.crane.model.service.AccountService;
import com.crane.model.service.lightweight.LightService;
import com.crane.view.*;
import com.crane.view.function.config.Config;
import com.crane.view.function.config.Language;
import com.crane.view.function.service.*;
import com.crane.view.function.tools.ShowMessage;
import com.crane.view.function.tools.TextTools;
import com.crane.view.module.CustomTitle;
import com.crane.view.module.QueueTextArea;
import com.crane.view.module.ScrollBarUi;
import com.crane.view.module.stylehelper.BlinkBorderHelper;
import com.crane.view.module.stylehelper.MenuBlinkBackHelper;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.plaf.metal.MetalToggleButtonUI;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import java.util.Timer;
import java.util.*;
import java.util.concurrent.*;

/**
 * @author Crane Resigned
 */
@Slf4j
public class MainFrame extends JFrame {

    /**
     * 搜索文本
     *
     * @Author Crane Resigned
     * @Date 2022-06-11 23:49:42
     */
    @Getter
    protected static JTextField searchText;

    /**
     * 数据表条数
     *
     * @Author Crane Resigned
     * @Date 2022-06-11 23:49:12
     */
    @Getter
    protected static JLabel resultNumbers;

    /**
     * 数据表格
     *
     * @Author Crane Resigned
     * @Date 2022-06-02 23:19:34
     */
    public static JTable jTable;

    /**
     * 鼠标右键弹出菜单
     *
     * @Author Crane Resigned
     * @Date 2022-06-02 23:19:39
     */
    protected final JPopupMenu jPopupMenu = new JPopupMenu();

    /**
     * 搜索按钮的文本切换标记
     * true为解密
     * false为查询
     * Author: Crane Resigned
     * Date: 2022-11-27 02:03:43
     */
    public static boolean switchRecord = false;

    /**
     * 实时搜索开关
     * Author: Crane Resigned
     * Date: 2022-11-26 23:50:37
     */
    protected final JToggleButton realTimeSearchBtn;

    public String SEARCH_BTN_TXT1 = Language.get("searchBtn");
    public String SEARCH_BTN_TXT2 = Language.get("searchBtn2");

    /**
     * 搜索按钮
     * Author: Crane Resigned
     * Date: 2022-11-27 15:22:53
     */
    public static JButton searchButton;

    /**
     * 活性时间
     * Author: Crane Resigned
     * Date: 2023-01-22 18:30:55
     */
    public static JLabel activistTimeLabel = new JLabel(String.valueOf(Constant.ACTIVE_TIME));

    /**
     * 复制提醒消息
     *
     * @Author Crane Resigned
     * @Date 2023-02-08 23:03:35
     */
    @Deprecated
    protected final JLabel copyAlertLabel;

    /**
     * 载存主窗体对象
     *
     * @Author Crane Resigned
     * @Date 2023-05-24 17:16:36
     */
    public static MainFrame mainFrame;

    protected JButton aboutBtn;

    @Getter
    protected static JToggleButton activistLockBtn;

    protected JLabel searchTip;

    protected JButton exportBtn;

    protected JButton importBtn;

    protected JLabel disclaimerLabel;

    private final Config colorConfig = Constant.colorConfig;

    @Getter
    private static QueueTextArea outputArea;

    public MainFrame() {
        this.setSize(1190, 800);
        this.setLayout(null);
        this.setResizable(false);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //设置标题栏的图标
        this.setIconImage(ImageService.getTitleImage());
        this.getContentPane().setBackground(Color.decode(colorConfig.get("contentPaneBg")));
        this.setUndecorated(true);
        CustomTitle title = new CustomTitle(this);
        this.add(title);

        title.setTitle((JdbcConnection.IS_TEST ? MainFrameCst.TEST_TITLE : MainFrameCst.MAIN_TITLE) + " >> "
                + (Constant.IS_LIGHT ? Language.get("isLightWeightVersion") : Language.get("isLightWeightVersion2")));

//        this.setUndecorated(true);
//        this.setOpacity(0.5f);
//        this.getContentPane().setBackground(Color.decode("#ffffff"));

        //数据显示表格
        jTable = new JTable(new DefaultTableModel(new Object[0][0], MainFrameCst.getTitles()));
        jTable.setRowHeight(30);
        jTable.setBackground(Color.decode(colorConfig.get("tableBg")));
        jTable.setFont(DefaultFont.WEI_RUAN_PLAIN_13.getFont());
        jTable.setForeground(Color.darkGray);
        jTable.setSelectionBackground(Color.decode(colorConfig.get("tableSelectBg")));
        jTable.setSelectionForeground(Color.decode(colorConfig.get("tableSelectFore")));
        jTable.setGridColor(Color.decode(colorConfig.get("tableGrid")));

        JTableHeader tableHeader = jTable.getTableHeader();
        tableHeader.setBorder(null);
        tableHeader.setBackground(Color.WHITE);
        tableHeader.setFont(DefaultFont.WEI_RUAN_PLAIN_13.getFont());
        JScrollPane jScrollPane = new JScrollPane(jTable, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        jScrollPane.setBounds(42, 151, 1100, 556);
        jScrollPane.getVerticalScrollBar().setUI(new ScrollBarUi());
        jScrollPane.getViewport().setBackground(Color.decode(colorConfig.get("scrollPaneBg")));
        jScrollPane.setBorder(BorderFactory.createLineBorder(Color.decode(colorConfig.get("scrollPaneBor"))));
        JLayeredPane mainLayeredPane = new JLayeredPane();
        mainLayeredPane.setSize(1200, 800);
        mainLayeredPane.add(jScrollPane, 9);


        //右键菜单
        JMenuItem updateMenuItem = new JMenuItem(Language.get("rightBtnMenuUpdate"));
        updateMenuItem.setActionCommand("更新");
        updateMenuItem.setFont(DefaultFont.WEI_RUAN_PLAIN_13.getFont());
        updateMenuItem.addActionListener(e -> {
            LinkedList<String> list = new AccountService().getRowValues(jTable);
            //如果是刚新增的id肯定不是数值
            if (!String.valueOf(list.get(0)).matches(Constant.IS_NUMBER)) {
                JOptionPane.showMessageDialog(null, Language.get("decodeThenSearchTipMsg"),
                        Language.get("decodeThenSearchTipTit"), JOptionPane.WARNING_MESSAGE);
            } else {
                if (switchRecord) {
                    //先解密
                    AccountService.decodeList(list);
                }
                list.add(Language.get("purposeUpdate"));
                new AddFrame(list, this).setVisible(true);
            }
            ActiveTimeService.activeTimeFresh();
        });

        JMenuItem deleteMenuItem = new JMenuItem(Language.get("deleteBtn"));
        deleteMenuItem.setActionCommand("删除");
        deleteMenuItem.setFont(DefaultFont.WEI_RUAN_PLAIN_13.getFont());
        deleteMenuItem.addActionListener(e -> {
            LinkedList<String> list = new AccountService().getRowValues(jTable);
            if (!String.valueOf(list.get(0)).matches(Constant.IS_NUMBER)) {
                ShowMessage.showWarningMessage(Language.get("decodeThenSearchTipMsg"), Language.get("decodeThenSearchTipTit"));
            } else {
                if (switchRecord) {
                    //先解密
                    AccountService.decodeList(list);
                }
                list.add(Language.get("purposeDelete"));
                new AddFrame(list, this).setVisible(true);
            }
            ActiveTimeService.activeTimeFresh();
        });

        //右键点击复制快捷账户密码信息
        JMenuItem quickCopyItem = new JMenuItem(Language.get("copyAccountBtn"));
        quickCopyItem.setFont(DefaultFont.WEI_RUAN_PLAIN_13.getFont());
        quickCopyItem.addActionListener(e -> {
            LinkedList<String> rowValues = new AccountService().getRowValues(jTable);
            //如果处于加密状态就解密
            if (switchRecord) {
                AccountService.decodeList(rowValues);
            }
            String generateStr = AccountService.generateAccountMsg(rowValues);
            stickAndShowCopySuccessMsg(generateStr, Language.get("copyAccountSuccessive"));
            ActiveTimeService.activeTimeFresh();
        });

        //复制功能
        JMenuItem copyFunctionItem = new JMenuItem(Language.get("copyBtn"));
        copyFunctionItem.setFont(DefaultFont.WEI_RUAN_PLAIN_13.getFont());
        copyFunctionItem.addActionListener(e -> {
            stickAndShowCopySuccessMsg(String.valueOf(jTable.getValueAt(jTable.getSelectedRow(), jTable.getSelectedColumn())), null);
            ActiveTimeService.activeTimeFresh();
        });

        //单个账户解码
        JMenuItem singleAcDecode = SingleDecodingModule.getInstance();

        //右键菜单统一添加和设置样式
        List<JMenuItem> styleList = Arrays.asList(copyFunctionItem, updateMenuItem, deleteMenuItem, quickCopyItem, singleAcDecode);
        for (JMenuItem jMenuItem : styleList) {
            jMenuItem.setPreferredSize(new Dimension(120, 30));
            MenuBlinkBackHelper.addBlinkBackground(jMenuItem, Color.decode(colorConfig.get("menuItemBlinkBgIn")),
                    Color.decode(colorConfig.get("menuItemBlinkBgOut")));
            BlinkBorderHelper.addBorder(jMenuItem, BorderFactory.createLineBorder(Color.decode(colorConfig.get("menuItemBlinkBorIn"))),
                    BorderFactory.createLineBorder(Color.decode(colorConfig.get("menuItemBlinkBorOut"))));
            jPopupMenu.add(jMenuItem);
        }

        BlinkBorderHelper.addBorder(jPopupMenu, BorderFactory.createLineBorder(Color.YELLOW),
                BorderFactory.createLineBorder(Color.white));
        jTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                //鼠标左键
                if (e.getButton() == MouseEvent.BUTTON1 && jTable.getSelectedRow() != -1) {
                    log.info("监测到鼠标左键");
                    //复制当前单元格
                    stickAndShowCopySuccessMsg(jTable.getValueAt(jTable.getSelectedRow(), jTable.getSelectedColumn()).toString(), null);
                }
                //鼠标右键
                if (e.getButton() == MouseEvent.BUTTON3 && jTable.getSelectedRow() != -1) {
                    log.info("监测到鼠标右键");
                    //右键时检测搜索按钮状态传入以显示不同文本
                    SingleDecodingModule.checkAndChange(switchRecord, jTable);
                    jPopupMenu.show(e.getComponent(), e.getX(), e.getY());
                }
            }
        });

        //搜索
        searchTip = new JLabel(Language.get("searchAny"));
        searchTip.setBounds(100, 71, 200, 40);
        searchTip.setForeground(Color.decode(colorConfig.get("searchTipFore")));
        searchTip.setFont(new Font("微软雅黑", Font.PLAIN, 16));
        this.add(searchTip);

        //搜索文本
        searchText = new JTextField("");
        searchText.setBounds(280, 76, 380, 30);
        searchText.setFont(DefaultFont.WEI_RUAN_PLAIN_15.getFont());
        searchText.setForeground(Color.decode(colorConfig.get("searchText")));
        //添加闪烁边框
        BlinkBorderHelper.addBorder(searchText, BorderFactory.createLineBorder(Color.decode(colorConfig.get("searchTextBlinkBorIn"))),
                BorderFactory.createLineBorder(Color.decode(colorConfig.get("searchTextBlinkBorOut"))));
        searchText.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                //如果按下回车就触发搜索事件
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    log.info("回车键搜索");
                    if (switchRecord) {
                        //解密
                        AccountService.setTableMessages(AccountService.getTableData());
                    } else {
                        //查询
                        if (Constant.IS_LIGHT) {
                            new LightService().searchAndSetTableMsg();
                        } else {
                            AccountService.setTableMessages();
                        }
                    }
                    //切换按钮状态
                    AccountService.toggleStatus(null);
                    //Double Enter模式，启动定时器，两秒后还原按钮状态
                    if (!realTimeSearchBtn.isSelected()) {
                        new Timer().schedule(new TimerTask() {
                            @Override
                            public void run() {
                                AccountService.toggleStatus(false);
                            }
                        }, Constant.DOUBLE_ENTER_DELAY);
                    }
                    ActiveTimeService.activeTimeFresh();
                }
            }
        });
        //点击事件：点击输入框清空内容
        searchText.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {

            }

            @Override
            public void mousePressed(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON3) {
                    log.info("鼠标右击，清空搜索框");
                    searchText.setText(null);
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {
                log.info("鼠标移入输入框");
                searchTip.setText(Language.get("searchTextMouseEntered"));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                log.info("鼠标移出输入框");
                searchTip.setText(Language.get("searchTextMouseExited"));
            }
        });
        this.add(searchText);

        searchButton = new JButton(SEARCH_BTN_TXT1);
        searchButton.setBounds(720, 76, 100, 30);
        searchButton.setForeground(Color.decode(colorConfig.get("searchBtnFore")));
        searchButton.setBackground(Color.decode(colorConfig.get("searchBtnBg")));
        searchButton.setFont(DefaultFont.WEI_RUAN_PLAIN_15.getFont());
        searchButton.addActionListener(e -> {
            if (switchRecord) {
                //解密
                AccountService.setTableMessages(AccountService.getTableData());
            } else {
                //查询
                if (Constant.IS_LIGHT) {
                    new LightService().searchAndSetTableMsg();
                } else {
                    AccountService.setTableMessages();
                }
                outputArea.outputMessage(Language.get("searchSuccessful"));
            }
            AccountService.toggleStatus(null);
            //清空单控解码模块集合
            SingleDecodingModule.clearList();
            ActiveTimeService.activeTimeFresh();
        });
        BlinkBorderHelper.addBorder(searchButton, BorderFactory.createLineBorder(Color.decode(
                colorConfig.get("searchBtnBlinkBorIn")), 2), null);

        //值改变事件
        searchText.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                ExecutorService threadPool = new ThreadPoolExecutor(2, 5,
                        1L, TimeUnit.SECONDS,
                        new LinkedBlockingQueue<>(3),
                        Executors.defaultThreadFactory(),
                        new ThreadPoolExecutor.AbortPolicy());
                threadPool.execute(() -> {
                    if (realTimeSearchBtn.isSelected()) {
                        if (StrUtil.isEmpty(searchText.getText())) {
                            jTable.setModel(new DefaultTableModel(new Object[0][0], MainFrameCst.getTitles()));
                            //查询
                        } else if (Constant.IS_LIGHT) {
                            new LightService().searchAndSetTableMsg();
                        } else {
                            AccountService.setTableMessages();
                        }
                    }
                });
                ActiveTimeService.activeTimeFresh();
                AccountService.toggleStatus(true);
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                ExecutorService threadPool = new ThreadPoolExecutor(2, 5,
                        1L, TimeUnit.SECONDS,
                        new LinkedBlockingQueue<>(3),
                        Executors.defaultThreadFactory(),
                        new ThreadPoolExecutor.AbortPolicy());
                threadPool.execute(() -> {
                    if (realTimeSearchBtn.isSelected()) {
                        if (StrUtil.isEmpty(searchText.getText())) {
                            jTable.setModel(new DefaultTableModel(new Object[0][0], MainFrameCst.getTitles()));
                            //查询
                        } else if (Constant.IS_LIGHT) {
                            new LightService().searchAndSetTableMsg();
                        } else {
                            AccountService.setTableMessages();
                        }
                    }
                });
                ActiveTimeService.activeTimeFresh();
                AccountService.toggleStatus(true);
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                ExecutorService threadPool = new ThreadPoolExecutor(2, 5,
                        1L, TimeUnit.SECONDS,
                        new LinkedBlockingQueue<>(3),
                        Executors.defaultThreadFactory(),
                        new ThreadPoolExecutor.AbortPolicy());
                threadPool.execute(() -> {
                    if (realTimeSearchBtn.isSelected()) {
                        if (StrUtil.isEmpty(searchText.getText())) {
                            jTable.setModel(new DefaultTableModel(new Object[0][0], MainFrameCst.getTitles()));
                            //查询
                        } else if (Constant.IS_LIGHT) {
                            new LightService().searchAndSetTableMsg();
                        } else {
                            AccountService.setTableMessages();
                        }
                    }
                });
                ActiveTimeService.activeTimeFresh();
                AccountService.toggleStatus(true);
            }
        });
        this.add(searchButton);

        //添加按钮
        JButton addButton = new JButton(Language.get("addBtn"));
        addButton.setBounds(845, 76, 100, 30);
        addButton.setForeground(Color.decode(colorConfig.get("addBtnFore")));
        addButton.setBackground(Color.decode(colorConfig.get("addBtnBg")));
        addButton.setFont(DefaultFont.WEI_RUAN_PLAIN_15.getFont());
        addButton.addActionListener(e -> {
            //添加按钮点击事件
            LinkedList<String> list = new LinkedList<>();
            int columnCount = jTable.getColumnCount();
            int maxCol = columnCount == 0 ? 5 : columnCount;
            for (int i = 0; i < maxCol; i++) {
                list.add("");
            }
            list.add(Language.get("purposeAdd"));
            new AddFrame(list, this).setVisible(true);
            this.setVisible(false);
            if (!activistLockBtn.isSelected()) {
                ActiveTimeService.activeTimeLock();
            }
        });
        BlinkBorderHelper.addBorder(addButton, BorderFactory.createLineBorder(Color.decode(
                colorConfig.get("addBtnBlinkBorIn")), 2), null);
        this.add(addButton);

        //显示结果状态文本（有多少条数据）
        resultNumbers = new JLabel(AccountService.getLatestAccountNumberText());
        resultNumbers.setBounds(42, 749, 200, 30);
        resultNumbers.setForeground(Color.decode(colorConfig.get("resultNumbersFore")));
        resultNumbers.setFont(new Font("微软雅黑", Font.PLAIN, 16));
        this.add(resultNumbers);

        //复制消息提醒
        copyAlertLabel = new JLabel();
        copyAlertLabel.setBounds(200, 751, 300, 30);
        copyAlertLabel.setForeground(Color.decode(colorConfig.get("copyAlertLabelFore")));
//        copyAlertLabel.setFont(new Font("微软雅黑", Font.BOLD, 13));
        copyAlertLabel.setFont(DefaultFont.WEI_RUAN_PLAIN_15.getFont());
        copyAlertLabel.setVisible(false);
        this.add(copyAlertLabel);

        //清空按钮
        JButton clearBtn = new JButton(Language.get("clearBtn"));
        clearBtn.setBounds(970, 76, 100, 30);
        clearBtn.setForeground(Color.decode(colorConfig.get("clearBtnFore")));
        clearBtn.setBackground(Color.decode(colorConfig.get("clearBtnBg")));
        clearBtn.setFont(DefaultFont.WEI_RUAN_PLAIN_15.getFont());
        clearBtn.addActionListener(e -> {
            searchText.setText(null);
            jTable.setModel(new DefaultTableModel(new Object[0][0], MainFrameCst.getTitles()));
            resultNumbers.setText(AccountService.getLatestAccountNumberText());
            AccountService.toggleStatus(false);
            outputArea.clearMessage();
            ActiveTimeService.activeTimeFresh();
        });
        BlinkBorderHelper.addBorder(clearBtn, BorderFactory.createLineBorder(
                Color.decode(colorConfig.get("clearBtnBorderIn")), 2), null);
        this.add(clearBtn);

        realTimeSearchBtn = new JToggleButton(Language.get("moderBtn"), true);
        realTimeSearchBtn.setBounds(1177, 166, 100, 30);
        new AccessAnimationService(realTimeSearchBtn).bind(80, 1, AccessAnimationService.Direction.Left);
        realTimeSearchBtn.setFocusPainted(false);
        BlinkBorderHelper.addBorder(realTimeSearchBtn, BorderFactory.createLineBorder(Color.decode(
                colorConfig.get("realTimeBtnBorderIn")), 2), null);
        realTimeSearchBtn.setForeground(Color.decode(colorConfig.get("realTimeBtnFore")));
        realTimeSearchBtn.setBorder(null);
        realTimeSearchBtn.setBackground(Color.decode(colorConfig.get("realTimeBtnBg")));
        realTimeSearchBtn.setUI(new MetalToggleButtonUI() {
            @Override
            protected Color getSelectColor() {
                return Color.decode(colorConfig.get("realTimeBtnSelect"));
            }
        });
        realTimeSearchBtn.setFont(DefaultFont.WEI_RUAN_PLAIN_15.getFont());
        realTimeSearchBtn.setHorizontalAlignment(JLabel.CENTER);
        realTimeSearchBtn.addActionListener(e -> {
            if (!realTimeSearchBtn.isSelected()) {
                realTimeSearchBtn.setText(Language.get("moderBtn2"));
                ShowMessage.showInformationMessage(Language.get("moderBtn2TipMsg1") + Constant.DOUBLE_ENTER_DELAY
                        + Language.get("moderBtn2TipMsg2"), Language.get("moderBtn2TipTit"));
            } else {
                realTimeSearchBtn.setText(Language.get("moderBtn"));
                ShowMessage.showInformationMessage(Language.get("moderBtnTipMsg"), Language.get("moderBtnTipTit"));
            }
            ActiveTimeService.activeTimeFresh();
        });
        this.add(realTimeSearchBtn);

        //切换场景
        JButton switchSceneBtn = new JButton(Language.get("switchBtn"));
        switchSceneBtn.setBounds(1177, 131, 100, 30);
        new AccessAnimationService(switchSceneBtn).bind(80, 1, AccessAnimationService.Direction.Left);
        switchSceneBtn.setFocusPainted(false);
        BlinkBorderHelper.addBorder(switchSceneBtn, BorderFactory.createLineBorder(Color.decode(
                colorConfig.get("switchBtnBorderIn")), 2), null);
        switchSceneBtn.setForeground(Color.decode(colorConfig.get("switchBtnFore")));
        switchSceneBtn.setBackground(Color.decode(colorConfig.get("switchBtnBg")));
        switchSceneBtn.setFont(DefaultFont.WEI_RUAN_PLAIN_15.getFont());
        switchSceneBtn.setHorizontalAlignment(JLabel.CENTER);
        switchSceneBtn.addActionListener(e -> {
            this.dispose();
            new LockFrame().setVisible(true);
        });
        this.add(switchSceneBtn);

        //查看日志按钮
        JButton lookLogBtn = new JButton(Language.get("lookLogBtn"));
        lookLogBtn.setBounds(1177, 96, 100, 30);
        new AccessAnimationService(lookLogBtn).bind(80, 1, AccessAnimationService.Direction.Left);
        lookLogBtn.setFocusPainted(false);
        BlinkBorderHelper.addBorder(lookLogBtn, BorderFactory.createLineBorder(Color.decode(
                colorConfig.get("logBtnBorderIn")), 2), null);
        lookLogBtn.setForeground(Color.decode(colorConfig.get("logBtnFore")));
        lookLogBtn.setBackground(Color.decode(colorConfig.get("logBtnBg")));
        lookLogBtn.setFont(DefaultFont.WEI_RUAN_PLAIN_15.getFont());
        lookLogBtn.setHorizontalAlignment(JLabel.CENTER);
        lookLogBtn.addActionListener(e -> new LogService().showLog());
        this.add(lookLogBtn);

        //查看功能按钮
        JButton lookFunBtn = new JButton(Language.get("mainLookFunBtn"));
        lookFunBtn.setBounds(1177, 61, 100, 30);
        new AccessAnimationService(lookFunBtn).bind(80, 1, AccessAnimationService.Direction.Left);
        lookFunBtn.setFocusPainted(false);
        BlinkBorderHelper.addBorder(lookFunBtn, BorderFactory.createLineBorder(Color.decode(
                colorConfig.get("funcBtnBorderIn")), 2), null);
        lookFunBtn.setForeground(Color.decode(colorConfig.get("funcBtnFore")));
        lookFunBtn.setBackground(Color.decode(colorConfig.get("funcBtnBg")));
        lookFunBtn.setFont(DefaultFont.WEI_RUAN_PLAIN_15.getFont());
        lookFunBtn.setHorizontalAlignment(JLabel.CENTER);
        lookFunBtn.addActionListener(e -> new LookFucService().openFile());
        this.add(lookFunBtn);

        //免责声明
//        disclaimerLabel = new JLabel(Language.get("disclaimer"));
//        disclaimerLabel.setBounds(1103, 749, 800, 30);
//        new AccessAnimationService(disclaimerLabel).bind(650, 1, AccessAnimationService.Direction.Left);
//        disclaimerLabel.setForeground(Color.decode(colorConfig.get("disclaimLabel")));
//        disclaimerLabel.setFont(new Font("微软雅黑", Font.ITALIC, 16));
//        this.add(disclaimerLabel);

        //关于
        aboutBtn = new JButton(Language.get("aboutBtn"));
        aboutBtn.setBounds(1177, 26, 80, 30);
        aboutBtn.setForeground(Color.decode(colorConfig.get("aboutBtnFore")));
        aboutBtn.setBackground(Color.decode(colorConfig.get("aboutBtnBg")));
        aboutBtn.setFont(DefaultFont.WEI_RUAN_PLAIN_15.getFont());
        aboutBtn.setHorizontalAlignment(JLabel.CENTER);
        aboutBtn.addActionListener(e -> {
            TextTools.stick(Language.get("repositoryAddress"));
            new AboutFrame().setVisible(true);
            ActiveTimeService.activeTimeFresh();
        });
        BlinkBorderHelper.addBorder(aboutBtn, BorderFactory.createLineBorder(Color.decode(
                colorConfig.get("aboutBtnBlinkBorIn")), 2), null);
        new AccessAnimationService(aboutBtn).bind(50, 1, AccessAnimationService.Direction.Left);
        this.add(aboutBtn);

        //导出数据按钮
        exportBtn = new JButton(Language.get("exportBtn"));
        exportBtn.setBounds(1177, 306, 100, 30);
        exportBtn.setForeground(Color.decode(colorConfig.get("exportBtnFore")));
        exportBtn.setBackground(Color.decode(colorConfig.get("exportBtnBg")));
        exportBtn.setFont(DefaultFont.WEI_RUAN_BOLD_13.getFont());
        exportBtn.setHorizontalAlignment(JLabel.CENTER);
        new AccessAnimationService(exportBtn).bind(80, 1, AccessAnimationService.Direction.Left);
        exportBtn.addActionListener(e -> {
            new ExportImportDataFrame(ExportImportCst.EXPORT).setVisible(true);
            ActiveTimeService.activeTimeFresh();
        });
        BlinkBorderHelper.addBorder(exportBtn, BorderFactory.createLineBorder(Color.decode(
                colorConfig.get("exportBtnBlinkBorIn")), 2), null);
        this.add(exportBtn);

        //导入
        importBtn = new JButton(Language.get("importBtn"));
        importBtn.setBounds(1177, 271, 100, 30);
        importBtn.setForeground(Color.decode(colorConfig.get("importBtnFore")));
        importBtn.setBackground(Color.decode(colorConfig.get("importBtnBg")));
        importBtn.setFont(DefaultFont.WEI_RUAN_BOLD_13.getFont());
        importBtn.setHorizontalAlignment(JLabel.CENTER);
        new AccessAnimationService(importBtn).bind(80, 1, AccessAnimationService.Direction.Left);
        importBtn.addActionListener(e -> {
            new ExportImportDataFrame(ExportImportCst.IMPORT).setVisible(true);
            ActiveTimeService.activeTimeFresh();
        });
        BlinkBorderHelper.addBorder(importBtn, BorderFactory.createLineBorder(Color.decode(
                colorConfig.get("importBtnBlinkBorIn")), 2), null);
        this.add(importBtn);

        //到达活性时间后加密table
        activistTimeLabel.setBounds(65, 26, 200, 30);
        activistTimeLabel.setForeground(Color.decode(colorConfig.get("activistTimeLabel")));
        activistTimeLabel.setFont(new Font("微软雅黑", Font.PLAIN, 16));
        this.add(activistTimeLabel);

        //激活活性时间定时器
        ActiveTimeService.activeTimeStart();

        //活性时间锁
        activistLockBtn = new JToggleButton(Language.get("activistBtn"));
        activistLockBtn.setBounds(-8, 26, 60, 30);
        activistLockBtn.setFont(DefaultFont.WEI_RUAN_PLAIN_15.getFont());
        activistLockBtn.setForeground(Color.decode(colorConfig.get("activeBtnFore")));
        activistLockBtn.setBorder(null);
        activistLockBtn.setBackground(Color.decode(colorConfig.get("activeBtnBg")));
        activistLockBtn.setHorizontalAlignment(JLabel.CENTER);
        activistLockBtn.setUI(new MetalToggleButtonUI() {
            @Override
            protected Color getSelectColor() {
                return Color.decode(colorConfig.get("activeBtnBgSelect"));
            }
        });
        activistLockBtn.addActionListener(e -> {
            if (activistLockBtn.isSelected()) {
                activistLockBtn.setText(Language.get("activistBtn2"));
                activistLockBtn.setForeground(Color.decode(colorConfig.get("activeBtnSelect")));
            } else {
                activistLockBtn.setText(Language.get("activistBtn"));
                activistLockBtn.setForeground(Color.decode(colorConfig.get("activeBtnFore")));
            }
            ActiveTimeService.activeTimeLock();
        });
        BlinkBorderHelper.addBorder(activistLockBtn, BorderFactory.createLineBorder(Color.decode(
                colorConfig.get("activeBtnBlinkBorIn")), 2), null);
        this.add(activistLockBtn);

        //前期版本兼容性导入数据
        //3.0
        JButton importBtn3 = new JButton("PM3.0");
        importBtn3.setBounds(1177, 201, 100, 30);
        importBtn3.setForeground(Color.decode(colorConfig.get("importBtn3Fore")));
        importBtn3.setBackground(Color.decode(colorConfig.get("importBtn3Bg")));
        importBtn3.setFont(DefaultFont.WEI_RUAN_BOLD_13.getFont());
        importBtn3.setHorizontalAlignment(JLabel.CENTER);
        importBtn3.setBorder(null);
        importBtn3.addActionListener(e -> {
            ShowMessage.showInformationMessage("该功能为v3.0准备，如导入非v3.0的数据，可能会导致数据错误等意外情况。", "导入v3.0的数据");
            new Import3DataFrame().setVisible(true);
            ActiveTimeService.activeTimeFresh();
        });
        importBtn3.setFocusPainted(false);
        new AccessAnimationService(importBtn3).bind(80, 1, AccessAnimationService.Direction.Left);
        importBtn3.setEnabled(!Constant.IS_LIGHT);
        this.add(importBtn3);

        //4.0
        JButton importBtn4 = new JButton("PM4.2");
        importBtn4.setBounds(1177, 236, 100, 30);
        importBtn4.setFocusPainted(false);
        new AccessAnimationService(importBtn4).bind(80, 1, AccessAnimationService.Direction.Left);
        importBtn4.setForeground(Color.decode(colorConfig.get("importBtn4Fore")));
        importBtn4.setBackground(Color.decode(colorConfig.get("importBtn4Bg")));
        importBtn4.setFont(DefaultFont.WEI_RUAN_BOLD_13.getFont());
        importBtn4.setBorder(null);
        importBtn4.setHorizontalAlignment(JLabel.CENTER);
        importBtn4.addActionListener(e -> {
            ShowMessage.showInformationMessage("该功能为v4.2准备，如导入非v4.2的数据，可能会导致数据错误等意外情况。", "导入v4.2的数据");
            new Import4DataFrame().setVisible(true);
            ActiveTimeService.activeTimeFresh();
        });
        importBtn4.setEnabled(!Constant.IS_LIGHT);
        this.add(importBtn4);

        //输出框
        outputArea = new QueueTextArea();
        outputArea.setBounds(42, 708, 1100, 44);
        outputArea.setFont(DefaultFont.WEI_RUAN_PLAIN_15.getFont());
        outputArea.setEditable(false);
        outputArea.setBackground(Color.decode(colorConfig.get("outputAreaBg")));
        outputArea.setForeground(Color.decode(colorConfig.get("outputAreaFore2")));
        outputArea.setBorder(BorderFactory.createLineBorder(Color.decode(colorConfig.get("outputAreaBor"))));
        this.add(outputArea);

        this.add(mainLayeredPane);
        mainFrame = this;
    }

    /**
     * 控制对象唯一,ShowStickMsg该类对象必须只有一个，否则多个对象同时操控copyAlertLabel会造成冲突
     *
     * @Author Crane Resigned
     * @Date 2023-10-05 14:49:59
     */
    @Deprecated
    private final ShowStickMsg showStickMsg = new ShowStickMsg();

    public void stickAndShowCopySuccessMsg(String value, String specialMsg) {
        outputArea.outputMessage(StrUtil.isEmpty(specialMsg) ? Language.get("copySuccessive").concat(value) : specialMsg);
        TextTools.stick(value);
    }

    /**
     * 显示复制成功的信息的内部类
     *
     * @Author Crane Resigned
     * @Date 2023-10-05 14:44:18
     */
    @Deprecated
    private class ShowStickMsg {

        /**
         * 标记线程数量
         *
         * @Author Crane Resigned
         * @Date 2023-10-05 14:53:57
         */
        private byte threadCount = 0;

        /**
         * 复制成功显示的方法
         * 触发复制成功时将信息显示出来，应该在2s后消失，若中途再次有复制应该刷新显示时间为2s
         * 所以最终该大次消失的时间点为最后一次点击+2s
         *
         * @Author Crane Resigned
         * @Date 2023-02-08 23:12:52
         */
        public void stickAndShowCopySuccessMsg(String value, String specialMsg) {
            threadCount++;
            TextTools.stick(value);

            //只有首次设置以减少消耗
            if (threadCount == 1) {
                copyAlertLabel.setVisible(true);
            }
            //创建定时器在n秒后判断是否消失，定时器执行代表该次点击线程结束，线程数--，然后判断线程数是否为0，为0消失
            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    threadCount--;
                    copyAlertLabel.setVisible(threadCount != 0);
                }
            }, 2000);
        }
    }

}
