package com.crane.view.frame.module;

import com.crane.constant.DefaultFont;
import com.crane.model.service.SecurityService;
import com.crane.view.config.Language;

import javax.swing.*;
import java.util.LinkedList;
import java.util.List;

/**
 * 单控解码模块，返回一个JMenuItem，
 * 用于主界面数据表的单个账户分控解码和加密
 * 应该是单例模式
 *
 * @author zhouxingxue
 * @date 2023/10/14 20:35:40
 */
public class SingleDecodingModule extends JMenuItem {
    private static SingleDecodingModule singleDecodingModule;

    private static final String SINGLE_AC_DECODE = Language.get("singleAcDecode");
    private static final String SINGLE_AC_ENCODE = Language.get("singleAcEncode");

    private static Boolean switchSearch;

    private static JTable jTable;

    /**
     * 操作数组
     *
     * @author zhouxingxue
     * @date 2023/10/14 20:47:50
     */
    private static final List<Integer> operationList = new LinkedList<>();

    public static SingleDecodingModule getInstance() {
        if (singleDecodingModule == null) {
            singleDecodingModule = new SingleDecodingModule();
        }
        return singleDecodingModule;
    }

    /**
     * 传入主界面的搜索按钮标识
     * 传入true当前按钮文本为解密
     * 控制操作
     *
     * @author zhouxingxue
     * @date 2023/10/14 20:40:02
     */
    private SingleDecodingModule() {
        this.setFont(DefaultFont.WEI_RUAN_PLAIN_13.getFont());
        this.addActionListener(e -> {
            int columnCount = jTable.getColumnCount();
            int selectedRow = jTable.getSelectedRow();
            boolean isNotMatch = operationList.stream().noneMatch(e1 -> e1 == jTable.getSelectedRow());
            //该次是单该次应该是加密显示，但该次应该执行解码操作
            for (int i = 2; i < columnCount; i++) {
                jTable.setValueAt(!(isNotMatch == switchSearch) ?
                                SecurityService.encodeBase64Salt(String.valueOf(jTable.getValueAt(selectedRow, i))) :
                                SecurityService.decodeBase64Salt(String.valueOf(jTable.getValueAt(selectedRow, i)))
                        , selectedRow, i);
            }
            if (isNotMatch) {
                operationList.add(selectedRow);
            } else {
                operationList.remove(new Integer(selectedRow));
            }
        });
    }

    /**
     * 在主界面右键点击时弹出右键菜单之前需要检测该项
     * 控制文本
     * zhouxingxue
     * 2023/10/14 20:54:57
     */
    public static void checkAndChange(boolean switchSearchNew, JTable jTableNew) {
        jTable = jTableNew;
        int selectedRow = jTable.getSelectedRow();
        switchSearch = switchSearchNew;
        boolean present = operationList.stream().anyMatch(e -> e == selectedRow);
        singleDecodingModule.setText((present != switchSearch) ? SINGLE_AC_DECODE : SINGLE_AC_ENCODE);
    }

    /**
     * 根据逻辑来说，这里的单控是无法影响到主搜索按钮的
     * 反倒是需要根据主控按钮的状态判断
     * 如此的话，在单控加入集合时（如有某行加入集合说明：该项与当前表其他数据的加密解密状态相反）点击主控按钮会导致表数据状态统一
     * 一旦表状态统一且集合没有清空，会导致单控状态判断错误，就会导致二次加密或二次解密
     * 综上，必须要提供一个方法给主控按钮，使其点击后清空单控的集合，即可较完美的解决该问题
     * zhouxingxue
     * 2023/10/14 22:37:43
     */
    public static void clearList() {
        operationList.clear();
    }

}
