public class testCalendar {
    public static void main(String[] args) {

        var testCal = new Calendar("2014-02-14");
        testCal.showCalendar();
        var testDisplay = new CalendarDisplay(testCal);
        testDisplay.show();
    }
}