package com.golfclub.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@Table(name = "members")
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String memberName;

    @Column(nullable = false)
    private String memberAddress;

    @Column(nullable = false, unique = true)
    private String memberEmail;

    private String phoneNumber;

    @Column(nullable = false)
    private LocalDate startDate;

    @Column(nullable = false)
    private Integer membershipDuration;

    @ManyToMany(mappedBy = "participants")
    private Set<Tournament> tournaments = new HashSet<>();
}