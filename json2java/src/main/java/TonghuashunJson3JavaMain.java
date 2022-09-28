
import com.alibaba.fastjson.JSON;
import org.apache.commons.io.FileUtils;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import java.util.Map;


public class TonghuashunJson3JavaMain {

    /**
     * 基础路径，需要手动设置
     */
    public static String tlfPath = System.getProperty("user.dir") + "\\json2java\\src\\main\\resources\\";//模板文件位置
    public static String packPath = tlfPath + "out\\";

    public static void main(String[] args) {
        TonghuashunJson3JavaMain main = new TonghuashunJson3JavaMain();
        String content = main.readFileContent("tonghuashun/", "furtures.json");
        JsonRootBeanTonghuashun2 jsonRootBean = JSON.parseObject(content, JsonRootBeanTonghuashun2.class);

        String[] indicators = jsonRootBean.getIndicators().split(",");
        String[] names = jsonRootBean.getNames().split(",");
        if (indicators.length != names.length) {
            System.out.println("指标长度不一！");
            return;
        }

        //自定义参数
        HashMap<String, String> customParameter = new HashMap<>();
        customParameter.put("author", "author：yjs");
        customParameter.put("date", "date：" + new SimpleDateFormat("yyyy-MM-dd").format(new Date()));

        String className = "TongHuaShunFuturesBasicQuotation";

        String classNameResp = className + "Resp";
        //读取模板、生成代码
        main.writer(tlfPath + "RespTonghuashun2.tlf", packPath + classNameResp + ".java", classNameResp, jsonRootBean.getCodes(), indicators, names, customParameter);

        String classNameReq = className + "Dto";

        //读取模板、生成代码
        main.writer2(tlfPath + "ReqTonghuashun2.tlf", packPath + classNameReq + ".java", classNameReq, jsonRootBean.getCodes(), indicators, names, customParameter);

        String classNameMain = className + "DataProvider";
        //读取模板、生成代码
        main.writer3(tlfPath + "MainTonghuashun2.tlf", packPath + classNameMain + ".java", className, jsonRootBean.getCodes(), customParameter);

        System.out.println(classNameResp + ".java, " + classNameReq + ".java  生成完毕!");
    }

    /**
     * 读取模板，设置内容，生成文件
     *
     * @param templatePath    模板文件路径
     * @param outputFile      文件生成路径
     * @param indicators      字段信息
     * @param names           字段名称信息
     * @param customParameter 自定义参数
     */
    public void writer(String templatePath, String outputFile, String className, String classNickName, String[] indicators, String[] names, Map<String, String> customParameter) {
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
                    for (int i = 0; i < indicators.length; i++) {
                        String tableColumns = forContent.toString()
                                //表字段信息：类型、名称、注释
                                .replaceAll("\\$\\{data.columnComment}", names[i])
                                .replaceAll("\\$\\{data.columnJsonName}", indicators[i])
                                .replaceAll("\\$\\{data.dataType}", StringUtil.typeMapping("list<string>"))
                                .replaceAll("\\$\\{data.columnName}", StringUtil.lowerName(StringUtil.camelCaseName(indicators[i])));

                        //清除多余#end，以及换行符
                        tableColumns = tableColumns.replaceAll("#end\n", "");
                        tableColumns = tableColumns.replaceAll("#end", "");

                        //设置日期反序列化
                        String dateStr = "";
         /*               //主键
                        if ("Date".equals(StringUtil.typeMapping(field.getSTYPE1()))) {
                            dateStr = "@JsonSerialize(using = DateJsonSerializer.class)\n" +
                                    "    @JsonDeserialize(using = DateJsonDeserializer.class)";
                        }*/
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
     * 读取模板，设置内容，生成文件
     *
     * @param templatePath    模板文件路径
     * @param outputFile      文件生成路径
     * @param indicators      字段信息
     * @param names           字段名称信息
     * @param customParameter 自定义参数
     */
    public void writer2(String templatePath, String outputFile, String className, String classNickName, String[] indicators, String[] names, Map<String, String> customParameter) {

        String today = new SimpleDateFormat("yyyyMMdd").format(new Date());
        String month = new SimpleDateFormat("yyyyMM").format(new Date());
        String year = new SimpleDateFormat("yyyy").format(new Date());

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
                    for (int i = 0; i < indicators.length; i++) {
                        String columnParam = "null";
                    /*    if (!CollectionUtils.isEmpty(field.getIndiparams())) {
                            List<String> paramList = field.getIndiparams();
                            StringBuilder paramBuilder = new StringBuilder("Arrays.asList(");
                            paramList.forEach(i -> {
                                if (!"Arrays.asList(".equals(paramBuilder.toString())) {
                                    paramBuilder.append(",");
                                }
                                if (today.equals(i)) {
                                    paramBuilder.append("currentDate");
                                } else if (month.equals(i)) {
                                    paramBuilder.append("currentMonth");
                                } else if (year.equals(i)) {
                                    paramBuilder.append("currentYear");
                                } else if ("100".equals(i)) {
                                    paramBuilder.append("number");
                                } else {
                                    paramBuilder.append('"').append(i).append('"');
                                }
                            });
                            paramBuilder.append(")");
                            columnParam = paramBuilder.toString();
                        }*/
                        String tableColumns = forContent.toString()
                                //表字段信息：类型、名称、注释
                                .replaceAll("\\$\\{data.columnComment}", names[i])
                                .replaceAll("\\$\\{data.columnJsonName}", indicators[i])
                                .replaceAll("\\$\\{data.columnParam}", columnParam);

                        //清除多余#end，以及换行符
                        tableColumns = tableColumns.replaceAll("#end\n", "");
                        tableColumns = tableColumns.replaceAll("#end", "");

                        //设置日期反序列化
                        String dateStr = "";
         /*               //主键
                        if ("Date".equals(StringUtil.typeMapping(field.getSTYPE1()))) {
                            dateStr = "@JsonSerialize(using = DateJsonSerializer.class)\n" +
                                    "    @JsonDeserialize(using = DateJsonDeserializer.class)";
                        }*/
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
     * 读取模板，设置内容，生成文件
     *
     * @param templatePath    模板文件路径
     * @param outputFile      文件生成路径
     * @param customParameter 自定义参数
     */
    public void writer3(String templatePath, String outputFile, String className, String classNickName, Map<String, String> customParameter) {

        String classPath = className.replaceAll("TongHuaShun", "");
        classPath = StringUtil.lowerName(classPath);
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
