public class DictionaryWord implements Comparable {
    public char word[];

    DictionaryWord() {
    }

    DictionaryWord(String s) {
        this.word = s.toCharArray();
    }

    DictionaryWord(char[] a) {
        this.word = a;
    }

    DictionaryWord(DictionaryWord dw) {
        this.word = dw.word;
    }

    public boolean equals(Object o) {
        DictionaryWord dw = (DictionaryWord) o;
        if (word.length != dw.word.length) {
            return false;
        }
        for (int i = word.length - 1; i >= 0; i--) {
            if (Character.toUpperCase(this.word[i]) != Character
                    .toUpperCase(dw.word[i])) {
                return false;
            }
        }
        return true;
    }

    public String toString() {
        return new String(word);
    }

    public int compareTo(Object o) {
        DictionaryWord dw = (DictionaryWord) o;
        int n = Math.min(this.word.length, dw.word.length);
        for (int i = 0; i < n; i++) {
            int c = Character.toUpperCase(this.word[i])
                    - Character.toUpperCase(dw.word[i]);
            if (c != 0) {
                return c;
            }
        }
        return this.word.length - dw.word.length;
    }
}