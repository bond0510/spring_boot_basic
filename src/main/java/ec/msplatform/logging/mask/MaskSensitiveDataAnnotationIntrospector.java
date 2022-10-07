package ec.msplatform.logging.mask;

import com.fasterxml.jackson.databind.introspect.Annotated;
import com.fasterxml.jackson.databind.introspect.NopAnnotationIntrospector;

public class MaskSensitiveDataAnnotationIntrospector extends NopAnnotationIntrospector {

	private static final long serialVersionUID = 1L;

	@Override
	public Object findSerializer(Annotated annotated) {
		Mask annotation = annotated.getAnnotation(Mask.class);
		if (annotation != null) {
			return new MaskingSerializer(annotation.leftVisible(), annotation.rightVisible());
		}
		return null;
	}
}
