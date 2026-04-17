package service.BoatStatusHandler;

import java.util.Map;

public interface BoatStatusHandler {
    public Map<String, Boolean> loadStatuses();
    public void saveBoatStatuses(Map<String, Boolean> statuses);
}
