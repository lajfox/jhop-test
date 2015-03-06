package org.hibernate.dialect;

import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import org.hibernate.LockMode;
import org.hibernate.MappingException;
import org.hibernate.cfg.Environment;
import org.hibernate.dialect.function.NoArgSQLFunction;
import org.hibernate.dialect.function.StandardSQLFunction;
import org.hibernate.internal.util.StringHelper;
import org.hibernate.type.StandardBasicTypes;

/**
 * 达梦数据库hibernate４ Dialect，官方提供的Dialect支持的hibernate版本太低，不支持hibernate４
 * 
 * @author sundoctor
 * 
 */
public class XDmDialect extends Dialect {

	public XDmDialect() {
		registerColumnType(Types.CHAR, "CHAR($l)");
		registerColumnType(Types.VARCHAR, "VARCHAR($l)");
		registerColumnType(Types.LONGVARCHAR, "TEXT");
		registerColumnType(Types.CLOB, "TEXT");
		registerColumnType(Types.BIT, "BIT");
		registerColumnType(Types.BOOLEAN, "BIT");
		registerColumnType(Types.TINYINT, "TINYINT");
		registerColumnType(Types.SMALLINT, "SMALLINT");
		registerColumnType(Types.INTEGER, "INTEGER");
		registerColumnType(Types.BIGINT, "BIGINT");
		registerColumnType(Types.REAL, "FLOAT");
		registerColumnType(Types.FLOAT, "FLOAT");
		registerColumnType(Types.DOUBLE, "DOUBLE");
		registerColumnType(Types.DECIMAL, "DECIMAL");
		registerColumnType(Types.NUMERIC, "DECIMAL");
		registerColumnType(Types.BINARY, "BINARY");
		registerColumnType(Types.VARBINARY, "BLOB");
		registerColumnType(Types.LONGVARBINARY, "BLOB");
		registerColumnType(Types.BLOB, "BLOB");
		registerColumnType(Types.DATE, "DATE");
		registerColumnType(Types.TIME, "TIME");
		registerColumnType(Types.TIMESTAMP, "DATETIME");
		registerFunction("abs", new StandardSQLFunction("abs"));
		registerFunction("asin", new StandardSQLFunction("asin", StandardBasicTypes.DOUBLE));
		registerFunction("acos", new StandardSQLFunction("acos", StandardBasicTypes.DOUBLE));
		registerFunction("atan", new StandardSQLFunction("atan", StandardBasicTypes.DOUBLE));
		registerFunction("atan2", new StandardSQLFunction("atan2", StandardBasicTypes.DOUBLE));
		registerFunction("ceil", new StandardSQLFunction("ceil", StandardBasicTypes.INTEGER));
		registerFunction("ceiling", new StandardSQLFunction("ceiling", StandardBasicTypes.INTEGER));
		registerFunction("cos", new StandardSQLFunction("cos", StandardBasicTypes.DOUBLE));
		registerFunction("cot", new StandardSQLFunction("cot", StandardBasicTypes.DOUBLE));
		registerFunction("cosh", new StandardSQLFunction("cosh", StandardBasicTypes.DOUBLE));
		registerFunction("degrees", new StandardSQLFunction("degrees"));
		registerFunction("exp", new StandardSQLFunction("exp", StandardBasicTypes.DOUBLE));
		registerFunction("GREATEST", new StandardSQLFunction("GREATEST", StandardBasicTypes.DOUBLE));
		registerFunction("floor", new StandardSQLFunction("floor", StandardBasicTypes.INTEGER));
		registerFunction("ln", new StandardSQLFunction("ln", StandardBasicTypes.DOUBLE));
		registerFunction("log", new StandardSQLFunction("log", StandardBasicTypes.DOUBLE));
		registerFunction("log10", new StandardSQLFunction("log10", StandardBasicTypes.DOUBLE));
		registerFunction("mod", new StandardSQLFunction("mod", StandardBasicTypes.INTEGER));
		registerFunction("pi", new NoArgSQLFunction("pi", StandardBasicTypes.DOUBLE));
		registerFunction("power", new StandardSQLFunction("power", StandardBasicTypes.DOUBLE));
		registerFunction("radians", new StandardSQLFunction("radians"));
		registerFunction("rand", new NoArgSQLFunction("rand", StandardBasicTypes.DOUBLE));
		registerFunction("round", new StandardSQLFunction("round"));
		registerFunction("sign", new StandardSQLFunction("sign", StandardBasicTypes.INTEGER));
		registerFunction("sin", new StandardSQLFunction("sin", StandardBasicTypes.DOUBLE));
		registerFunction("sinh", new StandardSQLFunction("sinh", StandardBasicTypes.DOUBLE));
		registerFunction("sqrt", new StandardSQLFunction("sqrt", StandardBasicTypes.DOUBLE));
		registerFunction("tan", new StandardSQLFunction("tan", StandardBasicTypes.DOUBLE));
		registerFunction("tanh", new StandardSQLFunction("tanh", StandardBasicTypes.DOUBLE));
		registerFunction("trunc", new StandardSQLFunction("trunc"));
		registerFunction("truncate", new StandardSQLFunction("truncate"));
		registerFunction("ascii", new StandardSQLFunction("ascii", StandardBasicTypes.INTEGER));
		registerFunction("bit_length", new StandardSQLFunction("bit_length", StandardBasicTypes.LONG));
		registerFunction("char", new StandardSQLFunction("char", StandardBasicTypes.CHARACTER));
		registerFunction("difference", new StandardSQLFunction("difference", StandardBasicTypes.INTEGER));
		registerFunction("LENGTH", new StandardSQLFunction("LENGTH", StandardBasicTypes.INTEGER));
		registerFunction("char_length", new StandardSQLFunction("char_length", StandardBasicTypes.LONG));
		registerFunction("character_length", new StandardSQLFunction("character_length", StandardBasicTypes.LONG));
		registerFunction("chr", new StandardSQLFunction("chr", StandardBasicTypes.CHARACTER));
		registerFunction("initcap", new StandardSQLFunction("initcap", StandardBasicTypes.STRING));
		registerFunction("insert", new StandardSQLFunction("insert", StandardBasicTypes.STRING));
		registerFunction("insstr", new StandardSQLFunction("insstr", StandardBasicTypes.STRING));
		registerFunction("instr", new StandardSQLFunction("instr", StandardBasicTypes.LONG));
		registerFunction("SUBSTRING", new StandardSQLFunction("SUBSTRING", StandardBasicTypes.STRING));
		registerFunction("instrb", new StandardSQLFunction("instrb", StandardBasicTypes.LONG));
		registerFunction("lcase", new StandardSQLFunction("lcase", StandardBasicTypes.STRING));
		registerFunction("left", new StandardSQLFunction("left", StandardBasicTypes.STRING));
		registerFunction("leftstr", new StandardSQLFunction("leftstr", StandardBasicTypes.STRING));
		registerFunction("len", new StandardSQLFunction("len", StandardBasicTypes.INTEGER));
		registerFunction("LENGTHB", new StandardSQLFunction("LENGTHB", StandardBasicTypes.INTEGER));
		registerFunction("octet_length", new StandardSQLFunction("octet_length", StandardBasicTypes.LONG));
		registerFunction("locate", new StandardSQLFunction("locate", StandardBasicTypes.LONG));
		registerFunction("lower", new StandardSQLFunction("lower", StandardBasicTypes.STRING));
		registerFunction("lpad", new StandardSQLFunction("lpad", StandardBasicTypes.STRING));
		registerFunction("ltrim", new StandardSQLFunction("ltrim", StandardBasicTypes.STRING));
		registerFunction("position", new StandardSQLFunction("position", StandardBasicTypes.INTEGER));
		registerFunction("INS", new StandardSQLFunction("INS", StandardBasicTypes.STRING));
		registerFunction("repeat", new StandardSQLFunction("repeat", StandardBasicTypes.STRING));
		registerFunction("REPLICATE", new StandardSQLFunction("REPLICATE", StandardBasicTypes.STRING));
		registerFunction("STUFF", new StandardSQLFunction("STUFF", StandardBasicTypes.STRING));
		registerFunction("repeatstr", new StandardSQLFunction("repeatstr", StandardBasicTypes.STRING));
		registerFunction("replace", new StandardSQLFunction("replace", StandardBasicTypes.STRING));
		registerFunction("reverse", new StandardSQLFunction("reverse", StandardBasicTypes.STRING));
		registerFunction("right", new StandardSQLFunction("right", StandardBasicTypes.STRING));
		registerFunction("rightstr", new StandardSQLFunction("rightstr", StandardBasicTypes.STRING));
		registerFunction("rpad", new StandardSQLFunction("rpad", StandardBasicTypes.STRING));
		registerFunction("TO_NUMBER", new StandardSQLFunction("TO_NUMBER"));
		registerFunction("rtrim", new StandardSQLFunction("rtrim", StandardBasicTypes.STRING));
		registerFunction("soundex", new StandardSQLFunction("soundex", StandardBasicTypes.STRING));
		registerFunction("space", new StandardSQLFunction("space", StandardBasicTypes.STRING));
		registerFunction("substr", new StandardSQLFunction("substr", StandardBasicTypes.STRING));
		registerFunction("substrb", new StandardSQLFunction("substrb", StandardBasicTypes.STRING));
		registerFunction("to_char", new StandardSQLFunction("to_char", StandardBasicTypes.STRING));
		registerFunction("STRPOSDEC", new StandardSQLFunction("STRPOSDEC", StandardBasicTypes.STRING));
		registerFunction("STRPOSINC", new StandardSQLFunction("STRPOSINC", StandardBasicTypes.STRING));
		registerFunction("VSIZE", new StandardSQLFunction("VSIZE", StandardBasicTypes.INTEGER));
		registerFunction("translate", new StandardSQLFunction("translate", StandardBasicTypes.STRING));
		registerFunction("trim", new StandardSQLFunction("trim", StandardBasicTypes.STRING));
		registerFunction("ucase", new StandardSQLFunction("ucase", StandardBasicTypes.STRING));
		registerFunction("upper", new StandardSQLFunction("upper", StandardBasicTypes.STRING));
		registerFunction("OVERLAPS", new StandardSQLFunction("OVERLAPS"));
		registerFunction("DATEPART", new StandardSQLFunction("DATEPART"));
		registerFunction("DATE_PART", new StandardSQLFunction("DATE_PART"));
		registerFunction("add_days", new StandardSQLFunction("add_days"));
		registerFunction("add_months", new StandardSQLFunction("add_months"));
		registerFunction("add_weeks", new StandardSQLFunction("add_weeks"));
		registerFunction("curdate", new NoArgSQLFunction("curdate", StandardBasicTypes.DATE));
		registerFunction("curtime", new NoArgSQLFunction("curtime", StandardBasicTypes.TIME));
		registerFunction("current_date", new NoArgSQLFunction("current_date", StandardBasicTypes.DATE));
		registerFunction("current_time", new NoArgSQLFunction("current_time", StandardBasicTypes.TIME));
		registerFunction("current_timestamp", new NoArgSQLFunction("current_timestamp", StandardBasicTypes.TIMESTAMP));
		registerFunction("dateadd", new StandardSQLFunction("dateadd", StandardBasicTypes.TIMESTAMP));
		registerFunction("CUR_TICK_TIME", new StandardSQLFunction("CUR_TICK_TIME"));
		registerFunction("datediff", new StandardSQLFunction("datediff", StandardBasicTypes.INTEGER));
		registerFunction("datepart", new StandardSQLFunction("datepart", StandardBasicTypes.INTEGER));
		registerFunction("dayname", new StandardSQLFunction("dayname", StandardBasicTypes.STRING));
		registerFunction("dayofmonth", new StandardSQLFunction("dayofmonth", StandardBasicTypes.INTEGER));
		registerFunction("dayofweek", new StandardSQLFunction("dayofweek", StandardBasicTypes.INTEGER));
		registerFunction("dayofyear", new StandardSQLFunction("dayofyear", StandardBasicTypes.INTEGER));
		registerFunction("days_between", new StandardSQLFunction("days_between", StandardBasicTypes.INTEGER));
		registerFunction("extract", new StandardSQLFunction("extract"));
		registerFunction("getdate", new StandardSQLFunction("getdate", StandardBasicTypes.TIMESTAMP));
		registerFunction("hour", new StandardSQLFunction("hour", StandardBasicTypes.INTEGER));
		registerFunction("LOCALTIMESTAMP", new StandardSQLFunction("LOCALTIMESTAMP"));
		registerFunction("NOW", new StandardSQLFunction("NOW"));
		registerFunction("last_day", new StandardSQLFunction("last_day"));
		registerFunction("minute", new StandardSQLFunction("minute", StandardBasicTypes.INTEGER));
		registerFunction("month", new StandardSQLFunction("month", StandardBasicTypes.INTEGER));
		registerFunction("monthname", new StandardSQLFunction("monthname", StandardBasicTypes.STRING));
		registerFunction("months_between", new StandardSQLFunction("months_between"));
		registerFunction("GREATEST", new StandardSQLFunction("GREATEST", StandardBasicTypes.DATE));
		registerFunction("TO_DATETIME", new StandardSQLFunction("TO_DATETIME"));
		registerFunction("next_day", new StandardSQLFunction("next_day"));
		registerFunction("quarter", new StandardSQLFunction("quarter", StandardBasicTypes.INTEGER));
		registerFunction("second", new StandardSQLFunction("second", StandardBasicTypes.INTEGER));
		registerFunction("round", new StandardSQLFunction("round"));
		registerFunction("timestampadd", new StandardSQLFunction("timestampadd", StandardBasicTypes.TIMESTAMP));
		registerFunction("timestampdiff", new StandardSQLFunction("timestampdiff", StandardBasicTypes.INTEGER));
		registerFunction("BIGDATEDIFF", new StandardSQLFunction("BIGDATEDIFF", StandardBasicTypes.LONG));
		registerFunction("sysdate", new StandardSQLFunction("sysdate", StandardBasicTypes.TIME));
		registerFunction("to_date", new StandardSQLFunction("to_date"));
		registerFunction("LEAST", new StandardSQLFunction("LEAST"));
		registerFunction("trunc", new StandardSQLFunction("trunc"));
		registerFunction("week", new StandardSQLFunction("week", StandardBasicTypes.INTEGER));
		registerFunction("weekday", new StandardSQLFunction("weekday", StandardBasicTypes.INTEGER));
		registerFunction("weeks_between", new StandardSQLFunction("weeks_between", StandardBasicTypes.INTEGER));
		registerFunction("year", new StandardSQLFunction("year", StandardBasicTypes.INTEGER));
		registerFunction("years_between", new StandardSQLFunction("years_between", StandardBasicTypes.INTEGER));
		registerFunction("coalesce", new StandardSQLFunction("coalesce"));
		registerFunction("ifnull", new StandardSQLFunction("ifnull"));
		registerFunction("isnull", new StandardSQLFunction("isnull"));
		registerFunction("nullif", new StandardSQLFunction("nullif"));
		registerFunction("nvl", new StandardSQLFunction("nvl"));
		registerFunction("str", new StandardSQLFunction("to_char", StandardBasicTypes.STRING));
		registerFunction("decode", new StandardSQLFunction("decode"));
		registerFunction("cur_database", new StandardSQLFunction("cur_database", StandardBasicTypes.STRING));
		registerFunction("page", new StandardSQLFunction("page", StandardBasicTypes.INTEGER));
		registerFunction("sessid", new StandardSQLFunction("sessid", StandardBasicTypes.LONG));
		registerFunction("uid", new StandardSQLFunction("uid", StandardBasicTypes.LONG));
		registerFunction("user", new StandardSQLFunction("user", StandardBasicTypes.STRING));
		registerFunction("vsize", new StandardSQLFunction("vsize", StandardBasicTypes.INTEGER));
		registerFunction("tabledef", new StandardSQLFunction("tabledef", StandardBasicTypes.STRING));

		getDefaultProperties().setProperty("hibernate.use_outer_join", "true");
		getDefaultProperties().setProperty(Environment.STATEMENT_BATCH_SIZE, DEFAULT_BATCH_SIZE);
	}

	public boolean supportsIdentityColumns() {
		return true;
	}

	public boolean supportsInsertSelectIdentity() {
		return true;
	}

	public boolean hasDataTypeInIdentityColumn() {
		return true;
	}

	public String getIdentitySelectString() throws MappingException {
		return "select scope_identity()";
	}

	public String appendIdentitySelectToInsert(String insertString) {
		return (new StringBuilder(String.valueOf(insertString))).append(" select scope_identity()").toString();
	}

	protected String getIdentityColumnString() throws MappingException {
		return "identity not null";
	}

	public boolean supportsSequences() {
		return true;
	}

	public String getSequenceNextValString(String sequenceName) throws MappingException {
		return (new StringBuilder("select ")).append(sequenceName).append(".nextval").toString();
	}

	protected String getCreateSequenceString(String sequenceName) throws MappingException {
		return (new StringBuilder("create sequence ")).append(sequenceName).toString();
	}

	protected String getDropSequenceString(String sequenceName) throws MappingException {
		return (new StringBuilder("drop sequence ")).append(sequenceName).toString();
	}

	public String getSelectGUIDString() {
		return "select GUID()";
	}

	public boolean supportsLimit() {
		return true;
	}

	public boolean supportsLimitOffset() {
		return supportsLimit();
	}

	public boolean supportsVariableLimit() {
		return supportsLimit();
	}

	public boolean bindLimitParametersInReverseOrder() {
		return true;
	}

	public boolean bindLimitParametersFirst() {
		return false;
	}

	public boolean useMaxForLimit() {
		return true;
	}

	public String getLimitString(String query, int offset, int limit) {
		return getLimitString(query, offset > 0);
	}

	public String getLimitString(String query, boolean hasOffset) {
		query = query.trim();
		boolean isForUpdate = false;
		if (query.toLowerCase().endsWith(" for update")) {
			query = query.substring(0, query.length() - 11);
			isForUpdate = true;
		}
		StringBuilder pagingSelect = new StringBuilder(query.length() + 100);
		if (hasOffset)
			pagingSelect.append("select * from ( select row_.*, rownum rownum_ from ( ");
		else
			pagingSelect.append("select * from ( ");
		pagingSelect.append(query);
		if (hasOffset)
			pagingSelect.append(" ) row_ where rownum <= ?) where rownum_ > ?");
		else
			pagingSelect.append(" ) where rownum <= ?");
		if (isForUpdate)
			pagingSelect.append(" for update");
		return pagingSelect.toString();
	}

	public boolean forUpdateOfColumns() {
		return true;
	}

	public boolean supportsOuterJoinForUpdate() {
		return false;
	}

	public String getForUpdateString(String aliases) {
		return "";
	}

	public String appendLockHint(LockMode mode, String tableName) {
		return tableName;
	}

	public boolean supportsTemporaryTables() {
		return true;
	}

	public String generateTemporaryTableName(String baseTableName) {
		return (new StringBuilder("#")).append(baseTableName).toString();
	}

	public boolean dropTemporaryTableAfterUse() {
		return false;
	}

	public ResultSet getResultSet(CallableStatement ps) throws SQLException {
		for (boolean isResultSet = ps.execute(); !isResultSet && ps.getUpdateCount() != -1; isResultSet = ps
				.getMoreResults())
			;
		return ps.getResultSet();
	}

	public boolean supportsCurrentTimestampSelection() {
		return true;
	}

	public String getCurrentTimestampSelectString() {
		return "select current_timestamp";
	}

	public boolean supportsUnionAll() {
		return true;
	}

	public String toBooleanValueString(boolean bool) {
		return bool ? "1" : "0";
	}

	public char openQuote() {
		return '"';
	}

	public char closeQuote() {
		return '"';
	}

	public String getAddColumnString() {
		return "add column";
	}

	public String getAddForeignKeyConstraintString(String constraintName, String foreignKey[], String referencedTable,
			String primaryKey[], boolean referencesPrimaryKey) {
		StringBuilder res = new StringBuilder(30);
		res.append(" add constraint ").append(constraintName).append(" foreign key (")
				.append(StringHelper.join(", ", foreignKey)).append(") references ").append(referencedTable);
		if (!referencesPrimaryKey)
			res.append(" (").append(StringHelper.join(", ", primaryKey)).append(')');
		return res.toString();
	}

	public boolean supportsIfExistsBeforeTableName() {
		return false;
	}

	public boolean supportsIfExistsAfterTableName() {
		return false;
	}

	public boolean supportsColumnCheck() {
		return true;
	}

	public boolean supportsTableCheck() {
		return true;
	}

	public boolean supportsCascadeDelete() {
		return true;
	}

	public boolean supportsNotNullUnique() {
		return true;
	}
}
