package integrations.fcm;

import java.util.HashMap;

import integrations.fcm.NotificationConstants.FCMNavigationType;

public class FCMNotification {

	String title;

	String body;

	String imageUrl;

	/*
	 * 
	 * if Silent is true, notification will not be shown, but some background
	 * task will be triggered in the application.
	 * 
	 * for eg. background profile sync, logout user, sync config settings, etc.
	 * 
	 */

	boolean silent;

	HashMap<String, Object> payload;

	long timeStamp;

	long expiryTimeStamp;

	boolean hasActions;

	FCMNavigationType type;

	String navigationInfo;

	FCMAction[] actions;

	public FCMNotification() {

		payload = new HashMap<String, Object>();

		actions = new FCMAction[3];

		title = "";

		body = "";

		imageUrl = "";

		navigationInfo = "";

	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public boolean isSilent() {
		return silent;
	}

	public void setSilent(boolean silent) {
		this.silent = silent;
	}

	public HashMap<String, Object> getPayload() {
		return payload;
	}

	public void setPayload(HashMap<String, Object> payload) {
		if(payload != null){
			this.payload = payload;
		}
	}

	public long getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(long timeStamp) {
		this.timeStamp = timeStamp;
	}

	public long getExpiryTimeStamp() {
		return expiryTimeStamp;
	}

	public void setExpiryTimeStamp(long expiryTimeStamp) {
		this.expiryTimeStamp = expiryTimeStamp;
	}

	public boolean isHasActions() {
		return hasActions;
	}

	public void setHasActions(boolean hasActions) {
		this.hasActions = hasActions;
	}

	public FCMNavigationType getType() {
		return type;
	}

	public void setType(FCMNavigationType type) {
		this.type = type;
	}

	public String getNavigationInfo() {
		return navigationInfo;
	}

	public void setNavigationInfo(String navigationInfo) {
		this.navigationInfo = navigationInfo;
	}

	public FCMAction[] getActions() {
		return actions;
	}

	public void setActions(FCMAction[] actions) {
		this.actions = actions;
	}

}
