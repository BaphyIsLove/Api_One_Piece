package com.api.onepiece.entity;

import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Saga {
    
    @Id
    private String uniqueKey;

    @Column(unique = true)
    @NotBlank(message = "Dale un nombre a la saga")
    private String name;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "saga", cascade = CascadeType.ALL)
    private Set<Arc> arcs;

}
