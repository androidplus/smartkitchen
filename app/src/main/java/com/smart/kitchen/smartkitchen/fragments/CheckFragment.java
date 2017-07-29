package com.smart.kitchen.smartkitchen.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import com.alibaba.fastjson.JSON;
import com.smart.kitchen.smartkitchen.BaseFragment;
import com.smart.kitchen.smartkitchen.R;
import com.smart.kitchen.smartkitchen.activity.CheckActivity;
import com.smart.kitchen.smartkitchen.adapters.CheckAdapter;
import com.smart.kitchen.smartkitchen.adapters.CheckAdapter.OnItemClickListener;
import com.smart.kitchen.smartkitchen.db.daoutils.UserInfoDaoManager;
import com.smart.kitchen.smartkitchen.entitys.FoodType;
import com.smart.kitchen.smartkitchen.entitys.Goods;
import com.smart.kitchen.smartkitchen.mvp.presenter.CheckPresenter;
import com.smart.kitchen.smartkitchen.mvp.view.CheckView;
import com.smart.kitchen.smartkitchen.popwindow.DialogUtils;
import com.smart.kitchen.smartkitchen.utils.Toasts;
import java.util.ArrayList;
import java.util.List;

public class CheckFragment extends BaseFragment implements OnItemClickListener, CheckView {
    private CheckAdapter adapter;
    private ListView checkInfolistView;
    private TextView checkNum;
    private List<Goods> goodsList = new ArrayList();
    private onNotityGoods notityGoods;
    private CheckPresenter presenter;
    private TextView tvCheckClear;
    private TextView tvCheckSubmit;

    public interface onNotityGoods {
        void notity();
    }

    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        return layoutInflater.inflate(R.layout.fragment_check, null);
    }

    protected void initView(View view) {
        this.checkInfolistView = (ListView) view.findViewById(R.id.checkInfolistView);
        this.checkNum = (TextView) view.findViewById(R.id.checkNum);
        this.tvCheckSubmit = (TextView) view.findViewById(R.id.tv_check_submit);
        this.tvCheckClear = (TextView) view.findViewById(R.id.tv_check_clear);
    }

    protected void initEvent() {
        this.presenter = new CheckPresenter(this.context, this);
        this.tvCheckSubmit.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                CheckFragment.this.presenter.submitCheck(JSON.toJSONString(CheckActivity.checkInfoMap), new UserInfoDaoManager().getNowUserInfo().getUserid());
            }
        });
        this.tvCheckClear.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                View view2 = view;
                CheckFragment.this.dialogUtils.showConfirm(view2, "确定要清空吗？", new String[]{"确定", "取消"}, new DialogUtils.OnClickListener() {
                    public void onClick(Object obj) {
                        CheckActivity.clear();
                    }
                }, null);
            }
        });
    }

    protected void initData() {
        addData();
    }

    public void addData() {
        this.goodsList.clear();
        for (int i = 0; i < CheckActivity.checkInfoMap.size(); i++) {
            this.goodsList.add((Goods) CheckActivity.checkInfoMap.get(i));
        }
        this.checkNum.setText("已盘点：" + CheckActivity.checkInfoMap.size() + "类");
        if (this.adapter == null) {
            this.adapter = new CheckAdapter(this.context, this.goodsList);
            this.adapter.setListener(this);
            this.checkInfolistView.setAdapter(this.adapter);
            return;
        }
        this.adapter.notifyDataSetChanged();
    }

    public void onItemClick(int i) {
        CheckActivity.remove(String.valueOf(((Goods) this.goodsList.get(i)).getId()));
    }

    public void onSuccess(List<FoodType> list) {
    }

    public void onFail() {
    }

    public void ShowFoodType(List<FoodType> list) {
    }

    public void inFrom(String str) {
        if ("onSuccess".equals(str)) {
            Toasts.show(this.context, "提交成功");
            CheckActivity.clear();
            if (this.notityGoods != null) {
                this.notityGoods.notity();
            }
        } else if ("onAlert".equals(str)) {
            Toasts.show(this.context, "提交出错,请重新提交");
        } else if ("onFailure".equals(str)) {
            Toasts.show(this.context, "提交失败,请重新提交");
        }
    }

    public void setOnNotityGoods(onNotityGoods com_smart_kitchen_smartkitchen_fragments_CheckFragment_onNotityGoods) {
        this.notityGoods = com_smart_kitchen_smartkitchen_fragments_CheckFragment_onNotityGoods;
    }
}
