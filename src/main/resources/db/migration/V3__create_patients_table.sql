CREATE TABLE patients
(
    id                      BIGSERIAL   NOT NULL,
    user_id                 BIGINT      NOT NULL,
    doctor_id               BIGINT,
    date_of_birth           DATE        NOT NULL,
    gender                  VARCHAR(10) NOT NULL,
    blood_group             VARCHAR(15),
    address                 VARCHAR(500),
    emergency_contact_name  VARCHAR(150),
    emergency_contact_phone VARCHAR(20),
    allergies               VARCHAR(500),
    medical_notes           TEXT,
    active                  BOOLEAN     NOT NULL DEFAULT TRUE,
    deleted                 BOOLEAN     NOT NULL DEFAULT FALSE,
    created_at              TIMESTAMP   NOT NULL DEFAULT NOW(),
    updated_at              TIMESTAMP   NOT NULL DEFAULT NOW(),
    created_by              BIGINT,
    updated_by              BIGINT,

    CONSTRAINT pk_patients PRIMARY KEY (id),
    CONSTRAINT uq_patients_user_id UNIQUE (user_id),
    CONSTRAINT fk_patients_user FOREIGN KEY (user_id) REFERENCES users (id),
    CONSTRAINT fk_patients_doctor FOREIGN KEY (doctor_id) REFERENCES doctors (id),
    CONSTRAINT chk_patients_gender CHECK (gender IN ('MALE', 'FEMALE', 'OTHER')),
    CONSTRAINT chk_patients_blood_group CHECK (blood_group IN (
        'A_POSITIVE', 'A_NEGATIVE', 'B_POSITIVE', 'B_NEGATIVE',
        'AB_POSITIVE', 'AB_NEGATIVE', 'O_POSITIVE', 'O_NEGATIVE'
    ))
);

CREATE INDEX idx_patients_user_id ON patients (user_id);
CREATE INDEX idx_patients_doctor_id ON patients (doctor_id);
CREATE INDEX idx_patients_gender ON patients (gender);
CREATE INDEX idx_patients_active_deleted ON patients (active, deleted);
