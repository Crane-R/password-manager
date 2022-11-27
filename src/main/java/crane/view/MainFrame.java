package crane.view;

import cn.hutool.core.util.StrUtil;
import crane.constant.Constant;
import crane.model.service.AccountService;
import crane.model.service.FrameService;
import lombok.extern.slf4j.Slf4j;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.LinkedList;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;

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

    public static void setSearchText(JTextField searchText) {
        MainFrame.searchText = searchText;
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
     * Author: Crane Resigned
     * Date: 2022-11-27 02:03:43
     */
    private boolean switchRecord = false;

    /**
     * 实时搜索开关
     * Author: Crane Resigned
     * Date: 2022-11-26 23:50:37
     */
    private final JRadioButton realTimeSearchBtn;

    private final String SEARCH_BTN_TXT1 = "锁 · 手动搜索";
    private final String SEARCH_BTN_TXT2 = "立即解密";

    /**
     * 搜索按钮
     * Author: Crane Resigned
     * Date: 2022-11-27 15:22:53
     */
    private final JButton searchButton = new JButton(SEARCH_BTN_TXT1);

    public MainFrame() {
        this.setTitle(Constant.MAIN_TITLE);
        this.setSize(1200, 800);
        this.setLayout(null);
        this.setResizable(false);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //设置标题栏的图标
        this.setIconImage(FrameService.getTitleImage());

        //数据显示表格
        jTable = new JTable(new DefaultTableModel(new Object[0][0], Constant.TITLES));
        jTable.setRowHeight(30);
        JScrollPane jScrollPane = new JScrollPane(jTable, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        jScrollPane.setBounds(42, 125, 1100, 600);
        this.add(jScrollPane);

        //右键菜单
        JMenuItem updateMenuItem = new JMenuItem("更新 · Update");
        updateMenuItem.setActionCommand("更新");
        updateMenuItem.addActionListener(e -> {
            LinkedList<String> list = new AccountService().getRowValues(jTable);
            if (StrUtil.equals(searchButton.getText(), SEARCH_BTN_TXT2)) {
                //先解密
                list.set(3, AccountService.decodeBase64Salt(list.get(3)));
                list.set(2, AccountService.decodeBase64Salt(list.get(2)));
            }
            list.add("更新");
            new AddFrame(list).setVisible(true);
        });

        JMenuItem deleteMenuItem = new JMenuItem("删除 · Delete");
        deleteMenuItem.setActionCommand("删除");
        deleteMenuItem.addActionListener(e -> {
            LinkedList<String> list = new AccountService().getRowValues(jTable);
            if (StrUtil.equals(searchButton.getText(), SEARCH_BTN_TXT2)) {
                //先解密
                list.set(3, AccountService.decodeBase64Salt(list.get(3)));
                list.set(2, AccountService.decodeBase64Salt(list.get(2)));
            }
            list.add("删除");
            new AddFrame(list).setVisible(true);
        });

        jPopupMenu.add(updateMenuItem);
        jPopupMenu.add(deleteMenuItem);
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
        searchText.setFont(new Font("微软雅黑", Font.PLAIN, 13));
        searchText.setForeground(Color.decode("#1A5599"));
        searchText.setBorder(BorderFactory.createLineBorder(Color.decode("#B8CE8E")));
        searchText.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                //如果按下回车就触发搜索事件
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    log.info("回车键搜索");
                    AccountService.setTableMessages();
                }
            }
        });
        this.add(searchText);

        searchButton.setBounds(750, 50, 100, 30);
        searchButton.setFocusPainted(false);
        searchButton.setForeground(Color.WHITE);
        searchButton.setBackground(Color.decode("#4FA485"));
        searchButton.setFont(new Font("微软雅黑", Font.PLAIN, 13));
        searchButton.setBorder(null);
        searchButton.addActionListener(e -> {
            if (!switchRecord) {
                log.info(SEARCH_BTN_TXT1);
                //搜索按钮事件
                AccountService.setTableMessages();
                switchRecord = true;
                searchButton.setText(SEARCH_BTN_TXT2);
            } else {
                log.info(SEARCH_BTN_TXT2);
                //解密
                AccountService.setTableMessages(AccountService.getTableData());
                switchRecord = false;
                searchButton.setText(SEARCH_BTN_TXT1);
            }

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
            }
        });
        this.add(searchButton);

        //添加按钮
        JButton addButton = new JButton("新增账户");
        addButton.setBounds(875, 50, 100, 30);
        addButton.setFocusPainted(false);
        addButton.setForeground(Color.WHITE);
        addButton.setBackground(Color.decode("#4792B9"));
        addButton.setFont(new Font("微软雅黑", Font.PLAIN, 13));
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
            new AddFrame(list).setVisible(true);
        });
        this.add(addButton);

        //显示结果状态文本（有多少条数据）
        resultNumbers = new JLabel(AccountService.getLatestAccountNumberText());
        resultNumbers.setBounds(42, 725, 600, 30);
        resultNumbers.setForeground(Color.decode("#80001E"));
        this.add(resultNumbers);

        //清空按钮
        JButton clearBtn = new JButton("清空当前");
        clearBtn.setBounds(1000, 50, 100, 30);
        clearBtn.setFocusPainted(false);
        clearBtn.setForeground(Color.WHITE);
        clearBtn.setBackground(Color.decode("#1A5599"));
        clearBtn.setFont(new Font("微软雅黑", Font.PLAIN, 13));
        clearBtn.setBorder(null);
        clearBtn.addActionListener(e -> {
            searchText.setText(null);
            jTable.setModel(new DefaultTableModel(new Object[0][0], Constant.TITLES));
        });
        this.add(clearBtn);

        realTimeSearchBtn = new JRadioButton("实时", true);
        realTimeSearchBtn.setBounds(1123, 0, 60, 30);
        realTimeSearchBtn.setFocusPainted(false);
        realTimeSearchBtn.setForeground(Color.WHITE);
        realTimeSearchBtn.setBackground(Color.decode("#7D2720"));
        realTimeSearchBtn.setFont(new Font("微软雅黑", Font.BOLD, 13));
        realTimeSearchBtn.setBorder(null);
        realTimeSearchBtn.setHorizontalAlignment(JLabel.CENTER);
        realTimeSearchBtn.addActionListener(e -> {
            if (!realTimeSearchBtn.isSelected()) {
                realTimeSearchBtn.setBackground(Color.decode("#2A3050"));
            } else {
                realTimeSearchBtn.setBackground(Color.decode("#7D2720"));
            }
        });
        this.add(realTimeSearchBtn);

    }

}
