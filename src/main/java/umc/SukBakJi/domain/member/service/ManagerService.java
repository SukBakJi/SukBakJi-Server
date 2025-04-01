package umc.SukBakJi.domain.member.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import umc.SukBakJi.domain.common.entity.enums.EducationCertificateType;
import umc.SukBakJi.domain.common.entity.enums.UpdateStatus;
import umc.SukBakJi.domain.member.model.dto.EducationVerificationResponseDTO;
import umc.SukBakJi.domain.member.model.entity.Image;
import umc.SukBakJi.domain.member.model.entity.Member;
import umc.SukBakJi.domain.member.repository.MemberRepository;
import umc.SukBakJi.global.apiPayload.code.status.ErrorStatus;
import umc.SukBakJi.global.apiPayload.exception.handler.MemberHandler;
import umc.SukBakJi.global.aws.s3.AmazonS3Manager;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ManagerService {
    private final MemberRepository memberRepository;
    private final AmazonS3Manager amazonS3Manager;

    public List<EducationVerificationResponseDTO> getAllPendingVerifications() {
        List<Member> pendingMembers = memberRepository.findAllByEducationVerificationStatus(UpdateStatus.PENDING);

        return pendingMembers.stream()
                .map(member -> {
                    Image image = member.getEducationCertificateImage();
                    EducationCertificateType type = image.getType();
                    String uuid = image.getUuid();

                    String key = amazonS3Manager.generateEducationCertificateKeyName(member.getId(), type, uuid);
                    String url = amazonS3Manager.getFileUrl(key);

                    return EducationVerificationResponseDTO.builder()
                            .memberId(member.getId())
                            .email(member.getEmail())
                            .name(member.getName())
                            .educationCertificateImageUrl(url)
                            .build();
                })
                .collect(Collectors.toList());
    }

    public Member approveEducation(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND));

        Image image = member.getEducationCertificateImage();
        if (image != null) {
            String key = amazonS3Manager.generateEducationCertificateKeyName(member.getId(), image.getType(), image.getUuid());
            amazonS3Manager.deleteFile(key);
        }

        member.setEducationVerificationStatus(UpdateStatus.APPROVED);
        memberRepository.save(member);
        return member;
    }

    public Member rejectEducation(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND));
        member.setEducationVerificationStatus(UpdateStatus.REJECTED);
        memberRepository.save(member);
        return member;
    }
}
