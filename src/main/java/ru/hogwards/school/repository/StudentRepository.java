package ru.hogwards.school.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.hogwards.school.dto.StudentDtoOut;
import ru.hogwards.school.entity.Student;

import java.awt.print.Pageable;
import java.util.List;

public interface StudentRepository extends JpaRepository<Student, Long> {
    List<Student> findAllByAge(int age);

    List<Student> findAllByAgeBetween(int ageFrom, int ageTo);

    List<Student> findAllByFaculty_Id(long facultyId);
    @Query("SELECT count(s) FROM Student s")
    int getCountOfStudents();

    @Query("SELECT avg (s.age) FROM Student s")
    double getAverageAge();
    @Query("SELECT s FROM Student s ORDER BY s.id DESC")
    List<Student> getLastStudents(Pageable pageable);


}
