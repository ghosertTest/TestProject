import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.TreeMap;
import java.util.TreeSet;

public class TestDataStruct {

	public static void main(String[] args) {
		// Browse the source code below.
		
		// ˳���(˳�����Ա�)��ʵ��, �ڲ�ά����������, �����漴��ȡ���ݣ���������ɾ
		// ����Ԫ��ʱ, ��Ԫ�����������鳤��, ��ԭ���鳤�ȵ�1.5��������չ���鳤��
		ArrayList arrayList = new ArrayList();
		
		// ��ʽ��(˫��ѭ����ʽ���Ա�)��ʵ��, ������ɾ�������ڴ�ȡ
		LinkedList linkedList = new LinkedList();
		
		// ������ʵ��:ά�����������(����һ�����Ǻ����)
		TreeMap treeMap = new TreeMap();
		// �洢����ʱ�������������, ��������:Comparator!=null��ComparatorΪ��С���ݽ���
		// Comparator==null��((Comparable)Object).compareTo(Object object)Ϊ��С���ݽ���
		treeMap.put("d", "4");
		treeMap.put("e", "5");
		treeMap.put("f", "6");
		treeMap.put("a", "1");
		treeMap.put("b", "2");
		treeMap.put("c", "3");
		// ��������ʱ���������������������(�������۰����).
		System.out.println(treeMap.get("e"));
		// Keys �������ԭ��������������и��������� �� ������
		// ��ʵ��ʱ�Էǵݹ��и�����(����ڵݹ��и����������ڵõ�ָ���ڵ��ǰ����̽ڵ㣬������Ҫ�Ǻ�̽ڵ�)��ά��������������õ�������
		System.out.println("TreeMap Keys:");
		Iterator iterator = treeMap.keySet().iterator();
		while (iterator.hasNext()) {
			String string = (String) iterator.next();
			System.out.println(string + " ");
		}
		System.out.println("TreeMap Values:");
		iterator = treeMap.values().iterator();
		while (iterator.hasNext()) {
			String string = (String) iterator.next();
			System.out.println(string + " ");
		}
		
		Message a = new Message("a");
		Message b = new Message("b");
		Message c = new Message("c");
		Message cc = new Message("c");
		
		// Hash ʵ��: �ڲ�ά���ɵ���ѭ�����������γɵĹ�ϣ��
		// ��i=hash(key) & (table.length -1)ȷ��������������ȷ�����������ȡ����, ��hashCode��Ӧ��������, ����Ӧ��ȡ��ַ
		// i�൱��ɢ����Ͱ�ı�� 
		// �ʶ��ڴ���HashMap������keyֵ��Object
		// 1. ����дhashCode: ÿ��ʵ����hashCode������ͬ, �����ܴ�����ͬһ��������, ȴֻ����ͬʵ��������ĳ���ڵ㻥���滻VALUEֵ(ִ��put����ʱ)
		// 2. ��дhashCode: ��ͬʵ����hashCodeҲ������ͬ, ������ͬһ����, ��ĳ���ڵ㻥���滻�Է���VALUEֵ(ִ��put����ʱ)
		// 3. ��дhashCode���ص�һֵ: ��ϣ���˻�Ϊ����ѭ������,����Զʹ��table[i]������ȡ����
		// ����: һ���Object, �Ƿ���дhashCode, ��Ӱ��HashMapʹ��, ���������3������������, Ӧ����
		//      ��ͨӦ����ʹ��String��Ϊkey�Ѳ����ڴ�����, String ����дhashcode,equals����
		HashMap hashMap = new HashMap();
		// ����Ԫ��ǰ�ȽϹ�ϣ����Ԫ�ظ����Ƿ񳬹�capacity * loadFactor(default:0.75)
		// ��������2 * table.length�Ĳ����������鳤��, �������е�Ԫ�ذ�i=object.hashcode() & (table.length -1)��������ָ����������
		// Ч�����ڱ���ɢ����Ͱ�ĸ�����ÿ��Ͱ���������ݵ�ƽ�⣬ʹ����������Ԫ�ص�������ɢ����ͰҲ���ӣ�ÿ��Ͱ�����ݼ�С
		// key��hashCode��ͬ, ����key��equals����Ϊtrue, valueֵ�Żᱻ�滻, ��������Ԫ��(��������ͬKEY��ȷ��������KEYΨһ�ԵĹؼ�)
		hashMap.put(a, "1");
		hashMap.put(b, "2");
		hashMap.put(c, "3");
		hashMap.put(cc, "4");
		hashMap.put(cc, "5");
		System.out.println("HashMap value for key Message a: " + hashMap.get(a));
		System.out.println("HashMap value for key Message b: " + hashMap.get(b));
		System.out.println("HashMap value for key Message c: " + hashMap.get(c));
		System.out.println("HashMap value for key Message cc: " + hashMap.get(cc));
		
		System.out.println("table index for a: " + (hash(a) & (16-1)));
		System.out.println("table index for b: " + (hash(b) & (16-1)));
		System.out.println("table index for c: " + (hash(c) & (16-1)));
		System.out.println("table index for cc: " + (hash(cc) & (16-1)));
		// ��ϣ��������״���ܽ�: һ������������в�ͬ��hashCode, ��ͬhashCode��key����һ��������
		// �õ�hashCode�����ù�ϣ�����, ����ͬ�����hashCode�����ܲ���ͬ
		
		
		
		// ������ʵ��:ά�����������(����һ�����Ǻ����)
		// ʵ��ΪTreeMap���˻��汾,��ȡObjectԪ��Ϊ: KEY-VALUEģ���е�KEY
		// VALUEΪ:private static final Object PRESENT = new Object();�㶨ֵ
		// TreeSetӵ��TreeMap��ȫ������
		TreeSet treeSet = new TreeSet();
		
		// Hash ʵ��: �ڲ�ά���ɵ���ѭ�����������γɵĹ�ϣ��
		// ʵ��ΪHashMap���˻��汾,��ȡObjectԪ��Ϊ: KEY-VALUEģ���е�KEY
		// VALUEΪ:private static final Object PRESENT = new Object();�㶨ֵ
		// HashSetӵ��HashMap��ȫ������
		// key��hashCode��ͬ, ����key��equals����Ϊtrue, �Ų�����Ԫ��, ��������Ԫ��(��������ͬԪ�أ�ȷ��������Ԫ��Ψһ�ԵĹؼ�)
		HashSet hashSet = new HashSet();
		hashSet.add(a);
		hashSet.add(b);
		hashSet.add(c);
		hashSet.add(cc);
		System.out.println("Iterator HashSet");
		Iterator it = hashSet.iterator();
		while (it.hasNext()) {
			Message message = (Message) it.next();
			System.out.println(message);
		}
	}
	
	private static int hash(Object x) {
        int h = x.hashCode();
        h += ~(h << 9);
        h ^=  (h >>> 14);
        h +=  (h << 4);
        h ^=  (h >>> 10);
        return h;
	}
	
	private static class Message {
		String string = null;
		public Message(String string) {
			this.string = string;
		}
		// ����д,�ɵ�ַ�Ƿ���ͬ�ж�true or false,��ֻ��ͬһʵ��equals������Ϊtrue, ��ͬʵ��equals������Ϊfalse.
		// ��д, ��ͬʵ��Ҳ����true
		public boolean equals(Object obj) {
			Message message = (Message)obj;
			return this.string.equals(message.toString());
		}
		// ����д,�ɵ�ַ����hashCode,��ֻ��ͬһʵ��hashCode��ֵ����ͬ, ��ͬʵ��hashCode�ز�ͬ.
		// ��д, ��ͬʵ��hashCodeҲ������ͬ
		public int hashCode() {
			return string.hashCode();
		}
		public String toString() {
			return string;
		}
	}
}
