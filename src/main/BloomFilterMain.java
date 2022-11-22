package main;

import dataStructures.List;

/**
 * Class where the Bloom Filter logic will be implemented.
 * This is NOT where you will implement the BloomFilter methods.
 * 
 * @author Gretchen Bonilla
 *
 */
public class BloomFilterMain extends BloomFilter {

			
	public static void main(String[] args) {
		/*
		 * BLOOM FILTER LOGIC DESCRIBED IN THE DOCUMENT GOES HERE
		 * 
		 * When the main finishes executing it should produce results.csv 
		 * and the output should be the same as the one shown in expected_output.csv
		 */
		BloomFilter bf = new BloomFilter();
		
		List<String> db = bf.createDatabase("inputFiles/database.csv");
		List<String> cb = bf.createCheck("inputFiles/db_check.csv");
		bf.bloomFilter();
		bf.fillingBloomFilter();
		bf.generateResults();
			
																						
	}
		
}
