package ec.msplatform.logging.mask;


import org.apache.commons.lang3.StringUtils;

public final class MaskUtils {
	private MaskUtils() {
	}

	public static String mask(String value, int showFirstDigitCount, int showLastDigitCount) {
		String leftPart = StringUtils.left(value, showFirstDigitCount);
		String maskedPart = StringUtils.repeat("*", value.length() - (showFirstDigitCount + showLastDigitCount));
		String rightPart = StringUtils.right(value, showLastDigitCount);
		return leftPart + maskedPart + rightPart;
	}

}
