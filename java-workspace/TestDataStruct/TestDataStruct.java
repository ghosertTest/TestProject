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
		
		// 顺序表(顺序线性表)的实现, 内部维护数据数组, 利于随即存取数据，不利于增删
		// 增加元素时, 若元素数大于数组长度, 以原数组长度的1.5倍步长扩展数组长度
		ArrayList arrayList = new ArrayList();
		
		// 链式表(双向循环链式线性表)的实现, 利于增删，不利于存取
		LinkedList linkedList = new LinkedList();
		
		// 二叉树实现:维护排序二叉树(更进一步的是红黑树)
		TreeMap treeMap = new TreeMap();
		// 存储数据时建立排序二叉树, 建树依据:Comparator!=null以Comparator为大小依据建树
		// Comparator==null以((Comparable)Object).compareTo(Object object)为大小依据建树
		treeMap.put("d", "4");
		treeMap.put("e", "5");
		treeMap.put("f", "6");
		treeMap.put("a", "1");
		treeMap.put("b", "2");
		treeMap.put("c", "3");
		// 返回数据时查找排序二叉树返回数据(类似于折半查找).
		System.out.println(treeMap.get("e"));
		// Keys 排序输出原理：排序二叉树的中根遍历序列 即 升序结果
		// 即实现时以非递归中根遍历(相对于递归中根遍历更便于得到指定节点的前驱后继节点，这里主要是后继节点)所维护的排序二叉树得到升序结果
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
		
		// Hash 实现: 内部维护由单向循环链表数组形成的哈希表
		// 由i=hash(key) & (table.length -1)确定数组索引进而确定单向链表存取数据, 即hashCode对应数组索引, 即对应存取地址
		// i相当于散列码桶的标记 
		// 故对于存入HashMap函数的key值的Object
		// 1. 不重写hashCode: 每个实例的hashCode都不相同, 但可能存在于同一条链表中, 却只有相同实例才能在某个节点互相替换VALUE值(执行put方法时)
		// 2. 重写hashCode: 不同实例的hashCode也可能相同, 存在于同一链表, 在某个节点互相替换对方的VALUE值(执行put方法时)
		// 3. 重写hashCode返回单一值: 哈希表退化为单向循环链表,即永远使用table[i]的链存取数据
		// 结论: 一般的Object, 是否重写hashCode, 不影响HashMap使用, 但针对上述3的情况性能最差, 应避免
		//      普通应用中使用String作为key已不存在此问题, String 已重写hashcode,equals方法
		HashMap hashMap = new HashMap();
		// 增加元素前比较哈希表中元素个数是否超过capacity * loadFactor(default:0.75)
		// 超过则以2 * table.length的步长增加数组长度, 并对其中的元素按i=object.hashcode() & (table.length -1)规则重新指定数组索引
		// 效果在于保持散列码桶的个数和每个桶内链表数据的平衡，使得随着数据元素的增长，散列码桶也增加，每个桶内数据减小
		// key的hashCode相同, 并且key的equals方法为true, value值才会被替换, 否则新增元素(不增加相同KEY，确保集合内KEY唯一性的关键)
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
		// 哈希链表数据状况总结: 一条链表里可能有不同的hashCode, 相同hashCode的key总在一条链表里
		// 好的hashCode尽量让哈希链表短, 即不同对象的hashCode尽可能不相同
		
		
		
		// 二叉树实现:维护排序二叉树(更进一步的是红黑树)
		// 实质为TreeMap的退化版本,存取Object元素为: KEY-VALUE模型中的KEY
		// VALUE为:private static final Object PRESENT = new Object();恒定值
		// TreeSet拥有TreeMap的全部特性
		TreeSet treeSet = new TreeSet();
		
		// Hash 实现: 内部维护由单向循环链表数组形成的哈希表
		// 实质为HashMap的退化版本,存取Object元素为: KEY-VALUE模型中的KEY
		// VALUE为:private static final Object PRESENT = new Object();恒定值
		// HashSet拥有HashMap的全部特性
		// key的hashCode相同, 并且key的equals方法为true, 才不新增元素, 否则新增元素(不增加相同元素，确保集合内元素唯一性的关键)
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
		// 不重写,由地址是否相同判断true or false,故只有同一实例equals方法才为true, 不同实例equals方法必为false.
		// 重写, 不同实例也可能true
		public boolean equals(Object obj) {
			Message message = (Message)obj;
			return this.string.equals(message.toString());
		}
		// 不重写,由地址计算hashCode,故只有同一实例hashCode数值才相同, 不同实例hashCode必不同.
		// 重写, 不同实例hashCode也可能相同
		public int hashCode() {
			return string.hashCode();
		}
		public String toString() {
			return string;
		}
	}
}
