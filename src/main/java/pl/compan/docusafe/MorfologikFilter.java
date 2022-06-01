package pl.compan.docusafe;

import morfologik.stemming.Dictionary;
import morfologik.stemming.DictionaryLookup;
import morfologik.stemming.IStemmer;
import morfologik.stemming.WordData;
import org.apache.lucene.analysis.CharacterUtils;
import org.apache.lucene.analysis.TokenFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.morfologik.MorphosyntacticTagsAttribute;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.KeywordAttribute;
import org.apache.lucene.analysis.tokenattributes.PositionIncrementAttribute;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.regex.Pattern;

public class MorfologikFilter extends TokenFilter {

    private final CharTermAttribute termAtt = addAttribute(CharTermAttribute.class);
    private final MorphosyntacticTagsAttribute tagsAtt = addAttribute(MorphosyntacticTagsAttribute.class);
    private final PositionIncrementAttribute posIncrAtt = addAttribute(PositionIncrementAttribute.class);
    private final KeywordAttribute keywordAttr = addAttribute(KeywordAttribute.class);
    private State current;
    private final TokenStream input;
    private final IStemmer stemmer;
    private LemaData lemmas;
    private final ArrayList<StringBuilder> tagsList = new ArrayList<>();

    public MorfologikFilter(final TokenStream in, final Dictionary dict) {
        super(in);
        this.input = in;
        this.stemmer = new DictionaryLookup(dict);
        this.lemmas = new LemaData();
    }

    private final static Pattern lemmaSplitter = Pattern.compile("\\+|\\|");

    private void popNextLemma() {
        // One tag (concatenated) per lemma.
        CharSequence tag = null;
        if(lemmas.hasNextWordData()){
            final WordData lemma =  lemmas.nextWordData();
            termAtt.setEmpty().append(lemma.getStem());
            tag = lemma.getTag();
        } else {
            termAtt.setEmpty().append(new String(lemmas.getPolishReplacementLema()));
        }
        if (tag != null) {
            String[] tags = lemmaSplitter.split(tag.toString());
            for (int i = 0; i < tags.length; i++) {
                if (tagsList.size() <= i) {
                    tagsList.add(new StringBuilder());
                }
                StringBuilder buffer = tagsList.get(i);
                buffer.setLength(0);
                buffer.append(tags[i]);
            }
            tagsAtt.setTags(tagsList.subList(0, tags.length));
        } else {
            tagsAtt.setTags(Collections.<StringBuilder> emptyList());
        }
    }
    private boolean lookupSurfaceForm(CharSequence token) {
        this.lemmas = new LemaData(this.stemmer.lookup(token), new PolishCharReplaceTokensGenerator().lookupForWord(token));
        return lemmas.isMorfologikNotEmpty() || lemmas.hasPolishReplacementLema();
    }

    @Override
    public final boolean incrementToken() throws IOException {
        if (lemmas.hasNextWordData() || lemmas.hasPolishReplacementLema()) {
            restoreState(current);
            posIncrAtt.setPositionIncrement(0);
            popNextLemma();
            return true;
        } else if (this.input.incrementToken()) {
            lowercaseTerm();
            if (!keywordAttr.isKeyword() && (lookupSurfaceForm(termAtt))) {
                current = captureState();
                popNextLemma();
            } else {
                tagsAtt.clear();
            }
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void reset() throws IOException {
        lemmas.clear();
        tagsList.clear();
        super.reset();
    }

    private void lowercaseTerm() {
        CharacterUtils.toLowerCase(termAtt.buffer(), 0, termAtt.length());
    }
}