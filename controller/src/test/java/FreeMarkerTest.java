import com.yjs.SpringbootMybaitsApplication;
import com.yjs.freemarker.StringTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.apache.commons.lang3.text.StrSubstitutor;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * author : yjs
 * createTime : 2018/7/6
 * description :
 * version : 1.0
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = SpringbootMybaitsApplication.class)
public class FreeMarkerTest {


    @Test
    public void freeMarkerTest() throws IOException, TemplateException {
        Configuration cfg = new Configuration();
        cfg.setTemplateLoader(new StringTemplateLoader("欢迎：${user}"));
        cfg.setDefaultEncoding("UTF-8");

        Template template = cfg.getTemplate("");

        Map root = new HashMap();
        root.put("user", "Keven Chen");

        StringWriter writer = new StringWriter();
        template.process(root, writer);
        System.out.println(writer.toString());
    }


    @Test
    public void StrSubstitutorTest() {
        Map valuesMap = new HashMap();
        valuesMap.put("animal", "quick brown fox");
        valuesMap.put("target", "lazy dog");
        String templateString = "The ${animal} jumped over the ${target}.";
        StrSubstitutor sub = new StrSubstitutor(valuesMap);
        String resolvedString = sub.replace(templateString);
        System.out.println(resolvedString);
    }


}


