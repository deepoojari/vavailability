package com.vaccine.slots.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.vaccine.slots.model.UserAction;
import com.vaccine.slots.service.SlotService;

@RestController
public class SlotController {

	@Autowired
	SlotService service;

	@RequestMapping(method = RequestMethod.POST, value = "/addUser", consumes = "application/json")
	public String getSlots(@RequestBody UserAction userAction) {
		System.out.println("userAction: " + userAction);
		service.addToUserActionList(userAction);
		System.out.println("userAction Added");
		return null;

	}
	
	@RequestMapping(method = RequestMethod.POST, value = "/removeUser", consumes = "application/json")
	public String removeUser(@RequestBody UserAction userAction) {
		System.out.println("userAction: " + userAction);
		service.removeUserActionList(userAction);
		System.out.println("userAction Removed");
		return null;

	}

}
