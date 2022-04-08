package hellojpa;

import java.util.Objects;

public class Address {

    private String city;
    private String street;
    private String zipcode;

    public Address() {
    }

    public Address(String city, String street, String zipcode) {
        this.city = city;
        this.street = street;
        this.zipcode = zipcode;
    }

    public String getCity() {
        return city;
    }

    public String getStreet() {
        return street;
    }

    public String getZipcode() {
        return zipcode;
    }

//    @Override
//    public boolean equals(Object o) {
//        if (this == o) return true;
//        if (!(o instanceof Address)) return false;
//        Address address = (Address) o;
//        return Objects.equals(city, address.city) && Objects.equals(street, address.street) && Objects.equals(zipcode, address.zipcode);
//    }
//
//    // equals 구현 시 hashCode도 같이 구현해줘야 hashCode 를 사용하는 hashMap 등에서 사용할 수 있다
//    @Override
//    public int hashCode() {
//        return Objects.hash(city, street, zipcode);
//    }
}
