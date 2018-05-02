package com.bravadohealth.clara1.enums;

public enum SupportedTimeZoneEnum {

	SAMOA("US/Samoa", "(GMT-11:00) US/Samoa", -11),
	HAWAII("US/Hawaii", "(GMT-10:00) US/Hawaii", -10),
	ALASKA("US/Alaska", "(GMT-9:00) US/Alaska", -9),
	PACIFIC("US/Pacific", "(GMT-8:00) US/Pacific", -8),
	MOUNTAIN("US/Mountain", "(GMT-7:00) US/Mountain", -7),
	CENTRAL("US/Central", "(GMT-6:00) US/Central", -6),
	EASTERN("US/Eastern", "(GMT-5:00) US/Eastern", -5),
    ATLANTIC("Atlantic/Bermuda", "(GMT-4:00) Atlantic/Bermuda", -5),
    CHAMORRO("Pacific/Guam", "(GMT+10:00) Pacific/Guam", 10),
    ;

    private final String value;
    private final String label;
    private final int offsetInHours;

    private SupportedTimeZoneEnum(String value, String label, int offsetInHours) {
        this.value = value;
        this.label = label;
        this.offsetInHours = offsetInHours;
    }
    
    public int getOffsetInHours() {
		return offsetInHours;
	}

	public String getLabel() {
        return this.label;
    }

	public String getValue() {
        return this.value;
    }
}
