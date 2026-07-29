CREATE TABLE appointments
(
    id                  BIGSERIAL    NOT NULL,
    patient_id          BIGINT       NOT NULL,
    doctor_id           BIGINT       NOT NULL,
    assessment_id       BIGINT,
    therapy_plan_id     BIGINT,
    appointment_type    VARCHAR(30)  NOT NULL,
    status              VARCHAR(20)  NOT NULL DEFAULT 'REQUESTED',
    appointment_date    TIMESTAMP    NOT NULL,
    duration_minutes    INTEGER      NOT NULL,
    patient_notes       TEXT,
    doctor_notes        TEXT,
    cancellation_reason TEXT,

    -- Audit
    active              BOOLEAN      NOT NULL DEFAULT TRUE,
    deleted             BOOLEAN      NOT NULL DEFAULT FALSE,
    created_at          TIMESTAMP    NOT NULL DEFAULT NOW(),
    updated_at          TIMESTAMP    NOT NULL DEFAULT NOW(),
    created_by          BIGINT,
    updated_by          BIGINT,

    CONSTRAINT pk_appointments          PRIMARY KEY (id),
    CONSTRAINT fk_appointments_patient  FOREIGN KEY (patient_id)      REFERENCES patients (id),
    CONSTRAINT fk_appointments_doctor   FOREIGN KEY (doctor_id)       REFERENCES doctors (id),
    CONSTRAINT fk_appointments_assess   FOREIGN KEY (assessment_id)   REFERENCES assessments (id),
    CONSTRAINT fk_appointments_plan     FOREIGN KEY (therapy_plan_id) REFERENCES therapy_plans (id),
    CONSTRAINT chk_appointments_type   CHECK (appointment_type IN (
        'INITIAL_ASSESSMENT', 'FOLLOW_UP', 'THERAPY_SESSION', 'REVIEW', 'EMERGENCY'
    )),
    CONSTRAINT chk_appointments_status CHECK (status IN (
        'REQUESTED', 'CONFIRMED', 'IN_PROGRESS', 'COMPLETED', 'CANCELLED', 'NO_SHOW'
    )),
    CONSTRAINT chk_appointments_duration CHECK (duration_minutes > 0)
);

CREATE INDEX idx_appointments_patient_id ON appointments (patient_id);
CREATE INDEX idx_appointments_doctor_id  ON appointments (doctor_id);
CREATE INDEX idx_appointments_status     ON appointments (status);
CREATE INDEX idx_appointments_date       ON appointments (appointment_date DESC);
CREATE INDEX idx_appointments_type       ON appointments (appointment_type);
