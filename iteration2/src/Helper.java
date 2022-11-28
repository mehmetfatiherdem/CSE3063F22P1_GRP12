package iteration2.src;

import java.util.Random;

public class Helper {
    private static Random rng = new Random();
    public static int generateRandomBetween(int lowerBound,int upperBound){
        return rng.nextInt(upperBound - lowerBound + 1) + lowerBound - 1;
    }

    public static float generateRandomFloat(){
        return rng.nextFloat();
    }

    public static int getSumOfPowersOfTwoUpTo(int pow){
        int power = 1;
        int sum = 0;

        for (int i = 0; i < pow; i++){
            sum += power;
            power *=2;
        }

        return sum;
    }



    public static int[] generateDistinctClassHours(int count){
        int[] randomHours = new int[count];

        for(int i = 0; i < count; i++){

            boolean allIsOk = false;

            while(!allIsOk)
            {
                int val = Helper.generateRandomBetween(0,42);
                randomHours[i] = val;
                allIsOk = true;

                for(int j = 0; j < i; j++){
                    if(val == randomHours[j]){
                        allIsOk = false;
                        break;
                    }
                }
            }
        }

        return randomHours;
    }

}
