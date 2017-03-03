package cas2xb3_A3_mitchell_jpm;

import java.util.ArrayList;

public class DFS {
	private boolean[] marked;
	private int[] edgeTo;
	private final int s;
	
	public DFS(ArrayList<City> cities, City source, City dest){
		marked = new boolean[cities.size()];
		edgeTo = new int[cities.size()];
		this.s = source.index;
		dfs(cities, s, dest.index);
	}
	
	private void dfs(ArrayList<City> cities, int v, int dest){
		marked[v] = true;
		for (City w : cities.get(s).getConnected()){
			if (!marked[w.index]){
				edgeTo[w.index] = v;
				dfs(cities, w.index, dest);
			}
		}
	}
}
