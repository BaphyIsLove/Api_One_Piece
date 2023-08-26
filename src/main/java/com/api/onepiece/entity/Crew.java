package com.api.onepiece.entity;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Crew {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String name;

    @OneToMany(mappedBy = "crew")
    private List<CharacterEntity> nakamas;

    @Column
    private Long crewBounty;

    public Long calculateCrewBounty(){
        if(nakamas!=null && !nakamas.isEmpty()){
            return crewBounty = nakamas.stream()
                .mapToLong(CharacterEntity :: getBounty).sum();
        } else{
            return crewBounty = 0L;
        }
    }

    public void setCrewBounty() {
        this.crewBounty = calculateCrewBounty();
    }

}
