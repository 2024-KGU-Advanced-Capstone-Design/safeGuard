package com.capstone.safeGuard.service;

import com.capstone.safeGuard.domain.*;
import com.capstone.safeGuard.dto.request.emergency.CommentRequestDTO;
import com.capstone.safeGuard.dto.request.emergency.EmergencyRequestDTO;
import com.capstone.safeGuard.dto.request.notification.FCMNotificationDTO;
import com.capstone.safeGuard.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EmergencyService {
    private final EmergencyRepository emergencyRepository;
    private final MemberRepository memberRepository;
    private final ChildRepository childRepository;
    private final MemberService memberService;
    private final CommentRepository commentRepository;
    private final EmergencyReceiverRepository emergencyReceiverRepository;
    private final FCMService fcmService;

    public ArrayList<String> getNeighborMembers(EmergencyRequestDTO dto, int distance){
        ArrayList<String> memberIdList = new ArrayList<>();
        ArrayList<Member> allMember = memberService.findAllMember();

        for (Member member : allMember) {
            if (isNeighbor(dto.getLatitude(), dto.getLongitude(), member.getLatitude(), member.getLongitude(), distance)){
                memberIdList.add(member.getMemberId());
            }
        }

        return memberIdList;
    }

    private boolean isNeighbor(double latitude, double longitude, double memberLatitude, double memberLongitude, int length) {
        double latitudeDistance = latitude - memberLatitude;
        double longitudeDistance = longitude - memberLongitude;

        // 좌표 -> km
        double distance = convertCoordinateToKm(latitudeDistance, longitudeDistance);

        return distance <= (length);
    }

    private double convertCoordinateToKm(double latitudeDistance, double longitudeDistance) {
        // 위도 35~38도(한국) 기준으로
        // 대략 위도는 1도당 111km, 경도는 1도당 89km
        double latitudeKm = 111 * latitudeDistance;
        double longitudeKm = 89 * longitudeDistance;

        return Math.sqrt( (latitudeKm * latitudeKm) + (longitudeKm * longitudeKm) );
    }

    @Transactional
    public Emergency saveEmergency(String receiverId, EmergencyRequestDTO dto) {
        // Emergency table에 저장
        Member member = memberRepository.findById(dto.getSenderId()).orElseThrow(NoSuchElementException::new);
        Child child = childRepository.findBychildName(dto.getChildName());
        String content = "아이 이름 : " + dto.getChildName();

        Emergency emergency = dto.dtoToDomain(member, child, content);
        emergencyRepository.save(emergency);

        EmergencyReceiver emergencyReceiver = EmergencyReceiver.builder()
                .emergency(emergency)
                .emergencyReceiverId(receiverId)
                .build();
        emergencyReceiverRepository.save(emergencyReceiver);

        return emergency;
    }

    @Transactional
    public boolean sendNotificationTo(String receiverId, Emergency emergency){
        FCMNotificationDTO message = makeMessage(receiverId, emergency);
        return fcmService.SendNotificationByToken(message) != null;
    }

    private FCMNotificationDTO makeMessage(String receiverId, Emergency emergency) {
        return FCMNotificationDTO.builder()
                .title(emergency.getTitle())
                .body(emergency.getContent())
                .receiverId(receiverId)
                .build();
    }

    public List<Emergency> getSentEmergency(String memberId) {
        Optional<Member> foundMember = memberRepository.findById(memberId);
        if (foundMember.isEmpty()){
            return null;
        }

        List<Emergency> foundEmergency = emergencyRepository.findAllBySenderId(foundMember.get());
        if (foundEmergency.isEmpty()){
            return null;
        }

        return foundEmergency;
    }

    public List<Emergency> getReceivedEmergency(String memberId) {
        List<Emergency> result = new ArrayList<>();

        List<EmergencyReceiver> foundEmergencyList = emergencyReceiverRepository.findAllByReceiverId(memberId);
        if (foundEmergencyList.isEmpty()){
            return null;
        }

        for (EmergencyReceiver received : foundEmergencyList) {
            result.add(received.getEmergency());
        }

        return result;
    }

    @Transactional
    public boolean writeComment(CommentRequestDTO commentRequestDTO) {
        Optional<Member> foundMember = memberRepository.findById(commentRequestDTO.getCommentatorId());
        Optional<Emergency> foundEmergency = emergencyRepository.findById(Long.valueOf(commentRequestDTO.getEmergencyId()));
        if(foundMember.isEmpty() || foundEmergency.isEmpty()){
            return false;
        }

        Comment comment = Comment.builder()
                .commentator(foundMember.get())
                .emergency(foundEmergency.get())
                .comment(commentRequestDTO.getCommentContent())
                .build();

        commentRepository.save(comment);
        foundEmergency.get().commentList.add(comment);

        return true;
    }


    @Transactional
    public Emergency getEmergencyDetail(String emergencyId) {
        Optional<Emergency> foundEmergency = emergencyRepository.findById(Long.valueOf(emergencyId));
        return foundEmergency.orElse(null);
    }

    @Transactional
    public List<Comment> getCommentOfEmergency(String emergencyId) {
        Emergency emergencyDetail = getEmergencyDetail(emergencyId);

        return commentRepository.findAllByEmergency(emergencyDetail);
    }
}
