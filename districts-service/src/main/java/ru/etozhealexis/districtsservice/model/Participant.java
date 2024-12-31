package ru.etozhealexis.districtsservice.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.etozhealexis.dto.Colorized;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Participant implements Colorized {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Long userId;

    private String username;

    @Column(unique = true)
    private String color;

    @ManyToOne
    @Column(unique = true)
    private Team team;

    public boolean hasTeam() {
        return team != null;
    }
}
