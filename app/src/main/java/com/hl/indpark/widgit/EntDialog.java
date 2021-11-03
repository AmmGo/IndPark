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
import com.hl.indpark.entities.new2.Clr;
import com.hl.indpark.entities.events.EntNameEvent;
import com.hl.indpark.entities.events.EntTypeEvent;
import com.hl.indpark.entities.new2.Level;
import com.hl.indpark.entities.events.MapPointEvent;
import com.hl.indpark.entities.events.NameEp;
import com.hl.indpark.entities.events.PhoneEvent;
import com.hl.indpark.entities.events.PopEvent;
import com.hl.indpark.entities.events.ReportTypeEvent;
import com.hl.indpark.entities.events.TypeEp;
import com.hl.indpark.uis.adapters.ClrAdapter;
import com.hl.indpark.uis.adapters.EntEPTypeAdapter;
import com.hl.indpark.uis.adapters.EntNameAdapter;
import com.hl.indpark.uis.adapters.EntNameSHAdapter;
import com.hl.indpark.uis.adapters.EntTypeAdapter;
import com.hl.indpark.uis.adapters.LevelAdapter;
import com.hl.indpark.uis.adapters.MachineAdapter;
import com.hl.indpark.uis.adapters.ReportTypeAdapter;
import com.hl.indpark.uis.adapters.XjryAdapter;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

/**
 * 100企业列表
 * 101工艺类型列表
 * 102上报事件类型
 * 104巡检人员
 * 105设备列表
 * 106级别
 * 107处理人
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
            case 99:
                initEntNameSHAdapter(popEvent.entNameEvents);
                break;
            case 100:
                initEntNameAdapter(popEvent.nameEp);
                break;
            case 101:
                initEntTypeAdapter(popEvent.entTypeEvents);
                break;
            case 102:
                initReportTypeAdapter(popEvent.reportTypeEventList);
                break;
            case 103:
                initEPTypeAdapter(popEvent.typeEp);
                break;
            case 104:
                initXjryAdapter(popEvent.phoneList);
                break;
            case 105:
                initMachineAdapter(popEvent.machineList);
                break;
            case 106:
                initLevelAdapter(popEvent.LevelList);
                break;
            case 107:
                initClrAdapter(popEvent.clrList);
                break;
            default:
        }

    }

    private void initEntNameAdapter(List<NameEp> list) {
        EntNameAdapter entAdapter = new EntNameAdapter(list, context);
        entAdapter.openLoadAnimation(BaseQuickAdapter.SCALEIN);
        //给RecyclerView设置适配器
        recyclerView.setAdapter(entAdapter);
        entAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                NameEp event = new NameEp();
                event.nameEp = list.get(position).nameEp;
                EventBus.getDefault().post(event);
            }
        });
    }

    private void initEntNameSHAdapter(List<EntNameEvent> list) {
        EntNameSHAdapter entAdapter = new EntNameSHAdapter(list, context);
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

    private void initEPTypeAdapter(List<TypeEp> list) {
        EntEPTypeAdapter entAdapter = new EntEPTypeAdapter(list, context);
        entAdapter.openLoadAnimation(BaseQuickAdapter.SCALEIN);
        //给RecyclerView设置适配器
        recyclerView.setAdapter(entAdapter);
        entAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                TypeEp event = new TypeEp();
                event.typeEp = list.get(position).typeEp;
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

    private void initLevelAdapter(List<Level> list) {
        LevelAdapter entAdapter = new LevelAdapter(list, context);
        entAdapter.openLoadAnimation(BaseQuickAdapter.SCALEIN);
        //给RecyclerView设置适配器
        recyclerView.setAdapter(entAdapter);
        entAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                Level event = new Level();
                event.name = list.get(position).name;
                event.id = list.get(position).id;
                EventBus.getDefault().post(event);
            }
        });
    }

    private void initClrAdapter(List<Clr> list) {
        ClrAdapter entAdapter = new ClrAdapter(list, context);
        entAdapter.openLoadAnimation(BaseQuickAdapter.SCALEIN);
        //给RecyclerView设置适配器
        recyclerView.setAdapter(entAdapter);
        entAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                Clr event = new Clr();
                event.name = list.get(position).name;
                event.id = list.get(position).id;
                EventBus.getDefault().post(event);
            }
        });
    }

    private void initXjryAdapter(List<PhoneEvent> list) {
        XjryAdapter entAdapter = new XjryAdapter(list, context);
        entAdapter.openLoadAnimation(BaseQuickAdapter.SCALEIN);
        //给RecyclerView设置适配器
        recyclerView.setAdapter(entAdapter);
        entAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                PhoneEvent event = new PhoneEvent();
                event.name = list.get(position).name;
                event.id = list.get(position).id;
                event.phone = list.get(position).phone;
                EventBus.getDefault().post(event);
            }
        });
    }

    private void initMachineAdapter(List<MapPointEvent> list) {
        MachineAdapter entAdapter = new MachineAdapter(list, context);
        entAdapter.openLoadAnimation(BaseQuickAdapter.SCALEIN);
        //给RecyclerView设置适配器
        recyclerView.setAdapter(entAdapter);
        entAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                MapPointEvent event = new MapPointEvent();
                event.name = list.get(position).name;
                event.address = list.get(position).address;
                event.model = list.get(position).model;
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