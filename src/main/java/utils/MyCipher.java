package utils;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import java.util.Base64;

public class MyCipher{


    public static String encrypt(String toEncrypt) throws Exception {
        try {
            String data = toEncrypt;
            String key = "1234567812345678";
            String iv = "1234567812345678";

            Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
            int blockSize = cipher.getBlockSize();

            byte[] dataBytes = data.getBytes();
            int plaintextLength = dataBytes.length;
            if (plaintextLength % blockSize != 0) {
                plaintextLength = plaintextLength + (blockSize - (plaintextLength % blockSize));
            }

            byte[] plaintext = new byte[plaintextLength];
            System.arraycopy(dataBytes, 0, plaintext, 0, dataBytes.length);

            SecretKeySpec keyspec = new SecretKeySpec(key.getBytes(), "AES");
            IvParameterSpec ivspec = new IvParameterSpec(iv.getBytes());

            cipher.init(Cipher.ENCRYPT_MODE, keyspec, ivspec);
            byte[] encrypted = cipher.doFinal(plaintext);

            String base64Encoded = Base64.getEncoder().encodeToString(encrypted);

            System.out.println(base64Encoded);

            return base64Encoded;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String desEncrypt(String toDecrypt) throws Exception {


        try
        {
            String data = toDecrypt ;
            String key = "1234567812345678";
            String iv = "1234567812345678";

            Base64.Decoder decoder = Base64.getDecoder();
            byte[] encrypted1 = decoder.decode(data);

            Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
            SecretKeySpec keyspec = new SecretKeySpec(key.getBytes(), "AES");
            IvParameterSpec ivspec = new IvParameterSpec(iv.getBytes());

            cipher.init(Cipher.DECRYPT_MODE, keyspec, ivspec);

            byte[] original = cipher.doFinal(encrypted1);
            String originalString = new String(original);

            System.out.println(originalString.trim());

            return originalString.trim();

        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }



}
