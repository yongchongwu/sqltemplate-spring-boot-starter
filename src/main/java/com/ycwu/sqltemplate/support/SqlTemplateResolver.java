package com.ycwu.sqltemplate.support;

import com.ycwu.sqltemplate.beans.SqlTemplateFile;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

public class SqlTemplateResolver {

  private final Logger logger = LoggerFactory.getLogger(SqlTemplateResolver.class);

  public List<SqlTemplateFile> resolve(String templateLocation) {
    List<SqlTemplateFile> sqlTemplateFileList = new ArrayList<SqlTemplateFile>();
    ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
    try {
      Resource[] resources = resolver.getResources(templateLocation);
      SqlTemplateFile sqlTemplateFile = null;
      for (Resource resource : resources) {
        logger.info("resolve sqltemplate file : {}",resource.getFile().getAbsolutePath());
        sqlTemplateFile = new SqlTemplateFile();
        sqlTemplateFile.setTemplatePath(resource.getFile().getAbsolutePath());
        sqlTemplateFile.setLastModified(resource.getFile().lastModified());
        sqlTemplateFile.setSqlTemplateNodes(
            new SqlTemplateParser()
                .parse(resource.getInputStream(), resource.getFile().getAbsolutePath()));
        sqlTemplateFileList.add(sqlTemplateFile);
      }
    } catch (IOException e) {
      logger.info("NOT FOUND : {} does not exist",templateLocation);
    }
    return sqlTemplateFileList;
  }

}
