package com.ycwu.sqltemplate.autoconfigure;

import com.ycwu.sqltemplate.config.SqlTemplateConfig;
import com.ycwu.sqltemplate.core.SqlTemplate;
import com.ycwu.sqltemplate.core.SqlTemplateFactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(SqlTemplateProperties.class)
@ConditionalOnClass(SqlTemplate.class)
@ConditionalOnProperty(prefix = "sqltemplate", value = "enabled", matchIfMissing = true)
public class SqlTemplateAutoConfiguration {

  @Autowired
  private SqlTemplateProperties sqlTemplateProperties;

  @Bean
  @ConditionalOnMissingBean(SqlTemplate.class)
  public SqlTemplate sqlTemplate() throws Exception {
    SqlTemplateConfig config = new SqlTemplateConfig();
    config.setTemplateLocation(sqlTemplateProperties.getTemplateLocation());
    config.setEncoding(sqlTemplateProperties.getEncoding());
    config.setDialect(sqlTemplateProperties.getDialect());
    config.setAutoload(sqlTemplateProperties.isAutoload());
    SqlTemplateFactoryBean factory = new SqlTemplateFactoryBean(config);
    return factory.getObject();
  }

}
