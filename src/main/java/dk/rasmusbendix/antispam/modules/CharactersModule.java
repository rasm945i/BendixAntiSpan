package dk.rasmusbendix.antispam.modules;

import dk.rasmusbendix.antispam.Message;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashMap;

@AllArgsConstructor
public class CharactersModule extends ChatModule {

    @Getter @Setter private int maxRepetitiveChars; // Max amount of times a single character can be after itself

    @Override
    public boolean allowChatEvent(Message message, ArrayList<Message> history) {

        int repetitiveChars = countRepetitiveChars(message.getContent());

        if(repetitiveChars < maxRepetitiveChars)
            return true;

        // Exceeded max repetitive chars
        message.flag(this);

        return hasAcceptableHistory(message, history);

    }

    // The max times one (any) character is used in a row
    public int countRepetitiveChars(String message) {
        int highscore = 0;
        int count = 0;
        char[] chars = message.toLowerCase().toCharArray();
        char previous = chars[0];
        for (int i = 0; i < chars.length; i++) {
            if(chars[i] == previous) {
                count++;
            } else {
                if(count > highscore)
                    highscore = count;
                previous = chars[i];
            }
        }

        if(count > highscore)
            highscore = count;

        return highscore;

    }


    // Find the most used character and return how many times it was used
    // This method was a mistake, but I don't want to delete it :(
    public int countChars(String message) {

        char[] chars = message.toLowerCase().toCharArray();

        HashMap<Character, Integer> charCount = new HashMap<>();

        for(char ch : chars) {
            charCount.put(ch, charCount.getOrDefault(ch, 0) + 1);
        }

        char mostUsed = message.charAt(0);

        for(char key : charCount.keySet()) {
            if(charCount.get(key) > charCount.get(mostUsed)) {
                mostUsed = key;
            }
        }

        return charCount.get(mostUsed);

    }

}