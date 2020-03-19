package io.nayasis.spring.extension.sql.phase.element;

import io.nayasis.basica.base.Strings;
import io.nayasis.basica.base.Types;
import io.nayasis.basica.validation.Validator;
import io.nayasis.spring.extension.sql.entity.QueryParameter;
import io.nayasis.spring.extension.sql.phase.element.abstracts.SqlElement;
import io.nayasis.spring.extension.sql.phase.element.exception.QueryParseException;
import org.nybatis.core.conf.Const;
import org.nybatis.core.db.sql.sqlMaker.QueryResolver;
import org.nybatis.core.exception.unchecked.SqlParseException;
import org.springframework.expression.Expression;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static io.nayasis.spring.extension.sql.common.QueryConstant.FOR_EACH_INDEX;
import static io.nayasis.spring.extension.sql.common.QueryConstant.PARAMETER_SINGLE;

public class ForEachSqlElement extends SqlElement {

	private List<SqlElement> children = new ArrayList<>();

	private String key;
	private String open;
	private String close;
	private String concater;
	private String index;

	public ForEachSqlElement( String key, String open, String close, String concater, String index ) {

		this.key         = Strings.trim( key );
		this.open        = Strings.trim( open );
		this.close       = Strings.trim( close );
		this.concater    = Strings.trim( concater );
		this.index       = Strings.trim( index );

	}

	@Override
    public String toString( QueryParameter inputParam ) throws QueryParseException {

		boolean hasConcater    = Strings.isNotEmpty( concater );
		boolean hasIndex       = Strings.isNotEmpty( index );
		boolean hasSingleParam = inputParam.containsKey( PARAMETER_SINGLE );

		List params = getParams( inputParam, hasSingleParam );

		if( Validator.isEmpty(params) ) return "";

		StringBuilder sql = new StringBuilder();

		for( int i = 0, iCnt = params.size() - 1; i <= iCnt; i++ ) {

			if( hasIndex ) {
				String keyIndex = String.format( "%s[%d]", FOR_EACH_INDEX, inputParam.addForeachIndex(i) );
			}

			if( hasIndex ) {
				param.put( index, i );
			}

			String innerSql = getInnerSql( param );

			String targetKey = String.format( "%s[%d]", key, i );
			innerSql = convertKeyToJsonPath( innerSql, key, targetKey );
			innerSql = bindSingleParamKey( innerSql, hasSingleParam );

			if( hasIndex ) {
				innerSql = setIndexKey( innerSql, i, inputParam );
			}

			sql.append( innerSql );

			if( hasConcater && i != iCnt && ! Strings.isBlank(innerSql) ) {
				sql.append( ' ' ).append( concater ).append( ' ' );
			}

		}

		if( Strings.isBlank(sql) ) {
			return sql.toString();
		} else {
			return String.format( "%s %s %s", open, sql, close ).trim();
		}

	}

	private String setIndexKey( String sql, int index, QueryParameter param ) {

		if( Strings.isEmpty( this.index ) ) return sql;

		String targetKey = String.format( "%s[%d].%s", key, index, this.index );

		int beforeSize = sql.length();

		sql = convertKeyToJsonPath( sql, this.index, targetKey );

		int afterSize = sql.length();

		if( beforeSize != afterSize ) {
			param.put( targetKey, index );
		}

		return sql;

	}

	private String convertKeyToJsonPath( String sql, String sourceKey, String targetKey ) {
		return sql.replaceAll( String.format( "#\\{%s(\\..+?)?\\}", sourceKey ), String.format( "#{%s$1}", targetKey ) );
	}


	private void toString( StringBuilder buffer, SqlElement node, int depth ) {

		String tab = Strings.lpad( "", depth * 2, ' ' );

		if( node instanceof IfSqlElement ) {

			IfSqlElement ifNode = (IfSqlElement) node;

			for( SqlElement child : ifNode.children() ) {
				toString( buffer, child, depth + 1 );
			}

		} else {
			buffer.append( String.format( "%s%s", tab, node.toString() ) );
		}

	}

	public String toString() {

		StringBuilder sb = new StringBuilder();

		for( SqlElement node : children ) {
			toString( sb, node, 0 );
		}

		return sb.toString();

	}

	private QueryParameter clone( Map param ) {
		return new QueryParameter( param );
	}

	private String getInnerSql( QueryParameter param ) throws QueryParseException {

		String sqlTemplate = super.toString( param );

		return QueryResolver.makeDynamicSql( sqlTemplate, param );

	}

	private boolean hasSingleParameter( QueryParameter param ) {
		return param.containsKey( PARAMETER_SINGLE );
	}

	private List getParams( QueryParameter inputParam, boolean hasSingleParam ) {

		Object value = getValue( inputParam, hasSingleParam );
		if( value == null ) return new ArrayList();

		if( Types.isArrayOrCollection(value) ) {
			return Types.toList( value );
		} else {
			return Arrays.asList( value );
		}

	}

	private Object getValue( QueryParameter param, boolean hasSingleParam ) {

		Object val = param.get( key );

		if( val == null && hasSingleParam ) {
			String modifiedParamKey = key.replaceFirst( "^.+?(\\..+?)?$", String.format( "%s$1", Const.db.PARAMETER_SINGLE ) );
			val = param.get( modifiedParamKey );
		}

		return val;

	}

	private String bindSingleParamKey( String sql, boolean hasSingleParam ) {

		if( hasSingleParam ) {
			return sql.replaceAll( "#\\{.+?(\\[.+?\\])?(\\..+?)?\\}", String.format( "#{%s$1$2}", Const.db.PARAMETER_SINGLE) );
		} else {
			return sql;
		}

	}

}
