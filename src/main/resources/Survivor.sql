-- 删除现有的数据库，如果存在的话
DROP
DATABASE IF EXISTS Survivor;

-- 创建一个新的数据库，使用UTF-8字符集
CREATE
DATABASE Survivor CHARSET=utf8;

-- 使用刚刚创建的数据库
USE
Survivor;

-- 删除现有的`users`表，如果存在的话
DROP TABLE IF EXISTS users;

-- 创建`users`表
CREATE TABLE users
(
    user_id       INT AUTO_INCREMENT PRIMARY KEY,     -- 用户唯一标识符，自增
    username      VARCHAR(50)  NOT NULL UNIQUE,       -- 用户名，唯一且不能为空
    password_hash VARCHAR(255) NOT NULL,              -- 加密后的密码，使用较长的长度以适应哈希值
    register_date DATETIME DEFAULT CURRENT_TIMESTAMP, -- 用户注册的日期，默认为当前时间
    last_login    DATETIME                            -- 用户上次登录的时间
);

-- 删除现有的`login_logs`表，如果存在的话
DROP TABLE IF EXISTS login_logs;

-- 创建`login_logs`表，记录每次用户的登录信息
CREATE TABLE login_logs
(
    log_id     INT AUTO_INCREMENT PRIMARY KEY,                         -- 日志唯一标识符，自增
    user_id    INT NOT NULL,                                           -- 对应用户的ID
    login_time DATETIME DEFAULT CURRENT_TIMESTAMP,                     -- 登录时间，默认为当前时间
    login_ip   VARCHAR(45),                                            -- 用户登录时的IP地址
    FOREIGN KEY (user_id) REFERENCES users (user_id) ON DELETE CASCADE -- 外键约束，用户删除时同时删除相关的登录记录
);

-- 删除现有的`achievements`表，如果存在的话
DROP TABLE IF EXISTS achievements;

-- 创建`achievements`表
CREATE TABLE achievements
(
    achievement_id INT AUTO_INCREMENT PRIMARY KEY,    -- 成就唯一标识符，自增
    name           VARCHAR(100) NOT NULL,             -- 成就名称
    description    TEXT         NOT NULL,             -- 成就描述
    image_path     VARCHAR(255),                      -- 成就图片的URL
    created_at     DATETIME DEFAULT CURRENT_TIMESTAMP -- 成就创建的时间
);

-- 删除现有的`user_achievements`表，如果存在的话
DROP TABLE IF EXISTS user_achievements;

-- 创建`user_achievements`表，记录用户所获得的成就
CREATE TABLE user_achievements
(
    user_id        INT NOT NULL,                                                             -- 用户ID
    achievement_id INT NOT NULL,                                                             -- 成就ID
    earned_at      DATETIME DEFAULT CURRENT_TIMESTAMP,                                       -- 成就获得的时间
    FOREIGN KEY (user_id) REFERENCES users (user_id) ON DELETE CASCADE,                      -- 外键约束，用户删除时同时删除相关的成就记录
    FOREIGN KEY (achievement_id) REFERENCES achievements (achievement_id) ON DELETE CASCADE, -- 外键约束，成就删除时同时删除相关的用户成就记录
    PRIMARY KEY (user_id, achievement_id)                                                    -- 用户和成就的组合唯一
);

-- 插入测试数据到`users`表中
INSERT INTO users (username, password_hash, last_login)
VALUES ('player1', SHA2('password1', 256), '2024-09-01 10:00:00'), -- 第一个用户的用户名为player1，密码使用SHA-256加密
       ('player2', SHA2('password2', 256), '2024-09-02 14:30:00'), -- 第二个用户player2，设置了上次登录时间
       ('player3', SHA2('password3', 256), NULL);
-- 第三个用户player3，尚未登录

-- 插入测试数据到`achievements`表中
INSERT INTO achievements (name, description, image_path, created_at)
VALUES ('First Login', 'Logged in for the first time.', 'first_login.png', CURRENT_TIMESTAMP),
       ('High Score', 'Achieved a high score of 1000 points.', 'high_score.png', CURRENT_TIMESTAMP),
       ('Completed Level 1', 'Completed the first level of the game.', 'level_1.png', CURRENT_TIMESTAMP),
       ('Survivor', 'Survived for 24 hours in the game.', 'survivor.png', CURRENT_TIMESTAMP),
       ('Master Explorer', 'Visited every location in the game.', 'explorer.png', CURRENT_TIMESTAMP),
       ('Unstoppable', 'Defeated 100 enemies.', 'unstoppable.png', CURRENT_TIMESTAMP),
       ('Treasure Hunter', 'Found all hidden treasures.', 'treasure.png', CURRENT_TIMESTAMP),
       ('Champion', 'Won 10 matches in a row.', 'champion.png', CURRENT_TIMESTAMP),
       ('Strategist', 'Completed all levels with 3 stars.', 'strategist.png', CURRENT_TIMESTAMP),
       ('Veteran', 'Played the game for 50 hours.', 'veteran.png', CURRENT_TIMESTAMP),
       ('Perfect Score', 'Achieved the highest score in a level.', 'perfect_score.png', CURRENT_TIMESTAMP),
       ('Survivor of the Night', 'Survived through a whole night cycle.', 'night_survivor.png', CURRENT_TIMESTAMP),
       ('Quick Thinker', 'Completed a level within 5 minutes.', 'quick_thinker.png', CURRENT_TIMESTAMP),
       ('Team Player', 'Completed a co-op mission successfully.', 'team_player.png', CURRENT_TIMESTAMP),
       ('Stealthy', 'Avoided detection in a level.', 'stealthy.png', CURRENT_TIMESTAMP),
       ('Resourceful', 'Crafted 100 items.', 'resourceful.png', CURRENT_TIMESTAMP),
       ('Eagle Eye', 'Found all hidden clues.', 'eagle_eye.png', CURRENT_TIMESTAMP),
       ('Master Collector', 'Collected all types of items.', 'master_collector.png', CURRENT_TIMESTAMP);


-- 插入测试数据到`login_logs`表中
INSERT INTO login_logs (user_id, login_ip)
VALUES (1, '192.168.1.10'), -- 用户ID 1 (player1) 的登录IP
       (2, '192.168.1.11'), -- 用户ID 2 (player2) 的登录IP
       (1, '192.168.1.12');
-- 用户ID 1 (player1) 再次登录

-- 插入测试数据到`user_achievements`表中
INSERT INTO user_achievements (user_id, achievement_id, earned_at)
VALUES (1, 1, '2024-09-01 10:00:00'), -- 用户ID 1 获得了 "First Login" 成就
       (2, 2, '2024-09-02 14:30:00'), -- 用户ID 2 获得了 "High Score" 成就
       (3, 3, '2024-09-03 09:00:00');
-- 用户ID 3 获得了 "Completed Level 1" 成就


-- 插入新的用户到 `users` 表中
INSERT INTO users (user_id, username, password_hash, last_login)
VALUES (123, 'user123', '123', null);
-- 密码明文存储，未设置上次登录时间

-- 插入测试数据到 `user_achievements` 表中
INSERT INTO user_achievements (user_id, achievement_id, earned_at)
VALUES (123, 1, CURRENT_TIMESTAMP), -- 用户ID 123 获得了 "First Login" 成就
       (123, 2, CURRENT_TIMESTAMP), -- 用户ID 123 获得了 "High Score" 成就
       (123, 3, CURRENT_TIMESTAMP), -- 用户ID 123 获得了 "Completed Level 1" 成就
       (123, 4, CURRENT_TIMESTAMP), -- 用户ID 123 获得了 "Survivor" 成就
       (123, 5, CURRENT_TIMESTAMP);
-- 用户ID 123 获得了 "Master Explorer" 成就


-- 查询`users`表的数据
SELECT *
FROM users;

-- 查询`login_logs`表的数据
SELECT *
FROM login_logs;

-- 查询`achievements`表的数据
SELECT *
FROM achievements;

-- 查询`user_achievements`表的数据
SELECT *
FROM user_achievements;




