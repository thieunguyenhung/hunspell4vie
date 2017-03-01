package com.spellchecker.suggestion;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class VietnameseDictionary {
	private HashMap<Integer, String> dictionaryHash;

	/**
	 * Constructor
	 * 
	 * @param filePath
	 *            String path to the dictionary file
	 * @throws FileNotFoundException
	 */
	public VietnameseDictionary(String filePath) throws FileNotFoundException {
		File file = new File(filePath);
		if (!file.exists())
			throw new FileNotFoundException();
		else
			readDictionary(filePath);
	}

	private void readDictionary(String filePath) {
		dictionaryHash = new HashMap<>();
		try {
			BufferedReader br = new BufferedReader(new FileReader(filePath));
			String line;
			int i = 0;
			while ((line = br.readLine()) != null) {
				dictionaryHash.put(i, line);
				i++;
			}
			br.close();
		} catch (Exception e) {
		}
	}

	/**
	 * Get a word from Dictionary
	 * 
	 * @param key
	 *            Integer start from 0
	 */
	public String getWord(int key) {
		if (key < 0 || key >= dictionaryHash.size())
			return "";
		return dictionaryHash.get(key);
	}

	/**
	 * Check if dictionary contain a word
	 * 
	 * @param value
	 *            String word to check
	 */
	public boolean contain(String value) {
		return dictionaryHash.containsValue(value);
	}

	/**
	 * Get similar word with input from dictionary, default limit is 20 words
	 * 
	 * @param word
	 *            String word to get similar list
	 */
	public ArrayList<String> getSuggestions(String word) {
		ArrayList<String> lstSuggestion = new ArrayList<>();
		Map<String, Double> hashRanking = new LinkedHashMap<>();
		for (String str : dictionaryHash.values()) {
			hashRanking.put(str, StringSimilarity.calculateSimilarity(word, str));
		}
		hashRanking = MapUtil.sortByValueDescending(hashRanking);
		int i = 0;
		for (String str : hashRanking.keySet()) {
			lstSuggestion.add(str);
			i++;
			if (i == 20)
				break;
		}
		return lstSuggestion;
	}

	/**
	 * Get similar word with input from dictionary
	 * 
	 * @param word
	 *            String word to get similar list
	 * @param limit
	 *            Integer limit word to get, maximum 100 word
	 * @return
	 */
	public ArrayList<String> getSuggestions(String word, int limit) {
		ArrayList<String> lstSuggestion = new ArrayList<>();
		Map<String, Double> hashRanking = new LinkedHashMap<>();
		for (String str : dictionaryHash.values()) {
			hashRanking.put(str, StringSimilarity.calculateSimilarity(word, str));
		}
		hashRanking = MapUtil.sortByValueDescending(hashRanking);
		int i = 0;
		if (limit > 100)
			limit = 100;
		for (String str : hashRanking.keySet()) {
			lstSuggestion.add(str);
			i++;
			if (i == limit)
				break;
		}
		return lstSuggestion;
	}
}
