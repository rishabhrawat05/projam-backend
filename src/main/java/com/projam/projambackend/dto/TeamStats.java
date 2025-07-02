package com.projam.projambackend.dto;

public class TeamStats {

    private int totalMembers;
    private int activeMembers;

    public TeamStats() {
    }

    public int getTotalMembers() {
        return totalMembers;
    }

    public void setTotalMembers(int totalMembers) {
        this.totalMembers = totalMembers;
    }

    public int getActiveMembers() {
        return activeMembers;
    }

    public void setActiveMembers(int activeMembers) {
        this.activeMembers = activeMembers;
    }
    
    public TeamStats(int totalMembers, int activeMembers) {
        this.totalMembers = totalMembers;
        this.activeMembers = activeMembers;
    }
}
