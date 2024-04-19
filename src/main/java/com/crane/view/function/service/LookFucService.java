package com.crane.view.function.service;

import com.crane.constant.MainFrameCst;
import com.crane.model.jdbc.JdbcConnection;
import com.crane.view.function.config.Language;
import com.crane.view.function.tools.FileTool;
import lombok.extern.slf4j.Slf4j;

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
@Slf4j
public class LookFucService {

    /**
     * 数据序号
     *
     * @author AXing
     * @date 2023/12/7 17:31:21
     */
    private int dataCnt = 1;

    private List<Object[]> resultList;

    private final File fucFile = new File(JdbcConnection.IS_TEST ? "src/main/resources/function_web/function.html" :
            Paths.get("").toAbsolutePath() + "/resources/function_web/function.html");

    private void createFile() {
        try {
            if (fucFile.exists()) {
                fucFile.delete();
            }
            boolean newFile = fucFile.createNewFile();
            if (!newFile) {
                return;
            }
            String html = new HtmlBuilderService(MainFrameCst.MAIN_TITLE + Language.get("lookFunBtn"))
                    .buildBadgeTitle()
                    .createTable(getData()).end();
            PrintWriter printWriter = new PrintWriter(new FileWriter(fucFile));
            printWriter.println(html);
            printWriter.flush();
            printWriter.close();
        } catch (IOException e) {
            log.error(e.toString());
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

        addHead("序号", "关于功能", "描述/解释");
        addDataNotLanguage("数据备份问题", "事实上作者无法提供可行的备份方案，因为软件不涉及服务器，所有内容都是存储在用户电脑本地的");
        addDataNotLanguage("活性时间", "活性时间到期后会回退到登录界面，在时间即将到期（10%）时会变为红色");
        addDataNotLanguage("数据库模式", "基于该软件的数据量、安全性、便捷性等方面的综合考虑，数据库模式在4.x后的版本已经不再维护</br>" +
                "但依旧可以经过配置文件的修改使用，具体的部署方案（如数据库表的字段是固定的）可能需要询问开发者");


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
        if (JdbcConnection.IS_TEST || !fucFile.exists()) {
            createFile();
        }
        FileTool.openFile(fucFile.getPath());
    }

}
