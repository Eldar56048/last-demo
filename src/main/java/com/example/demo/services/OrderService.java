package com.example.demo.services;

import com.example.demo.models.IncomingHistory;
import com.example.demo.models.Order;
import com.example.demo.models.OrderItem;
import com.example.demo.models.Product;
import com.example.demo.repo.IncomingHistoryRepository;
import com.example.demo.repo.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;


@Service
public class OrderService {
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private IncomingHistoryRepository incomingHistoryRepository;

    public Page<Order> findPaginated(int pageNo,int pageSize,String sortField,String sortDirection){
        Sort sort = sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortField).ascending() : Sort.by(sortField).descending();
        Pageable pageable = PageRequest.of(pageNo - 1,pageSize,sort);
        return this.orderRepository.findAll(pageable);
    }

    public float getLastPrice(Product product) {
        Iterable<IncomingHistory> incomingHistories = incomingHistoryRepository.getFirstByProductIdOrderByDateDesc(product.getId());
        for (IncomingHistory incomingHistory : incomingHistories) {
            return (float) incomingHistory.getPrice();
        }
        return 0;
    }

    public float getBossProportion(ArrayList<OrderItem> items){
        float bossProportion = 0;
        float coefficent = 0;
        float servicePercentage = 0;
        float price = 0;
        for(OrderItem orderItem : items){
            if(orderItem.getService()!=null){
                coefficent = ((float)1.0-(orderItem.getUserExperience().getCoefficient())/(float)100);
                servicePercentage = ((float)1.0-(orderItem.getServicePercentage())/(float)(100));
                if(orderItem.getOrder().getClient()!=null){
                    price = ((float)orderItem.getSoldPrice()*orderItem.getQuantity())*((float)(100-orderItem.getOrder().getDiscountPercent())/(float)100);
                }
                else{
                    price = ((float)orderItem.getSoldPrice()*orderItem.getQuantity());
                }
                if(coefficent!=0) {
                        bossProportion += (price) - (price * (float) (orderItem.getServicePercentage() / (float) 100) * (float) (orderItem.getUserExperience().getCoefficient() / (float) 100));
                }
                else{
                    bossProportion += ((float) price) * servicePercentage;
                }
            }
            else if(orderItem.getProduct()!=null){
                bossProportion += ((float)orderItem.getSoldPrice()-(float)orderItem.getLast_price())*orderItem.getQuantity();
            }
        }
        return bossProportion;
    }

    public  boolean  onlyDigits(String str, int n)
    {
        for (int i = 0; i < n; i++) {
            if (Character.isDigit(str.charAt(i))) {
                return true;
            }
            else {
                return false;
            }
        }
        return false;
    }

}
