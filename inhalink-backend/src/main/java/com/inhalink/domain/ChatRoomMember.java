package com.inhalink.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "chat_room_members",
        uniqueConstraints = @UniqueConstraint(columnNames = {"chat_room_id", "student_id"}))
@Getter
@NoArgsConstructor
public class ChatRoomMember extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chat_room_id", nullable = false)
    private ChatRoom chatRoom;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    private User user;

    public static ChatRoomMember of(ChatRoom chatRoom, User user) {
        ChatRoomMember member = new ChatRoomMember();
        member.chatRoom = chatRoom;
        member.user = user;
        return member;
    }
}
