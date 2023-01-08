import json
import os
from typing import List

class JsonParser:
    def __init__(self):
        self.advisors_file = "iteration2/resources/Advisors.json"
        self.assistants_file = "iteration2/resources/Assistants.json"
        self.lecturers_file = "iteration2/resources/Lecturers.json"
        self.courses_file = "iteration2/resources/Courses.json"
        self.semester_file = "iteration2/resources/Semester.json"
        self.students_dir = "iteration2/resources/students/"
        self.student_config_file = "iteration2/resources/StudentConfig.json"
        

    def parse_semester(self) -> Season:
        semester = self.read_json_file(self.semester_file)
        return Season(semester)

    def parse_advisors(self) -> List[Advisor]:
        arr = self.read_json_file(self.advisors_file)

        # parse each advisor
        advisor_list = []
        for adv in arr:
            names = self.parse_human(adv)
            advisor_list.append(Advisor(names[0], names[1], names[2]))

        return advisor_list

    def parse_assistants(self) -> List[Assistant]:
        arr = self.read_json_file(self.assistants_file)

        # parse each assistant
        assistant_list = []
        for adv in arr:
            names = self.parse_human(adv)
            assistant_list.append(Assistant(names[0], names[1], names[2]))

        return assistant_list

    def parse_lecturers(self) -> List[Lecturer]:
        arr = self.read_json_file(self.lecturers_file)

        # parse each lecturer
        lecturer_list = []
        for adv in arr:
            names = self.parse_human(adv)
            lecturer_list.append(Lecturer(names[0], names[1], names[2]))

        return lecturer_list
    
    def parse_courses(self, lecturers: List[Lecturer], assistants: List[Assistant]) -> List[Course]:
        arr = self.read_json_file(self.courses_file)

        prerequisite_codes = []
        course_list = []

        for c in arr:
            course_json = c
            course_code = course_json["CourseCode"]
            course_name = course_json["CourseName"]
            course_type = course_json["CourseType"]

            credits = course_json["Credits"]
            theoretical_hours = course_json["TheoreticalHours"]
            applied_hours = course_json["AppliedHours"]

            codes = course_json["PrerequisiteCodes"]
            prerequisite_codes.append(codes)

            first_grade_to_take = Grade(course_json["FirstGradeToTake"])
            first_season_to_take = Season(course_json["FirstSeasonToTake"])
            lecturers_json = course_json["Lecturers"]
            assistants_json = course_json["Assistants"]

            lecturers_of_this_course = self.find_lecturers_from(lecturers_json, lecturers)
            assistants_of_this_course = self.find_assistants_from(assistants_json, assistants)

            # add to list
            course = None
            if course_type == "Mandatory":
                course = MandatoryCourse(
                    course_code,
                    course_name,
                    credits,
                    theoretical_hours,
                    applied_hours,
                    first_grade_to_take,
                    first_season_to_take,
                    lecturers_of_this_course,
                    assistants_of_this_course,
                )
            elif course_type == "Faculty Technical Elective":
                course = FacultyTechnicalElectiveCourse(
                    course_code,
                    course_name,
                    credits,
                    theoretical_hours,
                    applied_hours,
                    first_grade_to_take,
                    first_season_to_take,
                    lecturers_of_this_course,
                    assistants_of_this_course,
                )
            elif course_type == "Technical Elective":
                course = TechnicalElectiveCourse(
                    course_code,
                    course_name,
                    credits,
                    theoretical_hours,
                    applied_hours,
                    first_grade_to_take,
                    first_season_to_take,
                    lecturers_of_this_course,
                    assistants_of_this_course,
                )
            elif course_type == "Non-Technical Elective | University Elective":
                course = NonTechnicalElectiveCourse(
                    course_code,
                    course_name,
                    credits,
                    theoretical_hours,
                    applied_hours,
                    first_grade_to_take,
                    first_season_to_take,
                    lecturers_of_this_course,
                    assistants_of_this_course,
                )

            course_list.append(course)

        self.assign_prerequisites_to_courses(course_list, prerequisite_codes)
        return course_list

    def parse_students(self, advisors: List[Advisor], courses: List[Course]) -> List[Student]:
        self.parse_student_config()

        student_list = []

        students_folder = os.path.join(self.students_dir)
        student_files = os.listdir(students_folder)

        for f in student_files:
            file_path = os.path.join(students_folder, f)
            if not os.path.isfile(file_path):
                continue

            student_json = self.read_json_file(file_path)

            student_id = student_json["ID"]
            grade = Grade(student_json["Grade"])
            advisor_name = student_json["AdvisorName"]
            advisor = self.find_advisor_by_name(advisors, advisor_name)

            names = self.parse_human(student_json)
            course_records_json = student_json["CourseRecords"]
            course_records = self.parse_course_records(course_records_json, courses)

            student_list.append(
                Student(names[0], names[1], names[2], student_id, grade, advisor, course_records)
            )

        return student_list
    
    def serialize_students(self, students: List[Student]):
        for s in students:
            records = s.get_transcript().get_taken_course_records()

            records_json = []

            for r in records:
                record = {}

                grade = r.get_score()
                grade = grade if grade is not None else -1

                record["CourseCode"] = r.get_course().get_code()
                record["LetterGrade"] = r.getl_grade().name
                record["Grade"] = grade
                record["Season"] = r.get_season().name
                record["RecordYear"] = r.get_grade().name
                record["Passed"] = r.get_is_passed()

                records_json.append(record)

            student_json = {}
            m_name = s.get_middle_name() if s.get_middle_name() is not None else "null"

            student_json["ID"] = s.get_student_id()
            student_json["Grade"] = s.get_grade().name
            student_json["AdvisorName"] = s.get_advisor().get_full_name()
            student_json["CourseRecords"] = records_json
            student_json["FirstName"] = s.get_first_name()
            student_json["MiddleName"] = m_name
            student_json["LastName"] = s.get_last_name()

            json_string = json.dumps(student_json)
            self.write_to_file(json_string, f"{self.students_dir}{s.get_student_id()}.json")

    def parse_student_config(self):
        config_json = self.read_json_file(self.student_config_file)

        Student.max_retake_chance = config_json["MaxRetakeChance"]
        Student.min_retake_chance = config_json["MinRetakeChance"]
        Student.max_fail_chance = config_json["MaxFailChance"]
        Student.min_fail_chance = config_json["MinFailChance"]
        Student.max_not_take_chance = config_json["MaxNotTakeChance"]
        Student.min_not_take_chance = config_json["MinNotTakeChance"]

    def parse_human(self, obj: Dict) -> List[str]:
        names = [obj["FirstName"], obj["MiddleName"], obj["LastName"]]
        return names if names[1] != "null" else names[:1] + names[2:]

    def parse_course_records(self, records_json: List, courses: List[Course]) -> List[CourseRecord]:
        course_records = []

        for r in records_json:
            course_code = r["CourseCode"]
            course = self.find_course_with_code(courses, course_code)
            letter_grade = LetterGrade[r["LetterGrade"]]
            grade = r["Grade"]
            season = Season[r["Season"]]
            record_year = Grade[r["RecordYear"]]
            passed = r["Passed"]

            grade = None if grade < 0 else grade

            course_records.append(
                CourseRecord(course, letter_grade, season, record_year, grade, passed)
            )

        return course_records
    
    def parse_names(self, names_json: List) -> List[str]:
        names = [n for n in names_json]
        return names

    def find_course_with_code(self, courses: List[Course], course_code: str) -> Course:
        for course in courses:
            if course.code == course_code:
                return course

    def find_assistants_from(self, assistants_json: List, assistants: List[Assistant]) -> List[Assistant]:
        assistants_found = []
        assistant_names = self.parse_names(assistants_json)

        for ast_name in assistant_names:
            for ast in assistants:
                if ast.full_name == ast_name:
                    assistants_found.append(ast)
                    break

        return assistants_found
    
    def find_lecturers_from(self, lecturers_json: List, lecturers: List[Lecturer]) -> List[Lecturer]:
        lecturers_found = []
        lecturer_names = self.parse_names(lecturers_json)

        for lec_name in lecturer_names:
            for lec in lecturers:
                if lec.full_name == lec_name:
                    lecturers_found.append(lec)
                    break

        return lecturers_found

    def find_advisor_by_name(self, advisors: List[Advisor], advisor_name: str) -> Optional[Advisor]:
        for adv in advisors:
            if adv.full_name == advisor_name:
                return adv

        return None

    def assign_prerequisites_to_courses(self, courses: List[Course], prerequisite_codes: List[List]) -> None:
        for i, course in enumerate(courses):
            codes = prerequisite_codes[i]

            for code in codes:
                prerequisite = self.find_course_with_code(courses, code)
                course.add_prerequisite(prerequisite)
                
    def read_json_file(file_name: str) -> Any:
        with open(file_name, "r") as f:
            return json.load(f)
        
    def write_to_file(str, file_name):
        try:
            with open(file_name, 'w') as writer:
                writer.write(str)
        except Exception as e:
            print(e)