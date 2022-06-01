package pl.compan.docusafe;

import morfologik.stemming.Dictionary;
import morfologik.stemming.polish.PolishStemmer;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.standard.StandardTokenizer;

public class MorfologikAnalyzer extends Analyzer {
    private final Dictionary dictionary;

    public MorfologikAnalyzer(final Dictionary dictionary) {
        this.dictionary = dictionary;
    }

    /**
     * Builds an analyzer with the default Morfologik's Polish dictionary.
     */
    public MorfologikAnalyzer() {
        this(new PolishStemmer().getDictionary());
    }


    @Override
    protected TokenStreamComponents createComponents(final String field) {
        final Tokenizer src = new StandardTokenizer();

        return new TokenStreamComponents(
                src,
                new MorfologikFilter(src, dictionary));
    }

}
