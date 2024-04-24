package codrea.model;

public class Order {
    private String id;
    private String productName;
    private String productPrice;
    private String date;
    private String address;
    private String orderedBy;

    public Order(String id, String productName, String productPrice, String date, String address, String orderedBy) {
        this.id = id;
        this.productName = productName;
        this.productPrice = productPrice;
        this.date = date;
        this.address = address;
        this.orderedBy = orderedBy;
    }

    // I need this for orders page
    public Order(String orderId, String productName, String productPrice, String date) {
        this.id = orderId;
        this.productName = productName;
        this.productPrice = productPrice;
        this.date = date;
    }


    // I need this for Excel
    public Order(String orderId,String productName,String productPrice,String address,String orderedBy){
        this.id = orderId;
        this.productName = productName;
        this.productPrice = productPrice;
        this.address = address;
        this.orderedBy = orderedBy;

    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(String productPrice) {
        this.productPrice = productPrice;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getOrderedBy() {
        return orderedBy;
    }

    public void setOrderedBy(String orderedBy) {
        this.orderedBy = orderedBy;
    }

    public boolean equals(Order otherOrder, boolean compareAddress) {
        if (otherOrder == this) {
            return true;
        }

        if (otherOrder == null) {
            return false;
        }
        if (compareAddress){
            return id.equals(otherOrder.getId()) &&
                    productName.equals(otherOrder.getProductName()) &&
                    productPrice.equals(otherOrder.getProductPrice()) &&
                    orderedBy.equals(otherOrder.getOrderedBy()) &&
                    address.equals(otherOrder.getAddress());
        }
        else {
            return id.equals(otherOrder.getId()) &&
                    productName.equals(otherOrder.getProductName()) &&
                    productPrice.equals(otherOrder.getProductPrice()) &&
                    orderedBy.equals(otherOrder.getOrderedBy()) &&
                    date.equals(otherOrder.getDate());
        }


    }

    @Override
    public String toString() {
        return "id='" + id +
                ", productName='" + productName +
                ", productPrice='" + productPrice +
                ", address='" + address +
                ", orderedBy='" + orderedBy;
    }
}

