package com.example.loveyoulmap;

import java.util.List;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.Toast;

import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMap.OnMarkerClickListener;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.InfoWindow.OnInfoWindowClickListener;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.overlayutil.PoiOverlay;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiCitySearchOption;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiDetailSearchOption;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;

public class MainActivity extends Activity {

//	MapView
	private MapView mapView;
//	百度地图   MapView.getMap();
	private BaiduMap baiduMap;
//	Poi  搜索
	private PoiSearch poiSearch;
//  webView 显示详情
	private WebView webView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

//		 去掉标题的显示(就是app 最顶上的显示内容！)
		requestWindowFeature(1);

		// 在使用SDK各组件之前初始化context信息，传入ApplicationContext
		// 注意该方法要再setContentView方法之前实现
		SDKInitializer.initialize(getApplicationContext());

		setContentView(R.layout.activity_main);

		// 获取 MapView
		mapView = (MapView) this.findViewById(R.id.map);
		webView = (WebView) this.findViewById(R.id.webview);
		
//		初始化  poiSearch 对象！
		poiSearch = PoiSearch.newInstance();
		
		// 获取地图视图：
		baiduMap = mapView.getMap();

		baiduMap.setTrafficEnabled(true);
		//
		baiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);

		baiduMap.setMaxAndMinZoomLevel(15, 19);

		// 1 创建当前位置的标志
		setOnePointToMap();

		// 2 设置为 点击事件：
		setPointListener();

		// 3 获取 兴趣点： 搜索服务！
		getPoiResourse();

	}
	

		/**
		 * 	//TODO 1 创建当前位置的标志
		 */
		private void setOnePointToMap() {

			// 2 描述其
			BitmapDescriptor descriptor = BitmapDescriptorFactory
					.fromResource(R.drawable.ic_launcher);

			// 3 位置 纬经度
			// 116.396364,39.916097
			LatLng latLng = new LatLng(39.916097, 116.396364);

			// 1 覆盖一层 透视的 图层！
			OverlayOptions overlayOptions = new MarkerOptions().title("北京站")
					.icon(descriptor).position(latLng);

			// 向地图添加一个 Overlay
			baiduMap.addOverlay(overlayOptions);
			
		}

		
		/**
		 * 	//TODO  2 设置 当前位置的  点击事件   弹出视图！
		 */
		private void setPointListener() {

//			baiduMap 的 覆盖物 被点击！ 触发该 事件。
			baiduMap.setOnMarkerClickListener(new OnMarkerClickListener() {

				@Override
				public boolean onMarkerClick(Marker marker) {
					
					// TODO 开启检索：  实在 点击之后触发的事件。  所搜方式： 城市内搜索！
					poiSearch.searchInCity(new PoiCitySearchOption().keyword("美食")
							.city("北京").pageNum(0).pageCapacity(10));

					Button btn = new Button(getApplicationContext());
					btn.setBackgroundColor(Color.RED);
					btn.setText(marker.getTitle());

					// btn 变成 View 图片
					BitmapDescriptor descriptor = BitmapDescriptorFactory
							.fromView(btn);

					/**
					 * 弹窗的点击事件：
					 *  - InfoWindow 展示的bitmap position 
					 *  - InfoWindow 显示的地理位置
					 *  - InfoWindow Y 轴偏移量 listener 
					 *  - InfoWindow 点击监听者
					 *  InfoWindow 点击的时候 消失。
					 * */
					InfoWindow infoWindow = new InfoWindow(descriptor, marker
							.getPosition(), -60, new OnInfoWindowClickListener() {

						public void onInfoWindowClick() {
							// TODO Auto-generated method stub
							// 当用户点击 弹窗 触发：
							// 开启 POI 检索、 开启 路径规矩, 跳转界面！

							// 1 隐藏 弹窗！
							baiduMap.hideInfoWindow();
						}
					});

					// 2 show infoWindow
					baiduMap.showInfoWindow(infoWindow);

					return false;
					
				}
			});
		}
		

	/**
	 * 	// 3 获取 兴趣点：
	 * 	点击 本地的 图标的时候 开始 所搜数据。
	 * 	TODO 1 把兴趣点 显示在地图上
	 *　 TODO 2 点击一个兴趣点 弹窗显示 该点的名字！
	 */
	private void getPoiResourse() {
//		设置poi检索监听者
		poiSearch.setOnGetPoiSearchResultListener(
				new OnGetPoiSearchResultListener() {
					@Override
					public void onGetPoiResult(PoiResult arg0) {
						// TODO 获取到 兴趣结果数据时 触发
						// bind to baidumap
						// PoiOverlay poiOverlay = new PoiOverlay(baiduMap);
						//显示所有的兴趣点结果:
						MyPoiOvrely poiOverlay = new MyPoiOvrely(baiduMap);
						//MyPoiOvrely 实现了 new OnMarkerClickListener() 所以点击覆盖物的时候 会触发事件！
						baiduMap.setOnMarkerClickListener(poiOverlay);

						poiOverlay.setData(arg0);
						poiOverlay.addToMap();
						// 价格地图缩放至 可以显示 全部情趣店的级别！
						poiOverlay.zoomToSpan();
					}

					@Override
					public void onGetPoiDetailResult(PoiDetailResult arg0) {
						// TODO 但获取 兴趣  详情时 触发
						
						String url = arg0.getDetailUrl();
						
//						设置 webView 显示的客户端！
						webView.getSettings().setJavaScriptEnabled(true);
						webView.setScrollBarStyle(0);
						WebSettings webSettings = webView.getSettings();
						webSettings.setAllowFileAccess(true);
						webSettings.setBuiltInZoomControls(true);
						webView.setWebViewClient(new WebViewClient(){
							@Override
							public boolean shouldOverrideUrlLoading(
									WebView view, String url) {
								// TODO Auto-generated method stub
								return false;
							}
						});
						//加载数据
						webView.loadUrl(url);
					}
				});
	}

	
//	自定义 PoiOverlay：
	class MyPoiOvrely extends PoiOverlay {

		public MyPoiOvrely(BaiduMap arg0) {
			super(arg0);
			// TODO Auto-generated constructor stub
		}

//		1 需要 点击事件：兴趣点的位置 索引！
		@Override
		public boolean onPoiClick(int index) {
			// TODO Auto-generated method stub
//			 获取当前 覆盖层的Poi结果对象PoiResult. 
			PoiResult result =  getPoiResult();
			
//			在获取 PoiResult 中的List《poiInfo》 集合数据！
			List<PoiInfo> lists =result.getAllPoi();
			
//			根据索引 获取 集合中的 数据信息！ 兴趣点对象数据对象。
			final PoiInfo info = lists.get(index);

// 			弹出弹窗 弹窗中 内容： poiinfo name！
			baiduMap.setOnMarkerClickListener(new OnMarkerClickListener() {

				@Override
				public boolean onMarkerClick(Marker marker) {
					// TODO Auto-generated method stub
//					点击获取 显示详细信息！
					getPoiInfo(info);

					return false;
				}

				/**
				 * 点击获取 显示详细信息！
				 * @param info
				 * @param marker
				 */
				private void getPoiInfo(final PoiInfo info) {
					
					// 设置弹窗 (View arg0, LatLng arg1, int arg2) y 偏移量 ，
					Button btn = new Button(getApplicationContext());
					
					btn.setBackgroundColor(Color.GREEN);
					btn.setText(info.name);
					// btn 变成 View 图片
					BitmapDescriptor descriptor = BitmapDescriptorFactory
							.fromView(btn);

					/**
					 * 弹窗的点击事件：
					 * 
					 * bd - InfoWindow 展示的bitmap position - InfoWindow 显示的地理位置
					 * yOffset - InfoWindow Y 轴偏移量 listener - InfoWindow 点击监听者
					 * 
					 * */
					InfoWindow infoWindow = new InfoWindow(
							descriptor, 
							info.location, 
							-60,
							new OnInfoWindowClickListener() {
								
								public void onInfoWindowClick() {
									//TODO 当用户点击 弹窗 触发：
									// 开启 POI 检索、 开启 路径规矩， 跳转界面！
									// 在弹窗监听器中  二次检索兴趣点的详情！
									//  开始搜索： 
									poiSearch.searchPoiDetail(
											new PoiDetailSearchOption().poiUid(info.uid));

									// 1 隐藏 弹窗！
									baiduMap.hideInfoWindow();
								}
							});

					// 2 show infoWindow
					baiduMap.showInfoWindow(infoWindow);
				}
			});
			
			return super.onPoiClick(index);
		}
	}

	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

		mapView.onResume();
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();

		mapView.onPause();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();

		mapView.onDestroy();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
