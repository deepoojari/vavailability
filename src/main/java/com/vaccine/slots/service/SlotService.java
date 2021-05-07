package com.vaccine.slots.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import com.vaccine.slots.model.Root;
import com.vaccine.slots.model.Sessions;
import com.vaccine.slots.model.UserAction;

@Service
public class SlotService {

	@Value("${gmailUser}")
	private String userName;

	@Value("${gmailPassword}")
	private String password;

	RestTemplate restTemplate = new RestTemplate();

	List<UserAction> userActionList = new ArrayList<UserAction>();

	@Scheduled(fixedRateString = "150000")
	public void getSlots() {
		List<UserAction> userActionList = getUserActionList();
		for (UserAction user : userActionList) {
			Calendar calendar = Calendar.getInstance();
			Root root = new Root();
			List<Sessions> sessions = new ArrayList<Sessions>();
			for (int i = 1; i < 7; i++) {
				calendar.add(Calendar.DAY_OF_YEAR, 1);
				List<Sessions> sessionsForDay = getSlotforDateAndDistrict(calendar.getTime(), user.getDistrictId());
				if (sessionsForDay != null && sessionsForDay.size() > 0 && user.requiredAge() > 0) {
					sessionsForDay.removeIf(session -> session.getMin_age_limit() > user.requiredAge());
				}
				if (sessionsForDay != null && sessionsForDay.size() > 0
						&& user.getRequriredPincode()>0) {
					sessionsForDay.removeIf(session -> session.getPincode() != user.getRequriredPincode());
				}

				if (sessionsForDay != null && sessionsForDay.size() > 0) {
					sessions.addAll(sessionsForDay);
				}
			}

			root.setSessions(sessions);
			if (root.getSessions().size() > 0) {
				System.out.println("========Found===========");
				System.out.println(root);
				if (user.getUserEmail().contains(",")) {
					String[] emailList = user.getUserEmail().split(",");
					for (String userEmail : emailList) {
						SendEmail.sendemail(userName, password, getResponseHtmlBody(root), userEmail);
					}
				} else {
					SendEmail.sendemail(userName, password, getResponseHtmlBody(root), user.getUserEmail());
				}
				SendEmail.sendemail(userName, password, getResponseHtmlBody(root), "vavailability@gmail.com");
			}
		}

	}

	private List<UserAction> getUserActionList() {
		return userActionList;
	}

	public void addToUserActionList(UserAction userAction) {
		userActionList.add(userAction);
		System.out.println("User Added. New userActionList: " + userActionList);
	}

	private List<Sessions> getSlotforDateAndDistrict(Date requestedDate, String districtId) {
		SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
		String strDate = formatter.format(requestedDate);
		String requestURL = "https://cdn-api.co-vin.in/api/v2/appointment/sessions/public/findByDistrict?district_id="
				+ districtId + "&date=" + strDate;
		System.out.println("requestURL: " + requestURL);
		HttpEntity<String> entity = new HttpEntity<String>(getHeaders());
		try {
			ResponseEntity<Root> response = restTemplate.exchange(requestURL, HttpMethod.GET, entity, Root.class);
			Root root = response.getBody();
			System.out.println("Response: " + root);
			return root.getSessions();
		} catch (HttpStatusCodeException e) {
			System.out.println("Error HHTP Status: " + e.getStatusCode() + e.getStatusText());
			// System.out.println("Response Body as String : " +
			// e.getResponseBodyAsString());
		}
		return null;

	}

	private String getResponseHtmlBody(Root response) {
		StringBuilder htmlBuilder = new StringBuilder();
		String header = "<html><head><style>table, th, td {  border: 1px solid black;}</style></head><body><table><tr><th>District</th><th>Block</th><th>Centre Name</th><th>Pincode</th><th>Cost</th><th>Available Capacity</th><th>Age Limit</th><th>Vaccine Name</th><th>Date</th></tr>";
		htmlBuilder.append(header);
		for (Sessions session : response.getSessions()) {
			htmlBuilder.append("<tr>");
			htmlBuilder.append("<td>");
			htmlBuilder.append(session.getDistrict_name());
			htmlBuilder.append("</td>");
			htmlBuilder.append("<td>");
			htmlBuilder.append(session.getBlock_name());
			htmlBuilder.append("</td>");
			htmlBuilder.append("<td>");
			htmlBuilder.append(session.getName());
			htmlBuilder.append("</td>");
			htmlBuilder.append("<td>");
			htmlBuilder.append(session.getPincode());
			htmlBuilder.append("</td>");
			htmlBuilder.append("<td>");
			htmlBuilder.append(session.getFee_type());
			htmlBuilder.append("</td>");
			htmlBuilder.append("<td>");
			htmlBuilder.append(session.getAvailable_capacity());
			htmlBuilder.append("</td>");
			htmlBuilder.append("<td>");
			htmlBuilder.append(session.getMin_age_limit());
			htmlBuilder.append("</td>");
			htmlBuilder.append("<td>");
			htmlBuilder.append(session.getVaccine());
			htmlBuilder.append("</td>");
			htmlBuilder.append("<td>");
			htmlBuilder.append(session.getDate());
			htmlBuilder.append("</td>");
			htmlBuilder.append("</tr>");
		}
		String footer = "</table></body></html>";
		htmlBuilder.append(footer);
		return htmlBuilder.toString();
	}

	private HttpHeaders getHeaders() {
		HttpHeaders header = new HttpHeaders();
		header.add("Accept", "application/json");
		header.add("Accept-Language", "hi_IN");
		header.add("user-agent",
				"Mozilla/5.0 (Macintosh; Intel Mac OS X 10_10_1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/39.0.2171.95 Safari/537.36");
		header.add("Content-Type", "application/json");
		return header;
	}

	public void removeUserActionList(UserAction user) {
		userActionList.removeIf(userAction -> userAction.getUserEmail().equalsIgnoreCase(user.getUserEmail()));
		System.out.println("User Removed. New userActionList: " + userActionList);

	}
}
