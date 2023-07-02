package ru.hogwards.school.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.hogwards.school.entity.Student;

public interface StudentRepository extends JpaRepository<Student, Long> {
}
