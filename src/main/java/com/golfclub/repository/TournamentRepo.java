package com.golfclub.repository;

import com.golfclub.model.Tournament;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.time.LocalDate;
import java.util.List;

public interface TournamentRepo extends JpaRepository<Tournament, Long> {
    List<Tournament> findByStartDateBetween(LocalDate startDate, LocalDate endDate);
    List<Tournament> findByLocationContainingIgnoreCase(String location);
    List<Tournament> findByStatus(Tournament.TournamentStatus status);
    List<Tournament> findByEntryFeeLessThanEqual(Double maxFee);
    List<Tournament> findByCashPrizeAmountGreaterThanEqual(Double minPrize);

    @Query("SELECT t FROM Tournament t WHERE SIZE(t.participatingMembers) >= :minCount")
    List<Tournament> findByMinimumParticipants(@Param("minCount") Integer minCount);

    @Query("SELECT t FROM Tournament t WHERE SIZE(t.participatingMembers) < t.maximumParticipants")
    List<Tournament> findAvailableTournaments();

    @Query("SELECT SUM(t.entryFee * SIZE(t.participatingMembers)) FROM Tournament t WHERE t.status = 'COMPLETED'")
    Double calculateTotalRevenue();

    @Query("SELECT t FROM Tournament t WHERE t.startDate <= :date AND t.endDate >= :date")
    List<Tournament> findCurrentTournaments(@Param("date") LocalDate date);
}