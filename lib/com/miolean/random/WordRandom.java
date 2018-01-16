package com.miolean.random;

import java.util.Random;

public class WordRandom {
    private static Random generator;

    public WordRandom(long seed) {
        generator = new Random(seed);
    }

    public WordRandom() {
        generator = new Random();
    }

    public String nextParagraph(int words) {
        String result = "\t";
        int randomSentenceWords;
        while(words > 0) {
            if(words < 15) {
                result += nextSentence(words);
                words = 0;
            }
            else {
                randomSentenceWords = generator.nextInt(11) + 5;
                result += nextSentence(randomSentenceWords);
                words -= randomSentenceWords;
            }
        }
        result += "\n";
        return result;
    }
    public String nextSentence(int words) {
        String result = "";
        for(int i = 0; i <= words; i++) {
            result += nextWord(generator.nextInt(7) / 3 + 1);
            result = (result.substring(0, 1).toUpperCase()) + result.substring(1, result.length());
            if(generator.nextInt(7) == 0 && ! (i == words)) result += ",";
            else if(i == words) result += ".";
            result += " ";
        }
        return result;
    }

    public String nextWord() {
        int[] syllableWeights = {0, 1, 6, 14, 19, 20};
        int r = generator.nextInt(20);
        for(int i = 0; i < syllableWeights.length; i++) {
            if(r < syllableWeights[i]) return nextWord(i);
        }
        return nextWord(2); //If all else fails, return a 2-syllable word
    }

    public String nextWord(int syllables) {
        int wordLength = syllables * 2;
        if(wordLength % 2 == 1) wordLength--;
        String result = "";
        for(int i = 0; i < wordLength; i++) {
            if(i % 2 == 0) result += randomConsonantSound();
            else result += randomVowelSound();
        }
        result += randomLastConsonantSound();
        return result;
    }
    public String randomVowelSound() {
        switch (generator.nextInt(20)) {
            default: return "a";

            case 0: return "a";
            case 1: return "e";
            case 2: return "i";
            case 3: return "o";
            case 4: return "u";
            case 5: return "ai";
            case 6: return "ou";
            case 7: return "ee";
            case 8: return "oi";
            case 9: return "ea";
            case 10: return "a";
            case 11: return "e";
            case 12: return "i";
            case 13: return "o";
            case 14: return "u";
            case 15: return "a";
            case 16: return "e";
            case 17: return "i";
            case 18: return "o";
            case 19: return "a";
            case 20: return "e";
        }
    }
    public String randomConsonantSound() {
        switch (generator.nextInt(76)) {
            default: return "b";

            case 0: return "b";
            case 1: return "c";
            case 2: return "d";
            case 3: return "f";
            case 4: return "f";
            case 5: return "g";
            case 6: return "h";
            case 7: return "j";
            case 8: return "k";
            case 9: return "l";
            case 10: return "m";
            case 11: return "n";
            case 12: return "p";
            case 13: return "qu";
            case 14: return "r";
            case 15: return "s";
            case 16: return "t";
            case 17: return "v";
            case 18: return "w";
            case 19: return "y";
            case 20: return "z";
            case 21: return "bl";
            case 22: return "br";
            case 23: return "pr";
            case 24: return "dr";
            case 25: return "fl";
            case 26: return "cl";
            case 27: return "gl";
            case 28: return "sl";
            case 29: return "cr";
            case 30: return "pl";
            case 31: return "fr";
            case 32: return "gr";
            case 33: return "tr";
            case 34: return "sc";
            case 35: return "sk";
            case 36: return "st";
            case 37: return "sw";
            case 38: return "sn";
            case 39: return "sm";
            case 40: return "wh";
            case 41: return "str";
            case 42: return "sh";
            case 43: return "th";
            case 44: return "tw";
            case 45: return "wr";
            case 46: return "sch";
            case 47: return "shr";
            case 48: return "sph";
            case 49: return "scr";
            case 50: return "spl";
            case 51: return "spr";
            case 52: return "squ";
            case 53: return "thr";
            case 54: return "b";
            case 55: return "d";
            case 56: return "f";
            case 57: return "g";
            case 58: return "l";
            case 59: return "m";
            case 60: return "n";
            case 61: return "p";
            case 62: return "r";
            case 63: return "s";
            case 64: return "t";
            case 65: return "b";
            case 66: return "d";
            case 67: return "f";
            case 68: return "g";
            case 69: return "l";
            case 70: return "m";
            case 71: return "n";
            case 72: return "p";
            case 73: return "r";
            case 74: return "s";
            case 75: return "t";
        }
    }
    private String randomLastConsonantSound() {
        switch (generator.nextInt(15)) {
            default: return "b";

            case 0: return "b";
            case 1: return "ck";
            case 2: return "d";
            case 3: return "f";
            case 4: return "g";
            case 5: return "ck";
            case 6: return "l";
            case 7: return "m";
            case 8: return "n";
            case 9: return "p";
            case 10: return "r";
            case 11: return "s";
            case 12: return "t";
            case 13: return "w";
            case 14: return "z";
        }
    }
}