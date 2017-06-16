package com.smart.kitchen.smartkitchen.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import com.smart.kitchen.smartkitchen.BaseFragmentActivity;
import com.smart.kitchen.smartkitchen.R;
import com.smart.kitchen.smartkitchen.adapters.ABaseFragmentPager;
import com.smart.kitchen.smartkitchen.entitys.TableArea;
import com.smart.kitchen.smartkitchen.fragments.SettingOtherFragment;
import com.smart.kitchen.smartkitchen.fragments.SettingPrintFragment;
import com.smart.kitchen.smartkitchen.mvp.presenter.LocalSettingPresenter;
import com.smart.kitchen.smartkitchen.mvp.view.LocalSettingView;
import com.smart.kitchen.smartkitchen.utils.FinishActivity;
import java.util.ArrayList;
import java.util.List;

public class LocalSettingActivity extends BaseFragmentActivity implements LocalSettingView {
    private final String HardwareSetting = "打印设置";
    private final String OtherSetting = "其他设置";
    private ImageView imgBack;
    private List<String> list_title;
    private LocalSettingPresenter presenter;
    private SettingOtherFragment settingOtherFragment = SettingOtherFragment.newInstance();
    private SettingPrintFragment settingPrintFrangment = SettingPrintFragment.newInstance();
    private SettingTabAdapter settingTabAdapter;
    private TabLayout tabTitle;
    private ViewPager viewPager;

    private class SettingTabAdapter extends ABaseFragmentPager<String> {
        public SettingTabAdapter(List<String> list, FragmentManager fragmentManager) {
            super(list, fragmentManager);
        }

        public Fragment getItem(int i, String str) {
            if (str.equals("打印设置")) {
                return LocalSettingActivity.this.settingPrintFrangment;
            }
            if (str.equals("其他设置")) {
                return LocalSettingActivity.this.settingOtherFragment;
            }
            return null;
        }

        public CharSequence getPageTitle(int i) {
            return (CharSequence) getData().get(i);
        }
    }

    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_local_setting);
        FinishActivity.add(this);
    }

    protected void initView() {
        this.tabTitle = (TabLayout) findViewById(R.id.activity_setting_tab);
        this.viewPager = (ViewPager) findViewById(R.id.fragment_setting_vp);
        this.imgBack = (ImageView) findViewById(R.id.img_back);
        this.tabTitle.setBackgroundColor(getResources().getColor(R.color.defaultColor));
        this.tabTitle.setTabTextColors(getResources().getColor(R.color.gray), getResources().getColor(R.color.white));
        this.tabTitle.setSelectedTabIndicatorHeight(0);
        this.list_title = new ArrayList();
        this.list_title.add("打印设置");
        this.list_title.add("其他设置");
        this.settingTabAdapter = new SettingTabAdapter(this.list_title, getSupportFragmentManager());
        this.viewPager.setAdapter(this.settingTabAdapter);
        this.tabTitle.setupWithViewPager(this.viewPager);
    }

    protected void initEvent() {
        this.imgBack.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                LocalSettingActivity.this.finish();
            }
        });
    }

    protected void initData() {
        this.presenter = new LocalSettingPresenter(this.context, this);
        this.presenter.getTableAreaFromNET();
    }

    public void ShowTableArea(List<TableArea> list) {
        this.settingOtherFragment.upDateArea(list);
    }
}
