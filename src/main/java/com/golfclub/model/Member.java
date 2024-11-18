package com.golfclub.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "members", indexes = {
        @Index(name = "idx_member_email", columnList = "memberEmail"),
        @Index(name = "idx_member_phone", columnList = "memberPhone")
})
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Name is required")
    @Pattern(regexp = "^[a-zA-Z\\s]{2,50}$", message = "Name must be 2-50 characters long and contain only letters")
    private String memberName;

    @NotBlank(message = "Address is required")
    private String memberAddress;

    @Email(message = "Invalid email format")
    @NotBlank(message = "Email is required")
    @Column(unique = true)
    private String memberEmail;

    @Pattern(regexp = "^\\d{3}-\\d{3}-\\d{4}$", message = "Phone number must be in format XXX-XXX-XXXX")
    @Column(unique = true)
    private String memberPhone;

    @NotNull(message = "Start date is required")
    @PastOrPresent(message = "Start date cannot be in the future")
    private LocalDate startDate;

    @Min(value = 1, message = "Duration must be at least 1 month")
    @Max(value = 60, message = "Duration cannot exceed 60 months")
    private Integer duration;

    @ManyToMany(mappedBy = "participatingMembers")
    private List<Tournament> tournaments = new ArrayList<>();

    @Version
    private Long version;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MembershipStatus status = MembershipStatus.ACTIVE;

    @Column(name = "total_tournaments_played")
    private Integer totalTournamentsPlayed = 0;

    @Column(name = "total_winnings")
    private Double totalWinnings = 0.0;

    public enum MembershipStatus {
        ACTIVE, EXPIRED, SUSPENDED, PENDING
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMemberName() {
        return memberName;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    public String getMemberAddress() {
        return memberAddress;
    }

    public void setMemberAddress(String memberAddress) {
        this.memberAddress = memberAddress;
    }

    public String getMemberEmail() {
        return memberEmail;
    }

    public void setMemberEmail(String memberEmail) {
        this.memberEmail = memberEmail;
    }

    public String getMemberPhone() {
        return memberPhone;
    }

    public void setMemberPhone(String memberPhone) {
        this.memberPhone = memberPhone;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public List<Tournament> getTournaments() {
        return tournaments;
    }

    public void setTournaments(List<Tournament> tournaments) {
        this.tournaments = tournaments;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    public MembershipStatus getStatus() {
        return status;
    }

    public void setStatus(MembershipStatus status) {
        this.status = status;
    }

    public Integer getTotalTournamentsPlayed() {
        return totalTournamentsPlayed;
    }

    public void setTotalTournamentsPlayed(Integer totalTournamentsPlayed) {
        this.totalTournamentsPlayed = totalTournamentsPlayed;
    }

    public Double getTotalWinnings() {
        return totalWinnings;
    }

    public void setTotalWinnings(Double totalWinnings) {
        this.totalWinnings = totalWinnings;
    }
}