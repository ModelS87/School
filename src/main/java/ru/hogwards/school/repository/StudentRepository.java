package ru.hogwards.school.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
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
    @Query(value = "SELECT s. * FROM students s ORDER BY s.id DESC LIMIT :count", nativeQuery = true)
    List<Student> getLastStudents(@Param("count")int count);



}
