package crane.constant;

import java.awt.*;

/**
 * Description: 默认字体枚举
 * FIXME:有一个小漏洞，这个默认字体对象是已经被new出来的，当A使用过后调用了set方法，会改变属性的值，从而导致下一次调用时是A改过的值
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
    DEFAULT_FONT_ONE("微软雅黑", Font.PLAIN, 13);

    /**
     * 字体名称
     * Author: Crane Resigned
     * Date: 2022-12-30 23:41:50
     */
    private String name;

    /**
     * 字体风格
     * Author: Crane Resigned
     * Date: 2022-12-30 23:42:10
     */
    private int style;

    /**
     * 字体大小
     * Author: Crane Resigned
     * Date: 2022-12-30 23:42:26
     */
    private int fontSize;

    DefaultFont(String name, int style, int fontSize) {
        this.name = name;
        this.style = style;
        this.fontSize = fontSize;
    }

    public Font getFont() {
        return new Font(name, style, fontSize);
    }

    /**
     * 连点设置方法
     * Author: Crane Resigned
     * Date: 2022-12-30 23:39:55
     */
    public DefaultFont setName(String name) {
        this.name = name;
        return this;
    }

    public DefaultFont setStyle(int style) {
        this.style = style;
        return this;
    }

    public DefaultFont setFontSize(int fontSize) {
        this.fontSize = fontSize;
        return this;
    }

}
