package com.golfclub.service;

import com.golfclub.model.Member;
import com.golfclub.repository.MemberRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class MemberService {
    private final MemberRepo memberRepository;

    @Autowired
    public MemberService(MemberRepo memberRepository) {
        this.memberRepository = memberRepository;
    }

    public Member saveMember(Member member) {
        validateMember(member);
        return memberRepository.save(member);
    }

    private void validateMember(Member member) {
        if (member.getMemberEmail() != null &&
                memberRepository.findByMemberEmail(member.getMemberEmail()).isPresent()) {
            throw new IllegalArgumentException("Email already exists");
        }
        if (member.getMemberPhone() != null &&
                memberRepository.findByMemberPhone(member.getMemberPhone()).isPresent()) {
            throw new IllegalArgumentException("Phone number already exists");
        }
    }

    public Optional<Member> getMemberById(Long id) {
        return memberRepository.findById(id);
    }

    public List<Member> getAllMembers() {
        return memberRepository.findAll();
    }

    public Member updateMember(Long id, Member updatedMember) {
        return memberRepository.findById(id)
                .map(existing -> {
                    if (!existing.getMemberEmail().equals(updatedMember.getMemberEmail())) {
                        validateMember(updatedMember);
                    }
                    existing.setMemberName(updatedMember.getMemberName());
                    existing.setMemberAddress(updatedMember.getMemberAddress());
                    existing.setMemberEmail(updatedMember.getMemberEmail());
                    existing.setMemberPhone(updatedMember.getMemberPhone());
                    existing.setStartDate(updatedMember.getStartDate());
                    existing.setDuration(updatedMember.getDuration());
                    return memberRepository.save(existing);
                })
                .orElseThrow(() -> new IllegalArgumentException("Member not found"));
    }

    public void deleteMember(Long id) {
        memberRepository.deleteById(id);
    }

    public List<Member> searchByName(String name) {
        return memberRepository.findByMemberNameContainingIgnoreCase(name);
    }

    public List<Member> searchByPhone(String phone) {
        return memberRepository.findByMemberPhoneContaining(phone);
    }

    public List<Member> findByStatus(Member.MembershipStatus status) {
        return memberRepository.findByStatus(status);
    }

    public List<Member> findByMinimumTournaments(Integer count) {
        return memberRepository.findByTotalTournamentsPlayedGreaterThan(count);
    }

    public void updateMemberStatus(Long memberId, Member.MembershipStatus status) {
        memberRepository.findById(memberId)
                .ifPresent(member -> {
                    member.setStatus(status);
                    memberRepository.save(member);
                });
    }

    public List<Member> findActiveMembers() {
        return memberRepository.findActiveMembers(LocalDate.now());
    }

    public List<Member> findByTournamentDate(LocalDate date) {
        return memberRepository.findByTournamentStartDate(date);
    }
}
