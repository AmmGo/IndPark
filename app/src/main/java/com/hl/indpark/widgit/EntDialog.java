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
import com.hl.indpark.entities.events.EntNameEvent;
import com.hl.indpark.entities.events.EntSEPTypeEvent;
import com.hl.indpark.entities.events.EntTypeEvent;
import com.hl.indpark.entities.events.PopEvent;
import com.hl.indpark.entities.events.ReportTypeEvent;
import com.hl.indpark.uis.adapters.EntEPTypeAdapter;
import com.hl.indpark.uis.adapters.EntNameAdapter;
import com.hl.indpark.uis.adapters.EntTypeAdapter;
import com.hl.indpark.uis.adapters.ReportTypeAdapter;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

/**
 * 100企业列表
 * 101工艺类型列表
 * 102上报事件类型
 */
public class EntDialog extends Dialog implements OnClickListener {
    Activity context;
    private View convertView;
    private PopEvent popEvent;
    private RecyclerView recyclerView;
    private TextView tvCancel;
    private TextView tvSave;
    private int entCose;

    public EntDialog(Activity context, PopEvent popEvent, int entCose) {
        super(context);
        this.context = context;
        this.popEvent = popEvent;
        this.entCose = entCose;
        initView();
    }

    private void initAdapter() {
        //创建布局管理
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        switch (entCose) {
            case 100:
                initEntNameAdapter(popEvent.entNameEvents);
                break;
            case 101:
                initEntTypeAdapter(popEvent.entTypeEvents);
                break;
            case 102:
                initReportTypeAdapter(popEvent.reportTypeEventList);
                break;
            case 103:
                initEPTypeAdapter(popEvent.entSEPTypeEvents);
                break;
            default:
        }

    }

    private void initEntNameAdapter(List<EntNameEvent> list) {
        EntNameAdapter entAdapter = new EntNameAdapter(list, context);
        entAdapter.openLoadAnimation(BaseQuickAdapter.SCALEIN);
        //给RecyclerView设置适配器
        recyclerView.setAdapter(entAdapter);
        entAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                EntNameEvent event = new EntNameEvent();
                event.id = list.get(position).id;
                event.name = list.get(position).name;
                event.companyCode = list.get(position).companyCode;
                event.psCode = list.get(position).psCode;
                EventBus.getDefault().post(event);
            }
        });
    }

    private void initEPTypeAdapter(List<EntSEPTypeEvent> list) {
        EntEPTypeAdapter entAdapter = new EntEPTypeAdapter(list, context);
        entAdapter.openLoadAnimation(BaseQuickAdapter.SCALEIN);
        //给RecyclerView设置适配器
        recyclerView.setAdapter(entAdapter);
        entAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                EntSEPTypeEvent event = new EntSEPTypeEvent();
                event.name = list.get(position).name;
                event.iocode= list.get(position).iocode;
                EventBus.getDefault().post(event);
            }
        });
    }

    private void initEntTypeAdapter(List<EntTypeEvent> list) {
        EntTypeAdapter entAdapter = new EntTypeAdapter(list, context);
        entAdapter.openLoadAnimation(BaseQuickAdapter.SCALEIN);
        //给RecyclerView设置适配器
        recyclerView.setAdapter(entAdapter);
        entAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                EntTypeEvent event = new EntTypeEvent();
                event.name = list.get(position).name;
                event.id = list.get(position).id;
                EventBus.getDefault().post(event);
            }
        });
    }

    private void initReportTypeAdapter(List<ReportTypeEvent> list) {
        ReportTypeAdapter entAdapter = new ReportTypeAdapter(list, context);
        entAdapter.openLoadAnimation(BaseQuickAdapter.SCALEIN);
        //给RecyclerView设置适配器
        recyclerView.setAdapter(entAdapter);
        entAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                ReportTypeEvent event = new ReportTypeEvent();
                event.name = list.get(position).name;
                event.id = list.get(position).id;
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
        initAdapter();
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