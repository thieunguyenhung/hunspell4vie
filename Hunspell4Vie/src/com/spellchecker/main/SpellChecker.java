package com.spellchecker.main;

import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.spellchecker.hunspell.Hunspell;
import com.spellchecker.osenum.OSArchitecture;
import com.spellchecker.suggestion.VietnameseDictionary;

public class SpellChecker {
	private Hunspell.Dictionary dict = null;
	private Pattern pattern;
	private VietnameseDictionary vnDictionary = null;

	/**
	 * Constructor using your own native library and dictionary. Watch out for
	 * exception
	 * 
	 * @param nativeLibPath
	 *            - path to native library<br>
	 *            <b>Windows</b> : use .dll file<br>
	 *            <b>OS X</b> : use .jnilib file<br>
	 *            <b>Linux</b> (Ex: Ubuntu) : use .so file
	 * @param dictPath
	 *            path to language dictionary <b>FOLDER</b> (must contain .aff
	 *            and .dic file)<br>
	 *            Ex: path/to/dict/vi_VN for Vietnamese
	 */
	public SpellChecker(String nativeLibPath, String dictPath) {
		try {
			dict = Hunspell.getInstance(nativeLibPath).getDictionary(dictPath);
			vnDictionary = new VietnameseDictionary(dictPath + ".dic");
		} catch (Exception e) {
			e.printStackTrace();
		}
		initialize();
	}

	/**
	 * Constructor using included native library and your own dictionary. Watch
	 * out for dictionary file exception
	 * 
	 * @param os
	 *            Operation system which is running this library
	 * @param dictPath
	 *            path to language dictionary <b>FOLDER</b> (must contain .aff
	 *            and .dic file)<br>
	 *            Ex: path/to/dict/vi_VN for Vietnamese
	 */
	public SpellChecker(OSArchitecture os, String dictPath) {
		String nativeLibPath = Paths.get("").toAbsolutePath().toString() + "/native-lib/" + getNativeLibFile(os);
		try {
			dict = Hunspell.getInstance(nativeLibPath).getDictionary(dictPath);
			vnDictionary = new VietnameseDictionary(dictPath + ".dic");
			initialize();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Constructor using included native library and default dictionary
	 * (Vietnamese)
	 * 
	 * @param os
	 *            Operation system which is running this library
	 */
	public SpellChecker(OSArchitecture os) {
		String dictPath = Paths.get("").toAbsolutePath().toString() + "/dict/vi_VN";
		String nativeLibPath = Paths.get("").toAbsolutePath().toString() + "/native-lib/" + getNativeLibFile(os);
		try {
			dict = Hunspell.getInstance(nativeLibPath).getDictionary(dictPath);
			vnDictionary = new VietnameseDictionary(dictPath + ".dic");
			initialize();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private String getNativeLibFile(OSArchitecture os) {
		String libFileName = "";
		switch (os) {
		case LINUX_32:
			libFileName = "libhunspell-linux-x86-32.so";
			break;
		case LINUX_64:
			libFileName = "libhunspell-linux-x86-64.so";
			break;
		case WINDOWS_32:
			libFileName = "hunspell-win-x86-32.dll";
			break;
		case WINDOWS_64:
			libFileName = "hunspell-win-x86-64.dll";
			break;
		case MACOS_32:
			libFileName = "hunspell-darwin-x86-32.jnilib";
			break;
		case MACOS_64:
			libFileName = "hunspell-darwin-x86-64.jnilib";
			break;
		case MACOS_PPC32:
			libFileName = "hunspell-darwin-ppc-32.jnilib";
			break;
		}
		return libFileName;
	}

	private void initialize() {
		String regexVietnamese = "[a-zA-ZÀÁÂÃÈÉÊÌÍÒÓÔÕÙÚĂĐĨŨƠàáâãèéêìíòóôõùúăđĩũơƯĂẠẢẤẦẨẪẬẮẰẲẴẶẸẺẼỀỀỂưăạảấầẩẫậắằẳẵặẹẻẽếềềểỄỆỈỊỌỎỐỒỔỖỘỚỜỞỠỢỤỦỨỪễệỉịọỏốồổỗộớờởỡợụủứừỬỮỰỲỴÝỶỸửữựỳỵỷỹ]+";
		pattern = Pattern.compile(regexVietnamese);
	}

	/**
	 * Check if word is misspelled
	 * 
	 * @param word
	 *            word to check
	 */
	public boolean isMisspelled(String word) {
		return dict.misspelled(word);
	}

	/**
	 * Check and fix all misspelled words in a String
	 * 
	 * @param fileContent
	 *            - file content to correct
	 * @param useHunspellSuggestion
	 *            <br>
	 *            <code>true</code> if you want to use Hunspell default
	 *            suggestion algorithm.<br>
	 *            <code>false</code> if you want to use Hunspell4Vie suggestion
	 *            algorithm.
	 * @return file content with correct word
	 */
	public String checkSpell(String fileContent, boolean useHunspellSuggestion) {
		HashMap<String, String> hashErrorWords = new HashMap<>();
		Matcher m = pattern.matcher(fileContent);
		/*-Hunspell suggestion algorithm*/
		if (useHunspellSuggestion) {
			while (m.find()) {
				String word = m.group();
				if (!isAcronym(word)) {/*-Check if word is Acronym */
					if (dict.misspelled(word)) {/*-Check if misspelled*/
						List<String> lstSuggestion = dict.suggest(word);
						if (null != lstSuggestion && lstSuggestion.size() > 0) {
							hashErrorWords.put(word, lstSuggestion.get(0).toLowerCase());
						}
					}
				}
			}
		}
		/*-Hunspell4Vie suggestion algorithm*/
		else {
			while (m.find()) {
				String word = m.group();
				if (!isAcronym(word)) {/*-Check if word is Acronym */
					if (dict.misspelled(word)) {/*-Check if misspelled*/
						List<String> lstSuggestion = vnDictionary.getSuggestions(word);
						if (null != lstSuggestion && lstSuggestion.size() > 0) {
							hashErrorWords.put(word, lstSuggestion.get(0).toLowerCase());
						}
					}
				}
			}
		}
		fileContent = correctSpelling(fileContent, hashErrorWords);
		return fileContent;
	}

	/**
	 * Correct all misspelled word in a file or string by replacing it.
	 * 
	 * @param fileContent
	 *            - file content need to replace
	 * @param hashError
	 *            - Hash map contain misspelled and correct word respectively
	 * @return file content with correct word
	 */
	private String correctSpelling(String fileContent, HashMap<String, String> hashError) {
		for (Map.Entry<String, String> entry : hashError.entrySet()) {
			String key = entry.getKey();
			String value = entry.getValue();
			fileContent = replaceWord(fileContent, key, value);
		}
		return fileContent;
	}

	/**
	 * Check if a word is acronym
	 * 
	 * @param word
	 *            - word to check
	 */
	private boolean isAcronym(String word) {
		return word.matches("[0-9]*[A-Z0-9]+[0-9]*");
	}

	/**
	 * Replace a misspelled word by correct one, using pattern to match the
	 * misspelled
	 * 
	 * @param source
	 *            - original file content
	 * @param missSpelledWord
	 *            - misspelled word need to replace
	 * @param correctWord
	 *            - correct word
	 * @return file content which was replaced by one word
	 */
	private String replaceWord(String source, String missSpelledWord, String correctWord) {
		String pattern = "\\b" + missSpelledWord.toLowerCase() + "\\b";
		Pattern p = Pattern.compile(pattern);
		Matcher m = p.matcher(source.toLowerCase());
		String fileContentFixed = "";
		int lastIndex = 0;
		while (m.find()) {
			fileContentFixed += source.substring(lastIndex, m.start()) + correctWord;
			lastIndex = m.end();
		}
		fileContentFixed += source.substring(lastIndex, source.length());
		return fileContentFixed;
	}
}
