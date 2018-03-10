package com.ycwu.sqltemplate.config;

public class SqlTemplateConfig {

  public static final String DEFAULT_TEMPLATE_LOCATION = "sqltemplate/*.xml";
  public static final String DEFAULT_ENCODING = "UTF-8";
  public static final String DEFAULT_DIALECT = "mysql";

  private String templateLocation = DEFAULT_TEMPLATE_LOCATION;
  private String encoding = DEFAULT_ENCODING;
  private String dialect = DEFAULT_DIALECT;
  private boolean autoload = true;

  public static String getDefaultTemplateLocation() {
    return DEFAULT_TEMPLATE_LOCATION;
  }

  public static String getDefaultEncoding() {
    return DEFAULT_ENCODING;
  }

  public static String getDefaultDialect() {
    return DEFAULT_DIALECT;
  }

  public String getTemplateLocation() {
    return templateLocation;
  }

  public void setTemplateLocation(String templateLocation) {
    this.templateLocation = templateLocation;
  }

  public String getEncoding() {
    return encoding;
  }

  public void setEncoding(String encoding) {
    this.encoding = encoding;
  }

  public String getDialect() {
    return dialect;
  }

  public void setDialect(String dialect) {
    this.dialect = dialect;
  }

  public boolean isAutoload() {
    return autoload;
  }

  public void setAutoload(boolean autoload) {
    this.autoload = autoload;
  }

}
