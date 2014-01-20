package org.geo.db;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import org.geo.configure.CommonConfig;
import org.geo.configure.DBConfig;

/**
 * @author lenovo
 * 依赖common.properties配置文件，配置字段为webserver
 *
 */
public class ContextFactory {
	private static Context env;
	
	static {
		
			/**
			 * context: context是一套name-to-object的绑??bindings),可以理解为层次或目录??
			 * 在使用命名和目录服务时获得initial context是对整个名字空间操作的入口??
			 */
		try {
			CommonConfig ccf=new  DBConfig();
			//端口号
			String queryPort=ccf.getInitParameter("WEBSERVER");
			
			if(queryPort==null){
				env = (Context) new InitialContext().lookup("java:comp/env");
				
			}else if(queryPort.equalsIgnoreCase("tomcat")){
				env = (Context) new InitialContext().lookup("java:comp/env");
				
			}else if(queryPort.equalsIgnoreCase("weblogic")){
				env = (Context) new InitialContext();
				
			}
			
		} catch(NamingException ne) {
			ne.printStackTrace();
		}
	}

	public static Context getContext() {
		return env;
	}

}
