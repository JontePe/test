package lab3;

import lincalc.LinCalcJohn;

import java.util.Scanner;

public class LinCalc {

	static StringStack stack = new StringStack();
	static StringQueue queue = new StringQueue();
	static String operatorer = "+-*/()";
	static String operander = "0123456789.,";

	public static void printArray(String[] array) {
		StringBuffer sb = new StringBuffer();
		sb.append("[");
		for (int i = 0; i < array.length; i++) {
			sb.append(array[i]);
			sb.append(", ");
		}
		// Replace the last ", " with "]"
		sb.replace(sb.length() - 2, sb.length(), "]");
		System.out.println(sb);
	}

	public static void main(String[] args) {
		Scanner in = new Scanner(System.in);
		String expression;

		double result;

		System.out.print("Enter expression: ");
		expression = in.nextLine();
		do {
			result = evaluate(expression);
			System.out.println("Result was: " + result);
			System.out.print("Enter expression: ");
		} while (!"".equals(expression = in.nextLine()));
	}

	// Räknar ut uttrycket
	//Jonathans calc-metod

	public static double calc(String[] lexedPostfix) {
		int count = -1;
		double[] hold = new double[500];

		for (int i = 0; i < lexedPostfix.length; i++) {
			String s = lexedPostfix[i];
			double result, tal1, tal2;

			
			//Skickar de två operanderna och operatorn till calculate metoden för att räkna
			if (isOperator(s)) {
				tal2 = (hold[count--]);
				tal1 = (hold[count--]);
				result = calculate(tal1, tal2, s);

				hold[++count] = result;
			} else {
				
				//Gör om operand till double och lägger till i array
				hold[++count] = Double.parseDouble(s);
			}

		}

		return hold[count];
	
	
	
		//Feraidons calc-metod
		
	/*for (int i = 0; i < lexedPostfix.length; i++) {
		double result;
		double x;
		double y;



		if(isOperator(lexedPostfix[i])){

		if (lexedPostfix[i].equals("+")) {
		x = Double.valueOf(stack.pop());				//  man vill inte skicka in string utan man måste skicka in ett tal
		y = Double.valueOf(stack.pop());
		result = y + x; 								
		stack.push(String.valueOf(result));				// man vill skicka in string
		}
		if (lexedPostfix[i].equals("-")) {
		x = Double.valueOf(stack.pop());
		y = Double.valueOf(stack.pop());
		result = y - x;
		stack.push(String.valueOf(result));
		}
		if (lexedPostfix[i].equals("*")) {
		x = Double.valueOf(stack.pop());
		y = Double.valueOf(stack.pop());
		result = x*y;
		stack.push(String.valueOf(result));
		}if (lexedPostfix[i].equals("/")) {
		x = Double.valueOf(stack.pop());
		y = Double.valueOf(stack.pop());
		result = y / x;
		stack.push(String.valueOf(result));
		}
		} else
		stack.push(lexedPostfix[i]);

		}


		return Double.valueOf(stack.pop());*/

	}

	// Skriver om uttrycket i postfix-notation

	public static String[] toPostfix(String[] inputData) {

		for (int i = 0; i < inputData.length; i++) {
			String current = inputData[i];

		

			// Fyller stack och queue med rätt symboler
			// Kontrollerar prioritet för att avgöra när operatorerna ska in i kö

			if (isOperator(current) && !current.equals("(")) {

				if (stack.isEmpty())
					stack.push(current);

				else if (!stack.isEmpty() && ((priority(current) > priority(stack.peek())) && !current.equals(")")))
					stack.push(current);
				else if (current.equals(")")) {
					
					//Tömmer stacken på allt som finns mellan parenteserna, slänger sedan bort parenteserna
					while (!stack.peek().equals("(") && !stack.peek().equals("-(")) {
						queue.enQueue(stack.pop());
					}
					
					//Specialfall för negativ parentes, multiplicerar parentesens innehåll med minus ett
					if (stack.peek().equals("-(")) {
						queue.enQueue("-1");
						queue.enQueue("*");
					}

					stack.pop();

				} else {
					
					//Lägger alla operatorer med högre prioritet än current i kön, lägger sedan current i stacken
					while (priority(current) <= priority(stack.peek()))
						queue.enQueue(stack.pop());

					stack.push(current);

				}
			}

			
			//En vänsterparentes ska alltid pushas på stacken
			else if (current.equals("-(") || current.equals("(")) {

				stack.push(current);

			}

			//Om current är en operand
			else {
				queue.enQueue(current);
			}
		}

		//Tömmer stacken då in-array är tom
		while (!stack.isEmpty()) {
			queue.enQueue(stack.pop());
		}

		return splitQueue(queue, inputData);

	}

	public static double evaluate(String expression) {
		String[] lexedInfix = lex(expression);
		String[] lexedPostfix = toPostfix(lexedInfix);
		return calc(lexedPostfix);
	}

	// Delar upp uttrycket med hjälp utav metoden split, till lexed String i en
	// array

	public static String[] lex(String expr) {

		expr = expr.replaceAll(",", ".");

		String[] arrayString = new String[expr.length()];

		arrayString = (split(expr));

		// Tar bort eventuella null som tillkommit då unära minus ingått

		int k = 0;
		do {
			k++;

		} while (k < arrayString.length && arrayString[k] != null);

		String[] lexComplete = new String[k];

		for (int i = 0; i < k; i++) {
			lexComplete[i] = arrayString[i];
		}

		return lexComplete;

	}

	// Utför räknesättet som motsvarar operatorn

	public static double calculate(double a, double b, String c) {
		switch (c) {
		case "+":
			return a + b;
		case "-":
			return a - b;
		case "*":
			return a * b;
		case "/":
			return a / b;
		default:
			return 0;

		}
	}

	// Kontrollerar prioritet för de olika operatorerna

	public static int priority(String operator) {
		switch (operator) {
		case "(":
		case "-(":
			return 0;
		case "+":
		case "-":
			return 1;
		case "*":
		case "/":
			return 2;
		case ")":
			return 3;
		case "empty":
		default:
			return -1;
		}
	}

	// Delar upp kö i en array

	public static String[] splitQueue(StringQueue queue, String[] lexedString) {
		String[] lexedArrayPostfix;
		int count = lexedString.length;

		// Tar bort eventuella null som tillkommit då parenteser tagits bort
		// Lägger till plats i array vid negativa parenteser

		for (int i = 0; i < lexedString.length; i++) {
			if (lexedString[i].equals("(") || lexedString[i].equals(")"))
				count--;
			else if (lexedString[i].equals("-("))
				count++;

		}

		// Skapar en array och lägger över värdena från kö till den

		lexedArrayPostfix = new String[count];

		for (int i = 0; i < count; i++) {
			if (!queue.isEmpty()) {
				lexedArrayPostfix[i] = queue.deQueue();
			}

		}

		

		return lexedArrayPostfix;
	}

	// Delar upp uttrycket i en array

	public static String[] split(String in) {
		String[] splitString = new String[in.length()];
		String temp = "", previous = "";
		int n = 0, check = 0;
		splitString[0] = "placeholder";

		for (int i = 0; i < in.length(); i++) {

			String s = Character.toString(in.charAt(i));
			
			//Sparar operander och unära minus i temp, tills en operator kommer
			if ((isOperand(s)) || (isOperator(previous) && s.equals("-")) || (s.equals("-") && check == 0)) {
				temp = temp + s;
				check++;
			}
			
			//Specialfall för negativ parentes
			else if (s.equals("(") && temp.equals("-")) {
				if (!splitString[n].equals("placeholder"))
					n += 1;

				temp = "";

				splitString[n] = "-(";

			}
			
			//Sparar in allt som ligger i temp samt operatorn, specialfall då symbol är en vänsterparentes och då symbol är den
			//första operatorn i uttrycket
			else if (isOperator(s)) {

				if ("(".equals(s)) {

					if (!splitString[n].equals("placeholder"))
						n += 1;

					splitString[n] = s;

				}

				else if (isOperator(previous)) {
					n += 1;
					splitString[n] = s;

				}

				else if (splitString[n].equals("placeholder")) {

					splitString[n] = temp;
					temp = "";
					n += 1;

					splitString[n] = s;

				}

				else {

					n += 1;
					splitString[n] = temp;
					temp = "";

					n += 1;

					splitString[n] = s;

				}

			}
			//Sparar symbolen som "previous" för att kunna jämföra med
			previous = s;
		}

		//Sparar sista symbolen, ersätter tomma platser med null
		if (n < splitString.length - 1) {
			if (!splitString[n].equals("") && !splitString[n].equals("placeholder")) {
				n += 1;
				splitString[n] = temp;
			} else
				splitString[n] = temp;
		}
		if (splitString[n] == "")
			splitString[n] = null;

		return splitString;
	}

	// Kontrollerar om symbol är en operand

	public static boolean isOperand(String s) {
		if (operander.contains(s))
			return true;
		else
			return false;

	}

	// Kontrollerar om symbol är en operator

	public static boolean isOperator(String s) {
		if (operatorer.contains(s))
			return true;
		else
			return false;

	}

}
