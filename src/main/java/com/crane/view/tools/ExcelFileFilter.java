package com.crane.view.tools;

import javax.swing.filechooser.FileFilter;
import java.io.File;

/**
 * Description: excel文件过滤器
 * Author: ZhouXingxue
 * Date: 2022/12/31 18:19
 *
 * @author Crane Resigned
 */
public class ExcelFileFilter extends FileFilter {
    @Override
    public boolean accept(File f) {
        String name = f.getName();
        return f.isDirectory() || name.toLowerCase().endsWith(".xls") || name.toLowerCase().endsWith(".xlsx");
    }

    @Override
    public String getDescription() {
        return "*.xls;*.xlsx";
    }
}
