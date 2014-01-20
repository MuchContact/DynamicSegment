package org.geo.dao;

public class UserBeanOne {
	private double x;
	private double y;
	private double distance;
	
	public UserBeanOne(double x, double y, double distance) {
		super();
		this.x = x;
		this.y = y;
		this.distance = distance;
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
	public double getDistance() {
		return distance;
	}
	public void setDistance(double distance) {
		this.distance = distance;
	}
	
}
