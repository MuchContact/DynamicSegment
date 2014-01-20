package org.geo.db;

import javax.naming.Context;
import javax.naming.NamingException;
import javax.sql.DataSource;

import org.geo.configure.CommonConfig;
import org.geo.configure.DBConfig;

public class DB {
	
	private static DataSource pool;
	
	static {
		
			/**
			 * context: context是一套name-to-object的绑定(bindings),可以理解为层次或目录。
			 * 在使用命名和目录服务时获得initial context是对整个名字空间操作的入口。
			 */
			Context env = null;
			try {
				
				env = ContextFactory.getContext();
				CommonConfig ccf=new  DBConfig();
				//端口号
				String jdbc=ccf.getInitParameter("JDBC");
				//获取连接池资源
				pool = (DataSource)env.lookup(jdbc);

				if(pool==null)
					System.err.println("'DBPool' is an unknown DataSource");
			} catch(NamingException ne) {
				ne.printStackTrace();
			}
	}

	public static DataSource getPool() {
		return pool;
	}	
}
