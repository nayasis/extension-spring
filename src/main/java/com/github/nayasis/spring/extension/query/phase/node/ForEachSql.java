package com.github.nayasis.spring.extension.query.phase.node;

import com.github.nayasis.basica.base.Strings;
import com.github.nayasis.basica.base.Types;
import com.github.nayasis.spring.extension.query.common.QueryConstant;
import com.github.nayasis.spring.extension.query.entity.QueryParameter;
import com.github.nayasis.spring.extension.query.phase.node.abstracts.BaseSql;
import org.springframework.expression.ParseException;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ForEachSql extends BaseSql {

	private String  key;
	private String  open;
	private String  close;
	private String  concater;
	private String  index;
	private boolean hasIndex;

	public ForEachSql( String key, String open, String close, String concater, String index ) {
		this.key      = Strings.trim( key );
		this.open     = Strings.trim( open );
		this.close    = Strings.trim( close );
		this.concater = Strings.trim( concater );
		this.index    = Strings.trim( index );
		this.hasIndex = Strings.isNotEmpty( index );
	}

	@Override
    public String toString( QueryParameter inputParam ) throws ParseException {

		List values = getValues(inputParam);
		if( values.isEmpty() ) return "";

		List<String> phases = new ArrayList<>();

		for( int i = 0; i < values.size(); i++ ) {

			QueryParameter param = inputParam.clone().setForEachInnerParam(key, values.get(i));

			if( hasIndex )
				param.put( index, i );

			String template = getSqlTemplate( param );
			template = bindSequenceToKey( template, key, i );
			template = bindSequenceToIndex( template, inputParam, i );

			if( Strings.isBlank(template) ) continue;

			phases.add( template );

		}

		return assembleSql( phases );

	}

	private List getValues( QueryParameter param ) {

		Object val = param.getByPath(key);

		if( val != null )
			return Types.toList(val);

		if( param.hasForEachInnerParam() ) {
			val = param.getForEachInnerParam( key );
			if( val != null )
				return Types.toList(val);
		} else {
			if( param.hasSingleParameter() )
				return Types.toList( param.getSingleParameter() );
		}

		return new ArrayList();

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

	private String bindSequenceToIndex( String sql, QueryParameter param, int i ) {
		if( ! hasIndex ) return sql;
		String paramIdx = String.format( "%s[%d]", QueryConstant.FOR_EACH_INDEX, param.addForeachIndex(i) );
		return replaceKey( sql, index, paramIdx );
	}

	private String bindSequenceToKey( String sql, String key, int i ) {
		String newKey = String.format( "%s[%d]", key, i );
		return replaceKey( sql, key, newKey );
	}

	private String replaceKey( String sql, String keyOrigin, String keyReplace ) {

		StringBuilder sb = new StringBuilder();

		Pattern pattern = Pattern.compile( String.format("%s(\\..+?)?", keyOrigin) );

		createQueryResolver().parse(sql, "#{", "}", ( prev, keyword, start, end ) -> {

			Matcher matcher = pattern.matcher(keyword);

			if( matcher.matches() ) {
				sb.append( sql.substring(prev, start) );
				sb.append("#{").append( keyReplace ).append( matcher.replaceFirst("$1") ).append("}");
			} else {
				sb.append( sql.substring(prev,end) );
			}

		}, remain -> {
			sb.append( remain );
		});

		return sb.toString();

	}

	private void toString( StringBuilder buffer, BaseSql node, int depth ) {
		String tab = Strings.line( ' ', depth * 2 );
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
		sb.append( String.format( "[FOREACH %s]\n", summaryAttrs()) );
		for( BaseSql node : children ) {
			toString( sb, node, 0 );
		}
		return sb.toString();
	}

	private String summaryAttrs() {
		List<String> attrs = new ArrayList<>();
		attrs.add( deco("key"      , key     ) );
		attrs.add( deco("open"     , open    ) );
		attrs.add( deco("close"    , close   ) );
		attrs.add( deco("concater" , concater) );
		attrs.add( deco("index"    , index   ) );
		return Strings.join( attrs, " " );
	}

	private String deco( String title, String value ) {
		if( Strings.isEmpty(value) ) return null;
		return String.format( "%s='%s'", title, value );
	}

	private String getSqlTemplate( QueryParameter param ) throws ParseException {
		String template = super.toString( param );
		return createQueryResolver().dynamicQuery( template, param );
	}

}
