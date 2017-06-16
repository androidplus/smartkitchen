package com.smart.kitchen.smartkitchen.adapters;

import android.content.Context;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.TextView;
import com.smart.kitchen.smartkitchen.R;
import com.smart.kitchen.smartkitchen.entitys.GoodSize;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CheckDialogAdapter extends BaseAdapter {
    private Context context;
    private List<GoodSize> liGoodsize;
    private Map<String, String> numMap = new HashMap();

    protected class ViewHolder {
        private EditText etCheck;
        int hposition;
        private TextView tvCheckFormer;
        private TextView tvCheckStandard;
        private TextView txCheckNew;
        private View v;

        public ViewHolder(View view) {
            this.tvCheckStandard = (TextView) view.findViewById(R.id.tv_check_standard);
            this.tvCheckFormer = (TextView) view.findViewById(R.id.tv_check_former);
            this.txCheckNew = (TextView) view.findViewById(R.id.tx_check_new);
            this.etCheck = (EditText) view.findViewById(R.id.et_check);
            this.v = view.findViewById(R.id.view_check);
            this.etCheck.addTextChangedListener(new TextWatcher() {
                CharSequence tmp = "";

                public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                }

                public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                    this.tmp = charSequence;
                }

                public void afterTextChanged(Editable editable) {
                    if (this.tmp.length() > 0) {
                        String substring = this.tmp.toString().substring(0, 1);
                        if (this.tmp.length() >= 2 && substring.equals("0")) {
                            editable.delete(0, 1);
                            ViewHolder.this.etCheck.setText(editable);
                            ViewHolder.this.etCheck.setSelection(this.tmp.length());
                        }
                    }
                    CheckDialogAdapter.this.numMap.put(((GoodSize) CheckDialogAdapter.this.liGoodsize.get(ViewHolder.this.hposition)).getId() + "", ViewHolder.this.etCheck.getText().toString().trim());
                }
            });
            this.etCheck.setOnFocusChangeListener(new OnFocusChangeListener() {
                public void onFocusChange(View view, boolean z) {
                    if (!z) {
                        if (TextUtils.isEmpty(ViewHolder.this.etCheck.getText().toString())) {
                            ViewHolder.this.etCheck.setText("0");
                        }
                        CheckDialogAdapter.this.numMap.put(((GoodSize) CheckDialogAdapter.this.liGoodsize.get(ViewHolder.this.hposition)).getId() + "", ViewHolder.this.etCheck.getText().toString().trim());
                    }
                }
            });
        }
    }

    public Map<String, String> getNumMap() {
        return this.numMap;
    }

    public CheckDialogAdapter(Context context, List<GoodSize> list) {
        this.liGoodsize = list;
        this.context = context;
    }

    public int getCount() {
        return this.liGoodsize.size();
    }

    public Object getItem(int i) {
        return this.liGoodsize.get(i);
    }

    public long getItemId(int i) {
        return 0;
    }

    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder;
        if (view == null) {
            view = LayoutInflater.from(this.context).inflate(R.layout.adapter_checkdialog, null);
            ViewHolder viewHolder2 = new ViewHolder(view);
            view.setTag(viewHolder2);
            viewHolder = viewHolder2;
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        if (i != 0) {
            viewHolder.v.setVisibility(0);
        }
        viewHolder.hposition = i;
        viewHolder.etCheck.setVisibility(0);
        viewHolder.txCheckNew.setVisibility(8);
        viewHolder.tvCheckStandard.setText(((GoodSize) this.liGoodsize.get(i)).getSpec_name());
        viewHolder.tvCheckFormer.setText(((GoodSize) this.liGoodsize.get(i)).getCount() + "");
        if (this.numMap.get(((GoodSize) this.liGoodsize.get(i)).getId() + "") == null) {
            viewHolder.etCheck.setText("0");
        } else {
            viewHolder.etCheck.setText((CharSequence) this.numMap.get(((GoodSize) this.liGoodsize.get(i)).getId() + ""));
        }
        return view;
    }
}
