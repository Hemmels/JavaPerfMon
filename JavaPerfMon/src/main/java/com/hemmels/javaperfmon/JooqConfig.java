package com.hemmels.javaperfmon;

import javax.sql.DataSource;

import org.jooq.ExecuteContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DataSourceConnectionProvider;
import org.jooq.impl.DefaultConfiguration;
import org.jooq.impl.DefaultDSLContext;
import org.jooq.impl.DefaultExecuteListener;
import org.jooq.impl.DefaultExecuteListenerProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.TransactionAwareDataSourceProxy;
import org.springframework.jdbc.support.SQLErrorCodeSQLExceptionTranslator;
import org.springframework.jdbc.support.SQLExceptionTranslator;

/**
 * Implements the prod-Jooq db settings.
 * @author Matt
 *
 */
public class JooqConfig {

	@Autowired
	private DataSource dataSource;

	@Bean
	public DataSourceConnectionProvider connectionProvider()
	{
		return new DataSourceConnectionProvider(new TransactionAwareDataSourceProxy(dataSource));
	}

	@Bean
	public DefaultDSLContext dsl()
	{
		return new DefaultDSLContext(configuration());
	}

	@Bean
	public TransactionAwareDataSourceProxy transactionAwareDataSource()
	{
		return new TransactionAwareDataSourceProxy(dataSource);
	}

	@Bean
	public DataSourceTransactionManager transactionManager()
	{
		return new DataSourceTransactionManager(dataSource);
	}

	@Bean
	public ExceptionTranslator exceptionTransformer()
	{
		return new ExceptionTranslator();
	}

	public DefaultConfiguration configuration()
	{
		DefaultConfiguration jooqConfiguration = new DefaultConfiguration();
		jooqConfiguration.set(connectionProvider());
		jooqConfiguration.set(new DefaultExecuteListenerProvider(exceptionTransformer()));

		return jooqConfiguration;
	}

	// So jooq errors can implement DataAccessException 
	private class ExceptionTranslator extends DefaultExecuteListener {
		private static final long serialVersionUID = 1L;

		public void exception(ExecuteContext context)
		{
			SQLDialect dialect = context.configuration().dialect();
			SQLExceptionTranslator translator = new SQLErrorCodeSQLExceptionTranslator(dialect.name());
			context.exception(translator.translate("Access database using Jooq", context.sql(), context.sqlException()));
		}
	}
}
