package com.example.demo.utilities;

import java.util.*;

import com.example.demo.models.*;

public class AllDataAllData {
    public static List<Customer> customers = new ArrayList<>();

    static {
        customers.add(new Customer(1, "John Doe"));
        customers.add(new Customer(2, "Jane Doe"));
        customers.add(new Customer(3, "Alice Johnson"));
    }
}
