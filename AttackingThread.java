/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

/**
 *
 * @author ElMof
 */
public class AttackingThread extends Thread {
    int ID;
    byte[] ByteSeq;
    String encrypted;
    SecretKeyFactory sf;
    DesEncrypter Attacker;
    public AttackingThread(int ID, byte[] ByteSeq, String encrypted, SecretKeyFactory sf) {
        this.ID = ID;
        this.ByteSeq = ByteSeq;
        this.encrypted = encrypted;
        this.sf = sf;
    }
    public boolean isEnglish(String[] words) {
    	int count = 0;
    	 for ( String word : words) {
    		 if(!MainAttacker.wordSet.contains(word.toLowerCase())) {
         	//	System.out.println("word " + word + " is not in dictionary");
         	}
         	else {
         		count++;
         		System.out.println("word " + word + " is in dictionary");
         	}
    		 
	 	}

    	if((float)((float)count/(float)words.length) >= 0.8) {
    		return true;
    	}
    	else return false;
    }
    @Override
    public void run()
    {
        byte[]KeyBytes;
    SecretKey TrialKey;
    String TrialPlain;
        Loop:
        for(int b=0;b<128;b++)
        {
            for(int c=0;c<128;c++)
            {
                for(int d=0;d<128;d++)
                {
                    for(int e=0;e<128;e++)
                    {
                        for(int f=0;f<128;f++)
                        {
                            for(int g=0;g<128;g++)
                            {
                                for(int h=0;h<128;h++)
                                {
                                    KeyBytes=new byte[] {ByteSeq[ID],ByteSeq[b],ByteSeq[c],ByteSeq[d],ByteSeq[e],ByteSeq[f],ByteSeq[g],(byte) ByteSeq[h]};
                                    try{
                                        TrialKey= sf.generateSecret(new DESKeySpec(KeyBytes));
	                                    Attacker = new DesEncrypter(TrialKey);
	                                    TrialPlain = Attacker.decrypt(encrypted);
                                    }
                                    catch(Exception eg){
                                        System.out.println("Exception eg ;)");
                                        continue;
                                    }
                                    //System.out.println("Plain text is " + TrialPlain);
                                    boolean isEnglish = true;
                                    String[] TrialPlainWords = TrialPlain.split(" ");   
                                    if(!TrialPlain.equals("Wrong KeySize") && TrialPlain.matches("[a-z A-Z 0-9 ? ! , . ; ']+$")) {
                                    	 System.out.println("Checking plain text "+ TrialPlain);
	                                    if(isEnglish(TrialPlainWords)) {
		                                        System.out.println("Key Found :D !! plain text is "+ TrialPlain);
		                                        MainAttacker.Finished = true;
		                                        PrintWriter CodeBreakWriter = null;
												try {
													CodeBreakWriter = new PrintWriter(new FileWriter(("CodeBreak" + MainAttacker.CipherCount +".txt"), true));
												} catch (IOException e1) {
													e1.printStackTrace();
												}
												CodeBreakWriter.println("Retrieved Message: " +  TrialPlain);
												CodeBreakWriter.println("Retrieved Key: " +  KeyBytes);
											//	CodeBreakWriter.println(");
												CodeBreakWriter.close();
		                                        break Loop;
		                                    }
		                                    if(MainAttacker.Finished){
		                                        break Loop;
		                                    }
	                                   
	                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    return;
    }
}
