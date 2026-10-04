package com.first.training.collection;


public class Employee {

	private MySystem mySystem;
	private int rollNo;
	private String name;
	
	
	public Employee(MySystem mySystem, int rollNo, String name) {
		super();
		this.mySystem = mySystem;
		this.rollNo = rollNo;
		this.name = name;
	}
	public Employee(int rollNo, String name) {
		super();
		this.rollNo = rollNo;
		this.name = name;
	}
	public int getRollNo() {
		return rollNo;
	}
	public void setRollNo(int rollNo) {
		this.rollNo = rollNo;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public MySystem getMySystem() {
		return mySystem;
	}
	public void setMySystem(MySystem mySystem) {
		this.mySystem = mySystem;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + rollNo;
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Employee other = (Employee) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (rollNo != other.rollNo)
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "Employee [rollNo=" + rollNo + ", name=" + name + "]";
	}
	
	
	
	
		
}
