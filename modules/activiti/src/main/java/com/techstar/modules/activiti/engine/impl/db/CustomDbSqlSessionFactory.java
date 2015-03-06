package com.techstar.modules.activiti.engine.impl.db;

import org.activiti.engine.impl.db.DbSqlSessionFactory;

/**
 * 增加达梦数据支持
 * @author sundoctor
 *
 */
public class CustomDbSqlSessionFactory extends DbSqlSessionFactory {

	static{
		String defaultOrderBy = " order by ${orderBy} ";
		
		 // dm
	    databaseSpecificLimitBeforeStatements.put("dm", "select * from ( select a.*, ROWNUM rnum from (");
	    databaseSpecificLimitAfterStatements.put("dm", "  ) a where ROWNUM < #{lastRow}) where rnum  >= #{firstRow}");
	    databaseSpecificLimitBetweenStatements.put("dm", "");
	    databaseOuterJoinLimitBetweenStatements.put("dm", "");
	    databaseSpecificOrderByStatements.put("dm", defaultOrderBy);
	    addDatabaseSpecificStatement("dm", "selectExclusiveJobsToExecute", "selectExclusiveJobsToExecute_integerBoolean");
	}
}
