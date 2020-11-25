package com.example.demo.controllers;

import com.example.demo.models.*;
import com.example.demo.repo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Optional;

@Controller
@RequestMapping("/products")
public class ProductController {
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private StorageRepository storageRepository;
    @Autowired
    private ProviderRepository providerRepository;
    @Autowired
    private IncomingHistoryRepository incomingHistoryRepository;
    @Autowired
    private RecievingHistoryRepository recievingHistoryRepository;
    @GetMapping
    public String getProductsPage(Model model){
        Iterable<Product> products = productRepository.findAll();
        model.addAttribute("products",products);
        return "products";
    }

    @PostMapping("/add")
    public String addProduct(Product product, Model model){
        Storage storage = new Storage(product,0);
        productRepository.save(product);
        storageRepository.save(storage);
        return "redirect:/products";
    }

    @GetMapping("/{id}")
    public String aboutProduct(@PathVariable(value = "id")long id, Model model){
        Optional<Product> product = productRepository.findById(id);
        ArrayList<Product> result = new ArrayList<>();
        product.ifPresent(result::add);
        model.addAttribute("product",result);
        Storage storage = storageRepository.getAllByProductId(id);
        model.addAttribute("storage",storage);
        return "product-info";
    }
    @GetMapping("/{id}/add")
    public String getProductForAddingToStorage(@PathVariable(value = "id")long id,Model model) throws ClassNotFoundException {
        Product product = productRepository.findById(id).orElseThrow(()-> new ClassNotFoundException());
        model.addAttribute("product",product);
        Iterable<Provider> providers = providerRepository.findAll();
        model.addAttribute("providers",providers);
        return "product-add-storage";
    }
    @PostMapping("/{id}/add")
    public String addProductToStorage(@PathVariable(value = "id")long id,@RequestParam long provider_id,@RequestParam int price,@RequestParam int quantity,Model model){
        Provider provider = providerRepository.getAllById(provider_id);
        Product product = productRepository.getAllById(id);
        IncomingHistory incomingHistory = new IncomingHistory(product,provider,quantity,price);
        incomingHistoryRepository.save(incomingHistory);
        Storage storage = storageRepository.getAllByProductId(id);
        storage.setQuantity(storage.getQuantity()+quantity);
        storageRepository.save(storage);
        return "redirect:/products/"+id;
    }
    @GetMapping("/{id}/remove")
    public String deleteProduct(@PathVariable(value = "id")long id,Model model) throws ClassNotFoundException {
        Product product = productRepository.findById(id).orElseThrow(()-> new ClassNotFoundException());
        productRepository.delete(product);
        return "redirect:/products";
    }
    @GetMapping("/{id}/update")
    public String getProductForUpdating(@PathVariable(value = "id")long id,Model model){
        Optional<Product> product = productRepository.findById(id);
        ArrayList<Product> result = new ArrayList<>();
        product.ifPresent(result::add);
        model.addAttribute("product",result);
        return "product-update";
    }
    @GetMapping("/{id}/history/incoming")
    public String getProductHistoryIncoming(@PathVariable(value = "id")long id,Model model){
        Iterable<IncomingHistory> incomingHistories = incomingHistoryRepository.getAllByProductId(id);
        model.addAttribute("incoming",incomingHistories);
        return "product-history";
    }
    @GetMapping("/{id}/history/recieving")
    public String getProductHistoryRecieving(@PathVariable(value = "id")long id,Model model){
        Iterable<RecievingHistory> recievingHistories = recievingHistoryRepository.getAllByOrderItemProductId(id);
        model.addAttribute("recieving",recievingHistories);
        return "product-history";
    }
    @PostMapping("/{id}/update")
    public String updateProvider(@PathVariable(value = "id") long id,@RequestParam String name,@RequestParam String description,@RequestParam int price, Model model) throws ClassNotFoundException {
        Product product = productRepository.findById(id).orElseThrow(()-> new ClassNotFoundException());
        product.setProduct_name(name);
        product.setDescription(description);
        product.setPrice(price);
        productRepository.save(product);
        return "redirect:/products/"+product.getId();
    }
}
