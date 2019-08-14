package net.etfbl.api;

import java.util.ArrayList;
import net.etfbl.model.Employee;
import net.etfbl.model.EmployeesData;

public class EmployeeService {

	EmployeesData dataInstance=EmployeesData.getInstance();
	int randomId;

	public ArrayList<Employee> getEmployees() {
		dataInstance.loadArray();
		return dataInstance.employees;
	}

	public Employee getByUsername(String username) {
		return dataInstance.getByUsername(username);
	}

	public boolean updatePassword(Employee employee) {
		return dataInstance.updateEmployee(employee);
	}

	public boolean logging(Employee employee, boolean login) {
		employee.setSignedIn(login);
		return dataInstance.loginLogout(employee.getUsername(), employee);
	}
	
	
	public String getEmployeeActivity(String username)
	{
		if(dataInstance.employees.stream().anyMatch(x->x.getUsername().equals(username)))
		{
			return getByUsername(username).getActivity();
		}
		return "";
	}
}
