package org.example;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

public class DuplicateChecker {
    public void findDuplicateTxtFiles(String directoryPath) throws IOException{
        MultiValueMap<String, String> duplicates = new LinkedMultiValueMap<>();
        File directory = new File(directoryPath);

        if (!directory.isDirectory()) throw new IOException("Not a directory");

        List<File> txtFiles = listTxtFiles(directory);
        for (File file : txtFiles) {
            try {
                byte[] fileContent = Files.readAllBytes(file.toPath());
                String hash = generateHash(fileContent);
                duplicates.add(hash, file.getName());
            } catch (IOException e) {
                throw new IOException("File reading error:" + file.getName());
            }
        }
        duplicates = filterDuplicates(duplicates);
        for (Collection<String> value : duplicates.values()) {
            System.out.println(value.toString().substring(1, value.toString().length() - 1));
        }
    }

    public List<File> listTxtFiles(File directory) {
        List<File> txtFiles = new ArrayList<>();
        File[] files = directory.listFiles();

        if (files != null) {
            for (File file : files) {
                if (file.isFile() && file.getName().toLowerCase().endsWith(".txt")) txtFiles.add(file);
                else if (file.isDirectory()) txtFiles.addAll(listTxtFiles(file));
            }
        }

        return txtFiles;
    }

    public String generateHash(byte[] content) throws RuntimeException {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
            byte[] hash = messageDigest.digest(content);
            return Arrays.toString(hash);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error while generating hash", e);
        }
    }

    private MultiValueMap<String, String> filterDuplicates(MultiValueMap<String, String> duplicates) {
        MultiValueMap<String, String> filteredDuplicates = new LinkedMultiValueMap<>();

        for (String hash : duplicates.keySet()) {
            List<String> files = duplicates.get(hash);
            if (files.size() > 1) {
                filteredDuplicates.put(hash, files);
            }
        }

        return filteredDuplicates;
    }
}
