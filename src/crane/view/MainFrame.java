package crane.view;

import crane.model.service.AccountService;
import crane.model.service.FrameService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.LinkedList;

/**
 * @author Crane Resigned
 */
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

    public static void setResultNumbers(JLabel resultNumbers) {
        MainFrame.resultNumbers = resultNumbers;
    }

    /**
     * 定义视图表标题
     *
     * @Author Crane Resigned
     * @Date 2022-06-11 22:55:44
     */
    public static final Object[] TITLES = {"数据库ID(●'◡'●)", "账户╰(*°▽°*)╯", "用户名φ(*￣0￣)", "密码(┬┬﹏┬┬)", "其他(❁´◡`❁)"};

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

    public MainFrame() {
        this.setTitle("Password Manager v3.0  阿星本地内测版");
        this.setSize(1200, 800);
        this.setLayout(null);
        this.setResizable(false);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //设置标题栏的图标
        Image image = FrameService.getTitleImage();
        this.setIconImage(image);

        //数据显示表格
        jTable = new JTable(new AccountService().selectData(""), TITLES);
        jTable.setRowHeight(30);
        JScrollPane jScrollPane = new JScrollPane(jTable, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        jScrollPane.setBounds(42, 125, 1100, 600);
        this.add(jScrollPane);

        //右键菜单
        JMenuItem updateJMenuItem = new JMenuItem("修改");
        updateJMenuItem.setActionCommand("修改");
        updateJMenuItem.addActionListener(e -> {
            LinkedList<String> list = new AccountService().getRowValues(jTable);
            list.add("修改");
            new AddFrame(list).setVisible(true);
        });

        JMenuItem deleteJMenuItem = new JMenuItem("删除");
        deleteJMenuItem.setActionCommand("删除");
        deleteJMenuItem.addActionListener(e -> {
            LinkedList<String> list = new AccountService().getRowValues(jTable);
            list.add("删除");
            new AddFrame(list).setVisible(true);
        });

        jPopupMenu.add(updateJMenuItem);
        jPopupMenu.add(deleteJMenuItem);
        jTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                if (e.getButton() == MouseEvent.BUTTON3 && jTable.getSelectedRow() != -1) {
                    System.out.println("监测到鼠标右键");
                    jPopupMenu.show(e.getComponent(), e.getX(), e.getY());
                }
            }
        });

        //搜索
        JLabel searchTip = new JLabel("输入任意信息搜索账户");
        searchTip.setBounds(180, 45, 200, 40);
        searchTip.setForeground(Color.decode("#002FA7"));
        searchTip.setFont(new Font("微软雅黑", Font.BOLD, 14));
        this.add(searchTip);

        //搜索文本
        searchText = new JTextField("");
        searchText.setBounds(390, 50, 300, 30);
        searchText.setFont(new Font("微软雅黑", Font.PLAIN, 13));
        searchText.setForeground(Color.decode("#002FA7"));
        searchText.setBorder(BorderFactory.createLineBorder(Color.decode("#002FA7")));
        this.add(searchText);

        JButton searchButton = new JButton("搜索账户");
        searchButton.setBounds(770, 50, 100, 30);
        searchButton.setFocusPainted(false);
        searchButton.setForeground(Color.WHITE);
        searchButton.setBackground(Color.decode("#002FA7"));
        searchButton.setFont(new Font("微软雅黑", Font.PLAIN, 13));
        searchButton.addActionListener(e -> {
            //搜索按钮事件
            MainFrame.jTable.setModel(new DefaultTableModel(new AccountService().selectData(searchText.getText()), TITLES));
        });
        //因为键盘事件必须处于聚焦时才生效，所以绑定输入框
        searchText.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                System.out.println("键盘事件检索错误");
                if (KeyEvent.VK_ENTER == e.getKeyCode()) {
                    System.out.println("搜索按钮键盘事件触发，检索成功");
                    MainFrame.jTable.setModel(new DefaultTableModel(new AccountService().selectData(searchText.getText()), TITLES));
                }
            }
        });
        this.add(searchButton);

        //添加按钮
        JButton addButton = new JButton("添加新账户");
        addButton.setBounds(900, 50, 100, 30);
        addButton.setFocusPainted(false);
        addButton.setForeground(Color.WHITE);
        addButton.setBackground(Color.decode("#002FA7"));
        addButton.setFont(new Font("微软雅黑", Font.PLAIN, 13));
        addButton.addActionListener(e -> {
            //添加按钮点击事件
            LinkedList<String> list = new LinkedList<>();
            int maxCol = jTable.getColumnCount();
            for (int i = 0; i < maxCol; i++) {
                list.add("");
            }
            list.add("添加");
            new AddFrame(list).setVisible(true);
        });
        this.add(addButton);

        //显示结果状态文本（有多少条数据）
        //显示文本交给定时器处理
        resultNumbers = new JLabel();
        resultNumbers.setBounds(42, 725, 100, 30);
        resultNumbers.setForeground(Color.decode("#002FA7"));
        this.add(resultNumbers);

        //启动定时器
        new AccountService().checkRowCount();
    }

}
