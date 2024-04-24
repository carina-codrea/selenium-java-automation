package codrea.testComponents;

import org.apache.commons.lang3.RandomStringUtils;
import java.util.concurrent.ThreadLocalRandom;

public class RandomGenerator {
    public static String generateInvalidPassword(){
        String password;
        do {
            password = RandomStringUtils.randomAlphanumeric(10); // Generate a random password
        } while (meetsAllCriteria(password)); // Keep generating passwords until an invalid one is found
        return password;
    }

    public static String generateValidPassword() {
        String password = "";

        // Generate random password until it meets all criteria
        while (!meetsAllCriteria(password)) {
            password = RandomStringUtils.random(10, true, true);
        }
        // Append a special character '@' at the end
        password += "@";
        return password;
    }

    public static String generateShortPassword(){
        return RandomStringUtils.randomAlphanumeric(ThreadLocalRandom.current().nextInt(1, 8));
    }
    public static String generateEmail(){
        String[] providers = {"@yahoo.com", "@gmail.com"};
        return RandomStringUtils.randomAlphanumeric(10) + providers[RandomStringUtils.random(1).charAt(0) % providers.length];
    }

    public static String generateValidPhoneNumber() {
        return RandomStringUtils.randomNumeric(10);
    }


    public static String generateInvalidPhoneNumber(boolean characters,boolean shortPhoneNumber ){
        if (characters)
            return RandomStringUtils.randomAlphanumeric(10);
        else if (shortPhoneNumber)
            return RandomStringUtils.randomNumeric(1,9);
        else
            return RandomStringUtils.randomNumeric(11,15);
    }


    private static boolean meetsAllCriteria(String password) {
        boolean hasNumeric = false;
        boolean hasUppercase = false;
        boolean hasLowercase = false;

        for (char c : password.toCharArray()) {
            if (Character.isDigit(c)) {
                hasNumeric = true;
            } else if (Character.isUpperCase(c)) {
                hasUppercase = true;
            } else if (Character.isLowerCase(c)) {
                hasLowercase = true;
            }
        }

        return hasNumeric && hasUppercase && hasLowercase;
    }
    }

