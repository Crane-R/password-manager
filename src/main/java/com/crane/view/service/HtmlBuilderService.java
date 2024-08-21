package com.crane.view.service;

import com.crane.constant.MainFrameCst;
import com.crane.constant.VersionCst;

import java.util.List;

/**
 * 构建html结构的服务
 *
 * @author AXing
 * @date 2023/12/7 16:56:32
 */
public class HtmlBuilderService {

    private final StringBuilder htmlBuilder;

    public HtmlBuilderService(String head) {
        htmlBuilder = new StringBuilder();
        htmlBuilder.append("<html><title>");
        htmlBuilder.append(head);
        htmlBuilder.append("</title>" +
                "<head><link rel=\"stylesheet\" href=\"bootstrap/bootstrap.css\"></head><body><div class=\"jumbotron\">");
    }

    /**
     * 构建徽章标题
     *
     * @Author Crane Resigned
     * @Date 2024/4/19 22:44:08
     */
    public HtmlBuilderService buildBadgeTitle() {
        htmlBuilder.append("<h1> ").append(MainFrameCst.FULL_TITLE).append(" 功能说明 ");
        String versionStr = VersionCst.VERSION.split("-")[1].toUpperCase();
        switch (versionStr) {
            case "RC":
                htmlBuilder.append("<span class=\"badge badge-secondary\">Release Candidate</span>");
                break;
            case "BETA":
                htmlBuilder.append("<span class=\"badge badge-secondary\">Beta</span>");
                break;
            case "ALPHA":
                htmlBuilder.append("<span class=\"badge badge-secondary\">Alpha</span>");
                break;
        }
        htmlBuilder.append("</h1>");
        return this;
    }

    /**
     * 创建单个表格
     * new Object[]{true,string,string....}
     * 铁铁记得要多个标题行的话就要创建多个表格也就是多调几次这个方法哦，一个方法只能创建一个表格（一个表格只有一行标题）
     *
     * @author AXing
     * @date 2023/12/7 17:03:21
     */
    public HtmlBuilderService createTable(List<Object[]> tableData) {
        htmlBuilder.append("<table class='table'>");

        int trCnt = tableData.size();
        if (trCnt == 0) {
            return this;
        }

        tableData.forEach(object -> {
            int dataLen = object.length;
            if(Boolean.parseBoolean(object[0].toString())){
                htmlBuilder.append("<tr>");
                for (int j = 1; j < dataLen; j++) {
                    htmlBuilder.append("<th scope='col'>").append(object[j]).append("</th>");
                }
                htmlBuilder.append("</tr>");
            }else {
                htmlBuilder.append("<tr>");
                htmlBuilder.append("<th scope='row'>").append(object[1]).append("</th>");
                for (int j = 2; j < dataLen; j++) {
                    htmlBuilder.append("<td>").append(object[j]).append("</td>");
                }
                htmlBuilder.append("</tr>");
            }
        });

        htmlBuilder.append("</table>");
        return this;
    }

    public String end() {
        htmlBuilder.append(
                "</div><script src=\"bootstrap/jquery-3.6.0.js\"></script>\n" +
                        "<script src=\"bootstrap/bootstrap.js\"></script>" +
                        "</body></html>");
        return htmlBuilder.toString();
    }

}
