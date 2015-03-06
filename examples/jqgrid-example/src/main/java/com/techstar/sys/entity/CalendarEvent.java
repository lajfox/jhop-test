package com.techstar.sys.entity;


/***
 * FullCalendarEvents 日历事件
 * @author lrm
 *
 */
public class CalendarEvent {
    private String id;//  可选，事件唯一标识，重复的事件具有相同的

    private String title;//必须，事件在日历上显示的title

    private boolean allDay = true;//可选，是否是整日事件

    private String start;//必须，事件的开始时间

    private String end;//可选，结束时间

    private String url;//可选，当指定后，事件被点击将打开对应url

    private String className;//指定事件的样式

    private boolean editable = false;//是否可拖拽

    private String source;//指向次event的eventsource对象

    private String color;//背景和边框颜色

    private String backgroundColor;//背景颜色

    private String borderColor;//边框颜色

    private String textColor;//文本颜色
    
    public CalendarEvent(){
	
    }

    public CalendarEvent(String title, String start) {
	this.title = title;
	this.start = start;
    }
    
    public CalendarEvent(String title, String start,String url,String className) {
	this.title = title;
	this.start = start;
	this.url = url;
	this.className = className;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isAllDay() {
        return allDay;
    }

    public void setAllDay(boolean allDay) {
        this.allDay = allDay;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public boolean isEditable() {
        return editable;
    }

    public void setEditable(boolean editable) {
        this.editable = editable;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(String backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public String getBorderColor() {
        return borderColor;
    }

    public void setBorderColor(String borderColor) {
        this.borderColor = borderColor;
    }

    public String getTextColor() {
        return textColor;
    }

    public void setTextColor(String textColor) {
        this.textColor = textColor;
    }
    
    
}
