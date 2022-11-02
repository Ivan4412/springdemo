
import com.alibaba.fastjson.JSON;
import org.apache.commons.io.FileUtils;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import qichacha.RespParamList;
import qichacha2.ParamList;
import qichacha2.Qcc2RootBean;
import qichacha2.QiChaChaBean;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.*;


public class QiChaChaJson2JavaMain {

    /**
     * 基础路径，需要手动设置
     */
    public static String tlfPath = System.getProperty("user.dir") + "\\json2java\\src\\main\\resources\\";//模板文件位置
    public static String packPath = tlfPath + "out\\";

    public static void main(String[] args) {
        QiChaChaJson2JavaMain main = new QiChaChaJson2JavaMain();

        String className = "QiChaChaCaseFilingDetail";
        String jsonName = "caseDetail";
        String jsonBody = jsonName + "Body.json";
        String jsonTitle = jsonName + "Title.json";

        String content = main.readFileContent("qichacha/json/", jsonBody);
        Qcc2RootBean rootBean = JSON.parseObject(content, Qcc2RootBean.class);

        String content2 = main.readFileContent("qichacha/json/", jsonTitle);
        QiChaChaBean titleBean = JSON.parseObject(content2, QiChaChaBean.class);

        String apiPath = rootBean.getTitles().getParamList().getApiUrl();
        apiPath = apiPath.replace("http://api.qichacha.com", "");

        //自定义参数
        HashMap<String, String> customParameter = new HashMap<>();
        customParameter.put("author", "author：yjs");
        customParameter.put("date", "date：" + new SimpleDateFormat("yyyy-MM-dd").format(new Date()));

        String classNameResp = className + "Resp";
        String classNameReq = className + "Req";
        String classNameMain = className + "DataProvider";
        String classNickName = titleBean.getApiName();
        String classNotes = titleBean.getDescription();

        boolean isKeywordType = false;
        boolean isKeywordPageType = false;
        List<ParamList> reqList = rootBean.getQuery().getParamList();
        if (reqList.size() == 2) {
            boolean flag = true;
            for (ParamList param : reqList) {
                if (!Arrays.asList("key", "keyword", "keyWord", "KeyWord", "searchKey", "SearchKey").contains(param.getName())) {
                    flag = false;
                }
            }
            if (flag) {
                isKeywordType = true;
            }
        } else if (reqList.size() == 4) {
            boolean flag = true;
            for (ParamList param : reqList) {
                if (!Arrays.asList("key", "keyword", "keyWord", "pageIndex", "searchKey", "pageSize").contains(param.getName())) {
                    flag = false;
                }
            }
            if (flag) {
                isKeywordPageType = true;
            }
        }

        //读取模板、生成代码
        main.writer(tlfPath + "RespQiChaCha.tlf", packPath + classNameResp + ".java", classNameResp, titleBean.getApiName(), rootBean.getReturn().getParamList(), customParameter);

        String mainTlf = "MainQiChaChaKeyword.tlf";
        if (!isKeywordType) {
            if (isKeywordPageType) {
                mainTlf = "MainQiChaChaKeywordReqContent2.tlf";
            } else {
                mainTlf = "MainQiChaChaKeywordReqContent.tlf";
            }
            //读取模板、生成req代码
            main.writer(tlfPath + "ReqQiChaCha.tlf", packPath + classNameReq + ".java", classNameReq, titleBean.getApiName(), rootBean.getQuery().getParamList(), customParameter);
        }

        //读取模板、生成代码
        main.writer3(tlfPath + mainTlf, packPath + classNameMain + ".java", className, classNickName, apiPath, classNotes, customParameter);

        System.out.println(classNameResp + ".java " + classNameMain + ".java 生成完毕!");
    }

    public void writer3(String templatePath, String outputFile, String className, String classNickName, String apiPath, String classNotes, Map<String, String> customParameter) {

        String classPath = className.replaceAll("QiChaCha", "");
        classPath = StringUtil.lowerName(classPath);

        try (FileReader fileReader = new FileReader(templatePath);
             BufferedReader reader = new BufferedReader(fileReader)) {
            //生成文件
            File file = FileUtil.createFile(outputFile);
            StringBuffer stringBuffer = new StringBuffer();

            //读取模板文件，拼接文件内容
            Object[] lines = reader.lines().toArray();
            for (Object o : lines) {
                String line = String.valueOf(o);

                /* 设置值 */
                if (line.contains("${classNickName}")) {
                    line = line.replaceAll("\\$\\{classNickName}", classNickName);
                }
                if (line.contains("${apiPath}")) {
                    line = line.replaceAll("\\$\\{apiPath}", apiPath);
                }
                if (line.contains("${classNotes}")) {
                    line = line.replaceAll("\\$\\{classNotes}", classNotes);
                }
                if (line.contains("${className}")) {
                    line = line.replaceAll("\\$\\{className}", StringUtil.camelCaseName(className));
                }
                if (line.contains("${classPath}")) {
                    line = line.replaceAll("\\$\\{classPath}", classPath);
                }
                //处理自定义参数
                line = customParameter(line, customParameter);

                stringBuffer.append(line).append("\n");
            }

            //写入数据到到文件中
            FileUtil.fileWriter(file, stringBuffer);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 读取模板，设置内容，生成文件
     *
     * @param templatePath    模板文件路径
     * @param outputFile      文件生成路径
     * @param fieldslist      字段信息
     * @param customParameter 自定义参数
     */
    public void writer(String templatePath, String outputFile, String className, String classNickName, List<ParamList> fieldslist, Map<String, String> customParameter) {

        //for循环标识
        boolean forFlag = false;
        StringBuilder forContent = new StringBuilder();

        try (FileReader fileReader = new FileReader(templatePath);
             BufferedReader reader = new BufferedReader(fileReader)) {
            //生成文件
            File file = FileUtil.createFile(outputFile);
            StringBuffer stringBuffer = new StringBuffer();

            //读取模板文件，拼接文件内容
            Object[] lines = reader.lines().toArray();
            for (Object o : lines) {
                String line = String.valueOf(o);

                /* 设置值 */
                if (line.contains("${classNickName}")) {
                    line = line.replaceAll("\\$\\{classNickName}", classNickName);
                }
                if (line.contains("${className}")) {
                    line = line.replaceAll("\\$\\{className}", StringUtil.camelCaseName(className));
                }

                //处理自定义参数
                line = customParameter(line, customParameter);

                //先取得循环体的内容
                if (forFlag) {
                    forContent.append(line).append("\n");
                }

                //是否为for循环遍历表字段
                if (line.contains("#for")) {
                    forFlag = true;
                }
                if (line.contains("#end")) {
                    forFlag = false;
                    line = line.replaceAll("#end\n", "");
                    line = line.replaceAll("#end", "");
                }

                //遍历循环体的内容，并设置值
                if (!forFlag && forContent.length() > 0) {
                    //遍历表字段
                    for (ParamList field : fieldslist) {
                        //表字段信息：类型、名称、注释
                        String columnComment = field.getDescription();
                        String columnJsonName = field.getName();
                        String dataType = StringUtil.typeMapping(field.getType());
                        String columnName = StringUtil.lowerName(StringUtil.camelCaseName(field.getName()));

                        if ("data".equals(columnName)) {
                            columnName = "dataItem";
                        }

                        if (dataType.equals("Object")) {
                            dataType = StringUtil.upperName(columnName);
                        }
                        if (dataType.equals("List<Object>")) {
                            dataType = "List<" + StringUtil.upperName(columnName) + ">";
                        }
                        String tableColumns = forContent.toString()
                                .replaceAll("\\$\\{data.columnComment}", columnComment)
                                .replaceAll("\\$\\{data.columnJsonName}", columnJsonName)
                                .replaceAll("\\$\\{data.dataType}", dataType)
                                .replaceAll("\\$\\{data.columnName}", columnName);

                        //清除多余#end，以及换行符
                        tableColumns = tableColumns.replaceAll("#end\n", "");
                        tableColumns = tableColumns.replaceAll("#end", "");

                        //设置对象内部类
                        String ObjectStr = "";
                        //主键
                        if (StringUtil.typeMapping(field.getType()).equals("Object") || StringUtil.typeMapping(field.getType()).equals("List<Object>")) {
                            ObjectStr = "\n    @Data\n" +
                                    "    public static class " + StringUtil.upperName(columnName) + " {\n";
                            List<ParamList> paramLists = field.getParamList();
                            if (!CollectionUtils.isEmpty(paramLists)) {
                                for (ParamList param : paramLists) {
                                    String paramType = StringUtil.typeMapping(param.getType());
                                    if (paramType.equals("Object")) {
                                        paramType = StringUtil.upperName(StringUtil.camelCaseName(param.getName()));
                                    }
                                    if (paramType.equals("List<Object>")) {
                                        paramType = "List<" + StringUtil.upperName(StringUtil.camelCaseName(param.getName())) + ">";
                                    }

                                    String temp = "     /**\n" +
                                            "       * " + param.getDescription() + "\n" +
                                            "       */\n" +
                                            "       @ApiModelProperty(\"" + param.getDescription() + "\")\n" +
                                            "       @JsonProperty(value = \"" + param.getName() + "\")\n" +
                                            "       protected " + paramType + " " + StringUtil.lowerName(StringUtil.camelCaseName(param.getName())) + ";\n";
                                    ObjectStr = ObjectStr + temp;

                                    List<ParamList> subList = param.getParamList();
                                    if (StringUtil.typeMapping(param.getType()).equals("Object") || StringUtil.typeMapping(param.getType()).equals("List<Object>")) {
                                        if (!CollectionUtils.isEmpty(subList)) {
                                            ObjectStr = ObjectStr + "\n    @Data\n" +
                                                    "        public static class " + StringUtil.upperName(StringUtil.camelCaseName(param.getName())) + " {\n";
                                            for (ParamList paramSub : subList) {

                                                String paramSubType = StringUtil.typeMapping(paramSub.getType());
                                                if (paramSubType.equals("Object")) {
                                                    paramSubType = StringUtil.upperName(StringUtil.camelCaseName(paramSub.getName()));
                                                }
                                                if (paramSubType.equals("List<Object>")) {
                                                    paramSubType = "List<" + StringUtil.upperName(StringUtil.camelCaseName(paramSub.getName())) + ">";
                                                }

                                                String tempSub = "          /**\n" +
                                                        "            * " + paramSub.getDescription() + "\n" +
                                                        "            */\n" +
                                                        "            @ApiModelProperty(\"" + paramSub.getDescription() + "\")\n" +
                                                        "            @JsonProperty(value = \"" + paramSub.getName() + "\")\n" +
                                                        "            protected " + paramSubType + " " + StringUtil.lowerName(StringUtil.camelCaseName(paramSub.getName())) + ";\n";
                                                ObjectStr = ObjectStr + tempSub;

                                                List<ParamList> subsubList = paramSub.getParamList();

                                                if (StringUtil.typeMapping(paramSub.getType()).equals("Object") || StringUtil.typeMapping(paramSub.getType()).equals("List<Object>")) {
                                                    if (!CollectionUtils.isEmpty(subsubList)) {
                                                        ObjectStr = ObjectStr + "\n        @Data\n" +
                                                                "        public static class " + StringUtil.upperName(StringUtil.camelCaseName(paramSub.getName())) + " {\n";
                                                        for (ParamList paramSubSub : subsubList) {
                                                            String tempSubSub = "          /**\n" +
                                                                    "            * " + paramSubSub.getDescription() + "\n" +
                                                                    "            */\n" +
                                                                    "            @ApiModelProperty(\"" + paramSubSub.getDescription() + "\")\n" +
                                                                    "            @JsonProperty(value = \"" + paramSubSub.getName() + "\")\n" +
                                                                    "            protected " + StringUtil.typeMapping(paramSubSub.getType()) + " " + StringUtil.lowerName(StringUtil.camelCaseName(paramSubSub.getName())) + ";\n";
                                                            ObjectStr = ObjectStr + tempSubSub;
                                                        }
                                                        ObjectStr = ObjectStr + "   }\n";
                                                    }
                                                }

                                            }

                                            ObjectStr = ObjectStr + "   }\n";
                                        }
                                    }
                                }
                            }
                            ObjectStr = ObjectStr + "   }\n";
                        }
                        if (StringUtils.isEmpty(ObjectStr)) {
                            tableColumns = tableColumns.replaceAll("    #ifObject\n", "");
                            tableColumns = tableColumns.replaceAll("#ifObject", "");
                        } else {
                            tableColumns = tableColumns.replaceAll("#ifObject", ObjectStr);
                        }


                        //设置日期反序列化
                        String dateStr = "";
                        //主键
                        if ("Date".equals(StringUtil.typeMapping(field.getType()))) {
                            dateStr = "@JsonSerialize(using = DateJsonSerializer.class)\n" +
                                    "    @JsonDeserialize(using = DateJsonDeserializer.class)";
                        }
                        if (StringUtils.isEmpty(dateStr)) {
                            tableColumns = tableColumns.replaceAll("    #ifDate\n", "");
                            tableColumns = tableColumns.replaceAll("#ifDate", "");
                        } else {
                            tableColumns = tableColumns.replaceAll("#ifDate", dateStr);
                        }

                        //处理自定义参数
                        tableColumns = customParameter(tableColumns, customParameter);

                        //前补tab，后补换行符
                        stringBuffer.append("    ").append(tableColumns.trim()).append("\n\n");
                    }
                    //置空
                    forContent.setLength(0);
                }

                if (!forFlag) {
                    stringBuffer.append(line).append("\n");
                }
            }

            //写入数据到到文件中
            FileUtil.fileWriter(file, stringBuffer);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 处理自定义参数
     */
    private String customParameter(String str, Map<String, String> customParameter) {
        for (String key : customParameter.keySet()) {
            str = str.replaceAll("\\$\\{" + key + "}", customParameter.get(key));
        }
        return str;
    }

    /**
     * <pre>
     * 读取文件内容
     * </pre>
     *
     * @param folderName 位于resource目录下的文件夹名称，如：static, static/map
     * @param fileName   文件名称，如：resource/static/hero.json 则直接传 /hero.json
     * @return
     */
    public String readFileContent(String folderName, String fileName) {
        URL url = this.getClass().getProtectionDomain().getClassLoader().getResource(folderName);
        if (url == null) {
            System.out.println("需要找的目录 static/hero 在资源文件夹下找不到");
            return null;
        }
        File file = new File(url.getPath() + fileName);
        String content = "";
        try {
            content = FileUtils.readFileToString(file, "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return content;
    }

}
