import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class WritePoetry {

    public String writePoem(String file, String startWord, int length, boolean printHashtable) {
        HashTable<String, WordFreqInfo> hashTable = new HashTable<>();
        HashTable<String, WordFreqInfo> hashTableComplete = generateHashTable(file, hashTable);

        if (printHashtable) {
            System.out.println(hashTableComplete.toString(hashTableComplete.size()));
        }

        StringBuilder poem = new StringBuilder();
        poem.append(startWord);
        String currentWordString = startWord;

        for (int i = 0; i < length; i++) {
            WordFreqInfo currentWord = hashTableComplete.find(currentWordString);
            int count = (int) (Math.random() * (currentWord.getOccurCount()-1));
            currentWordString = currentWord.getFollowWord(count);
            poem.append(" " + currentWordString);
        }




        return poem.toString();
    }

    private HashTable generateHashTable(String file, HashTable<String,WordFreqInfo> hashTable) {
        // first I need to read in the file one line at a time
        File poemFile = new File(file);
        try {
            Scanner input = new Scanner(poemFile);
            while (input.hasNextLine()) {
                //read the file one line at a time and insert the infor into the hash table
                String line = input.nextLine();

                String[] splitLine = line.split("[ ]");

                for (int i = 0; i < splitLine.length-1; i++) {
                    String word = splitLine[i];
                    String followingWord = splitLine[i+1];
                    followingWord = followingWord.replaceAll("[.?!,]", "");
                    word = word.toLowerCase();
                    followingWord = followingWord.toLowerCase();
                    Character lastChar = word.charAt(word.length()-1);
                    word = word.replaceAll("[?.,!]", "");

                    // special case when the word ends with punctuation
                    if (!Character.isLetter(lastChar)) {
                        // handle the special case
                        String punctuationMark = String.valueOf(lastChar);

                        if(hashTable.contains(punctuationMark)){
                            WordFreqInfo usedPunctuation = hashTable.find(punctuationMark);
                            usedPunctuation.updateFollows(followingWord);
                        } else {
                            WordFreqInfo newWordInfoPunctuation = new WordFreqInfo(punctuationMark, 0);
                            newWordInfoPunctuation.updateFollows(followingWord);
                            hashTable.insert(punctuationMark,newWordInfoPunctuation);
                        }

                        followingWord = String.valueOf(lastChar);

                    }

                    if (hashTable.contains(word)) {
                        WordFreqInfo usedWordInfo = hashTable.find(word);
                        usedWordInfo.updateFollows(followingWord);

                    } else {
                        WordFreqInfo newWordInfo = new WordFreqInfo(word, 0);
                        newWordInfo.updateFollows(followingWord);
                        hashTable.insert(word,newWordInfo);
                    }
                }


            }

        } catch (java.io.IOException ex) {
            System.out.println("An error occurred trying to read the poem: " + ex);
        }



        return hashTable;
    }


}
