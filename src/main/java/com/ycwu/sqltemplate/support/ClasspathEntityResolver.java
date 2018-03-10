package com.ycwu.sqltemplate.support;

import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class ClasspathEntityResolver implements EntityResolver {

  private final Logger logger = LoggerFactory.getLogger(ClasspathEntityResolver.class);

  private static final String DTD_NAME = "sql-template";
  private static final String SEARCH_PACKAGE = "/";

  public InputSource resolveEntity(String publicId, String systemId)
      throws SAXException, IOException {

    this.logger.debug("Trying to resolve XML entity with public ID ["
        + publicId + "] and system ID [" + systemId + "]");

    if (systemId != null
        && systemId.indexOf(DTD_NAME) > systemId.lastIndexOf("/")) {

      String dtdFile = systemId.substring(systemId
          .indexOf(DTD_NAME));

      this.logger.debug("Trying to locate [" + dtdFile + "] under ["
          + "/" + "]");

      try {
        Resource resource = new ClassPathResource(SEARCH_PACKAGE + dtdFile,
            getClass());
        InputSource source = new InputSource(resource.getInputStream());
        source.setPublicId(publicId);
        source.setSystemId(systemId);
        this.logger.debug("Found beans DTD [" + systemId
            + "] in classpath");
        return source;
      } catch (IOException ex) {
        this.logger.debug("Could not resolve beans DTD ["
            + systemId + "]: not found in classpath", ex);
      }
    }
    return null;
  }

}
