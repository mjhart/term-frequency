package termfrequncy;

import java.nio.file.Path;

public class FileFrequency {
	private final Path fileName;
	private final double frequency;
	
	FileFrequency(Path filePath, double frequency) {
		this.fileName = filePath;
		this.frequency = frequency;
	}
	
	public Path filePath() {
		return fileName;
	}
	
	public double frequency() {
		return frequency;
	}
	
	@Override
	public boolean equals(Object other) {
		if (other instanceof FileFrequency) {
			FileFrequency otherFf = (FileFrequency) other;
			return this.fileName == otherFf.fileName && this.frequency == otherFf.frequency;  
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		return fileName.hashCode() * 103 + Double.hashCode(frequency);
	}
}
