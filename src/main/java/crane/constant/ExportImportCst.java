package crane.constant;

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
            "导出数据为xlsx，请输入正确的格式，否则程序可能会崩溃",
            "输入或选择目标路径"
    ),
    /**
     * 导入
     * Author: Crane Resigned
     * Date: 2022-12-31 15:56:58
     */
    IMPORT(
            false,
            "导入账户，请使用文件选择器或输入文件路径",
            "选择源文件或输入源文件绝对路径");

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
