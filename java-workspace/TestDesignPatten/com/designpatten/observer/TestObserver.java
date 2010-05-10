package com.designpatten.observer;

import java.util.Observable;
import java.util.Observer;

public class TestObserver
{
	
    public static void main( String[] args )
    {
        Product product = new Product();
        
        new TestObserver1(product).addObserverToProduct();
        
        new TestObserver2(product).invokeTest();
        
        product.setName( "BigPig" );
        product.setPrice( 388 );
        
        // Observer 模式的几点好处:
        // Observer 的行为和 product 本身代码可以分离, 留出Observer这个接口给其它程序调用,尤其是在product的代码本身是组件式打包的时候
        // Observer 可以作为匿名内部类的形式访问TestObserver1类中的所有数据.TestObserver1类的本身和它的数据只要和product打交道.
        // 在任意时刻 作为同样拥有product的TestObserver2类, 通过调用product方法间接调用了TestObserver1的相关数据和行为, 但
        // TestObserver1, TestObserver2两者互不知道对方的存在,通过第三方介质product和对方打交道.
        // 结论:两个对象松耦合是通过第三方完成的.
        // 案例中的要素:需要松耦合的对象TestObserver1,TestObserver2,第三方介质Product, 常作为匿名内部类的Observer(Listener)对象
    }
}

class TestObserver1 {
	private String testObserverData = "testObserverData";
	private Product product = null;
	public TestObserver1(Product product) {
		this.product = product;
		
	}
	public void addObserverToProduct() {
        // Add observer class
        product.addObserver( new NameObserver() );
        product.addObserver( new PriceObserver() );
        product.addObserver(new Observer() {
			public void update(Observable o, Object arg) {
				if (arg instanceof String && arg.toString().equalsIgnoreCase("test")) {
					System.out.println("Visit the Test Observer Data: " + testObserverData);
				}
			}
        });
	}
}

class TestObserver2 {
	private Product product = null;
	public TestObserver2(Product product) {
		this.product = product;
	}
	public void invokeTest() {
		this.product.setName("test");
	}
}
