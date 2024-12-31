package ru.etozhealexis.districtsservice.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import ru.etozhealexis.dto.ActionType;

import java.time.LocalDateTime;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@ToString
public class Action {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    private District district;

    @ManyToOne(optional = false)
    private Participant participant;

    @Column(nullable = false)
    private String messageText;

    private Integer messageId;

    @Column(nullable = false)
    private LocalDateTime time;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ActionType actionType;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ActionStatus actionStatus;
}
