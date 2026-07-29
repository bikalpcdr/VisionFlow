CREATE TABLE users
(
    id         BIGSERIAL    NOT NULL,
    first_name VARCHAR(100) NOT NULL,
    last_name  VARCHAR(100) NOT NULL,
    email      VARCHAR(150) NOT NULL,
    password   VARCHAR(255) NOT NULL,
    role       VARCHAR(20)  NOT NULL,
    phone      VARCHAR(20),
    active     BOOLEAN      NOT NULL DEFAULT TRUE,
    deleted    BOOLEAN      NOT NULL DEFAULT FALSE,
    created_at TIMESTAMP    NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP    NOT NULL DEFAULT NOW(),
    created_by BIGINT,
    updated_by BIGINT,

    CONSTRAINT pk_users PRIMARY KEY (id),
    CONSTRAINT uq_users_email UNIQUE (email),
    CONSTRAINT chk_users_role CHECK (role IN ('ADMIN', 'DOCTOR', 'PATIENT'))
);

CREATE INDEX idx_users_email ON users (email);
CREATE INDEX idx_users_role ON users (role);
CREATE INDEX idx_users_active_deleted ON users (active, deleted);
