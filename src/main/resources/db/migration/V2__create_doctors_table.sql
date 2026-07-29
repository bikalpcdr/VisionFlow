CREATE TABLE doctors
(
    id                  BIGSERIAL    NOT NULL,
    user_id             BIGINT       NOT NULL,
    specialization      VARCHAR(50)  NOT NULL,
    license_number      VARCHAR(100) NOT NULL,
    qualification       VARCHAR(255) NOT NULL,
    years_of_experience INTEGER      NOT NULL,
    bio                 TEXT,
    clinic_name         VARCHAR(200),
    clinic_address      VARCHAR(500),
    active              BOOLEAN      NOT NULL DEFAULT TRUE,
    deleted             BOOLEAN      NOT NULL DEFAULT FALSE,
    created_at          TIMESTAMP    NOT NULL DEFAULT NOW(),
    updated_at          TIMESTAMP    NOT NULL DEFAULT NOW(),
    created_by          BIGINT,
    updated_by          BIGINT,

    CONSTRAINT pk_doctors PRIMARY KEY (id),
    CONSTRAINT uq_doctors_user_id UNIQUE (user_id),
    CONSTRAINT uq_doctors_license_number UNIQUE (license_number),
    CONSTRAINT fk_doctors_user FOREIGN KEY (user_id) REFERENCES users (id),
    CONSTRAINT chk_doctors_specialization CHECK (specialization IN (
        'OPTOMETRIST', 'OPHTHALMOLOGIST', 'VISION_THERAPIST', 'ORTHOPTIST', 'PEDIATRIC_OPTOMETRIST'
    )),
    CONSTRAINT chk_doctors_experience CHECK (years_of_experience >= 0)
);

CREATE INDEX idx_doctors_user_id ON doctors (user_id);
CREATE INDEX idx_doctors_specialization ON doctors (specialization);
CREATE INDEX idx_doctors_active_deleted ON doctors (active, deleted);
