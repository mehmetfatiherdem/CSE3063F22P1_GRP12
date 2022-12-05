package iteration2.src;

public class Helper {

    public static int[] generateDistinctClassHours(int count){
        int[] randomHours = new int[count];

        for(int i = 0; i < count; i++){

            boolean allIsOk = false;

            while(!allIsOk)
            {
                int val = RandomNumberGenerator.randomIntegerBetween(0,40);
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
