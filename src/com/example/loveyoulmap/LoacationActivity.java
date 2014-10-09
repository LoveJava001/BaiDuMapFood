package com.example.loveyoulmap;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.model.LatLng;

import android.animation.ArgbEvaluator;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

public class LoacationActivity extends Activity {

	
		// MapView
		private MapView mapView;
		// 百度地图 MapView.getMap();
		private BaiduMap baiduMap;
		
		// 客户端
		private LocationClient locationClient;
		
		public LoacationActivity() {
			// TODO Auto-generated constructor stub
		}
	
		@Override
		protected void onCreate(Bundle savedInstanceState) {
			// TODO Auto-generated method stub
			super.onCreate(savedInstanceState);
			
			// 去掉标题的显示(就是app 最顶上的显示内容！)
					requestWindowFeature(1);
	
			// 在使用SDK各组件之前初始化context信息，传入ApplicationContext
			// 注意该方法要再setContentView方法之前实现
				SDKInitializer.initialize(getApplicationContext());

				setContentView(R.layout.activity_main);

				// 获取 MapView
				mapView = (MapView) this.findViewById(R.id.map);

				getMapLocation();
				
				baiduMap = mapView.getMap();		
		
		}
		
		private void getMapLocation()
		{
			//1
			locationClient = new LocationClient(getApplicationContext());
			
			//2
			LocationClientOption option = new LocationClientOption();
			
			//设置坐标 type bf09ll
			option.setCoorType("bd09ll");
			
			//没三秒 定位
			option.setScanSpan(5000);
			
			//设置定位模式   高精度  wg , 低功耗 w  进设备 g     g :  GPS   w: wifi
			option.setLocationMode(LocationMode.Hight_Accuracy);
			//设置是否需要地址信息
			option.setIsNeedAddress(true);
			
			//3 cleint  设置参数
			locationClient.setLocOption(option);
			
			//4 cleint  设置  注册监听器  
			locationClient.registerLocationListener(new BDLocationListener() {
				
				@Override
				public void onReceiveLocation(BDLocation location) {
					// TODO 接收到信息 触发
					// 1 获取位置    2 设置点（使用经纬度）
					Log.i("lvoe", "经纬度：="+location.getLatitude()+ " -- "+location.getLongitude());
					
					LatLng latLng = new LatLng(location.getLatitude(),location.getLongitude());
					
				}
			});
			
			//5 开启客户端：
			locationClient.start();
			
			//6 定位
			if(locationClient.isStarted() && locationClient != null)
			{
				 locationClient.requestLocation();
			}
			
		}
	
	

}
