package m.z.imagelocus.activity.impress;

import android.app.Activity;
import android.os.Bundle;
import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.EActivity;
import com.googlecode.androidannotations.annotations.NoTitle;
import m.z.imagelocus.R;

/**
 * 登陆界面activity
 */
@NoTitle
@EActivity(R.layout.activity_impress)
public class ImpressActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @AfterViews
    void init() {
    }
}
