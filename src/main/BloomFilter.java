package main;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import dataStructures.ArrayList;
import dataStructures.LinkedList;
import dataStructures.List;


public class BloomFilter implements BaseBloomFilter {

	/*----------Private Fields----------*/
	private List<String> allBaseUsernames;
	private List<String> allCheckUsernames;
	List<Boolean> bList;

	/**
	 * createDatabase
	 * 
	 * @return A List of string of the usernames
	 * @param path for the buffer reader ("inputFiles/database.csv") 
	 */
	
	@Override
	public List<String> createDatabase(String path) {
		// TODO Auto-generated method stub
		String line = "";

		ArrayList<String> allBaseUsernames = new ArrayList<String>(20);
		try {
			BufferedReader br = new BufferedReader(new FileReader(path));


			while((line = br.readLine()) != null) {

				String[] columnValue = line.split(",");//excel into an array of strings 
				String userName = columnValue[0];
				String userEmail = columnValue[1];
				int userProfile = Integer.parseInt(columnValue[2]);

				allBaseUsernames.add(userName);
				
				
			}
			this.allBaseUsernames = allBaseUsernames;	
			return allBaseUsernames;
			
		}catch (FileNotFoundException e){
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
		
	}

	/**
	 * createCheck
	 * 
	 * @return A List of string of the usernames
	 * @param path for the buffer reader ("inputFiles/db_check.csv") 
	 */
	@Override
	public List<String> createCheck(String path) {
		// TODO Auto-generated method stub
		String line = "";
		allCheckUsernames = new ArrayList<String>(20);
		try {
			BufferedReader br = new BufferedReader(new FileReader(path));


			while((line = br.readLine()) != null) {
				String userName = line;

				allCheckUsernames.add(userName);
			}
			
			return allCheckUsernames;
			
		}catch (FileNotFoundException e){
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}

	/**
	 * 
	 * The bloomFilter method:
	 * 
	 * creates an Empty boolean list 
	 * and using dynamically the method Size(int n)
	 * 
	 * @returns empty boolean list
	 * 
	 * **/
	@Override
	public List<Boolean> bloomFilter() {
		// TODO Auto-generated method stub
		List<Boolean>bList = new LinkedList<Boolean>();
		int count = 0 ;
		while(count < Size(allBaseUsernames.size())) {
			bList.add(false);
			count++;
		}
		
		this.bList = bList;
		return bList;
	}

	/**
	 * 	The hashing method
	 * 
	 * encode the string and turning it into a number 
	 * for it to be used to calculate the position of our word
	 * 
	 * @return hashcode number for position of the word
	 * @param word is turned into ASCII code 
	 * @param seed is part of the formula to implement the hashing
	 * **/
	@Override
	public int hashing(String word, int seed) {
		int w = 0;
		char[] Wchar = word.toCharArray();
		for(char c: Wchar) {
			w += (int)c;
		}
		int result = (w*seed)/(word.length());
		return result;
	}

	/**
	 * The fillingBloomFilter method
	 * 
	 * returns a boolean list that uses the hashing, repeatHashing and positionAterHashing
	 * and determine if the word will be inserted in the list or not
	 * 
	 * @return a Boolean List
	 */
	@Override
	public List<Boolean> fillingBloomFilter() {
		// TODO Auto-generated method stub
		int n = allBaseUsernames.size();
		int m = bloomFilter().size();
		for(int i = 0; i< allBaseUsernames.size(); i++) {
			String w = allBaseUsernames.get(i);
			for(int j= 0; j< RepeatHashing(m, n); j++) {
				int h = hashing(w, j);
				bloomFilter().set(PositionAfterHashing(h, m),true);
			}
			
		}
		
		return bloomFilter();
	}

	/**
	 * The generateResults method:
	 * 
	 * Creates a result file that has the hashing done for each word
	 * and it checks the list for the username to be in the correct spot 
	 * 
	 * **/
	@Override
	public void generateResults() {
		// TODO Auto-generated method stub
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter("outputFiles/results.csv"));
			writer.write("Username,Result\n");
			List<Boolean> newList = new LinkedList<>();
			int n = allCheckUsernames.size();
			int m = bloomFilter().size();
			boolean inDatabase = true;
			for(String username : this.allCheckUsernames) {
				for(int j= 0; j < RepeatHashing(m, n); j++) {
					int pos = hashing(username, j) % this.bList.size();
					if(!bList.get(pos)) {
						writer.write(username + ",Not in the DB\n");
						inDatabase = false;
						break;
					}
					
				}
				
				if(inDatabase) {
					writer.write(username + ",Probaly in the DB\n");
				}
				
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		

	}

	/*--------Auxiliary Methods------------*/
	
	/**
	 * Size method
	 * 
	 * @param n is the size of the database
	 * **/
	public int Size(int n) {
		int result = (int) -((n*Math.log(0.0000001))/(Math.pow(Math.log(2), 2)));
		return result;
	}

	/**
	 * RepeatHashing method
	 * 
	 * @param m is the size of the boolean List
	 * @param n is the size of the database
	 * **/
	public int RepeatHashing(int m, int n) { 
		return (int) ((m * Math.log(2))/n);
	}

	/**
	 * PositionAfterHashing method
	 * 
	 * @param h is the number returned by our hashing𝑠 is the size of the boolean List
	 * @param s is the size of the boolean List
	 * **/
	public int PositionAfterHashing(int h, int s) {
		return h%s;
	}

}
