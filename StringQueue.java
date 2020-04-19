package lab3;

public class StringQueue {
	
	private String[] queueArray = new String[500];
	private int place = 0;
	
	
	//Lägger till ett element
	public void enQueue(String in) {
		queueArray[place++] = in;
	}
	
	//Returnerar och tar bort elementet som ligger först i kön
	public String deQueue() {
		if (!isEmpty()) {
			
			String firstPlace = queueArray[0];
			place--;
			
			for(int i = 0; i < place; i++) {
				queueArray[i] = queueArray[i+1];
			}
			return firstPlace;
			
		}
		else 
			return null;
	}
	
	//Kontrollerar om kön är tom
	public boolean isEmpty() {
		if (place < 0)
			return true;
		else 
			return false;
	}
	
	//Kontrollerar längd
	public int length() {
		return queueArray.length;
	}
	

}
