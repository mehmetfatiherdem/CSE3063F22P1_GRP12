import random
import time

class MathHelper:
    def __init__(self):
        self.rng = random.Random(time.time()*1000)

    @classmethod
    def random_float(cls):
        return cls.rng.random()

    @classmethod
    def random_float_between(cls, min, max):
        if max == min:
            return max
        if max < min:
            raise ValueError("Max must be greater than min")

        return cls.rng.uniform(min, max)

    @classmethod
    def random_integer(cls):
        return cls.rng.randint(0, 2**32 - 1)

    @classmethod
    def random_integer_between(cls, min, max):
        if max == min:
            return max
        if max < min:
            raise ValueError("Max must be greater than min")

        return cls.rng.randint(min, max)

    @classmethod
    def round_float(cls, num, no_of_decimal_digits):
        if no_of_decimal_digits > 6:
            no_of_decimal_digits = 6

        pow_of_10 = 10**no_of_decimal_digits
        temp = num * pow_of_10

        return round(temp) / pow_of_10
