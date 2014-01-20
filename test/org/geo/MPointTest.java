package org.geo;

import   static  org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.Collection;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class MPointTest {
	private MPoint point;
	private double x;
	private double y;
	private String res;
	@Before
	public void before(){
		point = new MPoint(x, y);
	}
	public MPointTest(double x, double y, String exception) {
		this.x = x;
		this.y = y;
		this.res = exception;
	}
	@SuppressWarnings("unchecked")
	@Parameters
	public static Collection data(){
		return  Arrays.asList( new  Object[][] {
                { 114.29,30.00,"横坐标：114.2900;纵坐标：30.0000"},
                { 114.29123,30.00,"横坐标：114.2912;纵坐标：30.0000"},
                { 114,30,"横坐标：114.0000;纵坐标：30.0000"}
       } );
	}
	
	@Test public void toStringTest() {
		assertEquals(res, point.toString());
	}
}
