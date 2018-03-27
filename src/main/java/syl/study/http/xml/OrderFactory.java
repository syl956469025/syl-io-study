package syl.study.http.xml;

/**
 * @author 史彦磊
 * @create 2018-03-26 16:27.
 */
public class OrderFactory {



    public static Order create(long orderID){
        Order order = new Order();
        order.setOrderNumber(orderID);
        order.setTotal(9999.999f);
        Address address = new Address();
        address.setCity("北京市");
        address.setCountry("中国");
        address.setPortCode("123321");
        address.setState("北京市");
        address.setStreet1("大望路");
        order.setBillTo(address);
        Customer customer = new Customer();
        customer.setFirstName("史");
        customer.setLastName("彦磊");
        order.setCustomer(customer);
        order.setShipping(Shipping.INTERNATIONAL_MAIL);
        order.setShipTo(address);

        return order;
    }
}
