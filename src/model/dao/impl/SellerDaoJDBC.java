package model.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import db.DB;
import db.DbException;
import model.dao.SellerDao;
import model.entities.Department;
import model.entities.Seller;

public class SellerDaoJDBC implements SellerDao {
	
	private Connection conn;

	public SellerDaoJDBC(Connection conn) {
		this.conn = conn;
	}

	@Override
	public void insert(Seller obj) {
		
		PreparedStatement sta = null;
		
		try {
			sta = conn.prepareStatement("INSERT INTO seller " 
									  + "(Name, Email, BirthDate, BaseSalary, DepartmentId) " 
									  + "VALUES " 
									  + "(?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
		
			sta.setString(1, obj.getName());
			sta.setString(2, obj.getEmail());
			sta.setDate(3, new java.sql.Date(obj.getBirthDate().getTime()));
			sta.setDouble(4, obj.getBaseSalary());
			sta.setInt(5, obj.getDepartment().getId());
			
			int rowsAffected = sta.executeUpdate();

			if (rowsAffected > 0) {
				ResultSet res = sta.getGeneratedKeys();
				
				if (res.next()) {
					int id = res.getInt(1);
					obj.setId(id);
				}
				
				DB.closeResultSet(res);
			}
			else {
				throw new DbException("Unexpected error! No rows affected!");
			}
		}
		
		catch (SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeStatement(sta);
		}
	}

	@Override
	public void update(Seller obj) {
		PreparedStatement sta = null;

		try {
			sta = conn.prepareStatement("UPDATE seller "
									  + "SET Name = ?, Email = ?, BirthDate = ?, BaseSalary = ?, DepartmentId = ? "
									  + "WHERE Id = ?");

			sta.setString(1, obj.getName());
			sta.setString(2, obj.getEmail());
			sta.setDate(3, new java.sql.Date(obj.getBirthDate().getTime()));
			sta.setDouble(4, obj.getBaseSalary());
			sta.setInt(5, obj.getDepartment().getId());
			sta.setInt(6, obj.getId());
			
			sta.executeUpdate();
			
		}
		catch (SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeStatement(sta);
		}
	}

	@Override
	public void deleteById(Integer id) {
		PreparedStatement sta = null;

		try {
			sta = conn.prepareStatement("DELETE FROM seller WHERE Id = ?");

			
			sta.setInt(1, id);
			
			sta.executeUpdate();
			
		}
		catch (SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeStatement(sta);
		}
	}

	@Override
	public Seller findById(Integer id) {
		PreparedStatement sta = null;
		ResultSet res = null;

		try {
			sta = conn.prepareStatement("SELECT seller.*,department.Name as DepName " 
									  + "FROM seller INNER JOIN department "
									  + "ON seller.DepartmentId = department.Id "
									  + "WHERE seller.Id = ?");
			
			sta.setInt(1, id);
			res = sta.executeQuery();

			if (res.next()) {
				Department dep = instantiateDep(res);

				Seller sel = instantiateSeller(res, dep);

				return sel;
			}
			return null;
		} 
		catch (SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeStatement(sta);
			DB.closeResultSet(res);
		}
	}

	private Seller instantiateSeller(ResultSet res, Department dep) throws SQLException {
		Seller sel = new Seller();
		
		sel.setId(res.getInt("Id"));
		sel.setName(res.getString("Name"));
		sel.setEmail(res.getString("Email"));
		sel.setBirthDate(res.getDate("BirthDate"));
		sel.setBaseSalary(res.getDouble("BaseSalary"));
		sel.setDepartment(dep);
		
		return sel;
	}

	private Department instantiateDep(ResultSet res) throws SQLException {
		Department dep = new Department();
		
		dep.setId(res.getInt("DepartmentId"));
		dep.setName(res.getString("DepName"));
		
		return dep;
	}
	
	@Override
	public List<Seller> findAll() {
		PreparedStatement sta = null;
		ResultSet res = null;

		try {
			sta = conn.prepareStatement("SELECT seller.*,department.Name as DepName "
									  + "FROM seller INNER JOIN department "
									  + "ON seller.DepartmentId = department.Id "
									  + "ORDER BY Name");
			
			res = sta.executeQuery();

			List<Seller> list = new ArrayList<>();
			Map<Integer, Department> map = new HashMap<>();
			
			while (res.next()) {

				Department dep = map.get(res.getInt("DepartmentId"));
				if (dep == null) {
					dep = instantiateDep(res);
					map.put(res.getInt("DepartmentId"), dep);
				}
				
				Seller sel = instantiateSeller(res, dep);

				list.add(sel);
			}
			return list;
		}
		catch (SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeStatement(sta);
			DB.closeResultSet(res);
		}
	}

	@Override
	public List<Seller> findByDepartment(Department department) {
		PreparedStatement sta = null;
		ResultSet res = null;

		try {
			sta = conn.prepareStatement("SELECT seller.*,department.Name as DepName "
									  + "FROM seller INNER JOIN department "
									  + "ON seller.DepartmentId = department.Id "
									  + "WHERE DepartmentId = ? "
									  + "ORDER BY Name");
			
			sta.setInt(1, department.getId());
			res = sta.executeQuery();

			List<Seller> list = new ArrayList<>();
			Map<Integer, Department> map = new HashMap<>();
			
			while (res.next()) {

				Department dep = map.get(res.getInt("DepartmentId"));
				if (dep == null) {
					dep = instantiateDep(res);
					map.put(res.getInt("DepartmentId"), dep);
				}
				
				Seller sel = instantiateSeller(res, dep);

				list.add(sel);
			}
			return list;
		}
		catch (SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeStatement(sta);
			DB.closeResultSet(res);
		}
	}
}