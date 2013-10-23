package m.z.imagelocus.activity;

import android.app.Activity;
import android.app.LocalActivityManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.EActivity;
import com.googlecode.androidannotations.annotations.NoTitle;
import com.googlecode.androidannotations.annotations.ViewById;
import m.z.imagelocus.R;
import m.z.imagelocus.activity.friend.FriendActivity_;
import m.z.imagelocus.activity.impress.ImpressActivity_;
import m.z.imagelocus.activity.map.MapActivity_;
import m.z.imagelocus.activity.setting.SettingActivity;

import java.util.ArrayList;

@NoTitle
@EActivity(R.layout.activity_main)
public class MainActivity extends Activity {

    public static MainActivity instance = null;
    public static LocalActivityManager manager = null;

    @ViewById(R.id.tabpager)
    ViewPager mTabPager;//容器

    @ViewById(R.id.ll_friends)
    LinearLayout mTab1;
    @ViewById(R.id.ll_maps)
    LinearLayout mTab2;
    @ViewById(R.id.ll_impress)
    LinearLayout mTab3;
    @ViewById(R.id.ll_settings)
    LinearLayout mTab4;

    @ViewById(R.id.iv_tab_now)
    ImageView mTabImg;//绿光动画图片
    @ViewById(R.id.iv_friends)
    ImageView mTabImg1;
    @ViewById(R.id.iv_maps)
    ImageView mTabImg2;
    @ViewById(R.id.iv_impress)
    ImageView mTabImg3;
    @ViewById(R.id.iv_settings)
    ImageView mTabImg4;

    private int currIndex = 0;// 当前页卡编号
    private int zero = 0;// 动画图片偏移量
    private int one;// 单个水平动画位移
    private int two;
    private int three;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);// 启动activity时不自动弹出软键盘
        instance = this;
        manager = new LocalActivityManager(this , true);
        manager.dispatchCreate(savedInstanceState);
    }

    @AfterViews
    void init() {

        mTabPager.setOnPageChangeListener(new MyOnPageChangeListener());
        mTab1.setOnClickListener(new MyOnClickListener(0));
        mTab2.setOnClickListener(new MyOnClickListener(1));
        mTab3.setOnClickListener(new MyOnClickListener(2));
        mTab4.setOnClickListener(new MyOnClickListener(3));

        Display currDisplay = getWindowManager().getDefaultDisplay();// 获取屏幕当前分辨率
        int displayWidth = currDisplay.getWidth();
        one = displayWidth / 4; // 设置水平动画平移大小
        two = one * 2;
        three = one * 3;

        // 将要分页显示的View装入数组中
        final ArrayList<View> views = new ArrayList<View>();
        views.add(getView("FRIEND", FriendActivity_.class));
        views.add(getView("MAP", MapActivity_.class));
        views.add(getView("IMPRESS", ImpressActivity_.class));
        views.add(getView("SETTING",  SettingActivity.class));

        // 填充ViewPager的数据适配器
        PagerAdapter mPagerAdapter = new PagerAdapter() {

            @Override
            public boolean isViewFromObject(View arg0, Object arg1) {
                return arg0 == arg1;
            }

            @Override
            public int getCount() {
                return views.size();
            }

            @Override
            public Object instantiateItem(View container, int position) {
                ((ViewPager) container).addView(views.get(position));
                return views.get(position);
            }

            @Override
            public void destroyItem(View container, int position, Object object) {
                ((ViewPager) container).removeView(views.get(position));
            }
        };
        mTabPager.setAdapter(mPagerAdapter);
    }

    /**
     * 图标点击监听
     */
    public class MyOnClickListener implements View.OnClickListener {
        private int index = 0;

        public MyOnClickListener(int i) {
            index = i;
        }

        public void onClick(View v) {
            mTabPager.setCurrentItem(index);
        }
    };

    /*
     * 页卡切换监听(原作者:D.Winter)
     */
    public class MyOnPageChangeListener implements OnPageChangeListener {

        public void onPageSelected(int arg0) {
            Animation animation = null;
            switch (arg0) {
                case 0:
                    mTabImg1.setImageDrawable(getResources().getDrawable(
                            R.drawable.tab_weixin_pressed));
                    if (currIndex == 1) {
                        animation = new TranslateAnimation(one, 0, 0, 0);
                        mTabImg2.setImageDrawable(getResources().getDrawable(
                                R.drawable.tab_find_frd_normal));
                    } else if (currIndex == 2) {
                        animation = new TranslateAnimation(two, 0, 0, 0);
                        mTabImg3.setImageDrawable(getResources().getDrawable(
                                R.drawable.tab_address_normal));
                    } else if (currIndex == 3) {
                        animation = new TranslateAnimation(three, 0, 0, 0);
                        mTabImg4.setImageDrawable(getResources().getDrawable(
                                R.drawable.tab_settings_normal));
                    }
                    break;
                case 1:
                    mTabImg2.setImageDrawable(getResources().getDrawable(
                            R.drawable.tab_find_frd_pressed));
                    if (currIndex == 0) {
                        animation = new TranslateAnimation(zero, one, 0, 0);
                        mTabImg1.setImageDrawable(getResources().getDrawable(
                                R.drawable.tab_weixin_normal));
                    } else if (currIndex == 2) {
                        animation = new TranslateAnimation(two, one, 0, 0);
                        mTabImg3.setImageDrawable(getResources().getDrawable(
                                R.drawable.tab_address_normal));
                    } else if (currIndex == 3) {
                        animation = new TranslateAnimation(three, one, 0, 0);
                        mTabImg4.setImageDrawable(getResources().getDrawable(
                                R.drawable.tab_settings_normal));
                    }
                    break;
                case 2:
                    mTabImg3.setImageDrawable(getResources().getDrawable(
                            R.drawable.tab_address_pressed));
                    if (currIndex == 0) {
                        animation = new TranslateAnimation(zero, two, 0, 0);
                        mTabImg1.setImageDrawable(getResources().getDrawable(
                                R.drawable.tab_weixin_normal));
                    } else if (currIndex == 1) {
                        animation = new TranslateAnimation(one, two, 0, 0);
                        mTabImg2.setImageDrawable(getResources().getDrawable(
                                R.drawable.tab_find_frd_normal));
                    } else if (currIndex == 3) {
                        animation = new TranslateAnimation(three, two, 0, 0);
                        mTabImg4.setImageDrawable(getResources().getDrawable(
                                R.drawable.tab_settings_normal));
                    }
                    break;
                case 3:
                    mTabImg4.setImageDrawable(getResources().getDrawable(
                            R.drawable.tab_settings_pressed));
                    if (currIndex == 0) {
                        animation = new TranslateAnimation(zero, three, 0, 0);
                        mTabImg1.setImageDrawable(getResources().getDrawable(
                                R.drawable.tab_weixin_normal));
                    } else if (currIndex == 1) {
                        animation = new TranslateAnimation(one, three, 0, 0);
                        mTabImg2.setImageDrawable(getResources().getDrawable(
                                R.drawable.tab_find_frd_normal));
                    } else if (currIndex == 2) {
                        animation = new TranslateAnimation(two, three, 0, 0);
                        mTabImg3.setImageDrawable(getResources().getDrawable(
                                R.drawable.tab_address_normal));
                    }
                    break;
            }
            currIndex = arg0;
            animation.setFillAfter(true);// True:图片停在动画结束位置
            animation.setDuration(150);// 动画持续时间
            mTabImg.startAnimation(animation);// 开始动画
        }

        @Override
        public void onPageScrollStateChanged(int i) {

        }

        @Override
        public void onPageScrolled(int i, float v, int i2) {

        }
    }

    /**
     * 为启动Activity初始化Intent信息
     * @param cls
     * @return
     */
    private Intent initIntent(Class<?> cls){
        return new Intent(this,    cls).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
    }

    /**
     * 供开发者在实现类中调用，能将Activity容器内的Activity移除，再将指定的某个Activity加入
     * @param activityName 加载的Activity在localActivityManager中的名字
     * @param activityClassTye    要加载Activity的类型
     */
    protected View getView(String activityName, Class<?> activityClassTye){
        //移除内容部分全部的View
        Activity contentActivity = manager.getActivity(activityName);
        if (null == contentActivity) {
            manager.startActivity(activityName, initIntent(activityClassTye));
        }
        //加载Activity
        return  manager.getActivity(activityName).getWindow().getDecorView();
    }
}