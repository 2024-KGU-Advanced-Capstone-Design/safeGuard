package com.capstone.safeGuard.dto.request.emergency;

import com.capstone.safeGuard.domain.Child;
import com.capstone.safeGuard.domain.Emergency;
import com.capstone.safeGuard.domain.Member;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class EmergencyRequestDTO {
    private String senderId;
    private String childName;
    private float latitude;
    private float longitude;

    private final String title = "주변 아이에게 도움이 필요합니다.";

    public Emergency dtoToDomain(Member member, Child child, String content){
        return Emergency
                .builder()
                .title(title)
                .content(content)
                .senderId(member)
                .child(child)
                .createdAt(LocalDateTime.now())
                .build();
    }
}