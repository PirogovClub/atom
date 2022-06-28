package automation.library.common;

import org.jboss.aerogear.security.otp.Totp;

/**
 * Helper method implementing Java One Time Password API by https://github.com/aerogear-attic/aerogear-otp-java.
 * This API is compatible with Google Authenticator apps available for Android and iPhone.
 */
public class MFA {

    /**
     * @param secretKey shared secret while user registration to connect to Google Authenticator
     * @return OTP
     */
    public static String get2FA(String secretKey) {
        Totp totp = new Totp(secretKey);
        return totp.now();
    }
}
