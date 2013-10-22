package m.z.imagelocus.config;

/**
 * @author Winnid
 * @Title: /双向绑定类型常量
 * @Description: /双向绑定类型常量
 * @date 13-9-11
 * @Version V1.0
 * Created by Winnid on 13-9-11.
 */
public class Code {

    /**
     * 内容类型存储State 　;"Type1","Type2","Type3","Type4"
     *
     * @author Winnid
     */
    public static class State {
        public static final int NORMAL= 0;
        public static final int DELETE = 1;
        public static final int FREEZE = 2;

        public static final String[] KEYS = {"NORMAL", "DELETE", "FREEZE"};
        public static final String[] NAMES = {"正常", "删除", "冻结"};

        public static String keyOf(int value) {
            return KEYS[value];
        }

        public static String nameOf(int value) {
            return NAMES[value];
        }

    }
}
