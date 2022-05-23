public class Job {
    int startTime;
    int jobID;
    int runTime;
    int core;
    int memory;
    int disk;

    public Job(int startTime, int jobID, int runTime, int core, int memory, int disk) {
        this.startTime = startTime;
        this.jobID = jobID;
        this.runTime = runTime;
        this.core = core;
        this.memory = memory;
        this.disk = disk;
    }
}