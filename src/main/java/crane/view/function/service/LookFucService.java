package crane.view.function.service;

import crane.constant.Constant;
import crane.constant.MainFrameCst;
import crane.view.function.config.Language;
import crane.view.function.tools.FileTool;
import crane.model.jdbc.JdbcConnection;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * 查看功能
 * 项目启动前会检查用于展示功能的文件是否存在，如果不存在会创建
 *
 * @author AXing
 * @date 2023/12/7 16:33:02
 */
public class LookFucService {

    /**
     * 数据序号
     *
     * @author AXing
     * @date 2023/12/7 17:31:21
     */
    private int dataCnt = 1;

    private List<Object[]> resultList;

    private final File fucFile = new File(JdbcConnection.IS_TEST ? "src/main/resources/function.html" :
            Paths.get("").toAbsolutePath() + "resources/function.html");

    private void createFile() {
        try {
            boolean newFile = fucFile.createNewFile();
            if (!newFile) {
                return;
            }
            String html = new HtmlBuilderService(MainFrameCst.MAIN_TITLE + Language.get("lookFunBtn"))
                    .createTable(getData()).end();
            PrintWriter printWriter = new PrintWriter(new FileWriter(fucFile));
            printWriter.println(html);
            printWriter.flush();
            printWriter.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 获取数据
     *
     * @author AXing
     * @date 2023/12/7 17:18:46
     */
    private List<Object[]> getData() {
        resultList = new ArrayList<>();
//        addHead("登录界面", "功能", "描述");
//        addData("keyDuplicateTipTitle", "keyDuplicateTipMsg");
//        addData("isLocal2TipTitle", "isLocal2TipMsg");
//        addData("sureCreate", "isCreateSceneTipMsg");
//        addData("isLightWeightVersion2", "isLightWeightVersion2TipMsg");
//        addHead("主界面", "功能", "描述");
//        addData("moderBtnTipTit", "moderBtnTipMsg");
//        resultList.add(new Object[]{false, dataCnt++, Language.get("moderBtn2TipTit"),
//                Language.get("moderBtn2TipMsg1") + Constant.DOUBLE_ENTER_DELAY + Language.get("moderBtn2TipMsg1")});

        addHead("前言", "关于功能", "描述/解释");
        addDataNotLanguage("有关数据备份问题", "实际上我无法提供备份方案，因为软件不涉及服务器，所有内容都是存储在用户电脑本地，就连我自身也是使用其他程序和百度网盘来达到备份效果的");
        addDataNotLanguage("活性时间", "活性时间到期后会回退到登录界面，在时间即将到期（10%）时会变为红色");
        addDataNotLanguage("数据库模式","在古老版本（3.0+）的时候其实是有服务器可用的，<br/>" +
                "但因为服务器到期，<br/>" +
                "而且考虑到这个软件数据在服务器感觉不是很安全，我自己也没百分百保证安全的，可以看到主界面有声明，尽管加密算法我已经增强过很多次了<br/>" +
                "而且数据库模式适合使用数据量大的时候，这个软件的体量根本无需使用数据库，所以还是使用文件模式吧，<br/>" +
                "而且在该版本的需求中也没有完善数据库模式<br/>" +
                "再而且，该软件使用的技术（Swing）已经很老了，我不应该在这里停滞不前，更新到此版本本应是万万不该的");
        addDataNotLanguage("So,", "其实吧功能不多多用用就会了的");

        return resultList;
    }

    private void addData(String function, String description) {
        resultList.add(new Object[]{false, dataCnt++, Language.get(function), Language.get(description)});
    }

    private void addDataNotLanguage(String function, String description) {
        resultList.add(new Object[]{false, dataCnt++, function, description});
    }

    private void addHead(String num, String head, String description) {
        resultList.add(new Object[]{true, num, head, description});
    }

    public void openFile() {
        if (!fucFile.exists()) {
            createFile();
        }
        FileTool.openFile(fucFile.getPath());
    }

}
