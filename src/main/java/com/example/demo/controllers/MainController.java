package com.example.demo.controllers;

import com.example.demo.models.*;
import com.example.demo.repo.*;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;

@Controller
public class MainController {
    @Autowired
    private TypeRepository typeRepository;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private ModelRepository modelRepository;
    @Autowired
    private ServiceRepository serviceRepository;
    @GetMapping("/orders")
    public String orders(Model model){
        Iterable<Type> types = typeRepository.findAll();
        Iterable<Order> orders = orderRepository.findByOrderByIdDesc();
        Iterable<com.example.demo.models.Model> models = modelRepository.findAll();
        model.addAttribute("orders",orders);
        model.addAttribute("types",types);
        model.addAttribute("models",models);
        return "orders";
    }

    @GetMapping("/")
    public String main(Model model){
        model.addAttribute("order",new Order());
        return "profile";
    }

    @GetMapping("/reg")
    public String reg(Model model){
        return "reg";
    }

    /*@PostMapping("/reg")
    public String registration(User user,Model model){
        user.setRoles(Collections.singleton(Role.USER));
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setEnabled(true);
        userRepository.save(user);
        return "redirect:/login";
    }*/

    @GetMapping("/roles")
    public String getUserRoles(@AuthenticationPrincipal User user,Model model){
        for(Role role : user.getRoles()){
            System.out.println(role.toString());
        }
        return "redirect:/orders";
    }

    /*@GetMapping("/read")
    public String readPrice(Model model) throws IOException {
        File excelFile = new File("price.xlsx");
        FileInputStream fis = new FileInputStream(excelFile);
        XSSFWorkbook workbook = new XSSFWorkbook(fis);
        XSSFSheet sheet = workbook.getSheetAt(0);

        Iterator<Row> rowIterator = sheet.iterator();
        while(rowIterator.hasNext()){
            Row row = rowIterator.next();
            Service service = new Service();
            Iterator<Cell> cellIterator = row.cellIterator();
            while(cellIterator.hasNext()){
                service.setService_name(cellIterator.next().toString());
                service.setPrice((int)Float.parseFloat(cellIterator.next().toString()));
                service.setService_description(cellIterator.next().toString());
                System.out.println(service.getService_name()+" "+service.getPrice());

            }
            serviceRepository.save(service);
        }

        return "redirect:/orders";
    }*/


}
