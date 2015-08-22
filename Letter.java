
public class Letter {
    static short             distribution[] = {
                                            /*
                                             * A B C D E F G H I J K L M N O P Q
                                             * R S T U V W X Y Z Blank
                                             */
                                            9, 2, 2, 4, 12, 2, 3, 2, 9, 1, 1,
            4, 2, 6, 8, 2, 1, 6, 4, 6, 4, 2, 2, 1, 2, 1, 2 };

    static short             scores[]       = {
                                            /*
                                             * A B C D E F G H I J K L M N O P Q
                                             * R S T U V W X Y Z Blank
                                             */
                                            1, 3, 3, 2, 1, 4, 2, 4, 1, 8, 5, 1,
            3, 1, 1, 3, 10, 1, 1, 1, 1, 4, 4, 8, 4, 10, 0 };

    public static final char BLANK          = '?';
    public static final char FIRST          = 'A';
    public static final char LAST           = 'Z';

    /**
     * @return the score for a given letter
     */
    public static int getScore(char letter) {
        return isBlank(letter) ? 0 : scores[letter - FIRST];
    }

    /**
     * @return the number of this type of letter in the scrabble set.
     */
    public static int getCount(char letter) {
        return letter == BLANK ? 2 : distribution[letter - FIRST];
    }

    private static void selfTest() {
        for (char a = FIRST; a <= LAST; a++) {
            System.out.println(a + " " + getScore(a) + " " + getCount(a));
        }
        System.out.println("total count " + totalCount());
        System.out.println("array sizes " + scores.length + " "
                + distribution.length);
    }

    public static void main(String av[]) {
        selfTest();
    }

    /**
     * @return the total number of letters
     */
    public static int totalCount() {
        int sum = 0;
        for (int i = 0; i < distribution.length; i++) {
            sum += distribution[i];
        }
        return sum;
    }

    public static boolean isBlank(char c) {
        return Character.isLowerCase(c);
    }
}