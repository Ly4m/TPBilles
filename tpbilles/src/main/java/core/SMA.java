package core;

import java.util.*;

/**
 * Created by William on 19/01/2016.
 */
public class SMA {

    private Environnement env;
    private List<Agent> agents;
    private Set<Agent> agentsASupprimer;
    private Set<Agent> agentsAAjouter;
    private boolean shuffle = true;

    public SMA(Environnement env, boolean shuffle) {
        this(env);
        this.shuffle = shuffle;
    }

    public SMA(Environnement env) {
        super();
        this.env = env;
        this.agents = new ArrayList<Agent>();
        this.agentsASupprimer = new HashSet<Agent>();
        this.agentsAAjouter = new HashSet<Agent>();
    }

    public void run() {

        long start = System.currentTimeMillis();

        update();
        
        if (shuffle) {
            Collections.shuffle(agents);
        }
        for (Agent agent : agents) {
            agent.decide();
        }

//        System.out.println(System.currentTimeMillis() - start + " agenst : "  + agents.size());

    }

    public int count(boolean b){
        if(b){
            return 1;
        } else {
            return 0;
        }
    }

    public Environnement getEnv() {
        return env;
    }

    public void setEnv(Environnement env) {
        this.env = env;
    }

    public List<Agent> getAgents() {
        return Collections.unmodifiableList(this.agents);
    }

    public void addAgent(Agent agent) {
        if (agent.isPhysique()) { // On n'ajoute à l'environnement que les
            // agentsPhysiques
            env.addAgent((AgentPhysique) agent);
        }
        this.agents.add(0, agent);
    }

    public void addAgentApres(Agent agent) {
        this.agentsAAjouter.add(agent);
    }

    public void removeAgentApres(Agent agent) {
        this.agentsASupprimer.add(agent);
    }

    public void removeAgentApres(Set<Agent> agents) {
        this.agentsASupprimer.addAll(agents);
    }

    public void update() {
        this.agents.addAll(agentsAAjouter);
        this.agentsAAjouter.clear();
        for (Agent a : agentsASupprimer) {
            this.removeAgent(a);
        }
        this.agentsASupprimer.clear();

    }

    private void removeAgent(Agent agent) {
        this.agents.remove(agent);
    }
}
