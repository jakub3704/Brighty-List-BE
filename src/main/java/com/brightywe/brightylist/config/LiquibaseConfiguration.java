/****************************************************************************
 * Copyright 2020 Jakub Koczur
 *
 * Unauthorized copying of this project, via any medium is strictly prohibited.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, 
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES  
 * OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. 
 * IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT,  
 * TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE 
 * OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 * 
 *****************************************************************************/

package com.brightywe.brightylist.config;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.autoconfigure.liquibase.LiquibaseDataSource;
import org.springframework.boot.autoconfigure.liquibase.LiquibaseProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import liquibase.integration.spring.SpringLiquibase;

import com.zaxxer.hikari.HikariDataSource;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
 * Class LiquibaseConfiguration for database Liquibase configuration.
 */
@Configuration
@AutoConfigureAfter(DatabaseConfiguration.class)
@PropertySource({"classpath:liquibase.properties"})
public class LiquibaseConfiguration implements InitializingBean {

    private Logger log = LoggerFactory.getLogger(LiquibaseConfiguration.class);

    @Override
    public void afterPropertiesSet() throws Exception {
        log.info("LiquibaseConfiguration active");
    }

    @Bean
    @ConfigurationProperties("migration.datasource")
    public DataSourceProperties migrationDataSourceProperties() {
	return new DataSourceProperties();
    }

    @LiquibaseDataSource
    @Bean(name = "migrationDataSource")
    @ConfigurationProperties(prefix = "migration.datasource")
    public DataSource migrationDataSource() {
    	DataSource ds = migrationDataSourceProperties().initializeDataSourceBuilder()
                .type(HikariDataSource.class)
                .build();
        if (ds instanceof HikariDataSource) {
            ((HikariDataSource) ds).setMaximumPoolSize(2);
            ((HikariDataSource) ds).setMinimumIdle(0);
            ((HikariDataSource) ds).setPoolName("LiquibaseHikariPool");
        }
        return ds;
    }

    @Bean
    @ConfigurationProperties(prefix = "spring.liquibase")
    public LiquibaseProperties liquibaseProperties() {
        return new LiquibaseProperties();
    }

    @Bean
    public SpringLiquibase liquibase(LiquibaseProperties liquibaseProperties) {
        var liquibase = new SpringLiquibase();
        liquibase.setDataSource(migrationDataSource());
        liquibase.setChangeLog(liquibaseProperties.getChangeLog());
        liquibase.setContexts(liquibaseProperties.getContexts());
        liquibase.setDefaultSchema(liquibaseProperties.getDefaultSchema());
        liquibase.setDropFirst(false);
        liquibase.setShouldRun(liquibaseProperties.isEnabled());
        liquibase.setLabels(liquibaseProperties.getLabels());
        liquibase.setChangeLogParameters(liquibaseProperties.getParameters());
        liquibase.setRollbackFile(liquibaseProperties.getRollbackFile());
        return liquibase;
    }
}
