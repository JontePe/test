package lab3;

public class StringStack {
	private final int max = 500;

	private String[] stackArray = new String[max];
	private int place = -1;
	
	
	
	//Lägger till ett element överst i stacken
	public void push(String in) {
		if (place < max) {
			stackArray[++place] = in;
			
		}
		
	}
	
	//Returnerar översta värdet och tar bort det från stacken
	public String pop() {
		if (!isEmpty()) {
			return stackArray[place--];
		}
		else
			return null;
	
	}
	
	//Returnerar översta värdet
	public String peek() {
		if (!isEmpty())
			return stackArray[place];
		
		else
			return "empty";
		
	}
	
	//Kontrollerar om stacken är tom
	public boolean isEmpty() {
		if (place < 0)
			return true;
		else
			return false;
	}
}
