CREATE TABLE therapy_plans
(
    id            BIGSERIAL    NOT NULL,
    patient_id    BIGINT       NOT NULL,
    doctor_id     BIGINT       NOT NULL,
    assessment_id BIGINT,
    title         VARCHAR(255) NOT NULL,
    description   TEXT,
    status        VARCHAR(20)  NOT NULL DEFAULT 'DRAFT',
    start_date    DATE         NOT NULL,
    end_date      DATE,
    goals         TEXT,
    notes         TEXT,
    active        BOOLEAN      NOT NULL DEFAULT TRUE,
    deleted       BOOLEAN      NOT NULL DEFAULT FALSE,
    created_at    TIMESTAMP    NOT NULL DEFAULT NOW(),
    updated_at    TIMESTAMP    NOT NULL DEFAULT NOW(),
    created_by    BIGINT,
    updated_by    BIGINT,

    CONSTRAINT pk_therapy_plans PRIMARY KEY (id),
    CONSTRAINT fk_therapy_plans_patient    FOREIGN KEY (patient_id)    REFERENCES patients (id),
    CONSTRAINT fk_therapy_plans_doctor     FOREIGN KEY (doctor_id)     REFERENCES doctors (id),
    CONSTRAINT fk_therapy_plans_assessment FOREIGN KEY (assessment_id) REFERENCES assessments (id),
    CONSTRAINT chk_therapy_plans_status CHECK (status IN ('DRAFT', 'ACTIVE', 'COMPLETED', 'CANCELLED')),
    CONSTRAINT chk_therapy_plans_dates CHECK (end_date IS NULL OR end_date > start_date)
);

CREATE INDEX idx_therapy_plans_patient_id     ON therapy_plans (patient_id);
CREATE INDEX idx_therapy_plans_doctor_id      ON therapy_plans (doctor_id);
CREATE INDEX idx_therapy_plans_status         ON therapy_plans (status);
CREATE INDEX idx_therapy_plans_active_deleted ON therapy_plans (active, deleted);

CREATE TABLE therapy_exercises
(
    id              BIGSERIAL    NOT NULL,
    therapy_plan_id BIGINT       NOT NULL,
    exercise_type   VARCHAR(50)  NOT NULL,
    name            VARCHAR(200) NOT NULL,
    description     TEXT,
    frequency       INTEGER      NOT NULL,
    frequency_unit  VARCHAR(20)  NOT NULL,
    duration_minutes INTEGER     NOT NULL,
    repetitions     INTEGER,
    order_index     INTEGER      NOT NULL,
    instructions    TEXT,
    active          BOOLEAN      NOT NULL DEFAULT TRUE,
    deleted         BOOLEAN      NOT NULL DEFAULT FALSE,
    created_at      TIMESTAMP    NOT NULL DEFAULT NOW(),
    updated_at      TIMESTAMP    NOT NULL DEFAULT NOW(),
    created_by      BIGINT,
    updated_by      BIGINT,

    CONSTRAINT pk_therapy_exercises PRIMARY KEY (id),
    CONSTRAINT fk_therapy_exercises_plan FOREIGN KEY (therapy_plan_id) REFERENCES therapy_plans (id),
    CONSTRAINT chk_therapy_exercises_frequency CHECK (frequency > 0),
    CONSTRAINT chk_therapy_exercises_duration  CHECK (duration_minutes > 0),
    CONSTRAINT chk_therapy_exercises_order     CHECK (order_index > 0),
    CONSTRAINT chk_therapy_exercises_type CHECK (exercise_type IN (
        'PATCHING', 'PENCIL_PUSH_UPS', 'BROCK_STRING', 'VECTOGRAMS', 'STEREOGRAM',
        'SACCADES', 'PURSUITS', 'ACCOMMODATIVE_ROCK', 'CONVERGENCE_EXERCISES',
        'DIVERGENCE_EXERCISES', 'ANTI_SUPPRESSION', 'FIXATION_DISPARITY', 'OTHER'
    )),
    CONSTRAINT chk_therapy_exercises_freq_unit CHECK (frequency_unit IN (
        'TIMES_PER_DAY', 'TIMES_PER_WEEK', 'TIMES_PER_MONTH'
    ))
);

CREATE INDEX idx_therapy_exercises_plan_id      ON therapy_exercises (therapy_plan_id);
CREATE INDEX idx_therapy_exercises_active_deleted ON therapy_exercises (active, deleted);
