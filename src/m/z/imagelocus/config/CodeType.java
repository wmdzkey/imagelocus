package m.z.imagelocus.config;

/**
 * @author Winnid
 * @Title: /双向绑定类型常量
 * @Description: /双向绑定类型常量
 * @date 13-9-11
 * @Version V1.0
 * Created by Winnid on 13-9-11.
 */
public class CodeType {

    /**
     * 内容类型存储Demo 　;"Type1","Type2","Type3","Type4"
     *
     * @author Winnid
     */
    public static class Demo {
        public static final int Type1 = 0;
        public static final int Type2 = 1;
        public static final int Type3 = 2;
        public static final int Type4 = 3;

        public static final String[] KEYS = {"TYPE1", "TYPE2", "TYPE3", "TYPE4"};
        public static final String[] NAMES = {"类型1", "类型2", "类型3", "类型4"};

        public static String keyOf(int value) {
            return KEYS[value];
        }

        public static String nameOf(int value) {
            return NAMES[value];
        }

    }
}
