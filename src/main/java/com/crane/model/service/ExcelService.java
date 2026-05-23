package com.crane.model.service;

import cn.hutool.core.date.DateUtil;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.write.handler.AbstractSheetWriteHandler;
import com.alibaba.excel.write.metadata.style.WriteCellStyle;
import com.alibaba.excel.write.metadata.style.WriteFont;
import com.alibaba.excel.write.style.HorizontalCellStyleStrategy;
import com.alibaba.excel.write.style.column.LongestMatchColumnWidthStyleStrategy;
import com.crane.constant.MainFrameCst;
import com.crane.model.bean.Account;
import com.crane.model.bean.vo.AccountVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.Sheet;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;

/**
 * Description: Excel操作服务类
 * Author: ZhouXingxue
 * Date: 2022/12/30 23:15
 *
 * @author Crane Resigned
 */
@Slf4j
public class ExcelService {

    /**
     * 导出数据为excel
     * Author: Crane Resigned
     * Date: 2022-12-30 23:20:09
     */
    public static boolean exportDataToExcel(List<Account> dataList, String absolutePath) {
        if (!checkPathIsContainXlsx(absolutePath)) {
            absolutePath = absolutePath + "/" + MainFrameCst.MAIN_TITLE + "_" + DateUtil.format(new Date(), "yyMMddHHmmss") + ".xlsx";
        }
        boolean newFileIsCreated = fileIsExistElseCreate(absolutePath);
        if (newFileIsCreated) {
            EasyExcel.write(absolutePath, AccountVo.class)
                    .registerWriteHandler(buildStyleStrategy())
                    .registerWriteHandler(new LongestMatchColumnWidthStyleStrategy())
                    .registerWriteHandler(new ExportSheetStyleHandler())
                    .sheet("账户数据")
                    .doWrite(dataList);
        }
        return newFileIsCreated;
    }

    private static HorizontalCellStyleStrategy buildStyleStrategy() {
        WriteCellStyle headStyle = new WriteCellStyle();
        headStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        headStyle.setFillPatternType(FillPatternType.SOLID_FOREGROUND);
        headStyle.setHorizontalAlignment(HorizontalAlignment.CENTER);
        headStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        headStyle.setBorderBottom(BorderStyle.THIN);
        headStyle.setBorderTop(BorderStyle.THIN);
        headStyle.setBorderLeft(BorderStyle.THIN);
        headStyle.setBorderRight(BorderStyle.THIN);

        WriteFont headFont = new WriteFont();
        headFont.setBold(true);
        headFont.setFontHeightInPoints((short) 11);
        headFont.setFontName("Microsoft YaHei");
        headStyle.setWriteFont(headFont);

        WriteCellStyle contentStyle = new WriteCellStyle();
        contentStyle.setHorizontalAlignment(HorizontalAlignment.LEFT);
        contentStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        contentStyle.setWrapped(Boolean.TRUE);
        contentStyle.setBorderBottom(BorderStyle.THIN);
        contentStyle.setBorderTop(BorderStyle.THIN);
        contentStyle.setBorderLeft(BorderStyle.THIN);
        contentStyle.setBorderRight(BorderStyle.THIN);

        WriteFont contentFont = new WriteFont();
        contentFont.setFontHeightInPoints((short) 10);
        contentFont.setFontName("Microsoft YaHei");
        contentStyle.setWriteFont(contentFont);
        return new HorizontalCellStyleStrategy(headStyle, contentStyle);
    }

    private static class ExportSheetStyleHandler extends AbstractSheetWriteHandler {

        @Override
        public void afterSheetCreate(com.alibaba.excel.write.metadata.holder.WriteWorkbookHolder writeWorkbookHolder,
                                     com.alibaba.excel.write.metadata.holder.WriteSheetHolder writeSheetHolder) {
            Sheet sheet = writeSheetHolder.getSheet();
            sheet.createFreezePane(0, 1);
            sheet.setDefaultRowHeightInPoints(20);
            sheet.setColumnWidth(0, 24 * 256);
            sheet.setColumnWidth(1, 24 * 256);
            sheet.setColumnWidth(2, 28 * 256);
            sheet.setColumnWidth(3, 40 * 256);
            if (sheet.getRow(0) != null) {
                sheet.getRow(0).setHeightInPoints(24);
            }
            sheet.setAutoFilter(new org.apache.poi.ss.util.CellRangeAddress(0, 0, 0, 3));
        }
    }

    /**
     * 检测路径是否包含.xlsx
     * Author: Crane Resigned
     * Date: 2022-12-31 13:00:42
     */
    public static boolean checkPathIsContainXlsx(String path) {
        StringBuilder stringBuilder = new StringBuilder(path).reverse();
        char[] checks = new char[]{'x', 's', 'l', 'x', '.'};
        int len = checks.length;
        for (int i = 0; i < len; i++) {
            if (stringBuilder.charAt(i) != checks[i]) {
                return false;
            }
        }
        return true;
    }

    /**
     * 检查文件是否存在，不存在创建
     *
     * @Author Crane Resigned
     * @Date 2023-02-04 00:12:58
     */
    public static boolean fileIsExistElseCreate(String path) {
        File targetFile = new File(path);
        if (!targetFile.exists()) {
            try {
                File parentFile = targetFile.getParentFile();
                if (parentFile != null && !parentFile.exists() && !parentFile.mkdirs()) {
                    log.error("create parent directory failed: {}", parentFile.getAbsolutePath());
                    return false;
                }
                return targetFile.createNewFile();
            } catch (IOException e) {
                log.error(path);
                throw new RuntimeException(e);
            }
        }
        return true;
    }

}
