package com.rainbow.bridge.estarget.utils;

import com.rainbow.bridge.core.constant.CommonCons;
import com.rainbow.bridge.core.entity.Column;
import com.rainbow.bridge.core.entity.OriginCol;
import com.rainbow.bridge.core.entity.Pk;
import com.rainbow.bridge.estarget.param.EsParam;
import com.rainbow.bridge.targetcore.model.Param;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.sql.*;
import java.util.*;

/**
 * @author gujiachun
 */
public class JdbcUtil {

    private static final Logger logger = LoggerFactory.getLogger(JdbcUtil.class);

    public static List<Map<String, Object>> select(DataSource ds, Param param) throws SQLException {
        if (param == null){
            return null;
        }

        Connection conn = null;
        PreparedStatement pst = null;
        try {
            // 获得连接
            conn = ds.getConnection();
            pst = buildSelectStatement(conn, param);
            if (pst != null) {
                // 执行
                ResultSet rs = pst.executeQuery();
                return convertList(rs);
            }
        } catch (SQLException e) {
            logger.error("执行sql异常:{}",e.getMessage());
            throw e;
        } finally {
            close(pst,conn);
        }
        return null;
    }

    public static List<Map<String, Object>> convertList(ResultSet rs) {
        List<Map<String, Object>> list = new ArrayList<>();
        try {
            ResultSetMetaData md = rs.getMetaData();
            int columnCount = md.getColumnCount();
            while (rs.next()) {
                Map<String, Object> rowData = new HashMap<>(50);
                for (int i = 1; i <= columnCount; i++) {
                    rowData.put(md.getColumnLabel(i), rs.getObject(i));
                }
                list.add(rowData);
            }
        } catch (SQLException e) {
            logger.error("rs转换list map异常:{}",e.getMessage());
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                rs = null;
            } catch (SQLException e) {
                logger.error("rs close异常:{}",e.getMessage());
            }
        }
        return list;
    }

    private static void close(PreparedStatement pst,Connection conn){
        try {
            if (pst!= null && !pst.isClosed()) {
                pst.close();
                logger.info("pst已正常关闭！");
            }
            if (conn != null && !conn.isClosed()) {
                conn.close();
                logger.info("connection已正常关闭！");
            }
        } catch (Exception e) {
            logger.error("!!!连接池关闭发生异常!!!" + e.getMessage());
        }
    }

    private static PreparedStatement buildSelectStatement(Connection conn,Param param) throws SQLException {
        EsParam entity = (EsParam) param;
        String sql = entity.getSqlFormat();
        logger.info(">>>>>>>执行sql:{}",sql);
        try{
            // 调用SQL
            PreparedStatement pst = conn.prepareStatement(sql);
            String sqlField = entity.getSqlFieldFormat();
            if (StringUtils.isNotBlank(sqlField)){
                String[] split = sqlField.split(CommonCons.split);
                int i = 1;
                for (String v : split) {
                    pst.setObject(i, v);
                    i++;
                }
            }
            return pst;
        }catch (Exception e){
            logger.error("构建PreparedStatement异常:{}",e.getMessage());
            throw e;
        }
    }
}
