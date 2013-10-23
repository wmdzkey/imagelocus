package m.z.imagelocus.activity.impress;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import com.googlecode.androidannotations.annotations.*;
import m.z.imagelocus.R;
import m.z.imagelocus.activity.map.MapFriendActivity_;

/**
 * 登陆界面activity
 */
@NoTitle
@EActivity(R.layout.activity_impress)
public class ImpressActivity extends Activity {

    public static ImpressActivity instance = null;

    //左上角第一个按钮
    @ViewById(R.id.btn_left)
    Button btn_left;
    //中间标题
    @ViewById(R.id.tv_middle)
    TextView tv_middle;
    //右上角第一个按钮
    @ViewById(R.id.btn_right)
    Button btn_right;

    @ViewById(R.id.btn_metor_1)
    Button btn_metor_1;
    @ViewById(R.id.tv_metor_title_1)
    TextView tv_metor_title_1;
    @ViewById(R.id.tv_metor_content_1)
    TextView tv_metor_content_1;
    @ViewById(R.id.btn_metor_2)
    Button btn_metor_2;
    @ViewById(R.id.tv_metor_title_2)
    TextView tv_metor_title_2;
    @ViewById(R.id.tv_metor_content_2)
    TextView tv_metor_content_2;
    @ViewById(R.id.btn_metor_3)
    Button btn_metor_3;
    @ViewById(R.id.tv_metor_title_3)
    TextView tv_metor_title_3;
    @ViewById(R.id.tv_metor_content_3)
    TextView tv_metor_content_4;
    @ViewById(R.id.btn_metor_4)
    Button btn_metor_4;
    @ViewById(R.id.tv_metor_title_4)
    TextView tv_metor_title_4;
    @ViewById(R.id.tv_metor_content_4)
    TextView tv_metor_content_;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        instance = this;
    }

    @AfterViews
    void init() {
        tv_middle.setText("印象");

        tv_metor_title_1.setText("我的位置");
        tv_metor_content_1.setText("看看我在哪");
        tv_metor_title_2.setText("我的印象");
        tv_metor_content_2.setText("看看我的印象中到过哪");
    }


    @Click(R.id.btn_metor_1)
    void btn_metor_1_onClick() {
        Intent _intent = new Intent(instance, ImpressMeActivity_.class);
        startActivity(_intent);
    }
    @Click(R.id.btn_metor_2)
    void btn_metor_2_onClick() {
        Intent _intent = new Intent(instance, ImpressHistoryActivity_.class);
        startActivity(_intent);
    }
    @Click(R.id.btn_metor_3)
    void btn_metor_3_onClick() {
    }
    @Click(R.id.btn_metor_4)
    void btn_metor_4_onClick() {
    }
}
