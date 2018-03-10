package com.ycwu.sqltemplate.beans;

import java.io.Serializable;

public class SqlTemplateNode implements Serializable {

  private String id;
  private String sql;

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getSql() {
    return sql;
  }

  public void setSql(String sql) {
    this.sql = sql;
  }

}
