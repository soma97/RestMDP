package net.etfbl.model;

import java.io.Serializable;

public class Employee implements Serializable {

	private String username,passwordHash;
	private String activity="";
	private boolean signedIn=false;
	private boolean isBlocked=false;
	private static final long serialVersionUID = -7428461917446500932L;

	public Employee() {}


	public Employee(String username, String passwordHash) {
		super();
		this.username = username;
		this.passwordHash = passwordHash;
	}

	public boolean getSignedIn()
	{
		return signedIn;
	}
	
	public void setSignedIn(boolean signedIn)
	{
		this.signedIn=signedIn;
	}
	
	public boolean getBlocked()
	{
		return isBlocked;
	}
	
	public void setBlocked(boolean isBlocked)
	{
		this.isBlocked=isBlocked;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPasswordHash() {
		return passwordHash;
	}

	public void setPasswordHash(String passwordHash) {
		this.passwordHash = passwordHash;
	}

	public String getActivity() {
		return activity;
	}

	public void addActivity(String oneActivity){ activity+=System.getProperty("line.separator")+oneActivity; }


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Employee other = (Employee) obj;
		if (!username.equals(((Employee)obj).getUsername()))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Employee " + username;
	}

}