package com.example.authentication.dto;

import java.util.List;

public class ManagerStatsDTO {
    private List<ManagerDTO> managers;
    private long totalManagers;
    private long activeManagers;
    private long pendingManagers;

    public ManagerStatsDTO(List<ManagerDTO> managers, long totalManagers, long activeManagers, long pendingManagers) {
        this.managers = managers;
        this.totalManagers = totalManagers;
        this.activeManagers = activeManagers;
        this.pendingManagers = pendingManagers;
    }

    // Getters and setters
    public List<ManagerDTO> getManagers() {
        return managers;
    }

    public void setManagers(List<ManagerDTO> managers) {
        this.managers = managers;
    }

    public long getTotalManagers() {
        return totalManagers;
    }

    public void setTotalManagers(long totalManagers) {
        this.totalManagers = totalManagers;
    }

    public long getActiveManagers() {
        return activeManagers;
    }

    public void setActiveManagers(long activeManagers) {
        this.activeManagers = activeManagers;
    }

    public long getPendingManagers() {
        return pendingManagers;
    }

    public void setPendingManagers(long pendingManagers) {
        this.pendingManagers = pendingManagers;
    }
}
