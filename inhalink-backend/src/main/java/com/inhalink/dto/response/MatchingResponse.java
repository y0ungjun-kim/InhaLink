package com.inhalink.dto.response;

import com.inhalink.domain.User;
import com.inhalink.domain.enums.MatchingStatus;
import lombok.Getter;

@Getter
public class MatchingResponse {

    private MatchingStatus status;
    private String statusDescription;
    private PartnerInfo partner;

    private MatchingResponse(MatchingStatus status, PartnerInfo partner) {
        this.status = status;
        this.statusDescription = status.getDescription();
        this.partner = partner;
    }

    public static MatchingResponse waiting() {
        return new MatchingResponse(MatchingStatus.WAITING, null);
    }

    public static MatchingResponse matched(User partner) {
        return new MatchingResponse(MatchingStatus.MATCHED, new PartnerInfo(partner));
    }

    public static MatchingResponse cancelled() {
        return new MatchingResponse(MatchingStatus.CANCELLED, null);
    }

    @Getter
    public static class PartnerInfo {
        private String name;
        private String department;
        private String domains;
        private String activities;
        private String contact;

        public PartnerInfo(User user) {
            this.name = user.getName();
            this.department = user.getDepartment();
            this.domains = user.getDomains();
            this.activities = user.getActivities();
            this.contact = user.getContact();
        }
    }
}
