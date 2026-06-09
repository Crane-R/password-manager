package com.crane.constant;

import com.crane.view.config.Language;

/**
 * 导入导出窗口枚举。
 */
public enum ExportImportCst {

    /**
     * 导出。
     */
    EXPORT(
            true,
            Language.get("exportFrameTitle"),
            Language.get("exportTipLabel")
    ),
    IMPORT_ENCRYPT(
            false,
            Language.get("importEncryptTit"),
            Language.get("importEncryptLabel")
    ),
    /**
     * 导入。
     */
    IMPORT(
            false,
            Language.get("importFrameTitle"),
            Language.get("importTipLabel"));

    /**
     * true 是导出，false 是导入。
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
