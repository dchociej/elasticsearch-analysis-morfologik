package pl.compan.docusafe;

import morfologik.stemming.WordData;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class LemaData {

    private List<WordData> morfologikLemmaList = Collections.emptyList();;
    private char[] polishReplacementLema = new char[0];

    private int lemmaListIndex = 0;

    private boolean polishReplacementUsed = true;

    public LemaData(List<WordData> morfologikLemmaList, char[] polishReplacementLema) {
        this.morfologikLemmaList = morfologikLemmaList;
        this.polishReplacementLema = polishReplacementLema;
        if (this.polishReplacementLema.length > 0) {
            this.polishReplacementUsed = false;
        }
    }

    public LemaData() {

    }

    public boolean isMorfologikNotEmpty() {
        return !morfologikLemmaList.isEmpty();
    }
    public boolean hasNextWordData () {
        return morfologikLemmaList.size() > lemmaListIndex;
    }

    public WordData nextWordData () {
        return morfologikLemmaList.size() > lemmaListIndex ? morfologikLemmaList.get(lemmaListIndex++) : null;
    }

    public boolean hasPolishReplacementLema() {
        return !polishReplacementUsed;
    }

    public char[] getPolishReplacementLema() {
        if(!polishReplacementUsed){
            polishReplacementUsed = true;
            return polishReplacementLema;
        }
        return new char[0];
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LemaData lemaData = (LemaData) o;
        return Objects.equals(morfologikLemmaList, lemaData.morfologikLemmaList) && Arrays.equals(polishReplacementLema, lemaData.polishReplacementLema);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(morfologikLemmaList);
        result = 31 * result + Arrays.hashCode(polishReplacementLema);
        return result;
    }

    public void clear() {
        this.morfologikLemmaList = Collections.emptyList();
        this.polishReplacementLema = new char[0];
        this.lemmaListIndex = 0;
        this.polishReplacementUsed = true;
    }
}
