package com.framework.dto.response.trello;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class GetCards {

    private String id;
    private Agent agent;
    private Badges badges;

    private List<Object> checkItemStates;

    private boolean closed;
    private boolean dueComplete;

    private String dateLastActivity;
    private String desc;
    private Object descData;

    private Object due;
    private Object dueReminder;
    private Object email;

    private String idBoard;
    private List<Object> idChecklists;
    private List<Object> idMembers;
    private List<Object> idMembersVoted;

    private int idShort;

    private Object idAttachmentCover;

    private List<Object> labels;
    private List<Object> idLabels;

    private boolean manualCoverAttachment;

    private String name;
    private String nodeId;

    private boolean pinned;
    private int pos;

    private String shortLink;
    private String shortUrl;

    private Object start;
    private boolean subscribed;

    private String url;

    private Cover cover;

    private boolean isTemplate;
    private Object cardRole;
    private Object mirrorSourceId;

    // ================= NESTED CLASSES =================

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Agent {
        private String name;
        private String conversationId;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Badges {
        private int attachments;
        private String fogbugz;

        private int checkItems;
        private int checkItemsChecked;

        private Object checkItemsEarliestDue;

        private int comments;
        private boolean description;

        private Object due;
        private boolean dueComplete;

        private boolean lastUpdatedByAi;

        private Object start;
        private Object externalSource;

        private AttachmentsByType attachmentsByType;

        private boolean location;
        private int votes;

        private int maliciousAttachments;
        private boolean viewingMemberVoted;
        private boolean subscribed;

        @Data
        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class AttachmentsByType {
            private Trello trello;

            @Data
            @JsonIgnoreProperties(ignoreUnknown = true)
            public static class Trello {
                private int board;
                private int card;
            }
        }
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Cover {
        private Object idAttachment;
        private Object color;
        private Object idUploadedBackground;

        private String size;
        private String brightness;

        private double yPosition;

        private Object idPlugin;
    }
}