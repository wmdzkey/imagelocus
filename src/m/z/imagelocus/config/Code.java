package m.z.imagelocus.config;

import java.util.AbstractMap;

/**
 * @author Winnid
 * @Title: /双向绑定类型常量
 * @Description: /双向绑定类型常量
 * @date 13-9-11
 * @Version V1.0
 * Created by Winnid on 13-9-11.
 */
public class Code {
    private abstract static class AbstractCode {
        public String[] KEYS;
        public String[] NAMES;
        public String keyOf(int value) {
            return KEYS[value];
        }
        public String nameOf(int value) {
            return NAMES[value];
        }
    }

    //状态
    public static class State extends AbstractCode {
        public static final int NORMAL= 0;
        public static final int DELETE = 1;
        public static final int FREEZE = 2;

        public static final String[] KEYS = {"NORMAL", "DELETE", "FREEZE"};
        public static final String[] NAMES = {"正常", "删除", "冻结"};
    }

    //性别
    public static class Sex extends AbstractCode {
        public static final int BOY= 0;
        public static final int GIRL = 1;

        public static final String[] KEYS = {"BOY", "GIRL"};
        public static final String[] NAMES = {"男", "女"};
    }

    //周边分类
    public static class Periphery extends AbstractCode {
        public static final int HOTEL= 0;
        public static final int RESTAURANT = 1;

        public static final String[] KEYS = {"HOTEL", "RESTAURANT"};
        public static final String[] NAMES = {"酒店", "餐厅"};
    }
}
