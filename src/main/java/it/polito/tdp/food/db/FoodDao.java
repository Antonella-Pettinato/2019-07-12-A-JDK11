package it.polito.tdp.food.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import it.polito.tdp.food.model.Arco;
import it.polito.tdp.food.model.Condiment;
import it.polito.tdp.food.model.Food;
import it.polito.tdp.food.model.Portion;

public class FoodDao {
	public List<Food> listAllFoods() {
		String sql = "SELECT * FROM food";
		try {
			Connection conn = DBConnect.getConnection();

			PreparedStatement st = conn.prepareStatement(sql);

			List<Food> list = new ArrayList<>();

			ResultSet res = st.executeQuery();

			while (res.next()) {
				try {
					list.add(new Food(res.getInt("food_code"), res.getString("display_name")));
				} catch (Throwable t) {
					t.printStackTrace();
				}
			}

			conn.close();
			return list;

		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}

	}

	public List<Condiment> listAllCondiments() {
		String sql = "SELECT * FROM condiment";
		try {
			Connection conn = DBConnect.getConnection();

			PreparedStatement st = conn.prepareStatement(sql);

			List<Condiment> list = new ArrayList<>();

			ResultSet res = st.executeQuery();

			while (res.next()) {
				try {
					list.add(new Condiment(res.getInt("condiment_code"), res.getString("display_name"),
							res.getDouble("condiment_calories"), res.getDouble("condiment_saturated_fats")));
				} catch (Throwable t) {
					t.printStackTrace();
				}
			}

			conn.close();
			return list;

		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	public List<Portion> listAllPortions() {
		String sql = "SELECT * FROM portion";
		try {
			Connection conn = DBConnect.getConnection();

			PreparedStatement st = conn.prepareStatement(sql);

			List<Portion> list = new ArrayList<>();

			ResultSet res = st.executeQuery();

			while (res.next()) {
				try {
					list.add(new Portion(res.getInt("portion_id"), res.getDouble("portion_amount"),
							res.getString("portion_display_name"), res.getDouble("calories"),
							res.getDouble("saturated_fats"), res.getInt("food_code")));
				} catch (Throwable t) {
					t.printStackTrace();
				}
			}

			conn.close();
			return list;

		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}

	}

	public List<Food> listAllVertici(int N, Map<Integer, Food> idMapFood) {
		String sql = "SELECT f.food_code, COUNT(p.portion_id) AS n " + "FROM `food_pyramid_mod`.`portion` p, food f "
				+ "WHERE p.food_code=f.food_code " + "GROUP BY p.food_code " + "HAVING COUNT(p.portion_id) <=?";
		try {
			Connection conn = DBConnect.getConnection();

			PreparedStatement st = conn.prepareStatement(sql);

			List<Food> list = new ArrayList<>();
			st.setInt(1, N);
			ResultSet res = st.executeQuery();

			while (res.next()) {
				try {
					list.add(idMapFood.get(res.getInt("f.food_code")));
				} catch (Throwable t) {
					t.printStackTrace();
				}
			}

			conn.close();
			return list;

		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}

	}

	public List<Arco> listAllArchi(Map<Integer, Food> idMapFood, List<Food> lista) {
		String sql = "SELECT  f1.food_code, f2.food_code, SUM(co.condiment_calories)as peso "
				+ "FROM food_condiment c1, food f1, food_condiment c2, food f2, condiment co "
				+ "WHERE f1.food_code=c1.food_code AND f2.food_code=c2.food_code AND c1.condiment_code=c2.condiment_code AND co.condiment_code=c1.condiment_code "
				+ "GROUP BY f1.food_code, f2.food_code";
		try {
			Connection conn = DBConnect.getConnection();

			PreparedStatement st = conn.prepareStatement(sql);

			List<Arco> list = new ArrayList<>();

			ResultSet res = st.executeQuery();

			while (res.next()) {
				try {
					Food f1 = idMapFood.get(res.getInt("f1.food_code"));
					Food f2 = idMapFood.get(res.getInt("f2.food_code"));
					double peso = res.getDouble("n");
					if (lista.contains(f1) && lista.contains(f2)) {
						Arco a = new Arco(f1, f2, peso);
						list.add(a);
					}
				} catch (Throwable t) {
					t.printStackTrace();
				}
			}

			conn.close();
			return list;

		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}

	}

}
