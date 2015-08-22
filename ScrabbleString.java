
import java.util.Arrays;

public class ScrabbleString {
    /**
     * arrange the letters of a string in alphabetical order.
     */
    public static String sort(String s) {
        char a[] = s.toCharArray();
        Arrays.sort(a);
        return new String(a);
    }

    public static void main(String av[]) {
        System.out.println(sort("JEIGUEK78 OK"));
    }
}