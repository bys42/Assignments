/* *****************************************************************************
 *  Name: BYS
 *  Date: 2019/03/18
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.FlowEdge;
import edu.princeton.cs.algs4.FlowNetwork;
import edu.princeton.cs.algs4.FordFulkerson;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.StdOut;

import java.util.HashMap;
import java.util.Map;

public class BaseballElimination {
    private final Map<String, Integer> teamIdMap = new HashMap<>();
    private final Map<String, Queue<String>> certOfElim = new HashMap<>();
    private final int teamNum;
    private final int[][] againstTable;
    private final int[] winMap;
    private final int[] lossMap;
    private final int[] remainMap;

    // create a baseball division from given filename in format specified below
    public BaseballElimination(String filename) {
        In gameRecord = new In(filename);

        teamNum = gameRecord.readInt();
        againstTable = new int[teamNum][teamNum];
        winMap = new int[teamNum];
        lossMap = new int[teamNum];
        remainMap = new int[teamNum];

        String[] teamNames = new String[teamNum];
        int teamId = 0;
        while (!gameRecord.isEmpty()) {
            String teamName = gameRecord.readString();

            winMap[teamId] = gameRecord.readInt();
            lossMap[teamId] = gameRecord.readInt();
            remainMap[teamId] = gameRecord.readInt();
            for (int i = 0; i < teamNum; i++) {
                againstTable[teamId][i] = gameRecord.readInt();
            }
            teamNames[teamId] = teamName;
            teamIdMap.put(teamName, teamId++);
        }

        final int sId = 0;
        final int tId = teamNum * (teamNum - 1) / 2 + 1;
        final int teamStart = tId - teamNum + 1;

        for (String teamName : teamIdMap.keySet()) {
            Queue<String> cert = new Queue<>();
            certOfElim.put(teamName, cert);
            teamId = teamIdMap.get(teamName);

            int maxWin = winMap[teamId] + remainMap[teamId];
            for (String team2 : teamIdMap.keySet()) {
                int team2Id = teamIdMap.get(team2);
                if (winMap[team2Id] > maxWin) cert.enqueue(team2);
            }
            if (!cert.isEmpty()) continue;

            FlowNetwork gameFlow = new FlowNetwork(tId + 1);

            int gameId = 1;
            for (int i = 0; i < teamNum - 1; i++) {
                int id1 = (i == teamId) ? teamNum - 1 : i;
                gameFlow.addEdge(new FlowEdge(teamStart + i, tId, maxWin - winMap[id1]));
                for (int j = 0; j < i; j++) {
                    int id2 = (j == teamId) ? teamNum - 1 : j;
                    gameFlow.addEdge(new FlowEdge(sId, gameId, againstTable[id1][id2]));
                    gameFlow.addEdge(new FlowEdge(gameId, teamStart + i, Double.POSITIVE_INFINITY));
                    gameFlow.addEdge(new FlowEdge(gameId, teamStart + j, Double.POSITIVE_INFINITY));
                    gameId++;
                }
            }

            FordFulkerson maxFlow = new FordFulkerson(gameFlow, sId, tId);
            for (int i = 0; i < teamNum - 1; i++) {
                int id = (i == teamId) ? teamNum - 1 : i;
                if (maxFlow.inCut(teamStart + i)) cert.enqueue(teamNames[id]);
            }
        }
    }

    // number of teams
    public int numberOfTeams() {
        return teamNum;
    }

    // all teams
    public Iterable<String> teams() {
        return teamIdMap.keySet();
    }

    // number of wins for given team
    public int wins(String team) {
        if (!teamIdMap.containsKey(team)) throw new IllegalArgumentException();

        return winMap[teamIdMap.get(team)];
    }

    // number of losses for given team
    public int losses(String team) {
        if (!teamIdMap.containsKey(team)) throw new IllegalArgumentException();

        return lossMap[teamIdMap.get(team)];
    }

    // number of remaining games for given team
    public int remaining(String team) {
        if (!teamIdMap.containsKey(team)) throw new IllegalArgumentException();

        return remainMap[teamIdMap.get(team)];
    }

    // number of remaining games between team1 and team2
    public int against(String team1, String team2) {
        if (!teamIdMap.containsKey(team1)) throw new IllegalArgumentException();
        if (!teamIdMap.containsKey(team2)) throw new IllegalArgumentException();

        return againstTable[teamIdMap.get(team1)][teamIdMap.get(team2)];
    }

    // is given team eliminated?
    public boolean isEliminated(String team) {
        if (!certOfElim.containsKey(team)) throw new IllegalArgumentException();
        return !certOfElim.get(team).isEmpty();
    }

    // subset R of teams that eliminates given team; null if not eliminated
    public Iterable<String> certificateOfElimination(String team) {
        if (!certOfElim.containsKey(team)) throw new IllegalArgumentException();
        Queue<String> cert = certOfElim.get(team);

        return cert.isEmpty() ? null : cert;
    }

    public static void main(String[] args) {
        BaseballElimination division = new BaseballElimination(args[0]);
        for (String team : division.teams()) {
            if (division.isEliminated(team)) {
                StdOut.print(team + " is eliminated by the subset R = { ");
                for (String t : division.certificateOfElimination(team)) {
                    StdOut.print(t + " ");
                }
                StdOut.println("}");
            }
            else {
                StdOut.println(team + " is not eliminated");
            }
        }
    }
}
