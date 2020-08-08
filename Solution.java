import java.util.*;

/**
 * Leetcode #310. Minimum Height Trees
 * https://leetcode.com/problems/minimum-height-trees/
 */
class Solution {
    public List<Integer> findMinHeightTrees(int n, int[][] edges) {
        List<Integer> ans = new ArrayList<>();

        /**
         * Outdegree of that TreeNode
         */
        int[] outDegree = new int[n];

        /**
         * Key: Node
         * Value: Set of adjacent nodes which has indegree for the Key TreeNode.
         */
        Map<Integer, Set<Integer>> connects = new HashMap<>(2 * n);

        // Build directed graph and outdgree map
        for (int[] edge : edges) {

            // Changing undirected graph into directed graph by double store the same edge from 2 directions.
            Set<Integer> set = connects.getOrDefault(edge[0], new HashSet<>());
            set.add(edge[1]);
            connects.put(edge[0], set);

            set = connects.getOrDefault(edge[1], new HashSet<>());
            set.add(edge[0]);
            connects.put(edge[1], set);

            outDegree[edge[0]]++;
            outDegree[edge[1]]++;
        }

        // This directed graph actually is an forest, which has isolated nodes.
        for (int i = 0; i < n; i++) {
            if (outDegree[i] == 0) {
                ans.add(i);
            }
        }
        if (!ans.isEmpty()) {
            return ans;
        }

        // Peeling the onion layer by layer
        Queue<Integer> bfs = new LinkedList<>();
        for (int i = 0; i < n; i++) {
            if (outDegree[i] == 1) {
                bfs.offer(i);
            }
        }

        // Remove the outmost layer (leaves) nodes layer by layer
        while (!bfs.isEmpty()) {
            int size = bfs.size();

            // List of leaves nodes, which each have only 1 outdegree.
            List<Integer> leaves = new ArrayList<>();

            // Traverse all outmost nodes
            for (int i = 0; i < size; i++) {
                int leaf = bfs.poll();
                leaves.add(leaf);

                // Remove the nodes in the outmost layer
                outDegree[leaf]--;

                // Find the parent node of current node and update their outdegrees
                Set<Integer> set = connects.get(leaf);
                for (int parent : set) {
                    if (outDegree[parent] > 0) {
                        outDegree[parent]--;
                        if (outDegree[parent] == 1) {
                            bfs.offer(parent);
                        }
                    }
                }
            }

            // We should always replace the ans as the list of outmost layer nodes
            // to avoid missing the record of the innermost core.
            ans = leaves;
        }
        return ans;
    }
}