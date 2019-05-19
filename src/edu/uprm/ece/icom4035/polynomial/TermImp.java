package edu.uprm.ece.icom4035.polynomial;

public class TermImp implements Term {
	private double coefficient;
	private int exponent;

	public TermImp(double coefficient, int exponent) {
		this.coefficient = coefficient;
		this.exponent = exponent;
	}

	@Override
	public double getCoefficient() {return coefficient;}
	public void setCoefficient(double newCoefficient) {this.coefficient = newCoefficient;}

	@Override
	public int getExponent() {return exponent;}
	public void setExponent(int newExponent) {this.exponent = newExponent;}

	@Override
	public boolean equals(Object obj) {
		if(!(obj instanceof Term)) return false;
		Term term = (Term) obj;
		// Comparing the coefficient and exponent of the Term to determine if they are equal or not
		return this.getCoefficient() == term.getCoefficient() && this.getExponent() == term.getExponent();
	}

	@Override
	public double evaluate(double x) {
		return coefficient * Math.pow(x, exponent);

	}
	
	//This method converts the term to a string, which is used in the PolynomialImp toString() method
	public String toString() {
		StringBuilder result = new StringBuilder(); // StringBuilder for efficiency purposes
		if(coefficient > 1 || coefficient < 0) { // This checks for any coefficient that has a number and/or a negative sign
			result.append(String.format("%.2f", coefficient)); // Here we format it to have 2 decimal places
			if(exponent > 1) {
				result.append("x^");
				result.append(Integer.toString(exponent));
			}
			else if(exponent == 1) result.append("x"); // In case of exponent one, we don't need the "^"
		}
		else { // This only activates when coefficient is 1 or 0, but 0 will eventually be removed
			if(exponent > 1) {
				result.append("x^");
				result.append(Integer.toString(exponent));
			}
			else if(exponent == 1) result.append("x");
			else result.append(String.format("%.2f", coefficient)); // This only activates when the term is a constant
		}
		return result.toString(); // Converting StringBuilder to String
	}

}
