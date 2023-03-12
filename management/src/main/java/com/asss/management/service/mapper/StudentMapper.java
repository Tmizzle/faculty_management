package com.asss.management.service.mapper;

import com.asss.management.entity.Student;
import com.asss.management.service.dto.StudentDTO;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface StudentMapper {

    StudentDTO entityToDTO (Student student);

    Student DTOToEntity (StudentDTO studentDTO);

    List<StudentDTO> entitiesToDTOs(List<Student> studentList);

    List<Student> DTOsToEntities(List<StudentDTO> studentDTOList);
}
