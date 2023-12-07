package crane.function;

import crane.constant.Constant;
import crane.constant.MainFrameCst;
import crane.constant.VersionCst;
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
            String html = new HtmlBuilderService(MainFrameCst.SIMPLE_TITLE + Language.get("lookFunBtn"))
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
        addHead("登录界面", "功能", "描述");
        addData("keyDuplicateTipTitle", "keyDuplicateTipMsg");
        addData("isLocal2TipTitle", "isLocal2TipMsg");
        addData("sureCreate", "isCreateSceneTipMsg");
        addData("isLightWeightVersion2", "isLightWeightVersion2TipMsg");
        addHead("主界面", "功能", "描述");
        addData("moderBtnTipTit", "moderBtnTipMsg");
        resultList.add(new Object[]{false, dataCnt++, Language.get("moderBtn2TipTit"),
                Language.get("moderBtn2TipMsg1") + Constant.DOUBLE_ENTER_DELAY + Language.get("moderBtn2TipMsg1")});

        return resultList;
    }

    private void addData(String function, String description) {
        resultList.add(new Object[]{false, dataCnt++, Language.get(function), Language.get(description)});
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
