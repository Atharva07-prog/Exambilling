package com.example.Exambilling;

public class AssignedBundle {
    private final int bundleId;
    private final String bundleName;
    private final int paperId;
    private final String paperName;

    public AssignedBundle(int bundleId, String bundleName, int paperId, String paperName) {
        this.bundleId = bundleId;
        this.bundleName = bundleName;
        this.paperId = paperId;
        this.paperName = paperName;
    }

    public int getBundleId() { return bundleId; }
    public String getBundleName() { return bundleName; }
    public int getPaperId() { return paperId; }
    public String getPaperName() { return paperName; }
}
