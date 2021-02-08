package com.example.demo.repo;

import com.example.demo.models.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;

public interface OrderRepository extends JpaRepository<Order,Long> {
    Iterable<Order> findByOrderByIdDesc();
    Order getAllById(Long id);
    Iterable<Order> findAllByStatus(Status status);
    List<Order> findAll();
    Iterable<Order> findAllByAccepteddateBetween(Date date1,Date date2);
    Iterable<Order> findAllByGavedateBetween(Date date1,Date date2);
    Iterable<Order> findAllByDonedateBetween(Date date1,Date date2);
    Iterable<Order> findAllByClient(Client client);
    Iterable<Order> findAllByTypesOfPayments(TypesOfPayments paymentType);
    /*Iterable<Order> findAllByDoneUserAndGavedateBetween(User user,Date date1,Date date2);*/
    /*Iterable<Order> getAllByAccepted_dateBetween(Date date1,Date date2);*/
}
