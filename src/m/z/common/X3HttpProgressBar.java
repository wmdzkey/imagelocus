package m.z.common;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.http.client.RequestParams;
import com.lidroid.xutils.util.LogUtils;
import m.z.imagelocus.R;

import java.util.Map;

/**
 * @author Winnid
 * @Title:带有回调函数的进度条(HTTP版)
 * @Description: doWork表示方法处理过程<br>doResult表示对于返回结果的处理调用
 * @date 13-10-7
 * @Version V1.0
 * Created by Winnid on 13-10-7.
 */
public abstract class X3HttpProgressBar implements DialogInterface.OnClickListener{
    private Context mContext;
    private ProgressDialog mProgressDialog;
    private String title;
    private String message;
    private boolean btnBackgroundVisible;
    private String btnBackgroundTitle;
    private boolean cancelable;
    private Handler mHandler;
    private String result;

    private RequestParams params;
    private HttpUtils http;

    /**
     * url：访问的链接
     * httpMethod：请求方法
     * urlParams：？后面的参数
     * postParams：post方法的参数
     * 默认
     * message:中间的消息="正在处理，请稍后..."
     * btnBackgroundVisible:是否显示隐藏到后台按钮 =不显示;
     * btnBackgroundTitle:隐藏到后台按钮标题 =不显示;
     * cancelable：是否可以按退回按键取消 =不可以;
     * */
    public X3HttpProgressBar(Context context, String url, HttpRequest.HttpMethod httpMethod, RequestParams params) {
        initParam(context, null, false, null, false);
        initProgressDialog(url,httpMethod,params);
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

    private void initProgressDialog(String url, HttpRequest.HttpMethod httpMethod, RequestParams params) {
        //添加异步操作
        HttpUtils http = new HttpUtils();
        http.send(httpMethod,
                url,
                params,
                new RequestCallBack<String>() {

                    @Override
                    public void onStart() {
                        LogUtils.e("conn...");
                        doStart();
                    }

                    @Override
                    public void onLoading(long total, long current) {
                        LogUtils.e(current + "/" + total);
                        doLoading(total,current);
                    }

                    @Override
                    public void onSuccess(String result) {
                        LogUtils.e("upload response:" + result);
                        doSuccess(result);
                        mProgressDialog.dismiss();
                    }

                    @Override
                    public void onFailure(HttpException error, String msg) {
                        LogUtils.e(error.getExceptionCode() + ":" + msg);
                        doFailure(error, msg);
                        mProgressDialog.dismiss();
                    }
                });
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        mProgressDialog.dismiss();
    }

    public void doStart(){

    }
    public void doLoading(long total, long current){

    }
    public void doFailure(HttpException error, String msg) {
        CommonView.displayShort(mContext, "请求错误");
    }

    abstract public void doSuccess(String result);

}
