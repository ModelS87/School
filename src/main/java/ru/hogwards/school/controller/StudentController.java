package ru.hogwards.school.controller;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.hogwards.school.dto.FacultyDtoOut;
import ru.hogwards.school.dto.StudentDtoIn;
import ru.hogwards.school.dto.StudentDtoOut;
import ru.hogwards.school.service.StudentService;

import java.util.List;

@RestController
@RequestMapping("/students")
public class StudentController {
    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @PostMapping
    public StudentDtoOut create(@RequestBody StudentDtoIn studentDtoIn) {
        return studentService.create(studentDtoIn);
    }

    @PutMapping("/{id}")
    public StudentDtoOut update(@PathVariable("id") long id, @RequestBody StudentDtoIn studentDtoIn) {
        return studentService.update(id, studentDtoIn);
    }

    @GetMapping("/{id}")
    public StudentDtoOut get(@PathVariable("id") long id) {
        return studentService.get(id);
    }

    @DeleteMapping("/{id}")
    public StudentDtoOut delete(@PathVariable("id") long id) {
        return studentService.delete(id);
    }

    @GetMapping
    public List<StudentDtoOut> findAll(@RequestParam(required = false) Integer age) {
        return studentService.findAll(age);
    }

    @GetMapping("/filter")
    public List<StudentDtoOut> findByAgeBetween(@RequestParam int ageFrom, @RequestParam int ageTo) {
        return studentService.findByAgeBetween(ageFrom, ageTo);
    }

    @GetMapping("/{id}/faculty")
    public FacultyDtoOut findFaculty(@PathVariable("id") long id) {
        return studentService.findFaculty(id);
    }
    @PatchMapping(value = "/{id}/avatar", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public StudentDtoOut uploadAvatar(@PathVariable long id,
                                      @RequestPart("avatar") MultipartFile multipartFile) {
        return studentService.uploadAvatar(id, multipartFile);
    }

    @GetMapping("/count")
    public int getCountOfStudents(){
        return studentService.getCountOfStudents();
    }
    @GetMapping("/averageAge")
    public double getAverageAge(){
        return studentService.getAverageAge();
    }
    @GetMapping("/lastStudents")
    public List<StudentDtoOut>getLastStudents(
            @RequestParam(value = "count", defaultValue = "5",required = false)int count){
        return studentService.getLastStudents(Math.abs(count));
    }
    @GetMapping("/names-start-with-a")
    public List<String>getNamesStartWithA(){
        return studentService.getNamesStartWithA();
    }
    @GetMapping("/avg-age")
    public double getAvgAge(){
        return studentService.getAverageAge();
    }

}