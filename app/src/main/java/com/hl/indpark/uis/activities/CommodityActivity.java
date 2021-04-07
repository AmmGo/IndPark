package com.hl.indpark.uis.activities;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.tu.loadingdialog.LoadingDailog;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.hl.indpark.R;
import com.hl.indpark.entities.Response;
import com.hl.indpark.entities.events.CommodityEvent;
import com.hl.indpark.nets.ApiObserver;
import com.hl.indpark.nets.repositories.ArticlesRepo;
import com.hl.indpark.uis.adapters.MyCommodityAdapter;
import com.hl.indpark.utils.Util;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import net.arvin.baselib.base.BaseActivity;
import net.arvin.baselib.utils.ToastUtil;
import net.arvin.baselib.widgets.TitleBar;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class CommodityActivity extends BaseActivity {
    @BindView(R.id.recy_my_commodity)
    RecyclerView recyclerView;
    private int pageNum = 1;
    private int pageSize = 10;
    private int total = 0;
    @BindView(R.id.refreshLayout)
    RefreshLayout refreshLayout;
    private List<CommodityEvent.RecordsBean> list;
    private List<CommodityEvent.RecordsBean> myList;
    private MyCommodityAdapter adapter;

    @Override
    protected int getContentView() {
        return R.layout.activity_commodity;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        TitleBar titleBar = findViewById(R.id.title_bar);
        titleBar.getCenterTextView().setText("商品兑换");
        titleBar.getLeftImageView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        refreshLayout.autoRefresh();
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull final RefreshLayout refreshLayout) {
                refreshLayout.getLayout().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        pageNum = 1;
                        pageSize = 10;
                        list.clear();
                        getData(pageNum, pageSize);
                    }
                }, 50);
            }
        });
        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                refreshLayout.getLayout().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        pageNum += 1;
                        getData(pageNum, pageSize);
                        if (total == 1) {
                            refreshLayout.finishLoadMoreWithNoMoreData();
                        } else {
                            refreshLayout.finishLoadMore();
                        }
                    }
                }, 50);
            }
        });
        initAdapter();
    }

    public void initAdapter() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        list = new ArrayList<>();
        adapter = new MyCommodityAdapter(list);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                switch (view.getId()){
                    case R.id.img_exchange:
                        showDialog("确定兑换"+list.get(position).name,list.get(position).id);
                        break;
                    default:
                }
            }
        });
    }
    public void showDialog(String msg,int commid) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflaterDl = LayoutInflater.from(this);
        LinearLayout layout = (LinearLayout) inflaterDl.inflate(R.layout.dialog_signin, null);
        final TextView logMsg = (TextView) layout.findViewById(R.id.tv_log_msg);
        final TextView logZDl = (TextView) layout.findViewById(R.id.tv_log_zdl);
        final TextView logqx = (TextView) layout.findViewById(R.id.tv_log_qx);
        logMsg.setText(msg);
        builder.setView(layout);
        AlertDialog ad = builder.create();
        ad.setCancelable(false);
        logZDl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getScoresExchangeCommodity(commid);
                ad.dismiss();
            }
        });
        logqx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ad.dismiss();
            }
        });
        ad.show();
    }
    private LoadingDailog dialog;
    public void getScoresExchangeCommodity(int commId) {
        LoadingDailog.Builder loadBuilder = new LoadingDailog.Builder(CommodityActivity.this)
                .setMessage("兑换中...")
                .setCancelable(false)
                .setCancelOutside(false);
        dialog = loadBuilder.create();
        dialog.getWindow().setDimAmount(0f);
        dialog.show();
        ArticlesRepo.getScoresExchangeCommodity(commId).observe(this, new ApiObserver<String>() {
            @Override
            public void onSuccess(Response<String> response) {
                dialog.cancel();
                ToastUtil.showToast(CommodityActivity.this,"兑换成功");
                BackPressed();
            }
            @Override
            public void onFailure(int code, String msg) {
                super.onFailure(code, msg);
                ToastUtil.showToast(CommodityActivity.this, msg);
                dialog.cancel();
                Util.login(String.valueOf(code), CommodityActivity.this);
            }
            @Override
            public void onError(Throwable throwable) {
                super.onError(throwable);
                dialog.cancel();
                ToastUtil.showToast(CommodityActivity.this, "兑换失败");
            }
        });
    }
    public void BackPressed() {
        Intent intent = new Intent();
        setResult(2, intent);
        finish();
    }
    public void getData(int pageNum, int pageSize) {
        ArticlesRepo.getCommodity(pageNum, pageSize).observe(this, new ApiObserver<CommodityEvent>() {
            @Override
            public void onSuccess(Response<CommodityEvent> response) {
                Log.e("商品列表", "onSuccess: ");
                CommodityEvent event = response.getData();
                myList = new ArrayList<>();
                myList = event.records;
                if (myList != null && event.records.size() > 0) {
                    list.addAll(myList);
                    adapter.setNewData(list);
                    total = 0;
                } else {
                    if (list.size() <= 0) {
                        View emptyView = getLayoutInflater().inflate(R.layout.layout_empty, (ViewGroup) recyclerView.getParent(), false);
                        list.clear();
                        adapter.setNewData(list);
                        adapter.setEmptyView(emptyView);
                    }
                    total = 1;
                }
                refreshLayout.finishRefresh();
            }

            @Override
            public void onFailure(int code, String msg) {
                super.onFailure(code, msg);
                Util.login(String.valueOf(code), CommodityActivity.this);
                refreshLayout.finishRefresh(false);
            }

            @Override
            public void onError(Throwable throwable) {
                super.onError(throwable);
                View emptyView = getLayoutInflater().inflate(R.layout.layout_empty, (ViewGroup) recyclerView.getParent(), false);
                list.clear();
                adapter.setNewData(list);
                adapter.setEmptyView(emptyView);
                refreshLayout.finishRefresh(false);
            }
        });
    }
}
