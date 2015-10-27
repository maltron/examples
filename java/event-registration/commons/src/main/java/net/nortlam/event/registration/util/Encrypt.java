package net.nortlam.event.registration.util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Mauricio "Maltron" Leal <maltron@gmail.com> */
public class Encrypt {

    private static final Logger LOG = Logger.getLogger(Encrypt.class.getName());
    
    public static final String REGEX_EMAIL_VALUE = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
    
    public static final String DEFAULT_ENCRYPT_ALGORITHM = "SHA-256";
    public static final String DEFAULT_ENCODING = "UTF-8";

    public static String encrypt(String password) {
        return encrypt(DEFAULT_ENCRYPT_ALGORITHM, DEFAULT_ENCODING, password);
    }

    public static String encrypt(String encryptionAlgorithm, String encoding, 
                                                            String password) {
        try {
            // Default: SHA-256
            MessageDigest digest = MessageDigest.getInstance(encryptionAlgorithm);
            // Default: UTF-8
            digest.update(password.getBytes(encoding));
           
            // USED ONLY JAVA 8
            return new String(Base64.getEncoder().encode(digest.digest()));
            
        } catch(NoSuchAlgorithmException ex) {
            // No supposed to happen
            LOG.log(Level.SEVERE, "### encrypt() NO SUCH ALGORITHM:{0}", ex.getMessage());
        } catch(UnsupportedEncodingException ex) {
            // No suppoed to happen
            LOG.log(Level.SEVERE, "### encrypt() UNSUPPORTED ENCONDING:{0}",
                    ex.getMessage());
        }
        
        return null;
    }
}
