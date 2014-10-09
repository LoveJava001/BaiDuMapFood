package com.example.loveyoulmap;

import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.overlayutil.DrivingRouteOverlay;
import com.baidu.mapapi.search.route.DrivingRouteLine;
import com.baidu.mapapi.search.route.DrivingRouteLine.DrivingStep;
import com.baidu.mapapi.search.route.DrivingRoutePlanOption;
import com.baidu.mapapi.search.route.DrivingRouteResult;
import com.baidu.mapapi.search.route.OnGetRoutePlanResultListener;
import com.baidu.mapapi.search.route.PlanNode;
import com.baidu.mapapi.search.route.RoutePlanSearch;
import com.baidu.mapapi.search.route.TransitRouteResult;
import com.baidu.mapapi.search.route.WalkingRouteResult;

public class RotePlanActivity extends Activity {

	// MapView
	private MapView mapView;
	// 百度地图 MapView.getMap();
	private BaiduMap baiduMap;

	private RoutePlanSearch routePlanSearch;
	
	private DrivingRouteLine line;
	
	public RotePlanActivity() {
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

		baiduMap = mapView.getMap();
		
		// 获取路线规划。
		getRoutePlanRsult();
		
	}

	private void getRoutePlanRsult() {
		//		1 初始化 路线
				routePlanSearch = RoutePlanSearch.newInstance();
		//		2 设置监听器: 当触发事件的 回调对应的函数 获取信息！
				routePlanSearch.setOnGetRoutePlanResultListener(new OnGetRoutePlanResultListener() {
					
					@Override
					public void onGetWalkingRouteResult(WalkingRouteResult arg0) {
						// TODO 当获取到 步行路线
						
					}
					
					@Override
					public void onGetTransitRouteResult(TransitRouteResult arg0) {
						// TODO 公交 地铁
						
					}
					
					@Override
					public void onGetDrivingRouteResult(DrivingRouteResult arg0) {
						// TODO 私家车
						//DrivingRouteOverlay drivingRouteOverlay = new  DrivingRouteOverlay(baiduMap);
						MyRouteOverlay drivingRouteOverlay = new  MyRouteOverlay(baiduMap);
						// 通过 getRouteLines（）获取到一个包含所有路线的List集合  
						// java.util.List<DrivingRouteLine> 	getRouteLines()
						
						List<DrivingRouteLine> lists = arg0.getRouteLines();
						// get(0) 获取第一个建议的路线：
					    line = lists.get(0);
						// 设置路线
						drivingRouteOverlay.setData(line);
						// 设置到 bitmap
						drivingRouteOverlay.addToMap();
					}
				});
				
		//		3 开启检索：
				routePlanSearch.drivingSearch(
						new DrivingRoutePlanOption().from(PlanNode.withCityNameAndPlaceName("烟台", "烟台大学"))
						.to(PlanNode.withCityNameAndPlaceName("烟台", "烟台大学文经学院")));
	}
	
//	4 自定义的 DrivingRouteOverlay 自驾车的 覆盖层：
	class MyRouteOverlay extends DrivingRouteOverlay
	{

		public MyRouteOverlay(BaiduMap arg0) {
			super(arg0);
			// TODO Auto-generated constructor stub
		}

//		点击 获取 当前的路线 信息。
		@Override
		public boolean onRouteNodeClick(int position) {
			// TODO Auto-generated method stub
			// 获取包含的那个气那路线的所有 节段的集合！
			List<DrivingStep> listSteps = line.getAllStep();
			// 获取当前点击出的 step 对象 和 信息。
			DrivingStep step = listSteps.get(position);
			Log.i("love", "----step.getInstructions()="+step.getInstructions());
			return super.onRouteNodeClick(position);
		}
		
	}

}
