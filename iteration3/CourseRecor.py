class CourseRecord:
    def __init__(self, course, lGrade, season, grade, score, isPassed):
        self.course = course
        self.lGrade = lGrade
        self.season = season
        self.grade = grade
        self.score = score
        self.isPassed = isPassed

    @property
    def course(self):
        return self._course

    @course.setter
    def course(self, course):
        self._course = course

    @property
    def score(self):
        return self._score

    @score.setter
    def score(self, score):
        if score < 0:
            return
        self._score = score

    @property
    def isPassed(self):
        return self._isPassed

    @isPassed.setter
    def isPassed(self, isPassed):
        self._isPassed = isPassed

    @property
    def season(self):
        return self._season

    @season.setter
    def season(self, season):
        self._season = season

    @property
    def lGrade(self):
        return self._lGrade

    @lGrade.setter
    def lGrade(self, lGrade):
        self._lGrade = lGrade

    @property
    def grade(self):
        return self._grade

    @grade.setter
    def grade(self, grade):
        self._grade = grade
