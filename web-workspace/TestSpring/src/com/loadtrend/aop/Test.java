package com.loadtrend.aop;

public class Test implements ITest
{
	public void doTest()
	{
		for (int i = 0; i < 10000000; i++)
		{
		}
	}

	public void executeTest()
	{
		for (int i = 0; i < 25000000; i++)
		{
		}
	}
}