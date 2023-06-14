package com.speakfreely.speakfreely.controllerTests;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.speakfreely.speakfreely.controller.CourseController;
import com.speakfreely.speakfreely.model.Course;
import com.speakfreely.speakfreely.repository.CourseRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Optional;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(MockitoJUnitRunner.class)
public class CourseControllerTests {

    @Mock
    private CourseRepository courseRepository;

    @InjectMocks
    private CourseController courseController;

    private MockMvc mockMvc;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(courseController).build();
    }

    @Test
    public void testGetCourseById() throws Exception {
        Course course = new Course();
        //course.setId(1L);
        course.setName("English");
        course.setDescription("English Language Course");

        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));

        mockMvc.perform(get("/courses/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("English")))
                .andExpect(jsonPath("$.description", is("English Language Course")));
    }

    @Test
    public void testCreateCourse() throws Exception {
        Course course = new Course();
        //course.setId(1L);
        course.setName("Math");
        course.setDescription("Mathematics Course");

        ObjectMapper objectMapper = new ObjectMapper();
        String courseJson = objectMapper.writeValueAsString(course);

        mockMvc.perform(post("/courses")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(courseJson))
                .andExpect(status().isOk());

        verify(courseRepository, times(1)).save(any(Course.class));
    }

    @Test
    public void testDeleteCourse() throws Exception {
        mockMvc.perform(delete("/courses/1"))
                .andExpect(status().isOk());

        verify(courseRepository, times(1)).deleteById(1L);
    }

}
