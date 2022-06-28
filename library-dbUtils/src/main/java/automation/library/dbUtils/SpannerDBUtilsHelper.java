package automation.library.dbUtils;

import automation.library.common.Property;
import org.apache.commons.dbutils.DbUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * Helper class providing wrapper methods around Apache DBUtils for the most common
 * usage cases.  These wrapper methods can be invoked from cucumber steps to provide
 * database CRUD activities.  The getDBBean() and getDBBeanList() wrapper methods
 * enable the DBUtils bean based methods to be defined and invoked with generic types
 */
public class SpannerDBUtilsHelper<T> {

    static Connection conn = null;
    static QueryRunner run = new QueryRunner();
    static boolean keepConnection = false;

    protected static Logger log = LogManager.getLogger(SpannerDBUtilsHelper.class);

    /**
     * Create the db connection reading the config details from environment config file
     * /src/test/resources/config/environments/env.properties
     * read value from - DBurl, DBDriver, DBusr, DBpwd
     */
    public static void createConn() throws SQLException {
        if (conn == null || conn.isClosed()) {

            String jdbcUrlFormat = "jdbc:cloudspanner:/projects/%s/instances/%s/databases/%s;credentials=%s";

            String propsPath = Constants.ENVIRONMENTPATH + Property.getVariable("cukes.env") + ".properties";

            String credentialFileLocation = Property.getProperty(propsPath, "jsonLocation");
            String projectId = Property.getProperty(propsPath,"projectId");
            String instance = Property.getProperty(propsPath,"instance");
            String database = Property.getProperty(propsPath,"database");
            String credentialsUrl = Constants.BASEPATH + credentialFileLocation;

            String url = String.format(jdbcUrlFormat, projectId, instance, database, credentialsUrl);

            log.info("Spanner DB url: " + url);

            conn = DriverManager.getConnection(url);
            conn.setAutoCommit(false);
        }
    }

    /**
     * create the db connection from the provided parameters
     * @param file credential json file relative path src/test/resources
     * @param projectId project ID of project
     * @param instance database instance
     * @param database database
     * @throws SQLException sqlException
     */
    public static void createConn(String file, String projectId, String instance, String database) throws SQLException {

        if (conn == null || conn.isClosed()) {
            String jdbcUrlFormat = "jdbc:cloudspanner:/projects/%s/instances/%s/databases/%s;credentials=%s";

            String credentialsUrl = Constants.BASEPATH+ file;
            String url = String.format(jdbcUrlFormat, projectId, instance, database, credentialsUrl);
            log.info("Spanner DB url: " + url);
            conn = DriverManager.getConnection(url);
            conn.setAutoCommit(false);
        }
    }

    /**
     * Method to create connection for given specific DB name from the environment config properties file
     * /src/test/resources/config/environments/env.properties
     * read value from - dbPrefix_DBurl, dbPrefix_DBDriver, dbPrefix_DBusr, dbPrefix_DBpwd
     * @param dbPrefix - input db prefix for which connection has to be made
     * @throws SQLException sqlException
     */

    public static void createDBConn(String dbPrefix) throws SQLException {
        String propsPath = Constants.ENVIRONMENTPATH + Property.getVariable("cukes.env") + ".properties";

        if (conn == null || conn.isClosed()) {
            String jdbcUrlFormat = "jdbc:cloudspanner:/projects/%s/instances/%s/databases/%s;credentials=%s";
            String file = Property.getProperty(propsPath,dbPrefix + "_jsonLocation");
            String projectId = Property.getProperty(propsPath,dbPrefix + "_projectId");
            String instance = Property.getProperty(propsPath,dbPrefix + "_instance");
            String database = Property.getProperty(propsPath,dbPrefix + "_database");

            String credentialsUrl = Constants.ENVIRONMENTPATH+ file;

            String url = String.format(jdbcUrlFormat, projectId, instance, database, credentialsUrl);
            log.info("Spanner DB url: " + url);
            conn = DriverManager.getConnection(url);
            conn.setAutoCommit(false);
        }
    }

    /**
     * close the connection
     */
    public static void closeConn() {
        if (!keepConnection) {
            DbUtils.closeQuietly(conn);
        }
    }

    /**
     * returns the row as object array
     * @param sql input sql string
     * @param params any additional parameters
     * @return object array
     */
    public static Object[] getDBArray(String sql, Object... params) {
        try {
            createConn();
            if (params == null) {
                return run.query(conn, sql, new ArrayHandler());
            } else {
                return run.query(conn, sql, new ArrayHandler(), params);
            }

        } catch (SQLException se) {
            se.printStackTrace();
            return null;
        } finally {
            closeConn();
        }
    }


    /**
     * returns the rows as list object array
     * @param sql input sql string
     * @param params any additional parameters
     * @return list of object array
     */
    public static List<Object[]> getDBArrayList(String sql, Object...params) {

        try {
            createConn();
            if (params == null) {
                return run.query(conn, sql, new ArrayListHandler());
            } else {
                return run.query(conn, sql, new ArrayListHandler(), params);
            }
        } catch (SQLException se) {
            se.printStackTrace();
            return null;
        } finally {
            closeConn();
        }
    }

    /**
     * returns the row as map
     * @param sql input sql string
     * @param params any additional parameters
     * @return map of row data
     */
    public static Map<String, Object> getDBMap(String sql, Object... params) {

        try {
            createConn();
            if (params == null) {
                return run.query(conn, sql, new MapHandler());
            } else {
                return run.query(conn, sql, new MapHandler(), params);
            }
        } catch (SQLException se) {
            se.printStackTrace();
            return null;
        } finally {
            closeConn();
        }
    }

    /**
     * returns the rows as list of map
     * @param sql input sql string
     * @param params any additional parameters
     * @return map of row data
     */
    public static List<Map<String, Object>> getDBMapList(String sql, Object... params) {

        try {
            createConn();
            if (params == null) {
                return run.query(conn, sql, new MapListHandler());
            } else {
                return run.query(conn, sql, new MapListHandler(), params);
            }
        } catch (SQLException se) {
            se.printStackTrace();
            return null;
        } finally {
            closeConn();
        }
    }

    /**
     * reads data from db and casts to supplied pojo format
     * @param sql - input sql string
     * @param clazz - class
     * @param params
     * @param <T>
     * @return
     * @throws SQLException sqlException
     */
    public static <T> Object getDBBean(String sql, Class<T> clazz, Object... params) throws SQLException {
        try {
            createConn();
            Object data;
            ResultSetHandler<T> rsh = new BeanHandler<T>(clazz);
            if (params == null) {
                data = run.query(conn, sql, rsh);
            } else {
                data = run.query(conn, sql, rsh, params);
            }
            return data;
        } finally {
            closeConn();
        }
    }

    /**
     * reads data from db and casts to supplied pojo format
     * @param sql - input sql string
     * @param clazz - class 
     * @throws SQLException sqlException
     */
    public static <T> List<T> getDBBeanList(String sql, Class<T> clazz, Object... params) throws SQLException {
        try {
            createConn();
            List<T> data;
            ResultSetHandler<List<T>> rsh = new BeanListHandler<T>(clazz);
            if (params == null) {
                data = run.query(conn, sql, rsh);
            } else {
                data = run.query(conn, sql, rsh, params);
            }
            return data;
        } finally {
            closeConn();
        }
    }


    /**
     * perform teh update query in db
     * @param sql input sql string
     * @param params additional params
     * @throws SQLException sqlException
     */
    public static int update(String sql, Object... params) throws SQLException {
        int affectedRows = 0;
        try {
            createConn();
            if (params == null) {
                affectedRows = run.update(conn, sql);
            } else {
                affectedRows = run.update(conn, sql, params);
            }

        } catch (SQLException e) {
            if (conn != null) {
                conn.rollback();
            }
            throw e;

        } finally {
            if (conn != null && !keepConnection)

                DbUtils.commitAndClose(conn);
        }

        return affectedRows;
    }

    /**
     * method to execute query for the given Db from environment config file
     *
     * @param dbPrefix     - prefix of database from environment file
     * @param sql    - query to be executed
     * @param params - additional parameters
     * @return Map object for the row data
     */
    public static Map<String, Object> getDBMap2(String dbPrefix, String sql, Object... params) {

        try {
            createDBConn(dbPrefix);
            if (params == null) {
                return run.query(conn, sql, new MapHandler());
            } else {
                return run.query(conn, sql, new MapHandler(), params);
            }
        } catch (SQLException se) {
            se.printStackTrace();
            return null;
        } finally {
            closeConn();
        }
    }

    /**
     * method to execute query for the given Db from environment config file
     *
     * @param dbPrefix     - - prefix of database from environment file
     * @param sql    - query to be executed
     * @param params - additional parameters
     * @return list of Map object for the row data
     */
    public static List<Map<String, Object>> getDBMapList2(String dbPrefix, String sql, Object... params) {

        try {
            createDBConn(dbPrefix);
            if (params == null) {
                return run.query(conn, sql, new MapListHandler());
            } else {
                return run.query(conn, sql, new MapListHandler(), params);
            }
        } catch (SQLException se) {
            se.printStackTrace();
            return null;
        } finally {
            closeConn();
        }
    }

    /**
     * method to execute query for the given Db from environment config file
     * @param dbPrefix     - - prefix of database from environment file
     * @param sql    - query to be executed
     * @param params - additional parameters
     * @return int number of affected rows
     * @throws SQLException sqlException
     */
    public static int update2(String dbPrefix, String sql, Object... params) throws SQLException {
        int affectedRows = 0;
        try {
            createDBConn(dbPrefix);
            if (params == null) {
                affectedRows = run.update(conn, sql);
            } else {
                affectedRows = run.update(conn, sql, params);
            }

        } catch (SQLException e) {
            if (conn != null) {
                conn.rollback();
            }
            throw e;
        } finally {
            if (conn != null)
                DbUtils.commitAndClose(conn);
        }

        return affectedRows;
    }


    /**
     * Run multiple database insert/update/delete queries
     *
     * @param sql    - string sql statement
     * @param params - optional array[][] of substitution variables to be applied to the sql string to create batch of statements
     * @return int array with number of rows inserted, updated or deleted for each sql statement
     * @throws SQLException sqlException
     */
    public static int[] bulkUpdate(String sql, Object[]... params) throws SQLException {
        int[] affectedRows = null;
        try {
            createConn();

            if (params == null) {
                affectedRows = run.batch(conn, sql, new Object[][]{{}});
            } else {
                affectedRows = run.batch(conn, sql, params);
            }
        } catch (SQLException e) {
            if (conn != null) {
                conn.rollback();
            }
            throw e;
        } finally {
            if (conn != null && !keepConnection)
                DbUtils.commitAndClose(conn);
        }

        return affectedRows;
    }

    /**
     * Run database insert/update/delete query
     *
     * @param sqls   - string array of different sql statements
     * @param params - optional array[] of substitution variables to be applied to the set of sql statements
     * @return int number of rows inserted, updated or deleted
     * @throws SQLException sqlException
     */
    public static int multiUpdate(String[] sqls, Object... params) throws SQLException {

        int affectedRows = 0;

        try {
            createConn();
            for (String sql : sqls) {
                if (params == null) {
                    affectedRows = run.update(conn, sql);
                } else {
                    affectedRows = run.update(conn, sql, params);
                }
            }
        } catch (SQLException e) {
            if (conn != null) {
                conn.rollback();
            }
            throw e;
        } finally {
            if (conn != null && !keepConnection)
                DbUtils.commitAndClose(conn);
        }

        return affectedRows;
    }
}