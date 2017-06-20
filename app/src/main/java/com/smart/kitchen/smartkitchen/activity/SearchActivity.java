package com.smart.kitchen.smartkitchen.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import com.smart.kitchen.smartkitchen.BaseFragmentActivity;
import com.smart.kitchen.smartkitchen.R;
import com.smart.kitchen.smartkitchen.entitys.VipInfo;
import com.smart.kitchen.smartkitchen.fragments.AddVipFragment;
import com.smart.kitchen.smartkitchen.fragments.VIPInfoFragment;
import com.smart.kitchen.smartkitchen.fragments.VipPayFragment;
import com.smart.kitchen.smartkitchen.mvp.presenter.SearchPresenter;
import com.smart.kitchen.smartkitchen.mvp.view.SearchView;
import com.smart.kitchen.smartkitchen.popwindow.DialogUtils;
import com.smart.kitchen.smartkitchen.popwindow.DialogUtils.OnClickListener;
import com.smart.kitchen.smartkitchen.utils.AuthorityUtil;
import com.smart.kitchen.smartkitchen.utils.Contants;
import com.smart.kitchen.smartkitchen.utils.FinishActivity;
import com.smart.kitchen.smartkitchen.utils.LogUtils;
import com.smart.kitchen.smartkitchen.utils.Toasts;
import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends BaseFragmentActivity implements SearchView {
    private static final String TAG = "SearchActivity";
    private AddVipFragment addVipFragment;
    private DialogUtils dialogUtils;
    private EditText etSearch;
    private List<Fragment> fragments = new ArrayList();
    private SearchPresenter presenter;
    private VIPInfoFragment vipInfoFragmrnt;
    private VipPayFragment vipPayFragment;

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_search);
        FinishActivity.add(this);
    }

    @Override
    protected void initView() {
        this.etSearch = (EditText) findViewById(R.id.et_search);
    }

    public void onSearch(View view) {
        search();
    }

    private void search() {
        if (Contants.isMobileNO(getInput())) {
            this.presenter.search(getInput());
        }
    }

    @Override
    protected void initEvent() {
        this.presenter = new SearchPresenter(this, this);
        this.dialogUtils = new DialogUtils(this);
        this.etSearch.addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                SearchActivity.this.search();
            }

            public void afterTextChanged(Editable editable) {
            }
        });
    }

    public void hide() {
        FragmentTransaction beginTransaction = getSupportFragmentManager().beginTransaction();
        hideFragment(beginTransaction);
        beginTransaction.commit();
    }

    @Override
    protected void initData() {
    }

    public void showVipPayFragment(VipInfo vipInfo) {
        FragmentTransaction beginTransaction = getSupportFragmentManager().beginTransaction();
        hideFragment(beginTransaction);
        if (this.fragments.contains(this.vipPayFragment)) {
            beginTransaction.show(this.vipPayFragment);
        } else {
            this.vipPayFragment = new VipPayFragment();
            this.fragments.add(this.vipPayFragment);
            beginTransaction.add((int) R.id.main_frag, this.vipPayFragment);
        }
        this.vipPayFragment.setVipInfo(vipInfo);
        beginTransaction.commit();
    }

    private void showVipFragment(VipInfo vipInfo) {
        FragmentTransaction beginTransaction = getSupportFragmentManager().beginTransaction();
        hideFragment(beginTransaction);
        if (this.fragments.contains(this.vipInfoFragmrnt)) {
            beginTransaction.show(this.vipInfoFragmrnt);
        } else {
            this.vipInfoFragmrnt = new VIPInfoFragment();
            this.fragments.add(this.vipInfoFragmrnt);
            beginTransaction.add((int) R.id.main_frag, this.vipInfoFragmrnt);
        }
        this.vipInfoFragmrnt.showVipInfo(vipInfo);
        beginTransaction.commit();
    }

    private void addVipFragment() {
        FragmentTransaction beginTransaction = getSupportFragmentManager().beginTransaction();
        hideFragment(beginTransaction);
        if (this.fragments.contains(this.addVipFragment)) {
            beginTransaction.show(this.addVipFragment);
        } else {
            this.addVipFragment = new AddVipFragment();
            this.fragments.add(this.addVipFragment);
            beginTransaction.add((int) R.id.main_frag, this.addVipFragment);
        }
        beginTransaction.commit();
    }

    private void hideFragment(FragmentTransaction fragmentTransaction) {
        for (int i = 0; i < this.fragments.size(); i++) {
            if (this.fragments.get(i) != null) {
                fragmentTransaction.hide((Fragment) this.fragments.get(i));
            }
        }
    }

    @Override
    public void onSuccess(VipInfo vipInfo) {
        if (vipInfo == null) {
            this.dialogUtils.showConfirm(this.etSearch, "会员不存在", new String[]{"添加", "取消"}, new OnClickListener() {
                public void onClick(Object obj) {
                    if (AuthorityUtil.getInstance().getRoleFlag() == 2) {
                        SearchActivity.this.addVipFragment();
                    } else if (AuthorityUtil.getInstance().permitAddVip()) {
                        SearchActivity.this.addVipFragment();
                    } else {
                        Toasts.show(SearchActivity.this.context, SearchActivity.this.getResources().getString(R.string.no_authority));
                    }
                }
            }, null);
            return;
        }
        showVipFragment(vipInfo);
    }

    @Override
    public void onFial(String str) {
        LogUtils.d("vip", str);
    }

    @Override
    public String getInput() {
        return this.etSearch.getText().toString().trim();
    }
}
