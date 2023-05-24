package crane.view;

import cn.hutool.core.util.StrUtil;
import crane.constant.Constant;
import crane.constant.DefaultFont;
import crane.constant.ExportImportCst;
import crane.constant.MainFrameCst;
import crane.function.Tools;
import crane.model.jdbc.JdbcConnection;
import crane.model.service.AccountService;
import crane.model.service.FrameService;
import crane.model.service.ShowMessgae;
import crane.model.service.lightweight.LightService;
import crane.view.module.ScrollBarUi;
import lombok.extern.slf4j.Slf4j;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.plaf.metal.MetalToggleButtonUI;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.*;
import java.util.LinkedList;
import java.util.Timer;
import java.util.TimerTask;
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
    private static JTextField searchText;

    public static JTextField getSearchText() {
        return searchText;
    }

    /**
     * 数据表条数
     *
     * @Author Crane Resigned
     * @Date 2022-06-11 23:49:12
     */
    private static JLabel resultNumbers;

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
    private final JPopupMenu jPopupMenu = new JPopupMenu();

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
    private final JToggleButton realTimeSearchBtn;

    public final static String SEARCH_BTN_TXT1 = "锁 | 手动搜索";
    public final static String SEARCH_BTN_TXT2 = "解码";

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
    private final JLabel copyAlertLabel;

    /**
     * 载存主窗体对象
     *
     * @Author Crane Resigned
     * @Date 2023-05-24 17:16:36
     */
    public static MainFrame mainFrame;

    public MainFrame() {
        this.setTitle(JdbcConnection.IS_TEST ? MainFrameCst.TEST_TITLE : MainFrameCst.MAIN_TITLE);
        this.setSize(1200, 800);
        this.setLayout(null);
        this.setResizable(false);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //设置标题栏的图标
        this.setIconImage(FrameService.getTitleImage());
        this.getContentPane().setBackground(Color.decode("#DAE4E6"));
//        this.getContentPane().setBackground(Color.decode("#ffffff"));

        //数据显示表格
        jTable = new JTable(new DefaultTableModel(new Object[0][0], MainFrameCst.TITLES));
        jTable.setRowHeight(30);
        jTable.setBackground(Color.decode("#EEF5FF"));
        jTable.setFont(DefaultFont.WEI_RUAN_PLAIN_13.getFont());
        jTable.setForeground(Color.darkGray);
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
        JMenuItem updateMenuItem = new JMenuItem("修改 · Modify");
        updateMenuItem.setActionCommand("更新");
        updateMenuItem.setFont(DefaultFont.WEI_RUAN_PLAIN_13.getFont());
        updateMenuItem.addActionListener(e -> {
            LinkedList<String> list = new AccountService().getRowValues(jTable);
            //如果是刚新增的id肯定不是数值
            if (!String.valueOf(list.get(0)).matches(Constant.IS_NUMBER)) {
                JOptionPane.showMessageDialog(null, "您需要先解密再搜索一次哦", "无法获取唯一标识", JOptionPane.WARNING_MESSAGE);
            } else {
                if (switchRecord) {
                    //先解密
                    AccountService.decodeList(list);
                }
                list.add("更新");
                new AddFrame(list, this).setVisible(true);
            }
            FrameService.activeTimeFresh();
        });

        JMenuItem deleteMenuItem = new JMenuItem("删除 · Delete");
        deleteMenuItem.setActionCommand("删除");
        deleteMenuItem.setFont(DefaultFont.WEI_RUAN_PLAIN_13.getFont());
        deleteMenuItem.addActionListener(e -> {
            LinkedList<String> list = new AccountService().getRowValues(jTable);
            if (!String.valueOf(list.get(0)).matches(Constant.IS_NUMBER)) {
                JOptionPane.showMessageDialog(null, "您需要先解密再搜索一次哦", "无法获取唯一标识", JOptionPane.WARNING_MESSAGE);
            } else {
                if (switchRecord) {
                    //先解密
                    AccountService.decodeList(list);
                }
                list.add("删除");
                new AddFrame(list, this).setVisible(true);
            }
            FrameService.activeTimeFresh();
        });

        //右键点击复制快捷账户密码信息
        JMenuItem quickCopyItem = new JMenuItem("复制账户信息");
        quickCopyItem.setFont(DefaultFont.WEI_RUAN_PLAIN_13.getFont());
        quickCopyItem.addActionListener(e -> {
            LinkedList<String> rowValues = new AccountService().getRowValues(jTable);
            //如果处于加密状态就解密
            if (switchRecord) {
                AccountService.decodeList(rowValues);
            }
            String generateStr = AccountService.generateAccountMsg(rowValues);
            stickAndShowCopySuccessMsg(generateStr, "复制账号信息成功");
            FrameService.activeTimeFresh();
        });

        //复制功能
        JMenuItem copyFunctionItem = new JMenuItem("复制 · Copy");
        copyFunctionItem.setFont(DefaultFont.WEI_RUAN_PLAIN_13.getFont());
        copyFunctionItem.addActionListener(e -> {
            stickAndShowCopySuccessMsg(String.valueOf(jTable.getValueAt(jTable.getSelectedRow(), jTable.getSelectedColumn())), null);
            FrameService.activeTimeFresh();
        });

        jPopupMenu.add(copyFunctionItem);
        jPopupMenu.add(updateMenuItem);
        jPopupMenu.add(deleteMenuItem);
        jPopupMenu.add(quickCopyItem);
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
                    jPopupMenu.show(e.getComponent(), e.getX(), e.getY());
                }
            }
        });

        //搜索
        JLabel searchTip = new JLabel("输入任意信息搜索账户");
        searchTip.setBounds(100, 45, 200, 40);
        searchTip.setForeground(Color.decode("#407E54"));
        searchTip.setFont(new Font("微软雅黑", Font.BOLD, 14));
        this.add(searchTip);

        //搜索文本
        searchText = new JTextField("");
        searchText.setBounds(280, 50, 380, 30);
        searchText.setFont(DefaultFont.WEI_RUAN_PLAIN_13.getFont());
        searchText.setForeground(Color.decode("#1A5599"));
        searchText.setBorder(BorderFactory.createLineBorder(Color.decode("#B8CE8E")));
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
                searchTip.setText("鼠标右击可清空关键字");
            }

            @Override
            public void mouseExited(MouseEvent e) {
                log.info("鼠标移出输入框");
                searchTip.setText("输入任意信息搜索账户");
            }
        });
        this.add(searchText);

        searchButton = new JButton(SEARCH_BTN_TXT1);
        searchButton.setBounds(720, 50, 100, 30);
        searchButton.setForeground(Color.WHITE);
        searchButton.setBackground(Color.decode("#4FA485"));
        searchButton.setFont(DefaultFont.WEI_RUAN_PLAIN_13.getFont());
        searchButton.setBorder(null);
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
            FrameService.activeTimeFresh();
        });

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
                            jTable.setModel(new DefaultTableModel(new Object[0][0], MainFrameCst.TITLES));
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
                            jTable.setModel(new DefaultTableModel(new Object[0][0], MainFrameCst.TITLES));
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
                            jTable.setModel(new DefaultTableModel(new Object[0][0], MainFrameCst.TITLES));
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
        JButton addButton = new JButton("新增账户");
        addButton.setBounds(845, 50, 100, 30);
        addButton.setForeground(Color.WHITE);
        addButton.setBackground(Color.decode("#4792B9"));
        addButton.setFont(DefaultFont.WEI_RUAN_PLAIN_13.getFont());
        addButton.setBorder(null);
        addButton.addActionListener(e -> {
            //添加按钮点击事件
            LinkedList<String> list = new LinkedList<>();
            int columnCount = jTable.getColumnCount();
            int maxCol = columnCount == 0 ? 5 : columnCount;
            for (int i = 0; i < maxCol; i++) {
                list.add("");
            }
            list.add("新增");
            new AddFrame(list, this).setVisible(true);
            this.setVisible(false);
            FrameService.activeTimeFresh();
        });
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
        copyAlertLabel.setFont(new Font("微软雅黑", Font.BOLD, 13));
        copyAlertLabel.setVisible(false);
        this.add(copyAlertLabel);

        //清空按钮
        JButton clearBtn = new JButton("清空当前");
        clearBtn.setBounds(970, 50, 100, 30);
        clearBtn.setForeground(Color.WHITE);
        clearBtn.setBackground(Color.decode("#1A5599"));
        clearBtn.setFont(DefaultFont.WEI_RUAN_PLAIN_13.getFont());
        clearBtn.setBorder(null);
        clearBtn.addActionListener(e -> {
            searchText.setText(null);
            jTable.setModel(new DefaultTableModel(new Object[0][0], MainFrameCst.TITLES));
            resultNumbers.setText(AccountService.getLatestAccountNumberText());
            AccountService.toggleStatus(false);
            FrameService.activeTimeFresh();
        });
        this.add(clearBtn);

        realTimeSearchBtn = new JToggleButton("实时模式", true);
        realTimeSearchBtn.setBounds(1095, 0, 100, 30);
        realTimeSearchBtn.setForeground(Color.WHITE);
        realTimeSearchBtn.setBackground(Color.decode("#2A3050"));
        realTimeSearchBtn.setUI(new MetalToggleButtonUI() {
            @Override
            protected Color getSelectColor() {
                return Color.decode("#7D2720");
            }
        });
        realTimeSearchBtn.setFont(new Font("微软雅黑", Font.BOLD, 13));
        realTimeSearchBtn.setBorder(null);
        realTimeSearchBtn.setHorizontalAlignment(JLabel.CENTER);
        realTimeSearchBtn.addActionListener(e -> {
            if (!realTimeSearchBtn.isSelected()) {
                realTimeSearchBtn.setText("双击模式");
                ShowMessgae.showInformationMessage("单击回车搜索，双击回车搜索并解码，双击判断间隔为" + Constant.DOUBLE_ENTER_DELAY + "ms", "双击回车模式");
            } else {
                realTimeSearchBtn.setText("实时模式");
                ShowMessgae.showInformationMessage("根据文本框值的增减、改变进行实时搜索", "实时搜索模式");
            }
            FrameService.activeTimeFresh();
        });
        this.add(realTimeSearchBtn);

        //切换场景
        JButton switchSceneBtn = new JButton("切换场景");
        switchSceneBtn.setBounds(990, 0, 100, 30);
        switchSceneBtn.setForeground(Color.WHITE);
        switchSceneBtn.setBackground(Color.BLACK);
        switchSceneBtn.setFont(new Font("微软雅黑", Font.BOLD, 13));
        switchSceneBtn.setBorder(null);
        switchSceneBtn.setHorizontalAlignment(JLabel.CENTER);
        switchSceneBtn.addActionListener(e -> {
            this.dispose();
            new LockFrame().setVisible(true);
        });
        this.add(switchSceneBtn);

        //免责声明
        JLabel disclaimerLabel = new JLabel("安全性声明：此程序不保证绝对安全，若您不是开发者请斟酌使用，造成的任何损失开发者不会负责。");
        disclaimerLabel.setBounds(540, 725, 800, 30);
        disclaimerLabel.setForeground(Color.decode("#9A2734"));
        disclaimerLabel.setFont(new Font("微软雅黑", Font.BOLD, 13));
        this.add(disclaimerLabel);

        //关于
        JButton aboutBtn = new JButton("关于");
        aboutBtn.setBounds(-8, 0, 60, 30);
        aboutBtn.setForeground(Color.WHITE);
        aboutBtn.setBackground(Color.decode("#58C3C3"));
        aboutBtn.setFont(DefaultFont.WEI_RUAN_BOLD_13.getFont());
        aboutBtn.setBorder(null);
        aboutBtn.setHorizontalAlignment(JLabel.CENTER);
        aboutBtn.addActionListener(e -> {
            new AboutFrame().setVisible(true);
            FrameService.activeTimeFresh();
        });
        this.add(aboutBtn);

        //导出数据按钮
        JButton exportBtn = new JButton("导出");
        exportBtn.setBounds(1138, 32, 60, 30);
        exportBtn.setForeground(Color.WHITE);
        exportBtn.setBackground(Color.decode("#046D35"));
        exportBtn.setFont(DefaultFont.WEI_RUAN_BOLD_13.getFont());
        exportBtn.setBorder(null);
        exportBtn.setHorizontalAlignment(JLabel.CENTER);
        exportBtn.addActionListener(e -> {
            new ExportImportDataFrame(ExportImportCst.EXPORT).setVisible(true);
            FrameService.activeTimeFresh();
        });
        exportBtn.setEnabled(!Constant.IS_LIGHT);
        this.add(exportBtn);

        //导入
        JButton importBtn = new JButton("导入");
        importBtn.setBounds(1138, 64, 60, 30);
        importBtn.setForeground(Color.WHITE);
        importBtn.setBackground(Color.decode("#5898C2"));
        importBtn.setFont(DefaultFont.WEI_RUAN_BOLD_13.getFont());
        importBtn.setBorder(null);
        importBtn.setHorizontalAlignment(JLabel.CENTER);
        importBtn.addActionListener(e -> {
            new ExportImportDataFrame(ExportImportCst.IMPORT).setVisible(true);
            FrameService.activeTimeFresh();
        });
        importBtn.setEnabled(!Constant.IS_LIGHT);
        this.add(importBtn);

        //到达活性时间后加密table
        activistTimeLabel.setBounds(65, 0, 200, 30);
        activistTimeLabel.setForeground(Color.decode("#1A5599"));
        activistTimeLabel.setFont(DefaultFont.WEI_RUAN_BOLD_13.getFont());
        this.add(activistTimeLabel);

        //激活活性时间定时器
        FrameService.activeTimeFresh();

        //活性时间锁
        JToggleButton activistLockBtn = new JToggleButton("活性锁");
        activistLockBtn.setBounds(220, 0, 60, 30);
        activistLockBtn.setFont(DefaultFont.WEI_RUAN_BOLD_13.getFont());
        activistLockBtn.setForeground(Color.WHITE);
        activistLockBtn.setBackground(Color.decode("#EB89A6"));
        activistLockBtn.setBorder(null);
        activistLockBtn.setHorizontalAlignment(JLabel.CENTER);
        activistLockBtn.addActionListener(e -> {
            if (activistLockBtn.isSelected()) {
                FrameService.activeTimeStop();
                activistLockBtn.setText("已锁定");
                activistLockBtn.setForeground(Color.BLACK);
            } else {
                FrameService.activeTimeFresh();
                activistLockBtn.setText("活性锁");
                activistLockBtn.setForeground(Color.WHITE);
            }
        });
        this.add(activistLockBtn);

        //前期版本兼容性导入数据
        //3.0
        JButton importBtn3 = new JButton("PM3.0");
        importBtn3.setBounds(-8, 32, 60, 30);
        importBtn3.setForeground(Color.WHITE);
        importBtn3.setBackground(Color.decode("#002FA7"));
        importBtn3.setFont(DefaultFont.WEI_RUAN_BOLD_13.getFont());
        importBtn3.setBorder(null);
        importBtn3.setHorizontalAlignment(JLabel.CENTER);
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
     * 复制成功显示的方法
     *
     * @Author Crane Resigned
     * @Date 2023-02-08 23:12:52
     */
    public void stickAndShowCopySuccessMsg(String value, String specialMsg) {
        Tools.stick(value);
        copyAlertLabel.setText(StrUtil.isEmpty(specialMsg) ? "复制成功：".concat(value) : specialMsg);
        copyAlertLabel.setVisible(true);
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                copyAlertLabel.setVisible(false);
            }
        }, 1000);
    }

}
