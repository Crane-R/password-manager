package com.crane.constant;

import com.crane.view.config.Language;

/**
 * Description: 构建窗口枚举
 * Author: ZhouXingxue
 * Date: 2022/12/31 15:52
 *
 * @author Crane Resigned
 */
public enum ExportImportCst {

    /**
     * 导出
     * Author: Crane Resigned
     * Date: 2022-12-31 15:55:34
     */
    EXPORT(
            true,
            Language.get("exportFrameTitle"),
            Language.get("exportTipLabel")
    ),
    /**
     * 导入
     * Author: Crane Resigned
     * Date: 2022-12-31 15:56:58
     */
    IMPORT(
            false,
            Language.get("importFrameTitle"),
            Language.get("importTipLabel"));

    /**
     * true是导出
     * false是导入
     * Author: Crane Resigned
     * Date: 2022-12-31 16:37:32
     */
    public final boolean IS_EXPORT;

    public final String TITLE;

    public final String TIP_LABEL;

    ExportImportCst(boolean isExport, String title, String tipLabel) {
        this.IS_EXPORT = isExport;
        this.TITLE = title;
        this.TIP_LABEL = tipLabel;
    }
}
