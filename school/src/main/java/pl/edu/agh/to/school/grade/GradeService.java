package pl.edu.agh.to.school.grade;

import org.springframework.stereotype.Service;

@Service
public class GradeService {
    private final GradeRepository gradeRepository;

    public GradeService(GradeRepository gradeRepository) {
        this.gradeRepository = gradeRepository;
    }

    public Grade saveGrade(Grade grade){
        return gradeRepository.save(grade);
    }


}
