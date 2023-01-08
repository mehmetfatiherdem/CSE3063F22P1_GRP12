from iteration3.FacultyTechnicalElectiveCourse import FacultyTechnicalElectiveCourse
from iteration3.Logger import Logger
from iteration3.MandatoryCourse import MandatoryCourse
from iteration3.NonTechnicalElectiveCourse import NonTechnicalElectiveCourse
from iteration3.TechnicalElectiveCourse import TechnicalElectiveCourse

class Department:
    instance = None

    department_code = "CSE"
    department_name = "Computer Engineering Department"
    current_season = None
    mandatory_courses = []
    technical_elective_courses = []
    faculty_technical_elective_courses = []
    non_technical_elective_courses = []
    students = []
    lecturers = []
    assistants = []
    advisors = []
    initialized = False

    def __init__(self):
        self.initialized = False

    @classmethod
    def get_instance(cls):
        if cls.instance is None:
            cls.instance = Department()
        return cls.instance

    def initialize(self, current_season, courses, lecturers, assistants, advisors, students):
        if self.initialized:
            return

        if courses is None or lecturers is None or assistants is None or advisors is None or students is None:
            return

        self.current_season = current_season
        self.lecturers = lecturers
        self.assistants = assistants
        self.advisors = advisors
        self.students = students

        self.mandatory_courses = []
        self.technical_elective_courses = []
        self.faculty_technical_elective_courses = []
        self.non_technical_elective_courses = []

        for c in courses:
            if isinstance(c, MandatoryCourse):
                self.mandatory_courses.append(c)
            elif isinstance(c, TechnicalElectiveCourse):
                self.technical_elective_courses.append(c)
            elif isinstance(c, FacultyTechnicalElectiveCourse):
                self.faculty_technical_elective_courses.append(c)
            elif isinstance(c, NonTechnicalElectiveCourse):
                self.non_technical_elective_courses.append(c)

        self.initialized = True

        self.generate_weekly_schedule_for_all_courses()

        def add_new_course_section(self, course):
            theoretical_hours = course.get_theoretical_hours()
            if theoretical_hours == 0:
                return

            schedule = self.get_new_section_schedule(course, course.get_theoretical_hours())
            new_section = course.add_course_section(schedule)

            Logger.increment_indentation()
            Logger.log("=> ADDING NEW COURSE SECTION FOR THE COURSE {} WITH THE CODE {}".format(course.get_code(),
                                                                                                new_section.to_string()))
            Logger.decrement_indentation()

    def add_new_lab_section(self, course):
        applied_hours = course.get_applied_hours()
        if applied_hours == 0:
            return

        schedule = self.get_new_section_schedule(course, course.get_applied_hours())
        course.add_lab_section(schedule)

        Logger.increment_indentation()
        Logger.log(
            "=> ADDING NEW LAB SECTION FOR THE COURSE {} WITH THE CODE {}".format(course.get_code(), self.to_string()))
        Logger.decrement_indentation()
    #getters
    def get_department_code(self):
        return self.department_code

    def get_department_name(self):
        return self.department_name

    def get_current_season(self):
        return self.current_season

    def get_students(self):
        return self.students

    def get_lecturers(self):
        return self.lecturers

    def get_assistants(self):
        return self.assistants

    def get_advisors(self):
        return self.advisors

    def get_all_courses(self):
        courses = self.mandatory_courses.copy()
        courses.extend(self.technical_elective_courses)
        courses.extend(self.faculty_technical_elective_courses)
        courses.extend(self.non_technical_elective_courses)

        return courses

    def get_mandatory_courses(self):
        return self.mandatory_courses

    def get_technical_elective_courses(self):
        return self.technical_elective_courses

    def get_faculty_technical_elective_courses(self):
        return self.faculty_technical_elective_courses

    def get_non_technical_elective_courses(self):
        return self.non_technical_elective_courses

    def get_new_section_schedule(self,course, class_hours):
        semester_courses = []

        for c in self.mandatory_courses:
            if course.first_year_to_take == c.first_year_to_take and course.first_season_to_take == c.first_season_to_take:
                semester_courses.append(c)

        available_class_hours = self.get_schedule_at_position(0, 40)

        for c in semester_courses:
            for s in c.all_sections:
                available_class_hours ^= s.class_schedule

        division = self.get_division(class_hours)
        schedule = self.assign_schedule_to_section(available_class_hours, division)

        hour_counter = 0
        checker = 1

        for i in range(40):
            if (schedule & checker) > 0:
                hour_counter += 1
            checker <<= 1

        if hour_counter < class_hours:
            available_class_hours = self.get_schedule_at_position(0, 40)
            schedule = self.assign_schedule_to_section(available_class_hours, division)

        return schedule

    def generate_weekly_schedule_for_all_courses(self):
        self.generate_weekly_schedule_for_mandatory_courses()
        self.generate_weekly_schedule_for_elective_courses()

    def generate_weekly_schedule_for_mandatory_courses(self):
        for semester in range(8):
            mandatory_courses_for_semester = []

            for c in self.mandatory_courses:
                if semester == c.course_semester:
                    mandatory_courses_for_semester.append(c)

            course_schedules = self.generate_collisionless_weekly_schedule_for_courses(mandatory_courses_for_semester)

            len = len(mandatory_courses_for_semester)

            for i in range(len):
                course = mandatory_courses_for_semester[i]
                schedules = course_schedules[i]

                course.add_course_section(schedules[0])
                course.add_lab_section(schedules[1])

    def generate_weekly_schedule_for_elective_courses(self):
        for c in self.non_technical_elective_courses:
            available_class_hours = self.get_schedule_at_position(0, 48)
            division = self.get_division(c.theoretical_hours)
            schedule = self.assign_schedule_to_section(available_class_hours, division)
            c.add_course_section(schedule)

        technical_electives = list(self.technical_elective_courses)
        technical_electives.extend(self.faculty_technical_elective_courses)

        for c in technical_electives:
            available_class_hours = self.get_schedule_at_position(0, 40)
            division = self.get_division(c.theoretical_hours)
            schedule = self.assign_schedule_to_section(available_class_hours, division)
            c.add_course_section(schedule)

    def generate_collisionless_weekly_schedule_for_courses(self,courses):
        course_divisions = self.get_divisions(courses)

        failed_attempt = False
        available_class_hours = None

        course_schedules = None

        len = len(course_divisions)

        while not failed_attempt:
            failed_attempt = False
            course_schedules = []

            available_class_hours = self.get_schedule_at_position(0, 40)

            for i in range(len):
                course_divisions = course_divisions[i]
                theoretical_division = course_divisions[0]
                applied_division = course_divisions[1]

                theoretical_schedule = self.assign_schedule_to_section(available_class_hours, theoretical_division)

                if theoretical_schedule == -1:
                    failed_attempt = True
                    break

                available_class_hours ^= theoretical_schedule

                applied_schedule = self.assign_schedule_to_section(available_class_hours, applied_division)

                if applied_schedule == -1:
                    failed_attempt = True
                    break

                available_class_hours ^= applied_schedule

                course_schedules.append((theoretical_schedule, applied_schedule))

        return course_schedules

    def get_divisions(self,courses):
        divisions = []

        for c in courses:
            theoretical_hours = c.theoretical_hours
            applied_hours = c.applied_hours

            theoretical_hours_division = self.get_division(theoretical_hours)
            applied_hours_division = self.get_division(applied_hours)
            divisions.append((theoretical_hours_division, applied_hours_division))

        return divisions

    def get_division(self,no_of_hours):
        division = []

        three_consecutive_hours_cumulative_probability = 0.05
        two_consecutive_hours_cumulative_probability = 0.80

        while not self.validate_division(division, no_of_hours):
            division = []
            remainder = no_of_hours

            for i in range(3):
                random = self.math_helper.random_float()

                if random <= three_consecutive_hours_cumulative_probability and remainder >= 3:
                    division.append(3)
                    remainder -= 3
                elif random <= two_consecutive_hours_cumulative_probability and remainder >= 2:
                    division.append(2)
                    remainder -= 2
                elif remainder >= 1:
                    division.append(1)
                    remainder -= 1

        return division

    def validate_division(division, no_of_hours):
        sum = 0
        one_hour_counter = 0

        for element in division:
            sum += element

            if element == 1:
                one_hour_counter += 1

        return sum == no_of_hours and one_hour_counter <= 1

    def assign_schedule_to_section(self,available_class_hours, division):
        section_schedule = 0
        days_used = []

        for no_of_hours in division:
            available_days = self.get_available_days(available_class_hours, no_of_hours)

            for day in days_used:
                if day in available_days:
                    available_days.remove(day)

            if not available_days:
                section_schedule = -1
                break

            random_day = self.math_helper.random_integer_between(0, len(available_days))
            random_day = available_days[random_day]
            days_used.append(random_day)

            available_hours = self.get_available_positions_on_day(available_class_hours, random_day, no_of_hours)
            random_starting_hour = self.math_helper.random_integer_between(0, len(available_hours))
            random_starting_hour = available_hours[random_starting_hour]

            schedule_for_these_hours = self.get_schedule_at_position(random_starting_hour, no_of_hours)
            section_schedule |= schedule_for_these_hours

        return section_schedule

    def get_available_days(self,schedule, count):
        available_days = []

        limit = 8 - count + 1

        for i in range(5):
            position = i * 8
            requested_hours = self.get_schedule_at_position(position, count)

            for j in range(limit):
                if schedule & requested_hours == requested_hours:
                    available_days.append(i)
                    break

                requested_hours <<= 1

        return available_days

    def get_available_positions_on_day(self,schedule, day_index, count):
        position = day_index * 8
        requested_hours = self.get_schedule_at_position(position, count)

        available_positions = []

        limit = 8 - count + 1

        for i in range(limit):
            if schedule & requested_hours == requested_hours:
                available_positions.append(position + i)

            requested_hours <<= 1

        return available_positions

    def get_schedule_at_position(position, count):
        schedule = -1
        schedule >>= (64 - count)
        schedule <<= position
        return schedule