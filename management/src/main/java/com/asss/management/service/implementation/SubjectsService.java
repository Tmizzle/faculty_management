package com.asss.management.service.implementation;

import com.asss.management.dao.SubjectsRepo;
import com.asss.management.entity.Enums.Semester;
import com.asss.management.entity.Enums.Year_of_studies;
import com.asss.management.entity.Subjects;
import com.asss.management.service.dto.SubjectsDTO;
import com.asss.management.service.mapper.SubjectsMapper;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Objects;

@Service
@Data
public class SubjectsService {

    private final SubjectsRepo subjectsRepo;
    private final SubjectsMapper subjectsMapper;

    // Retrieves all subjects
    public List<SubjectsDTO> getSubjects() {
        List<Subjects> subjectsList = subjectsRepo.findAll();
        List<SubjectsDTO> subjectsDTOList = subjectsMapper.entitiesToDTOs(subjectsList);
        return subjectsDTOList;
    }

    public List<SubjectsDTO> getSubjectsByYear(String year){
        List<SubjectsDTO> subjectsDTOList = subjectsRepo.findByYear(year);
        return subjectsDTOList;
    }

    public List<SubjectsDTO> getSubjectsBySemester(String semester){
        List<SubjectsDTO> subjectsDTOList = subjectsRepo.findBySemester(semester);
        return subjectsDTOList;
    }

    public void addNewSubject(Subjects subjects) {
        Subjects subjectsCheck = subjectsRepo.findByName(subjects.getName());
        if(subjectsCheck != null){
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Subject with that name already exists");
        }
        if(subjects.getYear() == Year_of_studies.FIRST_YEAR){
            if(subjects.getSemester() != Semester.FIRST && subjects.getSemester() != Semester.SECOND){
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Year and semester not matching");
            }
        }
        if(subjects.getYear() == Year_of_studies.SECOND_YEAR){
            if(subjects.getSemester() != Semester.THIRD && subjects.getSemester() != Semester.FOURTH){
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Year and semester not matching");
            }
        }
        if(subjects.getYear() == Year_of_studies.THIRD_YEAR){
            if(subjects.getSemester() != Semester.FIFTH && subjects.getSemester() != Semester.SIXTH){
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Year and semester not matching");
            }
        }
        subjectsRepo.save(subjects);
    }

    @Transactional
    public void updateSubject(Integer id,
                              Year_of_studies year,
                              Semester semester) {

        Subjects subjects = subjectsRepo.findById(id).orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND, "Specified subject has not been found."));

        if (year != null && !Objects.equals(subjects.getYear(), year) && semester == null) {
            if(year == Year_of_studies.FIRST_YEAR){
                if(subjects.getSemester() != Semester.FIRST && subjects.getSemester() != Semester.SECOND){
                    throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Year and semester not matching 1");
                }
            }
            if(year == Year_of_studies.SECOND_YEAR){
                if(subjects.getSemester() != Semester.THIRD && subjects.getSemester() != Semester.FOURTH){
                    throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Year and semester not matching 2");
                }
            }
            if(year == Year_of_studies.THIRD_YEAR){
                if(subjects.getSemester() != Semester.FIFTH && subjects.getSemester() != Semester.SIXTH){
                    throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Year and semester not matching 3");
                }
            }
            subjects.setYear(year);
        }
        if (semester != null && !Objects.equals(subjects.getSemester(), semester) && year == null) {
            if(semester != Semester.FIRST && semester != Semester.SECOND){
                if(subjects.getYear() == Year_of_studies.FIRST_YEAR){
                    throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Semester and year not matching 1");
                }
            }
            if(semester != Semester.THIRD && semester != Semester.FOURTH){
                if(subjects.getYear() == Year_of_studies.SECOND_YEAR){
                    throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Semester and year not matching 1");
                }
            }
            if(semester != Semester.FIFTH && semester != Semester.SIXTH){
                if(subjects.getYear() == Year_of_studies.THIRD_YEAR){
                    throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Semester and year not matching 1");
                }
            }
            subjects.setSemester(semester);
        }
        if(semester != null && year != null){
                if(year == Year_of_studies.FIRST_YEAR){
                    if(semester != Semester.FIRST && semester != Semester.SECOND){
                        throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Fuck");
                    }
                }
                if(year == Year_of_studies.SECOND_YEAR){
                    if(semester != Semester.THIRD && semester != Semester.FOURTH){
                        throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Fuck 2");
                    }
                }
                if(year == Year_of_studies.THIRD_YEAR){
                    if(semester != Semester.FIFTH && semester != Semester.SIXTH){
                        throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Fuck 3");
                    }
                }
                subjects.setYear(year);
                subjects.setSemester(semester);
        }
    }
}
