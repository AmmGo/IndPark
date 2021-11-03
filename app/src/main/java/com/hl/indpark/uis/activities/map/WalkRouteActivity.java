package com.hl.indpark.uis.activities.map;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.AMap.InfoWindowAdapter;
import com.amap.api.maps2d.AMap.OnInfoWindowClickListener;
import com.amap.api.maps2d.AMap.OnMapClickListener;
import com.amap.api.maps2d.AMap.OnMarkerClickListener;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.Marker;
import com.amap.api.services.core.AMapException;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.route.BusRouteResult;
import com.amap.api.services.route.DriveRouteResult;
import com.amap.api.services.route.RidePath;
import com.amap.api.services.route.RideRouteResult;
import com.amap.api.services.route.RouteSearch;
import com.amap.api.services.route.RouteSearch.OnRouteSearchListener;
import com.amap.api.services.route.RouteSearch.WalkRouteQuery;
import com.amap.api.services.route.WalkPath;
import com.amap.api.services.route.WalkRouteResult;
import com.hl.indpark.R;
import com.hl.indpark.utils.AMapUtil;
import com.hl.indpark.utils.JumpMap;
import com.hl.indpark.utils.overlay.WalkRouteOverlay;
import com.hl.indpark.widgit.RewritePopwindow;

import net.arvin.baselib.utils.ToastUtil;
import net.arvin.baselib.widgets.TitleBar;

;


public class WalkRouteActivity extends Activity implements OnMapClickListener,
		OnMarkerClickListener, OnInfoWindowClickListener, InfoWindowAdapter, OnRouteSearchListener, AMap.OnMapLoadedListener, OnClickListener {
	private AMap aMap;
	private MapView mapView;
	private Context mContext;
	private RouteSearch mRouteSearch;
	private WalkRouteResult mWalkRouteResult;
	private RideRouteResult mRideRouteResult;
	private final int ROUTE_TYPE_WALK = 3;
	private final int ROUTE_TYPE_RIDE = 4;
	private double loclat;
	private double loclng;
	private double endlat;
	private double endlng;
	private RelativeLayout mBottomLayout, mHeadLayout;
	private TextView mRotueTimeDes, mRouteDetailDes;
	private ProgressDialog progDialog = null;// 搜索时进度条
	private WalkRouteOverlay walkRouteOverlay;
	private LatLonPoint mStartPoint;
	private LatLonPoint mEndPoint;
	private LatLng siteLatLng;
	private LinearLayout ll_mord;
	@Override
	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		setContentView(R.layout.route_activity);
		loclat = getIntent().getDoubleExtra("locLat",38.508751);
		loclng = getIntent().getDoubleExtra("locLng",106.17572);
		endlat = getIntent().getDoubleExtra("endLat",38.500821);
		endlng = getIntent().getDoubleExtra("endLng",106.182154);
		mStartPoint = new LatLonPoint(loclat,loclng);
		mEndPoint = new LatLonPoint(endlat,endlng);
		siteLatLng = new LatLng(endlat,endlng);
		TitleBar titleBar = findViewById(R.id.title_bar);
		titleBar.getCenterTextView().setText("导航路线");
		titleBar.getLeftImageView().setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				onBackPressed();
			}
		});
		mContext = this.getApplicationContext();
		mapView = (MapView) findViewById(R.id.route_map);
		ll_mord = findViewById(R.id.ep_point);
		ll_mord.setOnClickListener(this);
		mapView.onCreate(bundle);// 此方法必须重写
		init();
	}

	/**
	 * 初始化AMap对象
	 */
	private void init() {
		if (aMap == null) {
			aMap = mapView.getMap();	
		}
		registerListener();
		try {
			mRouteSearch = new RouteSearch(this);
		} catch (AMapException e) {
			e.printStackTrace();
		}
		mRouteSearch.setRouteSearchListener(this);
		mBottomLayout = (RelativeLayout) findViewById(R.id.bottom_layout);
		mHeadLayout = (RelativeLayout) findViewById(R.id.routemap_header);
		mHeadLayout.setVisibility(View.GONE);
		mRotueTimeDes = (TextView) findViewById(R.id.firstline);
		mRouteDetailDes = (TextView) findViewById(R.id.secondline);

	}

	/**
	 * 注册监听
	 */
	private void registerListener() {
		aMap.setOnMapClickListener(WalkRouteActivity.this);
		aMap.setOnMarkerClickListener(WalkRouteActivity.this);
		aMap.setOnInfoWindowClickListener(WalkRouteActivity.this);
		aMap.setInfoWindowAdapter(WalkRouteActivity.this);
		aMap.setOnMapLoadedListener(this);
		
	}

	@Override
	public View getInfoContents(Marker arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public View getInfoWindow(Marker arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onInfoWindowClick(Marker arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean onMarkerClick(Marker arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onMapClick(LatLng arg0) {
		// TODO Auto-generated method stub
		
	}

	
	/**
	 * 开始搜索路径规划方案
	 */
	public void searchRouteResult(int routeType,int mode) {
		if (mStartPoint == null) {
			ToastUtil.showToast(this,"定位中，稍后再试...");
			return;
		}
		if (mEndPoint == null) {
			ToastUtil.showToast(this,"终点未设置");
		}
		showProgressDialog();
		final RouteSearch.FromAndTo fromAndTo = new RouteSearch.FromAndTo(
				mStartPoint, mEndPoint);
		if (routeType == ROUTE_TYPE_WALK) {// 步行路径规划
			WalkRouteQuery query = new WalkRouteQuery(fromAndTo);
			mRouteSearch.calculateWalkRouteAsyn(query);// 异步路径规划步行模式查询
		}else  if (routeType == ROUTE_TYPE_RIDE) {// 骑行路径规划
			RouteSearch.RideRouteQuery query = new RouteSearch.RideRouteQuery(fromAndTo, mode);
			mRouteSearch.calculateRideRouteAsyn(query);// 异步路径规划骑行模式查询
		}
	}

	@Override
	public void onBusRouteSearched(BusRouteResult result, int errorCode) {
		
	}

	@Override
	public void onDriveRouteSearched(DriveRouteResult result, int errorCode) {
		
	}

	@Override
	public void onWalkRouteSearched(WalkRouteResult result, int errorCode) {
		dissmissProgressDialog();
		aMap.clear();// 清理地图上的所有覆盖物
		if (errorCode == AMapException.CODE_AMAP_SUCCESS) {
			if (result != null && result.getPaths() != null) {
				if (result.getPaths().size() > 0) {
					mWalkRouteResult = result;
					final WalkPath walkPath = mWalkRouteResult.getPaths()
							.get(0);
					if (walkRouteOverlay != null){
						walkRouteOverlay.removeFromMap();
					}
					walkRouteOverlay = new WalkRouteOverlay(
							this, aMap, walkPath,
							mWalkRouteResult.getStartPos(),
							mWalkRouteResult.getTargetPos());
					walkRouteOverlay.addToMap();
					walkRouteOverlay.zoomToSpan();
					mBottomLayout.setVisibility(View.VISIBLE);
					int dis = (int) walkPath.getDistance();
					int dur = (int) walkPath.getDuration();
					String des = AMapUtil.getFriendlyTime(dur)+"("+ AMapUtil.getFriendlyLength(dis)+")";
					mRotueTimeDes.setText(des);
					mRouteDetailDes.setVisibility(View.GONE);
					mBottomLayout.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							Intent intent = new Intent(mContext,
									WalkRouteDetailActivity.class);
							intent.putExtra("walk_path", walkPath);
							intent.putExtra("walk_result",
									mWalkRouteResult);
							startActivity(intent);
						}
					});
				} else if (result != null && result.getPaths() == null) {
					MapToastUtil.show(mContext, "对不起，没有搜索到相关数据！");
				}
			} else {
				MapToastUtil.show(mContext,"对不起，没有搜索到相关数据！");
			}
		} else {
			MapToastUtil.showerror(this.getApplicationContext(), errorCode);
		}
	}

	@Override
	public void onRideRouteSearched(RideRouteResult result, int errorCode) {
		dissmissProgressDialog();
		aMap.clear();// 清理地图上的所有覆盖物
		if (errorCode == AMapException.CODE_AMAP_SUCCESS) {
			if (result != null && result.getPaths() != null) {
				if (result.getPaths().size() > 0) {
					mRideRouteResult = result;
					final RidePath ridePath = mRideRouteResult.getPaths()
							.get(0);
					RideRouteOverlay rideRouteOverlay = new RideRouteOverlay(
							this, aMap, ridePath,
							mRideRouteResult.getStartPos(),
							mRideRouteResult.getTargetPos());
					rideRouteOverlay.removeFromMap();
					rideRouteOverlay.addToMap();
					rideRouteOverlay.zoomToSpan();
					mBottomLayout.setVisibility(View.VISIBLE);
					int dis = (int) ridePath.getDistance();
					int dur = (int) ridePath.getDuration();
					String des = AMapUtil.getFriendlyTime(dur) + "(" + AMapUtil.getFriendlyLength(dis) + ")";
					mRotueTimeDes.setText(des);
					mRouteDetailDes.setVisibility(View.GONE);
					mBottomLayout.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							Intent intent = new Intent(mContext,
									RideRouteDetailActivity.class);
							intent.putExtra("ride_path", ridePath);
							intent.putExtra("ride_result",
									mRideRouteResult);
							startActivity(intent);
						}
					});
				} else if (result != null && result.getPaths() == null) {
					MapToastUtil.show(mContext, "对不起，没有搜索到相关数据！");
				}
			} else {
				MapToastUtil.show(mContext, "对不起，没有搜索到相关数据！");
			}
		} else {
			MapToastUtil.showerror(this.getApplicationContext(), errorCode);
		}

	}
	/**
	 * 显示进度框
	 */
	private void showProgressDialog() {
		if (progDialog == null)
			progDialog = new ProgressDialog(this);
		    progDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		    progDialog.setIndeterminate(false);
		    progDialog.setCancelable(true);
		    progDialog.setMessage("正在搜索");
		    progDialog.show();
	    }

	/**
	 * 隐藏进度框
	 */
	private void dissmissProgressDialog() {
		if (progDialog != null) {
			progDialog.dismiss();
		}
	}

	/**
	 * 方法必须重写
	 */
	@Override
	protected void onResume() {
		super.onResume();
		mapView.onResume();
	}

	/**
	 * 方法必须重写
	 */
	@Override
	protected void onPause() {
		super.onPause();
		mapView.onPause();
	}

	/**
	 * 方法必须重写
	 */
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		mapView.onSaveInstanceState(outState);
	}

	/**
	 * 方法必须重写
	 */
	@Override
	protected void onDestroy() {
		super.onDestroy();
		mapView.onDestroy();
	}



	@Override
	public void onMapLoaded() {
		searchRouteResult(ROUTE_TYPE_RIDE, RouteSearch.RIDING_DEFAULT);
	}
	private RewritePopwindow mPopwindow;
	@Override
	public void onClick(View view) {
		switch (view.getId()){
			case R.id.ep_point:
				mPopwindow = new RewritePopwindow(WalkRouteActivity.this, itemsOnClick);
				mPopwindow.showAtLocation(view,
						Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
			break;
			default:
		}
	}
	//为弹出窗口实现监听类
	private View.OnClickListener itemsOnClick = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			mPopwindow.dismiss();
			mPopwindow.backgroundAlpha(WalkRouteActivity.this, 1f);
			switch (v.getId()) {
				case R.id.ll_bd:
					JumpMap.openBaiduMap(WalkRouteActivity.this, siteLatLng);
					break;
				case R.id.ll_gd:
					JumpMap.openGaodeMap(WalkRouteActivity.this, siteLatLng);
					break;
				default:
					break;
			}
		}

	};

}

