package termfrequncy;

import java.nio.file.Path;

public class FileFrequency {
  private final Path filePath;
  private final double frequency;
  
  FileFrequency(Path filePath, double frequency) {
    this.filePath = filePath;
    this.frequency = frequency;
  }
  
  public Path filePath() {
    return filePath;
  }
  
  public double frequency() {
    return frequency;
  }
  
  @Override
  public boolean equals(Object other) {
    if (other instanceof FileFrequency) {
      FileFrequency otherFf = (FileFrequency) other;
      return this.filePath == otherFf.filePath && this.frequency == otherFf.frequency;  
    }
    return false;
  }
  
  @Override
  public int hashCode() {
    return filePath.hashCode() * 103 + Double.hashCode(frequency);
  }
}
