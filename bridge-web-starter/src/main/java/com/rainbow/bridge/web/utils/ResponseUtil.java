package com.rainbow.bridge.web.utils;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @description: response工具类
 * @author: yezilong
 * @date: 2019/12/24
 * @version: 1.0
 * @modifyBy: yezilong 2019/12/24
 * @modifyDesc:
 */
public class ResponseUtil {
    public static void write(HttpServletResponse response, Object o)throws Exception{
        response.setContentType("application/json;charset=utf-8");
        PrintWriter out = response.getWriter();
        out.println(o.toString());
        out.flush();
        out.close();
    }
    
    public static void resWrite(HttpServletResponse response, Object o){
        response.setContentType("application/json;charset=utf-8");
        PrintWriter out;
		try {
			out = response.getWriter();
			out.println(o.toString());
	        out.flush();
	        out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
}
