from enum import Enum
class LetterGrade(Enum):
    AA = 4
    BA = 3.5
    BB = 3
    CB = 2.5
    CC = 2
    DC = 1.5
    DD = 1
    FD = 0.5
    FF = 0
    ZZ = 0
    NOT_GRADED = 0

    def __init__(self, num_val):
        self.num_val = num_val
    
    def get_num_val(self):
        return self.num_val
