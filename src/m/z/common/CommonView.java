package m.z.common;

import android.content.Context;
import android.widget.Toast;

/**
 * @author Winnid
 * @version V1.3
 * @Title: 支持组件
 * @Package com.z.einstein.activity
 * @Description: 1.显示提示信息
 * @date 2013-9-11
 */
public class CommonView {

    /**
     * 显示msg
     */
    public static void display(Context context, String msg, int time) {
        Toast.makeText(context, msg, time).show();
    }

    /**
     * 长时间显示msg
     */
    public static void displayLong(Context context, String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
    }

    /**
     * 短时间显示msg
     */
    public static void displayShort(Context context, String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }

}
