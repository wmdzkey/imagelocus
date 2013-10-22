package m.z.imagelocus.activity.map;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;
import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.EActivity;
import com.googlecode.androidannotations.annotations.NoTitle;
import com.googlecode.androidannotations.annotations.ViewById;
import m.z.common.CommonView;
import m.z.imagelocus.R;
import m.z.imagelocus.config.SystemAdapter;
import m.z.imagelocus.adapter.map.LbsHistoryAdapter;
import m.z.imagelocus.entity.Lbs;
import m.z.imagelocus.service.LbsService;
import m.z.imagelocus.service.Service;

import java.util.ArrayList;
import java.util.List;

@NoTitle
@EActivity(R.layout.activity_lbshistory)
public class LbsHistoryActivity extends Activity {

    LbsService lbsServive;

    @ViewById(R.id.lv_lbshistory)
    ListView lv_lbshistory;

    private LbsHistoryAdapter lbsHistoryAdapter;
    private List<Lbs> lbsList = new ArrayList<Lbs>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @AfterViews
    void init() {

        lbsList = Service.lbsService.findByUser_id(SystemAdapter.currentUser.getId());
        if(lbsList == null) {
            lbsList = new ArrayList<Lbs>();
        }
        lbsHistoryAdapter = new LbsHistoryAdapter(this, lbsList);
        lv_lbshistory.setAdapter(lbsHistoryAdapter);
        CommonView.displayShort(this, "已读取历史记录");
    }

}