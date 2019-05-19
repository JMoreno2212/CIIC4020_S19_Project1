package edu.uprm.ece.icom4035.polynomial;

import java.util.Iterator;
import java.util.StringTokenizer;

import edu.uprm.ece.icom4035.list.List;
import edu.uprm.ece.icom4035.list.ListFactory;

public class PolynomialImp implements Polynomial {
	private ListFactory<Term> factory = TermListFactory.newListFactory();
	private List<Term> list;

	// We assume Polynomial received is on it's standard form
	public PolynomialImp(String polynomial) {
		this.list = factory.newInstance();
		createTerms(polynomial);
		//PolynomialImp.insertionSort(this.list);	// If not the case, we can call the sorter here
	}

	//This constructor creates an empty list to work with
	public PolynomialImp() {
		this.list = factory.newInstance();
	}

	//This constructor takes any list of terms, adds it to the Polynomial and sorts it
	public PolynomialImp(List<Term> listOfTerms) {
		this.list = listOfTerms;
		PolynomialImp.insertionSort(this.list);
	}

	@Override
	public Iterator<Term> iterator() {
		return list.iterator();
	}

	@Override
	public String toString() {
		StringBuilder result = new StringBuilder(); // Creating the String using StringBuilder for efficiency
		boolean firstTerm = true;
		for(Term term : this) {
			if(!firstTerm) result.append("+"); // If this is the first term we do not add a + before it.
			firstTerm = false;
			result.append(((TermImp) term).toString());	// Calls toString() method of each term (see TermImp Class)
		}
		return result.toString();	
	}

	@Override
	public Polynomial add(Polynomial P2) {
		PolynomialImp result = new PolynomialImp();
		for(Term term : this) { // For-each loop that adds all terms of our current Polynomial to the result.
			result.list.add(new TermImp(term.getCoefficient(), term.getExponent()));
		}
		
		for(Term p2Term : P2) { // Iteration over the second polynomial
			boolean wasAdded = false; 
			for(Term term : this.list) { // Iteration over the current polynomial to check for similar terms
				if(term.getExponent() == p2Term.getExponent()) {
					((TermImp) result.list.get(this.list.firstIndex(term))).setCoefficient(
							term.getCoefficient() + p2Term.getCoefficient());
					wasAdded = true; 
				}
			}
			// We check if the term was added. If not, that means there are no similar terms so we add it ourselves
			if(!wasAdded) result.list.add(p2Term);
		}

		PolynomialImp.insertionSort(result); // Sort the elements in decreasing order
		return result;
	}

	@Override
	public Polynomial subtract(Polynomial P2) {
		if(this.equals(P2)) return new PolynomialImp("0"); // Special Case where both polynomials are the same. Returns 0.
		if(this.equals(new PolynomialImp("0"))) { // Special Case where current polynomial is 0.
			return new PolynomialImp(P2.multiply(-1.00).toString()); // Returns the negative of the second polynomial
		}
		// If not a special case, this adds the negative of the second polynomial to the first one
		return new PolynomialImp(this.add(P2.multiply(-1.00)).toString());					   
	}

	@Override
	public Polynomial multiply(Polynomial P2) {
		// Testing for special case where any of the polynomials is 0.
		if(this.equals(new PolynomialImp("0")) || ((PolynomialImp) P2).equals(new PolynomialImp("0"))) 
			return new PolynomialImp("0"); // Returns 0
		// Temporary List where all values will be stored but no similar terms will be combined
		List<Term> tempResult = this.factory.newInstance();
		for(Term term : this) { // Loops that add all the terms
			for(Term p2Term : P2) {
				tempResult.add(new TermImp(term.getCoefficient() * p2Term.getCoefficient(), 
						term.getExponent() + p2Term.getExponent()));
			}
		}
		PolynomialImp.insertionSort(tempResult); // Sorting the list
		
		PolynomialImp result = new PolynomialImp(); // Final result
		double coefficient = 0;
		int exponent = tempResult.first().getExponent();
		Iterator<Term> iter = tempResult.iterator(); // Preparing to iterate over the Temporary List we have
		
		/* 
		 * Since the list is sorted by degree, that means all terms with similar exponents will be next to each other.
		 * This allows us to check exponents until we find a different one and add all the corresponding coefficients.
		 */
		while(iter.hasNext()) {
			Term term = iter.next();
			if(term.getExponent() == exponent) 
				coefficient += term.getCoefficient(); // If exponents are the same we just sum the coefficients 
			else { // When exponents are not the same we add a new term with the current values
				result.list.add(new TermImp(coefficient, exponent));
				coefficient = term.getCoefficient();
				exponent = term.getExponent();
			}
			// This ensures the last term is added to the result
			if(!(iter.hasNext())) result.list.add(new TermImp(coefficient, exponent));
		}		
		return result;
	}

	@Override
	public Polynomial multiply(double c) {
		if(c == 0.0) return new PolynomialImp("0"); // Special case if the constant is 0. Returns 0.
		PolynomialImp result = new PolynomialImp(); 
		for(Term term : this) {
			Term newTerm = new TermImp(term.getCoefficient() * c, term.getExponent()); // Multiplying coefficients times c
			result.list.add(newTerm);
		}
		return result;
	}

	@Override
	public Polynomial derivative() {
		if(this.degree() == 0) return new PolynomialImp("0"); //Special case where we only have constant values. Returns 0.
		PolynomialImp result = new PolynomialImp();
		for(Term term : this) {
			//Verifies is the term is not a constant and uses the Power Rule to obtain its derivative.
			if(term.getCoefficient() * term.getExponent() != 0.00) result.list.add(new 
					TermImp(term.getCoefficient() * term.getExponent(), term.getExponent() - 1));
		}		
		return result;
	}

	@Override
	public Polynomial indefiniteIntegral() {
		PolynomialImp result = new PolynomialImp();
		for(Term term : this) {
			// Obtains the Anti-Derivative of the corresponding term
			result.list.add(new TermImp(term.getCoefficient() / (term.getExponent() + 1.00), term.getExponent() + 1));
		}
		result.list.add(new TermImp(1.00, 0)); // Adds a 1 at the end, simulating the constant c that appears
		return result;

	}

	@Override
	public double definiteIntegral(double a, double b) {
		// Evaluates the Indefinite Integral on both numbers and returns the difference
		return this.indefiniteIntegral().evaluate(b) - this.indefiniteIntegral().evaluate(a);
	}

	@Override
	public int degree() {
		// Since the list will always be sorted we only need to get the exponent of the first term
		return this.list.first().getExponent();
	}

	@Override
	public double evaluate(double x) {
		double result = 0;
		for(Term term : this) { // For-each loop that calls the evaluate() method of each term and adds all results
			result += term.evaluate(x);
		}
		return result;
	}

	@Override
	public boolean equals(Polynomial P) {
		Iterator<Term> iter = P.iterator();
		// For efficiency purposes we first check if both Polynomials have the same amount of terms
		if(this.list.size() != ((PolynomialImp)P).list.size()) return false;
		for(Term term : this) { // If they have the same amount we loop through both simultaneously comparing each term
			if(!term.equals(iter.next())) return false;
		}
		return true;
	}
	
	/* 
	 * The following private function breaks the string every time it finds a "+" and parses the coefficient and exponent
	 * into the respective number values. 
	 */
	public void createTerms(String polynomial) {
		StringTokenizer tokens = new StringTokenizer(polynomial, "+"); // Breaks terms, after finding "+"
		while(tokens.hasMoreTokens()) {
			String term = tokens.nextToken();
			double coefficient;
			int exponent;
			if(term.contains("x^")) { // This ensures that the term contains an exponent
				if(term.startsWith("x")) {coefficient = 1;} // This checks if the coefficient is 1
				else if(term.startsWith("-x")) {coefficient = -1;} // This checks if the coefficient is -1
				else{coefficient = Double.parseDouble(term.substring(0, term.indexOf("x")));}
				exponent = Integer.parseInt(term.substring(term.indexOf("^") + 1));
			}
			else if(term.contains("x")) { // This ensures the exponent is 1
				if(term.startsWith("x")) {coefficient = 1;}
				else if(term.startsWith("-x")) {coefficient = -1;}
				else{coefficient = Double.parseDouble(term.substring(0, term.indexOf("x")));}
				exponent = 1;
			}
			else { // This means the exponent is 0 (term is a constant)
				coefficient = Double.parseDouble(term);
				exponent = 0;
			}
			list.add(new TermImp(coefficient, exponent)); // Adds the new Instances to the current Polynomial List
		}
	}
	
	// Re-using Insertion Sort Code from a previous course. This will sort the polynomial by their exponents
	public static void insertionSort(List<Term> listOfTerms) {
		// This first loop verifies is any coefficient is 0 and removes it only if there are more terms (list won't end up empty)
		for(int i = 0; i < listOfTerms.size(); i++) {
			if(listOfTerms.get(i).getCoefficient() == 0 && listOfTerms.size() - 1 != 0) {
				listOfTerms.remove(i);
			}
		}
		
		for (int i = 1; i < listOfTerms.size() - 1; i++) { // Actual exponent sorting of terms
			int j = i + 1;
			while ((j >= 1) && (listOfTerms.get(j).getExponent() > listOfTerms.get(j-1).getExponent())) {
				Term temp = listOfTerms.get(j);
				listOfTerms.set(j, listOfTerms.get(j - 1));
				listOfTerms.set(j-1, temp);
				j--;
			}
		}
	}

	// This insertion sort takes a Polynomial as a parameter and accesses it's list to sort it
	public static void insertionSort(Polynomial polynomial) {
			insertionSort(((PolynomialImp) polynomial).list);
	}
}