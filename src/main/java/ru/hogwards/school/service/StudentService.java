package ru.hogwards.school.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import ru.hogwards.school.dto.FacultyDtoOut;
import ru.hogwards.school.dto.StudentDtoIn;
import ru.hogwards.school.dto.StudentDtoOut;
import ru.hogwards.school.entity.Avatar;
import ru.hogwards.school.entity.Student;
import ru.hogwards.school.exception.FacultyNotFoundException;
import ru.hogwards.school.exception.StudentNotFoundException;
import ru.hogwards.school.mapper.FacultyMapper;
import ru.hogwards.school.mapper.StudentMapper;
import ru.hogwards.school.repository.FacultyRepository;
import ru.hogwards.school.repository.StudentRepository;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class StudentService {
    private static final Logger LOG = LoggerFactory.getLogger(StudentService.class);

    private final StudentRepository studentRepository;
    private final FacultyRepository facultyRepository;
    private final StudentMapper studentMapper;
    private final FacultyMapper facultyMapper;
    private final AvatarService avatarService;

    public StudentService(StudentRepository studentRepository,
                          FacultyRepository facultyRepository,
                          StudentMapper studentMapper,
                          FacultyMapper facultyMapper,
                          AvatarService avatarService){
        this.studentRepository = studentRepository;
        this.facultyRepository = facultyRepository;
        this.studentMapper = studentMapper;
        this.facultyMapper = facultyMapper;
        this.avatarService = avatarService;
    }

    public StudentDtoOut create(StudentDtoIn studentDtoIn) {
        LOG.info("Was invoked method create with parameter");
        return studentMapper.toDto(
                studentRepository.save(
                        studentMapper.toEntity(studentDtoIn)
                )
        );
    }

    public StudentDtoOut update(long id, StudentDtoIn studentDtoIn) {
        LOG.info("Was invoked method update with parameter id = {}", id);
        return studentRepository.findById(id)
                .map(oldStudent -> {
                    oldStudent.setAge(studentDtoIn.getAge());
                    oldStudent.setName(studentDtoIn.getName());
                    Optional.ofNullable(studentDtoIn.getFacultyId()).ifPresent(facultyId ->
                            oldStudent.setFaculty(
                                    facultyRepository.findById(facultyId)
                                            .orElseThrow(() -> new FacultyNotFoundException(facultyId))
                            )
                    );
                    return studentMapper.toDto(studentRepository.save(oldStudent));
                })
                .orElseThrow(() -> new StudentNotFoundException(id));
    }

    public StudentDtoOut delete(long id) {
        LOG.info("Was invoked method delete with parameter");
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new StudentNotFoundException(id));
        studentRepository.delete(student);
        return studentMapper.toDto(student);
    }

    public StudentDtoOut get(long id) {
        LOG.info("Was invoked method get with parameter id = {}", id);
        return studentRepository.findById(id)
                .map(studentMapper::toDto)
                .orElseThrow(() -> new StudentNotFoundException(id));
    }

    public List<StudentDtoOut> findAll(@Nullable Integer age) {
        LOG.info("Was invoked method findall with parameter");
        return Optional.ofNullable(age)
                .map(studentRepository::findAllByAge)
                .orElseGet(studentRepository::findAll).stream()
                .map(studentMapper::toDto)
                .collect(Collectors.toList());
    }

    public List<StudentDtoOut> findByAgeBetween(int ageFrom, int ageTo) {
        LOG.info("Was invoked method findByAgeBetween with parameter");
        return studentRepository.findAllByAgeBetween(ageFrom, ageTo).stream()
                .map(studentMapper::toDto)
                .collect(Collectors.toList());
    }

    public FacultyDtoOut findFaculty(long id) {
        LOG.info("Was invoked method findFaculty with parameter");
        return studentRepository.findById(id)
                .map(Student::getFaculty)
                .map(facultyMapper::toDto)
                .orElseThrow(() -> new StudentNotFoundException(id));
    }
    public StudentDtoOut uploadAvatar(long id, MultipartFile multipartFile) {
        LOG.info("Was invoked method uploadAvatar with parameter");
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new StudentNotFoundException(id));
        Avatar avatar = avatarService.create(student, multipartFile);
        StudentDtoOut studentDtoOut = studentMapper.toDto(student);
        return studentDtoOut;
    }

    public int getCountOfStudents() {
        LOG.info("Was invoked method gerCountOfStudents with parameter");
        return studentRepository.getCountOfStudents();
    }

    public double getAverageAge() {
        LOG.info("Was invoked method getAverageAge with parameter");
        return studentRepository.getAverageAge();
    }
    @Transactional(readOnly = true)
    public List<StudentDtoOut> getLastStudents(int count) {
        LOG.info("Was invoked method getLastStudents with parameter");
        return studentRepository.getLastStudents(count).stream()
                .map(studentMapper::toDto)
                .collect(Collectors.toList());
    }

    public List<String> getNamesStartWithA() {
        return studentRepository.findAll().stream()
                .map(student -> student.getName().toUpperCase())
                .filter(name->name.startsWith("A"))
                .sorted()
                .collect(Collectors.toList());
    }
    public double getAvgAge(){
        List<Student> students = studentRepository.findAll();
        int sumAge = students.stream()
                .map(Student::getAge)
                .reduce(Integer::sum)
                .get();
        return (double) sumAge/students.size();
    }
    public void taskThread(){
        List<Student> students = studentRepository.findAll();
        printStudent(students.get(0));
        printStudent(students.get(1));

        new Thread(()-> {
            printStudent(students.get(2));
            printStudent(students.get(3));
        }).start();

        new Thread(()-> {
            printStudent(students.get(4));
            printStudent(students.get(5));
        }).start();
        }
    public void taskThreadSync(){
        List<Student> students = studentRepository.findAll();
        LOG.info(students.toString());

        printStudentSync(students.get(0));
        printStudentSync(students.get(1));

        new Thread(()-> {
            printStudentSync(students.get(2));
            printStudentSync(students.get(3));
        }).start();

        new Thread(()-> {
            printStudentSync(students.get(4));
            printStudentSync(students.get(5));
        }).start();
    }
    private void printStudent(Student student){
        try {
            Thread.sleep(1000);
            LOG.info(student.toString());
        }catch (InterruptedException e){
            e.printStackTrace();
        }
    }
    private synchronized void printStudentSync(Student student){
        printStudent(student);
    }
}
