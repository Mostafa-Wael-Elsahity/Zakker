package com.example.elearningplatform.payment.copoun;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CopounRepository extends JpaRepository<Copoun, Integer> {

    List<Copoun> findByCourseId(Integer courseId);

}
