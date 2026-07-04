package Database;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseInitializer {

    public static void initialize() {
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement()) {

            // 1. Tạo bảng users theo cú pháp SQL Server (T-SQL)
            stmt.execute("""
                IF OBJECT_ID('users', 'U') IS NULL
                BEGIN
                    CREATE TABLE users (
                        id INT IDENTITY(1,1) PRIMARY KEY,
                        username VARCHAR(20) NOT NULL UNIQUE,
                        password_hash VARCHAR(64) NOT NULL,
                        wins INT DEFAULT 0,
                        losses INT DEFAULT 0,
                        draws INT DEFAULT 0,
                        created_at DATETIME2 DEFAULT GETDATE()
                    );
                END
            """);

            // 2. Tạo bảng matches theo cú pháp SQL Server (T-SQL)
            stmt.execute("""
                IF OBJECT_ID('matches', 'U') IS NULL
                BEGIN
                    CREATE TABLE matches (
                        id INT IDENTITY(1,1) PRIMARY KEY,
                        player1 VARCHAR(20) NOT NULL,
                        player2 VARCHAR(20) NOT NULL,
                        winner VARCHAR(20),
                        total_moves INT DEFAULT 0,
                        played_at DATETIME2 DEFAULT GETDATE()
                    );
                END
            """);

            // 3. Tạo index cho bảng matches (nếu chưa có) để tối ưu truy vấn tìm kiếm
            stmt.execute("""
                IF NOT EXISTS (SELECT * FROM sys.indexes WHERE name = 'idx_matches_player1' AND object_id = OBJECT_ID('matches'))
                BEGIN
                    CREATE INDEX idx_matches_player1 ON matches(player1);
                END
            """);

            stmt.execute("""
                IF NOT EXISTS (SELECT * FROM sys.indexes WHERE name = 'idx_matches_player2' AND object_id = OBJECT_ID('matches'))
                BEGIN
                    CREATE INDEX idx_matches_player2 ON matches(player2);
                END
            """);

            System.out.println("[DB] Khởi tạo các bảng trong database thành công.");
        } catch (SQLException e) {
            System.err.println("[DB LỖI] Không thể khởi tạo database: " + e.getMessage());
            e.printStackTrace(); // In chi tiết lỗi ra console để dễ debug nếu có sự cố
        }
    }

    // Hàm bổ trợ (nếu có dùng tới ở class khác) cũng được đổi sang cú pháp SQL Server
    public static String getCreateDatabaseSQL() {
        return """
            IF NOT EXISTS (SELECT * FROM sys.databases WHERE name = 'caro_game')
            BEGIN
                CREATE DATABASE caro_game;
            END
            """;
    }
}