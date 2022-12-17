package iteration2.src;

import java.util.Random;

public class MathHelper {
    private static Random rng = new Random(System.currentTimeMillis());

    private MathHelper() { }

    public static float RandomFloat(){
        return rng.nextFloat();
    }

    public static float randomFloatBetween(float min, float max){
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

    public static float roundFloat(float num, int noOfDecimalDigits){
        if(noOfDecimalDigits > 6)
            noOfDecimalDigits = 6;

        int powOf10 = (int)Math.pow(10,noOfDecimalDigits);
        float temp = num * powOf10;

        return Math.round(temp) / (float)powOf10;
    }
}
