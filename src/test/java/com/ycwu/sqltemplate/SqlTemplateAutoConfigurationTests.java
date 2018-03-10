package com.ycwu.sqltemplate;

import com.ycwu.sqltemplate.autoconfigure.SqlTemplateAutoConfiguration;
import com.ycwu.sqltemplate.core.SqlTemplate;
import java.util.HashMap;
import java.util.Map;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import static org.assertj.core.api.Assertions.assertThat;

public class SqlTemplateAutoConfigurationTests {

  AnnotationConfigApplicationContext context;

  @Before
  public void setUp() {
    this.context = new AnnotationConfigApplicationContext();
  }

  @After
  public void tearDown() {
    if (this.context != null) {
      this.context.close();
    }
  }

  @Test
  public void test(){
    this.context.register(SqlTemplateAutoConfiguration.class);
    this.context.refresh();
    SqlTemplate sqlTemplate=this.context.getBean(SqlTemplate.class);
    assertThat(sqlTemplate).isNotNull();

    String sql=null;
    Map<String,Object> paramMap=new HashMap<String,Object>();
    paramMap.put("username","ycwu");

    sql=sqlTemplate.getCountSql("test_sqltemplate_id",new Object[]{1,""},paramMap);
    assertThat(sql).isNotBlank();

    sql=sqlTemplate.getScrollSql("test_sqltemplate_id",new Object[]{1,""},paramMap,0,20);
    assertThat(sql).isNotBlank();

  }

}
