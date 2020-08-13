package com.github.nayasis.spring.extension.query.resolve.parse.implement;

import com.github.nayasis.spring.extension.query.resolve.checker.ParamDetector;
import com.github.nayasis.spring.extension.query.resolve.checker.implement.SqlCommentChecker;
import com.github.nayasis.spring.extension.query.resolve.checker.implement.SqlQuotChecker;
import com.github.nayasis.spring.extension.query.resolve.parse.QueryResolver;
import com.github.nayasis.spring.extension.query.resolve.parse.ResolveParam;
import com.github.nayasis.spring.extension.query.sql.mapper.SqlType;

import java.util.HashSet;
import java.util.Set;

import static com.github.nayasis.basica.base.Strings.isNotEmpty;

public class SqlResolver extends QueryResolver<SqlResolver> {

    private static Set<SqlType> TYPE_STRING = new HashSet<SqlType>() {{
       add( SqlType.VARCHAR );
       add( SqlType.LONGVARCHAR );
       add( SqlType.CHAR );
       add( SqlType.CLOB );
       add( SqlType.NCLOB );
    }};

    public SqlResolver() {
        super( new ParamDetector(new SqlCommentChecker(),new SqlQuotChecker()) );
    }

    public String debugString() {

        return getQuery((i,key,param) -> {

            Object  value = param.value();
            SqlType type  = type(param);

            if( type == SqlType.NULL ) {
                return "NULL";
            } else if( TYPE_STRING.contains(type) ) {
                return String.format( "'%s'", value );
            } else {
                return value.toString();
            }

        });

    }

    private SqlType type( ResolveParam param ) {

        if( param.value() == null )
            return SqlType.NULL;

        SqlType type = null;

        if( isNotEmpty(param.type()) ) {
            type = SqlType.find(param.type());
        }

        if( type == null )
            type = SqlType.find( param.value().getClass() );

        return type;

    }

}
