package nameCheckProcessor;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import java.util.Set;


/**
 * 可以用"*"表示支持所有Annotations
 */
@SupportedAnnotationTypes("*")
/**
 * 只支持JDK 6的Java代码
 */
@SupportedSourceVersion(SourceVersion.RELEASE_6)
/**
 * @author : yjs
 * @version : 1.0
 * @createTime : 2021/3/26
 * @description : 注解处理器
 */

public class NameCheckProcessor extends AbstractProcessor {

    private NameChecker nameChecker;

    @Override
    public void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        nameChecker = new NameChecker(processingEnv);
    }

    /**
     * 对输入的语法树的各个节点进行名称检查
     *
     * @param annotations 此注解处理器所要处理的注解集合
     * @param roundEnv    当前这个轮次（Round） 中的抽象语法树节点,每个语法树节点在这里都表示为一个Element。
     * @return
     */
    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        if (!roundEnv.processingOver()) {
            for (Element e : roundEnv.getRootElements()) {
                nameChecker.checkNames(e);
            }
        }
        return false;
    }

}
