import Course

class CourseRecord:
    #TODO: gereksiz getter setterları sil!.
    def __init__(self, course: Course, l_grade: LetterGrade, season: Season, grade: Grade,
                 score: float, is_passed: bool):
        self.course = course
        self.l_grade = l_grade
        self.season = season
        self.grade = grade
        self.score = score
        self.is_passed = is_passed

    @property
    def course(self) -> Course:
        return self.course

    @property
    def score(self) -> float:
        return self.score

    @property
    def is_passed(self) -> bool:
        return self.is_passed

    @property
    def season(self) -> Season:
        return self.season

    @property
    def l_grade(self) -> LetterGrade:
        return self.l_grade

    @property
    def grade(self) -> Grade:
        return self.grade

    @course.setter
    def course(self, course: Course):
        self.course = course

    @score.setter
    def score(self, score: float):
        if score < 0:
            return
        self.score = score

    @is_passed.setter
    def is_passed(self, is_passed: bool):
        self.is_passed = is_passed

    @l_grade.setter
    def l_grade(self, l_grade: LetterGrade):
        self.l_grade = l_grade

    @season.setter
    def season(self, season: Season):
        self.season = season