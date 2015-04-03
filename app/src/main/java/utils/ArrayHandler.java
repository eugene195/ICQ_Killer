package utils;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by eugene on 04.04.15.
 */
public class ArrayHandler {

    public static boolean findInArray(String client, String [] clientArray) {
        for (String aClientArray : clientArray)
            if (aClientArray.equals(client))
                return true;
        return false;
    }

    public static String[] removeFromArray(String[] input, String deleteMe) {
        List<String> result = new LinkedList<>();
        for(String item : input)
            if(!deleteMe.equals(item))
                result.add(item);
        return result.toArray(input);
    }

    public static String[] addToArray(String[] input, String addMe) {
        List<String> result = new LinkedList<>();
        Collections.addAll(result, input);
        result.add(addMe);
        return result.toArray(input);
    }
}
