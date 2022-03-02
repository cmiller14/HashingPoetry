import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class WritePoetry {

    public String writePoem(String file, String startWord, int length, boolean printHashtable) {
        HashTable<String, WordFreqInfo> hashTable = new HashTable<>();
        ArrayList<String> poemText = new ArrayList<>();
        poemText = readFile(file,poemText);
        HashTable<String, WordFreqInfo> hashTableComplete = generateHashTable(hashTable,poemText);

        if (printHashtable) {
            System.out.println(hashTableComplete.toString(hashTableComplete.size()));
        }

        StringBuilder poem = new StringBuilder();
        poem.append(startWord + " ");
        String currentWordString = startWord;

        for (int i = 1; i < length; i++) {
            WordFreqInfo currentWord = hashTableComplete.find(currentWordString);
            int count = (int) (Math.random() * (currentWord.getOccurCount()-1));
            currentWordString = currentWord.getFollowWord(count);
            if (isPunctuation(currentWordString)) {
                poem.deleteCharAt(poem.length()-1);
                poem.append(currentWordString + "\n");
            } else {
                poem.append(currentWordString + " ");
            }
        }

        poem = addPeriod(poem);
        return poem.toString();
    }

    private boolean isPunctuation(String word) {
        Character lastChar = word.charAt(word.length()-1);
        if (Character.isLetter(lastChar)) {
            return false;
        } else {
            return true;
        }
    }

    private StringBuilder addPeriod(StringBuilder poem) {
        poem = poem.deleteCharAt(poem.length()-1);
        String poemString = poem.toString();
        Character lastChar = poemString.charAt(poemString.length()-1);
        if (Character.isLetter(lastChar)) {
            return poem.append(".");
        } else {
            return poem;
        }
    }

    private ArrayList<String> readFile(String file, ArrayList<String> poemText) {
        File poemFile = new File(file);

        try {
            Scanner input = new Scanner(poemFile);
            while (input.hasNextLine()) {
                //read the file one line at a time and insert the infor into the hash table
                String line = input.nextLine();
                String[] splitLine = line.split("[ ]");
                // add all the words and characters to the hash table array list
                for (int i = 0; i < splitLine.length; i++) {
                    //Check for punctuation
                    String word = splitLine[i];
                    if (!word.equals("")) {
                        Character lastChar = word.charAt(word.length()-1);
                        word = word.toLowerCase();
                        word = word.replaceAll("[?.,!]", "");
                        poemText.add(word);
                        if (!Character.isLetter(lastChar) && word.length() > 1) {
                            poemText.add(String.valueOf(lastChar));
                        }
                    }
                }
            }
        } catch (java.io.IOException ex) {
            System.out.println("An error occurred trying to read the poem: " + ex);
        }
        return poemText;
    }

    private HashTable generateHashTable(HashTable<String,WordFreqInfo> hashTable, ArrayList<String> words) {
                //iterate through the array list and add everything to the hash table
        for (int i = 0; i < words.size()-1; i++) {
            String word = words.get(i);
            String followingWord = words.get(i+1);

            if (hashTable.contains(word)) {
                WordFreqInfo usedWordInfo = hashTable.find(word);
                usedWordInfo.updateFollows(followingWord);

            } else {
                WordFreqInfo newWordInfo = new WordFreqInfo(word, 0);
                newWordInfo.updateFollows(followingWord);
                hashTable.insert(word,newWordInfo);
            }
        }
        return hashTable;
    }


}
