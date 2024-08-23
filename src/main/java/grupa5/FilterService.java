package grupa5;

import java.util.List;

public class FilterService 
{
    private static FilterService instance;
    private String startDate;
    private String endDate;
    private String startPrice;
    private String endPrice;
    private List<String> selectedLocations;

    private FilterService() {}

    public static synchronized FilterService getInstance() {
        if (instance == null) instance = new FilterService();
        return instance;
    }
    
    public String getStartDate() { return startDate; }
    public void setStartDate(String startDate) { this.startDate = startDate; }
    public String getEndDate() { return endDate; }
    public void setEndDate(String endDate) { this.endDate = endDate; }
    public String getStartPrice() { return startPrice; }
    public void setStartPrice(String startPrice) { this.startPrice = startPrice; }
    public String getEndPrice() { return endPrice; }
    public void setEndPrice(String endPrice) { this.endPrice = endPrice; }
    public List<String> getSelectedLocations() { return selectedLocations; }
    public void setSelectedLocations(List<String> selectedLocations) { this.selectedLocations = selectedLocations; }
}
