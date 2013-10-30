package m.z.imagelocus.config;

/**
 * @author Winnid
 * @Title: /双向绑定类型常量
 * @Description: /双向绑定类型常量
 * @date 13-9-11
 * @Version V1.0
 * Created by Winnid on 13-9-11.
 */
public class CodeDetail {
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
    //周边分类 -餐厅
    public static class Restaurant extends AbstractCode {
        public static final int All = 0;
        public static final int HuoGuo = 1;
        public static final int ChuanCai = 2;
        public static final int JingCai = 3;
        public static final int LuCai = 4;
        public static final int XiangCai = 5;

        public static final String[] KEYS = {"All", "HuoGuo", "ChuanCai", "JingCai", "LuCai", "XiangCai"};
        public static final String[] NAMES = {"全部", "火锅", "川菜", "京菜", "鲁菜", "湘菜"};
    }

    //周边分类 -酒店
    public static class Hotel extends AbstractCode {
        public static final int All = 0;
        public static final int BinGuan = 1;
        public static final int LvGuan = 2;
        public static final int QingNianLvShe = 3;
        public static final int JingJiXingJiuDian = 4;
        public static final int XingJiBinGuan = 5;
        public static final int ZhaoDaiSuo = 6;

        public static final String[] KEYS = {"All", "BinGuan", "LvGuan", "QingNianLvShe", "JingJiXingJiuDian", "XingJiBinGuan", "ZhaoDaiSuo"};
        public static final String[] NAMES = {"全部", "宾馆", "旅馆", "青年旅社", "经济型酒店", "星级宾馆", "招待所"};
    }
}
