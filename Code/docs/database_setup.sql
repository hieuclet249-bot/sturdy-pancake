-- 1. Tạo Database nếu chưa tồn tại
USE master;
GO

IF NOT EXISTS (SELECT * FROM sys.databases WHERE name = 'caro_game')
BEGIN
    CREATE DATABASE caro_game;
END
GO

USE caro_game;
GO

-- 2. Tạo bảng users
IF OBJECT_ID('users', 'U') IS NULL
BEGIN
    CREATE TABLE users (
        id            INT IDENTITY(1,1) PRIMARY KEY, -- Thay cho AUTO_INCREMENT
        username      VARCHAR(20)  NOT NULL UNIQUE,  -- SQL Server không hỗ trợ từ khóa COMMENT ở đây
        password_hash VARCHAR(64)  NOT NULL,
        wins          INT DEFAULT 0,
        losses        INT DEFAULT 0,
        draws         INT DEFAULT 0,
        created_at    DATETIME2 DEFAULT GETDATE()    -- Thay cho TIMESTAMP vì TIMESTAMP trong SQL Server có ý nghĩa khác
    );
END
GO

-- 3. Tạo bảng matches
IF OBJECT_ID('matches', 'U') IS NULL
BEGIN
    CREATE TABLE matches (
        id           INT IDENTITY(1,1) PRIMARY KEY,
        player1      VARCHAR(20) NOT NULL,
        player2      VARCHAR(20) NOT NULL,
        winner       VARCHAR(20),
        total_moves  INT DEFAULT 0,
        played_at    DATETIME2 DEFAULT GETDATE()
    );
END
GO

-- 4. Tạo index để tìm lịch sử trận theo username nhanh hơn
IF NOT EXISTS (SELECT * FROM sys.indexes WHERE name = 'idx_matches_player1' AND object_id = OBJECT_ID('matches'))
BEGIN
    CREATE INDEX idx_matches_player1 ON matches(player1);
END

IF NOT EXISTS (SELECT * FROM sys.indexes WHERE name = 'idx_matches_player2' AND object_id = OBJECT_ID('matches'))
BEGIN
    CREATE INDEX idx_matches_player2 ON matches(player2);
END
GO

-- 5. Thêm tài khoản test (password: 123456)
-- Thay thế "INSERT IGNORE" bằng cách kiểm tra điều kiện NOT EXISTS trong SQL Server
IF NOT EXISTS (SELECT 1 FROM users WHERE username = 'test1')
BEGIN
    INSERT INTO users (username, password_hash) 
    VALUES ('test1', '8d969eef6ecad3c29a3a629280e686cf0c3f5d5a86aff3ca12020c923adc6c92');
END

IF NOT EXISTS (SELECT 1 FROM users WHERE username = 'test2')
BEGIN
    INSERT INTO users (username, password_hash) 
    VALUES ('test2', '8d969eef6ecad3c29a3a629280e686cf0c3f5d5a86aff3ca12020c923adc6c92');
END
GO

-- 6. Thông báo thành công
SELECT N'✅ Database caro_game đã sẵn sàng!' AS [status];
GO