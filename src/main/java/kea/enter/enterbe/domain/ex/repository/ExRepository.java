package kea.enter.enterbe.domain.ex.repository;

import kea.enter.enterbe.domain.ex.entity.ExEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExRepository extends JpaRepository<ExEntity,Long> {
}
