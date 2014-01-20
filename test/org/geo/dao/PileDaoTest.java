package org.geo.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.geo.db.DBEntry;

public class PileDaoTest {
	private String getSql(double pileName, String belongedLine, int flag){
		switch(flag){
			case 1:
				return "select count(zh) from lcz_d where ZH<"+pileName+" and SSLX=1" ;
				
			case 2:
				return "select count(zh) from lcz_d where ZH="+pileName+" and SSLX=1" ;
				
			case 3:
				return "select XZB,YZB,zh from lcz_d where zh in(select max(zh) from lcz_d where ZH<"+pileName+" and SSLX=1)" ;		
				
			case 4:
				return "select XZB,YZB from lcz_d where ZH="+pileName+" and SSLX=1" ;
			default:
				return null;
		}
	}
	
	public UserBeanOne queryProperPile(double pileName, String belongedLine){
		//获取执行脚本
		UserBeanOne bean = null;
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			conn = DBEntry.getConnection();
			//第一步，查询是否由正好匹配的桩点
			String sql = getSql( pileName, belongedLine,4);
			pstmt = conn.prepareStatement(sql);
			
			rs = pstmt.executeQuery();
			
			if(rs.next()){
				//如果正好有桩号可以使用
				bean = new UserBeanOne(rs.getFloat(1), rs.getFloat(2), 0);
			}else{
				
				sql = getSql( pileName, belongedLine,1);
				
				pstmt = conn.prepareStatement(sql);
				
				rs = pstmt.executeQuery();
				if(rs.next()){
					//如果范围查询查到数值
					if(rs.getInt(1)>0){
						sql = getSql( pileName, belongedLine,3);
						
						pstmt = conn.prepareStatement(sql);
						
						rs = pstmt.executeQuery();
						if(rs.next()){
							double zh = rs.getFloat(3);
							double deta = pileName - zh;
							
							bean = new UserBeanOne(rs.getFloat(1), rs.getFloat(2), deta);
						}
					}
				}
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
		return bean;
	}
}
