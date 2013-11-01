package m.z.common;

import android.content.Context;
import android.os.Looper;
import android.view.Gravity;
import android.view.View;
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
     * 短时间显示msg
     */
    public static void display(Context context, String msg) {
        display(context, msg, Toast.LENGTH_SHORT);
    }

    /**
     * 短时间显示msg
     */
    public static void displayShort(Context context, String msg) {
        display(context, msg, Toast.LENGTH_SHORT);
    }

    /**
     * 长时间显示msg
     */
    public static void displayLong(Context context, String msg) {
        display(context, msg, Toast.LENGTH_LONG);
    }

    public static void display(final Context context, final String msg, final int time) {
        new Thread(new Runnable() {

            @Override
            public void run() {
                Looper.prepare();
                Toast.makeText(context, msg, time).show();
                Looper.loop();
            }
        }).start();
    }


    /**
     * 短时间显示自定义位置（中间）
     */
    public static void displayGravity(final Context context, final String msg) {
        displayGravity(context, msg, Toast.LENGTH_SHORT, Gravity.CENTER);
    }
    /**
     * 短时间显示自定义位置（中间）
     */
    public static void displayLongGravity(final Context context, final String msg) {
        displayGravity(context, msg, Toast.LENGTH_LONG, Gravity.CENTER);
    }
    /**
     * 短时间显示自定义位置（中间）
     */
    public static void displayGravity(final Context context, final String msg, final int time, final int gravity) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Looper.prepare();
                Toast toast = Toast.makeText(context, msg, Toast.LENGTH_SHORT);
                toast.setGravity(gravity, 0, 0);
                toast.show();
                Looper.loop();
            }
        }).start();
    }

    /**
     * 短时间显示view
     */
    public static void displayView(Context context, View view) {
        displayView(context, view, Toast.LENGTH_SHORT);
    }

    /**
     * 短时间显示view
     */
    public static void displayViewShort(Context context, View view) {
        displayView(context, view, Toast.LENGTH_SHORT);
    }

    /**
     * 长时间显示view
     */
    public static void displayViewLong(Context context, View view) {
        displayView(context, view, Toast.LENGTH_LONG);
    }

    /**
     * 短时间显示view
     */
    public static void displayView(final Context context, final View view, final int time) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Looper.prepare();
                Toast toast = new Toast(context);
                toast.setView(view);
                toast.setDuration(time);
                toast.show();
                Looper.loop();
            }
        }).start();

    }
}
