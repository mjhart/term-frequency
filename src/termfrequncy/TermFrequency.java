package termfrequncy;

public class TermFrequency {
	private final String term;
	private final double frequency;
	
	TermFrequency(String term, double frequency) {
		this.term = term;
		this.frequency = frequency;
	}
	
	public String term() {
		return term;
	}
	
	public double frequency() {
		return frequency;
	}
	
	@Override
	public boolean equals(Object other) {
		if (other instanceof TermFrequency) {
			TermFrequency otherTf = (TermFrequency) other;
			return this.term == otherTf.term && this.frequency == otherTf.frequency;  
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		return term.hashCode() * 103 + Double.hashCode(frequency);
	}
}
