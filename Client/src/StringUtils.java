
public class StringUtils {

	public static String[] parseString(String arrayString) {
		arrayString = arrayString.substring(1, arrayString.length()-1);
		
		return arrayString.split(", ");
	}
}
