package com.golfclub.service;

import com.golfclub.model.Member;
import com.golfclub.model.Tournament;
import com.golfclub.repository.MemberRepo;
import com.golfclub.repository.TournamentRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class TournamentService {
    private final TournamentRepo tournamentRepository;
    private final MemberRepo memberRepository;

    @Autowired
    public TournamentService(TournamentRepo tournamentRepository, MemberRepo memberRepository) {
        this.tournamentRepository = tournamentRepository;
        this.memberRepository = memberRepository;
    }

    public Tournament saveTournament(Tournament tournament) {
        validateTournament(tournament);
        return tournamentRepository.save(tournament);
    }

    private void validateTournament(Tournament tournament) {
        if (tournament.getEndDate().isBefore(tournament.getStartDate())) {
            throw new IllegalArgumentException("End date cannot be before start date");
        }
        if (tournament.getMinimumParticipants() > tournament.getMaximumParticipants()) {
            throw new IllegalArgumentException("Minimum participants cannot be greater than maximum");
        }
    }

    public Optional<Tournament> getTournamentById(Long id) {
        return tournamentRepository.findById(id);
    }

    public List<Tournament> getAllTournaments() {
        return tournamentRepository.findAll();
    }

    public Tournament updateTournament(Long id, Tournament tournamentDetails) {
        return tournamentRepository.findById(id)
                .map(existing -> {
                    validateTournament(tournamentDetails);
                    existing.setStartDate(tournamentDetails.getStartDate());
                    existing.setEndDate(tournamentDetails.getEndDate());
                    existing.setLocation(tournamentDetails.getLocation());
                    existing.setEntryFee(tournamentDetails.getEntryFee());
                    existing.setCashPrizeAmount(tournamentDetails.getCashPrizeAmount());
                    existing.setMinimumParticipants(tournamentDetails.getMinimumParticipants());
                    existing.setMaximumParticipants(tournamentDetails.getMaximumParticipants());
                    return tournamentRepository.save(existing);
                })
                .orElseThrow(() -> new IllegalArgumentException("Tournament not found"));
    }

    public void deleteTournament(Long id) {
        tournamentRepository.deleteById(id);
    }

    public Tournament addMemberToTournament(Long tournamentId, Long memberId) {
        Tournament tournament = tournamentRepository.findById(tournamentId)
                .orElseThrow(() -> new IllegalArgumentException("Tournament not found"));
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("Member not found"));

        if (tournament.getParticipatingMembers().size() >= tournament.getMaximumParticipants()) {
            throw new IllegalStateException("Tournament has reached maximum participants");
        }

        if (member.getStatus() != Member.MembershipStatus.ACTIVE) {
            throw new IllegalStateException("Member is not active");
        }

        tournament.addMember(member);
        return tournamentRepository.save(tournament);
    }

    public Tournament removeMemberFromTournament(Long tournamentId, Long memberId) {
        Tournament tournament = tournamentRepository.findById(tournamentId)
                .orElseThrow(() -> new IllegalArgumentException("Tournament not found"));
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("Member not found"));

        tournament.removeMember(member);
        return tournamentRepository.save(tournament);
    }

    public List<Tournament> findByLocation(String location) {
        return tournamentRepository.findByLocationContainingIgnoreCase(location);
    }

    public List<Tournament> findByDateRange(LocalDate startDate, LocalDate endDate) {
        return tournamentRepository.findByStartDateBetween(startDate, endDate);
    }

    public List<Tournament> findByStatus(Tournament.TournamentStatus status) {
        return tournamentRepository.findByStatus(status);
    }

    public void updateTournamentStatus(Long tournamentId, Tournament.TournamentStatus status) {
        tournamentRepository.findById(tournamentId)
                .ifPresent(tournament -> {
                    validateStatusTransition(tournament, status);
                    tournament.setStatus(status);
                    if (status == Tournament.TournamentStatus.COMPLETED) {
                        updateMemberStats(tournament);
                    }
                    tournamentRepository.save(tournament);
                });
    }

    private void validateStatusTransition(Tournament tournament, Tournament.TournamentStatus newStatus) {
        if (tournament.getStatus() == Tournament.TournamentStatus.COMPLETED &&
                newStatus != Tournament.TournamentStatus.COMPLETED) {
            throw new IllegalStateException("Cannot change status of completed tournament");
        }
        if (tournament.getParticipatingMembers().size() < tournament.getMinimumParticipants() &&
                newStatus == Tournament.TournamentStatus.IN_PROGRESS) {
            throw new IllegalStateException("Cannot start tournament with insufficient participants");
        }
    }

    private void updateMemberStats(Tournament tournament) {
        for (Member member : tournament.getParticipatingMembers()) {
            member.setTotalTournamentsPlayed(member.getTotalTournamentsPlayed() + 1);
            memberRepository.save(member);
        }
    }

    public Double calculateTotalRevenue() {
        return tournamentRepository.calculateTotalRevenue();
    }

    public Double calculateTournamentRevenue(Long tournamentId) {
        return tournamentRepository.findById(tournamentId)
                .map(t -> t.getEntryFee() * t.getParticipatingMembers().size())
                .orElse(0.0);
    }

    public List<Tournament> findCurrentTournaments() {
        return tournamentRepository.findCurrentTournaments(LocalDate.now());
    }
}