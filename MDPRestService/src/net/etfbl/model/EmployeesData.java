package net.etfbl.model;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Properties;
import java.util.Set;
import net.etfbl.main.Main;
import redis.clients.jedis.Jedis;

public class EmployeesData {
	public ArrayList<Employee> employees = new ArrayList<>();
	private static EmployeesData instance = null;
	private String DATABASE=null;
	Jedis jedis=null;
	private static Employee temporaryEmployee;
	InputStream inputProp=getClass().getClassLoader().getResourceAsStream(".."+File.separatorChar+"resources"+File.separatorChar+"config.properties");

	public static EmployeesData getInstance() {
		if (instance == null)
		{
			instance = new EmployeesData();
			Properties prop = new Properties();
	        try {
	            prop.load(instance.inputProp);
	            instance.DATABASE = prop.getProperty("DATABASE");
	            instance.jedis=new Jedis(instance.DATABASE);
	            Main.BASE_URL=prop.getProperty("BASE_URL");
	        } catch (Exception ex) {
	            ex.printStackTrace();
	            Main.setErrorLog(ex);
	        }
			loadArray();
		}
		
		return instance;
	}
	public static void loadArray()
	{
		Set<String> keys=instance.jedis.keys("*");
		for(Object x:keys.toArray())
		{
			temporaryEmployee=getByUsername((String)x);
			if(!instance.employees.stream().anyMatch(employee->employee.getUsername().equals(temporaryEmployee.getUsername())))
				instance.employees.add(temporaryEmployee);
		}
	}
	
	public static Employee getByUsername(String username)
	{
		if(!instance.jedis.exists(username.getBytes()))
			return null;
		try(ByteArrayInputStream bis = new ByteArrayInputStream(instance.jedis.get(username.getBytes()));
			    ObjectInput in = new ObjectInputStream(bis))
		{
			return (Employee)in.readObject();
		}catch(Exception e)
		{
			e.printStackTrace();
			Main.setErrorLog(e);
		}
		return null;
	}
	
	public boolean updateEmployee(Employee employee)
	{
		Employee toChange=getByUsername(employee.getUsername());
		if(toChange==null) return false;
		if(!toChange.getSignedIn())
			return false;
		toChange.setPasswordHash(employee.getPasswordHash());
		jedis.del(toChange.getUsername().getBytes());
		temporaryEmployee=employee=toChange;
		instance.employees.removeIf(x->temporaryEmployee.getUsername().equals(x.getUsername()));
		instance.employees.add(toChange);
		
		try(ByteArrayOutputStream bos=new ByteArrayOutputStream();
				ObjectOutput out=new ObjectOutputStream(bos)) {  
		  out.writeObject(toChange);
		  out.flush();
		  byte[] objectBytes = bos.toByteArray();
		  jedis.set(toChange.getUsername().getBytes(), objectBytes);
		}catch(Exception ex)
		{
			ex.printStackTrace();
			Main.setErrorLog(ex);
			return false;
		}
		return true;
	}
	
	public boolean loginLogout(String username,Employee employee)
	{
		boolean authOk=false;
		Employee toChange=getByUsername(username);
		if(toChange==null || toChange.getBlocked())
			return false;
		if(toChange.getUsername().equals(employee.getUsername()) && toChange.getPasswordHash().equals(employee.getPasswordHash()))
			authOk=true;
		if(!authOk && employee.getSignedIn())
			return false;
		
		if(employee.getSignedIn())
		{
			toChange.setSignedIn(true);
			toChange.addActivity("Logged in: "+Instant.now().toString().split("\\.")[0].replace("T"," "));
		}else
		{
			toChange.setSignedIn(false);
			toChange.addActivity("Logged out: "+Instant.now().toString().split("\\.")[0].replace("T"," "));
		}
		temporaryEmployee=employee=toChange;
		instance.employees.removeIf(x->temporaryEmployee.getUsername().equals(x.getUsername()));
		instance.employees.add(toChange);
		jedis.del(toChange.getUsername().getBytes());
		
		try(ByteArrayOutputStream bos=new ByteArrayOutputStream();
				ObjectOutput out=new ObjectOutputStream(bos)) {  
		  out.writeObject(toChange);
		  out.flush();
		  byte[] objectBytes = bos.toByteArray();
		  jedis.set(toChange.getUsername().getBytes(), objectBytes);
		}catch(Exception ex)
		{
			ex.printStackTrace();
			Main.setErrorLog(ex);
			return false;
		}
		return true;
	}

}
