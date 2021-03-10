package com.hl.indpark.uis.fragments;


import android.app.Activity;
import android.os.Bundle;

import android.view.View;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.hl.indpark.R;
import com.hl.indpark.uis.adapters.MediaAdapter;
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
public class MediaFragment extends BaseFragment {
    private Activity activity;
    @BindView(R.id.recyclerView_media)
    RecyclerView recyclerView;
    private MediaAdapter adapter;
    private List<LocalMedia> selectList = new ArrayList<>();
    @Override
    protected int getContentView() {
        return R.layout.fragment_media;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        ConfigRecyclerView();
    }

    private void ConfigRecyclerView() {
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(activity));
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(activity, 4));
        adapter = new MediaAdapter(R.layout.item_media, selectList);
        recyclerView.setAdapter(adapter);
    }

    public void setActivity(Activity homeActivity, List<LocalMedia> data) {
        activity = homeActivity;
        selectList = data;
        if (selectList.size() > 0) {
            adapter.setNewData(selectList);
            adapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
                @Override
                public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                    int themeId = R.style.picture_default_style;
                    switch (view.getId()) {
                        case R.id.iv_img_or_video:
                            PictureSelector.create(activity).themeStyle(themeId).openExternalPreview(position, selectList);
                            break;
                        case R.id.img_play:
                            PictureSelector.create(activity).externalPictureVideo(selectList.get(position).getPath());
                            break;
                        case R.id.ll_del:
                            selectList.remove(position);
                            adapter.setNewData(selectList);
                            break;
                        default:
                    }
                }
            });
        }

    }
}
