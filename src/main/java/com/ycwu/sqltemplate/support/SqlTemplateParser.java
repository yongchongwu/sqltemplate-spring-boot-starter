package com.ycwu.sqltemplate.support;

import com.ycwu.sqltemplate.beans.SqlTemplateNode;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.digester.Digester;

public class SqlTemplateParser {

  private List<SqlTemplateNode> sqlTemplateNodeList;

  public List<SqlTemplateNode> parse(InputStream inputStream, String path) {
    Digester digester = new Digester();
    digester.setEntityResolver(new ClasspathEntityResolver());
    digester.setValidating(true);
    digester.push(this);

    digester.addObjectCreate("Template/sql-statement",
        "com.ycwu.sqltemplate.beans.SqlTemplateNode");

    digester.addSetProperties("Template/sql-statement");

    digester.addSetNext("Template/sql-statement", "addNode",
        "com.ycwu.sqltemplate.beans.SqlTemplateNode");

    digester.addCallMethod("Template/sql-statement", "setSql", 0);

    try {
      digester.parse(inputStream);
    } catch (Exception e) {
      throw new RuntimeException("problem parsing configuration:"
          + path, e);
    }
    return this.sqlTemplateNodeList;
  }

  public void addNode(SqlTemplateNode node) {
    if (this.sqlTemplateNodeList == null) {
      this.sqlTemplateNodeList = new ArrayList<SqlTemplateNode>();
    }
    this.sqlTemplateNodeList.add(node);
  }

}
