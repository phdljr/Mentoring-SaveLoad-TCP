import java.lang.reflect.InvocationTargetException;

enum Animal{
	DOG("개"),
	CAT("고양이");
	
	Animal(String value) {
		this.value = value;
	}
	
	private String value;
	
	public String getValue() {
		return value;
	}
}

public class Main {

	public static void main(String[] args) {
		myPrint("안녕");
		myPrint("안녕", "하세", "요");
		myPrint("안녕", "하십", "니까");
		
		System.out.println(Animal.DOG.getValue());
		
		Thread t = new Thread(()->{
			System.out.println("hi");
		});
	}
	
	public static void myPrint(String...strs) {
		for(String s : strs) {
			System.out.print(s);
		}
		System.out.println();
	}

}
