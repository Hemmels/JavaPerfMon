package com.hemmels.javaperfmon.exception;

import org.jooq.ExecuteContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DefaultExecuteListener;
import org.springframework.jdbc.support.SQLErrorCodeSQLExceptionTranslator;
import org.springframework.jdbc.support.SQLExceptionTranslator;

public class JooqExceptionTransformer extends DefaultExecuteListener {
	
	private static final long serialVersionUID = 1L;

	@Override
	public void exception(ExecuteContext context)
	{
		SQLDialect dialect = context.configuration().dialect();
		SQLExceptionTranslator translator = new SQLErrorCodeSQLExceptionTranslator(dialect.name());
		context.exception(translator.translate("Access database using Jooq", context.sql(), context.sqlException()));
	}
}
