package it.polito.tdp.food.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.food.db.FoodDao;

public class Model {
	private FoodDao dao;
	private SimpleWeightedGraph<Food, DefaultWeightedEdge> grafo;
	private Map<Integer, Food> idMapFood;

	public Model() {
		this.dao = new FoodDao();
		this.idMapFood = new HashMap<>();
		List<Food> allFoods = dao.listAllFoods();
		for (Food f : allFoods) {
			this.idMapFood.put(f.getFood_code(), f);
		}
	}
	public String creaGrafo(int Porzioni) {
		String risposta=" ";
		List<Food> vertici=dao.listAllVertici(Porzioni, idMapFood);
		Graphs.addAllVertices(grafo, vertici);
		List<Arco> archi=dao.listAllArchi(idMapFood, vertici);
		for(Arco a: archi) {
			Graphs.addEdgeWithVertices(grafo, a.getF1(), a.getF2(), a.getCalorie());
		}
		risposta+="#VERTICI:  "+grafo.vertexSet()+"\n";
		risposta+="#ARCHI:  "+grafo.edgeSet()+"\n";
		
		
		
		return risposta;
	}
}
