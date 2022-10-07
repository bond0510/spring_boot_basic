package ec.msplatform.logging.interceptor;

import com.fasterxml.jackson.databind.AnnotationIntrospector;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.introspect.AnnotationIntrospectorPair;

import ec.msplatform.logging.mask.MaskSensitiveDataAnnotationIntrospector;

public class LoggingObjectMapper extends ObjectMapper {

  private static final long serialVersionUID = -1620079742091329228L;

  public LoggingObjectMapper() {

    super();

    MaskSensitiveDataAnnotationIntrospector annotationIntrospector = new MaskSensitiveDataAnnotationIntrospector();
    AnnotationIntrospector pair = AnnotationIntrospectorPair.pair(getSerializationConfig().getAnnotationIntrospector(),
        annotationIntrospector);
    setAnnotationIntrospector(pair);
  }
}
