package com.crane.view.function.service;

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
        htmlBuilder.append("<html><head>");
        htmlBuilder.append(head);
        htmlBuilder.append("</head>    <style>\n" +
                "        table{ border: 1px solid gray;border-collapse:collapse;}\n" +
                "        table th,table td{\n" +
                "            border: 1px solid gray;\n" +
                "        }\n" +
                "    </style><body>");
    }

    /**
     * 创建表格
     *  new Object[]{true,string,string....}
     * @author AXing
     * @date 2023/12/7 17:03:21
     */
    public HtmlBuilderService createTable(List<Object[]> tableData) {
        htmlBuilder.append("<table border=1>");

        int trCnt = tableData.size();
        if (trCnt == 0) {
            return this;
        }

        for (Object[] tableDatum : tableData) {
            htmlBuilder.append("<tr>");
            int len = tableDatum.length;
            for (int j = 1; j < len; j++) {
                if (Boolean.parseBoolean(tableDatum[0].toString())) {
                    htmlBuilder.append("<th>");
                    htmlBuilder.append(tableDatum[j]);
                    htmlBuilder.append("</th>");
                } else {
                    htmlBuilder.append("<td>");
                    htmlBuilder.append(tableDatum[j]);
                    htmlBuilder.append("</td>");
                }

            }
            htmlBuilder.append("</tr>");
        }

        htmlBuilder.append("</table>");
        return this;
    }

    public String end() {
        htmlBuilder.append("</body></html>");
        return htmlBuilder.toString();
    }

}
