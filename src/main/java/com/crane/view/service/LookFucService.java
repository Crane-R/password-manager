package com.crane.view.service;

import com.crane.constant.Constant;
import com.crane.constant.MainFrameCst;
import com.crane.model.jdbc.JdbcConnection;
import com.crane.view.config.Language;
import com.crane.view.tools.FileTool;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Paths;
import java.util.ArrayList;

/**
 * 查看功能
 * 项目启动前会检查用于展示功能的文件是否存在，如果不存在会创建
 *
 * @author AXing
 * @date 2023/12/7 16:33:02
 */
@Slf4j
public class LookFucService {

    private final File fucFile = new File(JdbcConnection.IS_TEST ? "src/main/resources/function_web/function.html" :
            Paths.get("").toAbsolutePath() + "/resources/function_web/function.html");

    private void createFile() {
        try {
            if (fucFile.exists()) {
                boolean delete = fucFile.delete();
                if (delete) {
                    log.info("功能介绍文件被删除");
                }
            }
            boolean newFile = fucFile.createNewFile();
            if (!newFile) {
                return;
            }

            //构建数据
            FucContentList functionList = getFunctionList();

            String html = new HtmlBuilderService(MainFrameCst.MAIN_TITLE + Language.get("lookFunBtn"))
                    .buildBadgeTitle()
                    .createTable(functionList)
                    .end();
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
     * 构建功能说明集合
     *
     * @Author CraneResigned
     * @Date 2024/8/11 13:19:12
     */
    private static FucContentList getFunctionList() {
        FucContentList functionList = new FucContentList();
        functionList.addHead("function.head0", "function.head0.function", "function.head0.des");
        functionList.addDataByLanguage("loginShowScene", "function.head0.d1");
        functionList.addDataByLanguage("registerShowScene", "function.head0.d2");
        functionList.addDataByLanguage("function.head0.f3", "function.head0.d3");
        functionList.addDataByLanguage("isFileMode", "function.head0.d4");
        functionList.addDataByLanguage("isDataMode", "function.head0.d5");
        functionList.addDataByLanguage("isLocal2", "function.head0.d6");
        functionList.addDataByLanguage("isLocal", "function.head0.d7");

        functionList.addHead("function.head1", "function.head1.function", "function.head1.des");
        functionList.addDataByLanguage("function.head1.f1", "function.head1.d1");
        functionList.addDataByLanguage("activistBtn", "function.head1.d2");
        functionList.addDataByLanguage("activistBtn2", "function.head1.d3");
        functionList.addDataByLanguage("searchBtn", "function.head1.d4");
        functionList.addDataByLanguage("searchBtn2", "function.head1.d5");
        functionList.addDataByLanguage("addBtn", "function.head1.d6");
        functionList.addDataByLanguage("clearBtn", "function.head1.d7");
        functionList.addDataByStr(Language.get("moderBtn2"), Language.get("moderBtn2TipMsg1") + Constant.DOUBLE_ENTER_DELAY + Language.get("moderBtn2TipMsg2"));
        functionList.addDataByLanguage("moderBtn", "moderBtnTipMsg");
        functionList.addDataByLanguage("function.head1.f8", "function.head1.d8");
        functionList.addDataByLanguage("function.head1.f9", "function.head1.d9");

        return functionList;
    }

    public void openFile() {
        if (JdbcConnection.IS_TEST || !fucFile.exists()) {
            createFile();
        }
        FileTool.openFile(fucFile.getPath());
    }

    /**
     * 表格数据集合
     *
     * @Author Crane Resigned
     * @Date 2024/4/26 17:31:30
     */
    static class FucContentList extends ArrayList<Object[]> {

        private int count;

        public void addHead(String head, String function, String des) {
            super.add(new Object[]{true, Language.get(head), Language.get(function), Language.get(des)});
        }

        public void addDataByLanguage(String function, String description) {
            super.add(new Object[]{false, ++count, Language.get(function), Language.get(description)});
        }

        public void addDataByStr(String function, String description) {
            super.add(new Object[]{false, ++count, function, description});
        }

    }

}
