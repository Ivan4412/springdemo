/**
 * Copyright 2022 bejson.com
 */
package qichacha2;

import java.util.List;

/**
 * Auto-generated: 2022-10-09 11:7:6
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */
public class ParamList {

    private String name;
    private String type;
    private String description;
    private String length;
    private String content;
    private List<ParamList> paramList;

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setLength(String length) {
        this.length = length;
    }

    public String getLength() {
        return length;
    }

    public List<ParamList> getParamList() {
        return paramList;
    }

    public void setParamList(List<ParamList> paramList) {
        this.paramList = paramList;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }


}