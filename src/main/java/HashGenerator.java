import java.io.BufferedReader;
import java.io.FileReader;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.io.FileWriter;
import java.io.IOException;

public class HashGenerator {

    private static final String HASHES_FILE_PATH = "src/main/resources/hashes.txt";

    private static String generateHash(String input) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(input.getBytes());
            StringBuilder hexString = new StringBuilder();

            for (byte hashByte : hashBytes) {
                String hex = Integer.toHexString(0xff & hashByte);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }

            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static boolean isDuplicate(String inputString, String hashedString) {
        try (BufferedReader reader = new BufferedReader(new FileReader(HASHES_FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.contains(inputString) || line.contains(hashedString)) {
                    return true;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static void saveToTxtFile(String inputString, String hashedString) {
        if (!isDuplicate(inputString, hashedString)) {
            try (FileWriter writer = new FileWriter(HASHES_FILE_PATH, true)) {
                writer.write("Input String: " + inputString + "\n");
                writer.write("SHA-256 Hash: " + hashedString + "\n\n");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void starter(String input) {
        String hashedString = generateHash(input);
        saveToTxtFile(input, hashedString);
        System.out.println("Hash generated and saved to hashes.txt.");
    }
}