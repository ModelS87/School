package ru.hogwards.school.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.hogwards.school.entity.Faculty;

import java.util.List;

public interface FacultyRepository extends JpaRepository<Faculty,Long> {
    List<Faculty> findAllByColour(String color);

    List<Faculty> findAllByColourContainingIgnoreCaseOrNameContainingIgnoreCase(
            String color,
            String name
    );
}
