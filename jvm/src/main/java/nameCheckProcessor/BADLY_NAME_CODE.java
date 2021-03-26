package nameCheckProcessor;

/**
 * @author : yjs
 * @version : 1.0
 * @createTime : 2021/3/26
 * @description :
 */
public class BADLY_NAME_CODE {
    enum colors {
        red, blue, green;
    }

    static final int _FORTY_TWO = 42;
    public static int NOT_A_CONSTANT = _FORTY_TWO;

    protected void BADLY_NAMED_CODE() {
        return;
    }

    public void NOTcamelCASEmethodNAME() {
        return;
    }

    public static void main(String[] args){
        System.out.println("hello");
    }
}
