package com.smart.kitchen.smartkitchen.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.fastjson.parser.Feature;
import com.smart.kitchen.smartkitchen.BaseFragmentActivity;
import com.smart.kitchen.smartkitchen.R;
import com.smart.kitchen.smartkitchen.adapters.TableAreaAdapter;
import com.smart.kitchen.smartkitchen.entitys.OrderGoods;
import com.smart.kitchen.smartkitchen.entitys.OrderInfo;
import com.smart.kitchen.smartkitchen.entitys.TableArea;
import com.smart.kitchen.smartkitchen.entitys.TableNumber;
import com.smart.kitchen.smartkitchen.fragments.TableFragment;
import com.smart.kitchen.smartkitchen.fragments.TableShopFragment;
import com.smart.kitchen.smartkitchen.mvp.presenter.TablePresenter;
import com.smart.kitchen.smartkitchen.mvp.view.TableView;
import com.smart.kitchen.smartkitchen.popwindow.DialogUtils;
import com.smart.kitchen.smartkitchen.popwindow.DialogUtils.DistoryPopuListener;
import com.smart.kitchen.smartkitchen.utils.FinishActivity;
import com.smart.kitchen.smartkitchen.utils.LogUtils;
import com.smart.kitchen.smartkitchen.utils.SingletonTableNumberList;
import com.smart.kitchen.smartkitchen.utils.StringTableNumber;
import com.smart.kitchen.smartkitchen.utils.Toasts;
import com.smart.kitchen.smartkitchen.view.MyGridView;
import com.smart.kitchen.smartkitchen.view.MySwitchButton;
import com.smart.kitchen.smartkitchen.view.MySwitchButton.OnChangeListener;
import java.util.ArrayList;
import java.util.List;

public class TableActivity extends BaseFragmentActivity implements OnClickListener, TableView {
    public static final int MAX_ONE_PAGE = 10;
    private static final String TAG = "TableActivity";
    private static EditText etTablePeople;
    public static List<TableArea> listTmp;
    public static List<OrderGoods> shoppingCarMap;
    private static List<TextView> tvList;
    private static TextView tvTables;
    private final int CHANGE_TABLE = 1;
    private String TYPE;
    private TableAreaAdapter adapter;
    private int currentIndex = 1;
    private int flag = 0;
    private List<TableFragment> fragmentList = new ArrayList();
    private FrameLayout frameLayout;
    private MyGridView gridView;
    Handler handler = new Handler() {
        public void handleMessage(Message message) {
            super.handleMessage(message);
            switch (message.what) {
                case 1:
                    ((TableFragment) message.obj).change();
                    return;
                default:
                    return;
            }
        }
    };
    private LinearLayout ivMsg;
    private List<TableArea> list;
    private LinearLayout llMenuRight;
    private RelativeLayout llSearch;
    private LinearLayout llTopRight;
    private OrderInfo orderInfo;
    private TablePresenter presenter;
    private MySwitchButton sbTable;
    private TableShopFragment shopFragment;
    private List<TableNumber> tableNumberList = new ArrayList();
    private List<TableNumber> tableNumbers = new ArrayList();
    private int totalPage = 0;
    private TextView tvBefore;
    private TextView tvNext;
    private TextView tvTableCancel;
    private TextView tvTableConfirm;
    private TextView tvTableInform;
    private TextView tvTablePeople1;
    private TextView tvTablePeople2;
    private TextView tvTablePeople3;
    private TextView tvTablePeople4;
    private TextView tvTablePeople5;

    public static void notifyPeopleTable() {
        listClear();
        List stringTableName = new StringTableNumber().getStringTableName(SingletonTableNumberList.getInstance().getSelectList());
        for (int i = 0; i < SingletonTableNumberList.getInstance().getSelectList().size(); i++) {
            if (i < 5) {
                ((TextView) tvList.get(i)).setVisibility(View.VISIBLE);
                ((TextView) tvList.get(i)).setText((CharSequence) stringTableName.get(i));
            }
        }
        tvTables.setText("共" + SingletonTableNumberList.getInstance().getSelectList().size() + "桌");
    }

    public static void listClear() {
        for (int i = 0; i < tvList.size(); i++) {
            ((TextView) tvList.get(i)).setVisibility(View.INVISIBLE);
        }
    }

    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_table);
        FinishActivity.add(this);
    }

    protected void initView() {
        this.frameLayout = (FrameLayout) findViewById(R.id.main_frag);
        this.gridView = (MyGridView) findViewById(R.id.gridView);
        this.tvBefore = (TextView) findViewById(R.id.tv_before);
        this.tvNext = (TextView) findViewById(R.id.tv_next);
        this.llMenuRight = (LinearLayout) findViewById(R.id.ll_menu_right);
        this.llSearch = (RelativeLayout) findViewById(R.id.ll_search);
        this.llTopRight = (LinearLayout) findViewById(R.id.ll_top_right);
        this.ivMsg = (LinearLayout) findViewById(R.id.iv_msg);
        this.tvTableInform = (TextView) findViewById(R.id.tv_table_inform);
        this.tvTablePeople1 = (TextView) findViewById(R.id.tv_table_people1);
        this.tvTablePeople2 = (TextView) findViewById(R.id.tv_table_people2);
        this.tvTablePeople3 = (TextView) findViewById(R.id.tv_table_people3);
        this.tvTablePeople4 = (TextView) findViewById(R.id.tv_table_people4);
        this.tvTablePeople5 = (TextView) findViewById(R.id.tv_table_people5);
        tvTables = (TextView) findViewById(R.id.tv_tables);
        this.sbTable = (MySwitchButton) findViewById(R.id.sb_table);
        this.tvTableConfirm = (TextView) findViewById(R.id.tv_table_confirm);
        this.tvTableCancel = (TextView) findViewById(R.id.tv_table_cancel);
        etTablePeople = (EditText) findViewById(R.id.et_table_people);
        this.sbTable.setOnChangeListener(new OnChangeListener() {
            public void onChange(boolean z) {
                if (TableActivity.this.fragmentList != null) {
                    for (int i = 0; i < TableActivity.this.fragmentList.size(); i++) {
                        if (TableActivity.this.fragmentList.get(i) != null) {
                            ((TableFragment) TableActivity.this.fragmentList.get(i)).OnChange();
                            TableActivity.listClear();
                        }
                    }
                }
            }
        });
        tvList = new ArrayList();
        tvList.add(this.tvTablePeople1);
        tvList.add(this.tvTablePeople2);
        tvList.add(this.tvTablePeople3);
        tvList.add(this.tvTablePeople4);
        tvList.add(this.tvTablePeople5);
        this.ivMsg.setOnClickListener(this);
        this.tvBefore.setOnClickListener(this);
        this.tvNext.setOnClickListener(this);
        this.tvTableConfirm.setOnClickListener(this);
        this.tvTableCancel.setOnClickListener(this);
    }

    protected void onResume() {
        super.onResume();
        receiverMessages(null);
    }

    public void onWindowFocusChanged(boolean z) {
        super.onWindowFocusChanged(z);
        if (z && this.flag == 0 && !this.TYPE.equals("OrderRight")) {
            this.dialogUtils.repastNumber(this.tvTableConfirm, new DialogUtils.OnClickListener() {
                public void onClick(Object obj) {
                    TableActivity.this.dialogUtils.setDistoryPopuMessage(new DistoryPopuListener() {
                        public void sendMessage(String str, String str2) {
                            TableActivity.etTablePeople.setText(str2);
                        }
                    });
                    InputMethodManager inputMethodManager = (InputMethodManager) TableActivity.this.getSystemService(INPUT_METHOD_SERVICE);
                    Log.e(TableActivity.TAG, "onClick: " + inputMethodManager.isActive());
                    if (inputMethodManager.isActive()) {
                        inputMethodManager.toggleSoftInput(1, 2);
                    }
                }
            }, null);
            this.flag = 1;
        }
    }

    protected void initEvent() {
        this.presenter = new TablePresenter(this.context, this, this.progressDialog);
        this.orderInfo = (OrderInfo) getIntent().getSerializableExtra("ORD");
        this.TYPE = getIntent().getExtras().getString("TYPE");
        shoppingCarMap = (List) JSON.parseObject(this.orderInfo.getGoodslist(), new TypeReference<List<OrderGoods>>() {
        }, new Feature[0]);
        if (this.TYPE.equals("OrderRight") && this.orderInfo.getTablenumber() != null) {
            etTablePeople.setText(this.orderInfo.getUsersnum().toString());
            List gsonTableNum = new StringTableNumber().getGsonTableNum(this.orderInfo.getTablenumber());
            if (gsonTableNum.size() > 1) {
                this.sbTable.setText(true);
                this.sbTable.startAnim();
            }
            SingletonTableNumberList.getInstance().getSelectList().addAll(gsonTableNum);
            this.tableNumberList.addAll(gsonTableNum);
            tvTables.setText(String.valueOf(gsonTableNum.size()));
            List arrayList = new ArrayList();
            for (int i = 0; i < gsonTableNum.size(); i++) {
                TableNumber tableNumber = this.presenter.getTableNumber(((TableNumber) gsonTableNum.get(i)).getId().longValue());
                if (tableNumber != null) {
                    int intValue = tableNumber.getTable_type_count().intValue() - ((TableNumber) gsonTableNum.get(i)).getTable_type_count().intValue();
                    if (intValue <= 0) {
                        tableNumber.setTable_type_count(Integer.valueOf(0));
                        tableNumber.setTable_person(Integer.valueOf(0));
                    } else {
                        tableNumber.setTable_type_count(Integer.valueOf(intValue));
                        tableNumber.setTable_person(Integer.valueOf(1));
                    }
                    arrayList.add(tableNumber);
                }
            }
            this.presenter.setTableNumber(arrayList);
            notifyPeopleTable();
            notifyTableChange();
        }
        this.llSearch.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                TableActivity.this.startActivity(new Intent(TableActivity.this.context, SearchActivity.class));
            }
        });
        this.gridView.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long j) {
                TableActivity.this.adapter.setSelectFoodType(i);
                TableActivity.this.setSelect(((TableActivity.this.currentIndex - 1) * 10) + i);
            }
        });
        etTablePeople.addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            public void afterTextChanged(Editable editable) {
                String obj = editable.toString();
                int length = editable.toString().length();
                if (length == 1 && obj.equals("0")) {
                    editable.clear();
                }
                if (length == 2 && Integer.valueOf(obj).intValue() < 10) {
                    editable.delete(0, 1);
                }
                if (editable.length() > 4) {
                    editable.delete(4, TableActivity.etTablePeople.getSelectionEnd());
                }
            }
        });
    }

    protected void initData() {
        this.presenter.getAreaListFromDB();
        this.presenter.getAreaListFromNET();
        receiverMessages(null);
        initShopData();
    }

    private void initTableArea() {
        int i;
        for (i = 0; i < listTmp.size(); i++) {
            this.fragmentList.add(null);
        }
        if (listTmp.size() / 10 == 0) {
            i = 1;
        } else if (listTmp.size() % 10 == 0) {
            i = listTmp.size() / 10;
        } else {
            i = (listTmp.size() / 10) + 1;
        }
        this.totalPage = i;
        if (listTmp.size() > 10) {
            this.llMenuRight.setVisibility(View.VISIBLE);
        }
        addData();
        if (listTmp.size() > 0) {
            setSelect(0);
        }
    }

    private void addData() {
        if (this.currentIndex <= 1) {
            this.currentIndex = 1;
            this.tvBefore.setVisibility(View.INVISIBLE);
            this.tvNext.setVisibility(View.VISIBLE);
        }
        if (this.currentIndex >= this.totalPage) {
            this.currentIndex = this.totalPage;
        }
        if (this.currentIndex > 1 && this.currentIndex < this.totalPage) {
            this.tvBefore.setVisibility(View.VISIBLE);
            this.tvNext.setVisibility(View.VISIBLE);
        }
        if (this.list == null) {
            this.list = new ArrayList();
        }
        this.list.clear();
        int i = (this.currentIndex - 1) * 10;
        int i2 = this.currentIndex * 10;
        if (i2 > listTmp.size()) {
            i2 = listTmp.size();
        }
        while (i < i2) {
            this.list.add(listTmp.get(i));
            i++;
        }
        if (this.list.size() <= 5) {
            if (listTmp.size() <= 10) {
                this.tvBefore.setVisibility(View.GONE);
                this.tvNext.setVisibility(View.GONE);
            } else {
                this.tvBefore.setVisibility(View.VISIBLE);
                this.tvNext.setVisibility(View.GONE);
            }
            this.gridView.setNumColumns(this.list.size());
        } else {
            this.gridView.setNumColumns(5);
        }
        if (this.adapter == null) {
            this.adapter = new TableAreaAdapter(this.context, this.list);
            this.gridView.setAdapter(this.adapter);
            return;
        }
        this.adapter.notifyDataSetChanged();
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_before:
                this.currentIndex--;
                addData();
                return;
            case R.id.tv_next:
                this.currentIndex++;
                addData();
                return;
            case R.id.iv_msg:
                View view2 = view;
                this.dialogUtils.showMSG(view2, this.llTopRight, this.presenter.getMessaggeListNOReadFromDB(), this.presenter.getMessaggeListReadedFromDB(), new OnClickListener() {
                    public void onClick(View view) {
                        TableActivity.this.receiverMessages(null);
                    }
                });
                return;
            case R.id.tv_table_confirm:
                if ("".equals(getETTablePeople())) {
                    Toasts.showShort(this.context, (CharSequence) "请先输入人数");
                    return;
                } else if (SingletonTableNumberList.getInstance().getSelectList().size() == 0) {
                    Toasts.showShort(this.context, (CharSequence) "请先选择桌号");
                    return;
                } else {
                    this.orderInfo.setUsersnum(Integer.valueOf(getETTablePeople()));
                    setTables();
                    submitIndent();
                    return;
                }
            case R.id.tv_table_cancel:
                finishTo();
                return;
            default:
                return;
        }
    }

    private void submitIndent() {
        this.presenter.submitIndent(this.orderInfo);
    }

    private void setTables() {
        List selectList = SingletonTableNumberList.getInstance().getSelectList();
        this.tableNumbers.clear();
        this.tableNumbers.addAll(selectList);
        List arrayList = new ArrayList();
        int size = selectList.size();
        int i;
        if (size > 1) {
            int intValue = Integer.valueOf(getETTablePeople()).intValue() % size;
            int intValue2 = (Integer.valueOf(getETTablePeople()).intValue() - intValue) / size;
            int i2 = 0;
            i = -1;
            for (int i3 = 0; i3 < size; i3++) {
                TableNumber tableNumber = this.presenter.getTableNumber(((TableNumber) this.tableNumbers.get(i3)).getId().longValue());
                TableNumber tableNumber2 = new TableNumber(1l);
                tableNumber2.setId(((TableNumber) this.tableNumbers.get(i3)).getId());
                tableNumber2.setTable_name(((TableNumber) this.tableNumbers.get(i3)).getTable_name());
                tableNumber2.setArea_name(((TableNumber) this.tableNumbers.get(i3)).getArea_name());
                tableNumber2.setEating_count(((TableNumber) this.tableNumbers.get(i3)).getEating_count());
                tableNumber2.setTable_person(Integer.valueOf(1));
                tableNumber2.setPid(((TableNumber) this.tableNumbers.get(i3)).getPid());
                tableNumber2.setTable_type_count(Integer.valueOf(intValue2));
                int intValue3 = tableNumber.getTable_type_count().intValue() + intValue2;
                ((TableNumber) this.tableNumbers.get(i3)).setTable_person(Integer.valueOf(1));
                ((TableNumber) this.tableNumbers.get(i3)).setTable_type_count(Integer.valueOf(intValue3));
                arrayList.add(tableNumber2);
                if (i3 == 1) {
                    if (((TableNumber) this.tableNumbers.get(0)).getTable_type_count().intValue() <= ((TableNumber) this.tableNumbers.get(i3)).getTable_type_count().intValue()) {
                        i2 = ((TableNumber) this.tableNumbers.get(0)).getTable_type_count().intValue();
                        i = 0;
                    } else {
                        i2 = ((TableNumber) this.tableNumbers.get(i3)).getTable_type_count().intValue();
                        i = i3;
                    }
                }
                if (i2 > ((TableNumber) this.tableNumbers.get(i3)).getTable_type_count().intValue()) {
                    i2 = ((TableNumber) this.tableNumbers.get(i3)).getTable_type_count().intValue();
                    i = i3;
                }
                if (i3 == size - 1 && i != -1) {
                    ((TableNumber) this.tableNumbers.get(i)).setTable_type_count(Integer.valueOf(((TableNumber) this.tableNumbers.get(i)).getTable_type_count().intValue() + intValue));
                    ((TableNumber) arrayList.get(i)).setTable_type_count(Integer.valueOf(intValue + intValue2));
                }
            }
        } else {
            TableNumber tableNumber3 = this.presenter.getTableNumber(((TableNumber) this.tableNumbers.get(0)).getId().longValue());
            i = Integer.parseInt(getETTablePeople());
            TableNumber tableNumber4 = new TableNumber(0l);
            tableNumber4.setId(((TableNumber) this.tableNumbers.get(0)).getId());
            tableNumber4.setTable_name(((TableNumber) this.tableNumbers.get(0)).getTable_name());
            tableNumber4.setArea_name(((TableNumber) this.tableNumbers.get(0)).getArea_name());
            tableNumber4.setEating_count(((TableNumber) this.tableNumbers.get(0)).getEating_count());
            tableNumber4.setTable_person(Integer.valueOf(1));
            tableNumber4.setPid(((TableNumber) this.tableNumbers.get(0)).getPid());
            tableNumber4.setTable_type_count(Integer.valueOf(i));
            ((TableNumber) this.tableNumbers.get(0)).setTable_type_count(Integer.valueOf(tableNumber3.getTable_type_count().intValue() + i));
            ((TableNumber) this.tableNumbers.get(0)).setTable_person(Integer.valueOf(1));
            arrayList.add(tableNumber4);
        }
        this.orderInfo.setTablenumber(JSON.toJSONString(arrayList));
    }

    private void initShopData() {
        this.shopFragment = new TableShopFragment();
        FragmentTransaction beginTransaction = getSupportFragmentManager().beginTransaction();
        beginTransaction.add((int) R.id.main_frag_right, this.shopFragment);
        beginTransaction.commit();
    }

    private void setSelect(int i) {
        FragmentTransaction beginTransaction = getSupportFragmentManager().beginTransaction();
        hideFragment(beginTransaction);
        if (this.fragmentList.get(i) == null) {
            this.fragmentList.set(i, new TableFragment());
            ((TableFragment) this.fragmentList.get(i)).setIndexPage(i);
            beginTransaction.add((int) R.id.main_frag, (Fragment) this.fragmentList.get(i));
        } else {
            beginTransaction.show((Fragment) this.fragmentList.get(i));
        }
        try {
            beginTransaction.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void hideFragment(FragmentTransaction fragmentTransaction) {
        for (int i = 0; i < this.fragmentList.size(); i++) {
            if (this.fragmentList.get(i) != null) {
                fragmentTransaction.hide((Fragment) this.fragmentList.get(i));
            }
        }
    }

    public void onSuccess(List<TableArea> list) {
        ShowTableArea(list);
    }

    public void onFail() {
    }

    public void ShowTableArea(List<TableArea> list) {
        if (listTmp == null) {
            listTmp = new ArrayList();
        }
        listTmp.clear();
        listTmp.addAll(list);
        this.adapter = null;
        initTableArea();
    }

    public void isSubmitIndent(String str) {
        if ("onSuccess".equals(str)) {
            this.presenter.setTableNumber(this.tableNumbers);
            SingletonTableNumberList.getInstance().clear();
            finish();
            MainActivity.clear();
        } else if ("onAlert".equals(str)) {
            Toasts.show(this.context, "订单提交失败,请重新提交");
        } else if ("onFailure".equals(str)) {
            Toasts.show(this.context, "网络出现错误,请重新提交");
        }
    }

    public static String getETTablePeople() {
        return etTablePeople.getText().toString().trim();
    }

    public void receiverMessages(String str) {
        List messaggeListNOReadFromDB = this.presenter.getMessaggeListNOReadFromDB();
        this.tvTableInform.setText("0");
        if (messaggeListNOReadFromDB.size() > 0) {
            this.tvTableInform.setText("" + messaggeListNOReadFromDB.size());
        }
    }

    protected void onDestroy() {
        super.onDestroy();
        SingletonTableNumberList.getInstance().clear();
    }

    public void notifyTableChange() {
        if (this.fragmentList != null) {
            for (int i = 0; i < this.fragmentList.size(); i++) {
                TableFragment tableFragment = (TableFragment) this.fragmentList.get(i);
                if (tableFragment != null) {
                    Message message = new Message();
                    message.what = 1;
                    message.obj = tableFragment;
                    this.handler.sendMessage(message);
                    LogUtils.e(TAG, "notifyTableChange: ");
                }
            }
        }
    }

    public void toFinish(View view) {
        finishTo();
    }

    public boolean onKeyDown(int i, KeyEvent keyEvent) {
        if (i == 4) {
            finishTo();
        }
        return false;
    }

    private void finishTo() {
        if (this.TYPE.equals("OrderRight")) {
            this.presenter.setTableNumber(this.tableNumberList);
            finish();
            return;
        }
        finish();
    }
}
