package termfrequncy;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

public class TermFrequencyRunner {
  private static final String FILES_FLAG_NAME = "files";
  private static final String TERMS_FLAG_NAME = "terms";

  public static void main(String[] args) {
    Map<String, Set<String>> flags = parseFlags(args);
    if (!flags.containsKey(FILES_FLAG_NAME) || !flags.containsKey(TERMS_FLAG_NAME)) {
      System.out.println("Usage run.sh --files=file1,file2,... terms=term1,term2...");
      return;
    }
    Set<String> terms = flags.get(TERMS_FLAG_NAME);
    Set<String> fileNames = flags.get(FILES_FLAG_NAME);

    Map<String, FileFrequency> fileFrequencyByTerm = new HashMap<>();
    for (String fileName : fileNames) {
      Path filePath = FileSystems.getDefault().getPath(fileName);
      Map<String, Double> termFrequencies = computeTermFrequencies(terms, filePath);
      for (Entry<String, Double> entry : termFrequencies.entrySet()) {
    	String term = entry.getKey();
    	Double frequency = entry.getValue();
        FileFrequency newFileFrequency = new FileFrequency(filePath, frequency);
        // If present, compare new FileFrequency to old FileFrequency.
        fileFrequencyByTerm
          .computeIfPresent(
            term,
            (key, oldFileFrequency) -> maxFileFrequncy(newFileFrequency, oldFileFrequency));
        // If absent, add new FileFrequency.
        fileFrequencyByTerm.putIfAbsent(term, newFileFrequency);
      }
    }
    System.out.println(format(fileFrequencyByTerm));
  }
  
  private static Map<String, Double> computeTermFrequencies(Set<String> terms, Path filePath) {
    Map<String, Integer> termCounts = terms
        .stream()
        .collect(Collectors.toMap(Function.identity(), a -> 0));
    int totalCount = 0;

    try (BufferedReader reader = Files.newBufferedReader(filePath)) {
      String line = null;
      while ((line = reader.readLine()) != null) {
        for (String word : splitAndCleanLine(line)) {
          termCounts.computeIfPresent(word, (key, count) -> count + 1);
          totalCount++;
        }
      }
    } catch (IOException e) {
    System.out.println(String.format("Problem reading file: %s. Omitting from results.", filePath));
    return new HashMap<>();
    }

    final double floatingPointCount = (double) totalCount;
    return termCounts
      .entrySet()
      .stream()
      .collect(Collectors.toMap(
        entry -> entry.getKey(),
        entry -> entry.getValue().doubleValue() / floatingPointCount));
  }

  private static List<String> splitAndCleanLine(String line) {
    List<String> words = new ArrayList<>();
    StringBuilder wordBuilder = new StringBuilder(); 
    for (char c : line.toCharArray()) {
      if (Character.isLetterOrDigit(c)) {
        char lower = Character.toLowerCase(c);
        wordBuilder.append(lower);
      } else if (Character.isWhitespace(c)) {
        if (wordBuilder.length() > 0) {
          words.add(wordBuilder.toString());
          wordBuilder = new StringBuilder();
        }
      }
    }
    return words;
  }

  private static FileFrequency maxFileFrequncy(FileFrequency tf1, FileFrequency tf2) {
    return tf1.frequency() > tf2.frequency() ? tf1 : tf2;
  }

  private static String format(Map<String, FileFrequency> fileFrequencyByTerm) {
    StringBuilder sb = new StringBuilder();
    for (Entry<String, FileFrequency> entry : fileFrequencyByTerm.entrySet()) {
      sb.append("Term: ");
      sb.append(entry.getKey());
      sb.append('\t');
      sb.append("File name: ");
      sb.append(entry.getValue().filePath().getFileName());
      sb.append('\t');
      sb.append("Frequency: ");
      sb.append(entry.getValue().frequency());
      sb.append('\n');
    }
    return sb.toString();
  }

  private static Map<String, Set<String>> parseFlags(String[] args) {
    return Arrays.stream(args)
      .map(Flag::parse)
      .collect(Collectors.toMap(flag -> flag.name, flag -> flag.values));
  }
}
