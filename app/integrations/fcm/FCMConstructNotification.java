package integrations.fcm;

import java.util.Date;
import java.util.HashMap;

import integrations.fcm.NotificationConstants.FCMActionType;
import integrations.fcm.NotificationConstants.FCMNavigationType;

public class FCMConstructNotification {

	public FCMNotification simpleNotification(String title, String body, String navigationInfo,
			FCMNavigationType navType, HashMap<String, Object> payload, boolean isSilent) {
		FCMNotification fcmNotification = new FCMNotification();
		fcmNotification.setTitle(title);
		fcmNotification.setBody(body);
		fcmNotification.setNavigationInfo(navigationInfo);
		fcmNotification.setType(navType);
		fcmNotification.setPayload(payload);
		fcmNotification.setSilent(isSilent);
		fcmNotification.setTimeStamp(new Date().getTime());
		return fcmNotification;
	}

	public FCMNotification imageNotification(String title, String body, String navigationInfo,
			FCMNavigationType navType, String imageUrl, boolean isSilent) {
		FCMNotification fcmNotification = new FCMNotification();
		fcmNotification.setTitle(title);
		fcmNotification.setBody(body);
		fcmNotification.setNavigationInfo(navigationInfo);
		fcmNotification.setType(navType);
		fcmNotification.setSilent(isSilent);
		fcmNotification.setTimeStamp(new Date().getTime());
		fcmNotification.setImageUrl(imageUrl);
		return fcmNotification;
	}

	public FCMNotification actionNotification(String title, String body, String navigationInfo,
			FCMNavigationType navType, boolean isSilent, String positiveNavInfo, FCMNavigationType positiveNavType,
			String negativeNavInfo, FCMNavigationType negativeNavType) {
		FCMNotification fcmNotification = new FCMNotification();
		fcmNotification.setTitle(title);
		fcmNotification.setBody(body);
		fcmNotification.setNavigationInfo(navigationInfo);
		fcmNotification.setType(navType);
		fcmNotification.setSilent(isSilent);

		fcmNotification.setTimeStamp(new Date().getTime());
		fcmNotification.setHasActions(true);
		FCMAction actionok = new FCMAction();
		actionok.setName("OK");
		actionok.setIcon("done");
		actionok.setActionType(FCMActionType.POSITIVE);
		actionok.setNavigationType(positiveNavType);
		actionok.setNavigationInfo(positiveNavInfo);

		FCMAction actioncancel = new FCMAction();
		actioncancel.setName("Cancel");
		actioncancel.setIcon("cancel");
		actioncancel.setActionType(FCMActionType.NEGATIVE);
		actioncancel.setNavigationType(negativeNavType);
		actioncancel.setNavigationInfo(negativeNavInfo);

		FCMAction[] actions = fcmNotification.getActions();
		actions[0] = actionok;
		actions[1] = actioncancel;

		fcmNotification.setActions(actions);
		return fcmNotification;
	}

	public FCMNotification imageActionNotification(String title, String body, String navigationInfo,
			FCMNavigationType navType, boolean isSilent, String imageUrl, String positiveNavInfo,
			FCMNavigationType positiveNavType, String negativeNavInfo, FCMNavigationType negativeNavType) {
		FCMNotification fcmNotification = new FCMNotification();
		fcmNotification.setTitle(title);
		fcmNotification.setBody(body);
		fcmNotification.setNavigationInfo(navigationInfo);
		fcmNotification.setType(navType);
		fcmNotification.setSilent(isSilent);
		fcmNotification.setTimeStamp(new Date().getTime());

		fcmNotification.setImageUrl(imageUrl);
		fcmNotification.setHasActions(true);
		FCMAction actionok = new FCMAction();
		actionok.setName("Call");
		actionok.setIcon("call");
		actionok.setActionType(FCMActionType.POSITIVE);
		actionok.setNavigationType(positiveNavType);
		actionok.setNavigationInfo(positiveNavInfo);

		FCMAction actioncancel = new FCMAction();
		actioncancel.setName("Direction");
		actioncancel.setIcon("direction");
		actioncancel.setActionType(FCMActionType.NEGATIVE);
		actioncancel.setNavigationType(negativeNavType);
		actioncancel.setNavigationInfo(negativeNavInfo);

		FCMAction[] actions = fcmNotification.getActions();
		actions[0] = actionok;
		actions[1] = actioncancel;

		fcmNotification.setActions(actions);
		return fcmNotification;
	}

}
