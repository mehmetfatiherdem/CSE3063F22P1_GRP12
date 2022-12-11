package iteration2.src;

import java.util.Random;

public class RandomNumberGenerator {

    private static Random rng = new Random(System.currentTimeMillis());

    private RandomNumberGenerator() { }

    public static float RandomFloat(){
        return rng.nextFloat();
    }

    public static float RandomFloatBetween(float min, float max){
        if(max == min)
            return max;
        if(max < min)
            throw new IllegalArgumentException("Max must be greater than min");

        return rng.nextFloat() * (max - min) + min;
    }

    public static int randomInteger(){
        return rng.nextInt();
    }

    //Upperbound is exclusive
    public static int randomIntegerBetween(int min, int max){
        if(max == min)
            return max;
        if(max < min)
            throw new IllegalArgumentException("Max must be greater than min");

        return rng.nextInt(max - min) + min;
    }
}
