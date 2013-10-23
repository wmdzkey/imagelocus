package m.z.util;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.http.client.RequestParams;
import com.lidroid.xutils.util.LogUtils;
import m.z.common.CommonView;
import m.z.imagelocus.R;

/**
 * @author Winnid
 * @Title:带有回调函数的HTTP请求(HTTP版)
 * @Description: doWork表示方法处理过程<br>doResult表示对于返回结果的处理调用
 * @date 13-10-7
 * @Version V1.0
 * Created by Winnid on 13-10-7.
 */
public abstract class X3HttpUtil {

    private Context mContext;

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
    public X3HttpUtil(Context context, String url, HttpRequest.HttpMethod httpMethod, RequestParams params) {
        this.mContext = context;
        this.params = params;
        http = new HttpUtils();
        initProgressDialog(url,httpMethod,params);
    }

    private void initProgressDialog(String url, HttpRequest.HttpMethod httpMethod, RequestParams params) {
        //添加异步操作
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
                    }

                    @Override
                    public void onFailure(HttpException error, String msg) {
                        LogUtils.e(error.getExceptionCode() + ":" + msg);
                        doFailure(error, msg);
                    }
                });
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
