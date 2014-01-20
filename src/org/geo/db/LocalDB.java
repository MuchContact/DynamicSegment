package org.geo.db;

import java.sql.Connection;
import java.sql.DriverManager;

import org.geo.configure.CommonConfig;
import org.geo.configure.DBConfig;

public class LocalDB {
	
	public static Connection getConnection(){

		Connection c=null;
		
		CommonConfig ccf=new  DBConfig();
		//根据配置文件获取不同类型数据库的连接方式
		String queryPort=ccf.getInitParameter("LOCAL_DB_TYPE");
		
		if(queryPort.equalsIgnoreCase("mysql")){
			c = getMySQLConnection();
			
		}else if(queryPort.equalsIgnoreCase("oracle")){
			c = getOracleConnection();
			
		}else{
			c = getSqlServerConnection();
		}
		
		return c;
	}
	/**
	 * @return
	 */
	public static Connection getMySQLConnection(){
		
		Connection c=null;
		try { 
			Class.forName("com.mysql.jdbc.Driver");
			//与url指定的数据源建立连接 ，getConnection参数依次为：URL，用户名，密码
			c = DriverManager.getConnection("jdbc:mysql://localhost:3306/address", "root", "mysql"); 
			
		} catch (Exception e) { 
			e.printStackTrace();
		}
		return c;
	}
	/**
	 * @return
	 */
	public static Connection getOracleConnection(){
		
		Connection c=null;
		try { 
			Class.forName("oracle.jdbc.driver.OracleDriver").newInstance(); 
			//与url指定的数据源建立连接 ，getConnection的连接参数依次为URL（thin方式连接，...@ip:port:数据库名称）,用户名，密码
			 c = DriverManager.getConnection("jdbc:oracle:thin:@192.168.1.116:1521:orcl", "GIS520200000000", "GIS520200000000"); 
			
		} catch (Exception e) { 
			e.printStackTrace();
		}
		return c;
	}
	public static Connection getSqlServerConnection(){
		
		Connection c=null;
		try { 
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver").newInstance(); 
			//与url指定的数据源建立连接 ，getConnection的连接参数依次为URL（thin方式连接，...@ip:port:数据库名称）,用户名，密码
			 c = DriverManager.getConnection("jdbc:sqlserver://192.168.1.60:1433;DatabaseName=GSGL", "sa", "tms"); 
			
		} catch (Exception e) { 
			e.printStackTrace();
		}
		return c;
	}
	public static void main(String arg[]){
		LocalDB db=new LocalDB();
		System.out.println(db.getSqlServerConnection().toString());
	}
}
