package com.pullanner.domain.plan;

import com.pullanner.domain.user.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "plan")
@Entity
public class Plan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "plan_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User writer;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PlanType planType;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, length = 300)
    private String note;

    @Column(nullable = false)
    private LocalDateTime planDateTime;

    private String mainColor; // TODO : #FFFFFF 형식 검증 필요

    @Builder
    public Plan(User writer, PlanType planType, String name, String note,
        LocalDateTime planDateTime, String mainColor) {
        this.writer = writer;
        this.planType = planType;
        this.name = name;
        this.note = note;
        this.planDateTime = planDateTime;
        this.mainColor = mainColor;
    }


}
