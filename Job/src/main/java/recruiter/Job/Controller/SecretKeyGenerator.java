package recruiter.Job.Controller;

import java.util.Base64;
import java.security.Key;

import javax.crypto.KeyGenerator;

public class SecretKeyGenerator {
    public static void main(String[] args) {
        try {
            KeyGenerator keyGen = KeyGenerator.getInstance("HmacSHA256");
            keyGen.init(256); // for strong security
            Key key = keyGen.generateKey();
            String encodedKey = Base64.getEncoder().encodeToString(key.getEncoded());
            System.out.println("Generated Secret Key: " + encodedKey);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
