package com.projam.projambackend.dto;

import java.util.List;

public class TeamStats {

    private int totalMembers;
    private List<MemberResponse> activeMembersList;
    private int activeMembers;

    public TeamStats() {
    }

	public int getTotalMembers() {
		return totalMembers;
	}

	public void setTotalMembers(int totalMembers) {
		this.totalMembers = totalMembers;
	}

	public List<MemberResponse> getActiveMembersList() {
		return activeMembersList;
	}

	public void setActiveMembersList(List<MemberResponse> activeMembersList) {
		this.activeMembersList = activeMembersList;
	}

	public int getActiveMembers() {
		return activeMembers;
	}

	public void setActiveMembers(int activeMembers) {
		this.activeMembers = activeMembers;
	}

	/**
	 * @param totalMembers
	 * @param activeMembersList
	 * @param activeMembers
	 */
	public TeamStats(int totalMembers, List<MemberResponse> activeMembersList, int activeMembers) {
		this.totalMembers = totalMembers;
		this.activeMembersList = activeMembersList;
		this.activeMembers = activeMembers;
	}

    
    
    
}
