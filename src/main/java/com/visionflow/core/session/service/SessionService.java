package com.visionflow.core.session.service;

/**
 * @author bikalpa.chaudharii
 * @project visionflow
 * @created 26/7/29
 */

import com.visionflow.core.session.dto.request.CreateSessionRequest;
import com.visionflow.core.session.dto.request.UpdateSessionRequest;
import com.visionflow.core.session.dto.response.SessionResponse;
import com.visionflow.core.session.dto.response.SessionSummaryResponse;
import com.visionflow.core.session.entity.TherapySession;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface SessionService {

    SessionResponse create(CreateSessionRequest request);

    SessionResponse update(Long id, UpdateSessionRequest request);

    SessionResponse getById(Long id);

    TherapySession getEntityById(Long id);

    Page<SessionSummaryResponse> getAll(Long planId, Long patientId, Long doctorId, String status, Pageable pageable);

    List<SessionSummaryResponse> getByPlanId(Long planId);

    SessionResponse start(Long id);

    SessionResponse complete(Long id);

    void cancel(Long id);
}
