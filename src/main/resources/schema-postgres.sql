DROP TABLE IF EXISTS USER_PERMISSIONS;
DROP TABLE IF EXISTS PERMISSIONS;
DROP TABLE IF EXISTS USER_ROLES;
DROP TABLE IF EXISTS ACCOUNT_APPROVAL_REQUESTS;
DROP TABLE IF EXISTS LOGIN_HISTORY;
DROP TABLE IF EXISTS ROLES;
DROP TABLE IF EXISTS USERS;

CREATE TABLE USERS (
    user_id SERIAL PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    full_name VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    employee_id VARCHAR(50) NOT NULL UNIQUE,
    phone_number VARCHAR(20),
    designation VARCHAR(100),
    region VARCHAR(100),
    cost_center VARCHAR(100),
    business_unit VARCHAR(100),
    reporting_manager_email VARCHAR(100),
    department VARCHAR(100),
    last_login TIMESTAMP NULL,
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    account_status VARCHAR(20) NOT NULL DEFAULT 'PENDING' CHECK (account_status IN ('PENDING', 'ACTIVE', 'INACTIVE', 'REJECTED', 'LOCKED', 'BLOCKED')),
    failed_login_attempts INT NOT NULL DEFAULT 0,
    account_locked BOOLEAN NOT NULL DEFAULT FALSE,
    lock_time TIMESTAMP NULL,
    profile_picture VARCHAR(255),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by INT,
    updated_by INT,
    FOREIGN KEY (created_by) REFERENCES USERS(user_id),
    FOREIGN KEY (updated_by) REFERENCES USERS(user_id)
);

CREATE TABLE ROLES (
    role_id SERIAL PRIMARY KEY,
    role_name VARCHAR(50) NOT NULL UNIQUE,
    description TEXT,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by INT,
    updated_by INT,
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
    assigned_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by INT,
    updated_by INT,
    PRIMARY KEY (user_id, role_id),
    FOREIGN KEY (user_id) REFERENCES USERS(user_id),
    FOREIGN KEY (role_id) REFERENCES ROLES(role_id),
    FOREIGN KEY (created_by) REFERENCES USERS(user_id),
    FOREIGN KEY (updated_by) REFERENCES USERS(user_id)
);

CREATE TABLE ACCOUNT_APPROVAL_REQUESTS (
    request_id SERIAL PRIMARY KEY,
    user_id INT NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'PENDING' CHECK (status IN ('PENDING', 'APPROVED', 'REJECTED')),
    requested_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    rejection_reason TEXT,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by INT,
    updated_by INT,
    FOREIGN KEY (user_id) REFERENCES USERS(user_id),
    FOREIGN KEY (created_by) REFERENCES USERS(user_id),
    FOREIGN KEY (updated_by) REFERENCES USERS(user_id)
);

CREATE TABLE LOGIN_HISTORY (
    login_id SERIAL PRIMARY KEY,
    user_id INT NOT NULL,
    login_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    ip_address VARCHAR(45) NOT NULL,
    user_agent TEXT NOT NULL,
    success BOOLEAN NOT NULL,
    failure_reason VARCHAR(255),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by INT,
    FOREIGN KEY (user_id) REFERENCES USERS(user_id),
    FOREIGN KEY (created_by) REFERENCES USERS(user_id)
);

-- Insert RMG test user (password: test123)
INSERT INTO USERS (username, full_name, email, password_hash, employee_id, designation, department, account_status, is_active)
VALUES ('rmg_test', 'RMG Admin', 'rmg_test@example.com', '$2a$10$QYGLYLCVwFbAO0ZV7f5e5erEWmGpjPL.qMNC5hBhIqhfhO9UW9/jW', 'RMG001', 'RMG Administrator', 'Resource Management', 'ACTIVE', TRUE);

-- Link RMG test user to RMG role
INSERT INTO USER_ROLES (user_id, role_id)
SELECT u.user_id, r.role_id
FROM USERS u, ROLES r
WHERE u.username = 'rmg_test' AND r.role_name = 'RMG';

CREATE TABLE PERMISSIONS (
    permission_id SERIAL PRIMARY KEY,
    permission_name VARCHAR(50) NOT NULL UNIQUE,
    description VARCHAR(255),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by INT,
    updated_by INT,
    FOREIGN KEY (created_by) REFERENCES USERS(user_id),
    FOREIGN KEY (updated_by) REFERENCES USERS(user_id)
);

CREATE TABLE USER_PERMISSIONS (
    user_id INT NOT NULL,
    permission_id INT NOT NULL,
    assigned_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by INT,
    updated_by INT,
    PRIMARY KEY (user_id, permission_id),
    FOREIGN KEY (user_id) REFERENCES USERS(user_id),
    FOREIGN KEY (permission_id) REFERENCES PERMISSIONS(permission_id),
    FOREIGN KEY (created_by) REFERENCES USERS(user_id),
    FOREIGN KEY (updated_by) REFERENCES USERS(user_id)
);

DROP TABLE IF EXISTS HR;

CREATE TABLE HR (
    hr_id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

INSERT INTO HR (name, email) VALUES ('Shubham Singh', 'shubham16cse06@gmail.com');

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
('mng_dashboard', 'Access to Manager Dashboard', 1, 1),
('mng_notif', 'Manager Notifications', 1, 1),
('mng_pref', 'Manager Preferences', 1, 1),
('mng_app_status', 'Manager Application Status', 1, 1),
('mng_jb', 'Manage Job Board', 1, 1);

INSERT INTO USER_PERMISSIONS (user_id, permission_id, created_by, updated_by)
SELECT 1, p.permission_id, 1, 1
FROM PERMISSIONS p
WHERE p.permission_name LIKE 'rmg%';
