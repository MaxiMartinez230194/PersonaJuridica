/**
 *
 * @author CENTRO DE CÓMPUTOS DE LA PROVINCIA DE MISIONES
 * CIFRADO AES 128BITS
 */
package org.ccpm.dpj.utilidad;

import java.security.SecureRandom;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;

public class MiCipher {

    private SecureRandom sr = new SecureRandom();
    private String clave = "1234567890ccpm17"; // 128 bit
    private byte[] iv = new byte[16];
    private String passaEncriptar;
    private String encriptado;
    
    public MiCipher(){
        
    }
    

    public  String encriptar(String clave, byte[] iv, String value) {
        try {
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
            SecretKeySpec sks = new SecretKeySpec(clave.getBytes("UTF-8"), "AES");
            cipher.init(Cipher.ENCRYPT_MODE, sks, new IvParameterSpec(iv));
            byte[] encriptado = cipher.doFinal(value.getBytes());
            return DatatypeConverter.printBase64Binary(encriptado);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public  String decriptar(String clave, byte[] iv,String encriptado) {
        try {
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
            SecretKeySpec sks = new SecretKeySpec(clave.getBytes("UTF-8"), "AES");
            cipher.init(Cipher.DECRYPT_MODE, sks, new IvParameterSpec(iv));

            byte[] dec = cipher.doFinal(DatatypeConverter.parseBase64Binary(encriptado));
            return new String(dec);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public String getPassaEncriptar() {
        return passaEncriptar;
    }

    public void setPassaEncriptar(String passaEncriptar) {
        this.passaEncriptar = passaEncriptar;
    }

    
    
    
    public String getEncriptado() {  
         //sr.nextBytes(iv);
        
         return encriptar(clave, iv, this.getPassaEncriptar());
    }


    public void setEncriptado(String encriptado) {
        this.encriptado = encriptado;
    }

    public SecureRandom getSr() {
        return sr;
    }

    public void setSr(SecureRandom sr) {
        this.sr = sr;
    }

    public String getClave() {
        return clave;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }

    public byte[] getIv() {
        return iv;
    }

    public void setIv(byte[] iv) {
        this.iv = iv;
    }
    
    
}