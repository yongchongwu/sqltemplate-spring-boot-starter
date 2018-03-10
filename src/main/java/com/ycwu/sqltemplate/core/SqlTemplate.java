package com.ycwu.sqltemplate.core;

import com.ycwu.sqltemplate.beans.SqlTemplateFile;
import com.ycwu.sqltemplate.beans.SqlTemplateNode;
import com.ycwu.sqltemplate.config.SqlTemplateConfig;
import com.ycwu.sqltemplate.support.SqlTemplateResolver;
import com.ycwu.sqltemplate.util.SqlUtil;
import java.io.File;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SqlTemplate {

  private final Logger logger = LoggerFactory.getLogger(SqlTemplate.class);

  public final static String PARAMETER_KEY = "param";

  private freemarker.template.Configuration ftlCfg;

  private SqlTemplateConfig sqlTemplateConfig;

  private List<SqlTemplateFile> sqlTemplateFileList;

  private SqlTemplateResolver sqlTemplateResolver;

  public SqlTemplate() {
    this(new SqlTemplateConfig());
  }

  public SqlTemplate(SqlTemplateConfig sqlTemplateConfig) {
    this.sqlTemplateConfig = sqlTemplateConfig;
    this.sqlTemplateResolver = new SqlTemplateResolver();
  }

  public void setSqlTemplateConfig(SqlTemplateConfig sqlTemplateConfig) {
    this.sqlTemplateConfig = sqlTemplateConfig;
  }

  public void setSqlTemplateFileList(
      List<SqlTemplateFile> sqlTemplateFileList) {
    this.sqlTemplateFileList = sqlTemplateFileList;
  }

  public void init() {
    if (this.sqlTemplateConfig != null) {
      logger.info("start load sqltemplate : " + this.sqlTemplateConfig.getTemplateLocation());
      this.sqlTemplateFileList = this.sqlTemplateResolver
          .resolve(this.sqlTemplateConfig.getTemplateLocation());
      this.ftlCfg = new freemarker.template.Configuration(
          freemarker.template.Configuration.VERSION_2_3_23);
      this.ftlCfg.setLocalizedLookup(false);
      this.ftlCfg.setDefaultEncoding(this.sqlTemplateConfig.getEncoding());
      this.ftlCfg.setNumberFormat("##");
      freemarker.cache.StringTemplateLoader stringLoader = new freemarker.cache.StringTemplateLoader();
      for (SqlTemplateFile templateFile : sqlTemplateFileList) {
        for (SqlTemplateNode node : templateFile.getSqlTemplateNodes()) {
          Object val = stringLoader.findTemplateSource(node.getId());
          if (val != null) {
            throw new RuntimeException("duplicate sqltemplate id : " + node.getId());
          }
          stringLoader.putTemplate(node.getId(), node.getSql());
        }
      }
      this.ftlCfg.setTemplateLoader(stringLoader);
    }
  }

  private void conditionLoad() {
    if (sqlTemplateConfig != null && sqlTemplateConfig.isAutoload()) {
      if (hasChanged()) {
        sqlTemplateFileList = null;
        if (ftlCfg != null) {
          this.ftlCfg.clearTemplateCache();
          this.ftlCfg = null;
        }
        init();
      }
    }
  }

  private boolean hasChanged() {
    boolean modified = false;
    if (sqlTemplateFileList != null) {
      for (SqlTemplateFile sqlTemplateFile : sqlTemplateFileList) {
        File file = new File(sqlTemplateFile.getTemplatePath());
        if ((file != null) && (file.exists()) && (file.lastModified() > sqlTemplateFile
            .getLastModified())) {
          modified = true;
          break;
        }
      }
    }
    return modified;
  }

  public String getDialect(String dialect) {
    if (dialect != null && !"".equals(dialect)) {
      return dialect;
    } else {
      return this.sqlTemplateConfig.getDialect();
    }
  }

  public String getSql(String sqlId, Object[] params, Map<String, Object> paramsMap,
      String dialect) {

    if (this.ftlCfg != null) {

      conditionLoad();

      Map<String, Object> dataModel = new HashMap<String, Object>();

      dataModel.put("dialect", getDialect(dialect));

      Object param = null;
      if (params != null) {
        for (int i = 0; i < params.length; i++) {
          param = params[i];
          if (param == null) {
            continue;
          }
          dataModel.put(PARAMETER_KEY + i, param);
        }
      }
      if (paramsMap != null) {
        for (String key : paramsMap.keySet()) {
          param = paramsMap.get(key);
          if (param == null) {
            continue;
          }
          dataModel.put(key, param);
        }
      }
      String str = "";
      try {
        freemarker.template.Template template = this.ftlCfg.getTemplate(sqlId);
        StringWriter out = new StringWriter();
        template.process(dataModel, out);
        str = out.toString().trim();
        logger.debug("sql-statement[" + sqlId + "]:\n" + str);
      }catch (Exception e){
        e.printStackTrace();
      }
      return str;
    } else {
      throw new RuntimeException("must init the sqltemplate before use");
    }
  }

  public String getSql(String sqlId, Object[] params, Map<String, Object> paramsMap) {
    return getSql(sqlId, params, paramsMap, null);
  }

  public String getSql(String sqlId, Map<String, Object> paramsMap, String dialect) {
    return getSql(sqlId, null, paramsMap, dialect);
  }

  public String getSql(String sqlId, Object[] params, String dialect) {
    return getSql(sqlId, params, null, dialect);
  }

  public String getSql(String sqlId, Object[] params) {
    return getSql(sqlId, params, null, null);
  }

  public String getSql(String sqlId, Map<String, Object> paramsMap) {
    return getSql(sqlId, null, paramsMap, null);
  }

  public String getSql(String sqlId, String dialect) {
    return getSql(sqlId, null, null, dialect);
  }

  public String getSql(String sqlId) {
    return getSql(sqlId, null, null, null);
  }


  public String getCountSql(String sqlId, Object[] params, Map<String, Object> paramsMap,
      String dialect) {
    String sql = getSql(sqlId, params, paramsMap, dialect);
    sql = SqlUtil.makeCountSql(sql, getDialect(dialect));
    logger.debug("count-sql-statement[" + sqlId + "]:\n" + sql);
    return sql;
  }

  public String getCountSql(String sqlId, Object[] params, Map<String, Object> paramsMap) {
    return getCountSql(sqlId, params, paramsMap, null);
  }

  public String getCountSql(String sqlId, Object[] params, String dialect) {
    return getCountSql(sqlId, params, null, dialect);
  }

  public String getCountSql(String sqlId, Map<String, Object> paramsMap, String dialect) {
    return getCountSql(sqlId, null, paramsMap, dialect);
  }

  public String getCountSql(String sqlId, Object[] params) {
    return getCountSql(sqlId, params, null, null);
  }

  public String getCountSql(String sqlId, Map<String, Object> paramsMap) {
    return getCountSql(sqlId, null, paramsMap, null);
  }

  public String getCountSql(String sqlId, String dialect) {
    return getCountSql(sqlId, null, null, dialect);
  }

  public String getCountSql(String sqlId) {
    return getCountSql(sqlId, null, null, null);
  }


  public String getScrollSql(String sqlId, Object[] params, Map<String, Object> paramsMap,
      int start, int len, String dialect) {
    String sql = getSql(sqlId, params, paramsMap, dialect);
    sql = SqlUtil.makeScrollSql(sql, start, len, getDialect(dialect));
    logger.debug("scroll-sql-statement[" + sqlId + "]:\n" + sql);
    return sql;
  }

  public String getScrollSql(String sqlId, Object[] params, Map<String, Object> paramsMap,
      int start, int len) {
    return getScrollSql(sqlId, params, paramsMap, start, len, null);
  }

  public String getScrollSql(String sqlId, Object[] params, int start, int len, String dialect) {
    return getScrollSql(sqlId, params, null, start, len, dialect);
  }

  public String getScrollSql(String sqlId, Map<String, Object> paramsMap, int start, int len,
      String dialect) {
    return getScrollSql(sqlId, null, paramsMap, start, len, dialect);
  }

  public String getScrollSql(String sqlId, Object[] params, int start, int len) {
    return getScrollSql(sqlId, params, null, start, len, null);
  }

  public String getScrollSql(String sqlId, Map<String, Object> paramsMap, int start, int len) {
    return getScrollSql(sqlId, null, paramsMap, start, len, null);
  }

  public String getScrollSql(String sqlId, int start, int len, String dialect) {
    return getScrollSql(sqlId, null, null, start, len, dialect);
  }

  public String getScrollSql(String sqlId, int start, int len) {
    return getScrollSql(sqlId, null, null, start, len, null);
  }

}
