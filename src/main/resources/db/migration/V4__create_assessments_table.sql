CREATE TABLE assessments
(
    id                      BIGSERIAL    NOT NULL,
    patient_id              BIGINT       NOT NULL,
    doctor_id               BIGINT       NOT NULL,
    assessment_type         VARCHAR(50)  NOT NULL,
    status                  VARCHAR(20)  NOT NULL DEFAULT 'DRAFT',
    assessment_date         TIMESTAMP    NOT NULL,

    -- Visual Acuity
    visual_acuity_left      VARCHAR(20),
    visual_acuity_right     VARCHAR(20),
    visual_acuity_binocular VARCHAR(20),

    -- Refraction
    sphere_left             NUMERIC(5, 2),
    sphere_right            NUMERIC(5, 2),
    cylinder_left           NUMERIC(5, 2),
    cylinder_right          NUMERIC(5, 2),
    axis_left               SMALLINT,
    axis_right              SMALLINT,

    -- Binocular Vision
    cover_test_result       TEXT,
    npc_result              VARCHAR(100),
    stereopsis_result       VARCHAR(100),

    -- Condition-Specific
    severity                VARCHAR(20),
    affected_eye            VARCHAR(20),
    iop_left                NUMERIC(5, 2),
    iop_right               NUMERIC(5, 2),

    -- Clinical Notes
    chief_complaint         TEXT,
    clinical_findings       TEXT,
    recommendations         TEXT,
    follow_up_date          TIMESTAMP,

    -- Audit
    active                  BOOLEAN      NOT NULL DEFAULT TRUE,
    deleted                 BOOLEAN      NOT NULL DEFAULT FALSE,
    created_at              TIMESTAMP    NOT NULL DEFAULT NOW(),
    updated_at              TIMESTAMP    NOT NULL DEFAULT NOW(),
    created_by              BIGINT,
    updated_by              BIGINT,

    CONSTRAINT pk_assessments PRIMARY KEY (id),
    CONSTRAINT fk_assessments_patient FOREIGN KEY (patient_id) REFERENCES patients (id),
    CONSTRAINT fk_assessments_doctor  FOREIGN KEY (doctor_id)  REFERENCES doctors (id),
    CONSTRAINT chk_assessments_type CHECK (assessment_type IN (
        'AMBLYOPIA', 'STRABISMUS', 'CONVERGENCE_INSUFFICIENCY',
        'BINOCULAR_VISION', 'ACCOMMODATION', 'COLOR_VISION', 'VISUAL_FIELD', 'GENERAL'
    )),
    CONSTRAINT chk_assessments_status CHECK (status IN (
        'DRAFT', 'COMPLETED', 'REVIEWED', 'CANCELLED'
    )),
    CONSTRAINT chk_assessments_severity CHECK (severity IS NULL OR severity IN (
        'NONE', 'MILD', 'MODERATE', 'SEVERE'
    )),
    CONSTRAINT chk_axis_left  CHECK (axis_left  IS NULL OR (axis_left  >= 0 AND axis_left  <= 180)),
    CONSTRAINT chk_axis_right CHECK (axis_right IS NULL OR (axis_right >= 0 AND axis_right <= 180))
);

CREATE INDEX idx_assessments_patient_id     ON assessments (patient_id);
CREATE INDEX idx_assessments_doctor_id      ON assessments (doctor_id);
CREATE INDEX idx_assessments_type           ON assessments (assessment_type);
CREATE INDEX idx_assessments_status         ON assessments (status);
CREATE INDEX idx_assessments_date           ON assessments (assessment_date DESC);
CREATE INDEX idx_assessments_active_deleted ON assessments (active, deleted);
