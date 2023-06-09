package com.asss.management.service.implementation;

import com.asss.management.dao.*;
import com.asss.management.entity.*;
import com.asss.management.entity.Enums.Exam_status;
import com.asss.management.securityConfig.JwtService;
import com.asss.management.service.dto.ExamStatusInfoDTO;
import com.asss.management.service.mapper.ExamStatusInfoMapper;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.transaction.Transactional;
import java.security.Key;
import java.util.List;

@Service
@Data
public class ExamStatusInfoService {

    private final ExamStatusInfoRepo examStatusInfoRepo;
    private final ExamStatusInfoMapper examStatusInfoMapper;
    private final EmployeeRepo employeeRepo;
    private final EventsRepo eventsRepo;
    private final StudentRepo studentRepo;
    private final SubjectsRepo subjectsRepo;
    private final ExamsRepo examsRepo;
    private final JwtService jwtService;
    private final AssignedProfesorsRepo assignedProfesorsRepo;

    private Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);

    // get all passed exams
    public List<ExamStatusInfoDTO> getPassedExams(){
        List<ExamStatusInfo> examStatusInfoList = examStatusInfoRepo.findAll();
        List<ExamStatusInfoDTO> examStatusInfoDTOList = examStatusInfoMapper.entitiesToDTOs(examStatusInfoList);
        return examStatusInfoDTOList;
    }

    // Retrieves a passed exam by id
    public ExamStatusInfoDTO getPassedExamById(Integer id) {
        ExamStatusInfo examStatusInfo = examStatusInfoRepo.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Not found"));
        ExamStatusInfoDTO examStatusInfoDTO = examStatusInfoMapper.entityToDTO(examStatusInfo);
        return examStatusInfoDTO;
    }

    public List<ExamStatusInfoDTO> getPassedExamsByProfesor(String token) {
        String userEmail = jwtService.extractUsername(token);
        List<ExamStatusInfo> examStatusInfoList = examStatusInfoRepo.passedExamsByProfesor(userEmail);
        List<ExamStatusInfoDTO> examStatusInfoDTOList = examStatusInfoMapper.entitiesToDTOs(examStatusInfoList);
        return examStatusInfoDTOList;
    }

    public List<ExamStatusInfoDTO> getPassedExamsByStudent(String token) {
        String userEmail = jwtService.extractUsername(token);
        List<ExamStatusInfo> examStatusInfoList = examStatusInfoRepo.passedExamsByStudent(userEmail);
        List<ExamStatusInfoDTO> examStatusInfoDTOList = examStatusInfoMapper.entitiesToDTOs(examStatusInfoList);
        return examStatusInfoDTOList;
    }

    public List<ExamStatusInfoDTO> getUnpassedExamsByStudent(String token) {
        String userEmail = jwtService.extractUsername(token);
        List<ExamStatusInfo> examStatusInfoList = examStatusInfoRepo.unpassedExamsByStudent(userEmail);
        List<ExamStatusInfoDTO> examStatusInfoDTOList = examStatusInfoMapper.entitiesToDTOs(examStatusInfoList);
        return examStatusInfoDTOList;
    }

    @Transactional
    public void updateExamInfo(String index,
                               Integer subjectID,
                               Integer colloquiumOne,
                               Integer colloquiumTwo,
                               Integer colloquiumThree
                               ){
        ExamStatusInfo examStatusInfo = examStatusInfoRepo.findExamInfoForSubject(subjectID, index);

        if(examStatusInfo == null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Pogresno ste uneli podatke");
        }
        if(examStatusInfo.getStatus() == Exam_status.PASSED){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Ispit je vec oznacen kao polozen");
        }

        if(colloquiumOne < 0 || colloquiumOne > 50){
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Maksimum poena sa kolokvijuma je 50");
        }
        if(colloquiumTwo < 0 || colloquiumTwo > 50){
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Maksimum poena sa kolokvijuma je 50");
        }
        if(colloquiumThree < 0 || colloquiumThree > 50){
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Maksimum poena sa kolokvijuma je 50");
        }
        if(colloquiumOne + colloquiumTwo + colloquiumThree > 50){
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Maksimum poena sa kolokvijuma je 50");
        }

        examStatusInfo.setColloquiumOne(colloquiumOne);
        examStatusInfo.setColloquiumTwo(colloquiumTwo);
        examStatusInfo.setColloquiumThree(colloquiumThree);
    }

    @Transactional
    public void updateExamAsPassed(String index,
                                 Integer subjectID,
                                 String token,
                                 Integer eventID,
                                 Integer examPoints){
        int grade = 0;
        ExamStatusInfo examStatusInfo = examStatusInfoRepo.findExamInfoForSubject(subjectID, index);
        if(examStatusInfo == null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Pogresno ste uneli podatke");
        }
        if(examStatusInfo.getStatus() == Exam_status.PASSED){
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Student je vec polozio taj predmet");
        }

        String userEmail = jwtService.extractUsername(token);

        Employee employee = employeeRepo.findByEmail(userEmail);

        Events events = eventsRepo.findById(eventID).orElseThrow(
                ()-> new ResponseStatusException(HttpStatus.NOT_FOUND, "Pogresno ste uneli podatke"));
        Subjects subject = subjectsRepo.findById(subjectID).orElseThrow(
                ()-> new ResponseStatusException(HttpStatus.NOT_FOUND, "Pogresno ste uneli podatke"));

        if(examPoints < 26 || examPoints > 50){
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Maksimum poena na ispitu je 50");
        }

        if(subject.getCourse() != examStatusInfo.getStudent().getCourseOfStudies()){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Pogresan unos");
        }

        Exams reportedExam = examsRepo.findIfStudentRegistratedTheExam(subjectID, eventID, index);

        if(reportedExam == null){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Student nije prijavio ispit u tom roku");
        }

        int totalColloquiumPoints = examStatusInfo.getColloquiumOne() + examStatusInfo.getColloquiumTwo() + examStatusInfo.getColloquiumThree();
        if(totalColloquiumPoints < 26){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Student nema predispitne obaveze za taj ispit");
        }
        int totalPoints = totalColloquiumPoints + examPoints;
        if(totalPoints > 50 && totalPoints <61){
            grade = 6;
        } else if(totalPoints > 60 && totalPoints < 71){
            grade = 7;
        } else if(totalPoints > 70 && totalPoints < 81){
            grade = 8;
        } else if(totalPoints > 80 && totalPoints < 91){
            grade = 9;
        } else if(totalPoints > 90 && totalPoints <= 100){
            grade = 10;
        }

        examStatusInfo.setGrade(grade);
        examStatusInfo.setEvent(events);
        examStatusInfo.setProfesor(employee);
        examStatusInfo.setExamPoints(examPoints);
        examStatusInfo.setStatus(Exam_status.PASSED);
    }
}
