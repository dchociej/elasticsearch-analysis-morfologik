package pl.compan.docusafe;


import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.OffsetAttribute;
import org.apache.lucene.analysis.tokenattributes.PositionIncrementAttribute;
import org.apache.lucene.analysis.tokenattributes.TypeAttribute;

import java.util.*;

public class PolishCharReplaceTokensGenerator {

    private static final Map<Character, Character> polishCharsReplacementMap = getReplacementMap();

    public char[] lookupForWord(CharSequence word) {
        char[] out = new char[word.length()];
        Set<Character> polishCharacters = polishCharsReplacementMap.keySet();
        boolean replacementOccured = false;
        for (int i = 0; i < word.length(); i++) {
            if (polishCharacters.contains(word.charAt(i))) {
                out[i] = polishCharsReplacementMap.get(word.charAt(i));
                replacementOccured = true;
            } else {
                out[i] = word.charAt(i);
            }
        }
        if (replacementOccured) {
            return out;
        }
        return new char[0];
    }

    private static Map<Character, Character> getReplacementMap() {
        Map<Character, Character> result = new HashMap<>();
        result.put((char) 261, 'a');
        result.put((char) 260, 'A');
        result.put((char) 347, 's');
        result.put((char) 346, 'S');
        result.put((char) 380, 'z');
        result.put((char) 379, 'Z');
        result.put((char) 378, 'z');
        result.put((char) 377, 'Z');
        result.put((char) 281, 'e');
        result.put((char) 280, 'E');
        result.put((char) 263, 'c');
        result.put((char) 262, 'C');
        result.put((char) 324, 'n');
        result.put((char) 323, 'N');
        result.put((char) 322, 'l');
        result.put((char) 321, 'L');
        result.put((char) 243, 'o');
        result.put((char) 211, 'O');
        return result;
    }
}
