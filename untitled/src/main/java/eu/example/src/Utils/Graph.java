package eu.example.src.Utils;

import eu.example.src.domain.Friendship;
import eu.example.src.domain.Tuple;
import eu.example.src.domain.Utilizator;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public class Graph {
    List<List<Integer>> adjList;
    Integer n;
    Iterable<Utilizator> us;
    Integer maxVisited;
    public Graph(Iterable<Friendship> friendships, Integer utiliz, Iterable<Utilizator> all) {
        n = utiliz;
        us = all;
        maxVisited = 0;
        for(Utilizator u: all){
            if(u.getId()>maxVisited){
                maxVisited = u.getId().intValue();
            }
        }
        maxVisited++;
        // Adăugăm 1 pentru a putea utiliza indecșii direct (1-indexed)
        adjList = new ArrayList<>(maxVisited); // Păstrăm n + 1 pentru indecși de la 1 la n
        for (int i = 0; i <= maxVisited; i++) {
            adjList.add(new ArrayList<>());
        }

        // Acum adăugăm arce
        friendships.forEach(f -> {
            Tuple<Long, Long> users = f.getId();
            int u = users.getFirst().intValue();
            int v = users.getSecond().intValue();
            adjList.get(u).add(v);
            adjList.get(v).add(u);
        });
    }

    public int nrComunitati() {
        boolean[] visited = new boolean[maxVisited]; // Inițializăm cu dimensiunea n + 1
        AtomicInteger nrComunitati = new AtomicInteger();

        // Iterăm de la 1 la n
        us.forEach(u -> {
            int i = u.getId().intValue();
            if (!visited[i]) {
                dfs(i, visited); // Apelăm DFS pentru a vizita toate nodurile din componenta conexă
                nrComunitati.getAndIncrement(); // Incrementăm numărul de comunități
            }
        });
        return nrComunitati.get();
    }

    private void dfs(int i, boolean[] visited) {
        visited[i] = true; // Marcăm nodul ca vizitat
        adjList.get(i).forEach(j -> { // Iterăm prin lista de adiacență
            if (!visited[j]) {
                dfs(j, visited); // Apelăm recursiv DFS
            }
        });
    }

    public List<Integer> getCelMaiLungDrum() {
        boolean[] visited = new boolean[maxVisited];  // Vector de vizite
        int[] maxDistance = new int[1];          // Pentru a stoca lungimea maximă
        //atomic ca altfel nu l pot modifica in lambda
        AtomicReference<List<Integer>> longestPath = new AtomicReference<>(new ArrayList<>()); // Lista finală cu cel mai lung drum

        // Iterăm prin toate nodurile pentru a găsi cel mai lung drum în fiecare componentă conexă
            us.forEach(u -> {
        int i = u.getId().intValue();
        if (!visited[i]) {
            // Găsim un nod cel mai îndepărtat folosind DFS din nodul curent
            int farthestNode = dfsFindFarthest(i, visited).get(0);

            // Resetăm vectorul de vizite pentru componenta curentă
            boolean[] componentVisited = new boolean[maxVisited];

            // Găsim cel mai lung drum pornind de la farthestNode
            List<Integer> currentLongestPath = new ArrayList<>();
            dfsFindFarthestWithPath(farthestNode, componentVisited, 0, maxDistance, new ArrayList<>(), currentLongestPath);

            // Actualizăm cel mai lung drum dacă am găsit unul mai lung
            if (currentLongestPath.size() > longestPath.get().size()) {
                longestPath.set(currentLongestPath);
            }
        }
    });

        return longestPath.get();
    }

    // Funcție de DFS pentru a găsi cel mai îndepărtat nod
    private List<Integer> dfsFindFarthest(int node, boolean[] visited) {
        visited[node] = true;
        List<Integer> result = new ArrayList<>();
        result.add(node);

        adjList.get(node).forEach(neighbor -> {
            if (!visited[neighbor]) {
                List<Integer> farthestFromNeighbor = dfsFindFarthest(neighbor, visited);
                if (farthestFromNeighbor.size() > result.size()) {
                    result.clear();
                    result.addAll(farthestFromNeighbor);
                }
            }
        });
        return result; // returnăm nodul cel mai îndepărtat
    }

    // Funcția DFS care păstrează traseul și găsește cel mai lung drum
    private void dfsFindFarthestWithPath(int node, boolean[] visited, int distance, int[] maxDistance, List<Integer> currentPath, List<Integer> longestPath) {
        visited[node] = true;
        currentPath.add(node); // Adăugăm nodul curent la traseu

        // Dacă am găsit un drum mai lung, actualizăm drumul cel mai lung
        if (distance > maxDistance[0]) {
            maxDistance[0] = distance;
            longestPath.clear();
            longestPath.addAll(currentPath); // Copiem traseul curent în cel mai lung drum
        }

        // Continuăm DFS-ul pentru vecini
        adjList.get(node).forEach(neighbor -> {
            if (!visited[neighbor]) {
                dfsFindFarthestWithPath(neighbor, visited, distance + 1, maxDistance, currentPath, longestPath);
            }
        });

        // După ce terminăm vizita, scoatem nodul curent din traseu
        currentPath.remove(currentPath.size() - 1);
    }
}