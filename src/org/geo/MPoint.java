package org.geo;

import greatmap.utils.MMath;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class MPoint {
	public double x;
	public double y;
	public MPoint(double x,double y) {
		this.x = x;
		this.y = y;
	}
	public double getX() {
		return x;
	}
	public void setX(double x) {
		this.x = x;
	}
	public double getY() {
		return y;
	}
	public void setY(double y) {
		this.y = y;
	}
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "横坐标："+MMath.decimalFormat(x, 4)+";纵坐标："+MMath.decimalFormat(y, 4);
	}
}
