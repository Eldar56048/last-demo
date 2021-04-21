package com.example.demo.models;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import javax.persistence.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(generator = "sequence-order-generator")
    @GenericGenerator(
            name = "sequence-order-generator",
            strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
            parameters = {
                    @Parameter(name = "sequence_name", value = "order_sequence"),
                    @Parameter(name = "initial_value", value = "100"),
                    @Parameter(name = "increment_size", value = "1")
            }
    )
    private Long id;
    @Column(name = "name")
    private String name;
    @Column(name = "number")
    private String number;
    private String problem;
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> items;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "device_type")
    private Type type;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "model_type")
    private Model model;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "accepted_user_id")
    private User acceptedUser;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "done_user_id")
    private User doneUser;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "give_user_id")
    private User giveUser;
    @Column(name = "accepted_date")
    private Date accepteddate;
    @Column(name = "gave_date")
    private Date gavedate;
    @Column(name = "done_date")
    private Date donedate;
    private int price;
    private Status status;
    private com.example.demo.models.TypesOfPayments typesOfPayments;
    private Boolean notified;
    private String comment;
    private String modelCompany;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "clientId")
    private Client client;
    private String discountName;
    private int discountPercent;
    public Order(){}
    public Order(String name, String number, String problem, User acceptedUser, Type type, Model model,String modelCompany) {
        this.name = name;
        this.number = number;
        this.problem = problem;
        this.acceptedUser = acceptedUser;
        this.accepteddate = new Date();
        this.status = Status.NOTDONE;
        this.type = type;
        this.model = model;
        this.modelCompany = modelCompany;
    }

    public Order(String name, String number, String problem, User acceptedUser, Type type, Model model,String modelCompany,String discountName,int discountPercent ) {
        this.name = name;
        this.number = number;
        this.problem = problem;
        this.acceptedUser = acceptedUser;
        this.accepteddate = new Date();
        this.status = Status.NOTDONE;
        this.type = type;
        this.model = model;
        this.modelCompany = modelCompany;
        this.discountName = discountName;
        this.discountPercent = discountPercent;
    }

    public Order(Client client, String problem, User acceptedUser, Type type, Model model, String modelCompany) {
        this.problem = problem;
        this.acceptedUser = acceptedUser;
        this.accepteddate = new Date();
        this.status = Status.NOTDONE;
        this.type = type;
        this.model = model;
        this.modelCompany = modelCompany;
        this.discountName = client.getDiscount().getDiscountName();
        this.discountPercent = client.getDiscount().getPercentage();
        this.client = client;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public String getDiscountName() {
        return discountName;
    }

    public void setDiscountName(String discountName) {
        this.discountName = discountName;
    }

    public int getDiscountPercent() {
        return discountPercent;
    }

    public void setDiscountPercent(int discountPercent) {
        this.discountPercent = discountPercent;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public List<OrderItem> getItems() {
        return items;
    }

    public void setItems(List<OrderItem> items) {
        this.items = items;
    }
    public void addOrderItem(OrderItem orderItem){
        this.items.add(orderItem);
        if(orderItem.getProduct()!=null) {
            this.price += orderItem.getQuantity() * orderItem.getProduct().getPrice();
        }
        else {
            this.price+=orderItem.getQuantity()*orderItem.getService().getPrice();
        }
    }
    public OrderItem getOrderItemById(long id){
        for(OrderItem orderItem:items){
            if(orderItem.getId()==id){
                return orderItem;
            }
        }
        return null;
    }
    public void removeOrderItemById(long id){
        for(OrderItem orderItem:items){
            if(orderItem.getId()==id){
                if(orderItem.getProduct()!=null) {
                    this.price -= orderItem.getQuantity() * orderItem.getProduct().getPrice();
                    items.remove(orderItem);
                }
                else {
                    this.price-=orderItem.getQuantity()*orderItem.getService().getPrice();
                    items.remove(orderItem);
                }
                break;
            }
        }
    }
    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public User getAcceptedUser() {
        return acceptedUser;
    }

    public void setAcceptedUser(User acceptedUser) {
        this.acceptedUser = acceptedUser;
    }

    public User getDoneUser() {
        return doneUser;
    }

    public void setDoneUser(User doneUser) {
        this.doneUser = doneUser;
    }

    public User getGiveUser() {
        return giveUser;
    }

    public void setGiveUser(User giveUser) {
        this.giveUser = giveUser;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String client_name) {
        this.name = client_name;
    }

    public String getNumber() {
        return number;
    }

    public Model getModel() {
        return model;
    }

    public void setModel(Model model) {
        this.model = model;
    }

    public void setNumber(String client_number) {
        this.number = client_number;
    }

    public String getProblem() {
        return problem;
    }
    public String getDateByFormat(){
        SimpleDateFormat ft = new SimpleDateFormat ("E, dd MMM yyyy HH:mm:ss");
        return ft.format(this.accepteddate);
    }
    public String getAcceptedDateByFormat(){
        SimpleDateFormat ft = new SimpleDateFormat ("E, dd MMM yyyy HH:mm:ss");
        return ft.format(this.accepteddate);
    }
    public String getGaveDateByFormat(){
        SimpleDateFormat ft = new SimpleDateFormat ("E, dd MMM yyyy HH:mm:ss");
        return ft.format(this.gavedate);
    }
    public String getDoneDateByFormat(){
        SimpleDateFormat ft = new SimpleDateFormat ("E, dd MMM yyyy HH:mm:ss");
        return ft.format(this.donedate);
    }
    public void setProblem(String problem) {
        this.problem = problem;
    }

    public Date getAccepteddate() {
        return accepteddate;
    }

    public void setAccepteddate(Date accepted_date) {
        this.accepteddate = accepted_date;
    }

    public Date getGavedate() {
        return gavedate;
    }

    public void setGavedate(Date gave_date) {
        this.gavedate = gave_date;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public Boolean getNotified() {
        return notified;
    }

    public void setNotified(Boolean notified) {
        this.notified = notified;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public float getMastersProportion(){
        float masterProportion = 0;
        float coefficent = 0;
        float servicePercentage = 0;
        float price = 0;
        for(OrderItem orderItem : items){
            if(orderItem.getService()!=null){
                if(client!=null){
                    price = ((float)orderItem.getSoldPrice()*orderItem.getQuantity())*((float)(100-discountPercent)/(float)100);
                }
                else {
                    price = ((float)orderItem.getSoldPrice()*orderItem.getQuantity());
                }
                coefficent = ((float)(orderItem.getUserExperience().getCoefficient())/(float)100);
                servicePercentage = ((float)(orderItem.getServicePercentage())/(float)(100));
                masterProportion += ((float)price)*coefficent*servicePercentage;
            }
        }
        return masterProportion;
    }

    public float getMasterIdProportion(Long id){
        float masterProportion = 0;
        float coefficent = 0;
        float servicePercentage = 0;
        float price = 0;
        for(OrderItem orderItem : items){
            if(orderItem.getService()!=null&&orderItem.getDoneUser().getId()==id){
                if(client!=null){
                    price = ((float)orderItem.getSoldPrice()*orderItem.getQuantity())*((float)(100-discountPercent)/(float)100);
                }
                else {
                    price = ((float)orderItem.getSoldPrice()*orderItem.getQuantity());
                }
                coefficent = ((float)(orderItem.getUserExperience().getCoefficient())/(float)100);
                servicePercentage = ((float)(orderItem.getServicePercentage())/(float)(100));
                masterProportion += ((float)price)*coefficent*servicePercentage;
            }
        }
        return masterProportion;
    }


    public boolean masterDoneWorkInOrder(Long id){
        for(OrderItem orderItem:items){
            if(orderItem.getDoneUser().getId()==id){
                return true;
            }
        }
        return false;
    }

    public com.example.demo.models.TypesOfPayments getTypesOfPayments() {
        return typesOfPayments;
    }

    public void setTypesOfPayments(com.example.demo.models.TypesOfPayments typesOfPayments) {
        this.typesOfPayments = typesOfPayments;
    }

    public Date getDonedate() {
        return donedate;
    }

    public void setDonedate(Date donedate) {
        this.donedate = donedate;
    }

    public String getModelCompany() {
        return modelCompany;
    }

    public double getPriceWithDiscount(){
        return (double)price*((double)(100-discountPercent)/100.0);
    }

    public void setModelCompany(String modelType) {
        this.modelCompany = modelType;
    }

    public double getSumWithDiscount(){
        double sum =0;
        for(OrderItem orderItem:items){
            if(orderItem.getService()!=null){
                sum+=((double)orderItem.getSoldPrice()*orderItem.getQuantity())*((double)(100-discountPercent)/(double) 100);
            }
            else {
                sum+=orderItem.getSoldPrice()*orderItem.getQuantity();
            }
        }
        return sum;
    }

    public void deleteProductFromItems(com.example.demo.models.Product product){
        for(OrderItem orderItem:items){
            if(orderItem.getProduct()!=null&&orderItem.getProduct().getId()==product.getId()){
                items.remove(orderItem);
            }
        }
    }
}
