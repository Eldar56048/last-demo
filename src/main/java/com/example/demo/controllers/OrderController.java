package com.example.demo.controllers;

import com.example.demo.models.*;
import com.example.demo.repo.*;
import com.example.demo.smsc.Smsc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
public class OrderController {
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private TypeRepository typeRepository;
    @Autowired
    private Smsc smsc;
    @Autowired
    private ServiceRepository serviceRepository;
    @Autowired
    private StorageRepository storageRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private RecievingHistoryRepository recievingHistoryRepository;
    @Autowired
    private ModelRepository modelRepository;
    @PostMapping("/orders/add")
    public String ordersAdd(@AuthenticationPrincipal User user,@RequestParam String client_name,@RequestParam String client_number, @RequestParam String problem, @RequestParam Long type_id, @RequestParam Long model_id, Model model){
        System.out.println("Hello");
        System.out.println(client_name+" "+client_number+" "+problem);
        Type type = typeRepository.getAllById(type_id);
        com.example.demo.models.Model model1 = modelRepository.getAllById(model_id);
        Order order = new Order(client_name,client_number,problem,user,type,model1);
        orderRepository.save(order);
        return "redirect:/orders";
    }

    @GetMapping("/orders/{id}")
    public String orderById(@PathVariable(value = "id") long id, Model model){
        Optional<Order> order = orderRepository.findById(id);
        ArrayList<Order> result = new ArrayList<>();
        order.ifPresent(result::add);
        model.addAttribute("order",result);
        return "order-info";
    }
    @GetMapping("/orders/{id}/update")
    public String orderUpdateGet(@PathVariable(value = "id") long id,Model model){
        model.addAttribute("order",id);
        Iterable<Service> services = serviceRepository.findAll();
        model.addAttribute("services",services);
        return "order-update";
    }

    @GetMapping("/orders/{id}/add/product")
    public String addProductToOrder(@PathVariable(value = "id") long id, Model model){
        Order order = orderRepository.getAllById(id);
        Iterable<Product> products = productRepository.findAll();
        model.addAttribute("products",products);
        model.addAttribute("order",order);
        return "order-add-product";
    }

    @PostMapping("/orders/{id}/add/product")
    public String addProductToOrderPost(@PathVariable(value = "id") long id,@RequestParam long product_id,@RequestParam int quantity, Model model){
        Order order = orderRepository.getAllById(id);
        Product product = productRepository.getAllById(product_id);
        Storage storage = storageRepository.getAllByProductId(product_id);
        if(storage.getQuantity()<quantity){
            String message = "Не хватает количество товара вы требуете "+quantity+" товара но на складе доступно только "+storage.getQuantity();
            model.addAttribute("message",message);
            return "redirect:/orders/"+id+"/add/product";
        }
        OrderItem orderItem = new OrderItem(quantity,order,product);
        order.addOrderItem(orderItem);
        storage.setQuantity(storage.getQuantity()-quantity);
        RecievingHistory recievingHistory = new RecievingHistory(orderItem);
        storageRepository.save(storage);
        orderRepository.save(order);
        recievingHistoryRepository.save(recievingHistory);
        return "redirect:/orders/"+id;
    }

    @PostMapping("/orders/{id}/add")
    public String addService(@PathVariable(value = "id")long id,@RequestParam long service_id,@RequestParam int quantity,Model model) throws ClassNotFoundException {
        Order order = orderRepository.findById(id).orElseThrow(()-> new ClassNotFoundException());
        Service service = serviceRepository.findById(service_id).orElseThrow(()->new ClassNotFoundException());
        order.addOrderItem(new OrderItem(quantity,order,service));
        orderRepository.save(order);
        return "redirect:/orders/"+order.getId();
    }
    @GetMapping("/orders/{id}/done")
    public String orderDone(@AuthenticationPrincipal User user,@PathVariable(value = "id") long id, Model model) throws ClassNotFoundException {
        Order order = orderRepository.findById(id).orElseThrow(()-> new ClassNotFoundException());
        order.setStatus(Status.DONE);
        order.setDoneUser(user);
        orderRepository.save(order);
        String message = "Ваш заказ №"+order.getId()+" готов \n"+
                "Кто сделал: "+order.getDoneUser().getFname()+" "+order.getDoneUser().getLname()+"\n"+
                "Цена: "+order.getPrice()+"\n"+
                "С уважением команда WEB-PORTAL";
        smsc.send_sms(order.getClient_number(),message,0, "", "", 0, "", "");
        return "redirect:/orders/"+id;
    }

    @GetMapping("/orders/{id}/given")
    public String orderGiven(@AuthenticationPrincipal User user,@PathVariable(value = "id") long id, Model model) throws ClassNotFoundException {
        Order order = orderRepository.findById(id).orElseThrow(()-> new ClassNotFoundException());
        order.setGave_date(new Date());
        order.setStatus(Status.GIVEN);
        order.setGiveUser(user);
        orderRepository.save(order);
        return "redirect:/orders/"+id;
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/orders/{id}/remove")
    public String orderRemove(@PathVariable(value = "id")long id,Model model) throws ClassNotFoundException {
        orderRepository.deleteById(id);
        return "redirect:/orders";
    }

    @GetMapping("/orders/{id}/remove/{item_id}")
    public String orderItemsRemove(@PathVariable(value = "id")long id,@PathVariable(value = "item_id")long item_id,Model model) throws ClassNotFoundException {
        Order order = orderRepository.findById(id).orElseThrow(()-> new ClassNotFoundException());
        OrderItem orderItem = order.getOrderItemById(item_id);
        if(orderItem.getService()!=null) {
            order.removeOrderItemById(item_id);
            orderRepository.save(order);
        }
        else{
            order.removeOrderItemById(item_id);
            RecievingHistory recievingHistory = recievingHistoryRepository.getAllByOrderItemId(orderItem.getId());
            recievingHistoryRepository.deleteById(recievingHistory.getId());
            Storage storage = storageRepository.getAllByProductId(orderItem.getProduct().getId());
            storage.setQuantity(storage.getQuantity()+orderItem.getQuantity());
            storageRepository.save(storage);
            orderRepository.save(order);
        }
        return "redirect:/orders/"+order.getId();
    }

    @GetMapping("/orders/{id}/QRCode")
    public String getOrderQRCode(@PathVariable(value = "id") long id,Model model){
        Order order = orderRepository.getAllById(id);
        model.addAttribute("order",order);
        return "order-qr";
    }

    @GetMapping("/orders/search/notdone")
    public String getNotDoneOrders(Model model){
        Iterable<Order> orders = orderRepository.findAllByStatus(Status.NOTDONE);
        model.addAttribute("orders",orders);
        return "orders";
    }

    @GetMapping("/orders/search/done")
    public String getDoneOrders(Model model){
        Iterable<Order> orders = orderRepository.findAllByStatus(Status.DONE);
        model.addAttribute("orders",orders);
        return "orders";
    }

    @GetMapping("/orders/search/given")
    public String getGivenOrders(Model model){
        Iterable<Order> orders = orderRepository.findAllByStatus(Status.GIVEN);
        model.addAttribute("orders",orders);
        return "orders";
    }

}
