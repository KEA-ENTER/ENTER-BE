package kea.enter.enterbe.domain.ex.entity;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import kea.enter.enterbe.global.common.entity.BaseEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "ex")
public class ExEntity extends BaseEntity {
    @Column(name = "sum")
    Long sum;

    @Builder
    public ExEntity(Long sum) {
        this.sum = sum;
    }

    public static ExEntity of(Long sum) {
    return ExEntity.builder().sum(sum).build();
    }
}
