package application;

import java.util.List;
import java.util.Locale;
import java.util.Scanner;

import db.DB;
import model.dao.DaoFactory;
import model.dao.DepartmentDao;
import model.entities.Department;

public class Program2 {

	public static void main(String[] args) {
	
		Locale.setDefault(Locale.US);
		Scanner sc = new Scanner(System.in);
		
		System.out.println("--------------------------Test 1--------------------------");
		
		DepartmentDao depDao = DaoFactory.createDepartmentDao();
		
		Department dep = new Department(2, "Foods");
		
		depDao.insert(dep);
		
		System.out.println("Inserted! Id of the new department: " + dep.getId());

		System.out.println("--------------------------Test 2--------------------------");
		
		dep.setId(7);
		dep.setName("Foods");
		
		depDao.update(dep);
		
		System.out.println("Updated successfully!");
		
		System.out.println("--------------------------Test 3--------------------------");
		
		depDao.deleteById(6);
		
		System.out.println("Deleted successfully!");

		System.out.println("--------------------------Test 4--------------------------");
		
		System.out.print("Enter with the id of the department that you want: ");
		int id = sc.nextInt();
		System.out.println(depDao.findById(id));
		
		System.out.println("--------------------------Test 5--------------------------");
		
		List<Department> listOfDep = depDao.findAll();
		
		for(Department d : listOfDep) {
			System.out.println(d);
		}
		
		sc.close();
		
		DB.closeConnection();
		
	}

}
