import java.util.Arrays;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * 字符串处理工具类
 */
public class StringUtil {
    /**
     * 数据库类型->JAVA类型
     *
     * @param dbType 数据库类型
     * @return JAVA类型
     */
    public static String typeMapping(String dbType) {
        String javaType;
        if ("int|integer".contains(dbType)) {
            javaType = "String";
        } else if ("float|double|decimal|real".contains(dbType)) {
            javaType = "String";
        } else if ("Date|date|time|datetime|timestamp".contains(dbType)) {
            javaType = "Date";
        } else if (Arrays.asList("list<date>", "list<time>", "list<list<timestamp>").contains(dbType)) {
            javaType = "List<Date>";
        } else if (Arrays.asList("list<string>", "list<String>").contains(dbType)) {
            javaType = "List<String>";
        } else if (dbType.contains("list") || dbType.contains("List")) {
            javaType = "List<String>";
        } else if (dbType.startsWith("[")) {
            javaType = "List<String>";
        } else {
            javaType = "String";
        }
        return javaType;
    }

    /**
     * 驼峰转换为下划线
     */
    public static String underscoreName(String camelCaseName) {
        StringBuilder result = new StringBuilder();
        if (camelCaseName != null && camelCaseName.length() > 0) {
            result.append(camelCaseName.substring(0, 1).toLowerCase());
            for (int i = 1; i < camelCaseName.length(); i++) {
                char ch = camelCaseName.charAt(i);
                if (Character.isUpperCase(ch)) {
                    result.append("_");
                    result.append(Character.toLowerCase(ch));
                } else {
                    result.append(ch);
                }
            }
        }
        return result.toString();
    }

    /**
     * 首字母大写
     */
    public static String captureName(String name) {
        char[] cs = name.toCharArray();
        cs[0] -= 32;
        return String.valueOf(cs);

    }
    /**
     * 首字母小写
     */
    public static String upperName(String name) {

        String first = name.substring(0, 1);
        String after = name.substring(1);
        first = first.toUpperCase();
        return first + after;
    }
    /**
     * 首字母小写
     */
    public static String lowerName(String name) {

        String first = name.substring(0, 1);
        String after = name.substring(1);
        first = first.toLowerCase();
        return first + after;
    }

    /**
     * 下划线转换为驼峰
     */
    public static String camelCaseName(String underscoreName) {
        StringBuilder result = new StringBuilder();
        if (underscoreName != null && underscoreName.length() > 0) {
            boolean flag = false;
            for (int i = 0; i < underscoreName.length(); i++) {
                char ch = underscoreName.charAt(i);
                if ("_".charAt(0) == ch) {
                    flag = true;
                } else {
                    if (flag) {
                        result.append(Character.toUpperCase(ch));
                        flag = false;
                    } else {
                        result.append(ch);
                    }
                }
            }
        }
        return result.toString();
    }


    /**
     * 下划线转驼峰
     * user_name  ---->  userName
     * userName   --->  userName
     *
     * @param underlineStr 带有下划线的字符串
     * @return 驼峰字符串
     */
    public static String toCamelCase(String underlineStr) {
        if (underlineStr == null) {
            return null;
        }
        // 分成数组
        char[] charArray = underlineStr.toCharArray();
        // 判断上次循环的字符是否是"_"
        boolean underlineBefore = false;
        StringBuffer buffer = new StringBuffer();
        for (int i = 0, l = charArray.length; i < l; i++) {
            // 判断当前字符是否是"_",如果跳出本次循环
            if (charArray[i] == 95) {
                underlineBefore = true;
            } else if (underlineBefore) {
                // 如果为true，代表上次的字符是"_",当前字符需要转成大写
                buffer.append(charArray[i] -= 32);
                underlineBefore = false;
            } else {
                // 不是"_"后的字符就直接追加
                buffer.append(charArray[i]);
            }
        }
        return buffer.toString();
    }

    /**
     * 驼峰转 下划线
     * userName  ---->  user_name
     * user_name  ---->  user_name
     *
     * @param camelCaseStr 驼峰字符串
     * @return 带下滑线的String
     */
    public static String toUnderlineCase(String camelCaseStr) {
        if (camelCaseStr == null) {
            return null;
        }
        // 将驼峰字符串转换成数组
        char[] charArray = camelCaseStr.toCharArray();
        StringBuffer buffer = new StringBuffer();
        //处理字符串
        for (int i = 0, l = charArray.length; i < l; i++) {
            if (charArray[i] >= 65 && charArray[i] <= 90) {
                buffer.append("_").append(charArray[i] += 32);
            } else {
                buffer.append(charArray[i]);
            }
        }
        return buffer.toString();
    }

}
 