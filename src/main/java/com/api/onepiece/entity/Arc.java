package com.api.onepiece.entity;

import java.util.List;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Arc {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    @Pattern(regexp = "A-\\d{3}")
    private String uniqueKey;

    @Column(unique = true)
    @NotBlank(message = "Dale un nombre al arco")
    private String name;

    @ManyToOne
    @JoinColumn(name = "saga_id")
    private Saga saga;

    @ManyToMany(mappedBy = "arc")
    private List<Volume> volumes;

    @OneToMany(mappedBy = "mangaArc")
    private List<MangaChapter> mangaChapters;

    @OneToMany(mappedBy = "animeArc")
    private List<AnimeChapter> animeChapters;

}
