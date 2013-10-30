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
        public static final int Normal= 0;
        public static final int Delete = 1;
        public static final int Freeze = 2;

        public static final String[] KEYS = {"Normal", "Delete", "Freeze"};
        public static final String[] NAMES = {"正常", "删除", "冻结"};
    }

    //性别
    public static class Sex extends AbstractCode {
        public static final int Boy = 0;
        public static final int Girl = 1;

        public static final String[] KEYS = {"Boy", "Girl"};
        public static final String[] NAMES = {"男", "女"};
    }

    //周边分类
    public static class Category extends AbstractCode {
        public static CodeDetail.Hotel Hotel;
        public static CodeDetail.Restaurant Restaurant;

        public static final int Hotel_0 = 0;
        public static final int Restaurant_1 = 1;

        public static final String[] KEYS = {"Hotel", "Restaurant"};
        public static final String[] NAMES = {"酒店", "美食"};
    }
}
