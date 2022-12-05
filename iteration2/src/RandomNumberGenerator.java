package iteration2.src;

import java.util.Random;

public class RandomNumberGenerator {

    private static Random rng = new Random(System.currentTimeMillis());

    private RandomNumberGenerator() { }

    public static float RandomFloat(){
        return rng.nextFloat();
    }


    //Upperbound is exclusive
    public static int randomIntegerBetween(int lowerBound, int upperBound){
        if(upperBound == lowerBound)
            return upperBound;
        if(upperBound < lowerBound)
            throw new IllegalArgumentException("The upper bound must be greater than the lower bound");

        return rng.nextInt(upperBound - lowerBound) + lowerBound;
    }
}
