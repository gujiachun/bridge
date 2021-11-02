package com.rainbow.bridge.server.utils;

import com.rainbow.bridge.core.entity.*;
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

    public static int _update(DataSource ds,List<UpdateEntity> entities) throws SQLException {
        if (entities == null || entities.size() == 0){
            return 0;
        }

        if (entities.size() == 1){
            return update(ds,entities.get(0));
        }

        return update(ds,entities);
    }

    public static int _insert(DataSource ds,List<InsertEntity> entities) throws SQLException {
        if (entities == null || entities.size() == 0){
            return 0;
        }

        if (entities.size() == 1){
            return insert(ds,entities.get(0));
        }

        return insert(ds,entities);
    }

    public static int _delete(DataSource ds,List<DeleteEntity> entities) throws SQLException {
        if (entities == null || entities.size() == 0){
            return 0;
        }

        if (entities.size() == 1){
            return delete(ds,entities.get(0));
        }

        return delete(ds,entities);
    }

    public static int insert(DataSource ds, List<InsertEntity> entities) throws SQLException {
        if (entities == null || entities.size() == 0){
            return 0;
        }

        // 受影响的行数
        int affectedLine = 0;
        Connection conn = null;
        PreparedStatement pst = null;
        try {
            // 获得连接
            conn = ds.getConnection();
            conn.setAutoCommit(false);
            for (InsertEntity entity : entities) {
                pst = buildInsertStatement(conn, entity);
                if (pst != null) {
                    // 执行
                    affectedLine += pst.executeUpdate();
                }
            }
            conn.commit();
        } catch (SQLException e) {
            logger.error("执行sql异常:{}",e.getMessage());
            rollback(conn);
            throw e;
        } finally {
            close(pst,conn);
        }
        return affectedLine;

    }

    public static int update(DataSource ds, List<UpdateEntity> entities) throws SQLException {
        if (entities == null || entities.size() == 0){
            return 0;
        }

        // 受影响的行数
        int affectedLine = 0;
        Connection conn = null;
        PreparedStatement pst = null;
        try {
            // 获得连接
            conn = ds.getConnection();
            conn.setAutoCommit(false);
            for (UpdateEntity entity : entities) {
                pst = buildUpdateStatement(conn, entity);
                if (pst != null) {
                    // 执行
                    affectedLine += pst.executeUpdate();
                }
            }
            conn.commit();
        } catch (SQLException e) {
            logger.error("执行sql异常:{}",e.getMessage());
            rollback(conn);
            throw e;
        } finally {
            close(pst,conn);
        }
        return affectedLine;

    }

    public static int delete(DataSource ds, List<DeleteEntity> entities) throws SQLException {
        if (entities == null || entities.size() == 0){
            return 0;
        }

        // 受影响的行数
        int affectedLine = 0;
        Connection conn = null;
        PreparedStatement pst = null;
        try {
            // 获得连接
            conn = ds.getConnection();
            conn.setAutoCommit(false);
            for (DeleteEntity entity : entities) {
                pst = buildDeleteStatement(conn, entity);
                if (pst != null) {
                    // 执行
                    affectedLine += pst.executeUpdate();
                }
            }
            conn.commit();
        } catch (SQLException e) {
            logger.error("执行sql异常:{}",e.getMessage());
            rollback(conn);
            throw e;
        } finally {
            close(pst,conn);
        }
        return affectedLine;

    }

    private static void rollback(Connection conn){
        // 业务异常，回滚
        try {
            if(conn != null) {
                conn.rollback();
            }
        } catch (SQLException e1) {
            logger.error("执行回滚异常:{}",e1.getMessage());
        }
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

    private static PreparedStatement buildDeleteStatement(Connection conn,DeleteEntity entity) throws SQLException {
        StringBuilder sb = new StringBuilder();

        //直接删除
        if (entity.getDeleteStrategy() == 0){
            //组装表
            sb.append("delete from ").append(entity.getTableName()).append(" ");
            //组装set
            sb.append(" where 1=1 ");
            for (Pk pk: entity.getPkList()){
                sb.append(" and ");
                sb.append(pk.getPkCol()).append("=?");
            }

            String sql = sb.toString();
            logger.info(">>>>>>>执行sql:{}",sql);
            try{
                // 调用SQL
                PreparedStatement pst = conn.prepareStatement(sql);

                // 组装参数
                int i = 1;
                for (Pk pk: entity.getPkList()){
                    pst.setObject(i, pk.getPkValue());
                    i++;
                }
                return pst;
            }catch (Exception e){
                logger.error("构建PreparedStatement异常:{}",e.getMessage());
                throw e;
            }
        }
        //不一致就忽略
        else if (entity.getDeleteStrategy() == 1){
            //组装表
            sb.append("update ").append(entity.getTableName()).append(" ");
            //组装set
            sb.append("set ");
            int i = 0;
            for (Column col : entity.getColumnList()) {
                if (i > 0){
                    sb.append(",");
                }
                sb.append(col.getColName()).append("=(case when ");
                sb.append(col.getColName()).append("=? then ? else ");
                sb.append(col.getColName()).append(" end)");
                i++;
            }
            sb.append(" where 1=1 ");
            for (Pk pk: entity.getPkList()){
                sb.append(" and ");
                sb.append(pk.getPkCol()).append("=?");
            }

            String sql = sb.toString();
            logger.info(">>>>>>>执行sql:{}",sql);
            try{
                // 调用SQL
                PreparedStatement pst = conn.prepareStatement(sql);

                // 组装参数
                i = 1;
                for (Column col : entity.getColumnList()) {
                    pst.setObject(i, col.getColValue());
                    i++;
                    pst.setObject(i, null);
                    i++;
                }

                for (Pk pk: entity.getPkList()){
                    pst.setObject(i, pk.getPkValue());
                    i++;
                }
                return pst;
            }catch (Exception e){
                logger.error("构建PreparedStatement异常:{}",e.getMessage());
                throw e;
            }
        }
        //强制设置为null
        else if (entity.getDeleteStrategy() == 2){
            //组装表
            sb.append("update ").append(entity.getTableName()).append(" ");
            //组装set
            sb.append("set ");
            int i = 0;
            for (Column col : entity.getColumnList()) {
                if (i > 0){
                    sb.append(",");
                }
                sb.append(col.getColName()).append("=?");
                i++;
            }
            sb.append(" where 1=1 ");
            for (Pk pk: entity.getPkList()){
                sb.append(" and ");
                sb.append(pk.getPkCol()).append("=?");
            }

            String sql = sb.toString();
            logger.info(">>>>>>>执行sql:{}",sql);
            try{
                // 调用SQL
                PreparedStatement pst = conn.prepareStatement(sql);

                // 组装参数
                i = 1;
                for (Column col : entity.getColumnList()) {
                    pst.setObject(i, col.getColValue());
                    i++;
                }

                for (Pk pk: entity.getPkList()){
                    pst.setObject(i, pk.getPkValue());
                    i++;
                }
                return pst;
            }catch (Exception e){
                logger.error("构建PreparedStatement异常:{}",e.getMessage());
                throw e;
            }
        }
        return null;
    }

    private static PreparedStatement buildUpdateStatement(Connection conn,UpdateEntity entity) throws SQLException {
        StringBuilder sb = new StringBuilder();
        //组装表
        sb.append("update ").append(entity.getTableName()).append(" ");
        //组装set
        sb.append("set ");
        int i = 0;
        for (Column col : entity.getColumnList()) {
            if (i > 0){
                sb.append(",");
            }
            sb.append(col.getColName()).append("=?");
            i++;
        }
        sb.append(" where 1=1 ");
        for (Pk pk: entity.getPkList()){
            sb.append(" and ");
            sb.append(pk.getPkCol()).append("=?");
        }

        String sql = sb.toString();
        logger.info(">>>>>>>执行sql:{}",sql);
        try{
            // 调用SQL
            PreparedStatement pst = conn.prepareStatement(sql);

            // 组装参数
            i = 1;
            for (Column col : entity.getColumnList()) {
                pst.setObject(i, col.getColValue());
                i++;
            }

            for (Pk pk: entity.getPkList()){
                pst.setObject(i, pk.getPkValue());
                i++;
            }
            return pst;
        }catch (Exception e){
            logger.error("构建PreparedStatement异常:{}",e.getMessage());
            throw e;
        }
    }

    private static PreparedStatement buildInsertStatement(Connection conn,InsertEntity entity) throws SQLException {
        StringBuilder sb = new StringBuilder();

        StringBuilder sbValues = new StringBuilder();
        //组装表
        sb.append("insert into ").append(entity.getTableName()).append("( ");

        //组装新增的列名
        int i = 0;
        for (Pk pk: entity.getPkList()){
            if (i > 0){
                sb.append(",");
                sbValues.append(",");
            }
            sb.append(pk.getPkCol());
            sbValues.append("?");
            i++;
        }
        for (Column col : entity.getColumnList()) {
            if (i > 0){
                sb.append(",");
                sbValues.append(",");
            }
            sb.append(col.getColName());
            sbValues.append("?");
            i++;
        }
        for (OriginCol originCol: entity.getOriginColList()){
            sb.append(",");
            sbValues.append(",");
            sb.append(originCol.getOrigin());
            sbValues.append("?");
        }
        sb.append(" ) values (").append(sbValues).append(" )");

        String sql = sb.toString();
        logger.info(">>>>>>>执行sql:{}",sql);
        try{
            // 调用SQL
            PreparedStatement pst = conn.prepareStatement(sql);
            // 组装参数
            i = 1;
            for (Pk pk: entity.getPkList()){
                pst.setObject(i, pk.getPkValue());
                i++;
            }

            for (Column col : entity.getColumnList()) {
                pst.setObject(i, col.getColValue());
                i++;
            }

            for (OriginCol originCol: entity.getOriginColList()){
                pst.setObject(i, originCol.getValue());
                i++;
            }
            return pst;
        }catch (Exception e){
            logger.error("构建PreparedStatement异常:{}",e.getMessage());
            throw e;
        }
    }

    public static int delete(DataSource ds, DeleteEntity entity) throws SQLException {
        // 受影响的行数
        int affectedLine = 0;
        Connection conn = null;
        PreparedStatement pst = null;
        try {
            // 获得连接
            conn = ds.getConnection();
            pst = buildDeleteStatement(conn,entity);
            if (pst != null){
                // 执行
                affectedLine = pst.executeUpdate();
            }
        } catch (SQLException e) {
            logger.error("执行sql异常:{}",e.getMessage());
            throw e;
        } finally {
            close(pst,conn);
        }
        return affectedLine;
    }

    public static int update(DataSource ds, UpdateEntity entity) throws SQLException {
        // 受影响的行数
        int affectedLine = 0;
        Connection conn = null;
        PreparedStatement pst = null;
        try {
            // 获得连接
            conn = ds.getConnection();
            pst = buildUpdateStatement(conn,entity);
            if (pst != null){
                // 执行
                affectedLine = pst.executeUpdate();
            }
        } catch (SQLException e) {
            logger.error("执行sql异常:{}",e.getMessage());
            throw e;
        } finally {
            close(pst,conn);
        }
        return affectedLine;
    }

    public static int insert(DataSource ds, InsertEntity entity) throws SQLException {
        // 受影响的行数
        int affectedLine = 0;
        Connection conn = null;
        PreparedStatement pst = null;
        try {
            // 获得连接
            conn = ds.getConnection();
            pst = buildInsertStatement(conn,entity);
            if (pst != null){
                // 执行
                affectedLine = pst.executeUpdate();
            }
        } catch (SQLException e) {
            logger.error("执行sql异常:{}",e.getMessage());
            throw e;
        } finally {
            close(pst,conn);
        }
        return affectedLine;
    }
//
//    /**
//     * SQL 查询将查询结果直接放入ResultSet中
//     * @param sql SQL语句
//     * @param params 参数数组，若没有参数则为null
//     * @return 结果集
//     */
//    private ResultSet executeQueryRS(String sql, Object[] params) {
//        try {
//            // 获得连接
//            conn = this.getConnection();
//
//            // 调用SQL
//            pst = conn.prepareStatement(sql);
//
//            // 参数赋值
//            if (params != null) {
//                for (int i = 0; i < params.length; i++) {
//                    pst.setObject(i + 1, params[i]);
//                }
//            }
//
//            // 执行
//            rst = pst.executeQuery();
//
//        } catch (SQLException e) {
//            System.out.println(e.getMessage());
//        }
//
//        return rst;
//    }
//
//    /**
//     * SQL 查询将查询结果：一行一列
//     * @param sql SQL语句
//     * @param params 参数数组，若没有参数则为null
//     * @return 结果集
//     */
//    public Object executeQuerySingle(String sql, Object[] params) {
//        Object object = null;
//        try {
//            // 获得连接
//            conn = this.getConnection();
//
//            // 调用SQL
//            pst = conn.prepareStatement(sql);
//
//            // 参数赋值
//            if (params != null) {
//                for (int i = 0; i < params.length; i++) {
//                    pst.setObject(i + 1, params[i]);
//                }
//            }
//
//            // 执行
//            rst = pst.executeQuery();
//
//            if(rst.next()) {
//                object = rst.getObject(1);
//            }
//
//        } catch (SQLException e) {
//            System.out.println(e.getMessage());
//        } finally {
//            closeAll();
//        }
//
//        return object;
//    }
//
//    /**
//     * 获取结果集，并将结果放在List中
//     *
//     * @param sql  SQL语句
//     *         params  参数，没有则为null
//     * @return List
//     *                       结果集
//     */
//    public List<Object> excuteQuery(String sql, Object[] params) {
//        // 执行SQL获得结果集
//        ResultSet rs = executeQueryRS(sql, params);
//
//        // 创建ResultSetMetaData对象
//        ResultSetMetaData rsmd = null;
//
//        // 结果集列数
//        int columnCount = 0;
//        try {
//            rsmd = rs.getMetaData();
//
//            // 获得结果集列数
//            columnCount = rsmd.getColumnCount();
//        } catch (SQLException e1) {
//            System.out.println(e1.getMessage());
//        }
//
//        // 创建List
//        List<Object> list = new ArrayList<Object>();
//
//        try {
//            // 将ResultSet的结果保存到List中
//            while (rs.next()) {
//                Map<String, Object> map = new HashMap<String, Object>();
//                for (int i = 1; i <= columnCount; i++) {
//                    map.put(rsmd.getColumnLabel(i), rs.getObject(i));
//                }
//                list.add(map);//每一个map代表一条记录，把所有记录存在list中
//            }
//        } catch (SQLException e) {
//            System.out.println(e.getMessage());
//        } finally {
//            // 关闭所有资源
//            closeAll();
//        }
//
//        return list;
//    }
//
//    /**
//     * 存储过程带有一个输出参数的方法
//     * @param sql 存储过程语句
//     * @param params 参数数组
//     * @param outParamPos 输出参数位置
//     * @param SqlType 输出参数类型
//     * @return 输出参数的值
//     */
//    public Object excuteQuery(String sql, Object[] params,int outParamPos, int SqlType) {
//        Object object = null;
//        conn = this.getConnection();
//        try {
//            // 调用存储过程
//            // prepareCall:创建一个 CallableStatement 对象来调用数据库存储过程。
//            callableStatement = conn.prepareCall(sql);
//
//            // 给参数赋值
//            if(params != null) {
//                for(int i = 0; i < params.length; i++) {
//                    callableStatement.setObject(i + 1, params[i]);
//                }
//            }
//
//            // 注册输出参数
//            callableStatement.registerOutParameter(outParamPos, SqlType);
//
//            // 执行
//            callableStatement.execute();
//
//            // 得到输出参数
//            object = callableStatement.getObject(outParamPos);
//
//        } catch (SQLException e) {
//            System.out.println(e.getMessage());
//        } finally {
//            // 释放资源
//            closeAll();
//        }
//
//        return object;
//    }
//
//    /**
//     * 关闭所有资源
//     */
//    private void closeAll() {
//        // 关闭结果集对象
//        if (rst != null) {
//            try {
//                rst.close();
//            } catch (SQLException e) {
//                System.out.println(e.getMessage());
//            }
//        }
//
//        // 关闭PreparedStatement对象
//        if (pst != null) {
//            try {
//                pst.close();
//            } catch (SQLException e) {
//                System.out.println(e.getMessage());
//            }
//        }
//
//        // 关闭CallableStatement 对象
//        if (callableStatement != null) {
//            try {
//                callableStatement.close();
//            } catch (SQLException e) {
//                System.out.println(e.getMessage());
//            }
//        }
//
//        // 关闭Connection 对象
//        if (conn != null) {
//            try {
//                conn.close();
//            } catch (SQLException e) {
//                System.out.println(e.getMessage());
//            }
//        }
//    }

}
