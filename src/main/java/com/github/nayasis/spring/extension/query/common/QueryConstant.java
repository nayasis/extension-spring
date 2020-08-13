package com.github.nayasis.spring.extension.query.common;

public class QueryConstant {

    public static final String HIDDEN_KEY               = "__HIDDEN_KEY_QUERY";

    public static final String PARAMETER_SINGLE         = String.format( "%s_%s", HIDDEN_KEY, "single" );
    public static final String PARAMETER_INNER_FOR_EACH = String.format( "%s_%s", HIDDEN_KEY, "foreach_inner" );
    public static final String FOR_EACH_INDEX           = String.format( "%s_%s", HIDDEN_KEY, "foreach_index" );

}