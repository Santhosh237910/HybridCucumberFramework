package base;

import utils.ConfigReader;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Utility class for database operations
 * - Thread-safe: each thread/scenario gets its own Connection via ThreadLocal
 * - Supports executing queries and returning ResultSet as List<Map<String,String>>
 * - Ensures Statements and ResultSets are properly closed
 */
public class DBUtils {

    // ThreadLocal to store per-thread DB Connection
    private static final ThreadLocal<Connection> connectionHolder = new ThreadLocal<>();

    // Private constructor to prevent instantiation
    private DBUtils() {}

    /**
     * Get thread-safe Connection instance for current scenario/thread
     */
    public static Connection getConnection() throws SQLException {
        if (connectionHolder.get() == null || connectionHolder.get().isClosed()) {
            // Read DB connection details from config
            String url = ConfigReader.get("dbUrl");
            String user = ConfigReader.get("dbUser");
            String password = ConfigReader.get("dbPassword");

            // Establish new connection and store in ThreadLocal
            Connection conn = DriverManager.getConnection(url, user, password);
            connectionHolder.set(conn);
        }
        return connectionHolder.get();
    }

    /**
     * Close Connection for current thread to prevent memory leaks
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
                // Remove from ThreadLocal
                connectionHolder.remove();
            }
        }
    }

    /**
     * Execute SELECT query and return results as List<Map<String,String>>
     * - Closes Statement and ResultSet automatically
     * @param query SQL SELECT statement
     * @return List of rows, each row is a Map<columnName, value>
     * @throws SQLException if query fails
     */
    public static List<Map<String, String>> executeSelectQuery(String query) throws SQLException {
        List<Map<String, String>> results = new ArrayList<>();

        // Try-with-resources ensures Statement and ResultSet are closed automatically
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement(
                     ResultSet.TYPE_SCROLL_INSENSITIVE,
                     ResultSet.CONCUR_READ_ONLY);
             ResultSet rs = stmt.executeQuery(query)) {

            ResultSetMetaData metaData = rs.getMetaData();
            int columnCount = metaData.getColumnCount();

            while (rs.next()) {
                Map<String, String> row = new HashMap<>();
                for (int i = 1; i <= columnCount; i++) {
                    row.put(metaData.getColumnName(i), rs.getString(i));
                }
                results.add(row);
            }
        }
        return results;
    }

    /**
     * Execute INSERT, UPDATE, DELETE query
     * - Closes Statement automatically
     * @param query SQL statement
     * @return number of affected rows
     * @throws SQLException if execution fails
     */
    public static int executeUpdate(String query) throws SQLException {
        try (Statement stmt = getConnection().createStatement()) {
            return stmt.executeUpdate(query);
        }
    }

    public static ResultSet executeQuery(String query, Statement[] outStmt) throws SQLException {
        Connection conn = getConnection();
        Statement stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
        outStmt[0] = stmt; // Pass statement reference back
        return stmt.executeQuery(query);
    }

}