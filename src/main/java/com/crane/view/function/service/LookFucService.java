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
            FucContentList functionList = new FucContentList();
            functionList.addHead("function.head0", "function.head0Des");
            functionList.addData("function.head0.function1", "function.head0.des1");
            functionList.addData("function.head0.function2", "function.head0.des2");
            functionList.addHead("function.head1", "function.head1Des");
            functionList.addData("function.head1.function1", "function.head1.des1");
            functionList.addData("function.head1.function2", "function.head1.des2");
            functionList.addData("function.head1.function3", "function.head1.des3");

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

        public void addHead(String head, String des) {
            super.add(new Object[]{true, Language.get("function.num"), Language.get(head), Language.get(des)});
        }

        public void addData(String function, String description) {
            super.add(new Object[]{false, ++count, Language.get(function), Language.get(description)});
        }

    }

}
