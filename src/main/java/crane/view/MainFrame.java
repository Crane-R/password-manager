package crane.view;

import crane.constant.Constant;
import crane.constant.DefaultFont;
import crane.constant.ExportImportCst;
import crane.constant.MainFrameCst;
import crane.function.Tools;
import crane.model.service.AccountService;
import crane.model.service.FrameService;
import crane.model.service.ShowMessgae;
import lombok.extern.slf4j.Slf4j;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
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
    private final JRadioButton realTimeSearchBtn;

    public final static String SEARCH_BTN_TXT1 = "锁 · 手动搜索";
    public final static String SEARCH_BTN_TXT2 = "解码";

    /**
     * 搜索按钮
     * Author: Crane Resigned
     * Date: 2022-11-27 15:22:53
     */
    public static JButton searchButton = new JButton(SEARCH_BTN_TXT1);

    /**
     * 活性时间
     * Author: Crane Resigned
     * Date: 2023-01-22 18:30:55
     */
    public static JLabel activistTimeLabel = new JLabel(String.valueOf(Constant.ACTIVE_TIME));

    public MainFrame() {
        this.setTitle(MainFrameCst.MAIN_TITLE);
        this.setSize(1200, 800);
        this.setLayout(null);
        this.setResizable(false);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //设置标题栏的图标
        this.setIconImage(FrameService.getTitleImage());

        //数据显示表格
        jTable = new JTable(new DefaultTableModel(new Object[0][0], MainFrameCst.TITLES));
        jTable.setRowHeight(30);
        JScrollPane jScrollPane = new JScrollPane(jTable, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        jScrollPane.setBounds(42, 125, 1075, 600);
        this.add(jScrollPane);

        //右键菜单
        JMenuItem updateMenuItem = new JMenuItem("修改 · Modify");
        updateMenuItem.setActionCommand("更新");
        updateMenuItem.setFont(DefaultFont.DEFAULT_FONT_ONE.getFont());
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
        deleteMenuItem.setFont(DefaultFont.DEFAULT_FONT_ONE.getFont());
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
        quickCopyItem.setFont(DefaultFont.DEFAULT_FONT_ONE.getFont());
        quickCopyItem.addActionListener(e -> {
            LinkedList<String> rowValues = new AccountService().getRowValues(jTable);
            //如果处于加密状态就解密
            if (switchRecord) {
                AccountService.decodeList(rowValues);
            }
            String generateStr = AccountService.generateAccountMsg(rowValues);
            Tools.stick(generateStr);
            FrameService.activeTimeFresh();
        });

        //复制功能
        JMenuItem copyFunctionItem = new JMenuItem("复制 · Copy");
        copyFunctionItem.setFont(DefaultFont.DEFAULT_FONT_ONE.getFont());
        copyFunctionItem.addActionListener(e -> {
            Tools.stick(String.valueOf(jTable.getValueAt(jTable.getSelectedRow(), jTable.getSelectedColumn())));
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
        searchText.setFont(DefaultFont.DEFAULT_FONT_ONE.getFont());
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
                        AccountService.setTableMessages();
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
        this.add(searchText);

        searchButton.setBounds(720, 50, 100, 30);
        searchButton.setFocusPainted(false);
        searchButton.setForeground(Color.WHITE);
        searchButton.setBackground(Color.decode("#4FA485"));
        searchButton.setFont(DefaultFont.DEFAULT_FONT_ONE.getFont());
        searchButton.setBorder(null);
        searchButton.addActionListener(e -> {
            if (switchRecord) {
                //解密
                AccountService.setTableMessages(AccountService.getTableData());
            } else {
                //查询
                AccountService.setTableMessages();
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
                        AccountService.setTableMessages();
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
                        AccountService.setTableMessages();
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
                        AccountService.setTableMessages();
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
        addButton.setFocusPainted(false);
        addButton.setForeground(Color.WHITE);
        addButton.setBackground(Color.decode("#4792B9"));
        addButton.setFont(DefaultFont.DEFAULT_FONT_ONE.getFont());
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
        resultNumbers.setBounds(42, 725, 600, 30);
        resultNumbers.setForeground(Color.decode("#80001E"));
        resultNumbers.setFont(new Font(null, Font.PLAIN, 13));
        this.add(resultNumbers);

        //清空按钮
        JButton clearBtn = new JButton("清空当前");
        clearBtn.setBounds(970, 50, 100, 30);
        clearBtn.setFocusPainted(false);
        clearBtn.setForeground(Color.WHITE);
        clearBtn.setBackground(Color.decode("#1A5599"));
        clearBtn.setFont(DefaultFont.DEFAULT_FONT_ONE.getFont());
        clearBtn.setBorder(null);
        clearBtn.addActionListener(e -> {
            searchText.setText(null);
            jTable.setModel(new DefaultTableModel(new Object[0][0], MainFrameCst.TITLES));
            resultNumbers.setText(AccountService.getLatestAccountNumberText());
            AccountService.toggleStatus(false);
            FrameService.activeTimeFresh();
        });
        this.add(clearBtn);

        realTimeSearchBtn = new JRadioButton("Real Time", true);
        realTimeSearchBtn.setBounds(1044, 0, 140, 30);
        realTimeSearchBtn.setFocusPainted(false);
        realTimeSearchBtn.setForeground(Color.WHITE);
        realTimeSearchBtn.setBackground(Color.decode("#7D2720"));
        realTimeSearchBtn.setFont(new Font("微软雅黑", Font.BOLD, 13));
        realTimeSearchBtn.setBorder(null);
        realTimeSearchBtn.setHorizontalAlignment(JLabel.CENTER);
        realTimeSearchBtn.addActionListener(e -> {
            if (!realTimeSearchBtn.isSelected()) {
                realTimeSearchBtn.setBackground(Color.decode("#2A3050"));
                realTimeSearchBtn.setText("Double Enter");
                ShowMessgae.showInformationMessage("单击回车搜索，双击回车搜索并解码，双击判断间隔为" + Constant.DOUBLE_ENTER_DELAY + "ms", "双击回车模式");
            } else {
                realTimeSearchBtn.setBackground(Color.decode("#7D2720"));
                realTimeSearchBtn.setText("Real Time");
                ShowMessgae.showInformationMessage("根据文本框值的增减、改变进行实时搜索", "实时搜索模式");
            }
            FrameService.activeTimeFresh();
        });
        this.add(realTimeSearchBtn);

        //免责声明
        JLabel disclaimerLabel = new JLabel("安全性声明：此程序不保证绝对安全，如您不是开发者请斟酌使用，造成的任何损失开发者不会负责。");
        disclaimerLabel.setBounds(540, 725, 800, 30);
        disclaimerLabel.setForeground(Color.RED);
        disclaimerLabel.setFont(new Font("微软雅黑", Font.BOLD, 13));
        this.add(disclaimerLabel);

        //导出数据按钮
        JButton exportBtn = new JButton("导出");
        exportBtn.setBounds(1125, 32, 60, 30);
        exportBtn.setFocusPainted(false);
        exportBtn.setForeground(Color.WHITE);
        exportBtn.setBackground(Color.decode("#046D35"));
        exportBtn.setFont(DefaultFont.DEFAULT_FONT_ONE.setStyle(Font.BOLD).getFont());
        exportBtn.setBorder(null);
        exportBtn.setHorizontalAlignment(JLabel.CENTER);
        exportBtn.addActionListener(e -> {
            new ExportImportDataFrame(ExportImportCst.EXPORT).setVisible(true);
            FrameService.activeTimeFresh();
        });
        this.add(exportBtn);

        //导入
        JButton importBtn = new JButton("导入");
        importBtn.setBounds(1125, 64, 60, 30);
        importBtn.setFocusPainted(false);
        importBtn.setForeground(Color.WHITE);
        importBtn.setBackground(Color.decode("#5898C2"));
        importBtn.setFont(DefaultFont.DEFAULT_FONT_ONE.setStyle(Font.BOLD).getFont());
        importBtn.setBorder(null);
        importBtn.setHorizontalAlignment(JLabel.CENTER);
        importBtn.addActionListener(e -> {
            new ExportImportDataFrame(ExportImportCst.IMPORT).setVisible(true);
            FrameService.activeTimeFresh();
        });
        this.add(importBtn);

        //到达活性时间后加密table
        activistTimeLabel.setBounds(10, 0, 200, 30);
        activistTimeLabel.setForeground(Color.decode("#1A5599"));
        activistTimeLabel.setFont(DefaultFont.DEFAULT_FONT_ONE.setStyle(Font.BOLD).getFont());
        this.add(activistTimeLabel);

        //激活活性时间定时器
        FrameService.activeTimeFresh();

        //前期版本兼容性导入数据
        //3.0
        JButton importBtn3 = new JButton("PM3.0");
        importBtn3.setBounds(1125, 106, 60, 30);
        importBtn3.setFocusPainted(false);
        importBtn3.setForeground(Color.WHITE);
        importBtn3.setBackground(Color.decode("#002FA7"));
        importBtn3.setFont(DefaultFont.DEFAULT_FONT_ONE.setStyle(Font.BOLD).getFont());
        importBtn3.setBorder(null);
        importBtn3.setHorizontalAlignment(JLabel.CENTER);
        importBtn3.addActionListener(e -> {
            new Import3DataFrame().setVisible(true);
            FrameService.activeTimeFresh();
        });
        this.add(importBtn3);

        //4.0
        JButton importBtn4 = new JButton("PM4.2");
        importBtn4.setBounds(1125, 138, 60, 30);
        importBtn4.setFocusPainted(false);
        importBtn4.setForeground(Color.WHITE);
        importBtn4.setBackground(Color.decode("#4FA485"));
        importBtn4.setFont(DefaultFont.DEFAULT_FONT_ONE.setStyle(Font.BOLD).getFont());
        importBtn4.setBorder(null);
        importBtn4.setHorizontalAlignment(JLabel.CENTER);
        importBtn4.addActionListener(e -> {
            new Import4DataFrame().setVisible(true);
            FrameService.activeTimeFresh();
        });
        this.add(importBtn4);
    }

}
