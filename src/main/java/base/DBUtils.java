package base;

import utils.ConfigReader;

import java.sql.*;

/**
 * Utility class for database operations
 * - Thread-safe: each thread/scenario gets its own Connection
 * - Supports executing queries and returning ResultSet
 */
public class DBUtils {

    // ThreadLocal to store per-thread Connection
    private static final ThreadLocal<Connection> connectionHolder = new ThreadLocal<>();

    private DBUtils() {
        // private constructor to prevent instantiation
    }

    /**
     * Get thread-safe Connection instance
     */
    public static Connection getConnection() throws SQLException {
        if (connectionHolder.get() == null || connectionHolder.get().isClosed()) {
            String url = ConfigReader.get("dbUrl");
            String user = ConfigReader.get("dbUser");
            String password = ConfigReader.get("dbPassword");

            Connection conn = DriverManager.getConnection(url, user, password);
            connectionHolder.set(conn);
        }
        return connectionHolder.get();
    }

    /**
     * Close Connection for current thread
     */
    public static void closeConnection() {
        Connection conn = connectionHolder.get();
        if (conn != null) {
            try {
                if (!conn.isClosed()) {
                    conn.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                connectionHolder.remove();
            }
        }
    }

    /**
     * Execute SELECT query and return ResultSet
     */
    public static ResultSet executeQuery(String query) throws SQLException {
        Connection conn = getConnection();
        Statement stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
        return stmt.executeQuery(query);
    }

    /**
     * Execute INSERT, UPDATE, DELETE query
     */
    public static int executeUpdate(String query) throws SQLException {
        Connection conn = getConnection();
        Statement stmt = conn.createStatement();
        return stmt.executeUpdate(query);
    }
}
