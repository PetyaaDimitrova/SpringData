package com.softuni.SpringMVC.controllers;

import com.softuni.SpringMVC.services.CompanyService;
import com.softuni.SpringMVC.services.EmployeeService;
import com.softuni.SpringMVC.services.ProjectService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class HomeController {

    private final ProjectService projectService;
    private final EmployeeService employeeService;
    private final CompanyService companyService;

    public HomeController(ProjectService projectService, EmployeeService employeeService, CompanyService companyService) {
        this.projectService = projectService;
        this.employeeService = employeeService;
        this.companyService = companyService;
    }

    @GetMapping("/home")
    public ModelAndView index(){
        boolean areImported = projectService.areImported() && employeeService.areImported()
                && companyService.areImported();

        ModelAndView modelAndView = new ModelAndView("home");

        modelAndView.addObject("title", "Some title");
        modelAndView.addObject("areImported", areImported);
        return modelAndView;
    }


}
