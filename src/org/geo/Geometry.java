package org.geo;

import org.geo.dao.UserBeanTwo;
import org.geo.utils.LatLngUtil;

public class Geometry {

    /**
     * 点到直线的最短距离的判断 点（x0,y0） 到由两点组成的线段（x1,y1） ,( x2,y2 )
     * @param x1
     * @param y1
     * @param x2
     * @param y2
     * @param x0
     * @param y0
     * @return
     */
    private double minDistanceBetweenPoint2Segment(double x1, double y1, double x2, double y2,
    		double x0, double y0) {
        double space = 0;

        double a, b, c;

        a = lineSpace(x1, y1, x2, y2);// 线段的长度

        b = lineSpace(x1, y1, x0, y0);// (x1,y1)到点的距离

        c = lineSpace(x2, y2, x0, y0);// (x2,y2)到点的距离

        if (c+b == a) {//点在线段上

           space = 0;

           return space;

        }

        if (a <= 0.000001) {//不是线段，是一个点

           space = b;

           return space;

        }

        if (c * c >= a * a + b * b) { //组成直角三角形或钝角三角形，(x1,y1)为直角或钝角

           space = b;

           return space;

        }

        if (b * b >= a * a + c * c) {//组成直角三角形或钝角三角形，(x2,y2)为直角或钝角

           space = c;

           return space;

        }
        //组成锐角三角形，则求三角形的高
        double p = (a + b + c) / 2;// 半周长

        double s = Math.sqrt(p * (p - a) * (p - b) * (p - c));// 海伦公式求面积

        space = 2 * s / a;// 返回点到线的距离（利用三角形面积公式求高）

        return space;

    }
    /**
     * 计算两点之间的距离
     * @param x1
     * @param y1
     * @param x2
     * @param y2
     * @return
     */
    private double lineSpace(double x1, double y1, double x2, double y2) {

        double lineLength = 0;

        lineLength = Math.sqrt((x1 - x2) * (x1 - x2) + (y1 - y2)

               * (y1 - y2));

        return lineLength;

    }
    /**
     * 判断点是否是在线的2个端点内
     * 线上2端点(x1,y1,x2,y2).点(x0,y0)
     * @param x1
     * @param y1
     * @param x2
     * @param y2
     * @param x0
     * @param y0
     * @return
     */
    private Boolean pointBetweenTwoPoint(double x1, double y1, double x2, double y2, 
    		double x0, double y0) {
        return (Math.abs(x0 - x1) <= Math.abs(x1 - x2) && Math.abs(x0 - x2) <= Math.abs(x1 - x2)
      && Math.abs(y0 - y1) <= Math.abs(y1 - y2) && Math.abs(y0 - y2) <= Math.abs(y1 - y2));
    }
    /**
     * 计算一个点到线上最近的拐点的索引号
     * @param lnglatstr 经纬度坐标字符串(如:123,23;122,32),x:经度,y纬度
     * @param x
     * @param y
     */
    public UserBeanTwo getNeastPointBetweenPoint2Polyline(String lineStr, double x, double y) {
      String xyarr[] = lineStr.split(";");
      double mindis = -1;
      MPoint p1 = new MPoint(0, 0);
      MPoint p2 = new MPoint(0, 0);
      int index=0;
      for (int i = 1; i < xyarr.length; i++) {
          double x1 = Double.valueOf(xyarr[i - 1].split(",")[0]);
          double y1 = Double.valueOf(xyarr[i - 1].split(",")[1]);
          double x2 = Double.valueOf(xyarr[i].split(",")[0]);
          double y2 = Double.valueOf(xyarr[i].split(",")[1]);
          
		  double dis = minDistanceBetweenPoint2Segment(x1, y1, x2, y2, x, y);
	      if (mindis == -1 || dis < mindis) {
	          mindis = dis;
	          index=i;
	          p1.setX(x1);
	          p1.setY(y1);
	          p2.setX(x2);
	          p2.setY(y2);
	      }
      }

      MPoint pnt = minReachPointBetweenPoint2Segment(p1.getX(),p1.getY(),p2.getX(),p2.getY(),x,y);
      return new UserBeanTwo(pnt.getX(), pnt.getY(), index);
      
    }
    /**
     * 计算点到线段上最近的点坐标，垂足或者端点
     * @param x1
     * @param y1
     * @param x2
     * @param y2
     * @param x
     * @param y
     * @return
     */
    private MPoint minReachPointBetweenPoint2Segment(double x1, double y1, double x2, double y2, 
    		double x, double y) {
        if(pointBetweenTwoPoint(x1,y1,x2,y2,x,y)){
        	double a = (y1 - y2) / (x1 - x2);
            double b = y1 - a * x1;
            double m = x + a * y;

            double x0 = (m - a * b) / (a * a + 1);
            double y0 = a * x0 + b;
        	return new MPoint(x0,y0);
        }else{
        	double d1 = lineSpace(x,y,x1,y1);
        	double d2 = lineSpace(x,y,x2,y2);
        	
        	return d1<d2?new MPoint(x1,y1):new MPoint(x2,y2);
        }
        
    }
    /**
     * @param lineStr
     * @param index
     * @return
     */
    public String subString(String lineStr, int index){
    	StringBuffer buffer = new StringBuffer();
    	String xyarr[] = lineStr.split(";");
    	if(index>=xyarr.length-1){
    		return null;
    	}
    	for(int i=index;i<xyarr.length;i++){
    		buffer.append(xyarr[i]+";");
    	}
    	return buffer.toString();
    }
    /**
     * @param lineStr
     * @param distance
     * @param scale
     * @param referenceSrc
     * @return
     */
    public MPoint getPointOnLineByDistance(String lineStr, double distance, String scale, 
    		String referenceSrc) {
    	
    	MPoint point = null;
    	double detaDistance = 0.0d;
    	
    	String xyarr[] = lineStr.split(";");
    	double xx1 = Double.valueOf(xyarr[0].split(",")[0]);
        double yy1 = Double.valueOf(xyarr[0].split(",")[1]);
        if(distance <= 50 ){
        	point = new MPoint(xx1, yy1);
        	
        }else{
        	for (int i = 1; i < xyarr.length; i++) {
                double x1 = Double.valueOf(xyarr[i - 1].split(",")[0]);
                double y1 = Double.valueOf(xyarr[i - 1].split(",")[1]);
                double x2 = Double.valueOf(xyarr[i].split(",")[0]);
                double y2 = Double.valueOf(xyarr[i].split(",")[1]);
                double tempDistance = detaDistance + LatLngUtil.gps2m(x1, y1, x2, y2);
      		    if(Math.abs(tempDistance-distance)<=50){
      		    	point = new MPoint(x2, y2);
      		    	
      		    }else{
      		    	if(tempDistance>distance){
      		    		double degreeDistance = LatLngUtil.m2gps(tempDistance-distance);
      		    		point = getPositionOnLineByDistance(x1,y1,x2,y2,degreeDistance);
      		    		
      		    		break;
      		    	}else{
      		    		detaDistance = tempDistance;
      		    	}
      		    }
                
            }
        }

        return point;
    }
    /**
     * 获取带扩展信息的最近点坐标，包括该点左右节点的坐标。
     * @param lineStr
     * @param distance
     * @param scale
     * @param referenceSrc
     * @return
     */
    public String getExtentedPointOnLineByDistance(String lineStr, double distance, String scale, 
    		String referenceSrc) {
    	String pnt="";
    	MPoint point = null;
    	double detaDistance = 0.0d;
    	
    	String xyarr[] = lineStr.split(";");
    	double xx1 = Double.valueOf(xyarr[0].split(",")[0]);
        double yy1 = Double.valueOf(xyarr[0].split(",")[1]);
        if(distance <= 50 ){
        	point = new MPoint(xx1, yy1);
        	pnt=xx1+","+yy1+";"+xx1+","+yy1;
        }else{
        	for (int i = 1; i < xyarr.length; i++) {
                double x1 = Double.valueOf(xyarr[i - 1].split(",")[0]);
                double y1 = Double.valueOf(xyarr[i - 1].split(",")[1]);
                double x2 = Double.valueOf(xyarr[i].split(",")[0]);
                double y2 = Double.valueOf(xyarr[i].split(",")[1]);
                double tempDistance = detaDistance + LatLngUtil.gps2m(x1, y1, x2, y2);
      		    if(Math.abs(tempDistance-distance)<=50){
      		    	point = new MPoint(x2, y2);
      		    	pnt=x1+","+y1+";"+x2+","+y2;
      		    }else{
      		    	if(tempDistance>distance){
      		    		double degreeDistance = LatLngUtil.m2gps(tempDistance-distance);
      		    		point = getPositionOnLineByDistance(x1,y1,x2,y2,degreeDistance);
      		    		pnt=x1+","+y1+";"+x2+","+y2;
      		    		break;
      		    	}else{
      		    		detaDistance = tempDistance;
      		    	}
      		    }
                
            }
        }

        return point.toString()+"@"+pnt;
    }
    /**
     * 获取线上距离起点一定距离的点坐标
     * @param x1
     * @param y1
     * @param x2
     * @param y2
     * @param distance
     * @return
     */
    private MPoint getPositionOnLineByDistance(double x1, double y1, double x2, double y2, double distance) {
    	double radian = getLineRadian(x1,y1,x2,y2);
    	double x = x1+distance*Math.cos(radian);
    	double y = y1+distance*Math.sin(radian);
    	return new MPoint(x,y);
    }
    /**
     * 获取（矢量）线的倾角，线方向由（x1,y1）到（x2,y2）
     * @param x1
     * @param y1
     * @param x2
     * @param y2
     * @return
     */
    private double getLineRadian(double x1, double y1, double x2, double y2){
    	Boolean flag = false;
    	double radian=0;
    	if(x2<x1){
    		double tx = x1;
    		double ty = y1;
    		x1=x2;x2=tx;
    		y1=y2;y2=ty;
    		flag = true;
    	}
    	if(x1==x2){
			if(y1>y2){
				radian = 0;
			}else{
				radian = Math.PI;
			}
			return radian;
		}
    	if(y1==y2){
    		return Math.PI/2;
    	}
    	double k = (y2-y1)/(x2-x1);
    	radian = Math.atan(k);
    	if(flag){
    		if(k<0){
    			radian = radian+2*Math.PI;
    		}else{
    			radian = radian+Math.PI;
    		}
    		
    	}else{
			if(k<0){
				radian = radian+Math.PI;
			}
		}
    	return radian;
    }
    /**
     * @param radian
     * @return
     */
    public double radian2Angle(double radian){
    	return radian*360/(2*Math.PI);
    }
    /**
     * @param angle
     * @return
     */
    public double angle2Radian(double angle){
    	return angle*Math.PI/180;
    }
   
    
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Geometry geo = new Geometry();
		System.out.println(geo.radian2Angle(Math.atan(-1000)));
		System.out.println(geo.subString("112,30;114,43;113,28",2));
		//Math.
	}

}
