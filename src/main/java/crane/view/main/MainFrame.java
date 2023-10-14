package crane.view.main;

import cn.hutool.core.util.StrUtil;
import crane.constant.Constant;
import crane.constant.DefaultFont;
import crane.constant.ExportImportCst;
import crane.constant.MainFrameCst;
import crane.function.FileTool;
import crane.function.Language;
import crane.function.TextTools;
import crane.model.jdbc.JdbcConnection;
import crane.model.service.AccountService;
import crane.model.service.FrameService;
import crane.model.service.ShowMessgae;
import crane.model.service.lightweight.LightService;
import crane.view.*;
import crane.view.module.ScrollBarUi;
import crane.view.module.stylehelper.BlinkBorderHelper;
import crane.view.module.stylehelper.MenuBlinkBackHelper;
import lombok.extern.slf4j.Slf4j;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.plaf.metal.MetalToggleButtonUI;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
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
    protected static JTextField searchText;

    public static JTextField getSearchText() {
        return searchText;
    }

    /**
     * 数据表条数
     *
     * @Author Crane Resigned
     * @Date 2022-06-11 23:49:12
     */
    protected static JLabel resultNumbers;

    public static JLabel getResultNumbers() {
        return resultNumbers;
    }

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
    protected final JLabel copyAlertLabel;

    /**
     * 载存主窗体对象
     *
     * @Author Crane Resigned
     * @Date 2023-05-24 17:16:36
     */
    public static MainFrame mainFrame;

    protected JButton aboutBtn;

    protected JToggleButton activistLockBtn;

    protected JLabel searchTip;

    protected JButton exportBtn;

    protected JButton importBtn;

    protected JLabel disclaimerLabel;

    public MainFrame() {
        this.setTitle((JdbcConnection.IS_TEST ? MainFrameCst.TEST_TITLE : MainFrameCst.MAIN_TITLE) + " >> "
                + (Constant.IS_LIGHT ? Language.get("isLightWeightVersion") : Language.get("isLightWeightVersion2")));
        this.setSize(1200, 800);
        this.setLayout(null);
//        this.setResizable(false);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //设置标题栏的图标
        this.setIconImage(FrameService.getTitleImage());
        this.getContentPane().setBackground(Color.decode("#DAE4E6"));

//        this.setUndecorated(true);
//        this.setOpacity(0.5f);

//        this.getContentPane().setBackground(Color.decode("#ffffff"));

        //数据显示表格
        jTable = new JTable(new DefaultTableModel(new Object[0][0], MainFrameCst.getTitles()));
        jTable.setRowHeight(30);
        jTable.setBackground(Color.decode("#EEF5FF"));
        jTable.setFont(DefaultFont.WEI_RUAN_PLAIN_13.getFont());
        jTable.setForeground(Color.darkGray);
        jTable.setSelectionBackground(Color.decode("#FDF7F6"));
        jTable.setSelectionForeground(Color.decode("#382C78"));
        jTable.setGridColor(Color.decode("#BACCDA"));

        JTableHeader tableHeader = jTable.getTableHeader();
        tableHeader.setBorder(null);
        tableHeader.setBackground(Color.WHITE);
        tableHeader.setFont(DefaultFont.WEI_RUAN_PLAIN_13.getFont());
        JScrollPane jScrollPane = new JScrollPane(jTable, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        jScrollPane.setBounds(42, 125, 1100, 600);
        jScrollPane.getVerticalScrollBar().setUI(new ScrollBarUi());
        jScrollPane.getViewport().setBackground(Color.decode("#DAE4E6"));
        this.add(jScrollPane);

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
            FrameService.activeTimeFresh();
        });

        JMenuItem deleteMenuItem = new JMenuItem(Language.get("deleteBtn"));
        deleteMenuItem.setActionCommand("删除");
        deleteMenuItem.setFont(DefaultFont.WEI_RUAN_PLAIN_13.getFont());
        deleteMenuItem.addActionListener(e -> {
            LinkedList<String> list = new AccountService().getRowValues(jTable);
            if (!String.valueOf(list.get(0)).matches(Constant.IS_NUMBER)) {
                JOptionPane.showMessageDialog(null, Language.get("decodeThenSearchTipMsg"),
                        Language.get("decodeThenSearchTipTit"), JOptionPane.WARNING_MESSAGE);
            } else {
                if (switchRecord) {
                    //先解密
                    AccountService.decodeList(list);
                }
                list.add(Language.get("purposeDelete"));
                new AddFrame(list, this).setVisible(true);
            }
            FrameService.activeTimeFresh();
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
            FrameService.activeTimeFresh();
        });

        //复制功能
        JMenuItem copyFunctionItem = new JMenuItem(Language.get("copyBtn"));
        copyFunctionItem.setFont(DefaultFont.WEI_RUAN_PLAIN_13.getFont());
        copyFunctionItem.addActionListener(e -> {
            stickAndShowCopySuccessMsg(String.valueOf(jTable.getValueAt(jTable.getSelectedRow(), jTable.getSelectedColumn())), null);
            FrameService.activeTimeFresh();
        });

        //单个账户解码
        JMenuItem singleAcDecode = SingleDecodingModule.getInstance();

        //右键菜单统一添加和设置样式
        List<JMenuItem> styleList = Arrays.asList(copyFunctionItem, updateMenuItem, deleteMenuItem, quickCopyItem, singleAcDecode);
        for (JMenuItem jMenuItem : styleList) {
            jMenuItem.setPreferredSize(new Dimension(120, 30));
            MenuBlinkBackHelper.addBlinkBackground(jMenuItem, Color.decode("#F4CE69"), Color.decode("#F4F3EC"));
            BlinkBorderHelper.addBorder(jMenuItem, BorderFactory.createLineBorder(Color.YELLOW),
                    BorderFactory.createLineBorder(Color.WHITE));
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
        searchTip.setBounds(100, 45, 200, 40);
        searchTip.setForeground(Color.decode("#407E54"));
        searchTip.setFont(new Font("微软雅黑", Font.BOLD, 14));
        this.add(searchTip);

        //搜索文本
        searchText = new JTextField("");
        searchText.setBounds(280, 50, 380, 30);
        searchText.setFont(DefaultFont.WEI_RUAN_PLAIN_13.getFont());
        searchText.setForeground(Color.decode("#1A5599"));
        //添加闪烁边框
        BlinkBorderHelper.addBorder(searchText, BorderFactory.createLineBorder(Color.YELLOW),
                BorderFactory.createLineBorder(Color.decode("#B8CE8E")));
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
                    FrameService.activeTimeFresh();
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
        searchButton.setBounds(720, 50, 100, 30);
        searchButton.setForeground(Color.WHITE);
        searchButton.setBackground(Color.decode("#4FA485"));
        searchButton.setFont(DefaultFont.WEI_RUAN_PLAIN_13.getFont());
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
            }
            AccountService.toggleStatus(null);
            //清空单控解码模块集合
            SingleDecodingModule.clearList();
            FrameService.activeTimeFresh();
        });
        BlinkBorderHelper.addBorder(searchButton, BorderFactory.createLineBorder(Color.WHITE, 2), null);

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
                FrameService.activeTimeFresh();
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
                FrameService.activeTimeFresh();
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
                FrameService.activeTimeFresh();
                AccountService.toggleStatus(true);
            }
        });
        this.add(searchButton);

        //添加按钮
        JButton addButton = new JButton(Language.get("addBtn"));
        addButton.setBounds(845, 50, 100, 30);
        addButton.setForeground(Color.WHITE);
        addButton.setBackground(Color.decode("#4792B9"));
        addButton.setFont(DefaultFont.WEI_RUAN_PLAIN_13.getFont());
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
            FrameService.activeTimeFresh();
        });
        BlinkBorderHelper.addBorder(addButton, BorderFactory.createLineBorder(Color.WHITE, 2), null);
        this.add(addButton);

        //显示结果状态文本（有多少条数据）
        resultNumbers = new JLabel(AccountService.getLatestAccountNumberText());
        resultNumbers.setBounds(42, 725, 200, 30);
        resultNumbers.setForeground(Color.decode("#382C78"));
        resultNumbers.setFont(new Font("微软雅黑", Font.BOLD, 13));
        this.add(resultNumbers);

        //复制消息提醒
        copyAlertLabel = new JLabel();
        copyAlertLabel.setBounds(200, 725, 300, 30);
        copyAlertLabel.setForeground(Color.decode("#E41A16"));
//        copyAlertLabel.setFont(new Font("微软雅黑", Font.BOLD, 13));
        copyAlertLabel.setFont(DefaultFont.WEI_RUAN_PLAIN_13.getFont());
        copyAlertLabel.setVisible(false);
        this.add(copyAlertLabel);

        //清空按钮
        JButton clearBtn = new JButton(Language.get("clearBtn"));
        clearBtn.setBounds(970, 50, 100, 30);
        clearBtn.setForeground(Color.WHITE);
        clearBtn.setBackground(Color.decode("#1A5599"));
        clearBtn.setFont(DefaultFont.WEI_RUAN_PLAIN_13.getFont());
        clearBtn.addActionListener(e -> {
            searchText.setText(null);
            jTable.setModel(new DefaultTableModel(new Object[0][0], MainFrameCst.getTitles()));
            resultNumbers.setText(AccountService.getLatestAccountNumberText());
            AccountService.toggleStatus(false);
            FrameService.activeTimeFresh();
        });
        BlinkBorderHelper.addBorder(clearBtn, BorderFactory.createLineBorder(Color.WHITE, 2), null);
        this.add(clearBtn);

        realTimeSearchBtn = new JToggleButton(Language.get("moderBtn"), true);
        realTimeSearchBtn.setBounds(1095, 0, 100, 30);
        realTimeSearchBtn.setForeground(Color.WHITE);
        realTimeSearchBtn.setBorder(null);
        realTimeSearchBtn.setBackground(Color.decode("#2A3050"));
        realTimeSearchBtn.setUI(new MetalToggleButtonUI() {
            @Override
            protected Color getSelectColor() {
                return Color.decode("#7D2720");
            }
        });
        realTimeSearchBtn.setFont(new Font("微软雅黑", Font.BOLD, 13));
        realTimeSearchBtn.setHorizontalAlignment(JLabel.CENTER);
        realTimeSearchBtn.addActionListener(e -> {
            if (!realTimeSearchBtn.isSelected()) {
                realTimeSearchBtn.setText(Language.get("moderBtn2"));
                ShowMessgae.showInformationMessage(Language.get("moderBtn2TipMsg1") + Constant.DOUBLE_ENTER_DELAY
                        + Language.get("moderBtn2TipMsg2"), Language.get("moderBtn2TipTit"));
            } else {
                realTimeSearchBtn.setText(Language.get("moderBtn"));
                ShowMessgae.showInformationMessage(Language.get("moderBtnTipMsg"), Language.get("moderBtnTipTit"));
            }
            FrameService.activeTimeFresh();
        });
        this.add(realTimeSearchBtn);

        //切换场景
        JButton switchSceneBtn = new JButton(Language.get("switchBtn"));
        switchSceneBtn.setBounds(990, 0, 100, 30);
        switchSceneBtn.setForeground(Color.WHITE);
        switchSceneBtn.setBackground(Color.BLACK);
        switchSceneBtn.setFont(new Font("微软雅黑", Font.BOLD, 13));
        switchSceneBtn.setHorizontalAlignment(JLabel.CENTER);
        switchSceneBtn.addActionListener(e -> {
            this.dispose();
            new LockFrame().setVisible(true);
        });
        this.add(switchSceneBtn);

        //查看日志按钮
        JButton lookLogBtn = new JButton(Language.get("lookLogBtn"));
        lookLogBtn.setBounds(885, 0, 100, 30);
        lookLogBtn.setForeground(Color.BLACK);
        lookLogBtn.setBackground(Color.WHITE);
        lookLogBtn.setFont(new Font("微软雅黑", Font.BOLD, 13));
        lookLogBtn.setHorizontalAlignment(JLabel.CENTER);
        lookLogBtn.addActionListener(e -> {
            File logFile = new File("log.log");
            if (logFile.exists()) {
                FileTool.openFile(logFile.getPath());
            } else {
                ShowMessgae.showErrorMessage(Language.get("notLookFile"), Language.get("notLookFileTit"));
            }
        });
        this.add(lookLogBtn);

        //查看功能按钮
        JButton lookFunBtn = new JButton(Language.get("lookFunBtn"));
        lookFunBtn.setBounds(780, 0, 100, 30);
        lookFunBtn.setForeground(Color.BLACK);
        lookFunBtn.setBackground(Color.WHITE);
        lookFunBtn.setFont(new Font("微软雅黑", Font.BOLD, 13));
        lookFunBtn.setHorizontalAlignment(JLabel.CENTER);
        lookFunBtn.addActionListener(e -> {
            File logFile = new File("function.html");
            if (logFile.exists()) {
                FileTool.openFile(logFile.getPath());
            } else {
                ShowMessgae.showErrorMessage(Language.get("notFunFile"), Language.get("notFunFileTit"));
            }
        });
        this.add(lookFunBtn);

        //免责声明
        disclaimerLabel = new JLabel(Language.get("disclaimer"));
        disclaimerLabel.setBounds(540, 725, 800, 30);
        disclaimerLabel.setForeground(Color.decode("#9A2734"));
        disclaimerLabel.setFont(new Font("微软雅黑", Font.BOLD, 13));
        this.add(disclaimerLabel);

        //关于
        aboutBtn = new JButton(Language.get("aboutBtn"));
        aboutBtn.setBounds(-8, 0, 60, 30);
        aboutBtn.setForeground(Color.WHITE);
        aboutBtn.setBackground(Color.decode("#58C3C3"));
        aboutBtn.setFont(DefaultFont.WEI_RUAN_BOLD_13.getFont());
        aboutBtn.setHorizontalAlignment(JLabel.CENTER);
        aboutBtn.addActionListener(e -> {
            new AboutFrame().setVisible(true);
            FrameService.activeTimeFresh();
        });
        BlinkBorderHelper.addBorder(aboutBtn, BorderFactory.createLineBorder(Color.WHITE, 2), null);
        this.add(aboutBtn);

        //导出数据按钮
        exportBtn = new JButton(Language.get("exportBtn"));
        exportBtn.setBounds(1138, 32, 60, 30);
        exportBtn.setForeground(Color.WHITE);
        exportBtn.setBackground(Color.decode("#046D35"));
        exportBtn.setFont(DefaultFont.WEI_RUAN_BOLD_13.getFont());
        exportBtn.setHorizontalAlignment(JLabel.CENTER);
        exportBtn.addActionListener(e -> {
            new ExportImportDataFrame(ExportImportCst.EXPORT).setVisible(true);
            FrameService.activeTimeFresh();
        });
        BlinkBorderHelper.addBorder(exportBtn, BorderFactory.createLineBorder(Color.WHITE, 2), null);
        this.add(exportBtn);

        //导入
        importBtn = new JButton(Language.get("importBtn"));
        importBtn.setBounds(1138, 64, 60, 30);
        importBtn.setForeground(Color.WHITE);
        importBtn.setBackground(Color.decode("#5898C2"));
        importBtn.setFont(DefaultFont.WEI_RUAN_BOLD_13.getFont());
        importBtn.setHorizontalAlignment(JLabel.CENTER);
        importBtn.addActionListener(e -> {
            new ExportImportDataFrame(ExportImportCst.IMPORT).setVisible(true);
            FrameService.activeTimeFresh();
        });
        BlinkBorderHelper.addBorder(importBtn, BorderFactory.createLineBorder(Color.WHITE, 2), null);
        this.add(importBtn);

        //到达活性时间后加密table
        activistTimeLabel.setBounds(65, 0, 200, 30);
        activistTimeLabel.setForeground(Color.decode("#1A5599"));
        activistTimeLabel.setFont(DefaultFont.WEI_RUAN_BOLD_13.getFont());
        this.add(activistTimeLabel);

        //激活活性时间定时器
        FrameService.activeTimeFresh();

        //活性时间锁
        activistLockBtn = new JToggleButton(Language.get("activistBtn"));
        activistLockBtn.setBounds(220, 0, 60, 30);
        activistLockBtn.setFont(DefaultFont.WEI_RUAN_BOLD_13.getFont());
        activistLockBtn.setForeground(Color.WHITE);
        activistLockBtn.setBorder(null);
        activistLockBtn.setBackground(Color.decode("#EB89A6"));
        activistLockBtn.setHorizontalAlignment(JLabel.CENTER);
        activistLockBtn.addActionListener(e -> {
            if (activistLockBtn.isSelected()) {
                activistLockBtn.setText(Language.get("activistBtn2"));
                activistLockBtn.setForeground(Color.BLACK);
            } else {
                activistLockBtn.setText(Language.get("activistBtn"));
                activistLockBtn.setForeground(Color.WHITE);
            }
            FrameService.activeTimeLock();
        });
        BlinkBorderHelper.addBorder(activistLockBtn, BorderFactory.createLineBorder(Color.WHITE, 2), null);
        this.add(activistLockBtn);

        //前期版本兼容性导入数据
        //3.0
        JButton importBtn3 = new JButton("PM3.0");
        importBtn3.setBounds(-8, 32, 60, 30);
        importBtn3.setForeground(Color.WHITE);
        importBtn3.setBackground(Color.decode("#002FA7"));
        importBtn3.setFont(DefaultFont.WEI_RUAN_BOLD_13.getFont());
        importBtn3.setHorizontalAlignment(JLabel.CENTER);
        importBtn3.setBorder(null);
        importBtn3.addActionListener(e -> {
            ShowMessgae.showInformationMessage("该功能为v3.0准备，如导入非v3.0的数据，可能会导致数据错误等意外情况。", "导入v3.0的数据");
            new Import3DataFrame().setVisible(true);
            FrameService.activeTimeFresh();
        });
        importBtn3.setEnabled(!Constant.IS_LIGHT);
        this.add(importBtn3);

        //4.0
        JButton importBtn4 = new JButton("PM4.2");
        importBtn4.setBounds(-8, 64, 60, 30);
        importBtn4.setForeground(Color.WHITE);
        importBtn4.setBackground(Color.decode("#4FA485"));
        importBtn4.setFont(DefaultFont.WEI_RUAN_BOLD_13.getFont());
        importBtn4.setBorder(null);
        importBtn4.setHorizontalAlignment(JLabel.CENTER);
        importBtn4.addActionListener(e -> {
            ShowMessgae.showInformationMessage("该功能为v4.2准备，如导入非v4.2的数据，可能会导致数据错误等意外情况。", "导入v4.2的数据");
            new Import4DataFrame().setVisible(true);
            FrameService.activeTimeFresh();
        });
        importBtn4.setEnabled(!Constant.IS_LIGHT);
        this.add(importBtn4);

        mainFrame = this;
    }

    /**
     * 控制对象唯一,ShowStickMsg该类对象必须只有一个，否则多个对象同时操控copyAlertLabel会造成冲突
     *
     * @Author Crane Resigned
     * @Date 2023-10-05 14:49:59
     */
    private final ShowStickMsg showStickMsg = new ShowStickMsg();

    public void stickAndShowCopySuccessMsg(String value, String specialMsg) {
        showStickMsg.stickAndShowCopySuccessMsg(value, specialMsg);
    }

    /**
     * 显示复制成功的信息的内部类
     *
     * @Author Crane Resigned
     * @Date 2023-10-05 14:44:18
     */
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
            copyAlertLabel.setText(StrUtil.isEmpty(specialMsg) ? Language.get("copySuccessive").concat(value) : specialMsg);
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
