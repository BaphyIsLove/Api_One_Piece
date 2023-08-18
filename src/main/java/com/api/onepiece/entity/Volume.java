package com.api.onepiece.entity;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
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
public class Volume {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    @Pattern(regexp = "V-\\d{3}")
    private String uniqueKey;

    @Column(unique = true)
    @NotBlank(message = "Dale un nombre al tomo")
    private String name;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "volume_arc",
        joinColumns = @JoinColumn(name = "volume_id"),
        inverseJoinColumns = @JoinColumn(name ="arc_id")
    )
    private List<Arc> arc;

}
