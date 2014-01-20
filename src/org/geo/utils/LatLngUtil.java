package org.geo.utils;

import org.geo.MPoint;

/**
 * 经纬度操作类，提供：
 * 	①经纬度距离求解，根据球面距离公式计算（非椭球体）
 *  ②经纬度距离求解，根据椭球体弧面距离求解
 *  ③经纬度距离求解，先转换为高斯平面坐标，然后根据距离公式求解
 * @author Administrator
 *
 */
public class LatLngUtil
{	
	/**
	 * WGS84椭球体长半轴长度，单位：米
	 */
	public static double RADIUS = 6378137;
	
	public double Lat = 0.0;
	public double Lng = 0.0;
	
	public LatLngUtil(double pLat,double pLng)
	{
	  Lat = pLat;
	  Lng = pLng;
	}

	/**
	 * 两点间距离公式
	 * @param p1
	 * @param p2
	 * @return 距离，单位不定
	 */
	public  static double GetDistance(MPoint p1,MPoint p2)
	{
	  return Math.sqrt((p2.x - p1.x) * (p2.x - p1.x) + (p2.y - p1.y) * (p2.y - p1.y));
	}
	
	/**
	 * 经纬度距离求解，先转换为高斯平面坐标，然后根据距离公式求解
	 * @param AlatLng
	 * @param BlatLng
	 * @return
	 */
	public static double GetDistance(LatLngUtil AlatLng,LatLngUtil BlatLng)
	{
	  double rbc = 0;
	  MPoint pnt1 = BlToGs(AlatLng);
	  MPoint pnt2 = BlToGs(BlatLng);
	  //rbc = GetDistance(xgA, ygA, xgB, ygB);
	  rbc = GetDistance(pnt1,pnt2);
	  
	  return rbc;
	}
	
	/**
	 * 经纬度转为高斯平面直角坐标(bl单位度)  ，然后利用距离公式求解两点距离
	 * @param lon
	 * @param lat
	 * @return
	 */
	private static MPoint BlToGs(double lon, double lat)         
	{
		  int dh;//带号   
		  double temp1, temp2, a, b, c, d, e, f;
		  double l1, l2;
		  double xg, yg;
		  int n;
		  n = (int)(lon / 6);
		  dh = n + 1;
		  l2 = 3 + 6 * n;
		  l1 = lon - l2;
		  temp1 = lat * Math.PI / 180;
		  temp2 = l1 * Math.PI / 180;
		  a = 6367558.497 * temp1;
		  b = (16036.480 - 1597237.956 * Math.pow(temp2, 2) - 268563.280 * Math.pow(temp2, 4)) * Math.sin(2 * temp1);
		  c = (16.828 - 1340.831 * Math.pow(temp2, 2) + 201450.536 * Math.pow(temp2, 4)) * Math.sin(4 * temp1);
		  xg = a - b + c;
		  d = (6383594.975 + 535998.795 * Math.pow(temp2, 2) + 54206.791 * Math.pow(temp2, 4)) * Math.cos(temp1);
		  e = (5356.713 - 534204.967 * Math.pow(temp2, 2) - 134966.691 * Math.pow(temp2, 4)) * Math.cos(3 * temp1);
		  f = (6.744 + 81276.496 * Math.pow(temp2, 4)) * Math.cos(5 * temp1);
		  yg = (temp2) * (d - e + f) + 500000 + dh * 1000000;
		  return new MPoint(xg, yg);
	}
	/**
	 * 经纬度转高斯坐标
	 * @param p1
	 * @param p2
	 * @return
	 */
	public static MPoint BlToGs(LatLngUtil p1)         
	{
		return BlToGs(p1.Lng,p1.Lat);
	      
	}
	

	
//	public static double distanceDegree(double lon1, double lat1, double lon2, double lat2) {
//		return(distance(Math.toRadians(lon1), Math.toRadians(lat1), Math.toRadians(lon1), Math.toRadians(lat1)));
//	}
//	
//	public static double distance(double lon1, double lat1, double lon2, double lat2) {//(弧度)
//		return(RADIUS * Math.acos(Math.sin(lat1)*Math.sin(lat2) + Math.cos(lat1)*Math.cos(lat2)*Math.cos(lon2-lon1)));
//	}
	
	
	public static void print(Object obj){
		System.out.println(obj);
	}
	
	/**
	 * 球面距离求解函数，单位：米
	 * @param lng_a
	 * @param lat_a
	 * @param lng_b
	 * @param lat_b
	 * @return
	 */
	public static double gps2m(double lng_a, double lat_a, double lng_b, double lat_b) {  
       double radLat1 = (lat_a * Math.PI / 180.0);  
       double radLat2 = (lat_b * Math.PI / 180.0);  
       double a = radLat1 - radLat2;  
       double b = (lng_a - lng_b) * Math.PI / 180.0;  
       double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2)  
              + Math.cos(radLat1) * Math.cos(radLat2)  
              * Math.pow(Math.sin(b / 2), 2)));  
       s = s * RADIUS;  
       s = Math.round(s * 10000) / 10000;  
       return s;  
    }
	
	/**
	 * 球面距离反算，根据米制距离求度差
	 * @param distance
	 * @return
	 */
	public static double m2gps(double distance) {  
		return distance/gps2m(0,0,1,0);
	}
	
	public static void main(String[] args) {
	  
	  LatLngUtil l1 = new LatLngUtil(114.032378,30.636730);
	  LatLngUtil l2 = new LatLngUtil(114.04038,30.632350);
	  LatLngUtil l3 = new LatLngUtil(114.274294,30.461700);
	  print(GetDistance(l1,l2));
	  print(gps2m(l1.Lng,l1.Lat,l2.Lng,l2.Lat));
	  
	  print(GetDistance(l1,l3));
	  print(gps2m(l1.Lng,l1.Lat,l3.Lng,l3.Lat));
	  
	  print(m2gps(100000));
	  //print(getDistance(l1.Lng, l1.Lat, l2.Lng, l2.Lat));
	  
	}
}