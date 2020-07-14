package com.github.nayasis.spring.extension.sql.phase.element;

import com.github.nayasis.basica.base.Strings;
import com.github.nayasis.basica.base.Types;
import com.github.nayasis.spring.extension.sql.common.QueryConstant;
import com.github.nayasis.spring.extension.sql.entity.QueryParameter;
import com.github.nayasis.spring.extension.sql.phase.element.abstracts.BaseSql;
import com.github.nayasis.spring.extension.sql.sqlMaker.QueryResolver;
import org.springframework.expression.ParseException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ForEachSql extends BaseSql {

	private List<BaseSql> children = new ArrayList<>();

	private String key;
	private String open;
	private String close;
	private String concater;
	private String index;

	public ForEachSql( String key, String open, String close, String concater, String index ) {
		this.key      = Strings.trim( key );
		this.open     = Strings.trim( open );
		this.close    = Strings.trim( close );
		this.concater = Strings.trim( concater );
		this.index    = Strings.trim( index );
	}

	@Override
    public String toString( QueryParameter inputParam ) throws ParseException {

		String key    = getKey( inputParam );
		List   values = getValues( inputParam, key );

		if( values.isEmpty() ) return "";

		List<String> phases = new ArrayList<>();

		for( int i = 0, iCnt = values.size() - 1; i <= iCnt; i++ ) {

			String template = getSqlTemplate( inputParam );
			template = bindSequenceToKey( template, key, i );
			template = bindSequenceToIndex( template, inputParam, i );

			if( Strings.isBlank(template) ) continue;

			phases.add( template );

		}

		return assembleSql( phases );

	}

	private String assembleSql( List<String> phases ) {

		StringBuilder sb = new StringBuilder();

		String concater = this.concater.isEmpty() ? "" : String.format( " %s ", this.concater );

		if( ! open.isEmpty() )
			sb.append( open ).append( ' ' );

		sb.append( Strings.join( phases, concater ) );

		if( ! close.isEmpty() )
			sb.append( ' ' ).append( close );

		return sb.toString();

	}

	private boolean hasIndex() {
		return Strings.isNotEmpty( index );
	}

	private String getKey( QueryParameter parameter ) {
		if( ! parameter.containsJsonPath(key) ) {
			if ( parameter.hasSingleParameter() ) {
				return QueryConstant.PARAMETER_SINGLE;
			}
		}
		return key;
	}

	private String bindSequenceToIndex( String sql, QueryParameter param, int i ) {
		if( ! hasIndex() ) return sql;
		String newIndex = String.format( "%s[%d]", QueryConstant.FOR_EACH_INDEX, param.addForeachIndex(i) );
		return replaceKey( sql, index, newIndex );
	}

	private String bindSequenceToKey( String sql, String key, int i ) {
		String newKey = String.format( "%s[%d]", key, i );
		return replaceKey( sql, key, newKey );
	}

	private String replaceKey( String sql, String keyOrigin, String keyReplace ) {
		return sql.replaceAll( String.format( "#\\{%s(\\..+?)?\\}", keyOrigin ), String.format( "#{%s$1}", keyReplace ) );
	}

	private void toString( StringBuilder buffer, BaseSql node, int depth ) {

		String tab = Strings.lpad( "", depth * 2, ' ' );

		if( node instanceof IfSql ) {

			IfSql ifNode = (IfSql) node;

			for( BaseSql child : ifNode.children() ) {
				toString( buffer, child, depth + 1 );
			}

		} else {
			buffer.append( String.format( "%s%s", tab, node.toString() ) );
		}

	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		for( BaseSql node : children ) {
			toString( sb, node, 0 );
		}
		return sb.toString();
	}

	private String getSqlTemplate( QueryParameter param ) throws ParseException {
		String template = super.toString( param );
		template = QueryResolver.bindDynamicQuery( template, param );
		template = QueryResolver.bindSingleParameterKey( template, param );
		return template;
	}

	private List getValues( QueryParameter parameter, String key ) {

		Object value = parameter.getByJsonPath( key );
		if( value == null ) return new ArrayList();

		if( Types.isArrayOrCollection(value) ) {
			return Types.toList( value );
		} else {
			return Arrays.asList( value );
		}

	}

}
