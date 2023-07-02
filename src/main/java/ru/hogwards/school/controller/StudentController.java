package ru.hogwards.school.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.hogwards.school.dto.StudentDtoIn;
import ru.hogwards.school.dto.StudentDtoOut;
import ru.hogwards.school.entity.Student;
import ru.hogwards.school.service.StudentService;

import java.util.Collection;
import java.util.Collections;

@RestController
@RequestMapping("/student")
public class StudentController {
    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping("{id}")
    public ResponseEntity<Student> getStudentInfo(@PathVariable Long id) {
        Student student = studentService.findStudent(id);
        if (student == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(student);
    }

    @PostMapping
    public StudentDtoOut create(@RequestBody StudentDtoIn studentDtoIn) {
        return studentService.create(studentDtoIn);
    }

    @PutMapping("/{id}")
    public StudentDtoOut update (@PathVariable ("id") long id, @RequestBody StudentDtoIn studentDtoIn) {
        return studentService.update(id,studentDtoIn);
    }

    @DeleteMapping("{id}")
    public StudentDtoOut delete(@PathVariable ("id") long id) {
        return studentService.delete(id);
    }
    @GetMapping("{id}")
    public StudentDtoOut get(@PathVariable("id") long id){
            return studentService.get(id);
        }
}
