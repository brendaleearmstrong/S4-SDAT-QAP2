package com.golfclub.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "tournaments", indexes = {
        @Index(name = "idx_tournament_date", columnList = "startDate"),
        @Index(name = "idx_tournament_location", columnList = "location")
})
public class Tournament {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @FutureOrPresent
    private LocalDate startDate;

    @NotNull
    @FutureOrPresent
    private LocalDate endDate;

    @NotBlank
    private String location;

    @Positive
    @Column(nullable = false)
    private Double entryFee;

    @PositiveOrZero
    @Column(nullable = false)
    private Double cashPrizeAmount;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
            name = "tournament_members",
            joinColumns = @JoinColumn(name = "tournament_id"),
            inverseJoinColumns = @JoinColumn(name = "member_id")
    )
    private List<Member> participatingMembers = new ArrayList<>();

    @Version
    private Long version;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TournamentStatus status = TournamentStatus.SCHEDULED;

    @Min(2)
    @Column(nullable = false)
    private Integer minimumParticipants = 2;

    @Max(100)
    @Column(nullable = false)
    private Integer maximumParticipants = 100;

    public enum TournamentStatus {
        SCHEDULED, IN_PROGRESS, COMPLETED, CANCELLED
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Double getEntryFee() {
        return entryFee;
    }

    public void setEntryFee(Double entryFee) {
        this.entryFee = entryFee;
    }

    public Double getCashPrizeAmount() {
        return cashPrizeAmount;
    }

    public void setCashPrizeAmount(Double cashPrizeAmount) {
        this.cashPrizeAmount = cashPrizeAmount;
    }

    public List<Member> getParticipatingMembers() {
        return participatingMembers;
    }

    public void setParticipatingMembers(List<Member> participatingMembers) {
        this.participatingMembers = participatingMembers;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    public TournamentStatus getStatus() {
        return status;
    }

    public void setStatus(TournamentStatus status) {
        this.status = status;
    }

    public Integer getMinimumParticipants() {
        return minimumParticipants;
    }

    public void setMinimumParticipants(Integer minimumParticipants) {
        this.minimumParticipants = minimumParticipants;
    }

    public Integer getMaximumParticipants() {
        return maximumParticipants;
    }

    public void setMaximumParticipants(Integer maximumParticipants) {
        this.maximumParticipants = maximumParticipants;
    }

    public void addMember(Member member) {
        participatingMembers.add(member);
        member.getTournaments().add(this);
    }

    public void removeMember(Member member) {
        participatingMembers.remove(member);
        member.getTournaments().remove(this);
    }
}