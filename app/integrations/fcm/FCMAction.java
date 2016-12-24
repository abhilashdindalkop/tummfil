package integrations.fcm;

import integrations.fcm.NotificationConstants.FCMActionType;
import integrations.fcm.NotificationConstants.FCMNavigationType;

public class FCMAction {

	String name;

	String icon;

	FCMActionType actionType;

	FCMNavigationType navigationType;

	String navigationInfo;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public FCMActionType getActionType() {
		return actionType;
	}

	public void setActionType(FCMActionType actionType) {
		this.actionType = actionType;
	}

	public FCMNavigationType getNavigationType() {
		return navigationType;
	}

	public void setNavigationType(FCMNavigationType navigationType) {
		this.navigationType = navigationType;
	}

	public String getNavigationInfo() {
		return navigationInfo;
	}

	public void setNavigationInfo(String navigationInfo) {
		this.navigationInfo = navigationInfo;
	}

}
