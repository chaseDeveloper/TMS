package tms.main.util;

import java.nio.charset.Charset;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
 
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
 
import org.apache.commons.codec.binary.Base64;
 
public class KediAES256Util {
	private static volatile KediAES256Util INSTANCE;
	
	final static String secretKey = "1@3$5^7*9)1#5&9@4^8)0(8&6%4#2!48";
	static String IV = secretKey.substring(0, 16);
	
	public static KediAES256Util getInstance(){
		if(INSTANCE == null){
			synchronized(KediAES256Util.class){
				if(INSTANCE == null){
					INSTANCE = new KediAES256Util();
				}
			}
		}
		return INSTANCE;
	}    
 
    // 암호화
    public String aesEncode(String str) throws java.io.UnsupportedEncodingException, 
                                                    NoSuchAlgorithmException, 
                                                    NoSuchPaddingException, 
                                                    InvalidKeyException, 
                                                    InvalidAlgorithmParameterException, 
                                                    IllegalBlockSizeException, 
                                                    BadPaddingException {
    	byte[] keyData = secretKey.getBytes();
    	
    	SecretKey secureKey = new SecretKeySpec(keyData, "AES");
        Cipher c = Cipher.getInstance("AES/CBC/PKCS5Padding");
        c.init(Cipher.ENCRYPT_MODE, secureKey, new IvParameterSpec(IV.getBytes(Charset.forName("UTF-8"))));
 
        byte[] encrypted = c.doFinal(str.getBytes("UTF-8"));
        String enStr = new String(Base64.encodeBase64(encrypted));
 
        return enStr;
    }
 
    //복호화
    public String aesDecode(String str) throws java.io.UnsupportedEncodingException,
                                                        NoSuchAlgorithmException,
                                                        NoSuchPaddingException, 
                                                        InvalidKeyException, 
                                                        InvalidAlgorithmParameterException,
                                                        IllegalBlockSizeException, 
                                                        BadPaddingException {
        byte[] keyData = secretKey.getBytes();
        
        SecretKey secureKey = new SecretKeySpec(keyData, "AES");
        Cipher c = Cipher.getInstance("AES/CBC/PKCS5Padding");
        c.init(Cipher.DECRYPT_MODE, secureKey, new IvParameterSpec(IV.getBytes(Charset.forName("UTF-8"))));
 
        byte[] byteStr = Base64.decodeBase64(str.getBytes());
 
        return new String(c.doFinal(byteStr),"UTF-8");
    } 
}