package m.z.imagelocus.activity.map;

import android.app.Activity;
import android.os.Bundle;
import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.EActivity;
import m.z.imagelocus.R;

/**
 * Created by Winnid on 13-10-22.
 */
@EActivity(R.layout.activity_map)
public class MapActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @AfterViews
    void init() {
    }
}