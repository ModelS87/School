package ru.hogwards.school.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.javafaker.Faker;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.internal.verification.Times;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.amqp.RabbitProperties;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import ru.hogwards.school.dto.FacultyDtoIn;
import ru.hogwards.school.dto.FacultyDtoOut;
import ru.hogwards.school.entity.Faculty;
import ru.hogwards.school.mapper.FacultyMapper;
import ru.hogwards.school.mapper.StudentMapper;
import ru.hogwards.school.repository.FacultyRepository;
import ru.hogwards.school.repository.StudentRepository;
import ru.hogwards.school.service.FacultyService;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;

@WebMvcTest(controllers = FacultyController.class)
public class FacultyControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private FacultyRepository facultyRepository;

    @MockBean
    private StudentRepository studentRepository;

    @SpyBean
    private FacultyService facultyService;

    @SpyBean
    private FacultyMapper facultyMapper;

    @SpyBean
    private StudentMapper studentMapper;

    @Autowired
    private ObjectMapper objectMapper;
    private final Faker faker = new Faker();

        @Test
        public void createTest() throws Exception {
            FacultyDtoIn facultyDtoIn = generateDto();
            Faculty faculty = new Faculty();
            faculty.setId(1L);
            faculty.setName(facultyDtoIn.getName());
            faculty.setColour(facultyDtoIn.getColour());
            when(facultyRepository.save(any())).thenReturn(faculty);

            mockMvc.perform(
                            MockMvcRequestBuilders.put("/faculties")
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(objectMapper.writeValueAsString(facultyDtoIn))
                    )
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andExpect(result -> {
                        FacultyDtoOut facultyDtoOut = objectMapper.readValue(
                                result.getResponse().getContentAsString(),
                                FacultyDtoOut.class
                        );
                        assertThat(facultyDtoOut).isNotNull();
                        assertThat(facultyDtoOut.getId()).isEqualTo(1L);
                        assertThat(facultyDtoOut.getColour()).isEqualTo(facultyDtoIn.getColour());
                        assertThat(facultyDtoOut.getName()).isEqualTo(facultyDtoIn.getName());
                    });
            verify(facultyRepository, new Times(1)).save(any());
        }

        @Test
        public void updateTest() throws Exception {
            FacultyDtoIn facultyDtoIn = generateDto();

            Faculty oldFaculty = generate(1);

            when(facultyRepository.findById(eq(1L))).thenReturn(Optional.of(oldFaculty));

            oldFaculty.setColour(facultyDtoIn.getColour());
            oldFaculty.setName(facultyDtoIn.getName());
            when(facultyRepository.save(any())).thenReturn(oldFaculty);

            mockMvc.perform(
                            MockMvcRequestBuilders.post("/faculties/1")
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(objectMapper.writeValueAsString(facultyDtoIn))
                    ).andExpect(MockMvcResultMatchers.status().isOk())
                    .andExpect(result -> {
                        FacultyDtoOut facultyDtoOut = objectMapper.readValue(
                                result.getResponse().getContentAsString(),
                                FacultyDtoOut.class
                        );
                        assertThat(facultyDtoOut).isNotNull();
                        assertThat(facultyDtoOut.getId()).isEqualTo(1L);
                        assertThat(facultyDtoOut.getColour()).isEqualTo(facultyDtoIn.getColour());
                        assertThat(facultyDtoOut.getName()).isEqualTo(facultyDtoIn.getName());
                    });
            verify(facultyRepository, Mockito.times(1)).save(any());
            Mockito.reset(facultyRepository);

            // not found checking

            when(facultyRepository.findById(eq(2L))).thenReturn(Optional.empty());

            mockMvc.perform(
                            MockMvcRequestBuilders.put("/faculties/2")
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(objectMapper.writeValueAsString(facultyDtoIn))
                    ).andExpect(MockMvcResultMatchers.status().isNotFound())
                    .andExpect(result -> {
                        String responseString = result.getResponse().getContentAsString();
                        assertThat(responseString).isNotNull();
                        assertThat(responseString).isEqualTo("Факультет с id = 2 не найден!");
                    });
            verify(facultyRepository, never()).save(any());
        }

        @Test
        public void getTest() throws Exception {
            Faculty faculty = generate(1);

            when(facultyRepository.findById(eq(1L))).thenReturn(Optional.of(faculty));

            mockMvc.perform(
                            MockMvcRequestBuilders.get("/faculties/1")
                    ).andExpect(MockMvcResultMatchers.status().isOk())
                    .andExpect(result -> {
                        FacultyDtoOut facultyDtoOut = objectMapper.readValue(
                                result.getResponse().getContentAsString(),
                                FacultyDtoOut.class
                        );
                        assertThat(facultyDtoOut).isNotNull();
                        assertThat(facultyDtoOut.getId()).isEqualTo(1L);
                        assertThat(facultyDtoOut.getColour()).isEqualTo(faculty.getColour());
                        assertThat(facultyDtoOut.getName()).isEqualTo(faculty.getName());
                    });

            // not found checking

            when(facultyRepository.findById(eq(2L))).thenReturn(Optional.empty());

            mockMvc.perform(
                            MockMvcRequestBuilders.get("/faculties/2")
                    ).andExpect(MockMvcResultMatchers.status().isNotFound())
                    .andExpect(result -> {
                        String responseString = result.getResponse().getContentAsString();
                        assertThat(responseString).isNotNull();
                        assertThat(responseString).isEqualTo("Факультет с id = 2 не найден!");
                    });
        }

        @Test
        public void deleteTest() throws Exception {
            Faculty faculty = generate(1);

            when(facultyRepository.findById(eq(1L))).thenReturn(Optional.of(faculty));

            mockMvc.perform(
                            MockMvcRequestBuilders.delete("/faculties/1")
                    ).andExpect(MockMvcResultMatchers.status().isOk())
                    .andExpect(result -> {
                        FacultyDtoOut facultyDtoOut = objectMapper.readValue(
                                result.getResponse().getContentAsString(),
                                FacultyDtoOut.class
                        );
                        assertThat(facultyDtoOut).isNotNull();
                        assertThat(facultyDtoOut.getId()).isEqualTo(1L);
                        assertThat(facultyDtoOut.getColour()).isEqualTo(faculty.getColour());
                        assertThat(facultyDtoOut.getName()).isEqualTo(faculty.getName());
                    });
            verify(facultyRepository, times(1)).delete(any());
            Mockito.reset(facultyRepository);

            // not found checking

            when(facultyRepository.findById(eq(2L))).thenReturn(Optional.empty());

            mockMvc.perform(
                            MockMvcRequestBuilders.delete("/faculties/2")
                    ).andExpect(MockMvcResultMatchers.status().isNotFound())
                    .andExpect(result -> {
                        String responseString = result.getResponse().getContentAsString();
                        assertThat(responseString).isNotNull();
                        assertThat(responseString).isEqualTo("Факультет с id = 2 не найден!");
                    });
            verify(facultyRepository, never()).delete(any());
        }

        @Test
        public void findAllTest() throws Exception {
            List<Faculty> faculties = Stream.iterate(1, id -> id + 1)
                    .map(this::generate)
                    .limit(20)
                    .collect(Collectors.toList());
            List<FacultyDtoOut> expectedResult = faculties.stream()
                    .map(facultyMapper::toDto)
                    .collect(Collectors.toList());

            when(facultyRepository.findAll()).thenReturn(faculties);

            mockMvc.perform(
                            MockMvcRequestBuilders.get("/faculties")
                    ).andExpect(MockMvcResultMatchers.status().isOk())
                    .andExpect(result -> {
                        List<FacultyDtoOut> facultyDtoOuts = objectMapper.readValue(
                                result.getResponse().getContentAsString(),
                                new TypeReference<>() {
                                }
                        );
                        assertThat(facultyDtoOuts)
                                .isNotNull()
                                .isNotEmpty();
                        Stream.iterate(0, index -> index + 1)
                                .limit(facultyDtoOuts.size())
                                .forEach(index -> {
                                    FacultyDtoOut facultyDtoOut = facultyDtoOuts.get(index);
                                    FacultyDtoOut expected = expectedResult.get(index);
                                    assertThat(facultyDtoOut.getId()).isEqualTo(expected.getId());
                                    assertThat(facultyDtoOut.getColour()).isEqualTo(expected.getColour());
                                    assertThat(facultyDtoOut.getName()).isEqualTo(expected.getName());
                                });
                    });

            String colour = faculties.get(0).getColour();
            faculties = faculties.stream()
                    .filter(faculty -> faculty.getColour().equals(colour))
                    .collect(Collectors.toList());
            List<FacultyDtoOut> expectedResult2 = faculties.stream()
                    .filter(faculty -> faculty.getColour().equals(colour))
                    .map(facultyMapper::toDto)
                    .collect(Collectors.toList());
            when(facultyRepository.findAllByColour(eq(colour))).thenReturn(faculties);

            mockMvc.perform(
                            MockMvcRequestBuilders.get("/faculties?color={colour}", colour)
                    ).andExpect(MockMvcResultMatchers.status().isOk())
                    .andExpect(result -> {
                        List<FacultyDtoOut> facultyDtoOuts = objectMapper.readValue(
                                result.getResponse().getContentAsString(),
                                new TypeReference<>() {
                                }
                        );
                        assertThat(facultyDtoOuts)
                                .isNotNull()
                                .isNotEmpty();
                        Stream.iterate(0, index -> index + 1)
                                .limit(facultyDtoOuts.size())
                                .forEach(index -> {
                                    FacultyDtoOut facultyDtoOut = facultyDtoOuts.get(index);
                                    FacultyDtoOut expected = expectedResult2.get(index);
                                    assertThat(facultyDtoOut.getId()).isEqualTo(expected.getId());
                                    assertThat(facultyDtoOut.getColour()).isEqualTo(expected.getColour());
                                    assertThat(facultyDtoOut.getName()).isEqualTo(expected.getName());
                                });
                    });
        }

        private FacultyDtoIn generateDto() {
            FacultyDtoIn facultyDtoIn = new FacultyDtoIn();
            facultyDtoIn.setName(faker.harryPotter().house());
            facultyDtoIn.setColour(faker.color().name());
            return facultyDtoIn;
        }

        private Faculty generate(long id) {
            Faculty faculty = new Faculty();
            faculty.setId(id);
            faculty.setName(faker.harryPotter().house());
            faculty.setColour(faker.color().name());
            return faculty;
        }
    }

