package com.hl.indpark.uis.fragments;


import android.os.Bundle;
import android.view.View;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.hl.indpark.R;
import com.hl.indpark.nets.Api;
import com.hl.indpark.uis.activities.ReportApprovalActivity;
import com.hl.indpark.uis.adapters.ImageAdapter;
import com.hl.indpark.utils.GlideEngine;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.entity.LocalMedia;

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
    List<LocalMedia> dataImg = new ArrayList<>();
    public void setActivity(ReportApprovalActivity homeActivity, List<String> data) {
        activity = homeActivity;
        list = data;
        if (list.size() > 0) {
            try {
                for (int i = 0 ;i<list.size();i++){
                    LocalMedia localMedia = new LocalMedia();
                    localMedia.setPath(Api.BASE_URL_IMG+list.get(i));
                    localMedia.setRealPath(Api.BASE_URL_IMG+list.get(i));
                    localMedia.setCompressPath(Api.BASE_URL_IMG+list.get(i));
                    localMedia.setFileName(list.get(i));
                    dataImg.add(localMedia);
                }
                adapter.setNewData(list);
                adapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
                    @Override
                    public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                        int themeId = R.style.picture_default_style;
                        switch (view.getId()) {
                            case R.id.iv_img_or_video:
                                PictureSelector.create(activity)
                                        .themeStyle(themeId)
                                        .imageEngine(GlideEngine.createGlideEngine()) // 选择器展示不出图片则添加
                                        .openExternalPreview(position, dataImg);
                                break;
                            default:
                        }
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }
}
