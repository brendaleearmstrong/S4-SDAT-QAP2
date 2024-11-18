package com.golfclub.controller;

import com.golfclub.model.Tournament;
import com.golfclub.service.TournamentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/tournaments")
public class TournamentController {
    private final TournamentService tournamentService;

    @Autowired
    public TournamentController(TournamentService tournamentService) {
        this.tournamentService = tournamentService;
    }

    @PostMapping
    public ResponseEntity<Tournament> createTournament(@Valid @RequestBody Tournament tournament) {
        try {
            Tournament savedTournament = tournamentService.saveTournament(tournament);
            return new ResponseEntity<>(savedTournament, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping
    public List<Tournament> getAllTournaments() {
        return tournamentService.getAllTournaments();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Tournament> getTournament(@PathVariable Long id) {
        return tournamentService.getTournamentById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Tournament> updateTournament(
            @PathVariable Long id,
            @Valid @RequestBody Tournament tournament) {
        try {
            Tournament updated = tournamentService.updateTournament(id, tournament);
            return ResponseEntity.ok(updated);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTournament(@PathVariable Long id) {
        tournamentService.deleteTournament(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{tournamentId}/members/{memberId}")
    public ResponseEntity<Tournament> addMemberToTournament(
            @PathVariable Long tournamentId,
            @PathVariable Long memberId) {
        try {
            Tournament updated = tournamentService.addMemberToTournament(tournamentId, memberId);
            return ResponseEntity.ok(updated);
        } catch (IllegalArgumentException | IllegalStateException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{tournamentId}/members/{memberId}")
    public ResponseEntity<Tournament> removeMemberFromTournament(
            @PathVariable Long tournamentId,
            @PathVariable Long memberId) {
        try {
            Tournament updated = tournamentService.removeMemberFromTournament(tournamentId, memberId);
            return ResponseEntity.ok(updated);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/search/location/{location}")
    public List<Tournament> searchByLocation(@PathVariable String location) {
        return tournamentService.findByLocation(location);
    }

    @GetMapping("/search/date-range")
    public List<Tournament> searchByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        return tournamentService.findByDateRange(startDate, endDate);
    }

    @GetMapping("/search/status/{status}")
    public List<Tournament> searchByStatus(@PathVariable Tournament.TournamentStatus status) {
        return tournamentService.findByStatus(status);
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<Void> updateStatus(
            @PathVariable Long id,
            @RequestBody Tournament.TournamentStatus status) {
        try {
            tournamentService.updateTournamentStatus(id, status);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException | IllegalStateException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/{id}/revenue")
    public ResponseEntity<Double> getTournamentRevenue(@PathVariable Long id) {
        return ResponseEntity.ok(tournamentService.calculateTournamentRevenue(id));
    }

    @GetMapping("/revenue/total")
    public ResponseEntity<Double> getTotalRevenue() {
        return ResponseEntity.ok(tournamentService.calculateTotalRevenue());
    }

    @GetMapping("/current")
    public List<Tournament> getCurrentTournaments() {
        return tournamentService.findCurrentTournaments();
    }
}