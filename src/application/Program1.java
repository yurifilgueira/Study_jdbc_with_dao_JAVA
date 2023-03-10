package application;

import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;

import db.DB;
import model.dao.DaoFactory;
import model.dao.SellerDao;
import model.entities.Department;
import model.entities.Seller;

public class Program1 {

	public static void main(String[] args) {

		Locale.setDefault(Locale.US);
		Scanner sc = new Scanner(System.in);
		
		System.out.println("--------------------------Test 1--------------------------");
		SellerDao sellerDao = DaoFactory.createSellerDao();
		
		Seller seller = sellerDao.findById(2);
		
		System.out.println(seller);
		
		System.out.println("--------------------------Test 2--------------------------");
		
		Department dep = new Department(2, null);
		List<Seller> sellerList = sellerDao.findByDepartment(dep);

		for (Seller sel : sellerList) {
			System.out.println(sel);
		}
		
		System.out.println("--------------------------Test 3--------------------------");

		sellerList = sellerDao.findAll();

		for (Seller sel : sellerList) {
			System.out.println(sel);
		}
		
		System.out.println("--------------------------Test 4--------------------------");

		Seller newseller = new Seller(null, "Greg", "greg@gmail.com", new Date(), 5500.00, dep);
		
		sellerDao.insert(newseller);
		
		System.out.println("Inserted! New id = " + newseller.getId());
		
		System.out.println("--------------------------Test 5--------------------------");
		seller = sellerDao.findById(1);
		
		seller.setName("Bruce Wayne");
		
		sellerDao.update(seller);
		
		System.out.println("Update completed!");
		
		System.out.println("--------------------------Test 6--------------------------");
		
		System.out.print("Enter the id that will be deleted: ");
		int idToBeDeleted = sc.nextInt();
		
		sellerDao.deleteById(idToBeDeleted);
		
		System.out.println("Delete completed!");
		
		sc.close();

		DB.closeConnection();
	}
}