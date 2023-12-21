package crane.constant;

import java.awt.*;

/**
 * Description: 默认字体枚举
 * Author: ZhouXingxue
 * Date: 2022/12/30 22:00
 *
 * @author Crane Resigned
 */
public enum DefaultFont {

    /**
     * 默认类型
     * Author: Crane Resigned
     * Date: 2022-12-30 22:14:27
     */
    WEI_RUAN_PLAIN_13("微软雅黑", Font.PLAIN, 13),
    WEI_RUAN_PLAIN_15("微软雅黑",Font.PLAIN,15),
    WEI_RUAN_BOLD_13("微软雅黑", Font.BOLD, 13),
    WEI_RUAN_BOLD_12("微软雅黑", Font.BOLD, 12);

    /**
     * 字体名称
     * Author: Crane Resigned
     * Date: 2022-12-30 23:41:50
     */
    private final String name;

    /**
     * 字体风格
     * Author: Crane Resigned
     * Date: 2022-12-30 23:42:10
     */
    private final int style;

    /**
     * 字体大小
     * Author: Crane Resigned
     * Date: 2022-12-30 23:42:26
     */
    private final int fontSize;

    DefaultFont(String name, int style, int fontSize) {
        this.name = name;
        this.style = style;
        this.fontSize = fontSize;
    }

    public Font getFont() {
        return new Font(name, style, fontSize);
    }

}
