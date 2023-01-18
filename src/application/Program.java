package application;

import java.util.List;
import java.util.Locale;
import java.util.Scanner;

import model.dao.DaoFactory;
import model.dao.SellerDao;
import model.entities.Department;
import model.entities.Seller;

public class Program {

	public static void main(String[] args) {

		Locale.setDefault(Locale.US);
		Scanner sc = new Scanner(System.in);
		
		System.out.println("--------------------------Test 1--------------------------");
		SellerDao sellerDao = DaoFactory.creatSellerDao();
		
		Seller seller = sellerDao.findById(2);
		
		System.out.println(seller);
		
		System.out.println("--------------------------Test 2--------------------------");
		
		Department dep = new Department(2, null);
		List<Seller> sellerList = sellerDao.findByDepartment(dep);

		for (Seller sel : sellerList) {
			System.out.println(sel);
		}
		
		sc.close();

	}

}
