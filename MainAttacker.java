/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashSet;
import java.util.Set;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

import org.omg.CORBA.portable.InputStream;

import jdk.nashorn.internal.codegen.CompilerConstants;

/**
 *
 * @author ElMof
 */
public class MainAttacker {
    public static boolean Finished = false; 
    public static int CipherCount = 0;
    public static Set<String> wordSet = new HashSet<String>();
    public static Set<String> getDictionary() throws FileNotFoundException, IOException{
    	try(BufferedReader br = new BufferedReader(new FileReader("words.txt"))) {
        	String line = "";
            while ((line = br.readLine()) != null) {
                wordSet.add(line.toLowerCase());
            }
        }
    	return wordSet;
    }
    
	    public static void main(String[] argv) throws Exception 
		{
	    	Set<String> Dictionary = getDictionary();
	    	
	    	//Encryption For Testing
		    KeyGeneration KGen = new KeyGeneration();
		    byte[] ByteSeq = KGen.ByteSeq;
		    
		    SecretKeyFactory sf = SecretKeyFactory.getInstance("DES");
		    
		    byte[] B1=new byte[] {3,0,0,0,0,31,0x70,(byte) 0x80};
		    SecretKey K1= sf.generateSecret(new DESKeySpec(B1));
		    
		    System.out.println("#######Key 1:");
		    DesEncrypter encrypter = new DesEncrypter(K1);
		  
		    System.out.println(K1);
		    System.out.println(B1);    
		    String encrypted = encrypter.encrypt("Don't tell anybody! Hello Hey");
		    PrintWriter CodeBreakWriter = null;
			try {
				CodeBreakWriter = new PrintWriter(new FileWriter(("CipherFile.txt"), true));
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			CodeBreakWriter.println(encrypted);
		//	CodeBreakWriter.println(");
			CodeBreakWriter.close();
		    System.out.println(encrypted);
		    String decrypted = encrypter.decrypt(encrypted);
		    System.out.println(decrypted);
		    /////////////////////////////////////////

		    int NumbOfThreads=128;

		    AttackingThread [] attackers = new AttackingThread[NumbOfThreads];
		    try(BufferedReader br = new BufferedReader(new FileReader("CipherFile.txt"))) {
	        	String line = "";
	            while ((line = br.readLine()) != null) {
        		    	System.out.println("checking line " + line);
	        		    for(int a=0;a<NumbOfThreads;a++)
	        		    {
	        		        attackers[a] = new AttackingThread(a, ByteSeq, line, sf);
	        		        attackers[a].start();
	        		    }
	        		    for(int i=0; i<NumbOfThreads; i++){
	        		        attackers[i].join();
	        		    }
	            }
	            CipherCount++;
	        }
		    

			   return;   
		}
}
