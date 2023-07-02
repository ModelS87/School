package ru.hogwards.school.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.hogwards.school.entity.Faculty;

public interface FacultyRepository extends JpaRepository<Faculty,Long> {
}
