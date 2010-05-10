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
        
        // Observer ģʽ�ļ���ô�:
        // Observer ����Ϊ�� product ���������Է���, ����Observer����ӿڸ������������,��������product�Ĵ��뱾�������ʽ�����ʱ��
        // Observer ������Ϊ�����ڲ������ʽ����TestObserver1���е���������.TestObserver1��ı������������ֻҪ��product�򽻵�.
        // ������ʱ�� ��Ϊͬ��ӵ��product��TestObserver2��, ͨ������product������ӵ�����TestObserver1��������ݺ���Ϊ, ��
        // TestObserver1, TestObserver2���߻���֪���Է��Ĵ���,ͨ������������product�ͶԷ��򽻵�.
        // ����:���������������ͨ����������ɵ�.
        // �����е�Ҫ��:��Ҫ����ϵĶ���TestObserver1,TestObserver2,����������Product, ����Ϊ�����ڲ����Observer(Listener)����
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
