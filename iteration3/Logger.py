class Logger:
    logTxt = "iteration3/output/log.txt"
    unitIndentation = "    "

    enabled = True
    indentation = 0
    ignore_indentation = False

    cell_width = 14

    @staticmethod
    def enable():
        Logger.enabled = True
    
    @staticmethod
    def disable():
        Logger.enabled = False
    
    @staticmethod
    def clear_log_file():
        Logger.close_log_file(Logger.open_log_file(False))
    
    @staticmethod
    def decrement_indentation():
        Logger.set_indentation(Logger.indentation - 1)
    
    @staticmethod
    def increment_indentation():
        Logger.indentation += 1
    
    @staticmethod
    def set_indentation(indent: int):
        indent = 0 if indent < 0 else indent
        Logger.indentation = indent
    
    @staticmethod
    def get_indentation() -> int:
        return Logger.indentation
    
    @staticmethod
    def get_indentation_string() -> str:
        indentation_builder = ""

        for i in range(Logger.indentation):
            indentation_builder += Logger.unit_indentation

        return indentation_builder
    
    @staticmethod
    def new_line():
        Logger.ignore_indentation = True
        Logger.log("")
        Logger.ignore_indentation = False
    
    @staticmethod
    def new_line(line_type: HorizontalLineType):
        Logger.ignore_indentation = True

        if line_type == HorizontalLineType.Dash:
            Logger.log("---------------------------------------------------------------------------------------------------" +
                        "-----------------------------------------------------------------------------------------------" +
                        "-----------------------------------------------------------------------------------------------" +
                        "-----------------------------------------------------------------------------------------------" +
                        "-----------------------------------------------------------------------------------------------" +
                        "-------------------")
        elif line_type == HorizontalLineType.Dot:
            Logger.log("..................................................................................................." +
                        "..............................................................................................." +
                        "..............................................................................................." +
                        "..............................................................................................." +
                        "..............................................................................................." +
                        "...................")
        elif line_type == HorizontalLineType.Star:
            Logger.log("***************************************************************************************************" +
                        "***********************************************************************************************" +
                        "***********************************************************************************************" +
                        "***********************************************************************************************" +
                        "***********************************************************************************************" +
                        "*******************")
        elif line_type == HorizontalLineType.EqualsSign:
            Logger.log("===================================================================================================" +
                        "===============================================================================================" +
                        "===============================================================================================" +
                        "===============================================================================================" +
                        "===============================================================================================" +
                        "===================")
        
        Logger.ignore_indentation = False

    @staticmethod
    def log(message: str):
        if not Logger.enabled:
            return

        log = "" if Logger.ignore_indentation else Logger.get_indentation_string()
        log += message + "\n"

        writer = Logger.open_log_file(True)

        writer.append(log)
        print(log)

        Logger.close_log_file(writer)

    @staticmethod
    def log_schedule(schedule: int):
        schedule_string = bin(schedule)[2:]
        schedule_string = "0" * (48 - len(schedule_string)) + schedule_string
        schedule_string = schedule_string[:8] + "_" + schedule_string[8:17] + "_" + schedule_string[17:26] + "_" + schedule_string[26:35] + "_" + schedule_string[35:44] + "_" + schedule_string[44:]
        Logger.log(schedule_string)

    @staticmethod
    def log_student_schedule(enrolled_sections: List[Section], horizontal_line_type: HorizontalLineType, vertical_line: str):
        schedule = Section.combine_schedules(enrolled_sections)
        schedule_builder = ""
        indentation_string = Logger.get_indentation_string()
        horizontal_line = ""

        if vertical_line == " " or vertical_line == "\0" or vertical_line == "\t" or vertical_line == "\n":
            vertical_line = "|"

        if horizontal_line_type == HorizontalLineType.Dash:
            horizontal_line = f"\n{indentation_string}---------------------------------------------------------------" + "----------------------------------------------------------\n"
        elif horizontal_line_type == HorizontalLineType.Star:
            horizontal_line = f"\n{indentation_string}***************************************************************" + "**********************************************************\n"
        elif horizontal_line_type == HorizontalLineType.Dot:
            horizontal_line = f"\n{indentation_string}..............................................................." + "..........................................................\n"
        else:
            horizontal_line = f"\n{indentation_string}===============================================================" + "==========================================================\n"

        schedule_builder += horizontal_line
        schedule_builder += f"{indentation_string}{vertical_line}              {vertical_line}    Monday    {vertical_line}   Tuesday    {vertical_line}" + \
            f"  Wednesday   {vertical_line}   Thursday   {vertical_line}    Friday    {vertical_line}   Saturday   {vertical_line}    Sunday    {vertical_line}"

        for i in range(8):
            class_hour = Section.CLASS_HOURS[i]
            schedule_builder += horizontal_line + indentation_string
            Logger.add_cell(class_hour, vertical_line, schedule_builder)

            for j in range(7):
                section = schedule[j][i]

                if section is None:
                    schedule_builder += vertical_line
                    Logger.append_gap(Logger.cell_width, schedule_builder)
                else:
                    Logger.add_cell(str(section), vertical_line, schedule_builder)

            schedule_builder += vertical_line

        schedule_builder += horizontal_line

        Logger.ignore_indentation = True
        Logger.log(schedule_builder)
        Logger.ignore_indentation = False

    def log_course_codes(front_text, courses):
        builder = [front_text]

        if len(courses) > 0:
            for c in courses:
                builder.append(c.get_code())
                builder.append(", ")

            del builder[-2:]

        log("".join(builder))

    def log_simulation_entities():
        department = Department.get_instance()
        students = department.get_students()
        lecturers = department.get_lecturers()
        assistants = department.get_assistants()
        advisors = department.get_advisors()
        mandatory_courses = department.get_mandatory_courses()
        technical_elective_courses = department.get_technical_elective_courses()
        faculty_technical_elective_courses = department.get_faculty_technical_elective_courses()
        non_technical_elective_courses = department.get_non_technical_elective_courses()

        new_line(HorizontalLineType.Star)
        new_line()
        log("SEMESTER: " + str(department.get_current_season()))
        new_line()

        log("DEPARTMENT INFORMATION:")
        increment_indentation()

        log("DEPARTMENT NAME: " + department.get_department_name())
        log("DEPARTMENT CODE: " + department.get_department_code())

        log("LECTURERS:")
        log_people_names(lecturers)

        log("ASSISTANTS:")
        log_people_names(assistants)

        log("ADVISORS:")
        log_people_names(advisors)

        log("COURSES:")
        increment_indentation()
        log("MANDATORY COURSES:")
        log_courses(mandatory_courses)
        log("TECHNICAL ELECTIVE COURSES:")
        log_courses(technical_elective_courses)
        log("FACULTY TECHNICAL ELECTIVE COURSES:")
        log_courses(faculty_technical_elective_courses)
        log("NON-TECHNICAL ELECTIVE COURSES:")
        log_courses(non_technical_elective_courses)
        decrement_indentation()

        log("STUDENTS: ")
        log_students(students)

        decrement_indentation()
        new_line(HorizontalLineType.Star)
    
    def add_cell(cell_text, vertical_line, schedule):
        text_length = len(cell_text)
        gap_length = cell_width - text_length
        gap_width = gap_length // 2 if gap_length % 2 == 0 else (gap_length // 2) + 1

        schedule.append(vertical_line)
        append_gap(gap_width, schedule)
        schedule.append(cell_text)

        gap_width = cell_width - text_length - gap_width
        append_gap(gap_width, schedule)

    def append_gap(width, builder):
        for _ in range(width):
            builder.append(" ")

    def log_people_names(people):
        increment_indentation()

        for h in people:
            log(h.get_full_name())

        decrement_indentation()

    def log_courses(courses):
        new_line()
        increment_indentation()

        for c in courses:
            log("COURSE CODE: " + c.get_code())
            log("COURSE NAME: " + c.get_name())
            log("CREDITS: " + str(c.get_credits()))
            log("QUOTA: " + str(c.get_quota()))
            log("THEORETICAL HOURS: " + str(c.get_theoretical_hours()))
            log("APPLIED HOURS: " + str(c.get_applied_hours()))
            log_course_codes("PREREQUISITES: ", c.get_prerequisites())
            new_line()

        decrement_indentation()

    def log_students(students):
        new_line()
        increment_indentation()

        for s in students:
            log("STUDENT NAME: " + s.get_full_name())
            log("STUDENT ID: " + s.get_student_id())
            log("STUDENT GRADE: " + s.get_grade().name)
            log("ADVISOR: " + s.get_advisor().get_full_name())
            log("COMPLETED CREDITS: " + str(s.get_completed_credits()))
            log("GPA: " + str(math_helper.round_float(s.get_transcript().calculate_gpa(), 2)))
            new_line()

        decrement_indentation()

    def open_log_file(append):
        try:
            writer = open(log_txt, "a" if append else "w")
        except FileNotFoundError:
            print("Output file not found!")
            return None

        return writer

    def close_log_file(writer):
        writer.flush()
        writer.close()
