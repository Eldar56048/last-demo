package com.example.demo.controllers;

import com.example.demo.models.*;
import com.example.demo.repo.*;
import com.example.demo.services.OrderService;
import com.example.demo.smsc.Smsc;
import org.apache.poi.ss.usermodel.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.*;

@Controller
public class OrderController {
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private TypeRepository typeRepository;
    @Autowired
    private Smsc smsc;
    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private ServiceRepository serviceRepository;
    @Autowired
    private DiscountRepository discountRepository;
    @Autowired
    private StorageRepository storageRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private RecievingHistoryRepository recievingHistoryRepository;
    @Autowired
    private ModelRepository modelRepository;
    @Autowired
    private OrderService orderService;
    @Autowired
    private UserRepository userRepository;
    @PostMapping("/orders/add")
    public String ordersAdd(@AuthenticationPrincipal User user, @RequestParam String client_number,@RequestParam String clientId, @RequestParam String problem, @RequestParam Long type_id, @RequestParam Long model_id, Model model,@RequestParam Long discountId,@RequestParam String modelType) {
        Type type = typeRepository.getAllById(type_id);
        com.example.demo.models.Model model1 = modelRepository.getAllById(model_id);
        Discount discount = discountRepository.getAllById(discountId);
        Order order = null;
        Client client = null;
        if((discount.getDiscountName().equals("Стандартный")==false)&&(orderService.onlyDigits(clientId,clientId.length())==false)){
            String surname = "";
            String name = "";
            String[] words = clientId.split(" ");
            surname = words[0];
            name = words[1];
            client = new Client(name,surname,client_number,discount);
            clientRepository.save(client);
            order = new Order(client,problem,user,type,model1,modelType);
        }
        else if(orderService.onlyDigits(clientId,clientId.length())){
            client = clientRepository.getAllById(Long.parseLong(clientId));
            client.setDiscount(discount);
            client.setPhoneNumber(client_number);
            clientRepository.save(client);

            order = new Order(client,problem,user,type,model1,modelType);
        }
        else {
            order = new Order(clientId, client_number, problem, user, type, model1,modelType,discount.getDiscountName(),discount.getPercentage());
        }
        order.setNotified(false);
        orderRepository.save(order);
        return "redirect:/orders";
    }

    @GetMapping("/orders/{id}")
    public String orderById(@PathVariable(value = "id") long id, Model model) {
        Optional<Order> order = orderRepository.findById(id);
        ArrayList<Order> result = new ArrayList<>();
        order.ifPresent(result::add);
        model.addAttribute("order", result);
        return "order-info";
    }

    @GetMapping("/orders/{id}/update")
    public String orderUpdateGet(@PathVariable(value = "id") long id, Model model) {
        model.addAttribute("order", id);
        Iterable<Service> services = serviceRepository.findAll();
        model.addAttribute("services", services);
        return "order-update";
    }

    @GetMapping("/orders/{id}/add/product")
    public String addProductToOrder(@PathVariable(value = "id") long id, Model model) {
        Order order = orderRepository.getAllById(id);
        Iterable<Product> products = productRepository.findAll();
        model.addAttribute("products", products);
        model.addAttribute("order", order);
        return "order-add-product";
    }

    @PostMapping("/orders/{id}/add/product")
    public String addProductToOrderPost(@AuthenticationPrincipal User user,@PathVariable(value = "id") long id, @RequestParam long product_id, @RequestParam int quantity, Model model) {
        Order order = orderRepository.getAllById(id);
        Product product = productRepository.getAllById(product_id);
        Storage storage = storageRepository.getAllByProductId(product_id);
        if (storage.getQuantity() < quantity) {
            String message = "Не хватает количество товара вы требуете " + quantity + " товара но на складе доступно только " + storage.getQuantity();
            model.addAttribute("message", message);
            return "redirect:/orders/" + id + "/add/product";
        }
        OrderItem orderItem = new OrderItem(quantity, order, product,user, (int) orderService.getLastPrice(product));
        order.addOrderItem(orderItem);
        storage.setQuantity(storage.getQuantity() - quantity);
        RecievingHistory recievingHistory = new RecievingHistory(orderItem);
        storageRepository.save(storage);
        orderRepository.save(order);
        recievingHistoryRepository.save(recievingHistory);
        return "redirect:/orders/" + id;
    }

    @PostMapping("/orders/{id}/add")
    public String addService(@AuthenticationPrincipal User user,@PathVariable(value = "id") long id, @RequestParam long service_id, @RequestParam int quantity, Model model) throws ClassNotFoundException {
        Order order = orderRepository.findById(id).orElseThrow(() -> new ClassNotFoundException());
        Service service = serviceRepository.findById(service_id).orElseThrow(() -> new ClassNotFoundException());
        order.addOrderItem(new OrderItem(quantity, order, service,user));
        orderRepository.save(order);
        return "redirect:/orders/" + order.getId();
    }

    @GetMapping("/orders/{id}/done")
    public String orderDone(@AuthenticationPrincipal User user, @PathVariable(value = "id") long id, Model model) {
        Order order = orderRepository.getAllById(id);
        order.setStatus(Status.DONE);
        order.setDoneUser(user);
        order.setDonedate(new java.util.Date());
        orderRepository.save(order);
        return "redirect:/orders/" + id;
    }

    @GetMapping("/orders/{id}/comment")
    public String orderCommentAdd(@PathVariable(value = "id") long id, Model model) {
        Order order = orderRepository.getAllById(id);
        model.addAttribute("order",order);
        return "comment-update";
    }
    @GetMapping("/orders/{id}/comment/update")
    public String orderCommentUpdate(@PathVariable(value = "id") long id, Model model) {
        Order order = orderRepository.getAllById(id);
        model.addAttribute("order",order);
        return "comment-update";
    }

    @PostMapping("/orders/{id}/comment/update")
    public String orderCommentUpdatePost(@PathVariable(value = "id") long id,@RequestParam String comment, Model model) {
        Order order = orderRepository.getAllById(id);
        order.setComment(comment);
        orderRepository.save(order);
        return "redirect:/orders/"+id;
    }

    @GetMapping("/orders/{id}/notify")
    public String notify(@AuthenticationPrincipal User user, @PathVariable(value = "id") long id, Model model) {
        Order order = orderRepository.getAllById(id);
        String message = "Ваш заказ №" + order.getId() + " готов \n" +
                "Кто сделал: " + order.getDoneUser().getFname() + " " + order.getDoneUser().getLname() + "\n" +
                "Тел: " + order.getDoneUser().getPhoneNumber() + "\n" +
                "Цена: " + order.getPrice() + "\n" +
                "С уважением команда WEBPORTAL";
        smsc.send_sms(order.getNumber(), message, 1, "", "", 0, "", "");
        order.setNotified(true);
        orderRepository.save(order);
        return "redirect:/orders/" + id;
    }

    @GetMapping("/orders/{id}/cashier")
    public String orderGiven(@AuthenticationPrincipal User user, @PathVariable(value = "id") long id, Model model) throws ClassNotFoundException {
        Order order = orderRepository.findById(id).orElseThrow(() -> new ClassNotFoundException());
        /*order.setGavedate(new Date());*/
        order.setStatus(Status.WENTCASHIER);
        order.setGiveUser(user);
        orderRepository.save(order);
        return "redirect:/orders/" + id;
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/orders/{id}/remove")
    public String orderRemove(@PathVariable(value = "id") long id, Model model) throws ClassNotFoundException {
        orderRepository.deleteById(id);
        return "redirect:/orders";
    }

    @GetMapping("/orders/{id}/remove/{item_id}")
    public String orderItemsRemove(@PathVariable(value = "id") long id, @PathVariable(value = "item_id") long item_id, Model model) throws ClassNotFoundException {
        Order order = orderRepository.findById(id).orElseThrow(() -> new ClassNotFoundException());
        OrderItem orderItem = order.getOrderItemById(item_id);
        if (orderItem.getService() != null) {
            order.removeOrderItemById(item_id);
            orderRepository.save(order);
        } else {
            order.removeOrderItemById(item_id);
            RecievingHistory recievingHistory = recievingHistoryRepository.getAllByOrderItemId(orderItem.getId());
            recievingHistoryRepository.deleteById(recievingHistory.getId());
            Storage storage = storageRepository.getAllByProductId(orderItem.getProduct().getId());
            storage.setQuantity(storage.getQuantity() + orderItem.getQuantity());
            storageRepository.save(storage);
            orderRepository.save(order);
        }
        return "redirect:/orders/" + order.getId();
    }

    @GetMapping("/orders/{id}/QRCode")
    public String getOrderQRCode(@PathVariable(value = "id") long id, Model model) {
        Order order = orderRepository.getAllById(id);
        model.addAttribute("order", order);
        return "order-qr";
    }

    @GetMapping("/orders/search/notdone")
    public String getNotDoneOrders(Model model) {
        Iterable<Order> orders = orderRepository.findAllByStatus(Status.NOTDONE);
        model.addAttribute("orders", orders);
        return "orders";
    }

    @GetMapping("/orders/search/done")
    public String getDoneOrders(Model model) {
        Iterable<Order> orders = orderRepository.findAllByStatus(Status.DONE);
        model.addAttribute("orders", orders);
        return "orders";
    }

    @GetMapping("/orders/search/given")
    public String getGivenOrders(Model model) {
        Iterable<Order> orders = orderRepository.findAllByStatus(Status.GIVEN);
        model.addAttribute("orders", orders);
        return "orders";
    }

    @GetMapping("/order-admin/page/{pageNo}")
    public String findPaginated(@PathVariable(value = "pageNo") int pageNo, @RequestParam("sortField") String sortField,
                                @RequestParam("sortDir") String sortDir, Model model) {
        int pageSize = 5;
        Page<Order> page = orderService.findPaginated(pageNo, pageSize, sortField, sortDir);
        List<Order> orderList = page.getContent();
        model.addAttribute("currentPage", pageNo);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("totalItems", page.getTotalElements());
        model.addAttribute("orderList", orderList);
        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("reverseSortDir", sortDir.equals("asc") ? "desc" : "asc");
        return "order-admin";
    }

    @GetMapping("/order-admin")
    public String viewOrderNew(Model model) {
        return findPaginated(1, "id", "desc", model);
    }

    @PostMapping("/orders/findByDate")
    public String getOrdersByDate(@DateTimeFormat(pattern = "yyyy-MM-dd") @RequestParam("date1") Date date1,@DateTimeFormat(pattern = "yyyy-MM-dd") @RequestParam("date2") Date date2, Model model){
        Calendar cal = Calendar.getInstance();
        cal.setTime(date2);
        cal.add(Calendar.DATE ,1);
        date2 = cal.getTime();
        Iterable<Order> ordersAcceptDate = orderRepository.findAllByAccepteddateBetween(date1,date2);
        Iterable<Order> ordersGavenDate = orderRepository.findAllByGavedateBetween(date1,date2);
        model.addAttribute("ordersAccepted",ordersAcceptDate);
        model.addAttribute("ordersGaven",ordersGavenDate);
        model.addAttribute("orderService",orderService);
        return "report";
    }

    @GetMapping("/orders/findByDate")
    public String getOrdersByDateGet(@DateTimeFormat(pattern = "yyyy-MM-dd") @RequestParam("date1") Date date1,@DateTimeFormat(pattern = "yyyy-MM-dd") @RequestParam("date2") Date date2,@RequestParam String type, Model model){
        Calendar cal = Calendar.getInstance();
        cal.setTime(date2);
        cal.add(Calendar.DATE ,1);
        date2 = cal.getTime();
        if(type.equals("accepted")){
            Iterable<Order> ordersAcceptDate = orderRepository.findAllByAccepteddateBetween(date1,date2);
            model.addAttribute("ordersAccepted",ordersAcceptDate);
        }
        else if(type.equals("given")){
            Iterable<Order> ordersGavenDate = orderRepository.findAllByGavedateBetween(date1,date2);
            model.addAttribute("ordersGaven",ordersGavenDate);
        }
        else if(type.equals("done")){
            Iterable<Order> ordersDoneDate = orderRepository.findAllByDonedateBetween(date1,date2);
            model.addAttribute("ordersDone",ordersDoneDate);
        }
        model.addAttribute("orderService",orderService);
        return "report";
    }

    @PostMapping("/orders/findByDate-user")
    public String getOrdersByDateAndUsers(@DateTimeFormat(pattern = "yyyy-MM-dd") @RequestParam("date1") Date date1,@DateTimeFormat(pattern = "yyyy-MM-dd") @RequestParam("date2") Date date2,@RequestParam Long userid, Model model){
        User user = userRepository.getById(userid);
        Calendar cal = Calendar.getInstance();
        cal.setTime(date2);
        cal.add(Calendar.DATE ,1);
        date2 = cal.getTime();
        Iterable<Order> orders = orderRepository.findAllByGavedateBetween(date1,date2);
        model.addAttribute("orders",orders);
        model.addAttribute("user",user);
        model.addAttribute("orderService",orderService);
        return "report";
    }

    @GetMapping("/orders/findByDate-user")
    public String getOrdersByDateAndUsersGet(@DateTimeFormat(pattern = "yyyy-MM-dd") @RequestParam("date1") Date date1,@DateTimeFormat(pattern = "yyyy-MM-dd") @RequestParam("date2") Date date2,@RequestParam Long userid, Model model){
        User user = userRepository.getById(userid);
        Calendar cal = Calendar.getInstance();
        cal.setTime(date2);
        cal.add(Calendar.DATE ,1);
        date2 = cal.getTime();
        Iterable<Order> orders = orderRepository.findAllByGavedateBetween(date1,date2);
        model.addAttribute("orders",orders);
        model.addAttribute("user",user);
        model.addAttribute("orderService",orderService);
        return "report";
    }

    @GetMapping("/orders/{id}/payment/{type}")
    public String payment(@PathVariable(value = "id") Long id, @PathVariable(value = "type") String type,Model model){
        Order order = orderRepository.getAllById(id);
        TypesOfPayments typesOfPayments=TypesOfPayments.cash;
        switch (type){
            case "cash":
                typesOfPayments = TypesOfPayments.cash;
                break;
            case "online":
                typesOfPayments = TypesOfPayments.online;
                break;
            case "contract":
                typesOfPayments = TypesOfPayments.contract;
                break;
        }
        order.setTypesOfPayments(typesOfPayments);
        order.setStatus(Status.GIVEN);
        order.setGavedate(new Date());
        orderRepository.save(order);
        return "redirect:/cashier";
    }

}