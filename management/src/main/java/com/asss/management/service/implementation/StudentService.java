package com.asss.management.service.implementation;

import com.asss.management.dao.StudentRepo;
import com.asss.management.entity.Student;
import com.asss.management.service.dto.StudentDTO;
import com.asss.management.service.mapper.StudentMapper;
import lombok.Data;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Data
public class StudentService {

    private final StudentRepo studentRepo;
    private final StudentMapper studentMapper;

    // Retrieves all students
    public List<StudentDTO> getStudents() {
        List<Student> studentList = studentRepo.findAll();
        List<StudentDTO> studentDTOList = studentMapper.entitiesToDTOs(studentList);
        return studentDTOList;
    }
}
