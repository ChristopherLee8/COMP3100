public class Server {
    String type;
    int serverID;
    String state;
    int startTime;
    int coreCount;
    int memory;
    int disk;

    public Server (String type, int serverID, String state, int startTime, int coreCount, int memory, int disk) {
        this.type = type;
        this.serverID = serverID;
        this.state = state;
        this.startTime = startTime;
        this.coreCount = coreCount;
        this.memory = memory;
        this.disk = disk;
    }
}