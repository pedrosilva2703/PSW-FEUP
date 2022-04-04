package cli;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.math.BigInteger;
import java.security.SecureRandom;

public class encrypter {

    public String generateStrongPasswordHash(String password){
        int iterations = 5000;
        char[] chars = password.toCharArray();
        byte[] salt = getSalt();
        byte[] hash;
        try{
            PBEKeySpec spec = new PBEKeySpec(chars, salt, iterations, 64 * 8);
            SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");

            hash = skf.generateSecret(spec).getEncoded();
            return iterations + ":" + toHex(salt) + ":" + toHex(hash);
        }catch(Exception e){
            System.out.println(e);
        }
        return " ";
    }

    private static byte[] getSalt(){
        SecureRandom sr;
        byte[] salt = new byte[16];;
        try{
            sr = SecureRandom.getInstance("SHA1PRNG");
            sr.nextBytes(salt);
        }catch(Exception e){
            System.out.println(e);
        }

        return salt;
    }

    private static String toHex(byte[] array){
        BigInteger bi = new BigInteger(1, array);
        String hex = bi.toString(16);

        int paddingLength = (array.length * 2) - hex.length();
        if (paddingLength > 0) {
            return String.format("%0" + paddingLength + "d", 0) + hex;
        } else {
            return hex;
        }
    }
   public boolean validatePassword(String originalPassword, String storedPassword) {
        String[] parts = storedPassword.split(":");
        int iterations = Integer.parseInt(parts[0]);

        byte[] salt = fromHex(parts[1]);
        byte[] hash = fromHex(parts[2]);

        PBEKeySpec spec = new PBEKeySpec(originalPassword.toCharArray(),
                salt, iterations, hash.length * 8);
        try {
            SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            byte[] testHash = skf.generateSecret(spec).getEncoded();
            int diff = hash.length ^ testHash.length;
            for(int i = 0; i < hash.length && i < testHash.length; i++)
            {
                diff |= hash[i] ^ testHash[i];
            }
            return diff == 0;

        }catch(Exception e){
            System.out.println(e);
        }
        return false;
   }

   private static byte[] fromHex(String hex){
        byte[] bytes = new byte[hex.length() / 2];
        for(int i = 0; i < bytes.length ;i++)
        {
            bytes[i] = (byte)Integer.parseInt(hex.substring(2 * i, 2 * i + 2), 16);
        }
        return bytes;
   }
}
