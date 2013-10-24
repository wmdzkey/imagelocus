package m.z.common;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import m.z.imagelocus.R;

/**
 * @author Winnid
 * @Title:带有回调函数的进度条
 * @Description: doWork表示方法处理过程<br>doResult表示对于返回结果的处理调用
 * @date 13-10-7
 * @Version V1.0
 * Created by Winnid on 13-10-7.
 */
public class X3SimpleProgressBar implements DialogInterface.OnClickListener{
    private Context mContext;
    private ProgressDialog mProgressDialog;
    private String title;
    private String message;
    private boolean btnBackgroundVisible;
    private String btnBackgroundTitle;
    private boolean cancelable;
    private Handler mHandler;

    /**
     * 默认
     * message:中间的消息="正在处理，请稍后..."
     * btnBackgroundVisible:是否显示隐藏到后台按钮 =不显示;
     * btnBackgroundTitle:隐藏到后台按钮标题 =不显示;
     * cancelable：是否可以被取消 =不可以;
     * */
    public X3SimpleProgressBar(Context context) {
        initParam(context, null, false, null, false);
    }
    /**
     * cancelable：是否可以被取消;
     * 默认
     * message:中间的消息="正在处理，请稍后..."
     * btnBackgroundVisible:是否显示隐藏到后台按钮 =不显示;
     * btnBackgroundTitle:隐藏到后台按钮标题 =不显示;
     * */
    public X3SimpleProgressBar(Context context, Boolean cancelable) {
        initParam(context, null, false, null, cancelable);
    }
    /**
     * cancelable：是否可以被取消;
     * btnBackgroundVisible:是否显示隐藏到后台按钮;
     * 默认
     * message:中间的消息="正在处理，请稍后..."
     * btnBackgroundTitle:隐藏到后台按钮标题 =不显示;
     * */
    public X3SimpleProgressBar(Context context, Boolean btnBackgroundVisible, Boolean cancelable) {
        initParam(context, null, btnBackgroundVisible, null, cancelable);
    }
    /**
     * btnBackgroundTitle:隐藏到后台按钮标题;
     * 默认
     * message:中间的消息="正在处理，请稍后..."
     * cancelable：是否可以被取消 =不可以;
     * btnBackgroundVisible:是否显示隐藏到后台按钮=显示;
     * */
    public X3SimpleProgressBar(Context context, String btnBackgroundTitle) {
        initParam(context, btnBackgroundTitle, false, null, false);
    }
    /**
     * message:中间的消息="正在处理，请稍后..."
     * btnBackgroundVisible:是否显示隐藏到后台按钮;
     * btnBackgroundTitle:隐藏到后台按钮标题
     * cancelable：是否可以被取消
     * */
    public X3SimpleProgressBar(Context context, String message, Boolean btnBackgroundVisible, String btnBackgroundTitle, Boolean cancelable) {
        initParam(context, message, btnBackgroundVisible, btnBackgroundTitle, cancelable);
    }

    private void initParam(Context context, String message, Boolean btnBackgroundVisible, String btnBackgroundTitle, Boolean cancelable) {
        this.mContext = context;
        this.title = "请稍后...";
        this.message = (message == null) ? "正在处理，请稍后...":message;
        this.btnBackgroundVisible = btnBackgroundVisible == null ? false:true;
        this.btnBackgroundTitle = (btnBackgroundTitle == null) ? "隐藏到后台":btnBackgroundTitle;
        this.cancelable = cancelable;
        this.mHandler = new Handler();

        mProgressDialog = new ProgressDialog(mContext);//实例化
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);//设置进度条风格，风格为圆形，旋转的
        mProgressDialog.setTitle(title);//设置ProgressDialog 标题
        mProgressDialog.setMessage(message);//设置ProgressDialog 提示信息
        mProgressDialog.setIcon(R.drawable.icon_launcher);//设置ProgressDialog 标题图标
        if (btnBackgroundVisible) mProgressDialog.setButton(btnBackgroundTitle, this);//设置ProgressDialog 的一个Button
        mProgressDialog.setIndeterminate(true);//设置ProgressDialog 的进度条是否不明确
        mProgressDialog.setCancelable(false);//设置ProgressDialog 是否可以按退回按键取消
        mProgressDialog.show();
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        mProgressDialog.dismiss();
    }

    public void stop() {
        mProgressDialog.dismiss();
    }
}
