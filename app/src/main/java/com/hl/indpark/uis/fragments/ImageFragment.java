package com.hl.indpark.uis.fragments;


import android.os.Bundle;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hl.indpark.R;
import com.hl.indpark.uis.activities.ReportApprovalActivity;
import com.hl.indpark.uis.adapters.ImageAdapter;

import net.arvin.baselib.base.BaseFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * @author 添加视频图片
 * @time 2018/12/19 15:09
 */
public class ImageFragment extends BaseFragment {
    private ReportApprovalActivity activity;
    @BindView(R.id.recyclerView_media)
    RecyclerView recyclerView;
    private ImageAdapter adapter;
    private List<String> list = new ArrayList<>();

    @Override
    protected int getContentView() {
        return R.layout.fragment_imgae;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        ConfigRecyclerView();
    }

    private void ConfigRecyclerView() {
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(activity));
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(activity, 3));
        adapter = new ImageAdapter(R.layout.item_image, list);
        recyclerView.setAdapter(adapter);
    }

    public void setActivity(ReportApprovalActivity homeActivity, List<String> data) {
        activity = homeActivity;
        list = data;
        if (list.size() > 0) {
            adapter.setNewData(list);
        }

    }
}
