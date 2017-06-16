package com.smart.kitchen.smartkitchen.print.dialog;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
import com.smart.kitchen.smartkitchen.R;
import com.smart.kitchen.smartkitchen.adapters.BasicDialogAdapter;
import java.util.List;

public class BasicListDialog extends DialogFragment {
    public static final String BUNDLE_KEY_CONTENT_LIST = "listDiscount";
    public static final String BUNDLE_KEY_TITLE = "title";
    private ListView lvContent;
    private BasicDialogAdapter mAdapter;
    private Context mContext;
    private List<String> mList;
    private OnItemClickListener mListener;
    private String mTitle;
    private TextView tvTitle;

    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.mContext = activity;
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        Bundle arguments = getArguments();
        this.mTitle = arguments.getString(BUNDLE_KEY_TITLE);
        this.mList = arguments.getStringArrayList(BUNDLE_KEY_CONTENT_LIST);
    }

    public Dialog onCreateDialog(Bundle bundle) {
        View inflate = getActivity().getLayoutInflater().inflate(R.layout.dialog_list_basic, null);
        initView(inflate);
        setListener();
        setAdapter();
        Builder builder = new Builder(this.mContext);
        builder.setView(inflate).setCancelable(true).setNegativeButton(R.string.cancel, null);
        return builder.create();
    }

    private void initView(View view) {
        this.tvTitle = (TextView) view.findViewById(R.id.tv_dialog_list_basic_title);
        this.lvContent = (ListView) view.findViewById(R.id.lv_dialog_list_basic_content);
        this.tvTitle.setText(this.mTitle);
    }

    private void setListener() {
        this.lvContent.setOnItemClickListener(this.mListener);
    }

    private void setAdapter() {
        this.mAdapter = new BasicDialogAdapter(this.mContext, this.mList);
        this.lvContent.setAdapter(this.mAdapter);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.mListener = onItemClickListener;
    }
}
