package com.api.onepiece.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class CharacterEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    @NotBlank
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "akumaNoMi_id")
    private AkumaNoMi akumaNoMi;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "race_id")
    private Race race;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "occupation_id")
    private Occupation occupation;

    @Column
    private Long bounty;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "organizationz_id")
    private Organizationz organizationz;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "organizationzRoles_id")
    private Role organizationzRoles;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "crew_id")
    private Crew crew;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "crewRoles_id")
    private Role crewRoles;

    @Column
    private String age;
    
    @Column
    private Integer size;

    @Column
    private String birthdate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "firstApparitionManga_id")
    private MangaChapter firstApparitionManga;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "firstApparitionAnime_id")
    private AnimeChapter firstApparitionAnime;

    @Column
    private boolean status;

    @Column
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "origin_id")
    private Location origin;

}
