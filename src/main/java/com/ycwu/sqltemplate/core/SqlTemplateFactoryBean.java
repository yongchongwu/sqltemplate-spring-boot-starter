package com.ycwu.sqltemplate.core;

import com.ycwu.sqltemplate.config.SqlTemplateConfig;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;

public class SqlTemplateFactoryBean implements FactoryBean<SqlTemplate>,
    InitializingBean {

  private SqlTemplateConfig sqlTemplateConfig;

  private SqlTemplate sqlTemplate;

  public SqlTemplateFactoryBean(SqlTemplateConfig sqlTemplateConfig) {
    this.sqlTemplateConfig = sqlTemplateConfig;
  }

  @Override
  public SqlTemplate getObject() throws Exception {
    if (this.sqlTemplate == null) {
      this.afterPropertiesSet();
    }
    return this.sqlTemplate;
  }

  @Override
  public Class<?> getObjectType() {
    return this.sqlTemplate == null ? SqlTemplate.class
        : this.sqlTemplate.getClass();
  }

  @Override
  public boolean isSingleton() {
    return true;
  }

  @Override
  public void afterPropertiesSet() throws Exception {
    if (this.sqlTemplateConfig != null) {
      this.sqlTemplate = new SqlTemplate(sqlTemplateConfig);
      this.sqlTemplate.init();
    }
  }
}
