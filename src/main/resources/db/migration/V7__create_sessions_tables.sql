CREATE TABLE therapy_sessions
(
    id                  BIGSERIAL    NOT NULL,
    therapy_plan_id     BIGINT       NOT NULL,
    patient_id          BIGINT       NOT NULL,
    doctor_id           BIGINT       NOT NULL,
    session_number      INTEGER      NOT NULL,
    session_date        TIMESTAMP    NOT NULL,
    duration_minutes    INTEGER,
    status              VARCHAR(20)  NOT NULL DEFAULT 'SCHEDULED',
    overall_performance VARCHAR(20),
    session_notes       TEXT,
    doctor_observations TEXT,
    patient_feedback    TEXT,

    -- Audit
    active              BOOLEAN      NOT NULL DEFAULT TRUE,
    deleted             BOOLEAN      NOT NULL DEFAULT FALSE,
    created_at          TIMESTAMP    NOT NULL DEFAULT NOW(),
    updated_at          TIMESTAMP    NOT NULL DEFAULT NOW(),
    created_by          BIGINT,
    updated_by          BIGINT,

    CONSTRAINT pk_therapy_sessions PRIMARY KEY (id),
    CONSTRAINT fk_sessions_plan    FOREIGN KEY (therapy_plan_id) REFERENCES therapy_plans (id),
    CONSTRAINT fk_sessions_patient FOREIGN KEY (patient_id)      REFERENCES patients (id),
    CONSTRAINT fk_sessions_doctor  FOREIGN KEY (doctor_id)       REFERENCES doctors (id),
    CONSTRAINT chk_sessions_status CHECK (status IN ('SCHEDULED', 'IN_PROGRESS', 'COMPLETED', 'CANCELLED')),
    CONSTRAINT chk_sessions_performance CHECK (overall_performance IS NULL OR overall_performance IN
        ('POOR', 'FAIR', 'GOOD', 'VERY_GOOD', 'EXCELLENT')),
    CONSTRAINT chk_sessions_duration CHECK (duration_minutes IS NULL OR duration_minutes > 0),
    CONSTRAINT uq_sessions_plan_number UNIQUE (therapy_plan_id, session_number)
);

CREATE TABLE exercise_logs
(
    id                      BIGSERIAL   NOT NULL,
    session_id              BIGINT      NOT NULL,
    exercise_id             BIGINT      NOT NULL,
    completion_status       VARCHAR(20) NOT NULL DEFAULT 'NOT_STARTED',
    actual_duration_minutes INTEGER,
    difficulty_rating       INTEGER,
    patient_notes           TEXT,
    doctor_notes            TEXT,

    -- Audit
    active                  BOOLEAN     NOT NULL DEFAULT TRUE,
    deleted                 BOOLEAN     NOT NULL DEFAULT FALSE,
    created_at              TIMESTAMP   NOT NULL DEFAULT NOW(),
    updated_at              TIMESTAMP   NOT NULL DEFAULT NOW(),
    created_by              BIGINT,
    updated_by              BIGINT,

    CONSTRAINT pk_exercise_logs PRIMARY KEY (id),
    CONSTRAINT fk_logs_session  FOREIGN KEY (session_id)  REFERENCES therapy_sessions (id),
    CONSTRAINT fk_logs_exercise FOREIGN KEY (exercise_id) REFERENCES therapy_exercises (id),
    CONSTRAINT chk_logs_completion CHECK (completion_status IN ('NOT_STARTED', 'PARTIAL', 'COMPLETED', 'SKIPPED')),
    CONSTRAINT chk_logs_difficulty CHECK (difficulty_rating IS NULL OR (difficulty_rating >= 1 AND difficulty_rating <= 5)),
    CONSTRAINT chk_logs_duration   CHECK (actual_duration_minutes IS NULL OR actual_duration_minutes > 0)
);

CREATE INDEX idx_sessions_plan_id    ON therapy_sessions (therapy_plan_id);
CREATE INDEX idx_sessions_patient_id ON therapy_sessions (patient_id);
CREATE INDEX idx_sessions_doctor_id  ON therapy_sessions (doctor_id);
CREATE INDEX idx_sessions_status     ON therapy_sessions (status);
CREATE INDEX idx_sessions_date       ON therapy_sessions (session_date DESC);

CREATE INDEX idx_exercise_logs_session_id  ON exercise_logs (session_id);
CREATE INDEX idx_exercise_logs_exercise_id ON exercise_logs (exercise_id);
