package org.geo.service;

import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import org.geo.Geometry;
import org.geo.MPoint;
import org.geo.dao.LXJDDao;
import org.geo.dao.PileDao;
import org.geo.dao.UserBeanOne;
import org.geo.dao.UserBeanTwo;
import org.geo.db.LocalDB;

@Path("segment")
public class DynamicSegmentService {

	@GET
	@Path("locate")
	@Produces( { "application/xml", "application/json" })
	@Consumes( { "application/xml", "application/json" })
	public MPoint getLocationForPile(
			@DefaultValue("0") @QueryParam("zh") double pileName,
			@QueryParam("ssld") String belongedLine) {
		MPoint point=null;
		PileDao dao = new PileDao();
		UserBeanOne bean = dao.queryProperPile(pileName, belongedLine);
		if(bean==null){
			return point;
		}
		if(bean.getDistance()<=0){
			point =  new MPoint(bean.getX(),bean.getY());
			return point;
		}
		LXJDDao lx = new LXJDDao();
		//第一步利用索引减少比较范围
		String lineStr = lx.getSegment(bean.getX(), bean.getY(), 0.01, 0.01);
		Geometry geoTool = new Geometry();
		UserBeanTwo bean2 = geoTool.getNeastPointBetweenPoint2Polyline(lineStr, bean.getX(), bean.getY());
		String newLineStr = geoTool.subString(lineStr, bean2.getIndex());
		return geoTool.getPointOnLineByDistance(newLineStr, bean.getDistance(), "meter", "wgs84");
		
	}
	@GET
	@Path("lineInPnts")
	@Produces( { "application/xml", "application/json" })
	@Consumes( { "application/xml", "application/json" })
	public String getLineBetweenPiles(
			@DefaultValue("0") @QueryParam("zh1") double pileName1,
			@DefaultValue("0") @QueryParam("zh2") double pileName2,
			@QueryParam("ssld") String belongedLine) {
		String result1 = getExtLocationForPile(pileName1,belongedLine);
		String result2 = getExtLocationForPile(pileName2,belongedLine);
		return "";
	}
	
	public String getExtLocationForPile(double pileName, String belongedLine) {
		MPoint point=null;
		PileDao dao = new PileDao();
		UserBeanOne bean = dao.queryProperPile(pileName, belongedLine);
		if(bean==null){
			return null;
		}
		if(bean.getDistance()<=0){
			point =  new MPoint(bean.getX(),bean.getY());
			return point.toString();
		}
		LXJDDao lx = new LXJDDao();
		//第一步利用索引减少比较范围
		String lineStr = lx.getSegment(bean.getX(), bean.getY(), 0.01, 0.01);
		Geometry geoTool = new Geometry();
		UserBeanTwo bean2 = geoTool.getNeastPointBetweenPoint2Polyline(lineStr, bean.getX(), bean.getY());
		String newLineStr = geoTool.subString(lineStr, bean2.getIndex());
		return geoTool.getExtentedPointOnLineByDistance(newLineStr, bean.getDistance(), "meter", "wgs84");
	}
	private String getPointAfter(){
		
	}
	private String getPointBefore(){
		
	}
	public static void main(String arg[]){
		DynamicSegmentService db=new DynamicSegmentService();
		System.out.println(db.getLocationForPile(1337.4, "1"));
	}
}
