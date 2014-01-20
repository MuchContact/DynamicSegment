package org.geo.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.geo.db.DBEntry;


/**
 * 
 * @author lenovo
 *
 */
public class LXJDDaoTest {

	private String getSegmentCoordinateSql(double x, double y, double detax, double detay){
		double abx,abx2,aby,aby2;
		abx = x-detax;
		abx2 = x+detax;
		aby = y-detay;
		aby2 = y+detay;
		return "select id,x,y from lxjd where id not in(" +
					"select id from lxjd t where "+abx+">=t.maxx or " +
					abx2+"<=t.minx or "+aby+">=t.maxy or "+aby2+"<=t.miny " +
					"or minx is null) order by id asc" ;
	}
	
	/**
	 * 获取总的记录数
	 * @param sql
	 * @return
	 */
	public String getSegment(double x, double y, double detax, double detay){
		//获取执行脚本
		String sql = getSegmentCoordinateSql(x,y,detax,detay);
		
		StringBuffer buffer = new StringBuffer();
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			conn = DBEntry.getConnection();
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			
			while (rs.next()) {
				buffer.append(rs.getFloat(2)+","+rs.getFloat(3)+";");
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if(rs!=null){
					rs.close();
				}
				if(pstmt!=null){
					pstmt.close();
				}
				if(conn!=null){
					conn.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
		}
		return buffer.toString();
	}
}
