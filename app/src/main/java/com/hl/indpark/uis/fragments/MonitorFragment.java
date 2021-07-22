package com.hl.indpark.uis.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.hl.indpark.R;
import com.hl.indpark.entities.Response;
import com.hl.indpark.entities.events.CameraVideoEvent;
import com.hl.indpark.entities.events.EntNameEvent;
import com.hl.indpark.entities.events.PopEvent;
import com.hl.indpark.nets.ApiObserver;
import com.hl.indpark.nets.repositories.ArticlesRepo;
import com.hl.indpark.uis.activities.MonitorRtspActivity;
import com.hl.indpark.uis.activities.WebViewMonitorActivity;
import com.hl.indpark.uis.adapters.MonitorAdapter;
import com.hl.indpark.utils.MessageHelper;
import com.hl.indpark.utils.SharePreferenceUtil;
import com.hl.indpark.utils.Util;
import com.hl.indpark.widgit.EntDialog;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import net.arvin.baselib.base.BaseFragment;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static com.hl.indpark.uis.activities.MonitorRtspActivity.toURLEncoded;

/**
 * Created by arvinljw on 2018/11/16 14:41
 * Function：
 * Desc：监测
 */
public class MonitorFragment extends BaseFragment {
    private EntDialog pop;
    private PopEvent popEvent;
    @BindView(R.id.ll_show_gwh)
    LinearLayout ll_spin;
    @BindView(R.id.title_bar)
    LinearLayout title_show;
    @BindView(R.id.chooseText)
    TextView chooseText;
    @BindView(R.id.title)
    TextView tv_title;
    @BindView(R.id.rl_search)
    RelativeLayout rl_search;

    @BindView(R.id.recy_web_view_video)
    RecyclerView recyclerView;
    private int pageNum = 1;
    private int pageSize = 20;
    private int total = 0;
    @BindView(R.id.refreshLayout)
    RefreshLayout refreshLayout;
    private List<CameraVideoEvent> list;
    private List<CameraVideoEvent> myList;
    private MonitorAdapter adapter;
    private List<CameraVideoEvent> lookout;
    public String yqid = null;
    private String category;
    private int tagSelect = 1;

    @OnClick({R.id.ll_spin, R.id.rl_search, R.id.ll_gklw})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_spin:
                try {
                    pop = new EntDialog(getActivity(), popEvent, 99);
                    pop.setCanceledOnTouchOutside(true);
                    pop.show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case R.id.rl_search:
                btnIconClick();
                break;
            case R.id.ll_gklw:
                tagSelect = 2;
                pageNum = 1;
                pageSize = 20;
                list.clear();
                getLookout(pageNum, pageSize);
                break;
            default:
                break;
        }

    }

    private PopupWindow searchPopupWindow;
    private View layoutView;
    private EditText txtSearch;
    private ImageView imgClear;
    private String searchName = null;

    public void btnIconClick() {
        if (searchPopupWindow == null) {
            layoutView = getLayoutInflater().inflate(
                    R.layout.search_view, null, false);
            searchPopupWindow = MessageHelper.getPopupWindow(layoutView);
            searchPopupWindow.setWidth(LinearLayout.LayoutParams.MATCH_PARENT);
            txtSearch = (EditText) layoutView.findViewById(R.id.txtSearch);
            txtSearch.setText("");
            txtSearch.addTextChangedListener(watcherHeel);
            imgClear = (ImageView) layoutView.findViewById(R.id.imgClear);
            imgClear.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    searchName = null;
                    txtSearch.setText("");

                }
            });
            TextView lbSearch = (TextView) layoutView.findViewById(R.id.lbSearch);
            lbSearch.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    searchName = txtSearch.getText().toString();
                    pageNum = 1;
                    pageSize = 20;
                    list.clear();
                    getHazardCamera(pageNum, pageSize);
                    searchPopupWindow.dismiss();
                }
            });

            searchPopupWindow.showAsDropDown(getActivity().findViewById(R.id.title_bar), 0, 0);
        } else {
            if (searchPopupWindow.isShowing()) {
                searchPopupWindow.dismiss();
            } else {
                // 展示菜单
                searchPopupWindow.showAsDropDown(getActivity().findViewById(R.id.title_bar), 0,
                        0);
            }
        }
    }

    private TextWatcher watcherHeel = new TextWatcher() {

        @Override
        public void onTextChanged(CharSequence s, int start, int before,
                                  int count) {
            // TODO Auto-generated method stub

        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count,
                                      int after) {
            // TODO Auto-generated method stub

        }

        @Override
        public void afterTextChanged(Editable s) {
            // TODO Auto-generated method stub
            searchName = s.toString();
            if (TextUtils.isEmpty(searchName)) {
                imgClear.setVisibility(View.GONE);
            } else {
                imgClear.setVisibility(View.VISIBLE);

            }
        }
    };

    @Subscribe
    public void getEntName(EntNameEvent event) {
        tagSelect = 1;
        chooseText.setText(event.name);
        yqid = String.valueOf(event.id);
        pageNum = 1;
        pageSize = 20;
        list.clear();
        getHazardCamera(pageNum, pageSize);
        pop.cancel();
    }

    @Override
    protected int getContentView() {
        return R.layout.fragment_monitor;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        popEvent = new PopEvent();
        category = SharePreferenceUtil.getKeyValue("category");
        tv_title.setText("监控系统");
        if (category != null && !category.equals("") && category.equals("2")) {
            ll_spin.setVisibility(View.VISIBLE);
            getEntName();//获取企业id
        } else {
            ll_spin.setVisibility(View.GONE);
        }

        refreshLayout.autoRefresh();//自动刷新
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull final RefreshLayout refreshLayout) {
                refreshLayout.getLayout().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        searchName = null;
                        pageNum = 1;
                        pageSize = 20;
                        list.clear();
                        if (tagSelect == 1) {
                            getHazardCamera(pageNum, pageSize);
                        } else {
                            getLookout(pageNum, pageSize);
                        }

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
                        if (tagSelect == 1) {
                            getHazardCamera(pageNum, pageSize);
                        } else {
                            getLookout(pageNum, pageSize);
                        }
                        if (total == 1) {
                            refreshLayout.finishLoadMoreWithNoMoreData();
                        } else {
                            refreshLayout.finishLoadMore();
                        }
                    }
                }, 0);
            }
        });
        initAdapter();

    }

    public void initAdapter() {
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        list = new ArrayList<>();
        adapter = new MonitorAdapter(list);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                try {
                    if (list.get(position).ip!=null&&list.get(position).ip.contains("rtsp")){
                        Intent intent = new Intent(getActivity(), MonitorRtspActivity.class);
                        String webUrl = "http://www.nxzwgyyqgwh.com.cn/live?url="+ toURLEncoded(list.get(position).ip);;
                        intent.putExtra("URL", webUrl);
                        startActivity(intent);
                    }else{
                        Intent intent = new Intent(getActivity(), WebViewMonitorActivity.class);
                        String webUrl = "http://ops.zwhldk.com:7080/ditu/player/player.html?streamName=" + list.get(position).key;
                        intent.putExtra("URL", webUrl);
                        startActivity(intent);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void getEntName() {
        ArticlesRepo.getEnterpriseEvent().observe(this, new ApiObserver<List<EntNameEvent>>() {
            @Override
            public void onSuccess(Response<List<EntNameEvent>> response) {
                try {
                    popEvent.entNameEvents = response.getData();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int code, String msg) {
                super.onFailure(code, msg);
                Util.login(String.valueOf(code), getActivity());
            }

            @Override
            public void onError(Throwable throwable) {
                super.onError(throwable);

            }
        });
    }

    public void getLookout(int pageNum, int pageSize) {
        ArticlesRepo.getLookout(pageNum, pageSize).observe(this, new ApiObserver<List<CameraVideoEvent>>() {
            @Override
            public void onSuccess(Response<List<CameraVideoEvent>> response) {
                Log.e("成功", "onSuccess: ");
                myList = new ArrayList<>();
                myList = response.getData();
                if (myList != null && response.getData().size() > 0) {
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
                Util.login(String.valueOf(code), getActivity());
            }

            @Override
            public void onError(Throwable throwable) {
                super.onError(throwable);
            }
        });
    }

    public void getHazardCamera(int pageNum, int pageSize) {
        ArticlesRepo.getHazardCamera(pageNum, pageSize, yqid, searchName).observe(this, new ApiObserver<List<CameraVideoEvent>>() {
            @Override
            public void onSuccess(Response<List<CameraVideoEvent>> response) {
                Log.e("成功", "onSuccess: ");
                myList = new ArrayList<>();
                myList = response.getData();
                if (myList != null && response.getData().size() > 0) {
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
                Util.login(String.valueOf(code), getActivity());
                refreshLayout.finishRefresh();
            }

            @Override
            public void onError(Throwable throwable) {
                super.onError(throwable);
                refreshLayout.finishRefresh();
            }
        });
    }

    @Override
    public void onDestroy() {
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
        super.onDestroy();
    }

    @Override
    public void onStart() {
        super.onStart();
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }
}
