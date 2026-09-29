package com.example.demo.utilities;

import java.util.*;

import com.example.demo.models.*;

public class AccountData {
    public static List<Account> accounts = new ArrayList<>();

    static {
        accounts.add(new Account(1, "SAVINGS"));
        accounts.add(new Account(2, "CHECKING"));
        accounts.add(new Account(3, "SAVINGS"));
    }
}
