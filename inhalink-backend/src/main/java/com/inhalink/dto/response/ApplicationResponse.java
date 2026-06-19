package com.inhalink.dto.response;

import com.inhalink.domain.ProjectApplication;
import lombok.Getter;

@Getter
public class ApplicationResponse {
    private final Long id;
    private final String applicantStudentId;
    private final String applicantName;
    private final String applicantDepartment;
    private final String status;

    public ApplicationResponse(ProjectApplication app) {
        this.id = app.getId();
        this.applicantStudentId = app.getApplicant().getStudentId();
        this.applicantName = app.getApplicant().getName();
        this.applicantDepartment = app.getApplicant().getDepartment();
        this.status = app.getStatus().name();
    }
}
