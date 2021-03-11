package com.hl.indpark.widgit;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.hl.indpark.R;
import com.hl.indpark.entities.events.EntEvent;
import com.hl.indpark.uis.adapters.EntAdapter;

import net.arvin.baselib.utils.ToastUtil;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

public class EntDialog extends Dialog implements OnClickListener {
    Activity context;
    private View convertView;
    private List<EntEvent> list;
    private RecyclerView recyclerView;
    private TextView tvCancel;
    private TextView tvSave;
    private int entCose;

    public EntDialog(Activity context, List<EntEvent> entList,int entCose) {
        super(context);
        this.context = context;
        this.list = entList;
        this.entCose = entCose;
        initView();
    }

    private void initEntNameAdapter() {
        //创建布局管理
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        EntAdapter entAdapter = new EntAdapter(list, context);
        entAdapter.openLoadAnimation(BaseQuickAdapter.SCALEIN);
        //给RecyclerView设置适配器
        recyclerView.setAdapter(entAdapter);
        entAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                ToastUtil.showToast(context, list.get(position).name);
                EntEvent event = new EntEvent();
                switch (entCose){
                    case 100:
                        event.companyCode = list.get(position).companyCode;
                        event.id = list.get(position).id;
                        event.name = list.get(position).name;
                        event.entChose = 100;
                        break;
                    case 101:
                        event.name = list.get(position).name;
                        event.entChose = 101;
                        event.hazardType = list.get(position).hazardType;
                        break;
                        default:
                }
                EventBus.getDefault().post(event);
            }
        });
    }

    private void initView() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_ent_name);
        convertView = findViewById(R.id.rootView);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT);
        params.setMargins(0, 0, 0, 0);
        convertView.setLayoutParams(params);
        getWindow().setGravity(Gravity.BOTTOM);
        getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        recyclerView = convertView.findViewById(R.id.recy_ent_name);
        tvCancel = convertView.findViewById(R.id.tv_cancel);
        tvSave = convertView.findViewById(R.id.tv_save);
        tvCancel.setOnClickListener(this);
        tvSave.setOnClickListener(this);
        initEntNameAdapter();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_cancel:
                this.dismiss();
                break;
            case R.id.tv_save:
                this.dismiss();
                break;
            default:
                break;
        }

    }

}