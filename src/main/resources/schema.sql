CREATE TABLE USERS (
    user_id INT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50) NOT NULL UNIQUE,
    email VARCHAR(100) NOT NULL UNIQUE,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    department VARCHAR(100),
    password_hash VARCHAR(255) NOT NULL,
    last_login DATETIME NULL,
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    account_status ENUM('PENDING', 'ACTIVE', 'INACTIVE', 'REJECTED') NOT NULL DEFAULT 'PENDING',
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    created_by INT NULL,
    updated_by INT NULL,
    FOREIGN KEY (created_by) REFERENCES USERS(user_id),
    FOREIGN KEY (updated_by) REFERENCES USERS(user_id)
);

CREATE TABLE ROLES (
    role_id INT PRIMARY KEY AUTO_INCREMENT,
    role_name VARCHAR(50) NOT NULL UNIQUE,
    description TEXT NULL,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    created_by INT NULL,
    updated_by INT NULL,
    FOREIGN KEY (created_by) REFERENCES USERS(user_id),
    FOREIGN KEY (updated_by) REFERENCES USERS(user_id)
);

-- Initial data
INSERT INTO ROLES (role_name, description) VALUES
('RMG', 'Resource Management Group user who approves Manager accounts'),
('Manager', 'Regular user who can create resource requests and manage hiring processes');

CREATE TABLE USER_ROLES (
    user_id INT NOT NULL,
    role_id INT NOT NULL,
    assigned_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    created_by INT NULL,
    updated_by INT NULL,
    PRIMARY KEY (user_id, role_id),
    FOREIGN KEY (user_id) REFERENCES USERS(user_id),
    FOREIGN KEY (role_id) REFERENCES ROLES(role_id),
    FOREIGN KEY (created_by) REFERENCES USERS(user_id),
    FOREIGN KEY (updated_by) REFERENCES USERS(user_id)
);

CREATE TABLE ACCOUNT_APPROVAL_REQUESTS (
    request_id INT PRIMARY KEY AUTO_INCREMENT,
    user_id INT NOT NULL,
    status ENUM('PENDING', 'APPROVED', 'REJECTED') NOT NULL DEFAULT 'PENDING',
    requested_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    rejection_reason TEXT NULL,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    created_by INT NULL,
    updated_by INT NULL,
    FOREIGN KEY (user_id) REFERENCES USERS(user_id),
    FOREIGN KEY (created_by) REFERENCES USERS(user_id),
    FOREIGN KEY (updated_by) REFERENCES USERS(user_id)
);

CREATE TABLE LOGIN_HISTORY (
    login_id INT PRIMARY KEY AUTO_INCREMENT,
    user_id INT NOT NULL,
    login_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    ip_address VARCHAR(45) NOT NULL,
    user_agent TEXT NOT NULL,
    success BOOLEAN NOT NULL,
    failure_reason VARCHAR(255) NULL,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by INT NULL,
    FOREIGN KEY (user_id) REFERENCES USERS(user_id),
    FOREIGN KEY (created_by) REFERENCES USERS(user_id)
);

-- Insert RMG test user (password: test123)
INSERT INTO USERS (username, email, first_name, last_name, password_hash, account_status, is_active)
VALUES ('rmg_test', 'rmg_test@example.com', 'RMG', 'Admin', '$2a$10$QYGLYLCVwFbAO0ZV7f5e5erEWmGpjPL.qMNC5hBhIqhfhO9UW9/jW', 'ACTIVE', TRUE);

-- Link RMG test user to RMG role
INSERT INTO USER_ROLES (user_id, role_id)
SELECT (SELECT user_id FROM USERS WHERE username = 'rmg_test'),
       (SELECT role_id FROM ROLES WHERE role_name = 'RMG');

-- New tables and data

CREATE TABLE PERMISSIONS (
    permission_id INT PRIMARY KEY AUTO_INCREMENT,
    permission_name VARCHAR(50) NOT NULL UNIQUE,
    description VARCHAR(255),
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    created_by INT NULL,
    updated_by INT NULL,
    
    FOREIGN KEY (created_by) REFERENCES USERS(user_id),
    FOREIGN KEY (updated_by) REFERENCES USERS(user_id)
);

CREATE TABLE USER_PERMISSIONS (
    user_id INT NOT NULL,
    permission_id INT NOT NULL,
    assigned_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    created_by INT NULL,
    updated_by INT NULL,

    PRIMARY KEY (user_id, permission_id),

    FOREIGN KEY (user_id) REFERENCES USERS(user_id),
    FOREIGN KEY (permission_id) REFERENCES PERMISSIONS(permission_id),
    FOREIGN KEY (created_by) REFERENCES USERS(user_id),
    FOREIGN KEY (updated_by) REFERENCES USERS(user_id)
);

INSERT INTO PERMISSIONS (permission_name, description, created_by, updated_by)
VALUES
('rmg_dashboard', 'Access to RMG Dashboard', 1, 1),
('rmg_approval', 'Access to RMG Approvals', 1, 1),
('rmg_user_mng', 'Manage RMG Users', 1, 1),
('rmg_notif', 'RMG Notifications', 1, 1),
('rmg_interview_mng', 'Manage RMG Interviews', 1, 1),
('rmg_pref', 'RMG Preferences', 1, 1),
('rmg_candidate_pool', 'RMG Candidate Pool Access', 1, 1),
('rmg_track_status', 'Track RMG Status', 1, 1),

('mng_dashbaord', 'Access to Manager Dashboard', 1, 1),
('mng_notif', 'Manager Notifications', 1, 1),
('mng_pref', 'Manager Preferences', 1, 1),
('mng_app_status', 'Manager Application Status', 1, 1),
('mng_jb', 'Manage Job Board', 1, 1);

INSERT INTO USER_PERMISSIONS (user_id, permission_id, created_by, updated_by)
SELECT 1 AS user_id, permission_id, 1 AS created_by, 1 AS updated_by
FROM PERMISSIONS
WHERE permission_name LIKE 'rmg%';