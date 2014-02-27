package com.xdf.test;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.map.ItemizedOverlay;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.OverlayItem;
import com.baidu.mapapi.search.MKAddrInfo;
import com.baidu.mapapi.search.MKBusLineResult;
import com.baidu.mapapi.search.MKDrivingRouteResult;
import com.baidu.mapapi.search.MKPoiResult;
import com.baidu.mapapi.search.MKSearch;
import com.baidu.mapapi.search.MKSearchListener;
import com.baidu.mapapi.search.MKShareUrlResult;
import com.baidu.mapapi.search.MKSuggestionResult;
import com.baidu.mapapi.search.MKTransitRouteResult;
import com.baidu.mapapi.search.MKWalkingRouteResult;
import com.baidu.platform.comapi.basestruct.GeoPoint;

/**
 * 地理位置图层展示
 * 
 * @author Administrator
 * 
 */
public class GeoCoderTest extends Activity implements OnClickListener {

	private Button btn_txt;
	private Button btn_number;
	private MapView bmapView;
	MKSearch mSearch = null; // 搜索模块，也可去掉地图模块独立使用
	DemoApplication app = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		app = (DemoApplication) this.getApplication();
		if (app.mBMapManager == null) {
			app.mBMapManager = new BMapManager(getApplicationContext());
			app.mBMapManager.init(DemoApplication.strKey,
					new DemoApplication.MyGeneralListener());
		}
		setContentView(R.layout.geocoder_layout);
		CharSequence titleLable = "地理编码功能";
		setTitle(titleLable);
		init();

	}

	private void init() {
		btn_txt = (Button) findViewById(R.id.btn_txt);
		btn_txt.setOnClickListener(this);
		btn_number = (Button) findViewById(R.id.btn_number);
		btn_number.setOnClickListener(this);
		bmapView = (MapView) findViewById(R.id.bmapView);
		bmapView.getController().enableClick(true);
		bmapView.getController().setZoom(12);
		mSearch = new MKSearch();
		mSearch.init(app.mBMapManager, new MKSearchListener() {

			@Override
			public void onGetAddrResult(MKAddrInfo res, int error) {
				if (error != 0) {
					String str = String.format("错误号：%d", error);
					Toast.makeText(GeoCoderTest.this, str, Toast.LENGTH_LONG)
							.show();
					return;
				}
				// 地图移动到该点
				bmapView.getController().animateTo(res.geoPt);
				if (res.type == MKAddrInfo.MK_GEOCODE) {
					// 地理编码：通过地址检索坐标点
					String strInfo = String.format("纬度：%f 经度：%f",
							res.geoPt.getLatitudeE6() / 1e6,
							res.geoPt.getLongitudeE6() / 1e6);
					Toast.makeText(GeoCoderTest.this, strInfo,
							Toast.LENGTH_LONG).show();
				}
				if (res.type == MKAddrInfo.MK_REVERSEGEOCODE) {
					// 反地理编码：通过坐标点检索详细地址及周边poi
					String strInfo = res.strAddr;
					Toast.makeText(GeoCoderTest.this, strInfo,
							Toast.LENGTH_LONG).show();
						System.out.println("----------------------"+res.addressComponents.city);//城市名称
						System.out.println("----------------------"+res.addressComponents.street);//街道名称
						System.out.println("----------------------"+res.addressComponents.district);//区县名称
						System.out.println("----------------------"+res.addressComponents.province);//省份名称
						
				}

				// 生成ItemizedOverlay图层用来标注结果点
				ItemizedOverlay<OverlayItem> itemOverlay = new ItemizedOverlay<OverlayItem>(
						null, bmapView);
				// 生成Item
				OverlayItem item = new OverlayItem(res.geoPt, "", null);
				// 得到需要标在地图上的资源
				Drawable marker = getResources().getDrawable(
						R.drawable.icon_markf);
				// 为maker定义位置和边界
				marker.setBounds(0, 0, marker.getIntrinsicWidth(),
						marker.getIntrinsicHeight());
				// 给item设置marker
				item.setMarker(marker);
				// 在图层上添加item
				itemOverlay.addItem(item);
				// 清除地图其他图层
				bmapView.getOverlays().clear();
				// 添加一个标注ItemizedOverlay图层
				bmapView.getOverlays().add(itemOverlay);
				// 执行刷新使生效
				bmapView.refresh();
			}

			@Override
			public void onGetWalkingRouteResult(MKWalkingRouteResult arg0,
					int arg1) {
			}

			@Override
			public void onGetTransitRouteResult(MKTransitRouteResult arg0,
					int arg1) {
			}

			@Override
			public void onGetSuggestionResult(MKSuggestionResult arg0, int arg1) {
			}

			@Override
			public void onGetShareUrlResult(MKShareUrlResult arg0, int arg1,
					int arg2) {
			}

			@Override
			public void onGetPoiResult(MKPoiResult arg0, int arg1, int arg2) {
			}

			@Override
			public void onGetPoiDetailSearchResult(int arg0, int arg1) {
			}

			@Override
			public void onGetDrivingRouteResult(MKDrivingRouteResult arg0,
					int arg1) {
			}

			@Override
			public void onGetBusDetailResult(MKBusLineResult arg0, int arg1) {
			}
		});
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_txt:// 详细地址转换到经纬度坐标
			 // Geo搜索
			 mSearch.geocode("梁平", "重庆市");
			break;
		case R.id.btn_number:// 经纬度坐标 转换到 详细地理位置
			GeoPoint ptCenter = new GeoPoint((int) (39.904965 * 1e6), (int) (116.327764 * 1e6));
			// 反Geo搜索
			mSearch.reverseGeocode(ptCenter);
			break;

		default:
			break;
		}
	}
	
	@Override
    protected void onPause() {
        bmapView.onPause();
        super.onPause();
    }
    
    @Override
    protected void onResume() {
    	bmapView.onResume();
        super.onResume();
    }
    @Override
    protected void onDestroy() {
    	bmapView.destroy();
        mSearch.destory();
        super.onDestroy();
    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
    	super.onSaveInstanceState(outState);
    	bmapView.onSaveInstanceState(outState);
    	
    }
    
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
    	super.onRestoreInstanceState(savedInstanceState);
    	bmapView.onRestoreInstanceState(savedInstanceState);
    }
}
