package jp.co.tis.phr.web.constant;

public enum Term {
    TODAY("today"),
    WEEK("week");

    private String term;

    Term(String term) {
        this.term = term;
    }

    public String getTerm() {
        return term;
    }
}
