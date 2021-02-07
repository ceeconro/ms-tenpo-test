package org.tenpo.test.mstenpotest.multiply;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

public interface MultiplyRepository extends CrudRepository<MultiplyEntity, Integer>, JpaRepository<MultiplyEntity, Integer> {
}
