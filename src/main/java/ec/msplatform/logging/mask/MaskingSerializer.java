package ec.msplatform.logging.mask;

import java.io.IOException;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import lombok.Getter;

public class MaskingSerializer extends StdSerializer<String>{
	private static final long serialVersionUID = 5586926178824899685L;

    @Getter
    private int leftVisible;

    @Getter
    private int rightVisible;

    public MaskingSerializer(int leftVisible, int rightVisible) {
        super(String.class);
        this.leftVisible = leftVisible;
        this.rightVisible = rightVisible;
    }

    @Override
    public void serialize(String value, JsonGenerator generator, SerializerProvider provider) throws IOException {
        if (isSuitableForMasking(value)) {
            generator.writeString(MaskUtils.mask(value, leftVisible, rightVisible));
        } else {
            generator.writeString(value);
        }
    }

    private boolean isSuitableForMasking(String value) {
        if (StringUtils.isNotBlank(value)) {
            int visibleCharacterCount = leftVisible + rightVisible;
            return value.length() > visibleCharacterCount;
        }
        return false;
    }
}
