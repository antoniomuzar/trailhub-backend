package com.trailhub.backend.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;

@Entity
@Table(name = "race_entry", uniqueConstraints = @UniqueConstraint(name = "uq_race_user",
        columnNames = {"race_id", "user_id"}))
@Getter
@Setter
@NoArgsConstructor
public class RaceEntry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY,optional = false)
    @JoinColumn(name= "race_id",nullable= false)
    private Race race;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private AppUser appUser;

    @Column(name= "created_at" , nullable = false)
    private LocalDateTime createdAt;

}
