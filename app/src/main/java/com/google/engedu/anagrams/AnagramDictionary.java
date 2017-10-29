package com.google.engedu.anagrams;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

public class AnagramDictionary {

    private static final int MIN_NUM_ANAGRAMS = 5;
    private static final int DEFAULT_WORD_LENGTH = 3;
    private static final int MAX_WORD_LENGTH = 6;
    private Random random = new Random();
    private ArrayList<String> wordList = new ArrayList<>();
    private HashMap<String, ArrayList<String>> lettersToWord = new HashMap<>();
    private HashSet<String> wordSet = new HashSet<>();
    private HashMap<Integer, ArrayList<String>> sizeToWord = new HashMap<>();
    private int wordLength = DEFAULT_WORD_LENGTH - 1;

    public AnagramDictionary(Reader reader) throws IOException {
        BufferedReader in = new BufferedReader(reader);
        String line;
        while((line = in.readLine()) != null) {
            String word = line.trim();
            int wordSize = word.length();
            wordList.add(word);
            String sortedWord = sortLetters(word);

            // Add word to Sorted Letters->Words Map
            if (lettersToWord.containsKey(sortedWord)) {
                lettersToWord.get(sortedWord).add(word);
            } else {
                ArrayList<String> words = new ArrayList<>();
                words.add(word);
                lettersToWord.put(sortedWord, words);
            }

            // Add word to Word Length -> Words Map
            if (sizeToWord.containsKey(wordSize)) {
                sizeToWord.get(wordSize).add(word);
            } else {
                ArrayList<String> words = new ArrayList<>();
                words.add(word);
                sizeToWord.put(wordSize, words);
            }

            // Add word to the word set
            wordSet.add(word);
        }
    }

    public boolean isGoodWord(String word, String base) {
        if (!wordSet.contains(word) || word.contains(base)) {
            return false;
        }
        return true;
    }

    public List<String> getAnagrams(String targetWord) {
        /*
         -- WITHOUT USING HashMap, Time consuming
        ArrayList<String> result = new ArrayList<String>();
        for (String word : wordList) {
            if (word.length() == targetWord.length()
                    && sortLetters(word).equals(sortLetters(targetWord))) {
                result.add(word);
            }
        }
        word.length() < DEFAULT_WORD_LENGTH
        */

        return lettersToWord.get(sortLetters(targetWord));
    }

    private String sortLetters(String word) {
        char [] wordArray = word.toCharArray();
        Arrays.sort(wordArray);
        return new String(wordArray);
    }

    public List<String> getAnagramsWithOneMoreLetter(String word) {
        ArrayList<String> result = new ArrayList<String>();
        List wordAnagram;
        for (char letter = 'a'; letter <= 'z'; letter++) {
            wordAnagram = getAnagrams(word + letter);
            if (wordAnagram != null) {
                result.addAll(wordAnagram);
            }
        }
        return result;
    }

    public String pickGoodStarterWord() {
        if (wordLength < MAX_WORD_LENGTH) {
            wordLength++; // Increase difficulty level at each turn
        }
        ArrayList<String> requiredWords = sizeToWord.get(wordLength);
        int listSize = requiredWords.size();
        int index = random.nextInt(listSize);
        while (true) {
            String word = requiredWords.get(index);
            if (word.length() >= DEFAULT_WORD_LENGTH
                    && lettersToWord.get(sortLetters(word)).size() >= wordLength) {
                return word;
            }
            index = (index + 1) % listSize;
        }
    }
}
