package com.github.nayasis.spring.extension.sql.phase.element.abstracts;

/**
 * @author nayasis@gmail.com
 * @since 2015-10-23
 */
public class ElementText {

    private Class  klass;
    private String text;

    public ElementText( Class klass, String text ) {
        this.klass = klass;
        this.text  = text;
    }

    public Class getKlass() {
        return klass;
    }

    public String getText() {
        return text;
    }

    public String toString() {
        return text;
    }

}
