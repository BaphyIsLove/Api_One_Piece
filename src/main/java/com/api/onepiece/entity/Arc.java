package com.api.onepiece.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.NotBlank;
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
    private String uniqueKey;

    @Column(unique = true)
    @NotBlank(message = "Dale un nombre al arco")
    private String name;

    @ManyToOne (fetch = FetchType.LAZY)
    @JoinColumn(name = "saga_uniqueKey")
    private Saga saga;

    @Transient
    private String sagaKey;
}
