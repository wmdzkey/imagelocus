package m.z.imagelocus.activity.demo;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.widget.Button;
import com.googlecode.androidannotations.annotations.*;
import m.z.common.CommonView;
import m.z.common.X3ProgressBar;
import m.z.imagelocus.R;

/**
 * 登陆界面activity
 */
@NoTitle
@EActivity(R.layout.activity_demo_x3pgb)
public class X3ProgressBarActivity extends Activity implements DialogInterface.OnClickListener {

    @ViewById(R.id.btn_setTags)
    Button btn_setTags;
    @ViewById(R.id.btn_delTags)
    Button btn_delTags;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @AfterViews
    void init() {
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        CommonView.displayShort(this, "后台开始下载");
        m_Dialog.dismiss();
    }

    public ProgressDialog m_Dialog;
    public int waitResult = 0;
    @Click(R.id.btn_setTags)
    void btn_setTags_onClick() {

        new X3ProgressBar<Boolean>(this) {
            @Override
            public Boolean doWork() {
                    int i;
                    for(i=0; i<100; i++) {
                        try {
                            Thread.sleep(100);
                        } catch (InterruptedException e) {
                            return false;
                        }
                    }
                    return true;
            }
            @Override
            public void doResult(Boolean result) {
                if(result) {
                    CommonView.displayShort(X3ProgressBarActivity.this, "执行完成");
                } else {
                    CommonView.displayShort(X3ProgressBarActivity.this, "执行失败");
                }
            }
        };

//        //添加异步操作
//        m_Dialog=new ProgressDialog(this);
//        //实例化
//        m_Dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
//        //设置进度条风格，风格为圆形，旋转的
//        m_Dialog.setTitle("请等待...");
//        //设置ProgressDialog 标题
//        m_Dialog.setMessage("正在下载文件，请稍后...");
//        //设置ProgressDialog 提示信息
//        m_Dialog.setIcon(R.drawable.icon);
//        //设置ProgressDialog 标题图标
//        m_Dialog.setButton("后台下载",this);
//        //设置ProgressDialog 的一个Button
//        m_Dialog.setIndeterminate(false);
//        //设置ProgressDialog 的进度条是否不明确
//        m_Dialog.setCancelable(true);
//        //设置ProgressDialog 是否可以按退回按键取消
//        m_Dialog.show();
//        final Handler mHandler = new Handler();
//        new Thread(new Runnable(){
//            @Override
//            public void run() {
//                //加载数据
//                try{
//                    Thread.sleep(8000);
//                    waitResult = 1;
//                } catch(Exception ex){
//                    waitResult = -1;
//                }
//                //更新界面
//                mHandler.post(new Runnable() {
//                    public void run() {
//                        if(waitResult==1)
//                            CommonView.displayShort(X3ProgressBarActivity.this, "下载完成");
//                        else if(waitResult==-1)
//                            CommonView.displayShort(X3ProgressBarActivity.this, "下载文件失败,请检查网络连接");
//                    }
//                });
//                m_Dialog.dismiss();
//            }}).start();
    }

    @Click(R.id.btn_delTags)
    void btn_delTags_onClick() {
        waitResult = 2;
        CommonView.displayShort(X3ProgressBarActivity.this, "被干掉了");
    }
}
