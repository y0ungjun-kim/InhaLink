package com.inhalink.dto.response;

import com.inhalink.domain.ProjectPost;
import com.inhalink.domain.enums.ActivityMethod;
import com.inhalink.domain.enums.PostCategory;
import com.inhalink.domain.enums.PostStatus;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ProjectPostResponse {

    private Long id;
    private String writerStudentId;
    private String writerName;
    private String title;
    private PostCategory category;
    private String categoryDescription;
    private String projectName;
    private String content;
    private int maxMembers;
    private LocalDateTime deadline;
    private LocalDateTime teamFormationDate;
    private String preferredQualifications;
    private String message;
    private ActivityMethod activityMethod;
    private String activityMethodDescription;
    private PostStatus status;
    private String statusDescription;
    private LocalDateTime createdAt;

    public ProjectPostResponse(ProjectPost post) {
        this.id = post.getId();
        this.writerStudentId = post.getWriter().getStudentId();
        this.writerName = post.getWriter().getName();
        this.title = post.getTitle();
        this.category = post.getCategory();
        this.categoryDescription = post.getCategory().getDescription();
        this.projectName = post.getProjectName();
        this.content = post.getContent();
        this.maxMembers = post.getMaxMembers();
        this.deadline = post.getDeadline();
        this.teamFormationDate = post.getTeamFormationDate();
        this.preferredQualifications = post.getPreferredQualifications();
        this.message = post.getMessage();
        this.activityMethod = post.getActivityMethod();
        this.activityMethodDescription = post.getActivityMethod().getDescription();
        this.status = post.getStatus();
        this.statusDescription = post.getStatus().getDescription();
        this.createdAt = post.getCreatedAt();
    }
}
