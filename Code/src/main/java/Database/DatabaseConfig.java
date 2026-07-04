package Database;

public class DatabaseConfig {
    public static final String HOST = "localhost";
    public static final String INSTANCE = "SQLEXPRESS"; // Giữ nguyên thực thể SQLEXPRESS
    public static final String DB_NAME = "caro_game";
    public static final String USERNAME = "sa";
    public static final String PASSWORD = "123456"; // Mật khẩu sa bạn vừa kích hoạt ở Bước 3

    // Thêm cấu hình cổng mạng ";port=1435" vào chuỗi URL kết nối
    public static final String URL =
            "jdbc:sqlserver://" + HOST + "\\" + INSTANCE + ";port=1435" +
                    ";databaseName=" + DB_NAME +
                    ";encrypt=true;trustServerCertificate=true;";
}