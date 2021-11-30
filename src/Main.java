import java.lang.reflect.InvocationTargetException;

enum Animal{
	DOG("��"),
	CAT("�����");
	
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
		myPrint("�ȳ�");
		myPrint("�ȳ�", "�ϼ�", "��");
		myPrint("�ȳ�", "�Ͻ�", "�ϱ�");
		
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
