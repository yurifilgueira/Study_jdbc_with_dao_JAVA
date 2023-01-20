package model.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import db.DB;
import db.DbException;
import model.dao.DepartmentDao;
import model.entities.Department;

public class DepartmentDaoJDBC implements DepartmentDao{

	private Connection conn;
	 
	public DepartmentDaoJDBC(Connection conn) {
		this.conn = conn;
	}
	
	@Override
	public void insert(Department obj) {
		
		PreparedStatement sta = null;
		
		try {
			sta = conn.prepareStatement("INSERT INTO department " 
					  				  + "(Name) " 
					  				  + "VALUES " 
					  				  + "(?)", Statement.RETURN_GENERATED_KEYS);
			
			sta.setString(1, obj.getName());
			
			int rowsAffected = sta.executeUpdate();
			
			if (rowsAffected > 0) {
				ResultSet res = sta.getGeneratedKeys();
				
				if (res.next()) {
					int id = res.getInt(1);
					
					obj.setId(id);
				}
				else {
					throw new DbException("Unexpected error! No rows affected!");
				}
			}
			
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeStatement(sta);
		}
		
	}

	@Override
	public void update(Department obj) {
		
		PreparedStatement sta = null;
		
		try {
			sta = conn.prepareStatement("UPDATE department "
					  				  + "SET Name = ? "
					  				  + "WHERE Id = ?");
			
			sta.setString(1, obj.getName());
			sta.setInt(2, obj.getId());
			
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
			sta = conn.prepareStatement("DELETE FROM department WHERE Id = ?");
			
			sta.setInt(1, id);

			sta.executeUpdate();
			
		}
		catch(SQLException e){
			throw new DbException(e.getMessage());
		}
		
	}

	public Department instantiateDepartment(Integer id, String depName) {
		 Department dep = new Department(id, depName);
		 return dep;
	}
	
	@Override
	public Department findById(Integer id) {
		PreparedStatement sta = null;
		ResultSet res = null;
		
		try {
			sta = conn.prepareStatement("SELECT * FROM department WHERE Id = ?");
			
			sta.setInt(1, id);
			
			res = sta.executeQuery();
			
			if (res.next()) {
				
				return instantiateDepartment(res.getInt("Id"), res.getString("Name"));
				
			}
			return null;
			
		}
		catch(SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeStatement(sta);
			DB.closeResultSet(res);
		}
	}

	@Override
	public List<Department> findAll() {
		PreparedStatement sta = null;
		ResultSet res = null;
		
		try {
			sta = conn.prepareStatement("SELECT * FROM department ORDER BY Name");
			
			res = sta.executeQuery();
			
			List<Department> list = new ArrayList<>();
			
			while (res.next()) {
				
				list.add(instantiateDepartment(res.getInt("Id"), res.getString("Name")));
				
			}
			return list;
			
		}
		catch(SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeStatement(sta);
			DB.closeResultSet(res);
		}
	}
}
