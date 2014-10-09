package com.example.loveyoulmap;

import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.widget.Button;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMap.OnMarkerClickListener;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.InfoWindow.OnInfoWindowClickListener;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.overlayutil.PoiOverlay;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.poi.PoiCitySearchOption;
import com.baidu.mapapi.search.poi.PoiDetailSearchOption;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;

public class MyPoiOvrely extends PoiOverlay {

	private Context context;
	private PoiSearch poiSearch;
	private BaiduMap baiduMap;
	
	public MyPoiOvrely(BaiduMap baiduMap,Context context,PoiSearch poiSearch) {
		super(baiduMap);
		// TODO Auto-generated constructor stub
		this.context = context;
		this.poiSearch = poiSearch;
	}

//	1 需要 点击事件：兴趣点的位置 索引！
	@Override
	public boolean onPoiClick(int index) {
		// TODO Auto-generated method stub

//		 获取当前 覆盖层的Poi结果对象PoiResult. 
		PoiResult result =  getPoiResult();
//		在获取 PoiResult 中的List《poiInfo》 集合数据！
		List<PoiInfo> lists =result.getAllPoi();
//		根据索引 获取 集合中的 数据信息！ 兴趣点对象数据对象。
		final PoiInfo info = lists.get(index);

		// 弹出弹窗 弹窗中 内容： poiinfo name！
		baiduMap.setOnMarkerClickListener(new OnMarkerClickListener() {

			@Override
			public boolean onMarkerClick(Marker marker) {
				// TODO Auto-generated method stub

//				点击获取 显示详细信息！
				getPoiInfo(info);

				return false;
			}

			/**
			 * 点击获取 显示详细信息！
			 * @param info
			 * @param marker
			 */
			private void getPoiInfo(final PoiInfo info) {
				
				// 开启检索
				poiSearch.searchInCity(new PoiCitySearchOption()
						.keyword("美食").city("北京").pageNum(0)
						.pageCapacity(10));

				// View
				// 设置弹窗 (View arg0, LatLng arg1, int arg2) y 偏移量 ，
				Button btn = new Button(context);
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
								// TODO Auto-generated method stub
								// 当用户点击 弹窗 触发：
								Log.i("lvoe", "弹窗 被点击");
								// 开启 POI 检索、 开启 路径规矩， 跳转界面！
								
								// 在弹窗监听器中  二次检索兴趣点的详情！
//								开始搜索： 
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
