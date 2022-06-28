package automation.library.common;

import org.apache.commons.codec.binary.Base64;

/**
 * Decode the base64 encodes string.
 */
public class Encryption {

    public static  String decodeBase64(String password){
        byte[] decodedBytes = Base64.decodeBase64(password);
        return new String(decodedBytes);
    }
}
