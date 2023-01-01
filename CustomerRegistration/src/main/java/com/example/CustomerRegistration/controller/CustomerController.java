package com.example.CustomerRegistration.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import com.example.CustomerRegistration.service.CustomerService;
import com.example.CustomerRegistration.model.Customer;

@Controller
public class CustomerController {

	@Autowired
	private CustomerService customerService;

	@GetMapping("/")
	public String viewHomePage(Model model) {
		model.addAttribute("listCustomer", customerService.findbyAllOrderByIdDesc());
		return "Customerlist";
	}

	@GetMapping("/showForm")
	public String showForm(Model model) {
		Customer customer = new Customer();
		model.addAttribute("customer", customer);
		return "CustomerAdd";
	}

	@PostMapping("/saveCustomer")
	public String saveCustomer(@ModelAttribute("customer") Customer customer, Model model) {
		boolean emailResult = customerService.existsEmail(customer.getEmail());
		boolean mobileResult = customerService.existsMobile(customer.getMobile());
		String error = "";
		if (customer.getId() == 0) {
			if (mobileResult == true) {
				error = "Mobile already exists";
				model.addAttribute("mobileError", error);
				return "CustomerAdd";
			}
			if (emailResult == true) {
				error = "Email already exists";
				model.addAttribute("emailError", error);
				return "CustomerAdd";
			}
		}
		if (customer.getId() != 0) {
			Customer cust = customerService.getCustomerById(customer.getId());
			if (mobileResult == true && !(customer.getMobile().equals(cust.getMobile()))) {
				error = "Mobile already exists";
				model.addAttribute("mobileError", error);
				return "CustomerAdd";
			}
			if (emailResult == true && !(customer.getEmail().equals(cust.getEmail()))) {
				error = "Email already exists";
				model.addAttribute("emailError", error);
				return "CustomerAdd";
			}
		}
		customerService.saveCustomer(customer);
		return "redirect:/";
	}

	@GetMapping("/showFormForUpadte/{id}")
	public String showFormForUpdate(@PathVariable(value = "id") int id, Model model) {
		Customer customer = customerService.getCustomerById(id);
		model.addAttribute("customer", customer);
		return "CustomerAdd";
	}

	@GetMapping("/deleteCustomer/{id}")
	public String deleteCustomer(@PathVariable(value = "id") int id) {
		customerService.deleteCustomerById(id);
		return "redirect:/";
	}

}