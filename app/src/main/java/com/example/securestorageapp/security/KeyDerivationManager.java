package com.example.securestorageapp.security;

import android.util.Log;

import java.security.GeneralSecurityException;
import java.security.SecureRandom;

import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

public class KeyDerivationManager {
    private static final String TAG = "KeyDerivationManager";
    
    /**
     * Dérive une clé à partir d'un mot de passe en utilisant PBKDF2
     * @param password Mot de passe
     * @param salt Sel
     * @param iterations Nombre d'itérations
     * @param keyLength Longueur de la clé en bits
     * @return Clé dérivée ou null en cas d'erreur
     */
    public static byte[] deriveKeyPbkdf2(char[] password, byte[] salt, int iterations, int keyLength) {
        try {
            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
            PBEKeySpec spec = new PBEKeySpec(password, salt, iterations, keyLength);
            SecretKey key = factory.generateSecret(spec);
            
            Log.d(TAG, "Clé dérivée avec succès en utilisant PBKDF2");
            return key.getEncoded();
        } catch (GeneralSecurityException e) {
            Log.e(TAG, "Erreur lors de la dérivation de la clé: " + e.getMessage());
            return null;
        }
    }
    
    /**
     * Génère un sel aléatoire
     * @param length Longueur du sel en bytes
     * @return Sel généré
     */
    public static byte[] generateSalt(int length) {
        byte[] salt = new byte[length];
        new SecureRandom().nextBytes(salt);
        return salt;
    }
    
    /**
     * Crée une clé AES à partir de bytes
     * @param keyBytes Bytes de la clé
     * @return Clé AES
     */
    public static SecretKey createAesKey(byte[] keyBytes) {
        return new SecretKeySpec(keyBytes, "AES");
    }
    
    /**
     * Dérive une clé à partir d'un mot de passe et crée une clé AES
     * @param password Mot de passe
     * @param salt Sel
     * @return Clé AES
     */
    public static SecretKey deriveAesKey(char[] password, byte[] salt) {
        byte[] keyBytes = deriveKeyPbkdf2(password, salt, 10000, 256);
        if (keyBytes != null) {
            return createAesKey(keyBytes);
        }
        return null;
    }
}
