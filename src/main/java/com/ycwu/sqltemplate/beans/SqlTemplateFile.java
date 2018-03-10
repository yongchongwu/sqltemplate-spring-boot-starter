package com.ycwu.sqltemplate.beans;

import java.util.ArrayList;
import java.util.List;

public class SqlTemplateFile {

  private String templatePath;
  private long lastModified;
  private List<SqlTemplateNode> sqlTemplateNodes = new ArrayList<SqlTemplateNode>();

  public String getTemplatePath() {
    return templatePath;
  }

  public void setTemplatePath(String templatePath) {
    this.templatePath = templatePath;
  }

  public long getLastModified() {
    return lastModified;
  }

  public void setLastModified(long lastModified) {
    this.lastModified = lastModified;
  }

  public List<SqlTemplateNode> getSqlTemplateNodes() {
    return sqlTemplateNodes;
  }

  public void setSqlTemplateNodes(
      List<SqlTemplateNode> sqlTemplateNodes) {
    this.sqlTemplateNodes = sqlTemplateNodes;
  }

  public void addSqlTemplateNode(SqlTemplateNode node) {
    if (this.sqlTemplateNodes == null) {
      sqlTemplateNodes = new ArrayList<SqlTemplateNode>();
    }
    this.sqlTemplateNodes.add(node);
  }

}
